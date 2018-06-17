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
 * 供应商活动商品查询Dto
 *
 * @author sh
 * @version $Id: VendorPreproductQueryDto.java,v 0.1 2018年02月27日 下午 16:25 $Exp
 */
@Getter
public class VendorPreproductQueryDto implements Serializable {

  /**
   * 购买开始时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd ")
  private Date tmBuyStart;
  /**
   * 购买结束时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date tmBuyEnd;
  /**
   * 商品名称
   */
  private String productName;
  /**
   * 商品编码
   */
  private String sku;
  /**
   * 供应商id
   */
  private Long vendorId;
  /**
   * 区域id
   */
  private Long areaId;

  public void setTmBuyStart(Date tmBuyStart) {
    this.tmBuyStart = tmBuyStart;
  }

  public void setTmBuyEnd(Date tmBuyEnd) {
    this.tmBuyEnd = tmBuyEnd;
  }

  public void setProductName(String productName) {
    this.productName = productName != null ? StringUtil.trim(productName) : productName;
  }

  public void setSku(String sku) {
    this.sku = sku != null ? StringUtil.trim(sku) : sku;
  }

  public void setVendorId(Long vendorId) {
    this.vendorId = vendorId;
  }

  public void setAreaId(Long areaId) {
    this.areaId = areaId;
  }
}
