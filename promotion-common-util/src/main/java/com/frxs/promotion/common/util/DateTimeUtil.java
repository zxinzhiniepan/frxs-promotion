/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具
 *
 * @author sh
 * @version $Id: DateTimeUtil.java,v 0.1 2018年02月27日 下午 18:04 $Exp
 */
public class DateTimeUtil {

  /**
   * 将时间转成 yyyy-MM-dd 23:59:59
   *
   * @param date 时间
   * @return 时间
   */
  public static Date getFullTimeEnd(Date date) {

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    return calendar.getTime();
  }

  /**
   * 将时间转成 yyyy-MM-dd 00:00:00
   *
   * @param date 时间
   * @return 时间
   */
  public static Date getFullTimeStart(Date date) {

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTime();
  }
}
