package com.sss.ka.service;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.sss.ka.vo.ToClientCode;
import com.sss.ka.vo.WebSocketClientMsg;
import com.sss.ka.vo.WebSocketLogicMsg;
import com.sss.ka.vo.WebSocketToClientMsg;
import com.sss.security.domain.UserDetailsImpl;
import com.sss.security.service.UserDetailServiceImpl;
import com.sss.security.util.AccountUtil;
import com.sss.security.util.JWTUtil;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocket服务
 * 虽然 @Component 默认是单例模式的，
 * 但springboot还是会为每个websocket连接初始化一个bean
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{userId}")  // 接口路径 ws://ip:port/ws/userId;
public class WebSocketService{

    // 全局静态变量，保存 ApplicationContext
    public static ApplicationContext applicationContext;

    @Resource // 需要在onOpen里手动注入
    private JWTUtil jwtUtil;
    @Resource
    private AccountUtil accountUtil;
    @Resource
    private UserDetailServiceImpl userDetailsService;
    @Resource
    private WebSocketLogicService webSocketLogicService;

    private Session session; //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private long userId; // userId
    private boolean valid; // 做权限校验了才可以发消息，不然1分钟后自动断开
    private LocalDateTime connectTime; // 当前建立连接的时间，用来判断1分钟内做权限校验没有

    // 用来存在线连接用户信息, List考虑一个用户可以连接多次 WebSocketService由服务器管理，不是由spring管理，不是单例模式，每个websocket连接创建一个实例
    private static final ConcurrentHashMap<Long, List<WebSocketService>> websocketServicePool = new ConcurrentHashMap<>();
    private static final AtomicInteger connectNum = new AtomicInteger(0);
    private static final LinkedBlockingDeque<WebSocketService> validQueue = new LinkedBlockingDeque<>(); // 阻塞队列，用来清除没有做权限校验的连接

    static {
        // 不断去清除没有做权限校验的连接
       new Thread(() -> {
           while(true){
               try{
                   WebSocketService first = validQueue.getFirst();
                   LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
                   if(first.connectTime.isBefore(oneMinuteAgo)){
                       if(!first.valid){
                           log.warn("websocket 超时关闭连接: {}", first.userId);
                           first.closeConnection();
                       }
                       validQueue.pollFirst(); // 移除元素
                   }
               } catch (NoSuchElementException ignored){

               }

               try {
                   Thread.sleep(10); // 10ms检查一次， 1分钟可处理100 * 60 = 6k个
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
       }).start();
    }

    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId") long userId) {
        log.info("websocket 建立连接: {}", userId);
        log.info("websocket 连接数量: {}", connectNum.incrementAndGet());

        this.session = session;
        this.userId = userId;
        this.valid = false;
        this.connectTime = LocalDateTime.now();

        websocketServicePool.putIfAbsent(userId, new ArrayList<>());
        websocketServicePool.get(userId).add(this);

        validQueue.offerLast(this);

        // 由服务器管理，不是由spring管理，需要手动注入bean 放到最后，避免springboot还未初始化
        this.jwtUtil = applicationContext.getBean(JWTUtil.class);
        this.accountUtil = applicationContext.getBean(AccountUtil.class);
        this.userDetailsService = applicationContext.getBean(UserDetailServiceImpl.class);
        this.webSocketLogicService = applicationContext.getBean(WebSocketLogicService.class);
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        websocketServicePool.get(userId).remove(this);
        log.info("websocket 断开连接: {}", userId);
        log.info("websocket 连接数量: {}", connectNum.decrementAndGet()); //
    }

    /**
     * 主动关闭连接
     */
    @SneakyThrows
    public void closeConnection() {
        // 断开连接
        log.info("websocket 服务端断开连接: {}", userId);
        session.close(); // 会执行onClose
        onClose();
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("websocket 客户端({})--消息-->服务端: {}", userId, message);

        // 解析成约定结构
        WebSocketClientMsg webSocketClientMsg;
        try {
            webSocketClientMsg = JSONUtil.toBean(message, WebSocketClientMsg.class);
        } catch (JSONException e){
            sendMessageToClient(WebSocketToClientMsg.builder()
                    .code(ToClientCode.FORMAT_ERROR)
                    .body(message).build()
            );
            closeConnection();
            return;
        }

        // 解析 token 获取权限
        String token = webSocketClientMsg.getAuthorization();
        String username = jwtUtil.getUserNameFromToken(token);
        if(username != null && accountUtil.notExpire(username, token)){
            UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
            // 验证 uid
            if(userId != userDetails.getUser().getId()){
                log.warn("websocket userId({}) != token.user.id({})", userId, userDetails.getUser().getId());
                sendMessageToClient(WebSocketToClientMsg.builder()
                        .code(ToClientCode.USERID_ERROR)
                        .body(message).build()
                );
                closeConnection();
                return;
            }
            valid = true; // 已经做了权限校验了

            List<String> permissions = userDetails.getPermissions();
            WebSocketLogicMsg logicMsg = WebSocketLogicMsg.builder()
                    .user(userDetails.getUser())
                    .permissions(permissions)
                    .path(webSocketClientMsg.getPath())
                    .body(webSocketClientMsg.getBody()).build();

            // 业务逻辑处理
            try {
                // 反射去调用 webSocketLogicService 里的 path 方法, 需要定义好
                webSocketLogicService.getClass()
                        .getDeclaredMethod(logicMsg.getPath(), WebSocketLogicMsg.class)
                        .invoke(webSocketLogicService, logicMsg);
            }  catch (Exception e){
                e.printStackTrace();
                sendMessageToClient(WebSocketToClientMsg.builder()
                        .code(ToClientCode.PATH_INVALID)
                        .body(message).build()
                );
            }
        } else {
            sendMessageToClient(WebSocketToClientMsg.builder()
                    .code(ToClientCode.TOKEN_INVALID)
                    .body(message).build()
            );
            closeConnection();
        }
    }

    /** 发送错误时的处理
     * @param session 出错会话
     * @param error 错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket error: {}; uid = {}", error.getMessage(), userId);
        error.printStackTrace();
    }

    /**
     * 发信息给 this.session
     * @param message 信息
     */
    public void sendMessageToClient(@NonNull WebSocketToClientMsg message){
        if(!valid){
            log.warn("websocket {} 因未做权限认证导致消息未发送: {};", userId, message.toString());
            return;
        }

        String jsonStr = JSONUtil.toJsonStr(message);
        if(session.isOpen()){
            session.getAsyncRemote().sendText(jsonStr);
        }
    }

    /**
     * 给uid发消息
     * @param message 消息
     * @param uid uid
     */
    public static void sendMessageToClient(@NonNull WebSocketToClientMsg message, long uid){
        List<WebSocketService> webSocketServiceList = websocketServicePool.get(uid);
        if(webSocketServiceList != null){
            for (WebSocketService webSocketService : webSocketServiceList) {
                webSocketService.sendMessageToClient(message);
            }
        }
    }

    /**
     * 广播消息
     */
    public static void broadcastMessageToClient(@NonNull WebSocketToClientMsg message){
        websocketServicePool.forEach((userId, webSocketServiceList) -> {
            for (WebSocketService webSocketService : webSocketServiceList) {
                webSocketService.sendMessageToClient(message);
            }
        });
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WebSocketService)) {
            return false;
        }
        WebSocketService that = (WebSocketService) o;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
