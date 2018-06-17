/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.service.test;

import com.frxs.framework.common.domain.Money;
import com.frxs.framework.util.common.DateUtil;
import com.frxs.promotion.service.api.dto.consumer.AttrDto;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sh
 * @version $Id: MainTest.java,v 0.1 2018年01月30日 下午 19:21 $Exp
 */
public class MainTest {

  public static void main(String[] args) throws ParseException {
    /*Money a = new Money(12.36);
    System.out.println(a.getAmount());

    List<Integer> list1 = new ArrayList<>();
    list1.add(1);
    list1.add(2);
    list1.add(3);
    List<Integer> list2 = new ArrayList<>();
    list2.add(1);
    list2.add(2);
    list2.add(3);
    list2.add(4);
    list2.add(5);

    //list2.removeAll(list1);
    Date date1 = DateUtil.parseDateNoTime(new Date(), DateUtil.NEW_FORMAT);
    date1 = DateUtil.addDays(date1, 1);
    Date date2 = DateUtil.parseDateNoTime(new Date(), DateUtil.NEW_FORMAT);
    System.out.println(date1.compareTo(date2));*/

   /* Date now = DateUtil.parseDateNewFormat("2017-02-08 00:56:59");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    System.out.println(DateUtil.format(calendar.getTime(),DateUtil.NEW_FORMAT));*/

    //System.out.println(new Date(1520230278000L));

    Date date = DateUtil.parseDateNoTime("2018-04-20 17:34:10", "yyyy-MM-dd HH:mm:ss");
    Date now = DateUtil.parseDateNoTime("2018-04-20 17:34:36", "yyyy-MM-dd HH:mm:ss");
    int expire = (int) (DateUtil.getDateBetween(date, now) / 1000);
    System.out.println(expire);
    int expire2 = (int) DateUtil.getDiffSeconds(now, date);
    System.out.println(expire2);
  }
}
