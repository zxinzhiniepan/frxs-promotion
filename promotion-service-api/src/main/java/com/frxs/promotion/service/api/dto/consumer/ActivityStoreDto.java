/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.dto.consumer;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 门店DTO
 *
 * @author sh
 * @version $Id: StoreDto.java,v 0.1 2018年02月07日 上午 9:48 $Exp
 */
@Data
public class ActivityStoreDto implements Serializable {

  private static final long serialVersionUID = -3514068702660802406L;
  /**
   * 门店id
   */
  private Long stId;
  /**
   * 编号
   */
  private String code;
  /**
   * 名称
   */
  private String name;
  /**
   * 简介
   */
  private String brief;
  /**
   * 粉丝数
   */
  private Long fansQty;
  /**
   * 购买指数
   */
  private BigDecimal buyIndex;
  /**
   * 门店头像
   */
  private String avator;
  /**
   * 区域id
   */
  private Long areaId;
  /**
   * 区域名称
   */
  private String arName;
}
