package DataBase;

import java.sql.*;

import Util.Config;

public class DBConnector {

    public Connection connection;
    public Connection transactionConnection;

    private static DBConnector instance;


    private DBConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.print("cannot load sql driver.");
            System.exit(1);
        }
    }

    /**
     * 创建一个连接实例
     * @return
     */
    public static DBConnector getInstance() {

        if (instance == null)
            instance = new DBConnector();
        return instance;
    }

    /**
     *  创建数据库连接
     * @param userName 用户名
     * @param password 密码
     * @throws SQLException
     */
    public void connectDataBase(
            String userName,
            String password){
        try {
            connection = DriverManager.getConnection(Config.DB_URL, userName, password);

            transactionConnection = DriverManager.getConnection(Config.DB_URL, userName, password);
            transactionConnection.setAutoCommit(false);

        } catch (SQLException e) {
            System.err.println("failed to connect to sql database");
            System.exit(0);
        }
    }

    public void connectDataBase() {
        try {
            connection = DriverManager.getConnection(Config.DB_URL, Config.DB_MASTER_NAME, Config.DB_MASTER_PW);
            transactionConnection = DriverManager.getConnection(Config.DB_URL, Config.DB_MASTER_NAME, Config.DB_MASTER_PW);
            transactionConnection.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("failed to connect to sql database");
            System.exit(0);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
            transactionConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
