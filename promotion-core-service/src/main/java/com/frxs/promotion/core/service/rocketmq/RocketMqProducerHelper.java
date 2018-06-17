/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.rocketmq;

import com.frxs.framework.integration.rocketmq.adapter.RocketMqProducerAdapter;
import com.frxs.framework.integration.rocketmq.message.ObjectMessage;
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import com.frxs.promotion.core.service.config.RocketmqConstant;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息生产者
 *
 * @author sh
 * @version $Id: RocketMqProducerHelper.java,v 0.1 2018年03月03日 上午 10:19 $Exp
 */
@Component
public class RocketMqProducerHelper {

  @Autowired
  private RocketMqProducerAdapter rocketMqProducerAdapter;

  @Autowired
  private IProductCacheTool productCacheTool;

  /**
   * 广播通知更新首页内存缓存
   *
   * @param areaIds 区域id列表
   */
  public void broadcastRemoveIndexPreproductMemory(Set<Long> areaIds) {
    //广播
    ObjectMessage message = new ObjectMessage(RocketmqConstant.INDEX_DATA_TOPIC, areaIds);
    rocketMqProducerAdapter.send(message, RocketmqConstant.PROMOTION_PRODUCER_INSTANCE);

    //修改redis缓存
    for (Long areaId : areaIds) {
      productCacheTool.buildActivityIndexProductCache(areaId, 0);
    }
  }
}
