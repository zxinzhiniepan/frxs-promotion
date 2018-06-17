package com.frxs.promotion.common.dal.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.frxs.framework.data.persistent.AbstractSuperEntity;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 图文直播点赞明细
 * table name:  t_activity_online_imgtext_thumbsup
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */
@Data
@TableName("t_activity_online_imgtext_thumbsup")
public class ActivityOnlineImgtextThumbsup extends AbstractSuperEntity<ActivityOnlineImgtextThumbsup> {

  /**
   * 点赞id
   */
  @TableId
  private Long thumbsupId;
  /**
   * 被点赞的图文文本id
   */
  private Long textId;
  /**
   * 点赞用户id
   */
  private Long userId;
  /**
   * 微信名称
   */
  private String wxName;
  /**
   * 点赞用户头像
   */
  private String userAvatar;
  /**
   * 点赞时间
   */
  private Date tmThumbsup;

  @Override
  protected Serializable pkVal() {
    return this.thumbsupId;
  }
}