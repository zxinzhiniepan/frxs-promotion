package com.frxs.promotion.service.api.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 图文直播图片
 * table name:  t_activity_online_img
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */
@Data
public class OnlineImgDto extends AbstractSuperDto implements Serializable {

  private static final long serialVersionUID = 1732694323901767674L;
  /**
   * 图文图片明细id
   */
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
}