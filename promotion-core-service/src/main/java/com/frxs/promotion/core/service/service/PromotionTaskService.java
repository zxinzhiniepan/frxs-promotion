/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.service;

import com.frxs.promotion.service.api.result.PromotionBaseResult;

/**
 * 营销定时任务
 *
 * @author sh
 * @version $Id: PromotionTaskService.java,v 0.1 2018年02月08日 下午 19:51 $Exp
 */
public interface PromotionTaskService {

  /**
   * 同步已售量
   *
   * @return 结果
   */
  PromotionBaseResult synSaleQty();

  /**
   * 同频关注数
   */
  PromotionBaseResult synFollowQty();

  /**
   * 同步点赞数
   */
  PromotionBaseResult synThumbsupQty();

}
