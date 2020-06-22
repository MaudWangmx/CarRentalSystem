package DataBase.Activity;

import DataBase.Client.Client;

import java.sql.Timestamp;

public class Acitivity {

    private String actID;
    private String event;
    private String clientID;
    private String carID;
    private String employeeID;
    private Timestamp date;
    private String relateActID;
    private String finID;
    private int amount;
    private String fintype;

    public Acitivity(String actID, String event, String clientID, String carID, String employeeID, Timestamp date, String relateActID, String finID, int amount, String fintype) {
        this.actID = actID;
        this.event = event;
        this.clientID = clientID;
        this.carID = carID;
        this.employeeID = employeeID;
        this.date = date;
        this.relateActID = relateActID;
        this.finID = finID;
        this.amount = amount;
        this.fintype = fintype;
    }

    public String getActID() {
        return actID;
    }

    public void setActID(String actID) {
        this.actID = actID;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getRelateActID() {
        return relateActID;
    }

    public void setRelateActID(String relateActID) {
        this.relateActID = relateActID;
    }

    public String getFinID() {
        return finID;
    }

    public void setFinID(String finID) {
        this.finID = finID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getFintype() {
        return fintype;
    }

    public void setFintype(String fintype) {
        this.fintype = fintype;
    }

    @Override
    public String toString() {
        return "Acitivity{" +
                "actID='" + actID + '\'' +
                ", event='" + event + '\'' +
                ", clientID='" + clientID + '\'' +
                ", carID='" + carID + '\'' +
                ", employeeID='" + employeeID + '\'' +
                ", date=" + date +
                ", relateActID='" + relateActID + '\'' +
                ", finID='" + finID + '\'' +
                ", amount=" + amount +
                ", fintype='" + fintype + '\'' +
                '}';
    }
}
