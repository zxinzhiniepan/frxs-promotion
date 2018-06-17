package com.frxs.promotion.service.api.facade;

import com.baomidou.mybatisplus.plugins.Page;
import com.frxs.promotion.service.api.dto.AuditOnlineImgDto;
import com.frxs.promotion.service.api.dto.OnlineImgDto;
import com.frxs.promotion.service.api.dto.OnlineImgtextDto;
import com.frxs.promotion.service.api.dto.OnlineImgtextQueryDto;
import com.frxs.promotion.service.api.dto.OnlineTextDto;
import com.frxs.promotion.service.api.dto.VendorOnlineImgtextQueryDto;
import com.frxs.promotion.service.api.result.PromotionBaseResult;
import java.util.List;

/**
 * 图文直播接口
 *
 * @author sh
 * @version $Id: OnlineImgtextFacade.java,v 0.1 2018年01月25日 下午 15:05 $Exp
 */
public interface OnlineImgtextFacade {

  /**
   * 创建图文直播
   *
   * @param onlineImgtext 图文直播标题，商品信息和供应商信息
   * @param onlineText 文本内容
   * @param onlineImgList 图片列表（原图和压缩后的图片）
   */
  PromotionBaseResult createOnlineImgtext(OnlineImgtextDto onlineImgtext, OnlineTextDto onlineText,
      List<OnlineImgDto> onlineImgList);

  /**
   * 追加图片直播
   *
   * @param imgtextId 图文直播id
   * @param onlineText 文本内容
   * @param onlineImgList 图片列表（原图和压缩后的图片）
   */
  PromotionBaseResult appendOnlineImgtext(Long imgtextId, OnlineTextDto onlineText,
      List<OnlineImgDto> onlineImgList);


  /**
   * 查看图文直播详情
   *
   * @param imgtextId 图文直播id
   * @return 图文直播详情
   */
  PromotionBaseResult<OnlineImgtextDto> queryOnlineImgtextInfo(Long imgtextId);

  /**
   * 查询供应商端图文直播详情
   *
   * @param query 图文直播查询参数
   * @return 图文直播详情
   */
  PromotionBaseResult<OnlineImgtextDto> queryVendorOnlineImgtextInfo(VendorOnlineImgtextQueryDto query);

  /**
   * 删除图文直播 只有过期的才能删除
   *
   * @param onlineImgtextDtoList 图文直播list
   */
  PromotionBaseResult deleteOnlineImgtext(List<OnlineImgtextDto> onlineImgtextDtoList);

  /**
   * 区域端分页查询图文直播
   *
   * @param onlineImgtextQueryDto 查询条件
   * @param page 页码
   * @return 图文直播数量
   */
  PromotionBaseResult<Page<OnlineImgtextDto>> queryOnlineImgtextDtoList(OnlineImgtextQueryDto onlineImgtextQueryDto, Page<OnlineImgtextDto> page);

  /**
   * 图文直播审核
   *
   * @param auditOnlineImgDto 图文直播
   */
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
   * @param imgIds 图文直播图片id列表
   * @return 结果
   */
  PromotionBaseResult deleteOnlineImg(List<Long> imgIds);
}
