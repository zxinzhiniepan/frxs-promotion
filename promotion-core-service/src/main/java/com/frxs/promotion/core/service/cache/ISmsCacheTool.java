/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache;

import com.frxs.promotion.core.service.pojo.SmsCodePojo;
import com.frxs.promotion.core.service.pojo.SmsPojo;
import com.frxs.promotion.service.api.dto.SmsDto;

/**
 * 手机短信缓存工具
 *
 * @author sh
 * @version $Id: ISmsCacheTool.java,v 0.1 2018年02月28日 下午 20:17 $Exp
 */
public interface ISmsCacheTool {

  /**
   * 设置手机验证码缓存
   *
   * @param sms 短信参数
   */
  void setSmsCodeValidateCache(SmsDto sms);

  /**
   * 获取手机验证码缓存
   *
   * @param mobile 手机号
   * @param smsType 类型
   * @return 验证码
   */
  SmsPojo getSmsCodeValiateCache(String mobile, String smsType);

  /**
   * 获取验证码频次缓存
   *
   * @param mobile 手机号
   * @param smsType 类型
   * @return 频次结果
   */
  SmsCodePojo getSmsCodeFrenquencyCache(String mobile, String smsType);

  /**
   * 清理验证码
   *
   * @param mobile 手机号
   * @param smsType 验证类型
   */
  void removeSmsCodeValiateCache(String mobile, String smsType);

}
