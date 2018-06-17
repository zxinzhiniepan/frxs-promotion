package com.frxs.promotion.common.dal.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.frxs.framework.common.domain.Money;
import com.frxs.framework.data.persistent.AbstractSuperEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 预售商品
 * table name:  t_activity_preproduct
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */
@Data
@TableName("t_activity_preproduct")
public class ActivityPreproduct extends AbstractSuperEntity<ActivityPreproduct> {


  private static final long serialVersionUID = 8599715617253493624L;
  /**
   * 预售活动商品id
   */
  @TableId
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
   * 品牌名称
   */
  private String brandName;
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
  /**
   * 是否直采:TRUE-直采,FALSE-非直采
   */
  private String directMining;
  /**
   * 关注人数
   */
  private Long followQty;
  /**
   * 商品主图
   */
  private String primaryUrl;
  /**
   * 创建人id
   */
  private Long createUserId;
  /**
   * 创建人用户名
   */
  private String createUserName;
  /**
   * 修改人id
   */
  private Long modifyUserId;
  /**
   * 修改人用户名
   */
  private String modifyUserName;

  public ActivityPreproduct() {
  }

  @Override
  protected Serializable pkVal() {
    return this.preproductId;
  }
}