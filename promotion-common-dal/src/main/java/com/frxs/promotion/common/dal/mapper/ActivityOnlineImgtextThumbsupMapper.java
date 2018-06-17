/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.mapper;

import com.frxs.framework.data.persistent.SuperMapper;
import com.frxs.promotion.common.dal.entity.ActivityOnlineImgtextThumbsup;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityOnlineImgtextThumbsupMapper extends SuperMapper<ActivityOnlineImgtextThumbsup> {

  /**
   * 批量新增
   *
   * @param list 列表
   * @return结果
   */
  int insertBatch(List<ActivityOnlineImgtextThumbsup> list);
}