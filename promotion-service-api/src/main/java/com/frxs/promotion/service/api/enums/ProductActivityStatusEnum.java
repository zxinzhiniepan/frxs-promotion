/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 活动商品状态
 *
 * @author sh
 * @version $Id: ActivityStatusEnum.java,v 0.1 2018年01月24日 下午 15:39 $Exp
 */
public enum ProductActivityStatusEnum implements BaseEnum<String> {

  UP("UP", "上架"),
  DOWN("DOWN", "下架"),
  NOTSTART("NOTSTART", "未开始"),
  END("END", "已结束"),
  SOLDOUT("SOLDOUT", "已售罄"),;

  private String value;
  private String desc;

  ProductActivityStatusEnum(String value, String desc) {
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
