/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cmmon;

import com.frxs.merchant.service.api.dto.ProductDto;
import com.frxs.merchant.service.api.dto.VendorDto;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.service.api.dto.consumer.AttrDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import java.util.List;
import java.util.Map;

/**
 * 公共service
 *
 * @author sh
 * @version $Id: ConsumerCommonService.java,v 0.1 2018年03月05日 上午 10:28 $Exp
 */
public interface ConsumerCommonService {

  /**
   * 查询指定天的活动列表
   *
   * @param queryMap 查询参数
   * @param diffDay 与当前时间比较相差天数
   * @return 活动列表
   */
  List<Activity> querySpecialDayActivitys(Map<String, Object> queryMap, int diffDay);

  /**
   * 查询指定天的活动商品列表
   *
   * @param queryMap 查询参数
   * @param diffDay 与当前时间比较相差天数
   * @return 活动商品列表
   */
  List<ActivityPreproduct> querySpecialDayPreproducts(Map<String, Object> queryMap, int diffDay);

  /**
   * 查询预售商品属性
   *
   * @param preproductId 预售商品id
   * @return 属性列表
   */
  List<AttrDto> getPreproductAttr(Long preproductId);

  /**
   * 构建首页商品数据
   *
   * @param preproduct 预售商品
   * @param activity 活动商品
   * @param productDto 商品信息
   * @param vendor 供应商信息
   * @return 首页商品
   */
  IndexPreproductDto buildIndexPreproduct(ActivityPreproduct preproduct, Activity activity, ProductDto productDto, VendorDto vendor);

  /**
   * 获取活动中的区域id列表
   *
   * @param queryMap 查询参数
   * @param diffDay 与当前时间比较相差天数
   * @return 活动中的区域id过列表
   */
  List<Long> getActivityAreaIds(Map<String, Object> queryMap, int diffDay);

  /**
   * 查询首页数据
   *
   * @param areaId 区域id
   * @return 首页数据
   */
  List<IndexPreproductDto> queryIndexPreproduct(Long areaId);
}
