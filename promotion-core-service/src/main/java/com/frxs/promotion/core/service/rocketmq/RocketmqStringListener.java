/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 *//*


package com.frxs.promotion.core.service.rocketmq;

import com.frxs.framework.integration.rocketmq.listener.AbstractMessageListenerConcurrently;
import com.frxs.framework.util.common.log4j.LogUtil;
import java.util.List;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

*/
/**
 *  测试消息String消费者监听器
 *
 * @author mingbo.tang
 * @version $Id: RocketmqStringListener.java,v 0.1 2018年01月07日 下午 14:45 $Exp
 *//*

@Service("rocketmqStringListener")
public class RocketmqStringListener extends AbstractMessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus execute(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        MessageExt msg = msgs.get(0);
        try {
            String message = getStringBody(msg.getBody());
            // do something
            LogUtil.info("String消息消费成功:{}",message);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            LogUtil.error("[order-core:下单]订单核心下单成功测试消息消费异常",e);
        }
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }
}
*/
