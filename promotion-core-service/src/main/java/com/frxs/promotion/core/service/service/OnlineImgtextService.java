package com.frxs.promotion.core.service.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.promotion.common.dal.enums.OperateUserType;
import com.frxs.promotion.service.api.dto.AuditOnlineImgDto;
import com.frxs.promotion.service.api.dto.OnlineImgDto;
import com.frxs.promotion.service.api.dto.OnlineImgtextDto;
import com.frxs.promotion.service.api.dto.OnlineImgtextQueryDto;
import com.frxs.promotion.service.api.dto.OnlineTextDto;
import com.frxs.promotion.service.api.dto.VendorOnlineImgtextQueryDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.List;

/**
 * @author fygu
 * @version $Id: OnlineImgtextService.java,v 0.1 2018年01月29日 16:13 $Exp
 */
public interface OnlineImgtextService {


  PromotionBaseResult createOnlineImgtext(OnlineImgtextDto onlineImgtext, OnlineTextDto onlineText, List<OnlineImgDto> onlineImgList);


  PromotionBaseResult appendOnlineImgtext(Long imgtextId, OnlineTextDto onlineText, List<OnlineImgDto> onlineImgList);


  PromotionBaseResult<OnlineImgtextDto> queryOnlineImgtextInfo(VendorOnlineImgtextQueryDto query, OperateUserType operateUserType);

  PromotionBaseResult deleteOnlineImgtext(List<OnlineImgtextDto> onlineImgtextDtoList);


  public PromotionBaseResult<Page<OnlineImgtextDto>> queryOnlineImgtextDtoList(
      OnlineImgtextQueryDto onlineImgtextQueryDto, Page<OnlineImgtextDto> page);

  PromotionBaseResult auditOnlineImg(AuditOnlineImgDto auditOnlineImgDto);

  /**
   * 查询供应商图文直播列表
   *
   * @param query 查询条件
   * @param page 分页参数
   * @return 结果集
   */
  PromotionBaseResult<Page<OnlineImgtextDto>> queryVendorOnlineImgtextList(VendorOnlineImgtextQueryDto query, Page<OnlineImgtextDto> page);

  /**
   * 删除图文直播图片
   *
   * @param imgIds 图片id列表
   * @return 结果
   */
  PromotionBaseResult deleteOnlineImg(List<Long> imgIds);
}
