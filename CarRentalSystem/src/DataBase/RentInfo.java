package DataBase;

import DataBase.Car.Car;
import DataBase.Client.Client;

import java.sql.Timestamp;

public class RentInfo {
    Car car;
    String clientID;
    String employeeID;
    Timestamp rentTime;

    public RentInfo(){}

    public RentInfo(Car car, String clientID, String employeeID, Timestamp rentTime) {
        this.car = car;
        this.clientID = clientID;
        this.employeeID = employeeID;
        this.rentTime = rentTime;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public Timestamp getRentTime() {
        return rentTime;
    }

    public void setRentTime(Timestamp rentTime) {
        this.rentTime = rentTime;
    }

    @Override
    public String toString() {
        return "RentInfo{" +
                "car=" + car +
                ", clientID='" + clientID + '\'' +
                ", employeeID='" + employeeID + '\'' +
                ", rentTime=" + rentTime +
                '}';
    }
}
