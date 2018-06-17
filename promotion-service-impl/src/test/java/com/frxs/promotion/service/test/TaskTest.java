/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.test;

import com.alibaba.fastjson.JSON;
import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.promotion.common.dal.entity.Activity;
import com.frxs.promotion.common.dal.mapper.ActivityPreproductMapper;
import com.frxs.promotion.core.service.cache.CacheUtil;
import com.frxs.promotion.core.service.cache.IProductCacheTool;
import com.frxs.promotion.core.service.cmmon.ConsumerCommonService;
import com.frxs.promotion.core.service.pojo.SmsCodePojo;
import com.frxs.promotion.core.service.service.PromotionTaskService;
import com.frxs.promotion.service.PromotionApplication;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 定时任务测试
 *
 * @author sh
 * @version $Id: TaskTest.java,v 0.1 2018年02月11日 上午 9:09 $Exp
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PromotionApplication.class)
public class TaskTest {

  @Autowired
  private PromotionTaskService promotionTaskService;

  @Autowired
  private IProductCacheTool productCacheTool;

  @Autowired
  private ActivityPreproductMapper activityPreproductMapper;

  @Autowired
  private JedisService jedisService;

  @Autowired
  private ConsumerCommonService consumerCommonService;

  @Test
  public void saleQtyTest() {
    String json = "{\"codes\":[\"8268\",\"4727\"],\"num\":8,\"tmEnd\":\"2018-04-22 19:12:13\",\"tmSend\":\"2018-04-21 19:10:14\"}";
    jedisService.setex(CacheUtil.getSmsFrequencyCacheKey("18273144132", "LOGIN"), 2000, json);
  }

  @Test
  public void updateIndexCache() {
    List<Long> areaIds = new ArrayList<>();
    areaIds.add(4L);
    areaIds.add(1L);
    areaIds.add(2L);
    //cacheTool.broadcastRemoveIndexPreproductMemory(areaIds);
    productCacheTool.buildActivityIndexProductCache(4L, 0);
  }

  @Test
  public void synThumbsupQty() {
    PromotionBaseResult result = promotionTaskService.synThumbsupQty();
    System.err.println("结果：" + JSON.toJSONString(result));
  }

  @Test
  public void synFollowQty() {
    PromotionBaseResult result = promotionTaskService.synFollowQty();
    System.err.println("结果：" + JSON.toJSONString(result));
  }

  @Test
  public void synSaleQty() {

    /*List<ActivityPreproduct> preproducts = new ArrayList<>();
    ActivityPreproduct p1 = new ActivityPreproduct();
    p1.setSku("18020501901901");
    p1.setActivityId(94L);
    p1.setSaleQty(new BigDecimal("22"));
    ActivityPreproduct p2 = new ActivityPreproduct();
    p2.setSku("18020501901902");
    p2.setActivityId(94L);
    p2.setSaleQty(new BigDecimal("11"));
    preproducts.add(p1);
    preproducts.add(p2);
    activityPreproductMapper.updateSaleQtyBatch(preproducts);*/
    PromotionBaseResult result = promotionTaskService.synSaleQty();
    System.err.println("结果：" + JSON.toJSONString(result));
  }

  @Test
  public void indexData() {

    List<Activity> activityList = consumerCommonService.querySpecialDayActivitys(null, 0);
    if (activityList != null) {
      List<Long> areaIds = activityList.stream().map(Activity::getAreaId).distinct().collect(Collectors.toList());
      LogUtil.debug("定时任务将首页，详情，商品订单所需数据从数据库更新到redis缓存");

      if (!areaIds.isEmpty()) {
        Date now = new Date();
        for (Long areaId : areaIds) {
          try {
            List<IndexPreproductDto> indexPreproducts = productCacheTool.buildActivityIndexProductCache(areaId, 0);
            //设置首页缓存：有效期为一天
            String indexKey = CacheUtil.getIndexCacheKey(areaId, now);
            //更新redis缓存
            jedisService.setex(indexKey, CacheUtil.EXPIRE, JSON.toJSONString(indexPreproducts));

            //更新订单销量缓存
            productCacheTool.updateTomorrowTradeProductSaleQty(areaId, indexPreproducts);
          } catch (Exception e) {
            LogUtil.error(e, String.format("[IndexTomorrowDataToRedisJob:定时任务]定时任务将areaId=%s的首页，详情，商品订单所需数据从数据库更新到redis缓存异常", areaId));
          }
        }
      }
    }
  }
}
