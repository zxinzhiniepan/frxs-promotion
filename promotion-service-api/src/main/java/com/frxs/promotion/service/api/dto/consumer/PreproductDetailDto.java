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
import java.util.Map;
import lombok.Data;

/**
 * 商品详情
 *
 * @author sh
 * @version $Id: PreproductDetailDto.java,v 0.1 2018年02月06日 下午 16:50 $Exp
 */
@Data
public class PreproductDetailDto implements Serializable {

  private static final long serialVersionUID = 4433663687723435624L;

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
   * 所在区域id
   */
  private Long areaId;
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
   * 商品名称
   */
  private String prName;
  /**
   * 品牌名称
   */
  private String brName;
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
  private List<ConsumerInfoDto> consumers;
  /**
   * 商品副标题
   */
  private String prTitle;
  /**
   * 商品详情
   */
  private String prDetail;
  /**
   * 商品分享标题
   */
  private String shTitle;
  /**
   * 商品简介
   */
  private String prBrief;
  /**
   * 主图
   */
  private List<String> primaryUrls;
  /**
   * 图文详情图
   */
  private List<String> detailUrls;
  /**
   * 消费总人数
   */
  private Long consumerNum;
  /**
   * 直采直播id
   */
  private Long imgTxtId;
  /**
   * 直采直播标题
   */
  private String imgTxtTitle;
  /**
   * 文本列表
   */
  private List<PreproductDetailOnlineTextDto> texts;
  /**
   * 生产地
   */
  private String yieldly;
  /**
   * 门店id
   */
  private Long stId;
  /**
   * 商品状态：UP-上架，DOWN-下架
   */
  private String status;
}
