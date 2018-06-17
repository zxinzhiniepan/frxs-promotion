/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.framework.integration.dubbo.annotation.FrxsAutowired;
import com.frxs.promotion.common.dal.enums.OperateUserType;
import com.frxs.promotion.core.service.service.OnlineImgtextService;
import com.frxs.promotion.service.api.dto.AuditOnlineImgDto;
import com.frxs.promotion.service.api.dto.OnlineImgDto;
import com.frxs.promotion.service.api.dto.OnlineImgtextDto;
import com.frxs.promotion.service.api.dto.OnlineImgtextQueryDto;
import com.frxs.promotion.service.api.dto.OnlineTextDto;
import com.frxs.promotion.service.api.dto.VendorOnlineImgtextQueryDto;
import com.frxs.promotion.service.api.facade.OnlineImgtextFacade;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.List;


/**
 * 图文直播接口实现
 *
 * @author liudiwei
 * @version $Id: OnlineImgtextFacadeImpl.java,v 0.1 2018年01月31日 上午 9:46 $Exp
 */
@Service(version = "1.0.0")
public class OnlineImgtextFacadeImpl implements OnlineImgtextFacade {

  @FrxsAutowired
  private OnlineImgtextService onlineImgtextService;

  @Override
  public PromotionBaseResult createOnlineImgtext(OnlineImgtextDto onlineImgtext, OnlineTextDto onlineText, List<OnlineImgDto> onlineImgList) {
    return onlineImgtextService.createOnlineImgtext(onlineImgtext, onlineText, onlineImgList);
  }

  @Override
  public PromotionBaseResult appendOnlineImgtext(Long imgtextId, OnlineTextDto onlineText, List<OnlineImgDto> onlineImgList) {
    return onlineImgtextService.appendOnlineImgtext(imgtextId, onlineText, onlineImgList);
  }

  @Override
  public PromotionBaseResult<OnlineImgtextDto> queryOnlineImgtextInfo(Long imgtextId) {

    VendorOnlineImgtextQueryDto query = new VendorOnlineImgtextQueryDto();
    query.setImgtextId(imgtextId);
    return onlineImgtextService.queryOnlineImgtextInfo(query, OperateUserType.AREA);
  }

  @Override
  public PromotionBaseResult<OnlineImgtextDto> queryVendorOnlineImgtextInfo(VendorOnlineImgtextQueryDto query) {
    return onlineImgtextService.queryOnlineImgtextInfo(query, OperateUserType.VENDOR);
  }

  @Override
  public PromotionBaseResult deleteOnlineImgtext(List<OnlineImgtextDto> onlineImgtextDtoList) {
    return onlineImgtextService.deleteOnlineImgtext(onlineImgtextDtoList);
  }


  @Override
  public PromotionBaseResult<Page<OnlineImgtextDto>> queryOnlineImgtextDtoList(OnlineImgtextQueryDto onlineImgtextQueryDto, Page<OnlineImgtextDto> page) {
    return onlineImgtextService.queryOnlineImgtextDtoList(onlineImgtextQueryDto, page);
  }

  @Override
  public PromotionBaseResult auditOnlineImg(AuditOnlineImgDto auditOnlineImgDto) {
    return onlineImgtextService.auditOnlineImg(auditOnlineImgDto);
  }

  @Override
  public PromotionBaseResult<Page<OnlineImgtextDto>> queryVendorOnlineImgtextList(VendorOnlineImgtextQueryDto query, Page<OnlineImgtextDto> page) {

    return onlineImgtextService.queryVendorOnlineImgtextList(query, page);
  }

  @Override
  public PromotionBaseResult deleteOnlineImg(List<Long> imgIds) {
    return onlineImgtextService.deleteOnlineImg(imgIds);
  }
}
