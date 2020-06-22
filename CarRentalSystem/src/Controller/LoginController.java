package Controller;

import DataBase.Client.Client;
import DataBase.Client.ClientDao;
import DataBase.DBConnector;
import DataBase.Employee.Employee;
import DataBase.Employee.EmployeeDao;
import Main.Main;
import State.State;
import Util.Config;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.System.exit;

public class LoginController implements Initializable {

    @FXML
    Button Register_Btn;
    @FXML
    Button Login_Btn;
    @FXML
    Label LoginStatus_Label;
    @FXML
    private TextField Account_Input;
    @FXML
    private PasswordField PW_Input;
    @FXML
    private ComboBox<String> Identity_ComboBox;
    @FXML
    private GridPane Login_Pane;
    @FXML
    private Button Exit_Btn;
    @FXML
    private Button Config_Btn;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Identity_ComboBox.setItems(FXCollections.observableArrayList("Stuff", "Customer"));
        Identity_ComboBox.getSelectionModel().select(0);
        Account_Input.setText("1");
        PW_Input.setText("111");


        Login_Pane.setOnKeyPressed((keyValue)->{
            switch (keyValue.getCode()) {
                case ENTER:
                    Login();
                    break;
            }
        });

        Account_Input.setOnKeyPressed((keyValue) -> {
            switch (keyValue.getCode()){
                case ENTER:
                    Login();
                    break;
                case TAB:
                    PW_Input.requestFocus();
            }
        });

        PW_Input.setOnKeyPressed((keyValue) -> {
            switch (keyValue.getCode()){
                case ENTER:
                    Login();
                    break;
                case TAB:
                    Identity_ComboBox.requestFocus();
            }
        });

        Identity_ComboBox.setOnKeyPressed((keyValue)->{
            switch (keyValue.getCode()){
                case ENTER:
                    Login();
                    break;
                case SPACE:
                    Identity_ComboBox.show();
                    break;
                case TAB:
                    Account_Input.requestFocus();
                default:
                    break;
            }
        });

        Config_Btn.setOnMouseClicked(event -> {
            Scene scene = null;
            try {
                scene = new Scene(FXMLLoader.load(getClass().getResource("/View/DBConfig.fxml")));
                Main.getStage().setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    @FXML
    private void Login(){
        if (Account_Input.getText().isEmpty()){
            LoginStatus_Label.setText("Please Input Your Account.");
            LoginStatus_Label.setStyle("-fx-text-fill: red;");
            Account_Input.setStyle("-fx-background-color: pink");
            return;
        }else if (PW_Input.getText().isEmpty()){
            LoginStatus_Label.setText("Password cannot be Empty!");
            LoginStatus_Label.setStyle("-fx-text-fill: red;");
            PW_Input.setStyle("-fx-background-color: pink");
            return;
        }
        int userType;
        userType = Identity_ComboBox.getSelectionModel().getSelectedIndex();
        if (userType == 0) {
            ResultSet userResult = EmployeeDao.getEmployeeUserInfoByID(Account_Input.getText());
            if (userResult == null){
                LoginStatus_Label.setText("读取数据库错误，请联系管理员。");
                LoginStatus_Label.setStyle("-fx-text-fill: red;");
                return;
            }
            try {
                if (!userResult.next()) {
                    LoginStatus_Label.setText("用户不存在");
                    LoginStatus_Label.setStyle("-fx-text-fill: red;");
                    return;
                } else if (!userResult.getString("password").equals(PW_Input.getText())) {
                    LoginStatus_Label.setText("密码错误");
                    LoginStatus_Label.setStyle("-fx-text-fill: red;");
                    return;
                }
                Employee employee = new Employee(
                        userResult.getString("userID"),
                        userResult.getString("password"),
                        userResult.getString("clientID"),
                        userResult.getString("employeeID"),
                        userResult.getString("authority"),
                        userResult.getString("employeeID"),
                        userResult.getString("employeeName"),
                        userResult.getInt("age")
                        );
                DBConnector.getInstance().closeConnection();

                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " =Login: " + employee.toString());
                State.getINSTANCE().login(Config.Identity.Employee, null, employee);
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/View/EmployeeView.fxml")));
                Main.getStage().setScene(scene);

            }catch (SQLException | IOException e) {
                e.printStackTrace();
                return;
            }
        }else if (userType == 1){
            ResultSet userResult = ClientDao.getClientUserInfoByID(Account_Input.getText());
            if (userResult == null){
                LoginStatus_Label.setText("读取数据库错误，请联系管理员。");
                LoginStatus_Label.setStyle("-fx-text-fill: red;");
                return;
            }
            try {
                if (!userResult.next()) {
                    LoginStatus_Label.setText("用户不存在");
                    LoginStatus_Label.setStyle("-fx-text-fill: red;");
                    return;
                } else if (!userResult.getString("password").equals(PW_Input.getText())) {
                    LoginStatus_Label.setText("密码错误");
                    LoginStatus_Label.setStyle("-fx-text-fill: red;");
                    return;
                }
                Client client = new Client(userResult.getString("userID"),
                        userResult.getString("password"),
                        userResult.getString("clientID"),
                        userResult.getString("employeeID"),
                        userResult.getString("authority"),
                        userResult.getString("clientID"),
                        userResult.getString("clientName"),
                        userResult.getString("contact"),
                        userResult.getInt("credit"),
                        userResult.getBoolean("isVIP")
                        );
                DBConnector.getInstance().closeConnection();

                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " Login: " + client.toString());
                State.getINSTANCE().login(Config.Identity.Client, client, null);
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/View/ClientView.fxml")));
                Main.getStage().setScene(scene);

            }catch (SQLException | IOException e) {
                e.printStackTrace();
                return;
            }finally {
                DBConnector.getInstance().closeConnection();
            }
        }

    }

    @FXML
    private void Register(){
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/View/RegisterView.fxml")));
            scene.getStylesheets().add(getClass().getResource("/Resource/RegisterView.css").toExternalForm());
            Main.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void buttonExitClicked() {
        exit(0);
    }

    @FXML
    private void onAccountInput(){
        //Account_Input.setStyle("-fx-background-color: white");
    }

    @FXML
    private void onPasswordInput(){
        //PW_Input.setStyle("-fx-background-color: white");
    }


}
