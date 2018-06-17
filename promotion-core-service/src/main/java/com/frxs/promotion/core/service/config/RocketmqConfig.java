/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.config;

import com.frxs.framework.integration.rocketmq.adapter.RocketMqConsumerAdapter;
import com.frxs.framework.integration.rocketmq.adapter.RocketMqProducerAdapter;
import com.frxs.promotion.core.service.rocketmq.IndexDataObjectListener;
import java.util.HashMap;
import java.util.Map;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketmqConfig
 *
 * @author mingbo.tang
 * @version $Id: RocketmqConfig.java,v 0.1 2018年01月07日 下午 13:46 $Exp
 */
@Configuration
public class RocketmqConfig {

  @Bean("CONSUME_FROM_FIRST_OFFSET")
  public FieldRetrievingFactoryBean consumeFromFirstOffset() {
    FieldRetrievingFactoryBean fieldRetrievingFactoryBean = new FieldRetrievingFactoryBean();
    fieldRetrievingFactoryBean.setStaticField(
        "org.apache.rocketmq.common.consumer.ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET");
    return fieldRetrievingFactoryBean;
  }

  @Bean("CONSUME_FROM_LAST_OFFSET")
  public FieldRetrievingFactoryBean consumeFromLastOffset() {
    FieldRetrievingFactoryBean fieldRetrievingFactoryBean = new FieldRetrievingFactoryBean();
    fieldRetrievingFactoryBean.setStaticField(
        "org.apache.rocketmq.common.consumer.ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET");
    return fieldRetrievingFactoryBean;
  }

  @Bean("CONSUME_FROM_TIMESTAMP")
  public FieldRetrievingFactoryBean consumeFromTimestamp() {
    FieldRetrievingFactoryBean fieldRetrievingFactoryBean = new FieldRetrievingFactoryBean();
    fieldRetrievingFactoryBean.setStaticField(
        "org.apache.rocketmq.common.consumer.ConsumeFromWhere.CONSUME_FROM_TIMESTAMP");
    return fieldRetrievingFactoryBean;
  }

  /**
   * 配置消息生产者
   *
   * @return DefaultMQProducer
   */
  @Bean("promotionProducer")
  public DefaultMQProducer testDefaultMQProducer() {
    DefaultMQProducer producer = new DefaultMQProducer();
    producer.setProducerGroup(RocketmqConstant.PROMOTION_PRODUCER_GROUP);
    producer.setInstanceName(RocketmqConstant.PROMOTION_PRODUCER_INSTANCE);
    return producer;
  }

  /**
   * 配置消息生产者适配器
   *
   * @param promotionProducer 息生产者
   * @return RocketMqProducerAdapter
   */
  @Bean("rocketMqProducerAdapter")
  public RocketMqProducerAdapter rocketMqProducerAdapter(DefaultMQProducer promotionProducer) {
    RocketMqProducerAdapter rocketMqProducerAdapter = new RocketMqProducerAdapter();
    rocketMqProducerAdapter.getDefaultMQProducerMap().put(RocketmqConstant.PROMOTION_PRODUCER_INSTANCE, promotionProducer);
    return rocketMqProducerAdapter;
  }


  /**
   * 首页数据消费者
   *
   * @param indexDataObjectListener 广播模式Object消费者监听器
   * @return 测试消息消费者
   */
  @Bean("indexDataObjectConsumer")
  public DefaultMQPushConsumer indexDataObjectConsumer(IndexDataObjectListener indexDataObjectListener) {
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
    consumer.setConsumerGroup(RocketmqConstant.INDEX_DATA_CONSUMER_GROUP);
    consumer.setInstanceName(RocketmqConstant.INDEX_DATA_CONSUMER_INSTANCE);
    consumer.setMessageModel(MessageModel.BROADCASTING);
    Map<String, String> subscription = new HashMap<>();
    subscription.put(RocketmqConstant.INDEX_DATA_TOPIC, "*");
    consumer.setSubscription(subscription);
    consumer.setMessageListener(indexDataObjectListener);
    consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
    return consumer;
  }

  /**
   * 配置消息消费者适配器
   *
   * @param testStringConsumer 测试消息消费者
   * @return RocketMqConsumerAdapter
   */
  @Bean("rocketMqConsumerAdapter")
  public RocketMqConsumerAdapter rocketMqConsumerAdapter(DefaultMQPushConsumer testStringConsumer, DefaultMQPushConsumer testObjectConsumer) {
    RocketMqConsumerAdapter rocketMqConsumerAdapter = new RocketMqConsumerAdapter();
    rocketMqConsumerAdapter.getDefaultMQPushConsumerMap().put(RocketmqConstant.INDEX_DATA_CONSUMER_INSTANCE, testObjectConsumer);
    return rocketMqConsumerAdapter;
  }

}