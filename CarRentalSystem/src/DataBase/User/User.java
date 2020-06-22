package DataBase.User;

public class User {

    private String userID;
    private String password;
    private String clientID;
    private String employeeID;
    private String authority;

    public User(){}

    public User(String userID, String password, String clientID, String employeeID, String authority) {
        this.userID = userID;
        this.password = password;
        if (clientID != null)
            this.clientID = clientID;
        if (employeeID != null)
            this.employeeID = employeeID;
        this.authority = authority;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public User clone(){
        User newobj = new User(this.userID, this.password, this.clientID, this.employeeID, this.authority);
        return newobj;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", password='" + password + '\'' +
                ", clientID='" + clientID + '\'' +
                ", employeeID='" + employeeID + '\'' +
                ", authority='" + authority + '\'' +
                '}';
    }
}
