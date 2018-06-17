package com.frxs.promotion.core.service.component;

import com.frxs.promotion.service.api.dto.SmsDto;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author fygu
 * @version $Id: SmsSendQueue.java,v 0.1 2018年02月07日 15:15 $Exp
 */
public class SmsSendQueue {

  /**
   * 队列大小
   */
  static final int QUEUE_MAX_SIZE = 1000;

  static BlockingQueue<SmsDto> blockingQueue = new LinkedBlockingQueue<SmsDto>(QUEUE_MAX_SIZE);


  private SmsSendQueue() {
  }

  private static class SingletonHolder {

    private static SmsSendQueue queue = new SmsSendQueue();

  }

  /**
   * 单例队列
   */
  public static SmsSendQueue getSmsSendQueue() {
    return SingletonHolder.queue;
  }

  /**
   * 生产入队
   */
  public void produce(SmsDto smsDto) throws InterruptedException {
    blockingQueue.put(smsDto);
  }

  /**
   * 消费出队
   */
  public SmsDto consume() throws InterruptedException {
    return blockingQueue.take();
  }

  /**
   * 获取队列大小
   */
  public int size() {
    return blockingQueue.size();
  }

}
