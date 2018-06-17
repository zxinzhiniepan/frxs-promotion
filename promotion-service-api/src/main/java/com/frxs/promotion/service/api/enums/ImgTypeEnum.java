/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.enums;

import com.frxs.framework.common.enums.BaseEnum;
import java.io.Serializable;

/**
 * 图片类型：AD-广告图，PRIMARY-主图，DETAIL-详情图
 *
 * @author sh
 * @version $Id: ImgTypeEnum.java,v 0.1 2018年01月24日 下午 15:44 $Exp
 */
public enum ImgTypeEnum implements BaseEnum<String> {
  AD("AD", "广告图"),
  PRIMARY("PRIMARY", "主图"),
  DETAIL("DETAIL", "详情图"),;

  private String value;
  private String desc;

  ImgTypeEnum(String value, String desc) {
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
   * 获取图片类型
   *
   * @param value 值
   * @return 枚举
   */
  public static ImgTypeEnum getImgTypeEnum(String value) {
    for (ImgTypeEnum typeEnum : ImgTypeEnum.values()) {
      if (typeEnum.getValueDefined().equals(value)) {
        return typeEnum;
      }
    }
    return null;
  }
}
