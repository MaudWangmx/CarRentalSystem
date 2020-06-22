package DataBase.TableView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ActInfo {
    StringProperty dateTime;
    StringProperty actID;
    StringProperty event;
    StringProperty carID;
    StringProperty optID;
    StringProperty clientID;
    StringProperty income;
    StringProperty outcome;

    public ActInfo(Timestamp dateTime, String actID, String event, String carID, String optID, String clientID, int income, int outcome){
        this.dateTime = new SimpleStringProperty((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dateTime));
        this.actID = new SimpleStringProperty(actID);
        String eventString;
        switch (Integer.valueOf(event)){
            case 0:
                eventString = "Lease";
                break;
            case 1:
                eventString = "Return";
                break;
            case 2:
                eventString = "Damage";
                break;
            case 3:
                eventString = "Traffic Fine";
                break;
            case 4:
                eventString = "Repair";
                break;
            default:
                eventString = "";
                break;
        }
        this.event = new SimpleStringProperty(eventString);
        this.carID = new SimpleStringProperty(carID);
        this.optID = new SimpleStringProperty(optID);
        this.clientID = new SimpleStringProperty(clientID);
        this.income = new SimpleStringProperty(Integer.toString(income));
        this.outcome = new SimpleStringProperty(Integer.toString(outcome));
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public StringProperty dateTimeProperty() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime.set(dateTime);
    }

    public String getActID() {
        return actID.get();
    }

    public StringProperty actIDProperty() {
        return actID;
    }

    public void setActID(String actID) {
        this.actID.set(actID);
    }

    public String getEvent() {
        return event.get();
    }

    public StringProperty eventProperty() {
        return event;
    }

    public void setEvent(String event) {
        this.event.set(event);
    }

    public String getCarID() {
        return carID.get();
    }

    public StringProperty carIDProperty() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID.set(carID);
    }

    public String getOptID() {
        return optID.get();
    }

    public StringProperty optIDProperty() {
        return optID;
    }

    public void setOptID(String optID) {
        this.optID.set(optID);
    }

    public String getClientID() {
        return clientID.get();
    }

    public StringProperty clientIDProperty() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID.set(clientID);
    }

    public String getIncome() {
        return income.get();
    }

    public StringProperty incomeProperty() {
        return income;
    }

    public void setIncome(String income) {
        this.income.set(income);
    }

    public String getOutcome() {
        return outcome.get();
    }

    public StringProperty outcomeProperty() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome.set(outcome);
    }

    @Override
    public String toString() {
        return "ActInfo{" +
                "dateTime=" + dateTime +
                ", actID=" + actID +
                ", event=" + event +
                ", carID=" + carID +
                ", optID=" + optID +
                ", clientID=" + clientID +
                ", income=" + income +
                ", outcome=" + outcome +
                '}';
    }
}
