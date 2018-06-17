/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.mapper;

import com.frxs.framework.data.persistent.SuperMapper;
import com.frxs.promotion.common.dal.entity.ActivityPreproduct;
import com.frxs.promotion.common.dal.entity.ActivityPreproductSort;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityPreproductMapper extends SuperMapper<ActivityPreproduct> {

  /**
   * 查询某个时间段下区域的预售商品
   *
   * @param map 查询条件
   * @return 预售中的活动商品
   */
  List<ActivityPreproduct> selectSpecialTimePreproducts(Map<String, Object> map);

  /**
   * 查询正在展示中的预售商品
   *
   * @param map 查询条件
   * @return 展示中的预售商品
   */
  List<ActivityPreproduct> selectDisplayingPreproducts(Map<String, Object> map);

  /**
   * 批量插入活动预售商品
   *
   * @param activityPreproductList 预售商品列表
   * @return 插入数量
   */
  Integer insertBatch(List<ActivityPreproduct> activityPreproductList);

  /**
   * 排序列表查询
   *
   * @param map map
   * @return List<ActivityPreproductSort>
   */
  List<ActivityPreproductSort> findActivityPreproductSortByConditions(Map<String, Object> map);

  /**
   * 修改商品显示顺序
   *
   * @param preproductId 预售活动商品id
   * @param sortSeq 商品排序序号
   * @return Integer
   */
  Integer updateActivityPreproductSort(@Param("preproductId") Long preproductId, @Param("sortSeq") Integer sortSeq);

  /**
   * 通过productId查找预售商品
   *
   * @param productIds 商品Id
   * @param areaId 区域Id
   */
  List<ActivityPreproduct> findActivityPreproductByProductId(@Param("productIds") List<Long> productIds, @Param("areaId") Long areaId);

  /**
   * 活动显示时间内是否存在同一个商品的活动配置
   *
   * @param map 查询参数
   * @return 商品列表
   */
  List<ActivityPreproduct> selectPeriodPreproducts(Map<String, Object> map);

  /**
   * 修改指定字段
   *
   * @param record 修改对象
   * @return 修改数量
   */
  int updateByPrimaryKeySelective(ActivityPreproduct record);

  /**
   * 查询正在进行中的商品
   *
   * @param map 商品id
   * @return 预售商品
   */
  List<ActivityPreproduct> selectProductInActivity(Map<String, Object> map);

  /**
   * 批量更新更新商品关注数
   *
   * @return 结果
   */
  int updateFollowQtyBatch(List<ActivityPreproduct> list);

  /**
   * 批量更新商品销量
   *
   * @param list 活动商品列表
   * @return 结果
   */
  int updateSaleQtyBatch(List<ActivityPreproduct> list);

  /**
   * 查询活动商品数量
   *
   * @param map 查询条件
   * @return 总数量
   */
  int countPreproduct(@Param("query") Map<String, Object> map);

  /**
   * 分页查询活动商品
   *
   * @param limit 查询量
   * @param offset 起始行
   * @param map 查询参数
   * @return 活动数据
   */
  List<ActivityPreproduct> selectPreproductPage(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("query") Map<String, Object> map);

  /**
   * 查询供应商待售的所有商品（在活动中的商品）
   *
   * @param vendorId 供应商id
   * @return 在活动中的商品
   */
  List<ActivityPreproduct> selectVendorForSaleProduct(Long vendorId);

  /**
   * 查询门店线路活动商品
   *
   * @param areaId 区域id
   * @param tmPickUp 提货时间
   * @return 门店线路活动商品
   */
  List<ActivityPreproduct> selectStoreLinePreproduct(@Param("areaId") Long areaId, @Param("tmPickUp") String tmPickUp);
}