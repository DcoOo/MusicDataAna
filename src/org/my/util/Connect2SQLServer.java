package org.my.util;

import java.sql.*;

/**
 * Created by Administrator on 2016/4/22.
 */
public class Connect2SQLServer {
    /**IP:127.0.0.1  TCP：1433*/
    public static String connectionURL = "jdbc:sqlserver://127.0.0.1:1433;databaseName=学生数据库;";
    /**使用SQL账号密码连接
     * 账号：sa
     * 密码：123456*/
    public static String URL = "jdbc:sqlserver://127.0.0.1:1433;databaseName=学生数据库;user=sa;password=123456;";
    public static void main(String[] args){
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Loaded SQLServer Driver");
            con = DriverManager.getConnection(connectionURL,"sa","123456");
            System.out.println("Connected!");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
