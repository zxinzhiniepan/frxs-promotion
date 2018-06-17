/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 图文直播状态：EXPIRED-已过期，DISPLAY-展示中，REJECT-驳回
 *
 * @author sh
 * @version $Id: OnlineImgStatusEnum.java,v 0.1 2018年02月10日 下午 16:03 $Exp
 */
public enum OnlineImgStatusEnum implements BaseEnum<String> {

  EXPIRED("EXPIRED", "已过期"),
  DISPLAY("DISPLAY", "展示中"),
  REJECT("REJECT", "驳回"),;

  private String value;
  private String desc;

  OnlineImgStatusEnum(String value, String desc) {
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
