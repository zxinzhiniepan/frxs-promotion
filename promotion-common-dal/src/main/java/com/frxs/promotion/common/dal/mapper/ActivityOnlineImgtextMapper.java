/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.mapper;

import com.frxs.framework.data.persistent.SuperMapper;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtext;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtextManage;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityOnlineImgtextMapper extends SuperMapper<ActivityOnlineImgtext> {


  int countActivityOnlineImgtextManageList(
      @Param("map") Map<String, Object> map);


  /**
   * 图文直播列表展示
   *
   * @return List<ActivityOnlineImgtextManage>
   */
  List<ActivityOnlineImgtextManage> findActivityOnlineImgtextManage(
      @Param("map") Map<String, Object> map
      , @Param("pageSize") Integer pageSize, @Param("pageNo") Integer pageNo);

  /**
   * 修改不属性值不为空的字段
   *
   * @param record 对象
   * @return 结果
   */
  int updateByPrimaryKeySelective(ActivityOnlineImgtext record);

  /**
   * 批量更新点赞数
   *
   * @param map map
   * @return 结果
   */
  int updateThumbsupQtyBatch(@Param("map") Map<Long, Integer> map);

  /**
   * 查询供应商图文直播数量
   *
   * @param map 查询条件
   * @return 数量
   */
  int countVendorImgtext(@Param("query") Map<String, Object> map);

  /**
   * 查询供应商图文直播数量
   *
   * @param limit limit
   * @param offset offset
   * @param map 查询条件
   * @return 列表
   */
  List<ActivityOnlineImgtext> selectVendorImgtextPage(@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("query") Map<String, Object> map);
}