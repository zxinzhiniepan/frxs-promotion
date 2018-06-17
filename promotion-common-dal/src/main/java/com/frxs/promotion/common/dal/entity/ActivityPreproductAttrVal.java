package com.frxs.promotion.common.dal.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.frxs.framework.data.persistent.AbstractSuperEntity;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品属性值
 * table name:  t_activity_preproduct_attr_val
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */
@Data
@TableName("t_activity_preproduct_attr_val")
public class ActivityPreproductAttrVal extends AbstractSuperEntity<ActivityPreproductAttrVal>{

  /**
   * 预售商品属性关联表
   */
  @TableId
  private Long preproductAttrValId;
  /**
   * 预售商品id
   */
  private Long preproductId;
  /**
   * 属性id
   */
  private Long attrId;
  /**
   * 属性名称
   */
  private String attrName;
  /**
   * 属性值id
   */
  private Long attrValId;
  /**
   * 属性值
   */
  private String attrVal;
  /**
   * 层级:从0开始
   */
  private Integer attrLevel;
  /**
   * 创建人id
   */
  private Long createUserId;
  /**
   * 创建人用户名
   */
  private String createUserName;

  @Override
  protected Serializable pkVal() {
    return this.preproductAttrValId;
  }
}