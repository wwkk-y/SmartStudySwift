import com.sss.test.SssTestApplication;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest(classes = SssTestApplication.class)
public class RocketmqTest {
    final String NAMESRV_ADDR = "1.15.232.25:9876";
    final String PRODUCER_GROUP = "test-producer-group";
    final String CONSUMER_GROUP = "test-consumer-group";
    final String TOPIC = "test-topic";

    // 发消息
    @SneakyThrows
    @Test
    void producer(){
        DefaultMQProducer producer = new DefaultMQProducer("test-producer-group");
        producer.setNamesrvAddr(NAMESRV_ADDR);
        producer.start();
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("test-topic", "hello world!".getBytes());
            // while 解决消息丢失
            int times = 10;
            while(times-- > 0){
                SendResult send = producer.send(msg);
                if(SendStatus.SEND_OK == send.getSendStatus()){
                    break;
                }
            }
        }
        producer.shutdown();
    }

    @SneakyThrows
    @Test
    void asyncProducer(){
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        producer.setNamesrvAddr(NAMESRV_ADDR);
        producer.start();
        Message msg = new Message(TOPIC, "async: hello world!".getBytes());

        final int[] times = {10};
        producer.send(msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @SneakyThrows
            @Override
            public void onException(Throwable throwable) {
                if(times[0]-- > 0){
                    producer.send(msg, this);
                } else {
                    throw throwable;
                }
            }
        });
        System.in.read();
    }

    @SneakyThrows
    @Test
    void onewayProducer(){
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        producer.setNamesrvAddr(NAMESRV_ADDR);
        producer.start();
        Message msg = new Message(TOPIC, "async: hello world!".getBytes());
        producer.sendOneway(msg);
        producer.shutdown();
    }

    @Test
    @SneakyThrows
    void tagProducer(){
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        producer.setNamesrvAddr(NAMESRV_ADDR);
        producer.start();

        Message msg1 = new Message(TOPIC, "tag1", "I am tag1".getBytes());
        Message msg2 = new Message(TOPIC, "tag2", "I am tag2".getBytes());

        producer.sendOneway(msg1);
        producer.sendOneway(msg2);

        producer.shutdown();
    }

    @SneakyThrows
    @Test
    void tagConsumer(){
        DefaultMQPushConsumer consumer1 = new DefaultMQPushConsumer(CONSUMER_GROUP + "_1");
        consumer1.setNamesrvAddr(NAMESRV_ADDR);
        consumer1.subscribe(TOPIC, "tag1");
        MessageListenerConcurrently listener = new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                log.info(list.get(0).getTags());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        };
        consumer1.registerMessageListener(listener);
        consumer1.start();

        DefaultMQPushConsumer consumer2 = new DefaultMQPushConsumer(CONSUMER_GROUP + "_2");
        consumer2.setNamesrvAddr(NAMESRV_ADDR);
        consumer2.subscribe(TOPIC, "tag1 || tag2");
        consumer2.registerMessageListener(listener);
        consumer2.start();

        System.in.read();
    }

    @SneakyThrows
    @Test
    void consumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");
        consumer.setNamesrvAddr(NAMESRV_ADDR);
        // 订阅主题，*表示订阅这个主题中的所有消息
        consumer.subscribe("test-topic", "*");
        // 注册消息监听器(异步回调)
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> list,  // 消息对象
                    ConsumeConcurrentlyContext consumeConcurrentlyContext // 消息上下文
            ) {
                log.info(list.stream().map(Message::getBody).map(String::new).collect(Collectors.joining()));

                // 返回值 -> 是否签收这批消息
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read(); // 读取一个字符，等待消费
    }
}
