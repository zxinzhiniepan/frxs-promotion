/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.mapper;

import com.frxs.framework.data.persistent.SuperMapper;
import com.frxs.promotion.common.dal.entity.ActivityOnlineText;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityOnlineTextMapper extends SuperMapper<ActivityOnlineText> {

  /**
   * 查询图文直播文本信息
   *
   * @return List<ActivityOnlineText>
   */
  List<ActivityOnlineText> selectByImgtextId(Long imgtextId);

  /**
   * 批量更新点赞数量
   *
   * @param map map
   * @return 结果
   */
  int updateBatchThumbsupQty(@Param("map") Map<Long, Integer> map);
}