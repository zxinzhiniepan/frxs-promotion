package com.frxs.promotion.common.dal.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.frxs.framework.common.domain.Money;
import com.frxs.framework.data.persistent.AbstractSuperEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 预售商品服务费明细
 * table name:  t_activity_preproduct_service_detail
 * author name: sh
 * create time: 2018-01-25 13:34:11
 */
@Data
@TableName("t_activity_preproduct_service_detail")
public class ActivityPreproductServiceDetail extends
    AbstractSuperEntity<ActivityPreproductServiceDetail> {

  /**
   * 服务费用明细id
   */
  @TableId
  private Long serviceDetailId;
  /**
   * 预售商品id
   */
  private Long preproductId;
  /**
   * 平台服务费编码
   */
  private String serviceAmtCode;
  /**
   * 平台服务费值
   */
  @TableField("serviceAmt")
  private Money serviceAmt;
  /**
   * 平台服务费百分比
   */
  private BigDecimal serviceRate;
  /**
   * 描述
   */
  private String serviceDesc;
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
    return this.serviceDetailId;
  }
}