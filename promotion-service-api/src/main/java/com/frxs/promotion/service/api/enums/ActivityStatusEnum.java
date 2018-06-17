/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 活动状态：NOTSTARTED-未开始,ONGOING-进行中,END-已结束
 *
 * @author sh
 * @version $Id: ActivityStatusEnum.java,v 0.1 2018年01月24日 下午 15:39 $Exp
 */
public enum ActivityStatusEnum implements BaseEnum<String> {

  NOTSTARTED("NOTSTARTED", "未开始"),
  ONGOING("ONGOING", "进行中"),
  END("END", "已结束"),;

  private String value;
  private String desc;

  ActivityStatusEnum(String value, String desc) {
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
