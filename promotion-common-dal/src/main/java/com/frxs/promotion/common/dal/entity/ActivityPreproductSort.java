package com.frxs.promotion.common.dal.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.frxs.framework.common.domain.Money;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 商品显示顺序
 *
 * @author fygu
 * @version $Id: ActivityPreproductSort.java,v 0.1 2018年01月29日 13:10 $Exp
 */
@Data
public class ActivityPreproductSort {

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
  private Date tmDisplayStart;
  /**
   * 显示结束时间
   */
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
  @TableField("saleAmt")
  private Money saleAmt;
  /**
   * 市场价
   */
  @TableField("marketAmt")
  private Money marketAmt;
  /**
   * 供货价
   */
  @TableField("supplyAmt")
  private Money supplyAmt;
  /**
   * 平台服务费
   */
  @TableField("perServiceAmt")
  private Money perServiceAmt;
  /**
   * 每份提成
   */
  @TableField("perCommission")
  private Money perCommission;

}
