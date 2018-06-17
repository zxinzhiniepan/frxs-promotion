/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto.consumer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * 购物车
 *
 * @author sh
 * @version $Id: ShopCarDto.java,v 0.1 2018年01月24日 下午 16:53 $Exp
 */
@Data
public class ShopCarDto implements Serializable {

  private static final long serialVersionUID = -9052435366335873126L;

  /**
   * 购物车商品列表
   */
  private List<ShopCarProductDto> carPrs;
  /**
   * 合计
   */
  private BigDecimal totalAmt;
  /**
   * 用户id
   */
  private Long userId;
  /**
   * 微信名称
   */
  private String wxName;
  /**
   * 手机号
   */
  private String mobile;
  /**
   * 提货地址
   */
  private String pkUpPlace;
  /**
   * 提货店名称
   */
  private String stName;
  /**
   * 提货店手机号
   */
  private String stMobile;
}
