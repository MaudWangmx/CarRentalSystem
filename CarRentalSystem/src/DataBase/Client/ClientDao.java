package DataBase.Client;

import DataBase.DBConnector;
import DataBase.Exception.DeleteException;
import DataBase.Exception.UpdateException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ClientDao {

    public static List<Client> getWholeTable(){
        List<Client> clientList = new ArrayList<>();
        ResultSet searchResult;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        try {
            PreparedStatement statement = instance.connection.prepareStatement("select * from Client");
            searchResult = statement.executeQuery();
            while (searchResult.next()){
                clientList.add(new Client(
                        searchResult.getString(0),
                        searchResult.getString(1),
                        searchResult.getString(2),
                        searchResult.getInt(3),
                        searchResult.getBoolean(4)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            instance.closeConnection();
        }
        return clientList;
    }

    public static ResultSet getClientUserInfoByID(String clientID) {
        ResultSet searchResult = null;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        try {
            String sql = "select * from Client, Users where Client.clientID = Users.clientID and Client.clientID = ?";
            PreparedStatement statement = instance.connection.prepareStatement(sql);
            statement.setString(1, clientID);
            searchResult = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    public static String getClientNameByID(String clientID){
        ResultSet searchResult = null;
        DBConnector instance = DBConnector.getInstance();
        String name = "";
        instance.connectDataBase();
        try {
            String sql = "select * from Client, Users where Client.clientID = Users.clientID and Client.clientID = ?";
            PreparedStatement statement = instance.connection.prepareStatement(sql);
            statement.setString(1, clientID);
            searchResult = statement.executeQuery();
            searchResult.next();
            name = searchResult.getString("clientName");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static ResultSet getClientUserTable(String name, String clientID, int Identity){
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        ResultSet searchResult = null;
        PreparedStatement statement;
        String sql;
        boolean isVIP = (Identity == 2);
        try {
            switch (Identity) {
                default:
                case 0: {
                    if (name.isEmpty() && clientID.isEmpty()) {
                        sql = "select * from Client, Users where Client.clientID = Users.clientID";
                        statement = instance.connection.prepareStatement(sql);
                    } else if (name.isEmpty() && !clientID.isEmpty()) {
                        sql = "select * from Client, Users where Client.clientID = Users.clientID and Client.clientID like ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + clientID + "%");
                    } else if (!name.isEmpty() && clientID.isEmpty()) {
                        sql = "select * from Client, Users where Client.clientID = Users.clientID and clientName like ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + name + "%");
                    } else {
                        sql = "select * from Client, Users where Client.clientID = Users.clientID and Client.clientID like ? and clientName like ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + clientID + "%");
                        statement.setString(2, "%" + name + "%");
                    }
                    break;
                }
                case 1:
                case 2: {
                    if (name.isEmpty() && clientID.isEmpty()) {
                        sql = "select * from Client, Users where Client.clientID = Users.clientID and isVIP = ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setBoolean(1, isVIP);
                    } else if (name.isEmpty() && !clientID.isEmpty()) {
                        sql = "select * from Client, Users where Client.clientID = Users.clientID and Client.clientID like ? and isVIP = ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + clientID + "%");
                        statement.setBoolean(2, isVIP);
                    } else if (!name.isEmpty() && clientID.isEmpty()) {
                        sql = "select * from Client, Users where Client.clientID = Users.clientID and clientName like ? and isVIP = ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + name + "%");
                        statement.setBoolean(2, isVIP);
                    } else {
                        sql = "select * from Client, Users where Client.clientID = Users.clientID and Client.clientID like ? and clientName like ? and isVIP = ?";
                        statement = instance.connection.prepareStatement(sql);
                        statement.setString(1, "%" + clientID + "%");
                        statement.setString(2, "%" + name + "%");
                        statement.setBoolean(3, isVIP);
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

    public static boolean addAClient(Client newClient){
        int newClientID;
        int newUserID;
        ResultSet searchResult;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        String sql;
        PreparedStatement statement;
        PreparedStatement transactionStatement;
        try{
            sql = "select * from Client order by ABS(clientID)";
            statement = instance.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            searchResult = statement.executeQuery();
            if (searchResult.next()){
                searchResult.last();
                newClientID = Integer.parseInt(searchResult.getString("clientID")) + 1;
            }
            else
                newClientID = 1;
            newClient.setClientID(String.valueOf(newClientID));
            sql = "select * from Users order by ABS(userID)";
            statement = instance.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            searchResult = statement.executeQuery();
            if (searchResult.next()){
                searchResult.last();
                newUserID = Integer.parseInt(searchResult.getString("userID")) + 1;
            }
            else
                newUserID = 1;
            newClient.setUserID(String.valueOf(newUserID));

            sql = "insert into Client values (?, ?, ?, ?, ?)";
            transactionStatement = instance.transactionConnection.prepareStatement(sql);
            transactionStatement.setString(1, newClient.getClientID());
            transactionStatement.setString(2, newClient.getClientName());
            transactionStatement.setString(3, newClient.getContact());
            transactionStatement.setInt(4, newClient.getCredit());
            transactionStatement.setBoolean(5, newClient.isVIP());
            transactionStatement.executeUpdate();

            sql = "insert into Users values (?, ? , ?, null, '3')";
            transactionStatement = instance.transactionConnection.prepareStatement(sql);
            transactionStatement.setString(1, newClient.getUserID());
            transactionStatement.setString(2, newClient.getPassword());
            transactionStatement.setString(3, newClient.getClientID());
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

    public static boolean updateAClient(Client newClient, Client originClient) throws UpdateException{
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        PreparedStatement statement;
        PreparedStatement queryStatement;
        ResultSet searchResult;
        String sql;
        try {
            sql = "select * from Client, Users where Client.clientID = Users.clientID and Client.clientID = ?";
            queryStatement = instance.connection.prepareStatement(sql);
            queryStatement.setString(1, originClient.getClientID());
            searchResult = queryStatement.executeQuery();
            if (!searchResult.next()) {
                instance.closeConnection();
                throw new UpdateException("The client does not exist. Please refresh the table.", UpdateException.ErrorCode.itemNotExit);
            }
            if (!searchResult.getString("clientName").equals(originClient.getClientName().trim()) ||
                    searchResult.getInt("credit") != originClient.getCredit() ||
                    !searchResult.getString("password").equals(originClient.getPassword().trim()) ||
                    !searchResult.getString("contact").equals(originClient.getContact().trim())
            ) {
                instance.closeConnection();
                throw new UpdateException("Client information was updated. Please refresh the page and decide later", UpdateException.ErrorCode.itemInformationChanged);
            }

            sql = "update Client set clientName = ?, credit = ?, contact = ?, isVIP = ? where clientID = ?";
            statement = instance.transactionConnection.prepareStatement(sql);
            statement.setString(1, newClient.getClientName());
            statement.setInt(2, newClient.getCredit());
            statement.setString(3, newClient.getContact());
            statement.setBoolean(4, newClient.isVIP());
            statement.setString(5, originClient.getClientID());
            statement.executeUpdate();

            sql = "update Users set password = ? where clientID = ?";
            statement = instance.transactionConnection.prepareStatement(sql);
            statement.setString(1, newClient.getPassword());
            statement.setString(2, newClient.getClientID());
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

    public static boolean deleteAClient(Client deleteClient) throws DeleteException {
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        String sql;
        ResultSet searchResult;
        PreparedStatement statement;
        PreparedStatement transactionStatement;
        try {
            sql = "select * from Client, Users where Client.clientID = Users.clientID and Client.clientID = ?";
            statement = instance.connection.prepareStatement(sql);
            statement.setString(1, deleteClient.getClientID());
            searchResult = statement.executeQuery();
            if (!searchResult.next()) {
                instance.closeConnection();
                throw new DeleteException("The client is already deleted by other.", DeleteException.ErrorCode.itemNotExist);
            }

            if (!searchResult.getString("clientName").equals(deleteClient.getClientName().trim()) ||
                    searchResult.getInt("credit") != deleteClient.getCredit() ||
                    !searchResult.getString("password").equals(deleteClient.getPassword().trim()) ||
                    !searchResult.getString("contact").equals(deleteClient.getContact().trim())
            ) {
                instance.closeConnection();
                throw new DeleteException("Client information was updated. Please refresh the page and decide later", DeleteException.ErrorCode.itemInformationChanged);
            }

            sql = "delete from Client where clientID = ?";
            transactionStatement = instance.transactionConnection.prepareStatement(sql);
            transactionStatement.setString(1, deleteClient.getClientID());
            transactionStatement.executeUpdate();

            sql = "delete from Users where clientID = ?";
            transactionStatement = instance.transactionConnection.prepareStatement(sql);
            transactionStatement.setString(1, deleteClient.getClientID());
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

