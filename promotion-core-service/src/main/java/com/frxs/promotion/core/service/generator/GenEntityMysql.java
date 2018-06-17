package com.frxs.promotion.core.service.generator;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Colby.liu
 * @version $Id: GenEntityMysql.java,v 0.1 2018年01月24日 18:59 $Exp
 */
public class GenEntityMysql {

  private static final GenEntityMysql INSTANCE = new GenEntityMysql();

  /**
   * 表名
   */
  private String tableName;
  /**
   * 列名数组
   */
  private String[] colNames;
  /**
   * 列名类型数组
   */
  private String[] colTypes;
  private String[] comments;
  /**
   * 列名大小数组
   */
  private int[] colSizes;
  /**
   * 是否需要导入包java.util.*
   */
  private boolean needUtil = false;
  /**
   * 是否需要导入包java.sql.*
   */
  private boolean needSql = false;
  private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  /**
   * 数据库操作
   */
  private static final String SQL = "SELECT * FROM ";
  /**
   * 类TODO 需要修改的地方
   */
  private static final String URL = "jdbc:mysql://192.168.6.224:3306/frxs_promotion";
  private static final String NAME = "devo2o";
  private static final String PASS = "frxs@2017";
  private static final String DRIVER = "com.mysql.jdbc.Driver";
  /**
   * 指定实体生成所在包的路径
   */
  private String packageOutPath = "com.frxs.promotion.common.dal.entity";

  private String entityFilePath = "E:\\pojo";
  /**
   * 作者名字
   */
  private String authorName = "sh";

  /**
   * 类的构造方法
   */
  private GenEntityMysql() {
  }

  /**
   * @description 生成class的所有内容
   * @author paul
   * @date 2017年8月18日 下午5:30:07
   * @update 2017年8月18日 下午5:30:07
   * @version V1.0
   */
  private String parse() {
    StringBuffer sb = new StringBuffer();
    sb.append("package " + packageOutPath + ";\r\n");
    sb.append("\r\n");
    // 判断是否导入工具包
    if (needUtil) {
      sb.append("import java.util.Date;\r\n");
    }
    if (needSql) {
      sb.append("import java.sql.*;\r\n");
    }
    sb.append("import com.baomidou.mybatisplus.annotations.*;\r\n");
    sb.append("import java.io.Serializable;\r\n");
    sb.append("import com.frxs.framework.data.persistent.AbstractSuperEntity;\r\n");
    sb.append("import lombok.Data;\r\n");
    // 注释部分
    sb.append("/**\r\n");
    sb.append(" * table name:  " + tableName + "\r\n");
    sb.append(" * author name: " + authorName + "\r\n");
    sb.append(" * create time: " + SDF.format(new Date()) + "\r\n");
    sb.append(" */ \r\n");
    sb.append("@Data\r\n");
    sb.append("@TableName(\"" + tableName + "\")\r\n");
    // 实体部分
    String classNameStr = getTransStr(tableName, true);
    sb.append("public class " + classNameStr + " extends AbstractSuperEntity<" + classNameStr + "> " + "{\r\n\r\n");
    processAllAttrs(sb);
    sb.append("\r\n");
    //processAllMethod(sb);
    sb.append("}\r\n");

    File file = new File(entityFilePath + classNameStr + ".java");
    try {
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
      bufferedWriter.write(sb.toString());
      bufferedWriter.flush();
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  /**
   * @description 生成所有成员变量
   * @author paul
   * @date 2017年8月18日 下午5:15:04
   * @update 2017年8月18日 下午5:15:04
   * @version V1.0
   */
  private void processAllAttrs(StringBuffer sb) {

    for (int i = 0; i < colNames.length; i++) {
      if (colNames[i].equalsIgnoreCase("tmCreate") || colNames[i].equalsIgnoreCase("tmSmp")) {
        continue;
      }
      sb.append("\t/**\r\n");
      sb.append(" \t* " + comments[i] + "\r\n");
      sb.append(" \t*/ \r\n");
      if (i == 0) {
        sb.append("\t@TableId\r\n");
      }
      sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + getTransStr(colNames[i], false)
          + ";\r\n");
    }

    sb.append("\t@Override\n" +
        "\tprotected Serializable pkVal() {\n" +
        "\t\treturn this." + colNames[0] + ";\n" +
        "\t}");
  }

  /**
   * @description 生成所有get/set方法
   * @author paul
   * @date 2017年8月18日 下午5:14:47
   * @update 2017年8月18日 下午5:14:47
   * @version V1.0
   */
  private void processAllMethod(StringBuffer sb) {
    for (int i = 0; i < colNames.length; i++) {
      sb.append(
          "\tpublic void set" + getTransStr(colNames[i], true) + "(" + sqlType2JavaType(colTypes[i])
              + " "
              + getTransStr(colNames[i], false) + "){\r\n");
      sb.append(
          "\t\tthis." + getTransStr(colNames[i], false) + "=" + getTransStr(colNames[i], false)
              + ";\r\n");
      sb.append("\t}\r\n");
      sb.append(
          "\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + getTransStr(colNames[i], true)
              + "(){\r\n");
      sb.append("\t\treturn " + getTransStr(colNames[i], false) + ";\r\n");
      sb.append("\t}\r\n");
    }
  }

  /**
   * @param str 传入字符串
   * @description 将传入字符串的首字母转成大写
   * @author paul
   * @date 2017年8月18日 下午5:12:12
   * @update 2017年8月18日 下午5:12:12
   * @version V1.0
   */
  private String initCap(String str) {
    char[] ch = str.toCharArray();
    if (ch[0] >= 'a' && ch[0] <= 'z') {
      ch[0] = (char) (ch[0] - 32);
    }
    return new String(ch);
  }

  /**
   * @description 将mysql中表名和字段名转换成驼峰形式
   * @author paul
   * @date 2017年8月18日 下午4:55:07
   * @update 2017年8月18日 下午4:55:07
   * @version V1.0
   */
  private String getTransStr(String before, boolean firstChar2Upper) {
    //不带"_"的字符串,则直接首字母大写后返回
    if (!before.contains("_")) {
      return firstChar2Upper ? initCap(before) : before;
    }
    if (before.startsWith("t_")) {
      before = before.replaceFirst("t_", "");
    }
    String[] strs = before.split("_");
    StringBuffer after = null;
    if (firstChar2Upper) {
      after = new StringBuffer(initCap(strs[0]));
    } else {
      after = new StringBuffer(strs[0]);
    }
    for (int i = 1; i < strs.length; i++) {
      after.append(initCap(strs[i]));
    }
    return after.toString();
  }

  /**
   * @description 查找sql字段类型所对应的Java类型
   * @author paul
   * @date 2017年8月18日 下午4:55:41
   * @update 2017年8月18日 下午4:55:41
   * @version V1.0
   */
  private String sqlType2JavaType(String sqlType) {
    if (sqlType.equalsIgnoreCase("bit")) {
      return "Boolean";
    } else if (sqlType.equalsIgnoreCase("tinyint")) {
      return "Byte";
    } else if (sqlType.equalsIgnoreCase("smallint")) {
      return "Short";
    } else if (sqlType.equalsIgnoreCase("int")) {
      return "Integer";
    } else if (sqlType.equalsIgnoreCase("bigint")) {
      return "Long";
    } else if (sqlType.equalsIgnoreCase("float")) {
      return "Float";
    } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
        || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
        || sqlType.equalsIgnoreCase("smallmoney")) {
      return "BigDecimal";
          } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
        || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
        || sqlType.equalsIgnoreCase("text")) {
      return "String";
    } else if (sqlType.equalsIgnoreCase("datetime")) {
      return "Date";
    } else if (sqlType.equalsIgnoreCase("image")) {
      return "Blod";
    }
    return null;
  }

  /**
   * @description 生成方法
   * @author paul
   * @date 2017年8月18日 下午2:04:20
   * @update 2017年8月18日 下午2:04:20
   * @version V1.0
   */
  private void generate() throws Exception {
    //与数据库的连接
    Connection con;
    PreparedStatement pStemt = null;
    Class.forName(DRIVER);
    con = DriverManager.getConnection(URL, NAME, PASS);
    System.out.println("connect database success...");
    //获取数据库的元数据
    DatabaseMetaData db = con.getMetaData();
    //从元数据中获取到所有的表名
    ResultSet rs = db.getTables(null, null, null, new String[]{"TABLE"});
    String tableSql;
    PrintWriter pw = null;
    while (rs.next()) {
      tableName = rs.getString(3);
      tableSql = SQL + tableName;

      pStemt = con.prepareStatement(tableSql);
      ResultSetMetaData rsmd = pStemt.getMetaData();
      int size = rsmd.getColumnCount();
      colNames = new String[size];
      colTypes = new String[size];
      comments = new String[size];
      colSizes = new int[size];

      Statement stmt = con.createStatement();
      ResultSet rsSub = stmt.executeQuery("show full columns from " + tableName);
      int j = 0;
      while (rsSub.next()) {
        System.out.println(rsSub.getString("Field") + "\t:\t" + rsSub.getString("Comment"));
        comments[j] = rsSub.getString("Comment");
        j++;
      }
      //获取所需的信息
      for (int i = 0; i < size; i++) {
        colNames[i] = rsmd.getColumnName(i + 1);
        colTypes[i] = rsmd.getColumnTypeName(i + 1);
        if (colTypes[i].equalsIgnoreCase("datetime")) {
          needUtil = true;
        }
        if (colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text")) {
          needSql = true;
        }
        colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
      }
      //解析生成class的所有内容
      String content = parse();
      System.out.println(content);
      System.out.println("create class >>>>> " + tableName);
    }
    if (pw != null) {
      pw.close();
    }
  }

  /**
   * @description 执行方法
   * @author paul
   * @date 2017年8月18日 下午2:03:35
   * @update 2017年8月18日 下午2:03:35
   * @version V1.0
   */
  public static void main(String[] args) {
    try {
      INSTANCE.generate();
      System.out.println("generate classes success!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
