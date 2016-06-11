package org.my.util;

import java.sql.*;

/**
 * Created by Administrator on 2016/5/10.
 */
public class DatabaseOperator {

    private String ipAddress = "";
    private int port;
    private String userName;
    private String passwd;
    private String dbName;
    private Connection connection;
    private Statement stat;
    private ResultSet resSet;
    public DatabaseOperator(){
    }

    public DatabaseOperator(String ipAddress,int port,String userName,String passwd,String dbName){
        this.ipAddress = ipAddress;
        this.port = port;
        this.userName = userName;
        this.passwd = passwd;
        this.dbName = dbName;
        //连接到SQL Server的MusicDataAna数据库
        connect2SQLServerDB(ipAddress,port,dbName,userName,passwd);

    }
    /**连接到SQL Server的MusicDataAna数据库
     * @param ipAddress 连接到SQL Server的IP地址
     * @param port 连接到SQL Server的端口号
     * @param dbName 连接到数据库的名称
     * @param userName 用户名
     * @param passwd 密码
     * */
    private void connect2SQLServerDB (String ipAddress,int port,String dbName,String userName,String passwd){
        String connectionURL = "jdbc:sqlserver://"+ ipAddress +":"+port+";databaseName="+dbName+";";
        String URL = "jdbc:sqlserver://"+ ipAddress +":"+port+";databaseName="+dbName+";"+"user="+userName+";password="+passwd+";";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Loaded SQLServer Driven");
            connection = DriverManager.getConnection(connectionURL,userName,passwd);
            System.out.println("Connect Success");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**参数传入变量名，通过封装实现自适应表的字段长度
     * @param tableNmae 所要插入的表的名字
     * @param itemsValue 所要插入的数据
     * */
    public void add2table(String tableNmae, TableItemValues itemsValue){
        String sql_lang = "INSERT INTO "+tableNmae+" values"+itemsValue.getItemValues()+";";
        try {
            stat = connection.createStatement();
            stat.execute(sql_lang);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromTable(String sql_lang){
        try {
            stat = connection.createStatement();
            stat.execute(sql_lang);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**执行更新表中的数据
     * @param sql_lang 所要执行的SQL语句
     * */
    public void updateTable(String sql_lang){
        try {
            stat = connection.createStatement();
            stat.execute(sql_lang);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**执行查询表格的语句
     * @param sql_lang 查询的语句
     * @return 返回值为查询到的结果集
     * */
    public ResultSet selectFromTable(String sql_lang){
        try {
            stat = connection.createStatement();
            resSet = stat.executeQuery(sql_lang);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }
}
