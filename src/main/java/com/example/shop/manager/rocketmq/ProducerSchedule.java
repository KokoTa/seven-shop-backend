package com.example.shop.manager.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class ProducerSchedule {

    private DefaultMQProducer producer;

    @Value("${rocketmq.producer.producer-group}")
    private String producerGroup;

    @Value(("${rocketmq.namesrv-addr}"))
    private String namesrvAddr;

    @PostConstruct
    public void defaultMQProducer() throws MQClientException {
        if (this.producer == null) {
            this.producer = new DefaultMQProducer(this.producerGroup);
            this.producer.setNamesrvAddr(this.namesrvAddr);
        }

        // 启动生产者
        this.producer.start();
        System.out.println("-----Producer Start-----");
    }

    public String send(String topic, String messageText) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = new Message(topic, messageText.getBytes());

        // messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        message.setDelayTimeLevel(2); // 5s

        SendResult sendResult = this.producer.send(message);
        System.out.println("getMsgId: " + sendResult.getMsgId());
        System.out.println("getSendStatus: " + sendResult.getSendStatus());
        return sendResult.getMsgId();
    }
}
