/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.integration.client;

import com.alibaba.dubbo.config.annotation.Reference;
import com.frxs.framework.util.common.log4j.LogUtil;
import com.frxs.user.service.api.facade.UserFacade;
import com.frxs.user.service.api.result.UserResult;
import org.springframework.stereotype.Component;

/**
 * 用户dubbo服务
 *
 * @author sh
 * @version $Id: UserDubboProcess.java,v 0.1 2018年03月05日 下午 16:21 $Exp
 */
@Component
public class UserClient {

  @Reference(check = false, version = "1.0.0", timeout = 10000)
  private UserFacade userFacade;

  /**
   * 获取门店粉丝数
   *
   * @param storeId 门店id
   * @return 粉丝数
   */
  public long queryFansQty(Long storeId) {
    try {
      UserResult userResult = userFacade.getFansByStoreId(storeId);
      if (userResult.isSuccess()) {
        return Long.parseLong(userResult.getData() == null ? "0" : userResult.getData().toString());
      }
    } catch (Exception e) {
      LogUtil.error(e, "查询门店粉丝数失败");
    }
    return 0;
  }

}
