package com.frxs.promotion.core.service.component;

import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.core.service.service.SmsService;
import com.frxs.promotion.service.api.dto.SmsDto;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author fygu
 * @version $Id: ConsumeSmsTask.java,v 0.1 2018年02月07日 15:20 $Exp
 */
@Component
public class SmsSendTask {

  @Value("${sms.pool.count}")
  private Integer poolCount;

  @Autowired
  SmsService smsService;

  @PostConstruct
  public void startThread() {

    ExecutorService e = Executors.newFixedThreadPool(poolCount);

    for (int i = 0; i < poolCount; i++) {
      e.submit(new PollSms(smsService));
    }
  }

  class PollSms implements Runnable {

    SmsService smsService;

    public PollSms(SmsService smsService) {
      this.smsService = smsService;
    }

    @Override
    public void run() {
      while (true) {
        try {
          SmsDto smsDto = SmsSendQueue.getSmsSendQueue().consume();
          if (smsDto != null) {
            smsService.sendSms(smsDto);
          }
        } catch (Exception e) {
          LogUtil.error(e, "[SmsSendTask:短信发送task]短信发送异常");
        }
      }
    }
  }

  @PreDestroy
  public void stopThread() {

  }

}
