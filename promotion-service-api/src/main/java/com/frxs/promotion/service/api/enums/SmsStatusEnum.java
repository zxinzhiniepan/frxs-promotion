package com.frxs.promotion.service.api.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * @author fygu
 * @version $Id: SmsStatusEnum.java,v 0.1 2018年02月23日 16:49 $Exp
 */
public enum SmsStatusEnum implements BaseEnum {

  SENDED("SENDED", "已发送"),
  USED("USED", "已使用"),
  SENDFAIL("SENDFAIL", "发送失败"),;

  private String value;
  private String desc;

  SmsStatusEnum(String value, String desc) {
    this.value = value;
    this.desc = desc;
  }

  @Override
  public Serializable getValue() {
    return this.value;
  }

  @Override
  public String getValueDefined() {
    return this.value;
  }

  @Override
  public String getDesc() {
    return this.desc;
  }

  /**
   * 根据value获取枚举
   *
   * @param value 值
   * @return 枚举
   */
  public static SmsStatusEnum getAreaServiceamtCodeEnum(String value) {

    for (SmsStatusEnum smsStatusEnum : SmsStatusEnum.values()) {
      if (smsStatusEnum.getValueDefined().equals(value)) {
        return smsStatusEnum;
      }
    }
    return null;
  }

}
