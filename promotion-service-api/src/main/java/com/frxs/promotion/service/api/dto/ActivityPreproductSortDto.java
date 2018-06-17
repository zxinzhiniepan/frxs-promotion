package com.frxs.promotion.service.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author fygu
 * @version $Id: ActivityPreproductSortDto.java,v 0.1 2018年01月29日 14:23 $Exp
 */
@Data
public class ActivityPreproductSortDto implements Serializable {

  /**
   * 活动id
   */
  private Long activityId;


  /**
   * 预售活动商品id
   */
  private Long preproductId;

  /**
   * 显示开始时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmDisplayStart;
  /**
   * 显示结束时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmDisplayEnd;

  /**
   * 商品排序序号
   */
  private Integer sortSeq;

  /**
   * 商品sku
   */
  private String sku;
  /**
   * 商品名称
   */
  private String productName;

  /**
   * 供应商名称
   */
  private String vendorName;
  /**
   * 供应商编号
   */
  private String vendorCode;

  /**
   * 商品规格类型:SINGLE-单规格,MULTI-多规格
   */
  private String specType;
  /**
   * 包装数
   */
  private BigDecimal packageQty;

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
   * 规格属性:key=属性名称，value=属性值
   */
  private List<PreproductAttrValDto> attrs;
}
