/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.promotion.service.api.dto.ActivityDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.dto.consumer.ActivityDelDto;
import com.frxs.promotion.service.api.dto.ActivityQueryDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.List;

/**
 * 活动接口
 *
 * @author sh
 * @version $Id: ActivityService.java,v 0.1 2018年01月26日 上午 10:30 $Exp
 */
public interface ActivityService {

  /**
   * 创建预售活动
   *
   * @param activity 活动
   * @param preproductList 预售商品列表
   * @return 结果
   */
  PromotionBaseResult createPreproductActivity(ActivityDto activity, List<PreproductDto> preproductList);

  /**
   * 修改预售活动
   *
   * @param activity 活动
   * @param preproductList 预售商品列表
   * @return 结果
   */
  PromotionBaseResult updatePreproductActivity(ActivityDto activity, List<PreproductDto> preproductList);

  /**
   * 修改预售商品
   *
   * @param activity 活动
   * @param preproductList 预售商品列表
   * @return 结果
   */
  PromotionBaseResult updatePreproduct(ActivityDto activity, List<PreproductDto> preproductList);

  /**
   * 活动详情
   *
   * @param activityId 活动id
   * @return 活动详情
   */
  PromotionBaseResult<ActivityDto> queryPreprocutActivityInfo(Long activityId);

  /**
   * 活动审核
   *
   * @param activity 活动
   * @return 结果
   */
  PromotionBaseResult auditPreproductActivity(ActivityDto activity);

  /**
   * 删除活动
   *
   * @param activityDelDto 活动
   * @return 结果
   */
  PromotionBaseResult delActivity(ActivityDelDto activityDelDto);

  /**
   * 分页查询活动
   *
   * @param query 查询参数
   * @param page 分页参数
   * @return 活动列表
   */
  PromotionBaseResult<Page<ActivityDto>> queryActivityPage(ActivityQueryDto query, Page<ActivityDto> page);
}
