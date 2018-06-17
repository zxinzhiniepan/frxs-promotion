/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache;

import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import java.util.List;
import java.util.Map;
import org.springframework.scheduling.annotation.Async;

/**
 * 商品缓存工具
 *
 * @author sh
 * @version $Id: IProductCacheTool.java,v 0.1 2018年02月28日 下午 20:13 $Exp
 */
public interface IProductCacheTool {

  /**
   * 设置首页商品缓存数据:有效期为一天
   *
   * @param areaId 区域id
   * @param diffDay 相差天数，昨天-1，今天0，明天1
   * @return 首页待缓存的数据
   */
  List<IndexPreproductDto> buildActivityIndexProductCache(Long areaId, int diffDay);

  /**
   * 获取首页商品数据缓存
   *
   * @param areaId 区域id
   * @return 首页商品数据
   */
  List<IndexPreproductDto> getIndexPreproductCache(Long areaId);

  /**
   * 构建商品详情页缓存：无缓存则创建缓存，有则返回缓存数据:有效期为一天
   *
   * @param productId 商品详情页
   * @param productId activityId
   * @return 商品详情页缓存
   */
  PreproductDetailDto buildPreproductDetailCache(Long productId, Long activityId);

  /**
   * 关注商品:有效期为一天
   *
   * @param productId 商品id
   * @param activityId 活动id
   * @param followQty 关注人数
   * @return 关注数增加量
   */
  Long setPreproductFollowQty(Long productId, Long activityId, long followQty);

  /**
   * 异步设置所有商品的关注数:有效期为同步后清理
   *
   * @param activityId 活动id
   * @param productId 商品id
   * @param qty 数量
   */
  @Async
  void asyncSetAllPreproductFollowQtyCache(Long activityId, Long productId, Long qty);

  /**
   * 获取商品所有关注
   *
   * @param areaId 区域id
   * @return 商品所有关注
   */
  Map<String, String> getAllPreproductFollowQtyCache(Long areaId);

  /**
   * 删除商品所有关注数
   *
   * @param areaId 区域id
   */
  void removeAllPreproductFollowQtyCache(Long areaId);

  /**
   * 获取预售商品关注数缓存
   *
   * @param produtId 商品id
   * @param activityId 活动id
   * @return 关注数
   */
  long getPreproductFollowQtyCache(Long produtId, Long activityId);

  /**
   * 更新订单商品销量缓存
   *
   * @param areaId 区域id
   * @param tomorrowProducts 第二天的预售商品列表
   */
  void updateTomorrowTradeProductSaleQty(Long areaId, List<IndexPreproductDto> tomorrowProducts);

  /**
   * 根据区域id查询商品销量缓存
   *
   * @param areaId 区域id
   * @return 商品销量缓存
   */
  Map<String, String> getProductQtyCache(Long areaId);

  /**
   * 更新商品详情缓存
   *
   * @param detail 商品详情
   */
  void updatePreproductDetailCache(PreproductDetailDto detail);

  /**
   * 清理商品详情缓存
   *
   * @param productId 商品id
   * @param activityId 活动id
   */
  void removePreproductDetailCache(Long productId, Long activityId);
}
