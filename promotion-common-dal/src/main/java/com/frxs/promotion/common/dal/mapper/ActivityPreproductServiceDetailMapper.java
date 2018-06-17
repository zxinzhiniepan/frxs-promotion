/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.mapper;

import com.frxs.framework.data.persistent.SuperMapper;
import com.frxs.promotion.common.dal.entity.ActivityPreproductServiceDetail;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityPreproductServiceDetailMapper extends SuperMapper<ActivityPreproductServiceDetail> {

  Integer insertBatch(List<ActivityPreproductServiceDetail> list);
}