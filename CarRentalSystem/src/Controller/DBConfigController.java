package Controller;

import DataBase.Client.Client;
import DataBase.Client.ClientDao;
import Main.Main;
import Util.Config;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

public class DBConfigController {

    @FXML
    TextField DBURL_Input, DBUserName_Input, DBPassword_Input, DBName_Input, URLEg_Input;
    @FXML
    Button DBReset_Btn, DBConfirm_Btn, Exit_Btn;
    @FXML
    Label URLEg_Label;

    @FXML
    void initialize(){

        URLEg_Input.setStyle("-fx-background-color: transparent");
        URLEg_Input.setEditable(false);

        Exit_Btn.setOnMouseClicked(event -> {
            Scene scene;
            try {
                scene = new Scene(FXMLLoader.load(getClass().getResource("/View/LoginView.fxml")));
                scene.getStylesheets().add(getClass().getResource("/Resource/LoginView.css").toExternalForm());
                Main.getStage().setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        DBReset_Btn.setOnMouseClicked(event -> {
            DBName_Input.setText("");
            DBPassword_Input.setText("");
            DBURL_Input.setText("");
            DBUserName_Input.setText("");
        });

        DBUserName_Input.textProperty().addListener((observable -> {
            if (!DBUserName_Input.getText().trim().isEmpty())
                DBUserName_Input.setStyle("-fx-background-color: white");
        }));

        DBPassword_Input.textProperty().addListener((observable -> {
            if (!DBPassword_Input.getText().trim().isEmpty())
                DBPassword_Input.setStyle("-fx-background-color: white");
        }));

        DBName_Input.textProperty().addListener((observable -> {
            if (!DBName_Input.getText().trim().isEmpty())
                DBName_Input.setStyle("-fx-background-color: white");
        }));

        DBConfirm_Btn.setOnMouseClicked(event -> {
            if (DBUserName_Input.getText().trim().isEmpty()){
                DBUserName_Input.setStyle("-fx-background-color: pink");
                return;
            }
            if (DBPassword_Input.getText().trim().isEmpty()){
                DBPassword_Input.setStyle("-fx-background-color: pink");
                return;
            }
            if (DBName_Input.getText().trim().isEmpty()){
                DBName_Input.setStyle("-fx-background-color: pink");
                return;
            }
            Config.DB_MASTER_PW = DBPassword_Input.getText().trim();
            Config.DB_MASTER_NAME = DBUserName_Input.getText().trim();
            Config.DB_NAME = DBName_Input.getText().trim();
            if (!DBURL_Input.getText().isEmpty())
                Config.DB_URL = DBURL_Input.getText().trim();
            else
                Config.DB_URL = "jdbc:mysql://127.0.0.1:3306/" + DBName_Input.getText().trim() + "?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";

            Scene scene;
            try {
                scene = new Scene(FXMLLoader.load(getClass().getResource("/View/LoginView.fxml")));
                scene.getStylesheets().add(getClass().getResource("/Resource/LoginView.css").toExternalForm());
                Main.getStage().setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
