/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.mapper;

import com.frxs.framework.data.persistent.SuperMapper;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImg;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityOnlineImgMapper extends SuperMapper<ActivityOnlineImg> {


  /**
   * 查询图文直播图片信息
   *
   * @return List<ActivityOnlineImg>
   */
  List<ActivityOnlineImg> selectByTextId(Long textId);

  /**
   * 更新不为空的字段
   * @param record 修改记录
   * @return 结果
   */
  int updateByPrimaryKeySelective(ActivityOnlineImg record);
}