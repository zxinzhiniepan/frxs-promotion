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
 * 首页商品
 *
 * @author sh
 * @version $Id: IndexPreproductDto.java,v 0.1 2018年02月06日 下午 14:56 $Exp
 */
@Data
public class IndexPreproductDto implements Serializable {

  private static final long serialVersionUID = -4882611741021601183L;
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
   * 关注人数
   */
  private Long folQty;
  /**
   * 是否直采
   */
  private String dirMin;
  /**
   * 购买开始时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyStart;
  /**
   * 购买截止时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyEnd;
  /**
   * 显示开始时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmShowStart;
  /**
   * 显示结束时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmShowEnd;
  /**
   * 首页广告图
   */
  private String adUrl;
  /**
   * 商品名称
   */
  private String prName;
  /**
   * 商品规格
   */
  private List<AttrDto> attrs;
  /**
   * 提货时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmPickUp;
  /**
   * 销售数量
   */
  private BigDecimal saleQty;
  /**
   * 销售限购数量（为0不限量）
   */
  private BigDecimal limitQty;
  /**
   * 用户限购数量（为0不限量）
   */
  private BigDecimal ulimitQty;
  /**
   * 市场价
   */
  private BigDecimal marketAmt;
  /**
   * 销售价
   */
  private BigDecimal saleAmt;
  /**
   * 图文直播最后上传的商品图片时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmOnlineLasted;
  /**
   * 图文直播id
   */
  private Long imgTxtId;
  /**
   * 供应商ID
   */
  private Long veId;
  /**
   * 供应商编码
   */
  private String veCode;
  /**
   * 供应商LOGO
   */
  private String veLogo;
  /**
   * 供应商名称
   */
  private String veName;
  /**
   * 供应商简称
   */
  private String vesName;
  /**
   * 消费者
   */
  private List<ConsumerDto> consumers;
  /**
   * 主图
   */
  private String primaryUrl;
}
