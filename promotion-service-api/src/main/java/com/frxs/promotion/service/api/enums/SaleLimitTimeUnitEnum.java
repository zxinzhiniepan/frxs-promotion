/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 售后时限单位:DAY-天，HOUR-时
 *
 * @author sh
 * @version $Id: SaleLimitTimeUnitEnum.java,v 0.1 2018年01月24日 下午 15:43 $Exp
 */
public enum SaleLimitTimeUnitEnum implements BaseEnum<String> {
  DAY("DAY", "天"),
  HOUR("HOUR", "时"),;

  private String value;
  private String desc;

  SaleLimitTimeUnitEnum(String value, String desc) {
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
}
