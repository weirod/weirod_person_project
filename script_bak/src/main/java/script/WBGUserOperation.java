package script;

import com.csvreader.CsvReader;
import script.model.WBGUser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WBGUserOperation {

    // 定义一个CSV路径
    public static final String csvFilePath = "D://wbex_import-1.csv";
    public static final String WEB_USER_TABLE_NAME = "user_wbg";

    // 不同的数据库有不同的驱动
    public static final String driverName = "com.mysql.cj.jdbc.Driver";
    public static final String url = "jdbc:mysql://47.106.37.112:3306/exchange_user";
    public static final String user = "exchange";
    public static final String password = "OnxbO49eZjJjLj34rMTI";

    public static List<WBGUser> readCSV2List() {
        // 用来保存数据
        List<WBGUser> csvObjectList = new ArrayList();


        // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
        CsvReader reader = null;
        try {
            reader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
            // 跳过表头 如果需要表头的话，这句可以忽略
            reader.readHeaders();
            // 逐行读入除表头的数据
            while (reader.readRecord()) {
                WBGUser wbgUser = new WBGUser();
                wbgUser.setUserid(Long.valueOf(reader.get("userid")));
                wbgUser.setPassword(reader.get("password"));
                wbgUser.setUsername(reader.get("username"));
                wbgUser.setParentid(Long.valueOf(reader.get("parentid")));
                wbgUser.setReferral(reader.get("referral"));
                wbgUser.setWt( new BigDecimal(reader.get("wt")));
                csvObjectList.add(wbgUser);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            reader.close();
        }

        return csvObjectList;



    }


    public static void storage(Connection connection, String csvFilePath){

        PreparedStatement pstmt = null;
        try {

            //清空表
            pstmt = connection.prepareStatement(String.format("Truncate Table %s", WEB_USER_TABLE_NAME));
            pstmt.execute();
            System.out.println("清空临时表成功!!");

            pstmt = connection.prepareStatement(String.format("insert into %s (`userid`, `parentid`, `username`, `password`, `wt`, `referral`, `create_time`, `is_register`) values (?, ?, ?, ?, ?, ?, ?, 0)", WEB_USER_TABLE_NAME));
            // 遍历读取的CSV文件
            List<WBGUser> WBGUserList = readCSV2List();
            for (WBGUser wbgUser : WBGUserList) {
                pstmt.setLong(1, wbgUser.getUserid());
                pstmt.setLong(2,wbgUser.getParentid());
                pstmt.setString(3, wbgUser.getUsername());
                pstmt.setString(4, wbgUser.getPassword());
                pstmt.setBigDecimal(5, wbgUser.getWt());
                pstmt.setString(6, wbgUser.getReferral());
                pstmt.setTimestamp(7,  new Timestamp(System.currentTimeMillis()));
                pstmt.addBatch();
                System.out.println(wbgUser + ",加入批量新增中");
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public static Connection getConnection() {

        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("数据库连接成功..");

        return conn;
    }


    public static void main(String args[]){
        storage(getConnection(), csvFilePath);
    }
}
