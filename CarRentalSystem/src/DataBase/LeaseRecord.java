package DataBase;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class LeaseRecord {
    private String actID;
    private String clientID;
    private String employeeID;
    private String carID;
    private String brand;
    private int Rent;
    private int Pledge;
    private LocalDateTime leaseDateTime;

    public LeaseRecord(){}

    public LeaseRecord(String actID, String clientID, String employeeID, String carID, String brand, int rent, int pledge, Timestamp leaseDateTime) {
        this.actID = actID;
        this.clientID = clientID;
        this.employeeID = employeeID;
        this.carID = carID;
        this.brand = brand;
        Rent = rent;
        Pledge = pledge;
        this.leaseDateTime = leaseDateTime.toLocalDateTime();
    }

    public String getActID() {
        return actID;
    }

    public void setActID(String actID) {
        this.actID = actID;
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

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getRent() {
        return Rent;
    }

    public void setRent(int rent) {
        Rent = rent;
    }

    public int getPledge() {
        return Pledge;
    }

    public void setPledge(int pledge) {
        Pledge = pledge;
    }

    public LocalDateTime getLeaseDateTime() {
        return leaseDateTime;
    }

    public void setLeaseDateTime(LocalDateTime leaseDateTime) {
        this.leaseDateTime = leaseDateTime;
    }
}
