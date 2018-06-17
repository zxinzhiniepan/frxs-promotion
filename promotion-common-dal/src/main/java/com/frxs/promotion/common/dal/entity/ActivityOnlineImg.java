package com.frxs.promotion.common.dal.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.frxs.framework.data.persistent.AbstractSuperEntity;
import java.io.Serializable;
import lombok.Data;

/**
 * 图文直播图片
 * table name:  t_activity_online_img
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */
@Data
@TableName("t_activity_online_img")
public class ActivityOnlineImg extends AbstractSuperEntity<ActivityOnlineImg> {

  /**
   * 图文图片明细id
   */
  @TableId
  private Long imgId;
  /**
   * 图文id
   */
  private Long imgtextId;
  /**
   * 图文文本id
   */
  private Long textId;
  /**
   * 原图地址
   */
  private String originalImgUrl;
  /**
   * 60*60图片地址
   */
  private String imgUrl60;
  /**
   * 120*120图片地址
   */
  private String imgUrl120;
  /**
   * 200*200图片地址
   */
  private String imgUrl200;
  /**
   * 400*400图片地址
   */
  private String imgUrl400;
  /**
   * 状态:PENDING-待审核，PASS-审核通过,REJECT-驳回
   */
  private String imgStatus;
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
    return this.imgId;
  }
}