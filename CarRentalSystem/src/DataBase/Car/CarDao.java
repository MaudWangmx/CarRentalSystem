package DataBase.Car;

import DataBase.DBConnector;
import DataBase.Employee.Employee;
import DataBase.Exception.DeleteException;
import DataBase.Exception.UpdateException;
import DataBase.TableView.CarTable;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static Util.Config.*;

public class CarDao {

    public static List<Car> getAllCarInfo(){
        List<Car> carList = new ArrayList<>();
        ResultSet searchResult;
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        try {
            String sql = "select * from Car where Car.condition = '5' and Car.status = '1'";
            PreparedStatement statement = instance.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            searchResult = statement.executeQuery();
            while (searchResult.next()){
                carList.add(new Car(searchResult.getString("carID"),
                        searchResult.getString("brand"),
                        searchResult.getBoolean("status"),
                        searchResult.getInt("rent"),
                        searchResult.getInt("pledge"),
                        searchResult.getString("condition")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            instance.closeConnection();
        }
        return carList;
    }

    public static List<String> getAllBrand(){
        List<String> brandList = new ArrayList<>();
        ResultSet searchResult;
        DBConnector.getInstance().connectDataBase();
        try {
            String sql = "select distinct brand from Car where Car.condition = '5' and Car.status = '1'";
            PreparedStatement statement = DBConnector.getInstance().connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            searchResult = statement.executeQuery();
            while (searchResult.next()){
                brandList.add(searchResult.getString("brand"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnector.getInstance().closeConnection();
        }
        return brandList;
    }

    public static String getBrandByCarID(String carID){
        String brand = "";
        ResultSet searchResult;
        DBConnector.getInstance().connectDataBase();
        try {
            String sql = "select brand from Car where Car.carID = ?";
            PreparedStatement statement = DBConnector.getInstance().connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, carID);
            searchResult = statement.executeQuery();
            while (searchResult.next())
                brand = searchResult.getString("brand");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnector.getInstance().closeConnection();
        }
        return brand;
    }

    public static Car getCarByCarID(String carID){
        Car car = null;
        ResultSet searchResult;
        DBConnector.getInstance().connectDataBase();
        try {
            String sql = "select * from Car where Car.carID = ?";
            PreparedStatement statement = DBConnector.getInstance().connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, carID);
            searchResult = statement.executeQuery();
            while (searchResult.next())
                car = new Car(carID,
                        searchResult.getString("brand"),
                        searchResult.getBoolean("status"),
                        searchResult.getInt("rent"),
                        searchResult.getInt("pledge"),
                        searchResult.getString("condition"));
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnector.getInstance().closeConnection();
        }
        return car;
    }

    public static ResultSet getSearchCarTable(String brand, String carId, int rentMin, int rentMax, String condition){
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        System.out.println(Integer.toString(rentMin) + rentMax);
        String sql;
        PreparedStatement statement;
        ResultSet searchResult = null;
        try {
            if(condition.equals("6")){
                if (brand.isEmpty() && carId.isEmpty()){
                    sql = "select * from Car where rent >= ? and rent <= ?";
                    statement = instance.connection.prepareStatement(sql);
                }
                else if (brand.isEmpty() && !carId.isEmpty()){
                    System.out.println(2);
                    sql = "select * from Car where rent>= ? and rent <= ? and carID like ?";
                    statement = instance.connection.prepareStatement(sql);
                    statement.setString(3, "%" + carId + "%");
                }
                else if (!brand.isEmpty() && carId.isEmpty()){
                    System.out.println(3);
                    sql = "select * from Car where rent >= ? and rent <= ? and brand like ?";
                    statement = instance.connection.prepareStatement(sql);
                    statement.setString(3, "%" + brand + "%");
                }
                else {
                    System.out.println(4);
                    sql = "select * from Car where rent >= ? and rent <= ? and brand like ? and carID like ?";
                    statement = instance.connection.prepareStatement(sql);
                    statement.setString(3, "%" + brand + "%");
                    statement.setString(4, "%" + carId + "%");
                }
            }
            else if (brand.isEmpty() && carId.isEmpty()){
                sql = "select * from Car where rent >= ? and rent <= ? and Car.condition = ?";
                statement = instance.connection.prepareStatement(sql);
                statement.setString(3, condition);
            }
            else if (brand.isEmpty() && !carId.isEmpty()){
                System.out.println(2);
                sql = "select * from Car where rent>= ? and rent <= ? and Car.condition = ? and carID like ?";
                statement = instance.connection.prepareStatement(sql);
                statement.setString(3, condition);
                statement.setString(4, "%" + carId + "%");
            }
            else if (!brand.isEmpty() && carId.isEmpty()){
                System.out.println(3);
                sql = "select * from Car where rent >= ? and rent <= ? and Car.condition = ? and brand like ?";
                statement = instance.connection.prepareStatement(sql);
                statement.setString(3, condition);
                statement.setString(4, "%" + brand + "%");
            }
            else {
                System.out.println(4);
                sql = "select * from Car where rent >= ? and rent <= ? and Car.condition = ? and brand like ? and carID like ?";
                statement = instance.connection.prepareStatement(sql);
                statement.setString(3, condition);
                statement.setString(4, "%" + brand + "%");
                statement.setString(5, "%" + carId + "%");
            }
            statement.setInt(1, rentMin);
            statement.setInt(2, rentMax);
            searchResult = statement.executeQuery();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return searchResult;
    }

    public static boolean addACar(Car car) {
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        PreparedStatement statement;
        String sql;
        try {
            sql = "insert into Car values (?, ?, ?, ?, ?, ?)";
            statement = instance.transactionConnection.prepareStatement(sql);
            statement.setString(1, car.getCarID());
            statement.setString(2, car.getBrand());
            statement.setBoolean(3, car.isStatus());
            statement.setInt(4, car.getRent());
            statement.setInt(5, car.getPledge());
            statement.setString(6, car.getCondition());
            statement.executeUpdate();
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

    public static boolean updateACar(Car car, Car originCar) throws UpdateException {
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        PreparedStatement statement;
        PreparedStatement queryStatement;
        ResultSet searchResult;
        String sql;
        try {
            sql = "select * from Car where carID = ?";
            queryStatement = instance.connection.prepareStatement(sql);
            queryStatement.setString(1, originCar.getCarID());
            searchResult = queryStatement.executeQuery();
            if (!searchResult.next()) {
                instance.closeConnection();
                throw new UpdateException("The car does not exist. Please refresh the table.", UpdateException.ErrorCode.itemNotExit);
            }
            if (!searchResult.getString("brand").equals(originCar.getBrand().trim()) ||
                    searchResult.getInt("rent") != originCar.getRent() ||
                    searchResult.getInt("pledge") != originCar.getPledge() ||
                    !searchResult.getString("condition").equals(originCar.getCondition()) ||
                    searchResult.getBoolean("status") != originCar.isStatus()
            ) {
                instance.closeConnection();
                throw new UpdateException("Car information was updated. Please refresh the page and decide later", UpdateException.ErrorCode.itemInformationChanged);
            }
            if (!searchResult.getBoolean("status")) {
                instance.closeConnection();
                throw new UpdateException("Cannot revise information of a leased car.", UpdateException.ErrorCode.carLeased);
            }
            sql = "update Car set carID = ?, brand = ?, status = ?, rent = ?, pledge = ?, Car.condition = ? where carID = ?";
            statement = instance.transactionConnection.prepareStatement(sql);
            statement.setString(1, car.getCarID());
            statement.setString(2, car.getBrand());
            statement.setBoolean(3, car.isStatus());
            statement.setInt(4, car.getRent());
            statement.setInt(5, car.getPledge());
            statement.setString(6, car.getCondition());
            statement.setString(7, originCar.getCarID());
            statement.executeUpdate();
            instance.transactionConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                instance.transactionConnection.rollback();
                if (e instanceof SQLIntegrityConstraintViolationException)
                    throw new UpdateException("Duplicate License. Please Check.", UpdateException.ErrorCode.sqlException);
                else
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

    public static boolean deleteACar(Car deleteCar) throws DeleteException {
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        String sql;
        ResultSet searchResult;
        PreparedStatement statement;
        PreparedStatement transactionStatement;
        try {
            sql = "select * from Car where carID = ?";
            statement = instance.connection.prepareStatement(sql);
            statement.setString(1, deleteCar.getCarID());
            searchResult = statement.executeQuery();
            if (!searchResult.next()) {
                instance.closeConnection();
                throw new DeleteException("The car is already deleted by other.", DeleteException.ErrorCode.itemNotExist);
            }
            if (!searchResult.getString("brand").equals(deleteCar.getBrand().trim()) ||
                    searchResult.getInt("rent") != deleteCar.getRent() ||
                    searchResult.getInt("pledge") != deleteCar.getPledge() ||
                    !searchResult.getString("condition").equals(deleteCar.getCondition()) ||
                    (searchResult.getBoolean("status") != deleteCar.isStatus())
            ) {
                instance.closeConnection();
                throw new DeleteException("Car information was updated. Please refresh the page and decide later", DeleteException.ErrorCode.itemInformationChanged);
            }
            if (!searchResult.getBoolean("status")) {
                instance.closeConnection();
                throw new DeleteException("Cannot delete information of a leased car.", DeleteException.ErrorCode.carLeased);
            }
            sql = "delete from Car where carID = ?";
            transactionStatement = instance.transactionConnection.prepareStatement(sql);
            transactionStatement.setString(1, deleteCar.getCarID());
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

    public static boolean repairACar(Car car, String employeeID, int amount) throws UpdateException{
        DBConnector instance = DBConnector.getInstance();
        instance.connectDataBase();
        PreparedStatement transactionStatement;
        PreparedStatement statement;
        ResultSet searchResult;
        String sql;
        int  newActID;
        int newFinID;
        try {
            sql = "select * from Car where carID = ?";
            statement = instance.connection.prepareStatement(sql);
            statement.setString(1, car.getCarID());
            searchResult = statement.executeQuery();
            if (!searchResult.next()) {
                instance.closeConnection();
                throw new UpdateException("The car does not exist. Please refresh the table.", UpdateException.ErrorCode.itemNotExit);
            }
            if (!searchResult.getString("brand").equals(car.getBrand().trim()) ||
                    searchResult.getInt("rent") != car.getRent() ||
                    searchResult.getInt("pledge") != car.getPledge() ||
                    !searchResult.getString("condition").equals(car.getCondition()) ||
                    searchResult.getBoolean("status") != car.isStatus()
            ) {
                instance.closeConnection();
                throw new UpdateException("Car information was updated. Please refresh the page and decide later", UpdateException.ErrorCode.itemInformationChanged);
            }
            if (!searchResult.getBoolean("status")) {
                instance.closeConnection();
                throw new UpdateException("Cannot revise information of a leased car.", UpdateException.ErrorCode.carLeased);
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
            sql = "insert into Activity values (?, '4', null, ?, ?, ?, null, null)";
            transactionStatement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            transactionStatement.setString(1, Integer.toString(newActID));
            transactionStatement.setString(2, car.getCarID());
            transactionStatement.setString(3, employeeID);
            transactionStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            transactionStatement.executeUpdate();

            sql = "select finID from Finance order by ABS(finID)";
            statement = instance.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            searchResult = statement.executeQuery();
            if (searchResult.next()) {
                searchResult.last();
                newFinID = Integer.valueOf(searchResult.getString("finID")) + 1;
            }
            else
                newFinID = 1;
            sql = "insert into Finance values (?, ?, ?, '4', ?)";
            transactionStatement = instance.transactionConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            transactionStatement.setString(1, Integer.toString(newFinID));
            transactionStatement.setInt(2, amount);
            transactionStatement.setString(3, Integer.toString(newActID));
            transactionStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            transactionStatement.executeUpdate();

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

}


