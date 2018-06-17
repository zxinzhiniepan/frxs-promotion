/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 短信频次：
 * 1分钟内发送1次。
 * 5分钟内发送2次。
 * 24小时内发送10次。
 *
 * @author sh
 * @version $Id: SmsFrequencyEnum.java,v 0.1 2018年02月27日 上午 10:02 $Exp
 */
public enum SmsFrequencyEnum implements BaseEnum<Integer> {
  ONE_MINUTE(60, 1, "1分钟内发送1次"),
  FIVE_MINUTE(300, 2, "5分钟内发送2次"),
  WENTY_FOUR_HOUR(86400, 10, "24小时内发送10次"),;

  private int time;
  private int count;
  private String desc;

  SmsFrequencyEnum(int time, int count, String desc) {
    this.time = time;
    this.count = count;
    this.desc = desc;
  }

  @Override
  public Integer getValueDefined() {
    return this.time;
  }

  @Override
  public String getDesc() {
    return this.desc;
  }

  @Override
  public Serializable getValue() {
    return this.time;
  }

  public int getCount() {
    return count;
  }
}
