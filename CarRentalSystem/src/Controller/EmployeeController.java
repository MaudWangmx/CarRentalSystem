package Controller;

import DataBase.Activity.ActivityDao;
import DataBase.Car.Car;
import DataBase.Car.CarDao;
import DataBase.Client.Client;
import DataBase.Client.ClientDao;
import DataBase.DBConnector;
import DataBase.Employee.Employee;
import DataBase.Employee.EmployeeDao;
import DataBase.Exception.DeleteException;
import DataBase.Exception.UpdateException;
import DataBase.LeaseRecord;
import DataBase.RankItem;
import DataBase.TableView.ActInfo;
import DataBase.TableView.CarTable;
import DataBase.TableView.ClientInfo;
import DataBase.TableView.EmployeeInfo;
import Main.Main;
import State.State;
import Util.Config;
import Util.DateConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static Util.Config.*;


public class EmployeeController {

    @FXML
    Label EmployeeWelCome_Label;
    @FXML
    Button EmployeeExit_Btn;
    @FXML
    Label EmployeeTip_Label;
    @FXML
    TabPane Employee_TabPane;
    @FXML
    Tab CarInfo_Tab;
    @FXML
    TextField CarInfoCarID_Input, CarInfoBrand_Input, CarInfoRentMin_Input, CarInfoRentMax_Input;
    @FXML
    ComboBox<String> CarInfoCondition_Combo;
    @FXML
    Button CarInfoSearch_Btn;
    @FXML
    TableView<CarTable> CarInfo_Table;
    @FXML
    TableColumn<CarTable, String> CarInfoCarID_Col, CarInfoBrand_Col, CarInfoRent_Col, CarInfoPledge_Col, CarInfoCondition_Col, CarInfoStatus_Col;
    @FXML
    TextField SwitchCarInfoCarID_Input, SwitchCarInfoBrand_Input, SwitchCarInfoRent_Input, SwitchCarInfoPledge_Input, SwitchCarInfoCondition_Input, SwitchCarInfoStatus_Input;
    @FXML
    Button CarInfoAdd_Btn, CarInfoUpdate_Btn, CarInfoConfirm_Btn, CarInfoDelete_Btn, CarInfoRepair_Btn;

    private ObservableList<CarTable> searchCarsTableList = FXCollections.observableArrayList();
    Car newCar;
    Car originCar;

    @FXML
    Tab ClientInfo_Tab;
    @FXML
    TextField ClientInfoName_Input, ClientInfoClientID_Input;
    @FXML
    ComboBox<String> ClientInfoIdentity_Combo;
    @FXML
    Button ClientInfoSearch_Btn;
    @FXML
    TableView<ClientInfo> ClientInfo_Table;
    @FXML
    TableColumn<ClientInfo, String> ClientInfoClientID_Col, ClientInfoName_Col, ClientInfoUserID_Col, ClientInfoPassword_Col, ClientInfoCredit_Col, ClientInfoIsVIP_Col, ClientInfoContact_Col;
    @FXML
    TextField SwitchClientInfoClientID_Input, SwitchClientInfoName_Input, SwitchClientInfoUserID_Input, SwitchClientInfoPassword_Input, SwitchClientInfoCredit_Input, SwitchClientInfoIsVIP_Input, SwitchClientInfoContact_Input;
    @FXML
    Button ClientInfoAdd_Btn, ClientInfoUpdate_Btn, ClientInfoConfirm_Btn, ClientInfoDelete_Btn;

    private ObservableList<ClientInfo> ClientInfoTableList = FXCollections.observableArrayList();
    Client newClient;
    Client originClient;

    @FXML
    Tab EmployeeInfo_Tab;
    @FXML
    TextField EmployeeInfoName_Input, EmployeeInfoEmployeeID_Input;
    @FXML
    ComboBox<String>  EmployeeInfoAuthority_Combo;
    @FXML
    TableView<EmployeeInfo> EmployeeInfo_Table;
    @FXML
    TableColumn<EmployeeInfo, String> EmployeeInfoEmployeeID_Col, EmployeeInfoName_Col, EmployeeInfoUserID_Col, EmployeeInfoPassword_Col, EmployeeInfoAge_Col, EmployeeInfoAuthority_Col;
    @FXML
    TextField SwitchEmployeeInfoEmployeeID_Input, SwitchEmployeeInfoName_Input, SwitchEmployeeInfoUserID_Input, SwitchEmployeeInfoPassword_Input, SwitchEmployeeInfoAge_Input, SwitchEmployeeInfoAuthority_Input;
    @FXML
    Button EmployeeInfoAdd_Btn, EmployeeInfoUpdate_Btn, EmployeeInfoConfirm_Btn, EmployeeInfoDelete_Btn;
    private ObservableList<EmployeeInfo> employeeInfoTableList = FXCollections.observableArrayList();
    Employee newEmployee;
    Employee originEmployee;

    @FXML
    Tab ActInfo_Tab;
    @FXML
    TextField ActInfoOptID_Input, ActInfoClientID_Input, ActInfoCarID_Input;
    @FXML
    ComboBox<String> ActInfoEvent_Combo;
    @FXML
    DatePicker ActInfoStartDate_Picker, ActInfoEndDate_Picker;
    @FXML
    CheckBox ActInfoAllDate_CheckBox, ActInfoThisMonth_CheckBox;
    @FXML
    Button ActInfoSearch_Btn;
    @FXML
    TableView<ActInfo> ActInfo_Table;
    @FXML
    TableColumn<ActInfo, String> ActInfoDateTime_Col, ActInfoActID_Col, ActInfoEvent_Col, ActInfoCarID_Col, ActInfoOptID_Col, ActInfoClientID_Col, ActInfoIncome_Col, ActInfoOutcome_Col;

    private ObservableList<ActInfo> actInfoTableList = FXCollections.observableArrayList();

    @FXML
    Tab TrafficFine_Tab;
    @FXML
    TextField TrafficFineCarID_Input, TrafficFineAmount_Input;
    @FXML
    Label TrafficFineClientID_Label, TrafficFineClientName_Label, TrafficFineActID_Label, TrafficFineLeaseTime_Label;
    @FXML
    Button TrafficFineSearch_Btn, TrafficFineSubmit_Btn;

    @FXML
    PieChart FinanceYear_PieChart, FinanceSeason_PieChart, FinanceMonth_PieChart;

    @FXML
    Button FinanceYear_Btn, FinanceMonth_Btn, FinanceSeason_Btn;
    @FXML
    Label FinanceIncome_Label, FinanceOutcome_Label, FinanceProfit_Label;
    @FXML
    AnchorPane Chart_Pane;
    @FXML
    Label CarBrandRank_Label1, CarBrandRank_Label2, CarBrandRank_Label3, ClientConsumption_Label1,ClientConsumption_Label2,ClientConsumption_Label3;

    private List<RankItem> carBrandRankList, clientConsumptionRankList;
    BarChart<String, Number> FinanceYear_BarChart;
    BarChart<String, Number> FinanceSeason_BarChart;
    BarChart<String, Number> FinanceMonth_BarChart;
    NumberAxis yAxis;

    @FXML
    Button DataChooseBackupFile_Btn, DataChooseRestoreFile_Btn, DataBackup_Btn, DataRestore_Btn;
    @FXML
    TextField DataBackupDir_Input, DataRestoreFile_Input;
    final FileChooser fileChooser = new FileChooser();
    final DirectoryChooser backupDirChooser =new DirectoryChooser();

    @FXML
    void initialize(){

        DataRestoreFile_Input.setDisable(true);
        DataBackupDir_Input.setDisable(true);
        DataChooseRestoreFile_Btn.setOnAction(
                (final ActionEvent e) -> {
                    EmployeeTip_Label.setText("");
                    String cmd = " mysql -uroot -p'maud050092' hospital < /Users/wangmingxuan/Desktop/DataBase/exp3/CarRentalSystem/data/hospital20200619.sql";
                    java.io.File restoreFile = fileChooser.showOpenDialog(Main.getStage());
                    if (restoreFile!=null && !restoreFile.toString().endsWith(".sql")){
                        EmployeeTip_Label.setText("Incorrect File Format.");
                        return;
                    }
                    if (restoreFile != null) {
                        DataRestoreFile_Input.setText(restoreFile.toString());
                    }
                });
        DataChooseBackupFile_Btn.setOnMouseClicked(event -> {
            java.io.File backupFolder = backupDirChooser.showDialog(Main.getStage());

            String dataFile = "/car_rental" +  DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + ".sql";
            System.out.println(backupFolder.toString() + dataFile);
            if (backupFolder != null) {
                DataBackupDir_Input.setText(backupFolder.toString() + dataFile);
            }
        });

        DataBackup_Btn.setOnMouseClicked(event -> {
            EmployeeTip_Label.setText("");
            if (DataBackupDir_Input.getText().trim().isEmpty()){
                EmployeeTip_Label.setText("Choose a File first.");
                return;
            }
            String dataFile = DataBackupDir_Input.getText().trim();
            try {

                File backupFile = new File(dataFile);
                String mysqlCom=String.format("mysqldump -u%s -p%s %s",Config.DB_MASTER_NAME,Config.DB_MASTER_PW,DB_NAME);
                String[] command = new String[] { "/bin/bash", "-c",mysqlCom};

                ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList(command));
                processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
                processBuilder.redirectOutput(ProcessBuilder.Redirect.to(backupFile));
                Process process = processBuilder.start();

                if( process.waitFor() == 0){
                    System.out.println("DataBase Backup Succeeded! File Path："+ dataFile);
                    EmployeeTip_Label.setText("DataBase Backup Succeeded! File Path："+ dataFile);
                    return;
                 }
            } catch (InterruptedException | IOException e) {
                EmployeeTip_Label.setText("Backup Failed. Try later.");
                e.printStackTrace();
            }

        });

        DataRestore_Btn.setOnMouseClicked(event -> {
            EmployeeTip_Label.setText("");
            if (DataRestoreFile_Input.getText().trim().isEmpty()){
                EmployeeTip_Label.setText("Choose a File first.");
                return;
            }
            String dataFile = DataRestoreFile_Input.getText().trim();

            String mysqlCom=String.format("mysql -u%s -p%s %s < %s",Config.DB_MASTER_NAME,Config.DB_MASTER_PW,DB_NAME, dataFile);
            String[] command = new String[] { "/bin/bash", "-c",mysqlCom};

            try {

                Process p = Runtime.getRuntime().exec(command);
                if(p.waitFor() == 0){
                    System.out.println("DataBase Restore Succeeded!");
                    EmployeeTip_Label.setText("DataBase Restore Succeeded!");
                    return;
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                EmployeeTip_Label.setText("DataBase Restore Failed. Try Later.");
            }
        });


        String identity = "";
        switch (Integer.valueOf(State.getINSTANCE().getEmployee().getAuthority())){
            case 1:
                identity = "Manager";
                break;
            case 2:
                identity = "Employee";
                break;
        }
        EmployeeWelCome_Label.setText("Welcome!      " + identity + "      " +  State.getINSTANCE().getEmployee().getEmployeeName() + ".");

        EmployeeExit_Btn.setOnMouseClicked(event -> {
            try {
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/View/LoginView.fxml")));
                scene.getStylesheets().add(getClass().getResource("/Resource/LoginView.css").toExternalForm());
                Main.getStage().setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        CarInfoCondition_Combo.setItems(FXCollections.observableArrayList("All", "Perfect", "Good", "Slightly Damaged", "Bad", "Unusable"));
        CarInfoCondition_Combo.getSelectionModel().select(0);
        onCarInfoSearchBtnClicked();
        ClientInfoIdentity_Combo.setItems(FXCollections.observableArrayList("All", "Ordinary", "VIP"));
        ClientInfoIdentity_Combo.getSelectionModel().select(0);
        SwitchClientInfoUserID_Input.setEditable(false);
        SwitchClientInfoClientID_Input.setEditable(false);
        SwitchClientInfoIsVIP_Input.setEditable(false);
        ActInfoEvent_Combo.setItems(FXCollections.observableArrayList("All", "Lease", "Return", "Damage", "Traffic Fine", "Repair"));
        ActInfoEvent_Combo.getSelectionModel().select(0);

        if (State.getINSTANCE().getEmployee().getAuthority().equals("1")) {
            TrafficFine_Tab.setDisable(true);
            ActInfoOptID_Input.setDisable(false);
        }
        if (State.getINSTANCE().getEmployee().getAuthority().equals("2")){
            ActInfoOptID_Input.setText(State.getINSTANCE().getEmployee().getEmployeeID());
            ActInfoOptID_Input.setDisable(true);
        }

        EmployeeInfo_Tab.setOnSelectionChanged(event -> {
            EmployeeInfoAuthority_Combo.setItems(FXCollections.observableArrayList("All", "Manager", "Ordinary"));
            if (State.getINSTANCE().getEmployee().getAuthority().equals("2")){
                EmployeeInfoAuthority_Combo.getSelectionModel().select(2);
                EmployeeInfoAuthority_Combo.setEditable(false);
                EmployeeInfoAuthority_Combo.setDisable(true);
                SwitchEmployeeInfoAuthority_Input.setDisable(true);
            }else {
                EmployeeInfoAuthority_Combo.getSelectionModel().select(0);
                EmployeeInfoAuthority_Combo.setDisable(false);
                SwitchEmployeeInfoAuthority_Input.setDisable(false);
            }
        });
        SwitchEmployeeInfoUserID_Input.setDisable(true);
        SwitchEmployeeInfoEmployeeID_Input.setDisable(true);

        ActInfo_Tab.setOnSelectionChanged(event -> {
            ActInfoStartDate_Picker.setConverter(new DateConverter());
            ActInfoEndDate_Picker.setConverter(new DateConverter());
            ActInfoStartDate_Picker.setValue(LocalDate.now());
            ActInfoEndDate_Picker.setValue(LocalDate.now());
            ActInfoAllDate_CheckBox.setSelected(false);
            ActInfoAllDate_CheckBox.setSelected(true);

        });

        ActInfoAllDate_CheckBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (ActInfoAllDate_CheckBox.isSelected()){
                ActInfoStartDate_Picker.setDisable(true);
                ActInfoEndDate_Picker.setDisable(true);
                ActInfoThisMonth_CheckBox.setSelected(false);
                ActInfoThisMonth_CheckBox.setDisable(true);
            }else {
                ActInfoStartDate_Picker.setDisable(false);
                ActInfoEndDate_Picker.setDisable(false);
                ActInfoThisMonth_CheckBox.setDisable(false);
            }
        }));

        ActInfoThisMonth_CheckBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (ActInfoThisMonth_CheckBox.isSelected()){
                ActInfoStartDate_Picker.setDisable(true);
                ActInfoEndDate_Picker.setDisable(true);
                ActInfoAllDate_CheckBox.setSelected(false);
                ActInfoAllDate_CheckBox.setDisable(true);
            }else {
                ActInfoStartDate_Picker.setDisable(false);
                ActInfoEndDate_Picker.setDisable(false);
                ActInfoAllDate_CheckBox.setDisable(false);
            }
        }));


        CarInfoCarID_Col.setCellValueFactory((TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().licensePlateProperty());
        CarInfoBrand_Col.setCellValueFactory((TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().brandProperty());
        CarInfoRent_Col.setCellValueFactory((TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().rentProperty());
        CarInfoPledge_Col.setCellValueFactory((TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().pledgeProperty());
        CarInfoStatus_Col.setCellValueFactory((TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().statusProperty());
        CarInfoCondition_Col.setCellValueFactory((TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().conditionProperty());
        CarInfo_Table.setItems(searchCarsTableList);

        CarInfoRentMin_Input.setOnKeyTyped(event -> {
            if (!CarInfoRentMin_Input.getText().trim().isEmpty()) {
                if (!CarInfoRentMin_Input.getText().trim().matches("[0-9]*")) {
                    EmployeeTip_Label.setText("Rent range should be numbers");
                } else {
                    EmployeeTip_Label.setText("");
                }
            }
        });

        CarInfoRentMax_Input.setOnKeyTyped(event -> {
            if (!CarInfoRentMax_Input.getText().trim().isEmpty()) {
                if (!CarInfoRentMax_Input.getText().trim().matches("[0-9]*")){
                    EmployeeTip_Label.setText("Rent range should be numbers");
                } else {
                    EmployeeTip_Label.setText("");
                }
            }
        });


        CarInfo_Table.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            if (CarInfo_Table.getSelectionModel().getSelectedIndex() != -1){
                if (CarInfo_Table.getSelectionModel().getSelectedItem().getCondition().equals("Perfect")){
                    CarInfoRepair_Btn.setDisable(true);
                }else {
                    CarInfoRepair_Btn.setDisable(false);
                }
            }
        }));

        SwitchClientInfoCredit_Input.setOnKeyTyped(value -> {
            EmployeeTip_Label.setText("");
            if (!SwitchClientInfoCredit_Input.getText().trim().matches("[0-9]*")) {
                EmployeeTip_Label.setText("Credit value should be a number.");
                return;
            }
            if (!SwitchClientInfoCredit_Input.getText().trim().isEmpty()) {
                if (Integer.parseInt(SwitchClientInfoCredit_Input.getText().trim()) >= VIP_CREDIT)
                    SwitchClientInfoIsVIP_Input.setText("Yes");
                else
                    SwitchClientInfoIsVIP_Input.setText("No");
            }
        });


        ClientInfoClientID_Col.setCellValueFactory((TableColumn.CellDataFeatures<ClientInfo, String> param) -> param.getValue().clientIDProperty());
        ClientInfoName_Col.setCellValueFactory((TableColumn.CellDataFeatures<ClientInfo, String> param) -> param.getValue().nameProperty());
        ClientInfoUserID_Col.setCellValueFactory((TableColumn.CellDataFeatures<ClientInfo, String> param) -> param.getValue().userIDProperty());
        ClientInfoPassword_Col.setCellValueFactory((TableColumn.CellDataFeatures<ClientInfo, String> param) -> param.getValue().passwordProperty());
        ClientInfoCredit_Col.setCellValueFactory((TableColumn.CellDataFeatures<ClientInfo, String> param) -> param.getValue().creditProperty());
        ClientInfoIsVIP_Col.setCellValueFactory((TableColumn.CellDataFeatures<ClientInfo, String> param) -> param.getValue().isVIPProperty());
        ClientInfoContact_Col.setCellValueFactory((TableColumn.CellDataFeatures<ClientInfo, String> param) -> param.getValue().contactProperty());
        ClientInfo_Table.setItems(ClientInfoTableList);


        EmployeeInfoEmployeeID_Col.setCellValueFactory((TableColumn.CellDataFeatures<EmployeeInfo, String> param) -> param.getValue().employeeIDProperty());
        EmployeeInfoName_Col.setCellValueFactory((TableColumn.CellDataFeatures<EmployeeInfo, String> param) -> param.getValue().nameProperty());
        EmployeeInfoUserID_Col.setCellValueFactory((TableColumn.CellDataFeatures<EmployeeInfo, String> param) -> param.getValue().userIDProperty());
        EmployeeInfoPassword_Col.setCellValueFactory((TableColumn.CellDataFeatures<EmployeeInfo, String> param) -> param.getValue().passwordProperty());
        EmployeeInfoAge_Col.setCellValueFactory((TableColumn.CellDataFeatures<EmployeeInfo, String> param) -> param.getValue().ageProperty());
        EmployeeInfoAuthority_Col.setCellValueFactory((TableColumn.CellDataFeatures<EmployeeInfo, String> param) -> param.getValue().authorityProperty());
        EmployeeInfo_Table.setItems(employeeInfoTableList);


        ActInfoDateTime_Col.setCellValueFactory((TableColumn.CellDataFeatures<ActInfo, String> param) -> param.getValue().dateTimeProperty());
        ActInfoActID_Col.setCellValueFactory((TableColumn.CellDataFeatures<ActInfo, String> param) -> param.getValue().actIDProperty());
        ActInfoEvent_Col.setCellValueFactory((TableColumn.CellDataFeatures<ActInfo, String> param) -> param.getValue().eventProperty());
        ActInfoCarID_Col.setCellValueFactory((TableColumn.CellDataFeatures<ActInfo, String> param) -> param.getValue().carIDProperty());
        ActInfoOptID_Col.setCellValueFactory((TableColumn.CellDataFeatures<ActInfo, String> param) -> param.getValue().optIDProperty());
        ActInfoClientID_Col.setCellValueFactory((TableColumn.CellDataFeatures<ActInfo, String> param) -> param.getValue().clientIDProperty());
        ActInfoIncome_Col.setCellValueFactory((TableColumn.CellDataFeatures<ActInfo, String> param) -> param.getValue().incomeProperty());
        ActInfoOutcome_Col.setCellValueFactory((TableColumn.CellDataFeatures<ActInfo, String> param) -> param.getValue().outcomeProperty());
        ActInfo_Table.setItems(actInfoTableList);

        TrafficFineCarID_Input.textProperty().addListener((observable, oldValue, newValue) -> {
            TrafficFineSubmit_Btn.setDisable(true);
            TrafficFineLeaseTime_Label.setText("");
            TrafficFineClientName_Label.setText("");
            TrafficFineClientID_Label.setText("");
            TrafficFineActID_Label.setText("");
        });
        TrafficFineAmount_Input.textProperty().addListener((observable) -> {
            EmployeeTip_Label.setText("");
            if (TrafficFineAmount_Input.getText().trim().isEmpty())
                TrafficFineSubmit_Btn.setDisable(true);
            else if (!TrafficFineAmount_Input.getText().trim().matches("[0-9]*")){
                EmployeeTip_Label.setText("Traffic Fine value is invalid. Amount should be a number.");
                TrafficFineSubmit_Btn.setDisable(true);
            }
            else
                if (!TrafficFineClientID_Label.getText().isEmpty())
                    TrafficFineSubmit_Btn.setDisable(false);
        });
        TrafficFineSubmit_Btn.setDisable(true);


        CategoryAxis seasonBar_XAxis = new CategoryAxis();
        seasonBar_XAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(
                "1-3", "4-6", "7-9", "10-12")));
        seasonBar_XAxis.setLabel("Season");

        CategoryAxis monthBar_XAxis = new CategoryAxis();
        monthBar_XAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")));
        monthBar_XAxis.setLabel("Month");

        yAxis = new NumberAxis();
        yAxis.setLabel("RMB");

        FinanceSeason_BarChart = new BarChart<>(seasonBar_XAxis, yAxis);
        FinanceMonth_BarChart = new BarChart<>(monthBar_XAxis, yAxis);

        FinanceMonth_BarChart.setMaxWidth(530);
        FinanceMonth_BarChart.setMaxHeight(260);
        FinanceMonth_BarChart.setPrefSize(530, 260);
        FinanceSeason_BarChart.setMaxWidth(530);
        FinanceSeason_BarChart.setMaxHeight(260);
        FinanceSeason_BarChart.setPrefSize(530, 260);
        Chart_Pane.getChildren().add(FinanceMonth_BarChart);
        Chart_Pane.getChildren().add(FinanceSeason_BarChart);
        FinanceMonth_BarChart.setVisible(false);
        FinanceSeason_BarChart.setVisible(false);
        FinanceSeason_PieChart.setVisible(false);
        FinanceMonth_PieChart.setVisible(false);

        onFinanceYearBtnClicked();
    }

    @FXML
    void onCarInfoSearchBtnClicked(){
        String brandInput = CarInfoBrand_Input.getText();
        String licensePlate = CarInfoCarID_Input.getText();
        String rentMin = CarInfoRentMin_Input.getText().trim();
        String rentMax = CarInfoRentMax_Input.getText().trim();
        int rentMinNum;
        int rentMaxNum;
        if (!rentMin.isEmpty())
            if (!rentMin.matches("[0-9]*")) {
                EmployeeTip_Label.setText("Rent range should be numbers");
                return;
            }
        if (!rentMax.isEmpty())
            if (!rentMax.matches("[0-9]*")) {
                EmployeeTip_Label.setText("Rent range should be numbers");
                return;
            }
        if (rentMin.isEmpty())
            rentMinNum = 0;
        else
            rentMinNum = Integer.parseInt(rentMin);
        if (rentMax.isEmpty())
            rentMaxNum = RENT_MAX;
        else
            rentMaxNum = Integer.parseInt(rentMax);
        int conditionSelection = CarInfoCondition_Combo.getSelectionModel().getSelectedIndex();
        conditionSelection = 6 - conditionSelection;
        searchCarsTableList.clear();
        ResultSet searchResult = CarDao.getSearchCarTable(brandInput, licensePlate, rentMinNum, rentMaxNum, Integer.toString(conditionSelection));
        try {
            while (searchResult.next()){
                searchCarsTableList.add(new CarTable(
                        searchResult.getString("carID"),
                        searchResult.getString("brand"),
                        searchResult.getString("condition"),
                        searchResult.getInt("rent"),
                        searchResult.getInt("pledge"),
                        searchResult.getBoolean("status")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBConnector.getInstance().closeConnection();
        }
        System.out.println(searchCarsTableList);
    }


    boolean switchCarInfoCheck(){
        EmployeeTip_Label.setText("");
        if (SwitchCarInfoCarID_Input.getText().isEmpty() ||
                SwitchCarInfoBrand_Input.getText().isEmpty() ||
                SwitchCarInfoRent_Input.getText().isEmpty() ||
                SwitchCarInfoPledge_Input.getText().isEmpty() ||
                SwitchCarInfoCondition_Input.getText().isEmpty() ||
                SwitchCarInfoStatus_Input.getText().isEmpty()){
            EmployeeTip_Label.setText("Information is not Complete.");
            return false;
        }
        if (!SwitchCarInfoRent_Input.getText().trim().matches("[0-9]*")){
            EmployeeTip_Label.setText("Rent value should be a number.");
            return false;
        }
        if (!SwitchCarInfoPledge_Input.getText().trim().matches("[0-9]*")){
            EmployeeTip_Label.setText("Pledge value should be a number.");
            return false;
        }
        if (!SwitchCarInfoCondition_Input.getText().trim().matches("[1-5]|(Unusable)|(Slightly Damaged)|(Bad)|(Perfect)|(Good)")){
            EmployeeTip_Label.setText("Condition value is invalid.");
            return false;
        }
        if (!SwitchCarInfoStatus_Input.getText().trim().matches("[01]|(Leasable)|(Leased)")){
            EmployeeTip_Label.setText("Status value is invalid.");
            return false;
        }
        boolean status = (SwitchCarInfoStatus_Input.getText().trim().equals("1") || SwitchCarInfoStatus_Input.getText().equals("Leasable"));
        String condition = "5";
        switch (SwitchCarInfoCondition_Input.getText().trim()){
            case "Perfect":
            case "5":
                condition = "5";
                break;
            case "Good":
            case "4":
                condition = "4";
                break;
            case "Slightly Damaged":
            case "3":
                condition = "3";
                break;
            case "Bad":
            case "2":
                condition = "2";
                break;
            case "Unusable":
            case "1":
                condition = "1";
                break;
        }
        newCar = new Car(SwitchCarInfoCarID_Input.getText().trim(),
                SwitchCarInfoBrand_Input.getText().trim(),
                status,
                Integer.parseInt(SwitchCarInfoRent_Input.getText().trim()),
                Integer.parseInt(SwitchCarInfoPledge_Input.getText().trim()),
                condition
        );
        return true;
    }

    @FXML
    void onCarInfoAddBtnClicked(){
        if (!switchCarInfoCheck())
            return;
        if (CarDao.addACar(newCar)){
            EmployeeTip_Label.setText("Insert Succeed!");
            onCarInfoSearchBtnClicked();
        }
        else{
            EmployeeTip_Label.setText("Duplicate License. Please Check.");
        }
    }

    @FXML
    void onCarInfoUpdateBtnClicked(){
        EmployeeTip_Label.setText("");
        if (CarInfo_Table.getSelectionModel().getSelectedIndex() == -1){
            EmployeeTip_Label.setText("Please select a Row in the table first.");
            return;
        }
        CarTable updateCar = CarInfo_Table.getSelectionModel().getSelectedItem();
        SwitchCarInfoCarID_Input.setText(updateCar.getLicensePlate());
        SwitchCarInfoBrand_Input.setText(updateCar.getBrand());
        SwitchCarInfoRent_Input.setText(updateCar.getRent());
        SwitchCarInfoPledge_Input.setText(updateCar.getPledge());
        SwitchCarInfoStatus_Input.setText(updateCar.getStatus());
        SwitchCarInfoCondition_Input.setText(updateCar.getCondition());
        String condition = "5";
        switch (updateCar.getCondition().trim()){
            case "Perfect":
            case "5":
                condition = "5";
                break;
            case "Good":
            case "4":
                condition = "4";
                break;
            case "Slightly Damaged":
            case "3":
                condition = "3";
                break;
            case "Bad":
            case "2":
                condition = "2";
                break;
            case "Unusable":
            case "1":
                condition = "1";
                break;
        }
        boolean status = (CarInfo_Table.getSelectionModel().getSelectedItem().getStatus().trim().equals("1") || CarInfo_Table.getSelectionModel().getSelectedItem().getStatus().trim().equals("Leasable"));
        originCar = new Car(updateCar.getLicensePlate(),
                updateCar.getBrand(),
                status,
                Integer.parseInt(updateCar.getRent()),
                Integer.parseInt(updateCar.getPledge()),
                condition);
    }

    @FXML
    void onCarInfoConfirmBtnClicked(){
        if (!switchCarInfoCheck())
            return;
        try {
            if (CarDao.updateACar(newCar, originCar)){
                EmployeeTip_Label.setText("Update Succeed!");
                onCarInfoSearchBtnClicked();
            }
        } catch (UpdateException e) {
            EmployeeTip_Label.setText(e.getReason());
        }
    }

    @FXML
    void onCarInfoDeleteBtnClicked(){
        EmployeeTip_Label.setText("");
        if (CarInfo_Table.getSelectionModel().getSelectedIndex() == -1){
            EmployeeTip_Label.setText("Please select a Row in the table first.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setContentText("Car: " + CarInfo_Table.getSelectionModel().getSelectedItem().getLicensePlate());
        alert.setHeaderText("Are you sure to delete the selected Car?");


        alert.showAndWait().ifPresent(response ->{
            if (response == ButtonType.OK){

                CarTable deleteCar = CarInfo_Table.getSelectionModel().getSelectedItem();
                String condition = "5";
                switch (deleteCar.getCondition().trim()){
                    case "Perfect":
                    case "5":
                        condition = "5";
                        break;
                    case "Good":
                    case "4":
                        condition = "4";
                        break;
                    case "Slightly Damaged":
                    case "3":
                        condition = "3";
                        break;
                    case "Bad":
                    case "2":
                        condition = "2";
                        break;
                    case "Unusable":
                    case "1":
                        condition = "1";
                        break;
                }
                boolean status = (CarInfo_Table.getSelectionModel().getSelectedItem().getStatus().trim().equals("1") || CarInfo_Table.getSelectionModel().getSelectedItem().getStatus().trim().equals("Leasable"));
                newCar = new Car(deleteCar.getLicensePlate(),
                        deleteCar.getBrand(),
                        status,
                        Integer.parseInt(deleteCar.getRent()),
                        Integer.parseInt(deleteCar.getPledge()),
                        condition);
                try {
                    if (CarDao.deleteACar(newCar)){
                        EmployeeTip_Label.setText("Car " + newCar.getCarID() + " Deleted!");
                        onCarInfoSearchBtnClicked();
                    }
                } catch (DeleteException e) {
                    EmployeeTip_Label.setText(e.getReason());
                }
            }
        });
    }

    @FXML
    void onCarInfoRepairBtnClicked(){
        EmployeeTip_Label.setText("");
        if (CarInfo_Table.getSelectionModel().getSelectedIndex() == -1){
            EmployeeTip_Label.setText("Please select a Row in the table first.");
            return;
        }
        CarTable repairCarInfo = CarInfo_Table.getSelectionModel().getSelectedItem();
        if (repairCarInfo.getCondition().equals("Perfect")){
            EmployeeTip_Label.setText("The selected Car doesn't need to be repaired.");
            return;
        }
        String condition = "5";
        switch (repairCarInfo.getCondition().trim()){
            case "Perfect":
            case "5":
                condition = "5";
                break;
            case "Good":
            case "4":
                condition = "4";
                break;
            case "Slightly Damaged":
            case "3":
                condition = "3";
                break;
            case "Bad":
            case "2":
                condition = "2";
                break;
            case "Unusable":
            case "1":
                condition = "1";
                break;
        }
        Car repairCar = new Car(repairCarInfo.getLicensePlate(),
                repairCarInfo.getBrand(),
                repairCarInfo.getStatus().trim().equals("Leasable"),
                Integer.parseInt(repairCarInfo.getRent()),
                Integer.parseInt(repairCarInfo.getPledge()),
                condition);
        int repairFee = repairCar.getRent() * (5 - Integer.parseInt(condition));
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Repair Confirmation");
        alert.setContentText("Car: " + CarInfo_Table.getSelectionModel().getSelectedItem().getLicensePlate() +
                "     Repair Fee: " + repairFee);
        alert.setHeaderText("Are you sure to repair the selected Car?");


        alert.showAndWait().ifPresent(response ->{
            if (response == ButtonType.OK)
            {
                try {
                    if (CarDao.repairACar(repairCar, State.getINSTANCE().getEmployee().getEmployeeID(), repairFee)){
                        EmployeeTip_Label.setText("Car Repaired.");
                        onCarInfoSearchBtnClicked();
                    }
                } catch (UpdateException e) {
                    EmployeeTip_Label.setText(e.getReason());
                }}
        });
    }

    @FXML
    void onClientInfoSearchBtnClicked(){
        ResultSet searchResult = ClientDao.getClientUserTable(ClientInfoName_Input.getText().trim(),
                ClientInfoClientID_Input.getText().trim(),
                ClientInfoIdentity_Combo.getSelectionModel().getSelectedIndex());
        try {
            ClientInfoTableList.clear();
            while (searchResult.next()){
                ClientInfoTableList.add(new ClientInfo(searchResult.getString("Client.clientID"),
                        searchResult.getString("clientName"),
                        searchResult.getString("userID"),
                        searchResult.getString("password"),
                        searchResult.getInt("credit"),
                        searchResult.getBoolean("isVIP"),
                        searchResult.getString("contact")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnector.getInstance().closeConnection();
        }
    }

    private boolean switchClientInfoCheck(int op){
        EmployeeTip_Label.setText("");
        if (op == 1){                // case add
            if (SwitchClientInfoName_Input.getText().trim().isEmpty() ||
                    SwitchClientInfoPassword_Input.getText().trim().isEmpty() ||
                    SwitchClientInfoCredit_Input.getText().trim().isEmpty() ||
                    SwitchClientInfoIsVIP_Input.getText().trim().isEmpty()
            ) {
                EmployeeTip_Label.setText("Information is not Complete.");
                return false;
            }
        }
        if (op == 2) {              //case update
            if (SwitchClientInfoClientID_Input.getText().trim().isEmpty() ||
                    SwitchClientInfoName_Input.getText().trim().isEmpty() ||
                    SwitchClientInfoUserID_Input.getText().trim().isEmpty() ||
                    SwitchClientInfoPassword_Input.getText().trim().isEmpty() ||
                    SwitchClientInfoCredit_Input.getText().trim().isEmpty() ||
                    SwitchClientInfoIsVIP_Input.getText().trim().isEmpty()
            ) {
                EmployeeTip_Label.setText("Information is not Complete.");
                return false;
            }
        }
        if (!SwitchClientInfoCredit_Input.getText().trim().matches("[0-9]*")){
            EmployeeTip_Label.setText("Credit value should be a number.");
            return false;
        }
        boolean isVIP = false;
        if (Integer.parseInt(SwitchClientInfoCredit_Input.getText().trim()) >= VIP_CREDIT)
            isVIP = true;
        SwitchClientInfoIsVIP_Input.setText("Yes");
        newClient = new Client(SwitchClientInfoUserID_Input.getText().trim(),
                SwitchClientInfoPassword_Input.getText().trim(),
                SwitchClientInfoClientID_Input.getText().trim(),
                "",
                "3",
                SwitchClientInfoClientID_Input.getText().trim(),
                SwitchClientInfoName_Input.getText().trim(),
                SwitchClientInfoContact_Input.getText().trim(),
                Integer.parseInt(SwitchClientInfoCredit_Input.getText().trim()),
                isVIP
        );
        return true;
    }

    @FXML
    void onClientInfoAddBtnClicked(){
        if (!switchClientInfoCheck(1)){
            return;
        }
        if(ClientDao.addAClient(newClient)){
            EmployeeTip_Label.setText("Add Succeeded. UserID: " + newClient.getUserID() + "     ClientID: " + newClient.getClientID() + ".");
            SwitchClientInfoClientID_Input.setText(newClient.getClientID());
            SwitchClientInfoUserID_Input.setText(newClient.getUserID());
            onClientInfoSearchBtnClicked();
        }else {
            EmployeeTip_Label.setText("System Error. Please try later.");
        }
    }

    @FXML
    void onClientInfoUpdateBtnClicked(){
        EmployeeTip_Label.setText("");
        if (ClientInfo_Table.getSelectionModel().getSelectedIndex() == -1){
            EmployeeTip_Label.setText("Please select a Row in the table first.");
            return;
        }
        ClientInfo updateClient = ClientInfo_Table.getSelectionModel().getSelectedItem();
        SwitchClientInfoClientID_Input.setText(updateClient.getClientID());
        SwitchClientInfoName_Input.setText(updateClient.getName());
        SwitchClientInfoUserID_Input.setText(updateClient.getUserID());
        SwitchClientInfoPassword_Input.setText(updateClient.getPassword());
        SwitchClientInfoCredit_Input.setText(updateClient.getCredit());
        SwitchClientInfoIsVIP_Input.setText(updateClient.getIsVIP());
        SwitchClientInfoContact_Input.setText(updateClient.getContact());

        originClient = new Client(updateClient.getUserID(),
                updateClient.getPassword(),
                updateClient.getClientID(),
                "",
                "3",
                updateClient.getClientID(),
                updateClient.getName(),
                updateClient.getContact(),
                Integer.parseInt(updateClient.getCredit()),
                (updateClient.getIsVIP().equals("Yes"))
        );
    }

    @FXML
    void onClientInfoConfirmBtnClicked(){
        EmployeeTip_Label.setText("");
        if (!switchClientInfoCheck(2))
            return;
        try {
            if (ClientDao.updateAClient(newClient, originClient)){
                EmployeeTip_Label.setText("Update Succeeded!");
                onClientInfoSearchBtnClicked();
            }
        } catch (UpdateException e) {
            EmployeeTip_Label.setText(e.getReason());
        }
    }

    @FXML
    void onClientInfoDeleteBtnClicked(){
        EmployeeTip_Label.setText("");
        if (ClientInfo_Table.getSelectionModel().getSelectedIndex() == -1){
            EmployeeTip_Label.setText("Please select a Row in the table first.");
            return;
        }
        ClientInfo deleteClient = ClientInfo_Table.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setContentText("Client" + deleteClient.getClientID() + ": " + deleteClient.getName());
        alert.setHeaderText("Are you sure to delete the selected Client's information?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK){
                newClient = new Client(deleteClient.getUserID().trim(),
                        deleteClient.getPassword().trim(),
                        deleteClient.getClientID().trim(),
                        "",
                        "3",
                        deleteClient.getClientID().trim(),
                        deleteClient.getName().trim(),
                        deleteClient.getContact().trim(),
                        Integer.parseInt(deleteClient.getCredit().trim()),
                        deleteClient.getIsVIP().trim().equals("Yes")
                );
                try {
                    if (ClientDao.deleteAClient(newClient)){
                        EmployeeTip_Label.setText("Client" + newClient.getClientID() + ": " + newClient.getClientName() + "'s Information Deleted!");
                    }
                } catch (DeleteException e) {
                    EmployeeTip_Label.setText(e.getReason());
                }
            }
        });
    }

    @FXML
    void onEmployeeInfoSearchBtnClicked(){
        ResultSet searchResult = EmployeeDao.getEmployeeUserTable(EmployeeInfoName_Input.getText().trim(),
                EmployeeInfoEmployeeID_Input.getText().trim(),
                EmployeeInfoAuthority_Combo.getSelectionModel().getSelectedIndex());
        try {
            employeeInfoTableList.clear();
            while (searchResult.next()){
                employeeInfoTableList.add(new EmployeeInfo(searchResult.getString("Employee.employeeID"),
                        searchResult.getString("employeeName"),
                        searchResult.getString("userID"),
                        searchResult.getString("password"),
                        searchResult.getInt("age"),
                        searchResult.getString("authority")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnector.getInstance().closeConnection();
        }
    }

    private boolean switchEmployeeInfoCheck(int op){
        EmployeeTip_Label.setText("");
        String authority = "2";
        if (op == 1){       // add operation
            switch(Integer.parseInt(State.getINSTANCE().getEmployee().getAuthority())){
                default:
                case 1:         //Manager
                {
                    if (SwitchEmployeeInfoName_Input.getText().trim().isEmpty() ||
                    SwitchEmployeeInfoPassword_Input.getText().trim().isEmpty() ||
                    SwitchEmployeeInfoAuthority_Input.getText().trim().isEmpty())
                    {
                        EmployeeTip_Label.setText("Information is incomplete. Please enter Employee name, password and authority.");
                        return false;
                    }
                    if (!SwitchEmployeeInfoAuthority_Input.getText().trim().matches("[12]|(Manager)|(Ordinary Employee)|(Ordinary)"))
                    {
                        EmployeeTip_Label.setText("Authority value is invalid.");
                        return false;
                    }

                    authority = "2";
                    if (SwitchEmployeeInfoAuthority_Input.getText().trim().equals("Manager") || SwitchEmployeeInfoAuthority_Input.getText().trim().equals("1"))
                            authority = "1";
                    break;
                }
                case 2: {
                    SwitchEmployeeInfoAuthority_Input.setText("Ordinary Employee");
                    if (SwitchEmployeeInfoName_Input.getText().trim().isEmpty() ||
                            SwitchEmployeeInfoPassword_Input.getText().trim().isEmpty()) {
                        EmployeeTip_Label.setText("Information is incomplete. Please enter Employee name, password.");
                        return false;
                    }
                    authority = "2";
                    break;
                }
            }
        }
        else if (op == 2){  // update operation
            switch(Integer.parseInt(State.getINSTANCE().getEmployee().getAuthority())){
                default:
                case 1:         //Manager
                {
                    if (SwitchEmployeeInfoName_Input.getText().trim().isEmpty() ||
                            SwitchEmployeeInfoPassword_Input.getText().trim().isEmpty() ||
                            SwitchEmployeeInfoAuthority_Input.getText().trim().isEmpty() ||
                            SwitchEmployeeInfoUserID_Input.getText().trim().isEmpty() ||
                            SwitchEmployeeInfoEmployeeID_Input.getText().trim().isEmpty())
                    {
                        EmployeeTip_Label.setText("Information is incomplete. Please enter Employee name, password and authority.");
                        return false;
                    }
                    if (!SwitchEmployeeInfoAuthority_Input.getText().trim().matches("[12]|(Manager)|(Ordinary Employee)|(Ordinary)"))
                    {
                        EmployeeTip_Label.setText("Authority value is invalid.");
                        return false;
                    }
                    authority = "2";
                    if (SwitchEmployeeInfoAuthority_Input.getText().trim().equals("Manager") || SwitchEmployeeInfoAuthority_Input.getText().trim().equals("1"))
                        authority = "1";
                    break;
                }
                case 2:
                {
                    if (SwitchEmployeeInfoName_Input.getText().trim().isEmpty() ||
                            SwitchEmployeeInfoPassword_Input.getText().trim().isEmpty() ||
                            SwitchEmployeeInfoAuthority_Input.getText().trim().isEmpty() ||
                            SwitchEmployeeInfoUserID_Input.getText().trim().isEmpty() ||
                            SwitchEmployeeInfoEmployeeID_Input.getText().trim().isEmpty())
                    {
                        EmployeeTip_Label.setText("Information is incomplete. Please enter Employee name, password.");
                        return false;
                    }
                    authority = "2";
                    break;
                }

            }
        }
        newEmployee = new Employee(SwitchClientInfoUserID_Input.getText().trim(),
                SwitchEmployeeInfoPassword_Input.getText().trim(),
                "",
                SwitchEmployeeInfoEmployeeID_Input.getText().trim(),
                authority,
                SwitchEmployeeInfoEmployeeID_Input.getText().trim(),
                SwitchEmployeeInfoName_Input.getText().trim(),
                SwitchEmployeeInfoAge_Input.getText().trim().isEmpty()? 0:Integer.parseInt(SwitchEmployeeInfoAge_Input.getText().trim())
        );
        return true;
    }

    @FXML
    void onEmployeeInfoAddBtnClicked(){
        if (!switchEmployeeInfoCheck(1)){
            return;
        }
        if(EmployeeDao.addAEmployee(newEmployee)){
            EmployeeTip_Label.setText("Add Succeeded. UserID: " + newEmployee.getUserID() + "     EmployeeID: " + newEmployee.getEmployeeID() + ".");
            SwitchEmployeeInfoUserID_Input.setText(newEmployee.getUserID());
            SwitchEmployeeInfoEmployeeID_Input.setText(newEmployee.getEmployeeID());
            SwitchEmployeeInfoAuthority_Input.setText((newEmployee.getAuthority().equals("Manager")) ? "Manager" : "Ordinary Employee");
            onEmployeeInfoSearchBtnClicked();
        }else {
            EmployeeTip_Label.setText("System Error. Please try later.");
        }
    }

    @FXML
    void onEmployeeInfoUpdateBtnClicked(){
        EmployeeTip_Label.setText("");
        if (EmployeeInfo_Table.getSelectionModel().getSelectedIndex() == -1){
            EmployeeTip_Label.setText("Please select a Row in the table first.");
            return;
        }
        EmployeeInfo updateEmployee = EmployeeInfo_Table.getSelectionModel().getSelectedItem();
        SwitchEmployeeInfoEmployeeID_Input.setText(updateEmployee.getEmployeeID());
        SwitchEmployeeInfoName_Input.setText(updateEmployee.getName());
        SwitchEmployeeInfoUserID_Input.setText(updateEmployee.getUserID());
        SwitchEmployeeInfoPassword_Input.setText(updateEmployee.getPassword());
        SwitchEmployeeInfoAuthority_Input.setText(updateEmployee.getAuthority());
        SwitchEmployeeInfoAge_Input.setText(updateEmployee.getAge());


        originEmployee = new Employee(updateEmployee.getUserID().trim(),
                updateEmployee.getPassword().trim(),
                "",
                updateEmployee.getEmployeeID().trim(),
                (updateEmployee.getAuthority().trim().equals("Manager")) ? "1" : "2",
                updateEmployee.getEmployeeID().trim(),
                updateEmployee.getName().trim(),
                Integer.parseInt(updateEmployee.getAge().trim())
        );
    }

    @FXML
    void onEmployeeInfoConfirmBtnClicked(){
        EmployeeTip_Label.setText("");
        if (!switchEmployeeInfoCheck(2))
            return;
        try {
            if (EmployeeDao.updateAEmployee(newEmployee, originEmployee)){
                EmployeeTip_Label.setText("Update Succeeded!");
                onEmployeeInfoSearchBtnClicked();
            }
        } catch (UpdateException e) {
            EmployeeTip_Label.setText(e.getReason());
        }
    }

    @FXML
    void onEmployeeInfoDeleteBtnClicked(){
        EmployeeTip_Label.setText("");
        if (EmployeeInfo_Table.getSelectionModel().getSelectedIndex() == -1){
            EmployeeTip_Label.setText("Please select a Row in the table first.");
            return;
        }
        EmployeeInfo deleteEmployee = EmployeeInfo_Table.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setContentText(deleteEmployee.getAuthority() + deleteEmployee.getEmployeeID() + ": " + deleteEmployee.getName());
        alert.setHeaderText("Are you sure to delete the selected Employee's information?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK){
                newEmployee = new Employee(deleteEmployee.getUserID().trim(),
                        deleteEmployee.getPassword().trim(),
                        "",
                        deleteEmployee.getEmployeeID(),
                        (deleteEmployee.getAuthority().equals("Manager") ? "1" : "2"),
                        deleteEmployee.getEmployeeID(),
                        deleteEmployee.getName(),
                        Integer.parseInt(deleteEmployee.getAge())
                );

                try {
                    if (EmployeeDao.deleteAEmployee(newEmployee)){
                        EmployeeTip_Label.setText(deleteEmployee.getAuthority() + newEmployee.getEmployeeID() + ": " + newEmployee.getEmployeeID() + "'s Information Deleted!");
                        onEmployeeInfoSearchBtnClicked();
                    }
                } catch (DeleteException e) {
                    EmployeeTip_Label.setText(e.getReason());
                }
            }
        });
    }

    @FXML
    void onActInfoSearchBtnClicked(){
        String startTimeStamp;
        String endTimeStamp;
        ResultSet searchResult;
        ActInfo returnActInfo;
        int trafficFine = 0;
        int pledge = 0;
        if (ActInfoAllDate_CheckBox.isSelected()){
            startTimeStamp = "0000-00-00 00:00:00";
            //startTimeStamp = Timestamp.valueOf(LocalDateTime.parse("0000-00-00 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //endTimeStamp = Timestamp.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            endTimeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        }
        else if (ActInfoThisMonth_CheckBox.isSelected()){
            startTimeStamp = LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-01 00:00:00";
            endTimeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
//            startTimeStamp = Timestamp.valueOf(LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonth() + "-01 00:00:00");
//            endTimeStamp = Timestamp.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        }else {
            if (ActInfoStartDate_Picker.getValue().isAfter(ActInfoEndDate_Picker.getValue())) {
                EmployeeTip_Label.setText("Start Time should be a Date before End Time.");
                return;
            }
            startTimeStamp = ActInfoStartDate_Picker.getValue() + " 00:00:00";
            endTimeStamp = ActInfoEndDate_Picker.getValue() + " 24:00:00";
//            startTimeStamp = Timestamp.valueOf(RecordStartDate_Picker.getValue() + " 00:00:00");
//            endTimeStamp = Timestamp.valueOf(RecordEndDate_Picker.getValue() + " 24:00:00");
        }
        try {
            searchResult = ActivityDao.getActInfoTable(ActInfoOptID_Input.getText().trim(),
                    ActInfoClientID_Input.getText().trim(),
                    ActInfoCarID_Input.getText().trim(),
                    ActInfoEvent_Combo.getSelectionModel().getSelectedIndex(),
                    startTimeStamp,
                    endTimeStamp);
            actInfoTableList.clear();
            while (searchResult.next()){
                switch (Integer.parseInt(searchResult.getString("fintype"))){
                    case 0:         // lease pledge income
                    case 2:         // damage fine income
                    {
                        actInfoTableList.add(new ActInfo(searchResult.getTimestamp("date"),
                                searchResult.getString("Activity.actID"),
                                searchResult.getString("event"),
                                searchResult.getString("carID"),
                                searchResult.getString("employeeID"),
                                searchResult.getString("clientID"),
                                searchResult.getInt("amount"),
                                0));
                        break;
                    }
                    case 3:     // traffic fine outcome
                    case 4:     // repair fee outcome
                    {
                        actInfoTableList.add(new ActInfo(searchResult.getTimestamp("date"),
                                searchResult.getString("Activity.actID"),
                                searchResult.getString("event"),
                                searchResult.getString("carID"),
                                searchResult.getString("employeeID"),
                                searchResult.getString("clientID"),
                                0,
                                searchResult.getInt("amount")));
                        break;
                    }
                    case 1:         // return activity
                    {
                        returnActInfo = new ActInfo(searchResult.getTimestamp("date"),
                                searchResult.getString("Activity.actID"),
                                searchResult.getString("event"),
                                searchResult.getString("carID"),
                                searchResult.getString("employeeID"),
                                searchResult.getString("clientID"),
                                searchResult.getInt("amount"),
                                0);             // rent income
                        if (searchResult.next())
                            pledge = searchResult.getInt("amount");
                        if (searchResult.next())
                            trafficFine = searchResult.getInt("amount");
                        if (pledge >= trafficFine){
                            returnActInfo.setOutcome(String.valueOf(pledge - trafficFine));
                        }else {
                            returnActInfo.setOutcome("0");
                            returnActInfo.setIncome(String.valueOf(Integer.parseInt(returnActInfo.getIncome()) + trafficFine - pledge));
                        }
                        actInfoTableList.add(returnActInfo);
                        break;
                    }
                    case 5:
                    case 6:
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnector.getInstance().closeConnection();
        }
    }

    @FXML
    void onTrafficFineSearchBtnClicked(){
        EmployeeTip_Label.setText("");
        TrafficFineActID_Label.setText("");
        TrafficFineClientID_Label.setText("");
        TrafficFineClientName_Label.setText("");
        TrafficFineLeaseTime_Label.setText("");
        if (TrafficFineCarID_Input.getText().trim().isEmpty()){
            EmployeeTip_Label.setText("Please input the License first");
        }
        try {
            LeaseRecord leaseRecord = ActivityDao.getLeaseRecordByCarID(TrafficFineCarID_Input.getText().trim());
            if (leaseRecord != null){
                TrafficFineActID_Label.setText(leaseRecord.getActID());
                TrafficFineClientID_Label.setText(leaseRecord.getClientID());
                TrafficFineClientName_Label.setText(ClientDao.getClientNameByID(leaseRecord.getClientID()));
                TrafficFineLeaseTime_Label.setText(leaseRecord.getLeaseDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        } catch (UpdateException e) {
            EmployeeTip_Label.setText(e.getReason());
        }

    }

    @FXML
    void onTrafficFineSubmitBtnClicked(){
        EmployeeTip_Label.setText("");
        if (TrafficFineActID_Label.getText().trim().isEmpty() ||
                TrafficFineClientID_Label.getText().isEmpty() ||
                TrafficFineClientName_Label.getText().isEmpty() ||
                TrafficFineLeaseTime_Label.getText().isEmpty() ||
                TrafficFineAmount_Input.getText().trim().isEmpty() ||
                TrafficFineCarID_Input.getText().trim().isEmpty())
        {
         EmployeeTip_Label.setText("Information Incomplete.");
         return;
        }
        if (!TrafficFineAmount_Input.getText().trim().matches("[0-9]*")){
            EmployeeTip_Label.setText("Traffic Fine value is invalid. Amount should be a number.");
            return;
        }
        try {
            if (ActivityDao.importATrafficFine(TrafficFineCarID_Input.getText().trim(),
                    TrafficFineClientID_Label.getText().trim(),
                    State.getINSTANCE().getEmployee().getEmployeeID(),
                    TrafficFineActID_Label.getText().trim(),
                    Integer.parseInt(TrafficFineAmount_Input.getText().trim()))){
                EmployeeTip_Label.setText("Traffic Fine upload Succeeded.");
            }
        } catch (UpdateException e) {
            EmployeeTip_Label.setText(e.getReason());
        }

    }

    @FXML
    void onFinanceYearBtnClicked(){
        FinanceSeason_PieChart.setVisible(false);
        FinanceSeason_BarChart.setVisible(false);
        FinanceMonth_PieChart.setVisible(false);
        FinanceMonth_BarChart.setVisible(false);
        Chart_Pane.getChildren().remove(FinanceYear_BarChart);

        int thisYear = LocalDateTime.now().getYear();
        String startTime, endTime;
        int profit5, profit4, profit3, profit2, profit1;
        CategoryAxis yearXAxis = new CategoryAxis();
        yearXAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(
                String.valueOf(thisYear - 4),
                String.valueOf(thisYear - 3),
                String.valueOf(thisYear - 2),
                String.valueOf(thisYear - 1),
                String.valueOf(thisYear)
        )));

        FinanceYear_BarChart = new BarChart<>(yearXAxis, yAxis);
        FinanceYear_BarChart.getData().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Income");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Outcome");
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series3.setName("Profit");

        startTime = (thisYear - 4) + "-01-01 00:00:00";
        endTime = (thisYear-4) + "-12-31 24:59:59";
        profit5 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 4), ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 4), ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 4), profit5));

        startTime = (thisYear - 3) + "-01-01 00:00:00";
        endTime = (thisYear-3) + "-12-31 24:59:59";
        profit4 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 3), ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 3), ActivityDao.getIncome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 3), profit4));

        startTime = (thisYear - 2) + "-01-01 00:00:00";
        endTime = (thisYear-2) + "-12-31 24:59:59";
        profit3 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 2), ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 2), ActivityDao.getIncome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 2), profit3));

        startTime = (thisYear - 1) + "-01-01 00:00:00";
        endTime = (thisYear-1) + "-12-31 24:59:59";
        profit2 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 1), ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 1), ActivityDao.getIncome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>(String.valueOf(thisYear - 1), profit2));

        startTime = thisYear + "-01-01 00:00:00";
        endTime = thisYear + "-12-31 24:59:59";
        profit1 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>(String.valueOf(thisYear), ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>(String.valueOf(thisYear), ActivityDao.getIncome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>(String.valueOf(thisYear), profit1));

        FinanceYear_BarChart.getData().addAll(series1, series2, series3);

        FinanceYear_BarChart.setMaxWidth(530);
        FinanceYear_BarChart.setMaxHeight(260);
        Chart_Pane.getChildren().add(FinanceYear_BarChart);
        FinanceYear_BarChart.setVisible(true);
        FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
        FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
        FinanceProfit_Label.setText(String.valueOf(profit1));
        carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
        clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);




        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data(String.valueOf(thisYear - 4), profit5),
                        new PieChart.Data(String.valueOf(thisYear - 3), profit4),
                        new PieChart.Data(String.valueOf(thisYear - 2), profit3),
                        new PieChart.Data(String.valueOf(thisYear - 1), profit2),
                        new PieChart.Data(String.valueOf(thisYear), profit1));
        FinanceYear_PieChart.setData(pieChartData);
        FinanceYear_PieChart.setVisible(true);
        setRank();

    }

    @FXML
    void onFinanceSeasonBtnClicked(){
        FinanceYear_BarChart.setVisible(false);
        FinanceYear_PieChart.setVisible(false);
        FinanceMonth_BarChart.setVisible(false);
        FinanceMonth_PieChart.setVisible(false);

        int thisMonth = LocalDateTime.now().getMonthValue();
        int thisYear = LocalDateTime.now().getYear();
        String startTime, endTime;
        int profit4, profit3, profit2, profit1;

        FinanceSeason_BarChart.getData().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Income");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Outcome");
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series3.setName("Profit");

        startTime = thisYear + "-01-01 00:00:00";
        endTime = thisYear + "-03-31 24:59:59";
        profit4 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("1-3", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("1-3", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("1-3", profit4));
        if (thisMonth >= 1 && thisMonth <= 3){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit4));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-04-01 00:00:00";
        endTime = thisYear + "-06-31 24:59:59";
        profit3 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("4-6", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("4-6", ActivityDao.getIncome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("4-6", profit3));
        if (thisMonth >= 4 && thisMonth <= 6){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit3));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-07-01 00:00:00";
        endTime = thisYear + "-09-31 24:59:59";
        profit2 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("7-9", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("7-9", ActivityDao.getIncome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("7-9", profit2));
        if (thisMonth >= 7 && thisMonth <= 9){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit2));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-10-01 00:00:00";
        endTime = thisYear + "-12-31 24:59:59";
        profit1 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("10-12", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("10-12", ActivityDao.getIncome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("10-12", profit1));
        if (thisMonth >= 10 && thisMonth <= 12){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit1));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        FinanceSeason_BarChart.getData().addAll(series1, series2, series3);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("1-3", profit4),
                        new PieChart.Data("4-6", profit3),
                        new PieChart.Data("7-9", profit2),
                        new PieChart.Data("10-12", profit1));
        FinanceSeason_PieChart.setData(pieChartData);

        FinanceSeason_BarChart.setVisible(true);
        FinanceSeason_PieChart.setVisible(true);
        setRank();
    }

    @FXML
    void onFinanceMonthBtnClicked(){

        FinanceYear_BarChart.setVisible(false);
        FinanceYear_PieChart.setVisible(false);
        FinanceSeason_BarChart.setVisible(false);
        FinanceSeason_PieChart.setVisible(false);

        int thisMonth = LocalDateTime.now().getMonthValue();
        int thisYear = LocalDateTime.now().getYear();
        String startTime, endTime;
        int profit12, profit11, profit10, profit9, profit8, profit7, profit6, profit5, profit4, profit3, profit2, profit1;

        FinanceMonth_BarChart.getData().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Income");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Outcome");
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series3.setName("Profit");

        startTime = thisYear + "-01-01 00:00:00";
        endTime = thisYear + "-01-31 24:59:59";
        profit1 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Jan", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Jan", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Jan", profit1));
        if (thisMonth == 1){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit1));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }
        startTime = thisYear + "-02-01 00:00:00";
        endTime = thisYear + "-02-31 24:59:59";
        profit2 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Feb", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Feb", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Feb", profit2));
        if (thisMonth == 2){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit2));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-03-01 00:00:00";
        endTime = thisYear + "-03-31 24:59:59";
        profit3 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Mar", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Mar", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Mar", profit3));
        if (thisMonth == 3){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit3));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-04-01 00:00:00";
        endTime = thisYear + "-04-31 24:59:59";
        profit4 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Apr", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Apr", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Apr", profit4));
        if (thisMonth == 4){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit4));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-05-01 00:00:00";
        endTime = thisYear + "-05-31 24:59:59";
        profit5 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("May", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("May", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("May", profit5));
        if (thisMonth == 5){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit5));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-06-01 00:00:00";
        endTime = thisYear + "-06-31 24:59:59";
        profit6 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Jun", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Jun", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Jun", profit6));
        if (thisMonth == 6){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit6));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-07-01 00:00:00";
        endTime = thisYear + "-07-31 24:59:59";
        profit7 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Jul", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Jul", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Jul", profit7));
        if (thisMonth == 7){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit7));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }
        startTime = thisYear + "-08-01 00:00:00";
        endTime = thisYear + "-08-31 24:59:59";
        profit8 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Agu", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Agu", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Agu", profit8));
        if (thisMonth == 8){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit8));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }
        startTime = thisYear + "-09-01 00:00:00";
        endTime = thisYear + "-09-31 24:59:59";
        profit9 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Sep", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Sep", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Sep", profit9));
        if (thisMonth == 9){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit9));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-10-01 00:00:00";
        endTime = thisYear + "-10-31 24:59:59";
        profit10 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Oct", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Oct", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Oct", profit10));
        if (thisMonth == 10){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit10));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-11-01 00:00:00";
        endTime = thisYear + "-11-31 24:59:59";
        profit11 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Nov", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Nov", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Nov", profit11));
        if (thisMonth == 11){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit11));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        startTime = thisYear + "-12-01 00:00:00";
        endTime = thisYear + "-12-31 24:59:59";
        profit12 = ActivityDao.getIncome(startTime, endTime) - ActivityDao.getOutcome(startTime, endTime);
        series1.getData().add(new XYChart.Data<>("Dec", ActivityDao.getIncome(startTime, endTime)));
        series2.getData().add(new XYChart.Data<>("Dec", ActivityDao.getOutcome(startTime, endTime)));
        series3.getData().add(new XYChart.Data<>("Dec", profit12));
        if (thisMonth == 12){
            FinanceIncome_Label.setText(String.valueOf(ActivityDao.getIncome(startTime, endTime)));
            FinanceOutcome_Label.setText(String.valueOf(ActivityDao.getOutcome(startTime, endTime)));
            FinanceProfit_Label.setText(String.valueOf(profit12));
            carBrandRankList = ActivityDao.getCarLeasedBrandRank(startTime, endTime);
            clientConsumptionRankList = ActivityDao.getClientConsumptionRank(startTime, endTime);
        }

        FinanceMonth_BarChart.getData().addAll(series3);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Jan", profit1),
                        new PieChart.Data("Feb", profit2),
                        new PieChart.Data("Mar", profit3),
                        new PieChart.Data("Apr", profit4),
                        new PieChart.Data("May", profit5),
                        new PieChart.Data("Jun", profit6),
                        new PieChart.Data("Jul", profit7),
                        new PieChart.Data("Agu", profit8),
                        new PieChart.Data("Sep", profit9),
                        new PieChart.Data("Oct", profit10),
                        new PieChart.Data("Nov", profit11),
                        new PieChart.Data("Dec", profit12));
        FinanceMonth_PieChart.setData(pieChartData);

        FinanceMonth_BarChart.setVisible(true);
        FinanceMonth_PieChart.setVisible(true);
        setRank();
    }

    private void setRank(){
        CarBrandRank_Label1.setText("");
        CarBrandRank_Label2.setText("");
        CarBrandRank_Label3.setText("");
        ClientConsumption_Label1.setText("");
        ClientConsumption_Label2.setText("");
        ClientConsumption_Label3.setText("");
        if (carBrandRankList.size() >= 1)
            CarBrandRank_Label1.setText("·1 " + carBrandRankList.get(0).getItemName() + "   " + carBrandRankList.get(0).getNum());
        if (carBrandRankList.size() >= 2)
            CarBrandRank_Label2.setText("·2 " + carBrandRankList.get(1).getItemName() + "   " + carBrandRankList.get(1).getNum());
        if (carBrandRankList.size() >= 3)
            CarBrandRank_Label3.setText("·3 " + carBrandRankList.get(2).getItemName() + "   " + carBrandRankList.get(2).getNum());


        if (clientConsumptionRankList.size() >=1)
            ClientConsumption_Label1.setText("·1 " + clientConsumptionRankList.get(0).getItemName() + "   " + clientConsumptionRankList.get(0).getNum());
        if (clientConsumptionRankList.size() >=2)
            ClientConsumption_Label2.setText("·2 " + clientConsumptionRankList.get(1).getItemName() + "   " + clientConsumptionRankList.get(1).getNum());
        if (clientConsumptionRankList.size() >=3)
            ClientConsumption_Label3.setText("·3 " + clientConsumptionRankList.get(2).getItemName() + "   " + clientConsumptionRankList.get(2).getNum());


    }






}
