package com.frxs.promotion.service.api.dto;

import com.frxs.framework.util.common.StringUtil;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 图文直播查询参数
 *
 * @author sh
 * @version $Id: OnlineImgtextQueryDto.java,v 0.1 2018年01月25日 下午 15:46 $Exp
 */
@Getter
public class OnlineImgtextQueryDto implements Serializable {

  private static final long serialVersionUID = 746913843786029911L;

  /**
   * 供应商编码
   */
  private String vendorCode;

  /**
   * 供应商名称
   */
  private String vendorName;


  /**
   * 活动日期开始时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyStart;

  /**
   * 活动日期结束时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyEnd;

  /**
   * 提交日期开始时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmSubmitStart;

  /**
   * 提交日期结束时间
   */
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date tmSubmitEnd;

  /**
   * 商品名称
   */
  private String productName;

  /**
   * 图文直播状态：EXPIRED-已过期，DISPLAY-展示中，REJECT-驳回,PASS-审核通过
   */
  private String imgTextStatus;

  /**
   * 区域id
   */
  private Long areaId;

  public void setVendorCode(String vendorCode) {
    this.vendorCode = vendorCode != null ? StringUtil.trim(vendorCode) : vendorCode;
  }

  public void setVendorName(String vendorName) {
    this.vendorName = vendorName != null ? StringUtil.trim(vendorName) : vendorName;
  }

  public void setTmBuyStart(Date tmBuyStart) {
    this.tmBuyStart = tmBuyStart;
  }

  public void setTmBuyEnd(Date tmBuyEnd) {
    this.tmBuyEnd = tmBuyEnd;
  }

  public void setTmSubmitStart(Date tmSubmitStart) {
    this.tmSubmitStart = tmSubmitStart;
  }

  public void setTmSubmitEnd(Date tmSubmitEnd) {
    this.tmSubmitEnd = tmSubmitEnd;
  }

  public void setProductName(String productName) {
    this.productName = productName != null ? StringUtil.trim(productName) : productName;
  }

  public void setImgTextStatus(String imgTextStatus) {
    this.imgTextStatus = imgTextStatus != null ? StringUtil.trim(imgTextStatus) : imgTextStatus;
  }

  public void setAreaId(Long areaId) {
    this.areaId = areaId;
  }
}
