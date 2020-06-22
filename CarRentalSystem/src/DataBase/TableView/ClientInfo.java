package DataBase.TableView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientInfo {
    StringProperty clientID;
    StringProperty name;
    StringProperty userID;
    StringProperty password;
    StringProperty credit;
    StringProperty isVIP;
    StringProperty Contact;

    public ClientInfo(String clientID, String name, String userID, String password, int credit, boolean isVIP, String contact) {
        this.clientID = new SimpleStringProperty(clientID);
        this.name = new SimpleStringProperty(name);
        this.userID = new SimpleStringProperty(userID);
        this.password = new SimpleStringProperty(password);
        this.credit = new SimpleStringProperty(String.valueOf(credit));
        this.isVIP = new SimpleStringProperty( isVIP ? "Yes" : "No");
        Contact = new SimpleStringProperty(contact);
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getUserID() {
        return userID.get();
    }

    public StringProperty userIDProperty() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID.set(userID);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getCredit() {
        return credit.get();
    }

    public StringProperty creditProperty() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit.set(credit);
    }

    public String getIsVIP() {
        return isVIP.get();
    }

    public StringProperty isVIPProperty() {
        return isVIP;
    }

    public void setIsVIP(String isVIP) {
        this.isVIP.set(isVIP);
    }

    public String getContact() {
        return Contact.get();
    }

    public StringProperty contactProperty() {
        return Contact;
    }

    public void setContact(String contact) {
        this.Contact.set(contact);
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "clientID=" + clientID +
                ", name=" + name +
                ", userID=" + userID +
                ", password=" + password +
                ", credit=" + credit +
                ", isVIP=" + isVIP +
                ", Contact=" + Contact +
                '}';
    }
}
