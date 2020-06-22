package DataBase.Employee;

import DataBase.Client.Client;
import DataBase.DBConnector;
import DataBase.Exception.DeleteException;
import DataBase.Exception.UpdateException;
import javafx.beans.property.StringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeeDao {

    public static ResultSet getEmployeeUserInfoByID(String employeeID){
        ResultSet searchResult = null;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        try {
            String sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and Employee.employeeID = ?";
            PreparedStatement statement = instance.connection.prepareStatement(sql);
            statement.setString(1, employeeID);
            searchResult = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    public static String getRandomOperatorID(){
        ResultSet searchResult = null;
        int count;
        String ID = "";
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        try {
            String sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and authority = '2'";
            PreparedStatement statement = instance.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            searchResult = statement.executeQuery();
            searchResult.last();
            count = searchResult.getRow();
            searchResult.beforeFirst();
            System.out.println(count);
            int i = (int)( Math.random() * count + 1);
            System.out.println(i);
            for (; searchResult.next(); i--){
                if (i == 1) {
                    ID = searchResult.getString("Employee.employeeID");
                    System.out.println(searchResult.getString("employeeName"));
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            instance.closeConnection();
            return ID;
        }
    }

    public static ResultSet getEmployeeUserTable(String name, String employeeID, int authority){
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        ResultSet searchResult = null;
        PreparedStatement statement;
        String sql;
        try {
            switch (authority) {
                default:
                case 0: {
                    if (name.isEmpty() && employeeID.isEmpty()) {
                        sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID";
                        statement = instance.connection.prepareStatement(sql);
                    } else if (name.isEmpty() && !employeeID.isEmpty()) {
                        sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and Employee.employeeID like ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + employeeID + "%");
                    } else if (!name.isEmpty() && employeeID.isEmpty()) {
                        sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and employeeName like ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + name + "%");
                    } else {
                        sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and Employee.employeeID like ? and employeeName like ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + employeeID + "%");
                        statement.setString(2, "%" + name + "%");
                    }
                    break;
                }
                case 1:
                case 2: {
                    if (name.isEmpty() && employeeID.isEmpty()) {
                        sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and authority = ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, String.valueOf(authority));
                    } else if (name.isEmpty() && !employeeID.isEmpty()) {
                        sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and Employee.employeeID like ? and authority = ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + employeeID + "%");
                        statement.setString(2, String.valueOf(authority));
                    } else if (!name.isEmpty() && employeeID.isEmpty()) {
                        sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and employeeName like ? and authority = ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + name + "%");
                        statement.setString(2, String.valueOf(authority));
                    } else {
                        sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and Employee.employeeID like ? and employeeName like ? and authority = ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + employeeID + "%");
                        statement.setString(2, "%" + name + "%");
                        statement.setString(3, String.valueOf(authority));
                    }
                    break;
                }
            }
            System.out.println(statement);
            searchResult = statement.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    public static boolean addAEmployee(Employee newEmployee){
        int newEmployeeID;
        int newUserID;
        ResultSet searchResult;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        String sql;
        PreparedStatement statement;
        PreparedStatement transactionStatement;
        try{
            sql = "select * from Employee order by ABS(employeeID)";
            statement = instance.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            searchResult = statement.executeQuery();
            if (searchResult.next()){
                searchResult.last();
                newEmployeeID = Integer.parseInt(searchResult.getString("employeeID")) + 1;
            }
            else
                newEmployeeID = 1;
            newEmployee.setEmployeeID(String.valueOf(newEmployeeID));
            sql = "select * from Users order by ABS(userID)";
            statement = instance.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            searchResult = statement.executeQuery();
            if (searchResult.next()){
                searchResult.last();
                newUserID = Integer.parseInt(searchResult.getString("userID")) + 1;
            }
            else
                newUserID = 1;
            newEmployee.setUserID(String.valueOf(newUserID));

            sql = "insert into Employee values (?, ?, ?)";
            transactionStatement = instance.transactionConnection.prepareStatement(sql);
            transactionStatement.setString(1, newEmployee.getEmployeeID());
            transactionStatement.setString(2, newEmployee.getEmployeeName());
            transactionStatement.setInt(3, newEmployee.getAge());
            transactionStatement.executeUpdate();

            sql = "insert into Users values (?, ? , null, ?, ?)";
            transactionStatement = instance.transactionConnection.prepareStatement(sql);
            transactionStatement.setString(1, newEmployee.getUserID());
            transactionStatement.setString(2, newEmployee.getPassword());
            transactionStatement.setString(3, newEmployee.getEmployeeID());
            transactionStatement.setString(4, newEmployee.getAuthority());
            transactionStatement.executeUpdate();
            instance.transactionConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                instance.transactionConnection.rollback();
                return false;
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                instance.closeConnection();
            }
        }
        instance.closeConnection();
        return true;
    }

    public static boolean updateAEmployee(Employee newEmployee, Employee originEmployee) throws UpdateException {
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        PreparedStatement statement;
        PreparedStatement queryStatement;
        ResultSet searchResult;
        String sql;
        try {
            sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and Employee.employeeID = ?";
            queryStatement = instance.connection.prepareStatement(sql);
            queryStatement.setString(1, originEmployee.getEmployeeID());
            searchResult = queryStatement.executeQuery();
            if (!searchResult.next()) {
                instance.closeConnection();
                throw new UpdateException("The employee does not exist. Please refresh the table.", UpdateException.ErrorCode.itemNotExit);
            }
            if (!searchResult.getString("employeeName").equals(originEmployee.getEmployeeName().trim()) ||
                    searchResult.getInt("age") != originEmployee.getAge() ||
                    !searchResult.getString("password").equals(originEmployee.getPassword().trim()) ||
                    !searchResult.getString("authority").equals(originEmployee.getAuthority().trim())
            ) {
                instance.closeConnection();
                throw new UpdateException("Employee information was updated. Please refresh the page and decide later", UpdateException.ErrorCode.itemInformationChanged);
            }

            sql = "update Employee set employeeName = ?, age = ? where employeeID = ?";
            statement = instance.transactionConnection.prepareStatement(sql);
            statement.setString(1, newEmployee.getEmployeeName());
            statement.setInt(2, newEmployee.getAge());
            statement.setString(3, originEmployee.getEmployeeID());
            statement.executeUpdate();

            sql = "update Users set password = ?, authority = ? where employeeID = ?";
            statement = instance.transactionConnection.prepareStatement(sql);
            statement.setString(1, newEmployee.getPassword());
            statement.setString(2, newEmployee.getAuthority());
            statement.setString(3, newEmployee.getEmployeeID());
            statement.executeUpdate();
            instance.transactionConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                instance.transactionConnection.rollback();
                throw new UpdateException("System Error. Try Later.", UpdateException.ErrorCode.sqlException);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                instance.closeConnection();
            }
        }
        instance.closeConnection();
        return true;
    }

    public static boolean deleteAEmployee(Employee deleteEmployee) throws DeleteException {
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        String sql;
        ResultSet searchResult;
        PreparedStatement statement;
        PreparedStatement transactionStatement;
        try {
            sql = "select * from Employee, Users where Employee.employeeID = Users.employeeID and Employee.employeeID = ?";
            statement = instance.connection.prepareStatement(sql);
            statement.setString(1, deleteEmployee.getEmployeeID());
            searchResult = statement.executeQuery();
            if (!searchResult.next()) {
                instance.closeConnection();
                throw new DeleteException("The employee is already deleted by other.", DeleteException.ErrorCode.itemNotExist);
            }

            if (!searchResult.getString("employeeName").equals(deleteEmployee.getEmployeeName().trim()) ||
                    searchResult.getInt("age") != deleteEmployee.getAge() ||
                    !searchResult.getString("password").equals(deleteEmployee.getPassword().trim()) ||
                    !searchResult.getString("authority").equals(deleteEmployee.getAuthority().trim())
            ) {
                instance.closeConnection();
                throw new DeleteException("Employee information was updated. Please refresh the page and decide later", DeleteException.ErrorCode.itemInformationChanged);
            }

            sql = "delete from Employee where employeeID = ?";
            transactionStatement = instance.transactionConnection.prepareStatement(sql);
            transactionStatement.setString(1, deleteEmployee.getEmployeeID());
            transactionStatement.executeUpdate();

            sql = "delete from Users where employeeID = ?";
            transactionStatement = instance.transactionConnection.prepareStatement(sql);
            transactionStatement.setString(1, deleteEmployee.getEmployeeID());
            transactionStatement.executeUpdate();
            instance.transactionConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                instance.transactionConnection.rollback();
                throw new DeleteException("System Error. Please try later.", DeleteException.ErrorCode.sqlException);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                instance.closeConnection();
            }
        }
        instance.closeConnection();
        return true;
    }
}
