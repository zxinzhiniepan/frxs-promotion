/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto;

import com.frxs.framework.util.common.StringUtil;
import java.io.Serializable;
import lombok.Data;
import lombok.Getter;

/**
 * 供应商图文直播查询条件
 *
 * @author sh
 * @version $Id: VendorOnlineImgtextQueryDto.java,v 0.1 2018年02月11日 上午 11:44 $Exp
 */
@Getter
public class VendorOnlineImgtextQueryDto implements Serializable {

  private static final long serialVersionUID = 1037513138068195836L;
  /**
   * 供应商id
   */
  private Long vendorId;
  /**
   * 图文直播状态：EXPIRED-已过期，DISPLAY-展示中，REJECT-驳回,PASS-审核通过
   */
  private String imgTextStatus;
  /**
   * 图文直播id
   */
  private Long imgtextId;

  public void setVendorId(Long vendorId) {
    this.vendorId = vendorId;
  }

  public void setImgTextStatus(String imgTextStatus) {
    this.imgTextStatus = imgTextStatus != null ? StringUtil.trim(imgTextStatus) : imgTextStatus;
  }

  public void setImgtextId(Long imgtextId) {
    this.imgtextId = imgtextId;
  }
}
