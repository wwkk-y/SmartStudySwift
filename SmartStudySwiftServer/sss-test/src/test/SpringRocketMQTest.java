import com.sss.test.SssTestApplication;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = SssTestApplication.class)
public class SpringRocketMQTest {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Test
    void t1(){
        rocketMQTemplate.asyncSend("test-topic", "hello", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }
}
