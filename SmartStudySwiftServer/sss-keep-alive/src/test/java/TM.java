import cn.hutool.json.JSONUtil;
import com.sss.ka.vo.WebSocketClientMsg;
import com.sss.ka.vo.WebSocketLogicMsg;

public class TM {
    public static void main(String[] args) {
        String s = JSONUtil.toJsonStr(WebSocketClientMsg.builder().authorization("1").path("1").body("123").build());
        System.out.println(s);
    }
}
