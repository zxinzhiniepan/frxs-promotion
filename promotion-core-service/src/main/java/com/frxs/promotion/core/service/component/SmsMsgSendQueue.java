/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.component;

import com.frxs.promotion.service.api.dto.SmsMsgDto;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 短信消息队列
 *
 * @author sh
 * @version $Id: SmsSendQueue.java,v 0.1 2018年02月07日 15:15 $Exp
 */
public class SmsMsgSendQueue {

  /**
   * 队列大小
   */
  private static final int QUEUE_MAX_SIZE = 1000;

  private static BlockingQueue<SmsMsgDto> sendMsgBlockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);


  private SmsMsgSendQueue() {
  }


  private static class SingletonHolder {

    private static SmsMsgSendQueue sendMsgQueue = new SmsMsgSendQueue();

  }

  /**
   * 单例队列
   */
  public static SmsMsgSendQueue getSmsSendQueue() {
    return SingletonHolder.sendMsgQueue;
  }

  /**
   * 生产入队
   */
  public void produce(SmsMsgDto smsMsgDto) throws InterruptedException {
    sendMsgBlockingQueue.put(smsMsgDto);
  }

  /**
   * 消费出队
   */
  public SmsMsgDto consume() throws InterruptedException {
    return sendMsgBlockingQueue.take();
  }

  /**
   * 获取队列大小
   */
  public int size() {
    return sendMsgBlockingQueue.size();
  }

}
