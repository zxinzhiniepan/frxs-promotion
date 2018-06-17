/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 短信类型:FINDPWD-找回密码,UPDATEPWD-修改密码,LOGIN-登录验证码,WITHDRAWAL-提现
 *
 * @author sh
 * @version $Id: SmsTypeEnum.java,v 0.1 2018年01月24日 下午 15:46 $Exp
 */
public enum SmsTypeEnum implements BaseEnum<String> {
  FINDPWD("FINDPWD", "密码找回验证码：%s"),
  UPDATEPWD("UPDATEPWD", "密码修改验证码：%s"),
  LOGIN("LOGIN", "登录验证码：%s"),
  WITHDRAWAL("WITHDRAWAL", "提现验证码：%s，不要透露给其他人");
  private String value;
  private String desc;

  SmsTypeEnum(String value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  public static SmsTypeEnum getSmsTypeEnum(String value) {
    for (SmsTypeEnum smsTypeEnum : SmsTypeEnum.values()) {
      if (smsTypeEnum.getValue().equals(value)) {
        return smsTypeEnum;
      }
    }
    return null;
  }


  @Override
  public String getValueDefined() {
    return this.value;
  }

  @Override
  public String getDesc() {
    return this.desc;
  }

  @Override
  public Serializable getValue() {
    return this.value;
  }
}
