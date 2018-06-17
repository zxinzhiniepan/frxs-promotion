package com.frxs.promotion.common.dal.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.frxs.framework.data.persistent.AbstractSuperEntity;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 活动
 * table name:  t_activity
 * author name: sh
 * create time: 2018-01-24 20:38:59
 */
@Data
@TableName("t_activity")
public class Activity extends AbstractSuperEntity<Activity> {

  /**
   * 活动id
   */
  @TableId
  private Long activityId;
  /**
   * 区域id
   */
  private Long areaId;
  /**
   * 活动名称
   */
  private String activityName;
  /**
   * 活动类型：NORMAL-正常预售，SECKILL-秒杀
   */
  private String activityType;
  /**
   * 购买开始时间
   */
  private Date tmBuyStart;
  /**
   * 购买结束时间
   */
  private Date tmBuyEnd;
  /**
   * 显示开始时间
   */
  private Date tmDisplayStart;
  /**
   * 显示结束时间
   */
  private Date tmDisplayEnd;
  /**
   * 提货时间
   */
  private Date tmPickUp;
  /**
   * 活动状态：PENDING-待审核，PASS-审核通过，REJECT-驳回,DELETED-已删除
   */
  private String status;
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
    return this.activityId;
  }
}
