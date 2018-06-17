/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.api.task;

import com.frxs.promotion.service.api.result.PromotionBaseResult;

/**
 * 定时任务接口
 *
 * @author sh
 * @version $Id: PromotionTaskFacade.java,v 0.1 2018年02月08日 上午 8:51 $Exp
 */
public interface PromotionTaskFacade {

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
