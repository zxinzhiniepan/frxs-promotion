package com.frxs.promotion.common.dal.entity;

import java.util.Date;
import lombok.Data;

/**
 * @author fygu
 * @version $Id: ActivityOnlineImgtextManage.java,v 0.1 2018年01月29日 16:46 $Exp
 */
@Data
public class ActivityOnlineImgtextManage {

  /**
   * 活动id
   */
  private Long activityId;

  /**
   * 预售活动商品id
   */
  private Long preproductId;

  /**
   * 提交时间
   */
  private Date tmSubmit;

  /**
   * 商品名称
   */
  private String productName;

  /**
   * 活动名称
   */
  private String activityName;

  /**
   * 图文直播id
   */
  private Long imgtextId;

  /**
   * 供应商编码
   */
  private String vendorCode;
  /**
   * 供应商名称
   */
  private String vendorName;

  /**
   * 购买开始时间
   */
  private Date tmBuyStart;

  /**
   * 购买结束时间
   */
  private Date tmBuyEnd;


  /**
   * 审核人名
   */
  private String auditUserName;


  /**
   * 点赞用户人数
   */
  private Integer totalThumbsupQty;

}
