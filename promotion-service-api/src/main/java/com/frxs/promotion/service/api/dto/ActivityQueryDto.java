/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto;

import com.frxs.framework.util.common.StringUtil;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 活动查询参数
 *
 * @author sh
 * @version $Id: ActivityQueryDto.java,v 0.1 2018年02月02日 下午 15:09 $Exp
 */
@Getter
public class ActivityQueryDto implements Serializable {

  private static final long serialVersionUID = -171191426157669981L;
  /**
   * 活动名称
   */
  private String activityName;
  /**
   * 购买开始时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyStart;
  /**
   * 购买结束时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyEnd;
  /**
   * 活动状态：NOTSTARTED-未开始，ONGOING-进行中，END-已结束
   */
  private String actvityStatus;
  /**
   * 活动状态：PENDING-待审核，PASS-审核通过，REJECT-驳回,DELETED-已删除
   */
  private String status;
  /**
   * 商品名称
   */
  private String productName;
  /**
   * 商品编码
   */
  private String sku;
  /**
   * 区域id
   */
  private Long areaId;


  public void setActivityName(String activityName) {
    this.activityName = activityName != null ? StringUtil.trim(activityName) : activityName;
  }

  public void setTmBuyStart(Date tmBuyStart) {
    this.tmBuyStart = tmBuyStart;
  }

  public void setTmBuyEnd(Date tmBuyEnd) {
    this.tmBuyEnd = tmBuyEnd;
  }

  public void setActvityStatus(String actvityStatus) {
    this.actvityStatus = actvityStatus != null ? StringUtil.trim(actvityStatus) : actvityStatus;
  }

  public void setStatus(String status) {
    this.status = status != null ? StringUtil.trim(status) : status;
  }

  public void setProductName(String productName) {
    this.productName = productName != null ? StringUtil.trim(productName) : productName;
  }

  public void setSku(String sku) {
    this.sku = sku != null ? StringUtil.trim(sku) : sku;
  }

  public void setAreaId(Long areaId) {
    this.areaId = areaId;
  }
}
