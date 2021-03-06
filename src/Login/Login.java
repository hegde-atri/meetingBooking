package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application {

    public void start(Stage primaryStage)throws Exception{
        Parent root = (Parent) FXMLLoader.load(getClass().getResource("LoginFXML.fxml"));
        Scene scene =  new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/Stylesheets/Login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Meeting booker login");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
