package DataBase;

import DataBase.Client.Client;

import java.sql.Timestamp;

public class ReturnInfo {

    private LeaseRecord leaseRecord;
    private Client client;
    private String employeeID;
    private int totalRent;
    private int damageFine;
    private String condition;
    private Timestamp timestamp;
    private int trafficFineCount;
    private int trafficFine;


    public ReturnInfo(LeaseRecord leaseRecord, Client client, String employeeID, int totalRent, int damageFine, String condition, Timestamp timestamp, int trafficFineCount, int trafficFine) {
        this.leaseRecord = leaseRecord;
        this.client = client;
        this.employeeID = employeeID;
        this.totalRent = totalRent;
        this.damageFine = damageFine;
        this.condition = condition;
        this.timestamp = timestamp;
        this.trafficFineCount = trafficFineCount;
        this.trafficFine = trafficFine;
    }

    public ReturnInfo() { }

    public LeaseRecord getLeaseRecord() {
        return leaseRecord;
    }

    public void setLeaseRecord(LeaseRecord leaseRecord) {
        this.leaseRecord = leaseRecord;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public int getTotalRent() {
        return totalRent;
    }

    public void setTotalRent(int totalRent) {
        this.totalRent = totalRent;
    }

    public int getDamageFine() {
        return damageFine;
    }

    public void setDamageFine(int damageFine) {
        this.damageFine = damageFine;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getTrafficFineCount() {
        return trafficFineCount;
    }

    public void setTrafficFineCount(int trafficFineCount) {
        this.trafficFineCount = trafficFineCount;
    }

    public int getTrafficFine() {
        return trafficFine;
    }

    public void setTrafficFine(int trafficFine) {
        this.trafficFine = trafficFine;
    }

    @Override
    public String toString() {
        return "ReturnInfo{" +
                "leaseRecord=" + leaseRecord +
                ", client=" + client +
                ", employeeID='" + employeeID + '\'' +
                ", totalRent=" + totalRent +
                ", damageFine=" + damageFine +
                ", condition='" + condition + '\'' +
                ", timestamp=" + timestamp +
                ", trafficFineCount=" + trafficFineCount +
                ", trafficFine=" + trafficFine +
                '}';
    }
}
