/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.enums;

import com.frxs.framework.common.errorcode.constant.ErrorTypes;
import lombok.Getter;

/**
 * 错误码字典枚举
 *
 * <p>此枚举用于定义demo对外提供的错误码集合。 <p>demo返回给调用方的错误码，承诺不会超出此错误码集合。
 *
 * @author mingbo.tang
 * @version $Id: DemoErrorCodeEnum.java,v 0.1 2017年12月27日 上午 11:14 $Exp
 */
@Getter
public enum ErrorCodeEnum {

  /**
   * 未知错误
   */
  UNKNOWN_EXCEPTION(ErrorCodeScenarioEnum.UNKNOWN, ErrorCodeDetailEnum.UNKNOWN_EXCEPTION, "营销未知异常"),
  CONFIGURATION_ERROR(ErrorCodeScenarioEnum.CONFIG, ErrorCodeDetailEnum.CONFIGURATION_ERROR, "配置错误"),
  //========================================================================//
  //                               业务错误                                                                     //
  //========================================================================//
  STAGED_PAY_UNKNOWN_EXCEPTION(ErrorCodeScenarioEnum.ACTIVITY, ErrorCodeDetailEnum.ACTIVITY_CREATE_ERROR, "营销事务异常"),;

  /**
   * 枚举编码
   */
  private final String code;

  /**
   * 描述说明
   */
  private final String desc;

  /**
   * 错误码明细
   */
  private final ErrorCodeDetailEnum errorDtl;

  /**
   * 私有构造函数
   *
   * @param scenarioEnum 业务场景
   * @param errDtlEnum 错误码
   * @param desc 错误描述
   */
  ErrorCodeEnum(ErrorCodeScenarioEnum scenarioEnum, ErrorCodeDetailEnum errDtlEnum, String desc) {
    this.code = "FE0" + errDtlEnum.getErrorLevel() + ErrorTypes.BIZ + scenarioEnum.getCode()
        + errDtlEnum.getCode();
    this.desc = desc;
    this.errorDtl = errDtlEnum;
  }

  /**
   * 通过枚举<code>code</code>获得枚举
   *
   * @param code 枚举编码
   * @return 支付错误枚举
   */
  public static ErrorCodeEnum getByCode(String code) {
    for (ErrorCodeEnum errorCode : values()) {
      if (errorCode.getCode().equals(code)) {
        return errorCode;
      }
    }
    return null;
  }
}
