package com.frxs.promotion.service.api.facade;

import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.promotion.service.api.dto.SmsDto;
import com.frxs.promotion.service.api.dto.SmsMsgDto;
import com.frxs.promotion.service.api.dto.SmsQueryDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;

/**
 * 短信接口
 *
 * @author fygu
 * @version $Id: SmsFacade.java,v 0.1 2018年01月27日 12:23 $Exp
 */
public interface SmsFacade {

  /**
   * 短信列表查询
   *
   * @param smsQueryDto 短信查询Dto
   * @param page 分页数
   * @return smsList
   */
  PromotionBaseResult<Page<SmsDto>> smsList(SmsQueryDto smsQueryDto, Page<SmsDto> page);

  /**
   * 短信发送接口
   *
   * @param smsDto 短信接口请求参数
   * @return SmsResultDto 返回
   */
  PromotionBaseResult sendSms(SmsDto smsDto);


  PromotionBaseResult validateSms(SmsDto smsDto);

  /**
   * 发送消息
   *
   * @param smsMsg 短信
   * @return 结果
   */
  PromotionBaseResult sendSmsMsg(SmsMsgDto smsMsg);
}
