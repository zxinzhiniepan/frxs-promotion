/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.test;

import com.alibaba.fastjson.JSON;
import com.frxs.promotion.core.service.service.PreproductToConsumerService;
import com.frxs.promotion.core.service.service.PromotionTaskService;
import com.frxs.promotion.service.PromotionApplication;
import com.frxs.promotion.service.api.dto.consumer.IndexDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailQueryDto;
import com.frxs.promotion.service.api.dto.consumer.ThumbsupUserDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 活动测试类
 *
 * @author sh
 * @version $Id: ActivityTests.java,v 0.1 2018年02月05日 下午 13:42 $Exp
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PromotionApplication.class)
public class ConsumerTests {

  @Autowired
  private PreproductToConsumerService preproductToConsumerService;

  @Autowired
  private PromotionTaskService promotionTaskService;

  @Test
  public void queryIndexInfo() throws InterruptedException {

    PromotionBaseResult<IndexDto> result = preproductToConsumerService.queryIndexInfo(66880000000136L);
    preproductToConsumerService.queryIndexInfo(66880000000136L);
    System.err.println("结果：" + JSON.toJSONString(result));
    System.err.println("错误码：" + result.getErrorContext().fetchCurrentErrorCode());
    Thread.sleep(Integer.MAX_VALUE);
  }

  @Test
  public void queryPreproductInfo() {

    PreproductDetailQueryDto query = new PreproductDetailQueryDto();
    query.setActivityId(477L);
    query.setProductId(365L);
    query.setUserId(22L);
    query.setStoreId(1141L);
    PromotionBaseResult<PreproductDetailDto> result = preproductToConsumerService.queryPreproductInfo(query);
    System.err.println("结果：" + JSON.toJSONString(result));
  }

  @Test
  public void followPreproduct() {

    PromotionBaseResult<Long> result = preproductToConsumerService.followPreproduct(133L, 121L);
    System.err.println("结果：" + JSON.toJSONString(result));

    try {
      Thread.sleep(Integer.MAX_VALUE);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void thumbsupPreproduct() {

    //for (int i = 1; i < 2; i++) {
    ThumbsupUserDto thumbsupUserDto = new ThumbsupUserDto();
    thumbsupUserDto.setTextId(1L);
    thumbsupUserDto.setUserId((long) 4);
    thumbsupUserDto.setAvatar("头像" + 4);
    thumbsupUserDto.setWxName("微信名" + 4);
    PromotionBaseResult promotionBaseResult = preproductToConsumerService.thumbsupPreproduct(thumbsupUserDto);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
    //}

    try {
      Thread.sleep(Integer.MAX_VALUE);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
