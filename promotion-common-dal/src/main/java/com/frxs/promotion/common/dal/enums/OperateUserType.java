/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 操作人类型
 *
 * @author sh
 * @version $Id: OperateUserType.java,v 0.1 2018年02月26日 上午 9:09 $Exp
 */
public enum OperateUserType implements BaseEnum<String> {
  VENDOR("VENDOR", "供应商端"),
  CONSUMER("CONSUMER", "消费端"),
  AREA("AREA", "区域端"),;

  private String value;
  private String desc;

  OperateUserType(String value, String desc) {
    this.value = value;
    this.desc = desc;
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

