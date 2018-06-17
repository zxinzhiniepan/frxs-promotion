/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 活动类型：NORMAL-正常预售，SECKILL-秒杀
 *
 * @author sh
 * @version $Id: ActivityTypeEnum.java,v 0.1 2018年01月24日 下午 15:39 $Exp
 */
public enum ActivityTypeEnum implements BaseEnum<String> {

  NORMAL("NORMAL", "正常预售"),
  SECKILL("SECKILL", "秒杀"),;

  private String value;
  private String desc;

  ActivityTypeEnum(String value, String desc) {
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
