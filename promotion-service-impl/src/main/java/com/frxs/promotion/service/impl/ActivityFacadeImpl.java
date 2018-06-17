/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.integration.dubbo.annotation.FrxsAutowired;
import com.frxs.promotion.core.service.service.ActivityService;
import com.frxs.promotion.service.api.dto.ActivityDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.dto.consumer.ActivityDelDto;
import com.frxs.promotion.service.api.facade.ActivityFacade;
import com.frxs.promotion.service.api.dto.ActivityQueryDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.List;


/**
 * 活动接口实现
 *
 * @author sh
 * @version $Id: ActivityFacadeImpl.java,v 0.1 2018年01月29日 上午 9:46 $Exp
 */
@Service(version = "1.0.0")
public class ActivityFacadeImpl implements ActivityFacade {

  @FrxsAutowired
  private ActivityService activityService;

  @Override
  public PromotionBaseResult createPreproductActivity(ActivityDto activity, List<PreproductDto> preproductList) {
    return activityService.createPreproductActivity(activity, preproductList);
  }

  @Override
  public PromotionBaseResult updatePreproductActivity(ActivityDto activity, List<PreproductDto> preproductList) {
    return activityService.updatePreproductActivity(activity, preproductList);
  }

  @Override
  public PromotionBaseResult updatePreproduct(ActivityDto activity, List<PreproductDto> preproductList) {
    return activityService.updatePreproduct(activity, preproductList);
  }

  @Override
  public PromotionBaseResult<ActivityDto> queryPreprocutActivityInfo(Long activityId) {
    return activityService.queryPreprocutActivityInfo(activityId);
  }

  @Override
  public PromotionBaseResult auditPreproductActivity(ActivityDto activit) {
    return activityService.auditPreproductActivity(activit);
  }
  

  /**
   * author：fygu
   *
   * @param activityDelDto 活动Id
   * @return PromotionBaseResult
   */
  @Override
  public PromotionBaseResult delActivity(ActivityDelDto activityDelDto) {
    return activityService.delActivity(activityDelDto);
  }

  @Override
  public PromotionBaseResult<Page<ActivityDto>> queryActivityPage(ActivityQueryDto query, Page<ActivityDto> page) {
    return activityService.queryActivityPage(query, page);
  }
}
