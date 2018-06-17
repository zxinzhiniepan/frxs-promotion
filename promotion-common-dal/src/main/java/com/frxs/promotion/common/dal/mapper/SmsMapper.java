/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.dal.mapper;

import com.frxs.framework.data.persistent.SuperMapper;
import com.frxs.promotion.common.dal.entity.Sms;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsMapper extends SuperMapper<Sms> {


 // List<Sms> findSmsByConditions(@Param("phoneNum") String phoneNum, @Param("startSendTime") Date startSendTime, @Param("endSendTime") Date endSendTime,
  //    @Param("smsType") String smsType,@Param("smsStatus") String smsStatus,@Param("areaId")Long areaId);

  List<Sms> findSmsByConditions(@Param("phoneNum") String phoneNum, @Param("startSendTime") Date startSendTime, @Param("endSendTime") Date endSendTime,
      @Param("smsType") String smsType, @Param("smsStatus") String smsStatus, @Param("areaId") Long areaId, @Param("pageSize") Integer pageSize, @Param("pageNo") Integer pageNo);

  int countSmsList(@Param("phoneNum") String phoneNum, @Param("startSendTime") Date startSendTime, @Param("endSendTime") Date endSendTime,
      @Param("smsType") String smsType, @Param("smsStatus") String smsStatus, @Param("areaId") Long areaId);


  List<Sms> smsFrequency(@Param("phoneNum") String phoneNum);

  /**
   * 修改参数中不为空的字段
   * @param record 修改参数
   * @return 结果
   */
  int updateByPrimaryKeySelective(Sms record);
}