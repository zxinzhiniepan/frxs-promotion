/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 商品属性值
 * table name:  t_activity_preproduct_attr_val
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */

@Data
public class PreproductAttrValDto extends AbstractSuperDto implements Serializable {


  private static final long serialVersionUID = -8294656446717888324L;
  /**
   * 预售商品属性关联表
   */
  private Long preproductAttrValId;
  /**
   * 预售商品id
   */
  private Long preproductId;
  /**
   * 属性id
   */
  private Long attrId;
  /**
   * 属性名称
   */
  private String attrName;
  /**
   * 属性值id
   */
  private Long attrValId;
  /**
   * 属性值
   */
  private String attrVal;
  /**
   * 层级:从0开始
   */
  private Integer attrLevel;
}