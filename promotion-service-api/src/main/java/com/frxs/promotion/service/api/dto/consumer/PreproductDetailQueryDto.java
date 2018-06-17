/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto.consumer;

import java.io.Serializable;
import lombok.Data;

/**
 * 详情查询参数
 *
 * @author sh
 * @version $Id: PreproductDetailQueryDto.java,v 0.1 2018年02月11日 下午 15:06 $Exp
 */
@Data
public class PreproductDetailQueryDto implements Serializable {

  private static final long serialVersionUID = -4478096202791648721L;
  /**
   * 商品id
   */
  private Long productId;
  /**
   * 活动id
   */
  private Long activityId;
  /**
   * 登录用户id
   */
  private Long userId;
  /**
   * 门店id
   */
  private Long storeId;
}
