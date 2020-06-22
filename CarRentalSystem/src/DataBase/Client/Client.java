package DataBase.Client;

import DataBase.User.User;
import javafx.beans.property.StringProperty;

public class Client extends User {

    private String clientID;
    private String clientName;
    private String contact;
    private int credit;
    private boolean isVIP;

    public Client(){}

    public Client(String clientID, String clientName, String contact, int credit, boolean isVIP) {
        super();
        this.clientID = clientID;
        this.clientName = clientName;
        if (contact != null)
            this.contact = contact;
        this.credit = credit;
        this.isVIP = isVIP;
//        if (isVIP == '0')
//            this.isVIP = false;
//        else this.isVIP = true;
    }



    public Client(String userID, String password, String clientID, String employeeID, String authority, String clientID1, String clientName, String contact, int credit, boolean isVIP) {
        super(userID, password, clientID, employeeID, authority);
        this.clientID = clientID1;
        this.clientName = clientName;
        if (contact != null)
            this.contact = contact;
        this.credit = credit;
        this.isVIP = isVIP;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    public Client clone(){
        Client newobj = new Client(
                super.getUserID(),
                super.getPassword(),
                super.getClientID(),
                super.getEmployeeID(),
                super.getAuthority(),
                clientID,
                clientName,
                contact,
                credit,
                isVIP
        );
        return newobj;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientID='" + clientID + '\'' +
                ", clientName='" + clientName + '\'' +
                ", contact='" + contact + '\'' +
                ", credit=" + credit +
                ", isVIP=" + isVIP +
                "} " + super.toString();
    }
}
