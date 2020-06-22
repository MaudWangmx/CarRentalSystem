package DataBase.Activity;

import DataBase.*;
import DataBase.Exception.UpdateException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static Util.Config.*;

public class ActivityDao {

    public static LeaseRecord getLeaseRecordByClientID(String clientID){
        ResultSet leaseSearch;
        ResultSet checkLease;
        String actID;
        LeaseRecord leaseRecord = null;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        try {
            String sql = "select actID, Activity.carID carID, employeeID, brand, rent, pledge, date, isVIP " +
                    "from Activity, Car, Client " +
                    "where Activity.clientID = ? " +
                    "and ACtivity.clientID = Client.clientID " +
                    "and Car.carID = Activity.carID " +
                    "and event = '0' " +
                    "order by date desc limit 1";
            PreparedStatement statement = instance.connection.prepareStatement(sql);
            statement.setString(1, clientID);
            leaseSearch = statement.executeQuery();
            if (leaseSearch.next()) {
                actID = leaseSearch.getString("actID");
                sql = "select * from Activity where relateActID = ? and event = '1'";
                statement = instance.connection.prepareStatement(sql);
                statement.setString(1, actID);
                checkLease = statement.executeQuery();
                if (!checkLease.next()) {
                    int actualPledge = leaseSearch.getInt("pledge");
                    if (leaseSearch.getBoolean("isVIP"))
                        actualPledge /= 2;
                    leaseRecord = new LeaseRecord(leaseSearch.getString("actID"),
                            clientID,
                            leaseSearch.getString("employeeID"),
                            leaseSearch.getString("carID"),
                            leaseSearch.getString("brand"),
                            leaseSearch.getInt("rent"),
                            actualPledge,
                            leaseSearch.getTimestamp("date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            instance.closeConnection();
        }
        return leaseRecord;
    }

    public static LeaseRecord getLeaseRecordByCarID(String carID) throws UpdateException {
        ResultSet leaseSearch;
        ResultSet checkLease;
        String actID;
        LeaseRecord leaseRecord = null;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        try {
            String sql = "select actID, Client.clientID clientID, clientName, employeeID, brand, rent, pledge, date, isVIP " +
                    "from Activity, Car, Client " +
                    "where Activity.carID = ? " +
                    "and Activity.clientID = Client.clientID " +
                    "and Car.carID = Activity.carID " +
                    "and event = '0' " +
                    "order by date desc limit 1";
            PreparedStatement statement = instance.connection.prepareStatement(sql);
            statement.setString(1, carID);
            leaseSearch = statement.executeQuery();
            if (leaseSearch.next()) {
                actID = leaseSearch.getString("actID");
                sql = "select * from Activity where relateActID = ? and event = '1'";
                statement = instance.connection.prepareStatement(sql);
                statement.setString(1, actID);
                checkLease = statement.executeQuery();
                if (!checkLease.next()) {
                    int actualPledge = leaseSearch.getInt("pledge");
                    if (leaseSearch.getBoolean("isVIP"))
                        actualPledge /= 2;
                    leaseRecord = new LeaseRecord(leaseSearch.getString("actID"),
                            leaseSearch.getString("clientID"),
                            leaseSearch.getString("employeeID"),
                            carID,
                            leaseSearch.getString("brand"),
                            leaseSearch.getInt("rent"),
                            actualPledge,
                            leaseSearch.getTimestamp("date"));
                }else {
                    DBConnector.getInstance().closeConnection();
                    throw new UpdateException("Car is not being leased now.", UpdateException.ErrorCode.itemInformationChanged);
                }
            }else {
                DBConnector.getInstance().closeConnection();
                throw new UpdateException("Car is not being leased now.", UpdateException.ErrorCode.itemInformationChanged);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            instance.closeConnection();
        }
        return leaseRecord;
    }


    public static ResultSet getTrafficFineByActID(String actID){
        ResultSet searchResult = null;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        try {
            String sql = "select Activity.actID, finID, amount, date from Activity, Finance where Activity.relateActID = ? and Finance.actID = Activity.actID and Finance.finType = '3'";
            PreparedStatement statement = instance.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, actID);
            searchResult = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    public static boolean rentActivity(RentInfo rentInfo){
        ResultSet resultSet;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();

        try {
            int  newActID;
            int newFinID;
            /* get last actID */
            String sql = "select actID from Activity order by ABS(actID)";
            PreparedStatement statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                resultSet.last();
                newActID = Integer.parseInt(resultSet.getString("actID")) + 1;
            }
            else
                newActID = 1;
            sql = "insert into Activity values (?, '0', ?, ?, ?, ?, null, null)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, Integer.toString(newActID));
            statement.setString(2, rentInfo.getClientID());
            statement.setString(3, rentInfo.getCar().getCarID());
            statement.setString(4, rentInfo.getEmployeeID());
            statement.setTimestamp(5, rentInfo.getRentTime());
            System.out.println(statement.toString());
            statement.execute();

            sql = "select finID from Finance order by ABS(finID)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                resultSet.last();
                newFinID = Integer.parseInt(resultSet.getString("finID")) + 1;
            }
            else
                newFinID = 1;
            sql = "insert into Finance values (?, ?, ?, '0', ?)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, Integer.toString(newFinID));
            statement.setInt(2, rentInfo.getCar().getPledge());
            statement.setString(3, Integer.toString(newActID));
            statement.setTimestamp(4, rentInfo.getRentTime());
            statement.executeUpdate();

            sql = "update Car set Car.status = '0' where carID = ?";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, rentInfo.getCar().getCarID());
            statement.executeUpdate();

            instance.transactionConnection.commit();
        } catch (SQLException e) {
            System.out.println("error");
            e.printStackTrace();
            try {
                System.out.println("error");
                instance.transactionConnection.rollback();
                return false;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }finally {
                instance.closeConnection();
            }
        }
        instance.closeConnection();
        return true;
    }

    public static boolean returnActivity(ReturnInfo returnInfo){
        ResultSet resultSet;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();

        try {
            int  newActID;
            int newFinID;
            /* get last actID */
            String sql = "select actID from Activity order by ABS(actID)";
            PreparedStatement statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                resultSet.last();
                newActID = Integer.parseInt(resultSet.getString("actID")) + 1;
            }
            else
                newActID = 1;
            sql = "insert into Activity values (?, '1', ?, ?, ?, ?, ?, null)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, Integer.toString(newActID));
            statement.setString(2, returnInfo.getClient().getClientID());
            statement.setString(3, returnInfo.getLeaseRecord().getCarID());
            statement.setString(4, returnInfo.getEmployeeID());
            statement.setTimestamp(5, returnInfo.getTimestamp());
            statement.setString(6, returnInfo.getLeaseRecord().getActID());
            System.out.println(statement.toString());
            statement.execute();

            sql = "select finID from Finance order by ABS(finID)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                resultSet.last();
                newFinID = Integer.valueOf(resultSet.getString("finID")) + 1;
            }
            else
                newFinID = 1;
            sql = "insert into Finance values (?, ?, ?, '1', ?)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, Integer.toString(newFinID));
            statement.setInt(2, returnInfo.getTotalRent());
            statement.setString(3, Integer.toString(newActID));
            statement.setTimestamp(4, returnInfo.getTimestamp());
            statement.executeUpdate();

            sql = "select finID from Finance order by ABS(finID)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery();
            resultSet.last();
            newFinID = Integer.valueOf(resultSet.getString("finID")) + 1;
            sql = "insert into Finance values (?, ?, ?, '5', ?)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, Integer.toString(newFinID));
            statement.setInt(2, returnInfo.getLeaseRecord().getPledge());
            statement.setString(3, Integer.toString(newActID));
            statement.setTimestamp(4, returnInfo.getTimestamp());
            statement.executeUpdate();

            sql = "select finID from Finance order by ABS(finID)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery();
            resultSet.last();
            newFinID = Integer.valueOf(resultSet.getString("finID")) + 1;
            sql = "insert into Finance values (?, ?, ?, '6', ?)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, Integer.toString(newFinID));
            statement.setInt(2, returnInfo.getTrafficFine());
            statement.setString(3, Integer.toString(newActID));
            statement.setTimestamp(4, returnInfo.getTimestamp());
            statement.executeUpdate();

            sql = "select actID from Activity order by ABS(actID)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery();
            resultSet.last();
            newActID = Integer.valueOf(resultSet.getString("actID")) + 1;
            sql = "insert into Activity values (?, '2', ?, ?, ?, ?, ?, null)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, Integer.toString(newActID));
            statement.setString(2, returnInfo.getClient().getClientID());
            statement.setString(3, returnInfo.getLeaseRecord().getCarID());
            statement.setString(4, returnInfo.getEmployeeID());
            statement.setTimestamp(5, returnInfo.getTimestamp());
            statement.setString(6, returnInfo.getLeaseRecord().getActID());
            statement.execute();

            sql = "select * from Finance order by ABS(finID)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery();
            resultSet.last();
            newFinID = Integer.valueOf(resultSet.getString("finID")) + 1;
            sql = "insert into Finance values (?, ?, ?, '2', ?)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, Integer.toString(newFinID));
            statement.setInt(2, returnInfo.getDamageFine());
            statement.setString(3, Integer.toString(newActID));
            statement.setTimestamp(4, returnInfo.getTimestamp());
            statement.executeUpdate();

            sql = "update Car set Car.condition = ?, Car.status = '1' where carID = ?";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, returnInfo.getCondition());
            statement.setString(2, returnInfo.getLeaseRecord().getCarID());
            statement.executeUpdate();

            sql = "update Client set Client.credit = ?, Client.isVIP = ? where clientID = ?";
            int credit = returnInfo.getClient().getCredit() +
                    ((10 - (5 - Integer.valueOf(returnInfo.getCondition())) - returnInfo.getTrafficFineCount())
                            * returnInfo.getLeaseRecord().getRent() / 10);
            System.out.println(returnInfo);
            System.out.println(credit);
            String isVIP = (credit >= VIP_CREDIT)? "1": "0";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setInt(1, credit);
            statement.setString(2, isVIP);
            statement.setString(3, returnInfo.getClient().getClientID());
            statement.executeUpdate();

            instance.transactionConnection.commit();
        } catch (SQLException e) {
            System.out.println("error");
            e.printStackTrace();
            try {
                System.out.println("error");
                instance.transactionConnection.rollback();
                return false;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }finally {
                instance.closeConnection();
            }
        }
        instance.closeConnection();
        return true;

    }

    public static ResultSet getClientRecord(String clientID, String carID, int recordType, String startTime, String endTime){
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        ResultSet searchResult = null;
        StringBuffer sql = new StringBuffer();
        PreparedStatement statement;
        try{
            switch (recordType){
                default:
                case 0:
                    sql.append("select Activity.actID actID, event, Activity.carID, employeeID, date, amount, fintype, brand " +
                            "from Activity, Finance, Car " +
                            "where clientID = ? " +
                            "and Activity.carID = Car.carID " +
                            "and Activity.actID = Finance.actID " +
                            "and date <= ? " +
                            "and date >= ?");
                    break;
                case 1:
                    sql.append("select Activity.actID actID, event, Activity.carID carID, employeeID, date, amount, fintype, brand " +
                            "from Activity, Finance, Car " +
                            "where clientID = ? " +
                            "and Activity.carID = Car.carID " +
                            "and Activity.actID = Finance.actID " +
                            "and date <= ? " +
                            "and date >= ? " +
                            "and event = '0'");
                    break;
                case 2:
                    sql.append("select Activity.actID actID, event, Activity.carID carID, employeeID, date, amount, fintype, brand " +
                            "from Activity, Finance, Car " +
                            "where clientID = ? " +
                            "and Activity.carID = Car.carID " +
                            "and Activity.actID = Finance.actID " +
                            "and date <= ? " +
                            "and date >= ? " +
                            "and event = '1'");
                    break;
                case 3:
                    sql.append("select Activity.actID actID, event, Activity.carID carID, employeeID, date, amount, fintype, brand " +
                            "from Activity, Finance, Car " +
                            "where clientID = ? " +
                            "and Activity.carID = Car.carID " +
                            "and Activity.actID = Finance.actID " +
                            "and date <= ? " +
                            "and date >= ? " +
                            "and event = '2'");
                    break;
                case 4:
                    sql.append("select Activity.actID actID, event, Activity.carID carID, employeeID, date, amount, fintype, brand " +
                            "from Activity, Finance, Car " +
                            "where clientID = ? " +
                            "and Activity.carID = Car.carID " +
                            "and Activity.actID = Finance.actID " +
                            "and date <= ? " +
                            "and date >= ? " +
                            "and event = '3'");
                    break;
            }
            if (!carID.isEmpty()) {
                sql.append(" and carID = ?");
            }
            statement = instance.connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, clientID);
            statement.setString(2, endTime);
            statement.setString(3, startTime);
            if (!carID.isEmpty()) {
                statement.setString(4, carID);
            }
            searchResult = statement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return searchResult;
    }

    public static ResultSet getActInfoTable(String optID, String clientID, String carID, int event, String startTime, String endTime){
        ResultSet searchResult = null;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer();

        try {
            switch (event){
                case 0:     // All event
                {
                    sql.append("select * from Activity, Finance where Activity.actID = Finance.actID and date >= ? and date <= ?");
                    break;
                }
                case 1:     // Lease
                {
                    sql.append("select * from Activity, Finance where Activity.actID = Finance.actID and event = '0' and date >= ? and date <= ?");
                    break;
                }
                case 2:     // Return
                {
                    sql.append("select * from Activity, Finance where Activity.actID = Finance.actID and event = '1' and date >= ? and date <= ?");
                    break;
                }
                case 3:     // Damage
                {
                    sql.append("select * from Activity, Finance where Activity.actID = Finance.actID and event = '2' and date >= ? and date <= ?");
                    break;
                }
                case 4:     // Traffic Fine
                {
                    sql.append("select * from Activity, Finance where Activity.actID = Finance.actID and event = '3' and date >= ? and date <= ?");
                    break;
                }
                case 5:     // Repair
                {
                    sql.append("select * from Activity, Finance where Activity.actID = Finance.actID and event = '4' and date >= ? and date <= ?");
                    break;
                }
            }
            if (optID.isEmpty() && clientID.isEmpty() && carID.isEmpty()){
                sql.append(" order by date");
                statement = instance.connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.setString(1, startTime);
                statement.setString(2, endTime);
            }
            else if (optID.isEmpty() && clientID.isEmpty() && !carID.isEmpty()){
                sql.append(" and carID like ? order by date");
                statement = instance.connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.setString(1, startTime);
                statement.setString(2, endTime);
                statement.setString(3, "%" + carID + "%");
            }
            else if (optID.isEmpty() && !clientID.isEmpty() && carID.isEmpty()){
                sql.append(" and clientID like ? order by date");
                statement = instance.connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.setString(1, startTime);
                statement.setString(2, endTime);
                statement.setString(3, "%" + clientID + "%");
            }
            else if (optID.isEmpty() && !clientID.isEmpty() && !carID.isEmpty()){
                sql.append(" and carID like ? and clientID like ? order by date");
                statement = instance.connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.setString(1, startTime);
                statement.setString(2, endTime);
                statement.setString(3, "%" + carID + "%");
                statement.setString(4, "%" + clientID + "%");
            }
            else if (!optID.isEmpty() && clientID.isEmpty() && carID.isEmpty()){
                sql.append(" and employeeID like ? order by date");
                statement = instance.connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.setString(1, startTime);
                statement.setString(2, endTime);
                statement.setString(3, "%" + optID + "%");
            }
            else if (!optID.isEmpty() && clientID.isEmpty() && !carID.isEmpty()){
                sql.append(" and employeeID like ? and carID like ? order by date");
                statement = instance.connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.setString(1, startTime);
                statement.setString(2, endTime);
                statement.setString(3, "%" + optID + "%");
                statement.setString(4, "%" + carID + "%");
            }
            else if (!optID.isEmpty() && !clientID.isEmpty() && carID.isEmpty()){
                sql.append(" and employeeID like ? and clientID like ? order by date");
                statement = instance.connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.setString(1, startTime);
                statement.setString(2, endTime);
                statement.setString(3, "%" + optID + "%");
                statement.setString(4, "%" + clientID + "%");
            }
            else if (!optID.isEmpty() && !clientID.isEmpty() && !carID.isEmpty()){
                sql.append(" and employeeID like ? and clientID like ? and carID like ? order by date");
                statement = instance.connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.setString(1, startTime);
                statement.setString(2, endTime);
                statement.setString(3, "%" + optID + "%");
                statement.setString(4, "%" + clientID + "%");
                statement.setString(5, "%" + carID + "%");
            }
            System.out.println(statement);
            searchResult = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    public static boolean importATrafficFine(String carID, String clientID, String employeeID, String relateActID, int amount) throws UpdateException{
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        ResultSet searchResult;
        PreparedStatement statement;
        String sql;
        int newActID, newFinID;
        try {
            sql = "select * from Activity where relateActID = ? and event = '1'";
            statement = instance.connection.prepareStatement(sql);
            statement.setString(1, relateActID);
            searchResult = statement.executeQuery();
            if (searchResult.next()){
                instance.closeConnection();
                throw new UpdateException("Car Lease Information Updated Before. Please refresh the page.", UpdateException.ErrorCode.itemInformationChanged);
            }
            /* get last actID */
            sql = "select actID from Activity order by ABS(actID)";
            statement = instance.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            searchResult = statement.executeQuery();
            if (searchResult.next()) {
                searchResult.last();
                newActID = Integer.valueOf(searchResult.getString("actID")) + 1;
            }
            else
                newActID = 1;

            sql = "insert into Activity values (?, '3', ?, ?, ?, ?, ?, null )";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, Integer.toString(newActID));
            statement.setString(2, clientID);
            statement.setString(3, carID);
            statement.setString(4, employeeID);
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(6, relateActID);
            statement.executeUpdate();

            sql = "select finID from Finance order by ABS(finID)";
            statement = instance.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            searchResult = statement.executeQuery();
            if (searchResult.next()) {
                searchResult.last();
                newFinID = Integer.valueOf(searchResult.getString("finID")) + 1;
            }
            else
                newFinID = 1;
            sql = "insert into Finance values (?, ?, ?, '3', ?)";
            statement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, Integer.toString(newFinID));
            statement.setInt(2, amount);
            statement.setString(3, Integer.toString(newActID));
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            instance.transactionConnection.commit();

        }  catch (SQLException e) {
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

    public static int getIncome(String startTime, String endTime){
        int income = 0;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        String sql;
        PreparedStatement statement;
        ResultSet searchResult;
        try{
            sql = "select SUM(amount) income from Finance, Activity where fintype in ('0', '1', '2', '6') and Finance.actID = Activity.actID and date >= ? and date <= ?";
            statement = instance.connection.prepareStatement(sql);
            statement.setString(1, startTime);
            statement.setString(2, endTime);
            searchResult = statement.executeQuery();
            if (searchResult.next())
                income = searchResult.getInt("income");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            instance.closeConnection();
        }
        return income;
    }

    public static int getOutcome(String startTime, String endTime){
        int outcome = 0;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        String sql;
        PreparedStatement statement;
        ResultSet searchResult;
        try{
            sql = "select SUM(amount) outcome from Finance, Activity where fintype in ('3', '4', '5') and Finance.actID = Activity.actID and date >= ? and date <= ?";
            statement = instance.connection.prepareStatement(sql);
            statement.setString(1, startTime);
            statement.setString(2, endTime);
            searchResult = statement.executeQuery();
            if (searchResult.next())
                outcome = searchResult.getInt("outcome");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            instance.closeConnection();
        }
        return outcome;
    }

    public static List<RankItem> getCarLeasedBrandRank(String startTime, String endTime){
        List<RankItem> brandList = new ArrayList<>();
        ResultSet searchResult;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        try{
            String sql = "select brand, count(*) count " +
                    "from Activity, Car " +
                    "where Activity.event = '0' " +
                    "and Car.carID = Activity.carID " +
                    "and date >= ? " +
                    "and date <= ? " +
                    "group by brand " +
                    "order by COUNT(*) desc limit 3";
            PreparedStatement statement = instance.connection.prepareStatement(sql);
            statement.setString(1, startTime);
            statement.setString(2, endTime);
            searchResult = statement.executeQuery();
            while (searchResult.next()){
                brandList.add(new RankItem(searchResult.getString("brand"), searchResult.getInt("count")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            instance.closeConnection();
        }

        return brandList;
    }

    public static List<RankItem> getClientConsumptionRank(String startTime, String endTime){
        List<RankItem> clientList = new ArrayList<>();
        ResultSet searchResult;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        try{
            String sql = "select Client.clientID clientID, clientName, sum(amount) amount " +
                    "from Activity, Finance, Client " +
                    "where Activity.actID = Finance.actID " +
                    "and Activity.clientID = Client.clientID " +
                    "and fintype = '1' " +
                    "and date >= ? " +
                    "and date <= ? " +
                    "group by Client.clientID, clientName " +
                    "order by sum(amount) desc limit 3";
            PreparedStatement statement = instance.connection.prepareStatement(sql);
            statement.setString(1, startTime);
            statement.setString(2, endTime);
            searchResult = statement.executeQuery();
            while (searchResult.next()){
                clientList.add(new RankItem(searchResult.getString("clientID") + " " + searchResult.getString("clientName"),
                        searchResult.getInt("amount")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            instance.closeConnection();
        }

        return clientList;
    }
}
