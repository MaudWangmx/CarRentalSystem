package Controller;

import DataBase.Client.Client;
import DataBase.Client.ClientDao;
import Main.Main;
import javafx.fxml.FXML;

//import java.awt.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

public class RegisterController {

    @FXML
    TextField RegisterName_Input, RegisterContact_Input;
    @FXML
    PasswordField RegisterPassword_Input;
    @FXML
    Button RegisterReset_Btn, RegisterSubmit_Btn, RegisterExit_Btn;
    @FXML
    Label RegisterTip_Label;
    private Client newClient;

    @FXML
    void initialize(){

        RegisterExit_Btn.setOnMouseClicked(event -> {
            Scene scene;
            try {
                scene = new Scene(FXMLLoader.load(getClass().getResource("/View/LoginView.fxml")));
                scene.getStylesheets().add(getClass().getResource("/Resource/LoginView.css").toExternalForm());
                Main.getStage().setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        RegisterReset_Btn.setOnMouseClicked(event -> {
            RegisterContact_Input.setText("");
            RegisterName_Input.setText("");
            RegisterPassword_Input.setText("");
        });

        RegisterName_Input.textProperty().addListener((observable -> {
            if (!RegisterName_Input.getText().trim().isEmpty())
                RegisterTip_Label.setText("");
        }));

        RegisterPassword_Input.textProperty().addListener((observable -> {
            if (!RegisterPassword_Input.getText().trim().isEmpty())
                RegisterTip_Label.setText("");
        }));

        RegisterSubmit_Btn.setOnMouseClicked(event -> {
            RegisterTip_Label.setText("");
            if (RegisterName_Input.getText().trim().isEmpty()){
                RegisterTip_Label.setText("Please enter your name.");
                return;
            }
            if (RegisterPassword_Input.getText().trim().isEmpty()){
                RegisterTip_Label.setText("Please enter your password.");
                return;
            }
            newClient = new Client("",
                    RegisterPassword_Input.getText().trim(),
                    "",
                    "",
                    "3",
                    "",
                    RegisterName_Input.getText().trim(),
                    RegisterContact_Input.getText().trim(),
                    0,
                    false
            );
            if (ClientDao.addAClient(newClient)){
                RegisterTip_Label.setText("Register Succeed! ClientID: " + newClient.getClientID() + " .");
            }
        });

    }
}
