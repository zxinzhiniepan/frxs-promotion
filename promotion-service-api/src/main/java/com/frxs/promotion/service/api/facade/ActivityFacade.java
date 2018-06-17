package com.frxs.promotion.service.api.facade;

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
 * @version $Id: ActivityFacade.java,v 0.1 2018年01月24日 下午 19:02 $Exp
 */
public interface ActivityFacade {

  /**
   * 创建预售商品活动
   *
   * @param activity 活动信息
   * @param preproductList 预售活动商品列表
   */
  PromotionBaseResult createPreproductActivity(ActivityDto activity, List<PreproductDto> preproductList);

  /**
   * 修改预售商品活动
   * 一但活动是进行中（不管有没有审核）就不能移除活动中的商品，进行中的活动都只能修改商品：排序，限量，用户限量，市场价，是否直采，其他都不能修改
   *
   * @param activity 活动信息
   * @param preproductList 预售活动商品列表
   */
  PromotionBaseResult updatePreproductActivity(ActivityDto activity, List<PreproductDto> preproductList);

  /**
   * 修改预售活动商品
   * 进行中的活动都只能修改商品：排序，限量，用户限量，市场价，是否直采，其他都不能修改
   *
   * @param activity 活动
   * @param preproductList 预售活动商品列表
   */
  PromotionBaseResult updatePreproduct(ActivityDto activity, List<PreproductDto> preproductList);

  /**
   * 查询预售活动详情
   *
   * @param activityId 活动id
   * @return 活动信息和预售商品信息
   */
  PromotionBaseResult<ActivityDto> queryPreprocutActivityInfo(Long activityId);

  /**
   * 审核预售活动
   *
   * @param activit 活动:审核活动id，审核人id，审核人名称，状态（PENDING-待审核（反审核），PASS-审核通过，REJECT-驳回）
   * @return 审核结果
   */
  PromotionBaseResult auditPreproductActivity(ActivityDto activit);

  /**
   * 删除活动
   * 校验规则： 只能在活动购买时间开始前才能删除。
   */
  PromotionBaseResult delActivity(ActivityDelDto activityDelDto);

  /**
   * 分页查询活动信息
   *
   * @param query 查询条件
   * @param page 分页参数
   * @return 分页数据
   */
  PromotionBaseResult<Page<ActivityDto>> queryActivityPage(ActivityQueryDto query, Page<ActivityDto> page);
}
