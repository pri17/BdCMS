package com.bidanet.bdcms.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by xuejike-pc on 2017/2/17.
 */
public class SqlServerTool {
    public static String localhostIpAndPost="127.0.0.1:80";
    public static String serverOldIpAddressAndPost="192.168.3.5:8090";
    private String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String URL = "jdbc:sqlserver://127.0.0.1\\MSSQLSERVER;databaseName=oper_check";
    private String USER = "admin";
    private String PWD ="zmkkm" ;


    public SqlServerTool(){}

    public  SqlServerTool(String DbName,String ip,String userName,String passWord){
        this.URL="jdbc:sqlserver://"+ip+"\\MSSQLSERVER;databaseName="+DbName;
        this.USER=userName;
        this.PWD=passWord;
    }

    public Connection getConnection(){
        Connection con = null;
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER,PWD);
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return con;
    }

}
