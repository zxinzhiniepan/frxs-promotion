/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto.consumer;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 购物车商品
 *
 * @author sh
 * @version $Id: ShopCarProductDto.java,v 0.1 2018年01月24日 下午 16:55 $Exp
 */
@Data
public class ShopCarProductDto implements Serializable {

  private static final long serialVersionUID = -7664218601840630170L;
  /**
   * 商品id
   */
  private Long prId;
  /**
   * 活动id
   */
  private Long acId;
  /**
   * 预售商品id
   */
  private Long preId;
  /**
   * sku
   */
  private String sku;
  /**
   * 商品规格
   */
  private List<AttrDto> attrs;
  /**
   * 售价（元）
   */
  private BigDecimal saleAmt;
  /**
   * 市场价
   */
  private BigDecimal marketAmt;
  /**
   * 加入购物车的数量
   */
  private BigDecimal buyQty;
  /**
   * 购物车商品图片地址
   */
  private String imgUrl;
  /**
   * 提货时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmPickUp;
  /**
   * 是否可以购买:TRUE-可购买,FALSE-不可购买
   */
  private String canBuy;
  /**
   * 提示信息
   */
  private String msg;
}
