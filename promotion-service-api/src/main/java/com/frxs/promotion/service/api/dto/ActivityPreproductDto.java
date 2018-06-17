package com.frxs.promotion.service.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author fygu
 * @version $Id: ActivityPreproductDto.java,v 0.1 2018年01月29日 11:22 $Exp
 */
@Data
public class ActivityPreproductDto extends AbstractSuperDto implements Serializable {

  private static final long serialVersionUID = -6362791047982207674L;
  /**
   * 预售活动商品id
   */
  private Long preproductId;
  /**
   * 活动id
   */
  private Long activityId;
  /**
   * 商品id
   */
  private Long productId;
  /**
   * 商品sku
   */
  private String sku;
  /**
   * 商品名称
   */
  private String productName;
  /**
   * 商品标题
   */
  private String productTitle;
  /**
   * 供应商id
   */
  private Long vendorId;
  /**
   * 供应商名称
   */
  private String vendorName;
  /**
   * 供应商编号
   */
  private String vendorCode;
  /**
   * 生产地
   */
  private String yieldly;
  /**
   * 商品规格类型:SINGLE-单规格,MULTI-多规格
   */
  private String specType;
  /**
   * 包装数
   */
  private BigDecimal packageQty;
  /**
   * 售后时限
   */
  private BigDecimal saleLimitTime;
  /**
   * 售后时限单位:DAY-天，HOUR-时
   */
  private String saleLimitTimeUnit;
  /**
   * 条形码列表JSON串
   */
  private String barCodes;
  /**
   * 商品排序序号
   */
  private Integer sortSeq;
  /**
   * 商品排序时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmSort;
  /**
   * 限订数量
   */
  private BigDecimal limitQty;
  /**
   * 用户限订数量
   */
  private BigDecimal userLimitQty;
  /**
   * 已售数量
   */
  private BigDecimal saleQty;
  /**
   * 销售价
   */
  private BigDecimal saleAmt;
  /**
   * 市场价
   */
  private BigDecimal marketAmt;
  /**
   * 供货价
   */
  private BigDecimal supplyAmt;
  /**
   * 平台服务费
   */
  private BigDecimal perServiceAmt;
  /**
   * 每份提成
   */
  private BigDecimal perCommission;
  /**
   * 是否直采:TRUE-直采,FALSE-非直采
   */
  private String directMining;
  /**
   * 关注人数
   */
  private Integer followQty;

}
