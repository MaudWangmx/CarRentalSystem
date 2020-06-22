package Main;

import DataBase.DBConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    private static Stage mainStage;

    public static Stage getStage(){
        return mainStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

//        try {
//            DBConnector.getInstance().connectDataBase("root", "maud050092");
//        } catch (
//                SQLException e) {
//            System.err.println("failed to connect to sql database");
//            System.exit(0);
//        }
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginView.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/View/ClientView.fxml"));
        mainStage = primaryStage;
        primaryStage.setTitle("Car Rental System");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Resource/LoginView.css").toExternalForm());
//        scene.getStylesheets().add(getClass().getResource("/Resource/ClientView.css").toExternalForm());


        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
