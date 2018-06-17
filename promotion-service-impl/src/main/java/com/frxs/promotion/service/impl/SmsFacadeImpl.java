package com.frxs.promotion.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.integration.dubbo.annotation.FrxsAutowired;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.common.dal.enums.ErrorCodeDetailEnum;
import com.frxs.promotion.common.dal.enums.ErrorCodeScenarioEnum;
import com.frxs.promotion.common.util.exception.BasePromotionException;
import com.frxs.promotion.core.service.component.SmsMsgSendQueue;
import com.frxs.promotion.core.service.component.SmsSendQueue;
import com.frxs.promotion.core.service.helper.PromotionResultHelper;
import com.frxs.promotion.core.service.service.SmsService;
import com.frxs.promotion.service.api.dto.SmsDto;
import com.frxs.promotion.service.api.dto.SmsMsgDto;
import com.frxs.promotion.service.api.dto.SmsQueryDto;
import com.frxs.promotion.service.api.facade.SmsFacade;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import com.google.common.base.Preconditions;

/**
 * @author fygu
 * @version $Id: SmsFacadeImpl.java,v 0.1 2018年01月31日 14:34 $Exp
 */
@Service(version = "1.0.0")
public class SmsFacadeImpl implements SmsFacade {

  @FrxsAutowired
  private SmsService smsService;


  @FrxsAutowired
  private PromotionResultHelper<PromotionBaseResult> promotionResultHelper;

  @Override
  public PromotionBaseResult<Page<com.frxs.promotion.service.api.dto.SmsDto>> smsList(SmsQueryDto smsQueryDto, Page<com.frxs.promotion.service.api.dto.SmsDto> page) {
    return smsService.smsList(smsQueryDto, page);
  }

  @Override
  public PromotionBaseResult sendSms(SmsDto smsDto) {
    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
    try {
      //添加短信频率校验
      PromotionBaseResult validaterResult = smsService.validateSendFrenquency(smsDto);
      if (!validaterResult.isSuccess()) {
        return validaterResult;
      }
      SmsSendQueue.getSmsSendQueue().produce(smsDto);
      promotionBaseResult.setSuccess(true);
    } catch (InterruptedException e) {
      LogUtil.error(e, "短信发送失败");
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
    }
    return promotionBaseResult;

  }

  @Override
  public PromotionBaseResult validateSms(SmsDto smsDto) {
    return smsService.validateSms(smsDto);
  }

  @Override
  public PromotionBaseResult sendSmsMsg(SmsMsgDto smsMsg) {

    PromotionBaseResult promotionBaseResult = new PromotionBaseResult();
    try {
      Preconditions.checkArgument(smsMsg.getPhoneNums() != null && !smsMsg.getPhoneNums().isEmpty(), "手机号不能为空");
      Preconditions.checkArgument(StringUtil.isNotBlank(smsMsg.getContent()), "短信内容不能为空");
      SmsMsgSendQueue.getSmsSendQueue().produce(smsMsg);
      promotionResultHelper.fillWithSuccess(promotionBaseResult);
    } catch (Exception e) {
      LogUtil.error(e, "短信发送失败");
      promotionResultHelper.fillWithFailure(promotionBaseResult, ErrorCodeScenarioEnum.SMS, new BasePromotionException(ErrorCodeDetailEnum.SMS_SEND_ERROR, "短信发送失败"));
    }
    return promotionBaseResult;
  }
}
