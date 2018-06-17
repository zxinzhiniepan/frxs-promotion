package com.frxs.promotion.service.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 图文直播
 *
 * @author sh
 * @version $Id: OnlineImgtextDto.java,v 0.1 2018年01月24日 下午 17:23 $Exp
 */
@Data
public class OnlineImgtextDto extends AbstractSuperDto implements Serializable {

  private static final long serialVersionUID = 16463527115684574L;
  /**
   * 直采直播id
   */
  private Long imgtextId;
  /**
   * 商品名称
   */
  private String productName;
  /**
   * 活动id
   */
  private Long activityId;
  /**
   * 活动名称
   */
  private String activityName;
  /**
   * 商品id
   */
  private Long productId;
  /**
   * 供应商id
   */
  private Long vendorId;
  /**
   * 供应商编码
   */
  private String vendorCode;
  /**
   * 供应商名称
   */
  private String vendorName;
  /**
   * 直采直播标题
   */
  private String imgTextTitle;
  /**
   * 文本列表
   */
  private List<OnlineTextDto> onlineTextDTOs;
  /**
   * 图文直播状态：EXPIRED-已过期，DISPLAY-展示中，REJECT-驳回
   */
  private String imgTextStatus;
  /**
   * 提交时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmSubmit;

  /**
   * 购买开始时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyStart;
  /**
   * 购买结束时间
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date tmBuyEnd;
  /**
   * 点赞人数
   */
  private Integer totalThumbsupQty;
  /**
   * 审核人名
   */
  private String auditUserName;
  /**
   * 图文审核状态：PENDING-待审核,PASS-审核通过,REJECT-驳回,有一个图文未审核通过则为驳回状态
   */
  private String imgTextAuditStatus;
}
