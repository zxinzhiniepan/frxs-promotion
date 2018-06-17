/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.promotion.common.dal.enums.OperateUserType;
import com.frxs.promotion.core.service.service.ActivityPreproductService;
import com.frxs.promotion.core.service.service.OnlineImgtextService;
import com.frxs.promotion.service.PromotionApplication;
import com.frxs.promotion.service.api.dto.AuditOnlineImgDto;
import com.frxs.promotion.service.api.dto.OnlineImgDto;
import com.frxs.promotion.service.api.dto.OnlineImgtextDto;
import com.frxs.promotion.service.api.dto.OnlineImgtextQueryDto;
import com.frxs.promotion.service.api.dto.OnlineTextDto;
import com.frxs.promotion.service.api.dto.VendorOnlineImgtextQueryDto;
import com.frxs.promotion.service.api.dto.consumer.IndexPreproductDto;
import com.frxs.promotion.service.api.enums.AuditStatusEnum;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PromotionApplication.class)
public class OnlineImgtextApplicationTests {

  @Autowired
  OnlineImgtextService onlineImgtextService;

  @Autowired
  private ActivityPreproductService activityPreproductService;

  @Test
  public void contextLoads() {
  }

  //创建图文直播
  @Test
  public void createOnlineImgtextTest() {
    OnlineImgtextDto onlineImgtext = new OnlineImgtextDto();
    onlineImgtext.setImgTextTitle("这个是标题!!!!!!!!");
    onlineImgtext.setProductId(54L);
    onlineImgtext.setActivityId(1L);
    onlineImgtext.setCreateUserId(55L);
    onlineImgtext.setCreateUserName("eeee");
    OnlineTextDto onlineText = new OnlineTextDto();
    String textContent = "测试数据";
    onlineText.setTextContent(textContent);
    List<OnlineImgDto> onlineImgList = new ArrayList<>();
    OnlineImgDto onlineImgDto = new OnlineImgDto();
    onlineImgDto.setImgUrl60("http://img4q.duitang.com/uploads/item/201407/25/20140725193427_4kAsu.jpeg");
    OnlineImgDto onlineImgDto1 = new OnlineImgDto();
    onlineImgDto1.setImgUrl60("http://img4q.duitang.com/uploads/item/201407/25/20140725193427_4kAsu.jpeg");
    OnlineImgDto onlineImgDto2 = new OnlineImgDto();
    onlineImgDto2.setImgUrl60("http://img4q.duitang.com/uploads/item/201407/25/20140725193427_4kAsu.jpeg");
    onlineImgList.add(onlineImgDto);
    onlineImgList.add(onlineImgDto1);
    onlineImgList.add(onlineImgDto2);
    PromotionBaseResult promotionBaseResult = onlineImgtextService.createOnlineImgtext(onlineImgtext, onlineText, onlineImgList);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  //追加图文直播文本信息
  @Test
  public void appendCreateOnlineImgtextTest() {
    Long imgtextId = 4L;
    OnlineTextDto onlineText = new OnlineTextDto();
    String textContent = "我这个是追加的数据,又来了哦!!!";
    onlineText.setTextContent(textContent);
    onlineText.setCreateUserId(55L);
    onlineText.setCreateUserName("eeee");
    List<OnlineImgDto> onlineImgList = new ArrayList<>();
    OnlineImgDto onlineImgDto = new OnlineImgDto();
    onlineImgDto.setImgUrl60("http://www.qq.com");
    onlineImgList.add(onlineImgDto);
    PromotionBaseResult promotionBaseResult = onlineImgtextService.appendOnlineImgtext(imgtextId, onlineText, onlineImgList);
    System.err.println("结果：" + JSON.toJSONString(promotionBaseResult));
  }

  //删除图文直播信息
  @Test
  public void deleteOnlineImgtextTest() {
    List<OnlineImgtextDto> onlineImgtextDtoList = new ArrayList<>();
    OnlineImgtextDto onlineImgtextDto = new OnlineImgtextDto();
    OnlineImgtextDto onlineImgtextDtoOne = new OnlineImgtextDto();
    onlineImgtextDto.setImgtextId(1L);
    onlineImgtextDto.setModifyUserId(11L);
    onlineImgtextDto.setModifyUserName("eeee");
    onlineImgtextDtoOne.setImgtextId(2L);
    onlineImgtextDtoOne.setModifyUserId(11L);
    onlineImgtextDtoOne.setModifyUserName("eeee");
    onlineImgtextDtoList.add(onlineImgtextDto);
//    onlineImgtextDtoList.add(onlineImgtextDtoOne);
    PromotionBaseResult promotionBaseResult = onlineImgtextService.deleteOnlineImgtext(onlineImgtextDtoList);
  }

  //预览
  @Test
  public void queryOnlineImgtextInfoTest() {
    VendorOnlineImgtextQueryDto query = new VendorOnlineImgtextQueryDto();
    query.setImgtextId(7L);
    PromotionBaseResult promotionBaseResult = onlineImgtextService.queryOnlineImgtextInfo(query, OperateUserType.VENDOR);
    System.err.println("结果" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void queryOnlineImgtextPage() {
    OnlineImgtextQueryDto query = new OnlineImgtextQueryDto();
    Page<OnlineImgtextDto> page = new Page<>(1, 3);
    PromotionBaseResult promotionBaseResult = onlineImgtextService.queryOnlineImgtextDtoList(query, page);
    System.err.println("结果" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void auditImg() {
    AuditOnlineImgDto onlineImgDto = new AuditOnlineImgDto();
    List<Long> imgIds = new ArrayList<>();
    imgIds.add(12L);
    imgIds.add(13L);
    onlineImgDto.setImgIds(imgIds);
    onlineImgDto.setImgtextId(7L);
    onlineImgDto.setImgTextAuditStatus(AuditStatusEnum.REJECT.getValueDefined());
    onlineImgDto.setModifyUserId(1L);
    onlineImgDto.setModifyUserName("审核");
    PromotionBaseResult promotionBaseResult = onlineImgtextService.auditOnlineImg(onlineImgDto);
    System.err.println("结果" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void queryVendorOnlineImgtextPage() {
    VendorOnlineImgtextQueryDto query = new VendorOnlineImgtextQueryDto();
    query.setVendorId(1L);
    query.setImgTextStatus(AuditStatusEnum.PASS.getValueDefined());
    Page<OnlineImgtextDto> page = new Page<>(1, 10);
    PromotionBaseResult promotionBaseResult = onlineImgtextService.queryVendorOnlineImgtextList(query, page);
    System.err.println("结果" + JSON.toJSONString(promotionBaseResult));
  }

  @Test
  public void queryVendorDisplayingPreproduct() {
    PromotionBaseResult<List<IndexPreproductDto>> promotionBaseResult = activityPreproductService.queryVendorDisplayingPreproduct(76880000000084L);
    System.err.println("结果" + JSON.toJSONString(promotionBaseResult));
  }
}
