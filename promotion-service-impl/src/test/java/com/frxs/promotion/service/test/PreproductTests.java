/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.util.common.DateUtil;
import com.frxs.promotion.core.service.service.ActivityPreproductService;
import com.frxs.promotion.core.service.service.AreaPreproductQueryService;
import com.frxs.promotion.service.PromotionApplication;
import com.frxs.promotion.service.api.dto.ActivityPreproductQueryDto;
import com.frxs.promotion.service.api.dto.ActivityPreproductSortDto;
import com.frxs.promotion.service.api.dto.PickUpPreproductQueryDto;
import com.frxs.promotion.service.api.dto.PreproductDto;
import com.frxs.promotion.service.api.dto.VendorPreproductQueryDto;
import com.frxs.promotion.service.api.dto.consumer.PreproductDetailDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 活动商品测试类
 *
 * @author sh
 * @version $Id: ActivityTests.java,v 0.1 2018年02月05日 下午 13:42 $Exp
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PromotionApplication.class)
public class PreproductTests {

  @Autowired
  private ActivityPreproductService activityPreproductService;

  @Autowired
  private AreaPreproductQueryService areaPreproductQueryService;

  @Test
  public void queryVendorPreproduct() throws ParseException {
    Page<PreproductDto> page = new Page<>(1, 10);
    VendorPreproductQueryDto query = new VendorPreproductQueryDto();
    query.setVendorId(1L);
    Date date = DateUtil.parseDateNoTime("2018-02-21", DateUtil.DATA_FORMAT_YYYY_MM_DD);
    query.setTmBuyStart(date);
    query.setProductName("9");
    PromotionBaseResult<Page<PreproductDto>> promotionBaseResult = activityPreproductService.queryVendorPreproduct(query, page);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void findActivityPreproductSortDtoList() throws ParseException {
    ActivityPreproductQueryDto query = new ActivityPreproductQueryDto();
    query.setAreaId(101L);
    // query.setActivityStatus("0");
    PromotionBaseResult<List<ActivityPreproductSortDto>> promotionBaseResult = activityPreproductService.findActivityPreproductSortDtoList(query);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void updateDetailCache() throws ParseException {
    PreproductDetailDto detailDto = new PreproductDetailDto();
    detailDto.setPrId(133L);
    detailDto.setAreaId(101L);
    detailDto.setPrBrief("16的商品1简介");
    detailDto.setPrDetail("这个是详情");
    detailDto.setShTitle("标题");

    List<String> prList = new ArrayList<>();
    prList.add("http://frxs-devevn2018.oss-cn-shenzhen.aliyuncs.com/productMainImg/20180308/25903bd6-a922-4ce6-886b-7f1c23ba58a01520486763850.jpg");
    prList.add("http://frxs-devevn2018.oss-cn-shenzhen.aliyuncs.com/productMainImg/20180308/af9c00cf-2529-4310-bcd3-9fd657dbd1601520486763809.jpg");
    prList.add("http://frxs-devevn2018.oss-cn-shenzhen.aliyuncs.com/productMainImg/20180308/a0b703da-4daa-45cb-a60d-14b6be430ac01520486763778.jpg");

    detailDto.setPrimaryUrls(prList);
    PromotionBaseResult promotionBaseResult = activityPreproductService.updatePreproductDetailCache(detailDto);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void checkProductInActivity() {
    List<Long> list = new ArrayList<>();
    list.add(144L);
    PromotionBaseResult promotionBaseResult = activityPreproductService.checkProductInActivity(list);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void checkVendorHasPreproduct() {
    VendorPreproductQueryDto query = new VendorPreproductQueryDto();
    query.setVendorId(76880000000309L);
    query.setAreaId(104L);
    PromotionBaseResult promotionBaseResult = activityPreproductService.checkVendorHasPreproduct(query);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void queryStoreLinePreproduct() {

    PickUpPreproductQueryDto query = new PickUpPreproductQueryDto();
    query.setAreaId(101L);
    Date tmPickUp = DateUtil.addDays(new Date(), -1);
    query.setTmPickUp(tmPickUp);
    PromotionBaseResult promotionBaseResult = areaPreproductQueryService.queryStoreLinePreproduct(query);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }
}
