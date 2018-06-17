/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.task;

import com.alibaba.dubbo.config.annotation.Service;
import com.frxs.framework.integration.dubbo.annotation.FrxsAutowired;
import com.frxs.promotion.core.service.service.PromotionTaskService;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import com.frxs.promotion.service.api.task.PromotionTaskFacade;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 营销活动定时任务实现
 *
 * @author sh
 * @version $Id: PromotionTaskFacadeImpl.java,v 0.1 2018年02月08日 下午 19:53 $Exp
 */
@Service(version = "1.0.0")
public class PromotionTaskFacadeImpl implements PromotionTaskFacade {

  @FrxsAutowired
  private PromotionTaskService promotionTaskService;

  @Override
  public PromotionBaseResult synSaleQty() {
    return promotionTaskService.synSaleQty();
  }

  @Override
  public PromotionBaseResult synFollowQty() {
    return promotionTaskService.synFollowQty();
  }

  @Override
  public PromotionBaseResult synThumbsupQty() {
    return promotionTaskService.synThumbsupQty();
  }
}
