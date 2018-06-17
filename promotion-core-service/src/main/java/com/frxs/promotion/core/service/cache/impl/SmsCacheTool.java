/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.core.service.cache.impl;

import com.alibaba.fastjson.JSON;
import com.frxs.framework.integration.redis.JedisService;
import com.frxs.framework.util.common.DateUtil;
import com.frxs.framework.util.common.StringUtil;
import com.frxs.promotion.common.dal.enums.SmsFrequencyEnum;
import com.frxs.promotion.core.service.cache.CacheUtil;
import com.frxs.promotion.core.service.cache.ISmsCacheTool;
import com.frxs.promotion.core.service.pojo.SmsCodePojo;
import com.frxs.promotion.core.service.pojo.SmsPojo;
import com.frxs.promotion.service.api.dto.SmsDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 短信缓存实现
 *
 * @author sh
 * @version $Id: SmsCacheTool.java,v 0.1 2018年02月28日 下午 20:40 $Exp
 */
@Component
public class SmsCacheTool implements ISmsCacheTool {

  @Autowired
  private JedisService jedisService;

  @Value("${sms.valid.time}")
  private Integer validTime;

  @Override
  public void setSmsCodeValidateCache(SmsDto sms) {

    //锁
    String lockKey = CacheUtil.getSmsFrequencyLockCacheKey(sms.getPhoneNum(), sms.getSmsType());
    try {
      if (jedisService.incr(lockKey) == 1L) {
        //校验码
        SmsPojo smsPojo = new SmsPojo();
        smsPojo.setSmsId(sms.getSmsId());
        smsPojo.setCode(sms.getVerificationCode());
        jedisService.setex(CacheUtil.getSmsCodeValidateCacheKey(sms.getPhoneNum(), sms.getSmsType()), validTime, JSON.toJSONString(smsPojo));

        SmsCodePojo redisSms = getSmsCodeFrenquencyCache(sms.getPhoneNum(), sms.getSmsType());
        int expire = SmsFrequencyEnum.WENTY_FOUR_HOUR.getValueDefined();
        Date now = new Date();
        if (redisSms != null) {
          int num = redisSms.getNum();
          expire = (int) DateUtil.getDiffSeconds(redisSms.getTmEnd(), now);
          if (expire > 0) {
            num++;
            int diffTime = (int) DateUtil.getDiffSeconds(now, redisSms.getTmSend());
            List<String> codes = new ArrayList<>();
            if (diffTime <= SmsFrequencyEnum.FIVE_MINUTE.getValueDefined()) {
              codes = redisSms.getCodes();
              if (codes == null) {
                codes = new ArrayList<>();
              }
              codes.add(sms.getVerificationCode());
            } else {
              codes.add(sms.getVerificationCode());
              redisSms.setTmSend(now);
            }
            redisSms.setNum(num);
            redisSms.setCodes(codes);
          } else {
            redisSms = null;
          }
        }
        if (redisSms == null) {
          redisSms = new SmsCodePojo();
          List<String> codes = new ArrayList<>();
          codes.add(sms.getVerificationCode());
          Date tmEnd = DateUtil.addSeconds(now, expire);
          redisSms.setTmSend(now);
          redisSms.setTmEnd(tmEnd);
          redisSms.setCodes(codes);
          redisSms.setNum(1);
          expire = SmsFrequencyEnum.WENTY_FOUR_HOUR.getValueDefined();
        }
        jedisService.setex(CacheUtil.getSmsFrequencyCacheKey(sms.getPhoneNum(), sms.getSmsType()), expire, JSON.toJSONString(redisSms));
      }
    } finally {
      jedisService.del(lockKey);
    }
  }

  @Override
  public SmsPojo getSmsCodeValiateCache(String mobile, String smsType) {

    String json = jedisService.get(CacheUtil.getSmsCodeValidateCacheKey(mobile, smsType));
    if (StringUtil.isNotBlank(json)) {
      return JSON.parseObject(json, SmsPojo.class);
    }
    return null;
  }

  @Override
  public SmsCodePojo getSmsCodeFrenquencyCache(String mobile, String smsType) {
    String json = jedisService.get(CacheUtil.getSmsFrequencyCacheKey(mobile, smsType));
    if (StringUtil.isNotBlank(json)) {
      return JSON.parseObject(json, SmsCodePojo.class);
    }
    return null;
  }

  @Override
  public void removeSmsCodeValiateCache(String mobile, String smsType) {
    jedisService.del(CacheUtil.getSmsCodeValidateCacheKey(mobile, smsType));
  }
}
