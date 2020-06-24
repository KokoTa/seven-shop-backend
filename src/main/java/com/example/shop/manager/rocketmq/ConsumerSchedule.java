package com.example.shop.manager.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsumerSchedule implements CommandLineRunner {

    @Value("${rocketmq.consumer.consumer-group}")
    private String consumerGroup;

    @Value(("${rocketmq.namesrv-addr}"))
    private String namesrvAddr;

    public void defaultMQConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.consumerGroup);
        consumer.setNamesrvAddr(namesrvAddr);

        consumer.subscribe("TopicTest", "*"); // 订阅某个主题的消息，* 表示监听所有消息
        consumer.setConsumeMessageBatchMaxSize(1); // 消费一条数据

        // 注册监听器
        consumer.registerMessageListener((MessageListenerConcurrently) (messages, context) -> {
            for (Message message: messages) System.out.println(new String(message.getBody()));
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        // 启动消费者
        consumer.start();
        System.out.println("-----Consumer Start-----");
    }

    @Override
    public void run(String... args) throws Exception {
        this.defaultMQConsumer();
    }
}
