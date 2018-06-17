/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.biz.event.scheduled.impl;

import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.merchant.service.api.dto.AreaDto;
import com.frxs.promotion.biz.event.scheduled.IScheduled;
import com.frxs.promotion.common.integration.client.AreaClient;
import com.frxs.promotion.core.service.cache.LruCacheHelper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 门店粉丝数内存缓存10分钟清理一次
 *
 * @author sh
 * @version $Id: CleanFansQtyMemoryScheduled.java,v 0.1 2018年04月20日 下午 16:12 $Exp
 */
@Component
public class CleanFansQtyMemoryScheduled implements IScheduled {

  @Autowired
  private AreaClient areaClient;

  @Scheduled(cron = "0 0/10 * * * ?")
  @Override
  public void executeScheduled() {

    LogUtil.debug("清理门店粉丝数内存缓存");
    try {
      List<AreaDto> areaList = areaClient.queryAllArea();
      if (!areaList.isEmpty()) {
        for (AreaDto area : areaList) {
          try {
            LruCacheHelper.removeStoreFansQtyCache(area.getAreaId().longValue());
          } catch (Exception e) {
            LogUtil.error(e, String.format("[CleanFansQtyMemoryScheduled:定时任务]清理区域areaId=%s下的门店粉丝数内存缓存异常", area.getAreaId()));
          }
        }
      }
    } catch (Exception e) {
      LogUtil.error(e, "[CleanFansQtyMemoryScheduled:定时任务]清理门店粉丝数内存缓存异常");
    }
  }
}
