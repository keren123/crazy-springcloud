package com.crazymaker.cloud.ha.middleware.controller;

import com.crazymaker.springcloud.common.exception.BusinessException;
import com.crazymaker.springcloud.common.result.RestOut;
import com.crazymaker.springcloud.common.util.JsonUtil;
import com.crazymaker.springcloud.seckill.api.dto.SeckillDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seckill/rockmq/")
@Api(tags = "Rockmq消息Demo")
@Slf4j
public class RockmqMessageController implements ApplicationContextAware {
    public static final String TOPIC_SECKILL = "seckill-topic-cluster";
    DefaultMQProducer producer = new DefaultMQProducer("seckill_producerGroup");

    @Value("${rocketmq.name-server}")
    private String rocketmqAddress;


    /**
     * Consumer Group,非常重要的概念，后续会慢慢补充
     */
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("seckill_consumer");

    {
        consumer.setMessageModel(MessageModel.CLUSTERING);
    }

    SendCallback sendCallback = new SendCallback() {

        @Override
        public void onSuccess(SendResult sendResult) {
            log.info("异步消息发送成功: {}", sendResult.toString());
        }

        @Override
        public void onException(Throwable e) {
            log.error("异步消息发送失败:", e);
            // 消息发送失败，可以持久化这条数据，后续进行补偿处理
        }
    };

    /**
     * 执行同步发送
     * <p>
     * <p>
     {
     "exposedKey": "4b70903f6e1aa87788d3ea962f8b2f0e",
     "newStockNum": 10000,
     "seckillSkuId": 1157197244718385152,
     "seckillToken": "0f8459cbae1748c7b14e4cea3d991000",
     "userId": 37
     }
     *
     * @return
     */
    @ApiOperation(value = "同步发送")
    @PostMapping("/syncSend/v1")
    RestOut<String> syncSend(@RequestBody SeckillDTO dto) {
        String content = JsonUtil.pojoToJson(dto);

        try {
            //构建消息
            Message msg = new Message(TOPIC_SECKILL /* Topic */,
                    "TagA" /* Tag */,
                    (content).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );

            //同步发送消息
            SendResult sendResult = producer.send(msg);
            System.out.printf("同步发送返回：%s%n", sendResult);

            log.info("同步发送完成 {}", sendResult.getMsgId());
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.builder().errMsg(e.getMessage()).build();
        }

        return RestOut.success("发送完成");
    }

    /**
     * 执行延迟发送
     * <p>
     * <p>
     {
     "exposedKey": "4b70903f6e1aa87788d3ea962f8b2f0e",
     "newStockNum": 10000,
     "seckillSkuId": 1157197244718385152,
     "seckillToken": "0f8459cbae1748c7b14e4cea3d991000",
     "userId": 37
     }
     * @return
     */
    @ApiOperation(value = "延迟发送")
    @PostMapping("/delaySend/v1")
    RestOut<String> delaySend(@RequestBody SeckillDTO dto) {
        String content = JsonUtil.pojoToJson(dto);

        try {
            //构建消息
            Message msg = new Message(TOPIC_SECKILL /* Topic */,
                    "TagA" /* Tag */,
                    (content).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );

            //设置消息延时级别  3对应10秒后发送
            msg.setDelayTimeLevel(3);
            //同步发送消息
            SendResult sendResult = producer.send(msg);
            System.out.printf("delaySend发送返回：%s%n", sendResult);

            log.info("delaySend发送完成 {}", sendResult.getMsgId());
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.builder().errMsg(e.getMessage()).build();
        }

        return RestOut.success("delaySend发送完成");
    }

    /**
     * 执行异步发送
     * <p>
     * <p>
     {
     "exposedKey": "4b70903f6e1aa87788d3ea962f8b2f0e",
     "newStockNum": 10000,
     "seckillSkuId": 1157197244718385152,
     "seckillToken": "0f8459cbae1748c7b14e4cea3d991000",
     "userId": 37
     }
     *
     * @return
     */
    @ApiOperation(value = "异步发送")
    @PostMapping("/asyncSend/v1")
    RestOut<String> asyncSend(@RequestBody SeckillDTO dto) {
        String content = JsonUtil.pojoToJson(dto);

        try {
            //构建消息
            Message msg = new Message(TOPIC_SECKILL /* Topic */,
                    "TagA" /* Tag */,
                    (content).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );

            //异步发送消息
            producer.send(msg, sendCallback);

            //同步发送消息
//            SendResult sendResult = producer.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.builder().errMsg(e.getMessage()).build();
        }

        return RestOut.success("发送完成");
    }

    /**
     * Send Messages in One-way Mode
     * <p>
     * <p>

      {
      "exposedKey": "4b70903f6e1aa87788d3ea962f8b2f0e",
      "newStockNum": 10000,
      "seckillSkuId": 1157197244718385152,
      "seckillToken": "0f8459cbae1748c7b14e4cea3d991000",
      "userId": 37
      }

     *
     * @return
     */
    @ApiOperation(value = "Send Messages in One-way Mode")
    @PostMapping("/oneWaySend/v1")
    RestOut<String> oneWaySend(@RequestBody SeckillDTO dto) {
        String content = JsonUtil.pojoToJson(dto);

        try {
            //构建消息
            Message msg = new Message(TOPIC_SECKILL /* Topic */,
                    "TagA" /* Tag */,
                    (content).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );


            //Call send message to deliver message to one of brokers.
            //发送消息
            producer.sendOneway(msg);

            log.info("oneWaySend 发送完成  ");
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.builder().errMsg(e.getMessage()).build();
        }

        return RestOut.success("发送完成");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        startProducer();
        startConsumer();
    }

    private void startProducer() {
        //指定NameServer地址
        producer.setNamesrvAddr(rocketmqAddress); //修改为自己的
        producer.setInstanceName("producer1");
        producer.setRetryTimesWhenSendFailed(3);
        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        try {
            producer.start();
            log.info("product start ...");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    private void startConsumer() {
        //指定NameServer地址，多个地址以 ; 隔开
        consumer.setNamesrvAddr(rocketmqAddress); //修改为自己的
//        consumer.setNamesrvAddr("192.168.116.115:9876;192.168.116.116:9876"); //修改为自己的

//        consumer.setMessageModel( MessageModel.BROADCASTING);

/**
 * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
 * 如果非第一次启动，那么按照上次消费的位置继续消费
 */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);


        //模拟并行消费
        consumer.setConsumeThreadMin(3);
        try {
            consumer.subscribe(TOPIC_SECKILL, "*");
        } catch (MQClientException e) {
            e.printStackTrace();
            return;
        }

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (int i = 0; i < msgs.size(); i++) {
                    MessageExt msg = msgs.get(i);
                    String content = new String(msg.getBody());
                    log.info("收到消息：{}", msg.getMsgId() + " " + msg.getTopic() + " " + msg.getTags() + " " + content);
                    SeckillDTO dto = JsonUtil.jsonToPojo(content, SeckillDTO.class);
                    try {
                        //消费者的业务代码
//                        redisSeckillServiceImpl.executeSeckill(dto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }
}
