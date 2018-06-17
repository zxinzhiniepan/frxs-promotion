package com.frxs.promotion.service.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.dal.mapper.ActivityOnlineImgtextMapper;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import com.frxs.promotion.core.service.component.SmsMsgSendQueue;
import com.frxs.promotion.core.service.component.SmsSendQueue;
import com.frxs.promotion.core.service.helper.PromotionResultHelper;
import com.frxs.promotion.core.service.service.SmsService;
import com.frxs.promotion.service.PromotionApplication;
import com.frxs.promotion.service.api.dto.SmsDto;
import com.frxs.promotion.service.api.dto.SmsMsgDto;
import com.frxs.promotion.service.api.dto.SmsQueryDto;
import com.frxs.promotion.service.api.enums.SmsTypeEnum;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author fygu
 * @version $Id: FyguTest.java,v 0.1 2018年02月07日 15:36 $Exp
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PromotionApplication.class)
public class FyguTest {

  @Autowired
  private SmsService smsService;

  @Autowired
  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;


  @Test
  public void validSms() {
    SmsDto smsDto = new SmsDto();
    smsDto.setSmsType(SmsTypeEnum.LOGIN.getValueDefined());
    smsDto.setPhoneNum("18273144132");
    smsDto.setVerificationCode("3461");
    smsDto.setAreaId(101L);
    PromotionBaseResult res = smsService.validateSms(smsDto);

    System.out.print(1111);
  }

  @Test
  public void testSendSms() throws InterruptedException {

    SmsDto smsDto = new SmsDto();
    smsDto.setSmsType(SmsTypeEnum.LOGIN.getValueDefined());
    smsDto.setPhoneNum("18273144132");
    smsDto.setAreaId(101L);
    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
    //添加短信频率校验
    PromotionBaseResult validaterResult = smsService.validateSendFrenquency(smsDto);
    if (validaterResult.isSuccess()) {
      SmsSendQueue.getSmsSendQueue().produce(smsDto);
      promotionBaseResult.setSuccess(true);
    } else {
      promotionBaseResult.setSuccess(false);
    }
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
    Thread.sleep(Integer.MAX_VALUE);
  }

  @Test
  public void sendSms() {

    SmsDto smsDto = new SmsDto();
    smsDto.setAreaId(4L);
    smsDto.setPhoneNum("18273144132");
    smsDto.setNum(4);
    smsDto.setSmsType(SmsTypeEnum.LOGIN.getValueDefined());
    PromotionBaseResult promotionBaseResult = smsService.sendSms(smsDto);

    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void validateSendFrenquency() {
    SmsDto smsDto = new SmsDto();
    smsDto.setAreaId(4L);
    smsDto.setPhoneNum("18273144132");
    smsDto.setSmsType(SmsTypeEnum.LOGIN.getValueDefined());
    PromotionBaseResult promotionBaseResult = smsService.validateSendFrenquency(smsDto);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void validateSms() {
    SmsDto smsDto = new SmsDto();
    smsDto.setAreaId(4L);
    smsDto.setPhoneNum("15173167893");
    smsDto.setSmsType(SmsTypeEnum.LOGIN.getValueDefined());
    smsDto.setVerificationCode("5511");
    PromotionBaseResult promotionBaseResult = smsService.validateSms(smsDto);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void smsList() {
    SmsQueryDto query = new SmsQueryDto();
    query.setAreaId(101L);
    Page<SmsDto> page = new Page<>(1, 10);
    PromotionBaseResult<Page<SmsDto>> promotionBaseResult = smsService.smsList(query, page);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void testSendSmsMsgQueue() throws InterruptedException {

    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
    SmsMsgDto smsMsgDto = new SmsMsgDto();
    List<String> list = new ArrayList<>();
    list.add("18273144132");
    list.add("15874233550");
    smsMsgDto.setContent("我测试一下，不要理会最后测1个！！");
    smsMsgDto.setPhoneNums(list);
    try {
      SmsMsgSendQueue.getSmsSendQueue().produce(smsMsgDto);
      promotionResultHelper.fillWithSuccess(promotionBaseResult);
    } catch (Exception e) {
      LogUtil.error(e, "短信发送失败");
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
    }
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
    Thread.sleep(Integer.MAX_VALUE);
  }

  @Test
  public void testSendSmsMsg() throws InterruptedException {

    SmsMsgDto smsMsgDto = new SmsMsgDto();
    List<String> list = new ArrayList<>();
    list.add("18273144132");
    list.add("15874233550");
    smsMsgDto.setContent("我测试一下，不要理会");
    smsMsgDto.setPhoneNums(list);
    PromotionBaseResult promotionBaseResult = smsService.sendSmsMsg(smsMsgDto);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
    Thread.sleep(Integer.MAX_VALUE);
  }
}
