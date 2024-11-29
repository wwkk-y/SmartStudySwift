// websocket连接处理类
/*
 public class WebSocketToClientMsg {
    private int code; // 状态码
    private String path; // 路由
    private String body; // 具体消息
}
*/

import { kaBaseUrl } from "@/config/UrlConfig";
import { useTokenStore } from "@/stores/token";

let ws = null;
let msgListener = {}

// 建立连接
function connect() {
    // 已经建立连接了
    if(ws){
        return;
    }
    // 没有登录
    let tokenStore = useTokenStore();
    if(!tokenStore || !tokenStore.getUser().id){
        console.warn('未登录, 建立ws连接失败')
        return;
    }

    ws = new WebSocket(`${kaBaseUrl}/ws/${tokenStore.getUser().id}`);

    ws.onopen = () => {
        // 权限认证
        ws.send(JSON.stringify({
            authorization: tokenStore.token,
            path: 'connect',
            body: 'connect'
        }))
    };

    ws.onmessage = (event) => {
        let msg = JSON.parse(event.data);
        if(msg.code == 0){
            if(msgListener[msg.path]){
                msgListener[msg.path](msg.body);
            } else {
                console.warn('ws消息未处理', msg)
            }
        } else {
            // TODO 处理错误情况的code
            console.warn('ws消息未处理', msg)
        }
    };

    ws.onclose = () => {
        // close
        ws = null;
    };

    ws.onerror = (error) => {
        // error
    };
}

/**
 * 注册消息监听器
 * @param {String} path 
 * @param {Function} func
 */
function registerListener(path, func){
    msgListener[path] = func;
}

// 定时器不断尝试去建立连接
setInterval(() => connect(), 2000);

export default {
    registerListener
};