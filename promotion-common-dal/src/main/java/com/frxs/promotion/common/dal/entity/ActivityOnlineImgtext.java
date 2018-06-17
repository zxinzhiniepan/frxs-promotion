package com.frxs.promotion.common.dal.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.frxs.framework.data.persistent.AbstractSuperEntity;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 图文直播
 * table name:  t_activity_online_imgtext
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */
@Data
@TableName("t_activity_online_imgtext")
public class ActivityOnlineImgtext extends AbstractSuperEntity<ActivityOnlineImgtext> {

  /**
   * 图文直播id
   */
  @TableId
  private Long imgtextId;
  /**
   * 预售活动id
   */
  private Long activityId;
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
   * 直采标题
   */
  private String imgTextTitle;
  /**
   * 提交时间
   */
  private Date tmSubmit;
  /**
   * 直采删除状态:FALSE-未删除,TRUE-已删除
   */
  private String delStatus;
  /**
   * 图文审核状态：PENDING-待审核,PASS-审核通过,REJECT-驳回,有一个图文未审核通过则为驳回状态
   */
  private String imgTextAuditStatus;
  /**
   * 审核人id
   */
  private Long auditUserId;
  /**
   * 审核人名
   */
  private String auditUserName;
  /**
   * 审核时间
   */
  private Date tmAudit;
  /**
   * 总计点赞人数
   */
  private Integer totalThumbsupQty;
  /**
   * 创建人id
   */
  private Long createUserId;
  /**
   * 创建人用户名
   */
  private String createUserName;
  /**
   * 修改人id
   */
  private Long modifyUserId;
  /**
   * 修改人用户名
   */
  private String modifyUserName;

  @Override
  protected Serializable pkVal() {
    return this.imgtextId;
  }
}