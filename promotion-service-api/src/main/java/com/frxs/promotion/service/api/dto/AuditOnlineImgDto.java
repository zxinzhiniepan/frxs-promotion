/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author fygu
 * @version $Id: AuditOnlineImgDto.java,v 0.1 2018年02月09日 13:07 $Exp
 */
@Data
public class AuditOnlineImgDto extends AbstractSuperDto implements Serializable {

  /**
   * 图文直播id
   */
  private Long imgtextId;

  /**
   * 图文图片明细id
   */
  private List<Long> imgIds;

  /**
   * 图文审核状态
   */
  private String imgTextAuditStatus;

}
