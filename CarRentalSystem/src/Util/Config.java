package Util;

import DataBase.Employee.Employee;
import javafx.beans.property.StringProperty;

public class Config {

    public static String DB_URL = "jdbc:mysql://127.0.0.1:3306/car_rental?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
    public static String DB_MASTER_NAME = "root";
    public static String DB_MASTER_PW = "maud050092";
    public static String DB_NAME = "car_rental";

    public static final int RENT_MAX = 999999999;
    public static final int VIP_CREDIT = 400;


    public enum Identity{
        Employee,
        Client
    };

}
