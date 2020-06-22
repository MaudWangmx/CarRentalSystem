package DataBase.TableView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.swing.*;

public class EmployeeInfo {
    private StringProperty employeeID;
    private StringProperty name;
    private StringProperty userID;
    private StringProperty password;
    private StringProperty age;
    private StringProperty  authority;


    public EmployeeInfo(String employeeID, String name, String userID, String password, int age, String authority){
        this.employeeID = new SimpleStringProperty(employeeID);
        this.name = new SimpleStringProperty(name);
        this.userID = new SimpleStringProperty(userID);
        this.password = new SimpleStringProperty(password);
        this.age = new SimpleStringProperty(String.valueOf(age));
        this.authority = new SimpleStringProperty((authority.equals("1")) ? "Manager" : "Ordinary Employee");
    }

    public String getEmployeeID() {
        return employeeID.get();
    }

    public StringProperty employeeIDProperty() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID.set(employeeID);
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

    public String getAge() {
        return age.get();
    }

    public StringProperty ageProperty() {
        return age;
    }

    public void setAge(String age) {
        this.age.set(age);
    }

    public String getAuthority() {
        return authority.get();
    }

    public StringProperty authorityProperty() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority.set(authority);
    }

    @Override
    public String toString() {
        return "EmployeeInfo{" +
                "name=" + name +
                ", userID=" + userID +
                ", password=" + password +
                ", age=" + age +
                ", authority=" + authority +
                '}';
    }
}
