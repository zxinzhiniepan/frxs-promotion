/*
 * frxs Inc.  兴盛社区网络服务股份有限公司.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */

package com.frxs.promotion.common.integration.client.util;

import java.util.ArrayList;
import java.util.List;

/**
 * dubbo批量查询工具类，防止数量过大
 *
 * @author sh
 * @version $Id: ClientBatchQueryUtil.java,v 0.1 2018年04月25日 下午 12:42 $Exp
 */
public class ClientBatchQueryUtil {

  /**
   * 查询最大数量
   */
  public static final int LIMIT = 40;

  /**
   * 每次查询最大数量
   *
   * @param size size
   * @return 最大数量
   */
  public static int getMaxStep(int size) {
    return (size + LIMIT - 1) / LIMIT;
  }

  /**
   * 索引
   *
   * @param i 索引号
   * @param size 长度
   * @return 索引
   */
  public static int getMaxIndex(int i, int size) {
    int maxIndex = (i + 1) * LIMIT;
    return maxIndex > size ? size : maxIndex;
  }

  public static void main(String[] args) {

    int size = 80;
    List<Long> list = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      list.add((long) i);
    }

    for (int i = 0; i < getMaxStep(size); i++) {
      int maxIndex = getMaxIndex(i, size);
      System.out.println("第" + i + "批：" + list.subList(i * LIMIT, maxIndex));
    }
  }
}
