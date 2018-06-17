/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.common.DateUtil;
import com.frxs.promotion.core.service.service.ActivityPreproductService;
import com.frxs.promotion.core.service.service.ActivityService;
import com.frxs.promotion.service.PromotionApplication;
import com.frxs.promotion.service.api.dto.ActivityDto;
import com.frxs.promotion.service.api.dto.ActivityQueryDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.dto.PreproductImgDto;
import com.frxs.promotion.service.api.enums.ActivityTypeEnum;
import com.frxs.promotion.service.api.enums.AuditStatusEnum;
import com.frxs.promotion.service.api.enums.ImgTypeEnum;
import com.frxs.promotion.service.api.enums.SaleLimitTimeUnitEnum;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class ActivityTests {

  @Autowired
  ActivityService activityService;

  @Autowired
  JedisService jedisService;

  @Autowired
  private ActivityPreproductService activityPreproductService;


  @Test
  public void createRedisData() {
    //System.out.println(activityMapper.selectById(4));
    /*Long areaId = 32L;
    Long productId = 3L;
    PreproductDto preproductDto = createPreproductDto(areaId, productId);
    jedisService
        .set(CacheUtil.getCurAppCacheKey(CacheKeyEnum.AREA.getPrefix() + "_" + 1L, CacheKeyEnum.ACTIVITY.getPrefix() + "_" + areaId, CacheKeyEnum.PRODUCT.getPrefix() + "_" + productId), JSON.toJSONString(preproductDto));*/
  }

  private PreproductDto createPreproductDto(Long activityId, Long productId) {

    PreproductDto preproduct = new PreproductDto();
    preproduct.setProductId(productId);
    preproduct.setLimitQty(new BigDecimal("100"));
    preproduct.setUserLimitQty(new BigDecimal("2"));
    preproduct.setSaleAmt(new BigDecimal("11.23"));
    preproduct.setMarketAmt(new BigDecimal("12.11"));
    preproduct.setPerServiceAmt(new BigDecimal("3.56"));
    preproduct.setPerCommission(new BigDecimal("1.02"));
    preproduct.setDirectMining(Boolean.TRUE.toString());
    preproduct.setActivityId(activityId);
    preproduct.setSku("0000000" + productId);
    preproduct.setProductName("测试商品" + productId);
    preproduct.setProductTitle("测试商品" + productId + "的标题");
    preproduct.setDetailDesc("我是详情");
    preproduct.setBriefDesc("我是简介");
    preproduct.setVendorId(1L);
    preproduct.setVendorName("测试");
    preproduct.setYieldly("长沙");
    preproduct.setSupplyAmt(new BigDecimal("11.21"));
    preproduct.setPackageQty(new BigDecimal("11"));
    preproduct.setSaleLimitTime(new BigDecimal("2.4"));
    preproduct.setSaleLimitTimeUnit(SaleLimitTimeUnitEnum.DAY.getValueDefined());
    preproduct.setFollowQty(0L);
    preproduct.setUserLimitQty(new BigDecimal("2"));
    List<PreproductImgDto> imgs = new ArrayList<>();
    PreproductImgDto img1 = new PreproductImgDto();
    img1.setImgType(ImgTypeEnum.AD.getValueDefined());
    img1.setImgUrl60("60的图");
    PreproductImgDto img2 = new PreproductImgDto();
    img2.setImgType(ImgTypeEnum.PRIMARY.getValueDefined());
    img2.setImgUrl60("60的图");
    imgs.add(img1);
    imgs.add(img2);
    preproduct.setPrimaryImgs(imgs);
    return preproduct;
  }

  /**
   * 创建活动
   */
  @Test
  public void createActivity() {

    ActivityDto activityDto = new ActivityDto();
    activityDto.setActivityName("这个活动我测一下");
    activityDto.setTmBuyStart(DateUtil.addDays(new Date(), 1));
    activityDto.setAreaId(101L);
    activityDto.setTmBuyEnd(DateUtil.addDays(new Date(), 30));
    activityDto.setTmDisplayStart(DateUtil.addDays(new Date(), -1));
    activityDto.setTmDisplayEnd(DateUtil.addDays(new Date(), 30));
    activityDto.setActivityType(ActivityTypeEnum.SECKILL.getValueDefined());
    activityDto.setTmPickUp(DateUtil.addDays(new Date(), 31));
    activityDto.setCreateUserId(1L);
    activityDto.setCreateUserName("测试");

    List<PreproductDto> preproductDtoList = new ArrayList<>();
    PreproductDto preproductDto1 = new PreproductDto();
    preproductDto1.setProductId(88L);
    preproductDto1.setLimitQty(new BigDecimal(500));
    preproductDto1.setUserLimitQty(new BigDecimal(200));
    preproductDto1.setSaleAmt(new BigDecimal(33.95));
    preproductDto1.setMarketAmt(new BigDecimal(35.99));
    preproductDto1.setPerServiceAmt(new BigDecimal(2.16));
    preproductDto1.setPerCommission(new BigDecimal(1.97));
    preproductDto1.setDirectMining(Boolean.TRUE.toString());

    PreproductDto preproductDto2 = new PreproductDto();
    preproductDto2.setProductId(92L);
    preproductDto2.setLimitQty(new BigDecimal(100));
    preproductDto2.setUserLimitQty(new BigDecimal(2));
    preproductDto2.setSaleAmt(new BigDecimal(11.23));
    preproductDto2.setMarketAmt(new BigDecimal(12.11));
    preproductDto2.setPerServiceAmt(new BigDecimal(3.56));
    preproductDto2.setPerCommission(new BigDecimal(1.02));
    //preproductDto2.setDirectMining(Boolean.TRUE.toString());

    preproductDtoList.add(preproductDto1);
    //preproductDtoList.add(preproductDto2);
    PromotionBaseResult promotionBaseResult = activityService.createPreproductActivity(activityDto, preproductDtoList);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  /**
   * 修改活动
   */
  @Test
  public void updateActivity() {

    ActivityDto activityDto = new ActivityDto();
    activityDto.setActivityId(37L);
    activityDto.setActivityName("测试活动update");
    activityDto.setTmBuyStart(DateUtil.addDays(new Date(), 1));
    activityDto.setAreaId(1L);
    activityDto.setTmBuyEnd(DateUtil.addDays(new Date(), 2));
    activityDto.setTmDisplayStart(new Date());
    activityDto.setTmDisplayEnd(DateUtil.addDays(new Date(), 2));
    activityDto.setActivityType(ActivityTypeEnum.NORMAL.getValueDefined());
    activityDto.setTmPickUp(DateUtil.addDays(new Date(), 3));
    activityDto.setModifyUserId(1L);
    activityDto.setModifyUserName("测试");

    List<PreproductDto> preproductDtoList = new ArrayList<>();
    PreproductDto preproductDto1 = new PreproductDto();
    preproductDto1.setProductId(599L);
    preproductDto1.setLimitQty(new BigDecimal(100));
    preproductDto1.setUserLimitQty(new BigDecimal(2));
    preproductDto1.setSaleAmt(new BigDecimal(11.23));
    preproductDto1.setMarketAmt(new BigDecimal(12.11));
    preproductDto1.setPerServiceAmt(new BigDecimal(3.56));
    preproductDto1.setPerCommission(new BigDecimal(1.02));
    preproductDto1.setDirectMining(Boolean.TRUE.toString());

    PreproductDto preproductDto2 = new PreproductDto();
    preproductDto2.setProductId(66L);
    preproductDto2.setLimitQty(new BigDecimal(100));
    preproductDto2.setUserLimitQty(new BigDecimal(2));
    preproductDto2.setSaleAmt(new BigDecimal(11.23));
    preproductDto2.setMarketAmt(new BigDecimal(12.11));
    preproductDto2.setPerServiceAmt(new BigDecimal(3.56));
    preproductDto2.setPerCommission(new BigDecimal(1.02));
    //preproductDto2.setDirectMining(Boolean.TRUE.toString());

    preproductDtoList.add(preproductDto1);
    preproductDtoList.add(preproductDto2);
    PromotionBaseResult promotionBaseResult = activityService.updatePreproductActivity(activityDto, preproductDtoList);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void updatePreproduct() {

    ActivityDto activityDto = new ActivityDto();
    activityDto.setActivityId(37L);
    activityDto.setModifyUserId(1L);
    activityDto.setModifyUserName("测试");

    List<PreproductDto> preproductDtoList = new ArrayList<>();
    PreproductDto preproductDto1 = new PreproductDto();
    preproductDto1.setProductId(11L);
    preproductDto1.setLimitQty(new BigDecimal(100));
    preproductDto1.setUserLimitQty(new BigDecimal(2));
    preproductDto1.setSaleAmt(new BigDecimal(11.23));
    preproductDto1.setMarketAmt(new BigDecimal(12.11));
    preproductDto1.setPerServiceAmt(new BigDecimal(3.56));
    preproductDto1.setPerCommission(new BigDecimal(1.02));
    preproductDto1.setDirectMining(Boolean.TRUE.toString());

    PreproductDto preproductDto2 = new PreproductDto();
    preproductDto2.setProductId(12L);
    preproductDto2.setLimitQty(new BigDecimal(100));
    preproductDto2.setUserLimitQty(new BigDecimal(2));
    preproductDto2.setSaleAmt(new BigDecimal(11.23));
    preproductDto2.setMarketAmt(new BigDecimal(12.11));
    preproductDto2.setPerServiceAmt(new BigDecimal(3.56));
    preproductDto2.setPerCommission(new BigDecimal(1.02));
    //preproductDto2.setDirectMining(Boolean.TRUE.toString());

    preproductDtoList.add(preproductDto1);
    preproductDtoList.add(preproductDto2);
    PromotionBaseResult promotionBaseResult = activityService.updatePreproduct(activityDto, preproductDtoList);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void auditActivity() {

    ActivityDto activityDto = new ActivityDto();
    activityDto.setActivityId(42L);
    activityDto.setAuditUserId(22L);
    activityDto.setAuditUserName("审核人");
    activityDto.setStatus(AuditStatusEnum.PENDING.getValueDefined());
    PromotionBaseResult promotionBaseResult = activityService.auditPreproductActivity(activityDto);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));

    /*try {
      Thread.sleep(Integer.MAX_VALUE);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }*/
  }

  @Test
  public void queryAcitivtyInfo() {
    PromotionBaseResult promotionBaseResult = activityService.queryPreprocutActivityInfo(43L);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void queryAcitivtyPage() {
    ActivityQueryDto query = new ActivityQueryDto();
    Page<ActivityDto> page = new Page<>();
    page.setCurrent(2);
    page.setSize(5);
    query.setProductName("   b   ");
    PromotionBaseResult promotionBaseResult = activityService.queryActivityPage(query, page);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void checkProductInActivity() {
    List<Long> list = new ArrayList<>();
    list.add(1L);
    list.add(42L);
    list.add(41L);
    PromotionBaseResult<List<PreproductDto>> result = activityPreproductService.checkProductInActivity(list);
    System.err.println("结果：" + JSON.toJSONString(result));
  }

}
