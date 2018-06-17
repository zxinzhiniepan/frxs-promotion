/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.util;

import java.util.Random;

/**
 * 字符串工具
 *
 * @author sh
 * @version $Id: StringTool.java,v 0.1 2018年03月17日 上午 10:27 $Exp
 */
public class StringTool {

  private static final String NUMBER_CHAR = "0123456789";

  /**
   * 随机数字串
   *
   * @param n 长度
   * @return 随机数字串
   */
  public static String getRandomNumberChar(int n) {

    StringBuffer sb = new StringBuffer();
    Random random = new Random();
    for (int i = 0; i < n; i++) {
      sb.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));
    }
    return sb.toString();
  }
}
