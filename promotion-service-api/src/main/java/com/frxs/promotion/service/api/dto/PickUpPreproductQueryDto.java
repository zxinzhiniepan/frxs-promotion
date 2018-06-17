/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 按提货时间查询区域商品
 *
 * @author sh
 * @version $Id: PickUpPreproductQueryDto.java,v 0.1 2018年04月28日 上午 11:26 $Exp
 */
@Data
public class PickUpPreproductQueryDto implements Serializable {

  private static final long serialVersionUID = -382663999922088285L;

  /**
   * 提货时间
   */
  private Date tmPickUp;

  /**
   * 区域id
   */
  private Long areaId;
}
