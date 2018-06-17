/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.mapper;

import com.frxs.framework.data.persistent.SuperMapper;
import com.frxs.promotion.common.dal.entity.Activity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityMapper extends SuperMapper<Activity> {

  /**
   * 活动删除
   *
   * @param activityIdList 活动id
   * @param areaId 区域Id
   * @return int
   */
  int batchDelActivity(@Param("activityIdList") List<Long> activityIdList, @Param("areaId") Long areaId,
      @Param("operateId") Long operateId, @Param("operateName") String operateName);

  /**
   * 查询活动
   *
   * @param activityIdList 查询活动
   * @param areaId 区域Id
   * @return List<Activity>
   */
  List<Activity> selectByActivityId(@Param("activityIdList")List<Long> activityIdList, @Param("areaId")Long areaId);

  /**
   * 修改不为空的活动信息
   *
   * @param record 活动
   * @return 修改条数
   */
  int updateByPrimaryKeySelective(Activity record);

  /**
   * 查询活动数量
   *
   * @param map 查询条件
   * @return 总数量
   */
  int countActivity(@Param("query") Map<String, Object> map);

  /**
   * 分页查询活动
   *
   * @param limit 查询量
   * @param offset 起始行
   * @param map 查询参数
   * @return 活动数据
   */
  List<Activity> selectActivityPage(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("query") Map<String, Object> map);

  /**
   * 查询展示中的活动
   *
   * @param map 查询条件
   * @return 预售中的活动商品
   */
  List<Activity> selectSpecialDayActivitys(Map<String, Object> map);

}