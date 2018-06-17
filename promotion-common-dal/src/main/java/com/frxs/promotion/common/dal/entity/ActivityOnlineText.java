package com.frxs.promotion.common.dal.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.frxs.framework.data.persistent.AbstractSuperEntity;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 图文直播文本
 * table name:  t_activity_online_text
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */
@Data
@TableName("t_activity_online_text")
public class ActivityOnlineText extends AbstractSuperEntity<ActivityOnlineText> {

  /**
   * 文本内容id
   */
  @TableId
  private Long textId;
  /**
   * 图文id
   */
  private Long imgtextId;
  /**
   * 文字内容
   */
  private String textContent;
  /**
   * 点赞人数
   */
  private Integer thumbsupQty;
  /**
   * 发布时间
   */
  private Date tmPublish;
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
    return this.textId;
  }
}