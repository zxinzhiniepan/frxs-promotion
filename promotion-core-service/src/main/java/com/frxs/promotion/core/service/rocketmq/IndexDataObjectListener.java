/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.rocketmq;

import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.integration.rocketmq.listener.AbstractMessageListenerConcurrently;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.core.service.cache.LruCacheHelper;
import java.util.List;
import java.util.Set;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 首页数据监听
 *
 * @author sh
 * @version $Id: IndexDataObjectListener.java,v 0.1 2018年01月07日 下午 14:45 $Exp
 */
@Service("indexDataObjectListener")
public class IndexDataObjectListener extends AbstractMessageListenerConcurrently {

  @Autowired
  protected JedisService jedisService;

  @Override
  public ConsumeConcurrentlyStatus execute(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

    try {

      MessageExt msg = msgs.get(0);
      Set<Long> areaIds = (Set<Long>) getObjectBody(msg.getBody());
      LogUtil.info("收到消息" + areaIds);
      if (areaIds != null && !areaIds.isEmpty()) {
        for (Long areaId : areaIds) {
          try {
            //清理内存缓存
            LruCacheHelper.removeIndexDataCache(areaId);
          } catch (Exception e) {
            LogUtil.error(e, String.format("[IndexDataObjectListener:首页数据监听]区域areaId=%s首页数据MQ消息同步异常", areaId));
          }
        }
      }
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    } catch (Exception e) {
      LogUtil.error(e, "[IndexDataObjectListener:首页数据监听]首页数据MQ消息同步异常");
    }
    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
  }
}
