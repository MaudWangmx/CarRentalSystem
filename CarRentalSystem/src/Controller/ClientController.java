package Controller;

import DataBase.Activity.ActivityDao;
import DataBase.Car.Car;
import DataBase.Car.CarDao;
import DataBase.Client.Client;
import DataBase.DBConnector;
import DataBase.Employee.EmployeeDao;
import DataBase.LeaseRecord;
import DataBase.RentInfo;
import DataBase.ReturnInfo;
import Main.Main;
import State.State;
import Util.AutoCompleteComboBoxListener;
import Util.DateConverter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import DataBase.TableView.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Util.Config.*;

public class ClientController {


    @FXML
    private Label ClientWelCome_Label;
    @FXML
    private Button ClientExit_Btn;
    @FXML
    private Tab Lease_Tab;
    @FXML
    private Tab Return_Tab;
    @FXML
    private Tab Search_Tab;
    @FXML
    private ComboBox<String> LeaseBrand_Combo;
    @FXML
    private ComboBox<String> LeaseLicense_Combo;
    @FXML
    private Label LeaseRent_Label;
    @FXML
    private Label LeasePledge_Label;
    @FXML
    private TextField LeaseOptID_Input;
    @FXML
    private Label LeaseTotal_Label;
    @FXML
    private Label LeaseTip_Label;
    @FXML
    private Button LeaseReset_Btn;
    @FXML
    private Button LeaseSubmit_Btn;
    @FXML
    private Label ReturnLicense_Label;
    @FXML
    private Label ReturnBrand_Label;
    @FXML
    private Label ReturnRent_Label;
    @FXML
    private Label ReturnRentTime_Label;
    @FXML
    private Label ReturnRentalDuration_Label;
    @FXML
    private Label ReturnPledgeSub_Label;
    @FXML
    private TextField ReturnCarCondition_Input;
    @FXML
    private Label ReturnTotalRent_Label;
    @FXML
    private Label ReturnTrafficFine_Label;
    @FXML
    private Label ReturnDamageFine_Label;
    @FXML
    private Label ReturnWithDraw_Label;
    @FXML
    private Label ReturnPayAmount_Label;
    @FXML
    private Label ReturnTotalCost_Label;
    @FXML
    private Label ReturnTip_Label;
    @FXML
    private Label ReturnOperatorID_Label;
    @FXML
    private Button ReturnCount_Btn;
    @FXML
    private Button ReturnConfirm_Btn;
    @FXML
    private Tab SearchCars_Tab;
    @FXML
    private Tab SearchRecord_Tab;
    @FXML
    private TextField SearchCarsBrand_Input;
    @FXML
    private TextField SearchCarsLicense_Input;
    @FXML
    private TextField SearchCarsRentMin_Input;
    @FXML
    private TextField SearchCarsRentMax_Input;
    @FXML
    private Button CarsSearch_Btn;
    @FXML
    private TableView<CarTable> SearchCars_Table;
    @FXML
    private TableColumn<CarTable, String> CarsLic_Col, CarsBrand_Col, CarsCondition_Col, CarsRent_Col, CarsPledge_Col, CarsStatus_Col;
    @FXML
    private Label SearchCarsTip_Label;
    @FXML
    private TextField RecordLicense_Input;
    @FXML
    private ComboBox<String> RecordType_Combo;
    @FXML
    private TableView<ClientRecordTable> SearchRecord_Table;
    @FXML
    private TableColumn<ClientRecordTable, String> RecordDateTime_Col, RecordLics_Col, RecordBrand_Col, RecordRecType_Col, RecordOperator_Col, RecordCost_Col, RecordWithdraw_Col;
    @FXML
    private DatePicker RecordStartDate_Picker;
    @FXML
    private DatePicker RecordEndDate_Picker;
    @FXML
    private CheckBox RecordAllDate_CheckBox;
    @FXML
    private CheckBox RecordThisMonth_CheckBox;
    @FXML
    private Label SearchRecordTip_Label;
    @FXML
    private Button RecordSearch_Btn;
    @FXML
    private AnchorPane Return_Pane;
    @FXML
    private GridPane Return_Pane1;
    @FXML
    private VBox Return_Pane2;
    @FXML
    private HBox Return_Pane3;

    private String operatorID;
    private LeaseRecord leaseRecord;

    /* Return Pane */
    private int currentCondition;
    private int trafficFineAmount;
    private int damageFineAmount;
    private int pledgeWithdraw;
    private int totalRent;

    /* Lease Pane */
    private ObservableList<String> brandItemList;
    private ObservableList<String> carIDItemList;
    private List<Car> allCarInfoList;
    private List<String> allBrandList;

    /* Search Pane */
    private ObservableList<CarTable> searchCarsTableList = FXCollections.observableArrayList();
    private ObservableList<ClientRecordTable> searchRecordTableList = FXCollections.observableArrayList();



    @FXML
    private void initialize(){
        ClientWelCome_Label.setText("Welcome, " + State.getINSTANCE().getClient().getClientName()
                + ".         ID: " + State.getINSTANCE().getClient().getClientID()
        + "        Credit: " + State.getINSTANCE().getClient().getCredit()
        + "        Identity: " + (State.getINSTANCE().getClient().isVIP() ? "VIP" : "Normal User"));

        LeaseBrand_Combo.setEditable(true);
        LeaseLicense_Combo.setEditable(true);
        LeaseRent_Label.setText("");
        LeasePledge_Label.setText("");
        onLeaseTabSelected();
        LeaseOptID_Input.setEditable(false);

        allCarInfoList = CarDao.getAllCarInfo();
        allBrandList = CarDao.getAllBrand();
        brandItemList = FXCollections.observableArrayList();
        carIDItemList = FXCollections.observableArrayList();
        for (String s : allBrandList)
            brandItemList.add(s);
        for (Car c : allCarInfoList)
            carIDItemList.add(c.getCarID());
        LeaseBrand_Combo.setItems(brandItemList);
        LeaseLicense_Combo.setItems(carIDItemList);
        AutoCompleteComboBoxListener auto = new AutoCompleteComboBoxListener<String>(LeaseBrand_Combo);


        LeaseBrand_Combo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
                allBrandList = CarDao.getAllBrand();
                allCarInfoList = CarDao.getAllCarInfo();
                boolean licenseComboChangeFlag = true;  // check if present License value matches Brand selection
                if (newValue == null)
                    System.out.println("null newValue");

                List<String> license_Temp = new ArrayList<>();
                for (Car car : allCarInfoList){
                    if (car.getBrand().equals(newValue))
                        license_Temp.add(car.getCarID());
                }
                if (LeaseLicense_Combo.getSelectionModel().getSelectedIndex() != -1 && !LeaseLicense_Combo.getValue().trim().isEmpty()){
                    for (String s : license_Temp){
                        if (s.equals(LeaseLicense_Combo.getValue()))
                            licenseComboChangeFlag = false;
                    }
                }
                String license = LeaseLicense_Combo.getValue();
                carIDItemList.remove(0, carIDItemList.size());
                for (Car car : allCarInfoList) {
                    if (car.getBrand().equals(newValue))
                        carIDItemList.add(car.getCarID());
                }
                if (!licenseComboChangeFlag)
                    LeaseLicense_Combo.setValue(license);
                else
                    LeaseLicense_Combo.getSelectionModel().select(-1);

                /* LeaseBrand select 为空 */
                if(LeaseBrand_Combo.getSelectionModel().getSelectedIndex() == -1) {
                    System.out.println("select -1");
                    carIDItemList.remove(0, carIDItemList.size());
                    allCarInfoList = CarDao.getAllCarInfo();
                    for (Car car : allCarInfoList) {
                        carIDItemList.add(car.getCarID());
                    }
                }
                LeaseLicense_Combo.setItems(carIDItemList);
                AutoCompleteComboBoxListener auto1 = new AutoCompleteComboBoxListener<>(LeaseLicense_Combo);

            }
        });

        LeaseLicense_Combo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (LeaseLicense_Combo.getSelectionModel().getSelectedIndex() != -1 && !newValue.isEmpty()){
                Car car = CarDao.getCarByCarID(LeaseLicense_Combo.getValue());
                if (LeaseBrand_Combo.getSelectionModel().getSelectedIndex() == -1 || LeaseBrand_Combo.getValue().isEmpty()){
                    LeaseBrand_Combo.setValue(car.getBrand());
                }
                LeaseRent_Label.setText("¥" + car.getRent() + " per day");
                if (State.getINSTANCE().getClient().isVIP())
                    LeasePledge_Label.setText("¥" + (int)(car.getPledge()/2));
                else
                    LeasePledge_Label.setText("¥" + car.getPledge());
                LeaseTotal_Label.setText("¥" + car.getPledge());
            }else {
                LeaseRent_Label.setText("");
                LeasePledge_Label.setText("");
                LeaseTotal_Label.setText("");
            }
        });


        LeaseSubmit_Btn.setOnMouseClicked(event -> {
            if (LeaseTotal_Label.getText().isEmpty() || LeasePledge_Label.getText().isEmpty() || LeaseRent_Label.getText().isEmpty() ||
            LeaseBrand_Combo.getValue().isEmpty() || LeaseLicense_Combo.getValue().isEmpty() ||
                    LeaseBrand_Combo.getSelectionModel().getSelectedIndex() == -1 ||
            LeaseLicense_Combo.getSelectionModel().getSelectedIndex() == -1){
                LeaseTip_Label.setText("Please Select A Car.");
                return;
            }else {
                Car rentCar = CarDao.getCarByCarID(LeaseLicense_Combo.getValue());
                int VIPAttr = 1;
                if (State.getINSTANCE().getClient().isVIP())
                    VIPAttr = 2;

                System.out.println("Rent: " + Integer.valueOf(LeaseRent_Label.getText().split(" ")[0].substring(1)));
                System.out.println("Pledge: " + (Integer.valueOf(LeasePledge_Label.getText().trim().substring(1)) * VIPAttr));
                if (rentCar.getRent() != Integer.valueOf(LeaseRent_Label.getText().split(" ")[0].substring(1)) || rentCar.getPledge() != (Integer.valueOf(LeasePledge_Label.getText().trim().substring(1)) * VIPAttr) || !rentCar.getBrand().equals(LeaseBrand_Combo.getValue())){
                    LeaseTip_Label.setText("Page hasn't refreshed yet. Check new Rent and Pledge");
                    return;
                }
                if (State.getINSTANCE().getClient().isVIP()){
                    rentCar.setPledge(rentCar.getPledge()/2);
                }
                RentInfo rentInfo = new RentInfo(rentCar,
                        State.getINSTANCE().getClient().getClientID(),
                        operatorID,
                        Timestamp.valueOf(LocalDateTime.now()));
                if(ActivityDao.rentActivity(rentInfo)){
                    LeaseTip_Label.setText("Succeed!");
                    onLeaseTabSelected();
                }else {
                    LeaseTip_Label.setText("System Error. Please Try Later.");
                }

            }


        });


        RecordType_Combo.setItems(FXCollections.observableArrayList("All",
                "Lease Record",
                "Return Record",
                "Damage Record",
                "Traffic Fine"));
        RecordType_Combo.getSelectionModel().select(0);
        RecordStartDate_Picker.setValue(LocalDate.now());
        RecordEndDate_Picker.setValue(LocalDate.now());
        RecordStartDate_Picker.setConverter(new DateConverter());
        RecordEndDate_Picker.setConverter(new DateConverter());
        RecordAllDate_CheckBox.setSelected(true);
        onAllDateComboClicked();

        CarsLic_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().licensePlateProperty());
        CarsBrand_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().brandProperty());
        CarsCondition_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().conditionProperty());
        CarsRent_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().rentProperty());
        CarsPledge_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().pledgeProperty());
        CarsStatus_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<CarTable, String> param) -> param.getValue().statusProperty());
        SearchCars_Table.setItems(searchCarsTableList);

        RecordDateTime_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<ClientRecordTable, String> param) -> param.getValue().dateTimeProperty());
        RecordLics_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<ClientRecordTable, String> param) -> param.getValue().licsPlateProperty());
        RecordBrand_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<ClientRecordTable, String> param) -> param.getValue().brandProperty());
        RecordRecType_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<ClientRecordTable, String> param) -> param.getValue().recordTypeProperty());
        RecordOperator_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<ClientRecordTable, String> param) -> param.getValue().operatorIDProperty());
        RecordCost_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<ClientRecordTable, String> param) -> param.getValue().costProperty());
        RecordWithdraw_Col.setCellValueFactory(
                (TableColumn.CellDataFeatures<ClientRecordTable, String> param) -> param.getValue().withDrawProperty());
        SearchRecord_Table.setItems(searchRecordTableList);

        SearchCarsRentMax_Input.setOnKeyTyped(keyEvent->{
            SearchCarsTip_Label.setText("");
            if (!SearchCarsRentMax_Input.getText().isEmpty()){
                if (!SearchCarsRentMax_Input.getText().trim().matches("[0-9]*")){
                    SearchCarsTip_Label.setText("Rent range should be numbers");
                }
            }
        });

        SearchCarsRentMin_Input.setOnKeyTyped(keyEvent->{
            SearchCarsTip_Label.setText("");
            if (!SearchCarsRentMin_Input.getText().isEmpty()){
                if (!SearchCarsRentMin_Input.getText().trim().matches("[0-9]*")){
                    SearchCarsTip_Label.setText("Rent range should be numbers");
                }
            }
        });



    }

    @FXML
    private void ClientExitBtnClicked(){
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/View/LoginView.fxml")));
            scene.getStylesheets().add(getClass().getResource("/Resource/LoginView.css").toExternalForm());
            Main.getStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            State.getINSTANCE().logout();
        }
    }

    @FXML
    private void onAllDateComboClicked(){
        if (RecordAllDate_CheckBox.isSelected()){
            RecordThisMonth_CheckBox.setSelected(false);
            RecordThisMonth_CheckBox.setDisable(true);
            RecordStartDate_Picker.setDisable(true);
            RecordEndDate_Picker.setDisable(true);
        }else {
            RecordThisMonth_CheckBox.setDisable(false);
            RecordStartDate_Picker.setDisable(false);
            RecordEndDate_Picker.setDisable(false);
        }
    }

    @FXML
    private void onThisMonthComboClicked(){
        if (RecordThisMonth_CheckBox.isSelected()){
            RecordAllDate_CheckBox.setSelected(false);
            RecordAllDate_CheckBox.setDisable(true);
            RecordStartDate_Picker.setDisable(true);
            RecordEndDate_Picker.setDisable(true);
        }else {
            RecordAllDate_CheckBox.setDisable(false);
            RecordStartDate_Picker.setDisable(false);
            RecordEndDate_Picker.setDisable(false);
        }
    }

    @FXML
    private void onReturnTabSelected(){
        leaseRecord = ActivityDao.getLeaseRecordByClientID(State.getINSTANCE().getClient().getClientID());
        if (leaseRecord != null){
            ReturnTip_Label.setText("");
            Return_Pane1.setVisible(true);
            Return_Pane2.setVisible(true);
            Return_Pane3.setVisible(true);
            ReturnConfirm_Btn.setDisable(true);
            ReturnCount_Btn.setDisable(false);
            ReturnLicense_Label.setText(leaseRecord.getCarID());
            ReturnBrand_Label.setText(leaseRecord.getBrand());
            ReturnPledgeSub_Label.setText("¥" + Integer.toString(leaseRecord.getPledge(), 10));
            ReturnRent_Label.setText("¥" + Integer.toString(leaseRecord.getRent(), 10) + " per Day");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss");
            ReturnRentTime_Label.setText(leaseRecord.getLeaseDateTime().format(formatter));
            Duration duration = Duration.between(leaseRecord.getLeaseDateTime(), LocalDateTime.now());
            int dayDuration = 0;
            if ((int)duration.toDays() == 0)
                dayDuration = 1;
            else dayDuration = (int)duration.toDays();
            ReturnRentalDuration_Label.setText(dayDuration + " day(s)");
            totalRent = (int) (dayDuration * leaseRecord.getRent());
            ReturnTotalRent_Label.setText("¥" + totalRent);
            operatorID = EmployeeDao.getRandomOperatorID();
            ReturnOperatorID_Label.setText(operatorID);

            try {
                ResultSet trafficFineSearch = ActivityDao.getTrafficFineByActID(leaseRecord.getActID());
                trafficFineAmount = 0;
                while (trafficFineSearch.next())
                    trafficFineAmount += trafficFineSearch.getInt("amount");
                ReturnTrafficFine_Label.setText("¥" + trafficFineAmount);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                DBConnector.getInstance().closeConnection();
            }

        }else {
            ReturnTip_Label.setText("You are not renting a Car at present.");
            Return_Pane1.setVisible(false);
            Return_Pane2.setVisible(false);
            Return_Pane3.setVisible(false);
            ReturnConfirm_Btn.setDisable(true);
            ReturnCount_Btn.setDisable(true);

        }
    }

    @FXML
    private void onSearchRecordTabSelected(Event event) {
    }

    @FXML
    private void onSearchCarsTabSelected(){

    }

    @FXML
    private void onSearchTabSelected(){

    }

    @FXML
    private void onLeaseTabSelected(){
        LeaseRecord leaseRecord = ActivityDao.getLeaseRecordByClientID(State.getINSTANCE().getClient().getClientID());
        allCarInfoList = CarDao.getAllCarInfo();
        allBrandList = CarDao.getAllBrand();

        if (leaseRecord != null){
            LeaseTip_Label.setText("You've already rent a Car. The Number you can rent is limited to 1.");
            LeaseSubmit_Btn.setDisable(true);
            return;
        }
        LeaseTip_Label.setText("");
        LeaseSubmit_Btn.setDisable(false);
        operatorID = EmployeeDao.getRandomOperatorID();
        LeaseOptID_Input.setText(operatorID);
    }

    @FXML
    private void onLeaseResetBtnClicked(){
        LeaseLicense_Combo.getEditor().clear();
        LeaseBrand_Combo.getEditor().clear();
        LeaseRent_Label.setText("");
        LeasePledge_Label.setText("");
        LeaseTotal_Label.setText("");
        System.out.println((LeaseRent_Label.getText().isEmpty()?"true": "false"));
    }

    @FXML
    private void onCarConditionTextInput(){
        ReturnConfirm_Btn.setDisable(true);
    }

    @FXML
    private void onReturnCountBtnClicked(){
        ReturnConfirm_Btn.setDisable(true);
        if (ReturnCarCondition_Input.getText().isEmpty()){
            ReturnTip_Label.setText("Please enter the current condition of Car " + leaseRecord.getCarID() + ".");
            return;
        }
        if (!ReturnCarCondition_Input.getText().trim().matches("[1-5]")){
            ReturnTip_Label.setText("Car Condition should be a number between 1 and 5 (included)");
            return;
        }
        ReturnTip_Label.setText("");
        ReturnConfirm_Btn.setDisable(false);
        currentCondition = Integer.valueOf(ReturnCarCondition_Input.getText().trim());
        damageFineAmount = (5-currentCondition) * leaseRecord.getRent() / 2;
        ReturnDamageFine_Label.setText("¥" + damageFineAmount);
        pledgeWithdraw = leaseRecord.getPledge() - trafficFineAmount - damageFineAmount;
        if (pledgeWithdraw < 0){
            ReturnWithDraw_Label.setText("¥" + "0");
            ReturnPayAmount_Label.setText("¥" + (int)(totalRent - pledgeWithdraw));
        }else {
            ReturnWithDraw_Label.setText("¥" + pledgeWithdraw);
            ReturnPayAmount_Label.setText("¥" + totalRent);
        }
        ReturnTotalCost_Label.setText("¥" + (int)(totalRent + trafficFineAmount + damageFineAmount));

    }

    @FXML
    private void onReturnConfirmBtnClicked(){
        onReturnCountBtnClicked();
        if (!ReturnTip_Label.getText().isEmpty())
            return;
        int trafficFineCount;
        ResultSet resultSet = ActivityDao.getTrafficFineByActID(leaseRecord.getActID());
        ReturnInfo returnInfo = null;
        try {
            resultSet.last();
            trafficFineCount = resultSet.getRow();
            returnInfo = new ReturnInfo(leaseRecord,
                    State.getINSTANCE().getClient(),
                    operatorID,
                    totalRent,
                    damageFineAmount,
                    Integer.toString(currentCondition),
                    Timestamp.valueOf(LocalDateTime.now()),
                    trafficFineCount,
                    trafficFineAmount);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnector.getInstance().closeConnection();
        }
        if(returnInfo != null) {
             if (ActivityDao.returnActivity(returnInfo)){
                 ReturnTip_Label.setText("Succeed!");
                 onReturnTabSelected();
             }else {
                 ReturnTip_Label.setText("System Error. Please Try Later.");
             }

        }
    }

    @FXML
    private void onSearchCarsBtnClicked(){
        String brandInput = SearchCarsBrand_Input.getText();
        String licensePlate = SearchCarsLicense_Input.getText();
        String rentMin = SearchCarsRentMin_Input.getText().trim();
        String rentMax = SearchCarsRentMax_Input.getText().trim();
        int rentMinNum;
        int rentMaxNum;
        if (!rentMin.isEmpty())
            if (!rentMin.matches("[0-9]*")) {
                SearchCarsTip_Label.setText("Rent range should be numbers");
                return;
            }
        if (!rentMax.isEmpty())
            if (!rentMax.matches("[0-9]*")) {
                SearchCarsTip_Label.setText("Rent range should be numbers");
                return;
            }
        if (rentMin.isEmpty())
            rentMinNum = 0;
        else
            rentMinNum = Integer.valueOf(rentMin);
        if (rentMax.isEmpty())
            rentMaxNum = RENT_MAX;
        else
            rentMaxNum = Integer.valueOf(rentMax);
        searchCarsTableList.clear();
        ResultSet searchResult = CarDao.getSearchCarTable(brandInput, licensePlate, rentMinNum, rentMaxNum, "5");
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

    @FXML
    private void onSearchRecordBtnClicked(){
        String startTimeStamp;
        String endTimeStamp;
        if (RecordAllDate_CheckBox.isSelected()){
            startTimeStamp = "0000-00-00 00:00:00";
            //startTimeStamp = Timestamp.valueOf(LocalDateTime.parse("0000-00-00 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //endTimeStamp = Timestamp.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            endTimeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        }
        else if (RecordThisMonth_CheckBox.isSelected()){
            startTimeStamp = LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-01 00:00:00";
            endTimeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
//            startTimeStamp = Timestamp.valueOf(LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonth() + "-01 00:00:00");
//            endTimeStamp = Timestamp.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        }else {
            if (RecordStartDate_Picker.getValue().isAfter(RecordEndDate_Picker.getValue())) {
                SearchRecordTip_Label.setText("Start Time should be a Date before End Time.");
                return;
            }
            startTimeStamp = RecordStartDate_Picker.getValue() + " 00:00:00";
            endTimeStamp = RecordEndDate_Picker.getValue() + " 24:00:00";
//            startTimeStamp = Timestamp.valueOf(RecordStartDate_Picker.getValue() + " 00:00:00");
//            endTimeStamp = Timestamp.valueOf(RecordEndDate_Picker.getValue() + " 24:00:00");
        }
        try{
            ResultSet searchResult = ActivityDao.getClientRecord(
                    State.getINSTANCE().getClient().getClientID(),
                    RecordLicense_Input.getText(),
                    RecordType_Combo.getSelectionModel().getSelectedIndex(),
                    startTimeStamp,
                    endTimeStamp);
            searchRecordTableList.clear();
            while (searchResult.next()){
                if (Integer.parseInt(searchResult.getString("fintype")) == 6)
                    continue;
                ClientRecordTable clientRecord = new ClientRecordTable(
                        searchResult.getTimestamp("date"),
                        searchResult.getString("carID"),
                        searchResult.getString("brand"),
                        searchResult.getString("event"),
                        searchResult.getString("employeeID"),
                        searchResult.getInt("amount"),
                        0
                );
                if (Integer.valueOf(searchResult.getString("event")) == 1){
                    if(searchResult.next())
                        clientRecord.setWithDraw(searchResult.getInt("amount"));
                }
                searchRecordTableList.add(clientRecord);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBConnector.getInstance().closeConnection();
        }


    }
}
