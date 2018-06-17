/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto;

import com.frxs.framework.util.common.StringUtil;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author fygu
 * @version $Id: ActivityPreproductQueryDto.java,v 0.1 2018年01月29日 11:24 $Exp
 */
@Getter
public class ActivityPreproductQueryDto implements Serializable {

  /**
   * 显示时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date showStartTime;

  /**
   * 活动状态 0 未开始 1 进行中 2 已结束
   */
  private String activityStatus;

  /**
   * 商品名称
   */
  private String productName;

  /**
   * 商品编码
   */
  private String sku;

  /**
   * 区域Id
   */
  private Long areaId;

  public void setShowStartTime(Date showStartTime) {
    this.showStartTime = showStartTime;
  }

  public void setActivityStatus(String activityStatus) {
    this.activityStatus = activityStatus != null ? StringUtil.trim(activityStatus) : activityStatus;
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
