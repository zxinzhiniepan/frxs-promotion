package com.frxs.promotion.service.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.frxs.promotion.service.api.dto.consumer.ConsumerDto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 预售商品详情
 */
@Data
public class PreproductDto extends AbstractSuperDto implements Serializable {

  private static final long serialVersionUID = -7416445550939124443L;
  /**
   * 预售活动商品id
   */
  private Long preproductId;
  /**
   * 商品id
   */
  private Long productId;
  /**
   * 商品sku
   */
  private String sku;
  /**
   * 活动id
   */
  private Long activityId;
  /**
   * 活动类型：NORMAL-正常预售，SECKILL-秒杀
   */
  private String activityType;
  /**
   * 供应商id
   */
  private Long vendorId;
  /**
   * 供应商编号
   */
  private String vendorCode;
  /**
   * 供应商名称
   */
  private String vendorName;
  /**
   * 供应商简称
   */
  private String vendorShortName;
  /**
   * 供应商logo地址
   */
  private String vendorLogoUrl;
  /**
   * 广告图片地址
   */
  private String adImgUrl;
  /**
   * 关注人数
   */
  private Long followQty;
  /**
   * 是否直采：TRUE-直采,FALSE-非直采
   */
  private String directMining;
  /**
   * 是否有图文直播:TRUE-有，FALSE-无
   */
  private String hasImgTextOnline;
  /**
   * 商品名称
   */
  private String productName;
  /**
   * 规格属性:key=属性名称，value=属性值
   */
  private List<PreproductAttrValDto> attrs;
  /**
   * 购买开始时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyStart;
  /**
   * 购买结束时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyEnd;
  /**
   * 提货时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmPickUp;
  /**
   * 售价（元）
   */
  private BigDecimal saleAmt;
  /**
   * 市场价（元）
   */
  private BigDecimal marketAmt;
  /**
   * 供货价
   */
  private BigDecimal supplyAmt;
  /**
   * 已售份数
   */
  private BigDecimal saleQty;
  /**
   * 限量
   */
  private BigDecimal limitQty;
  /**
   * 用户限量
   */
  private BigDecimal userLimitQty;
  /**
   * 消费者用户列表
   */
  private List<ConsumerDto> consumers;
  /**
   * 商品主图地址列表(商品轮播图)
   */
  private List<PreproductImgDto> primaryImgs;
  /**
   * 图文详情图片列表
   */
  private List<PreproductImgDto> descImgs;
  /**
   * 活动详情
   */
  private String detailDesc;
  /**
   * 商品简介
   */
  private String briefDesc;
  /**
   * 商品副标题
   */
  private String productTitle;
  /**
   * 加入购物车的数量
   */
  private BigDecimal shopCarQty;
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
   * 平台服务费
   */
  private BigDecimal perServiceAmt;
  /**
   * 每份提成
   */
  private BigDecimal perCommission;
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
   * 品牌名称
   */
  private String brandName;

  /**
   * 生产地
   */
  private String yieldly;
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
   * 商品规格类型:SINGLE-单规格,MULTI-多规格
   */
  private String specType;
  /**
   * 条形码列表JSON串
   */
  private String barCodes;

  /**
   * 图文直播
   */
  private OnlineImgtextDto onlineImgtext;
  /**
   * 活动名称
   */
  private String activityName;
  /**
   * 商品主图
   */
  private String primaryUrl;
  /**
   * 商品名称+规格
   */
  private String nameSpec;
}