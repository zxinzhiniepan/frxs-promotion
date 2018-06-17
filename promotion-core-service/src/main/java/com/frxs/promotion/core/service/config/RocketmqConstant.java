/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.config;

/**
 * 配置常量
 *
 * @author mingbo.tang
 * @version $Id: RocketmqConstant.java,v 0.1 2018年1月7日 下午 14:28 $Exp
 */
public interface RocketmqConstant {
  //========================================================================//
  //                              PRODUCER_GROUP                                                                                            //
  //========================================================================//
  /**
   * 营销生产者
   * PROMOTION_PRODUCER_GROUP
   */
  String PROMOTION_PRODUCER_GROUP = "PROMOTION_PRODUCT_DATA_SYNCHRONIZE_PRODUCER_GROUP";

  //========================================================================//
  //                              PRODUCER_INSTANCE                                                                                            //
  //========================================================================//
  /**
   * 生产者实例
   * PROMOTION_PRODUCER_INSTANCE
   */
  String PROMOTION_PRODUCER_INSTANCE = "PROMOTION_PRODUCT_DATA_SYNCHRONIZE_PRODUCER_INSTANCE";

  //========================================================================//
  //                              CONSUMER_GROUP                                                                                            //
  //========================================================================//
  /**
   * 广播模式对象消费者组
   * INDEX_DATA_CONSUMER_GROUP
   */
  String INDEX_DATA_CONSUMER_GROUP = "PROMOTION_PRODUCT_DATA_SYNCHRONIZE_CONSUMER_GROUP";
  //========================================================================//
  //                              CONSUMER_INSTANCE                                                                                            //
  //========================================================================//
  /**
   * 广播模式对象消费者实例
   * INDEX_DATA_CONSUMER_INSTANCE
   */
  String INDEX_DATA_CONSUMER_INSTANCE = "PROMOTION_PRODUCT_DATA_SYNCHRONIZE_CONSUMER_INSTANCE";
  //========================================================================//
  //                              TOPIC                                                                                           //
  //========================================================================//
  /**
   * 首页数据TOPIC
   * INDEX_DATA_TOPIC
   */
  String INDEX_DATA_TOPIC = "PROMOTION_PRODUCT_DATA_SYNCHRONIZE_TOPIC";
}
