package com.frxs.promotion.core.service.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.promotion.service.api.dto.SmsDto;
import com.frxs.promotion.service.api.dto.SmsMsgDto;
import com.frxs.promotion.service.api.dto.SmsQueryDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;

/**
 * 短信发送、验证、列表查询
 *
 * @author fygu
 * @version $Id: SmsService.java,v 0.1 2018年01月27日 12:39 $Exp
 */
public interface SmsService {


  /**
   * 短信列表查询
   *
   * @param smsQueryDto 短信查询Dto
   * @param page 分页数
   * @return smsList
   */
  PromotionBaseResult<Page<SmsDto>> smsList(SmsQueryDto smsQueryDto,
      Page<SmsDto> page);

  /**
   * 短信验证码发送接口
   *
   * @param smsDto 电话号码
   * @return SmsResultDto 返回
   */
  PromotionBaseResult sendSms(SmsDto smsDto);


  /**
   * 短信校验
   *
   * @return 结果
   */
  PromotionBaseResult validateSms(SmsDto smsDto);

  /**
   * 校验验证码频次
   *
   * @param smsDto 手机短信信息
   * @return 是否可发送
   */
  PromotionBaseResult validateSendFrenquency(SmsDto smsDto);

  /**
   * 短信消息发送接口
   *
   * @param smsMsgDto smsMsgDto
   * @return PromotionBaseResult
   */
  PromotionBaseResult sendSmsMsg(SmsMsgDto smsMsgDto);

}
