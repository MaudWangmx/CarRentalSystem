package DataBase.Employee;

import DataBase.User.User;

public class Employee extends User {
    private String employeeID;
    private String employeeName;
    private int age;

    public Employee(){}

    public Employee(String employeeID, String employeeName, int age) {
        super();
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.age = age;
    }

    public Employee(String userID, String password, String clientID, String employeeID, String authority, String employeeID1, String employeeName, int age) {
        super(userID, password, clientID, employeeID, authority);
        this.employeeID = employeeID1;
        this.employeeName = employeeName;
        this.age = age;
    }

    @Override
    public String getEmployeeID() {
        return employeeID;
    }

    @Override
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public Employee clone(){
        Employee newobj = new Employee(
                super.getUserID(),
                super.getPassword(),
                super.getClientID(),
                super.getEmployeeID(),
                super.getAuthority(),
                employeeID,
                employeeName,
                age);
        return newobj;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID='" + employeeID + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", age=" + age +
                "} " + super.toString();
    }
}
