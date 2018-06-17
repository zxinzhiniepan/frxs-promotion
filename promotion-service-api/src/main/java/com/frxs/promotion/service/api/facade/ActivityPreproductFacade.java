package com.frxs.promotion.service.api.facade;

import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.promotion.service.api.dto.ActivityPreproductQueryDto;
import com.frxs.promotion.service.api.dto.ActivityPreproductSortDto;
import com.frxs.promotion.service.api.dto.ActivityQueryDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.dto.VendorPreproductQueryDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.List;

/**
 * 活动商品接口
 *
 * @author fygu
 * @version $Id: ActivityPreproductFacade.java,v 0.1 2018年01月29日 11:15 $Exp
 */
public interface ActivityPreproductFacade {


  /**
   * 商品显示顺序列表
   *
   * @param activityPreproductQueryDto 商品显示顺序调整查询条件
   * @return List<ActivityPreproductDto>
   */
  PromotionBaseResult<List<ActivityPreproductSortDto>> findActivityPreproductSortDtoList(ActivityPreproductQueryDto activityPreproductQueryDto);


  /**
   * 商品显示顺序列表
   *
   * @param reqList 预售活动商品
   */
  PromotionBaseResult updateActivityPreproductSort(List<ActivityPreproductSortDto> reqList);


  /**
   * 校验商品是否在活动中（进行中）
   *
   * @param productIds 商品Id
   * @return 有参于进行中活动的商品id
   */
  PromotionBaseResult<List<PreproductDto>> checkProductInActivity(List<Long> productIds);

  /**
   * 查询供应商图文直播的预售商品
   *
   * @param vendorId 供应商id
   * @return 预售商品列表
   */
  PromotionBaseResult<List<IndexPreproductDto>> queryVendorDisplayingPreproduct(Long vendorId);

  /**
   * 分页查询供应商活动商品（供应商罚款）
   *
   * @param query 查询条件
   * @param page 分页参数
   * @return 结果集
   */
  PromotionBaseResult<Page<PreproductDto>> queryVendorPreproduct(VendorPreproductQueryDto query, Page<PreproductDto> page);

  /**
   * 查询后台活动商品(活动商品导出)
   *
   * @param query 查询条件
   * @param page 分页参数
   * @return 结果集
   */
  PromotionBaseResult<Page<PreproductDto>> queryBossPreproduct(ActivityQueryDto query, Page<PreproductDto> page);

  /**
   * 更新商品详情缓存内容
   *
   * @param detail 详情
   * @return 结果
   */
  PromotionBaseResult updatePreproductDetailCache(PreproductDetailDto detail);

  /**
   * 校验供应商是否有关联图片商品
   *
   * @param query 供应商活动商品查询参数
   * @return 结果
   */
  PromotionBaseResult<Boolean> checkVendorHasPreproduct(VendorPreproductQueryDto query);
}
