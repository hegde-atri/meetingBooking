package Login;

import Admin.AdminController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    LoginModel loginModel = new LoginModel();
    @FXML
    private Label connectionLabel;
    @FXML
    private Label credentialsLabel;
    @FXML
    private Label meetingBookerLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> accountType;


//Initialize method the tells us whether if successfully connected to the database or not
    public void initialize(URL url, ResourceBundle rb) {
        if (this.loginModel.isConnected()) {
            connectionLabel.setText("Connected to DB");
        } else {
            connectionLabel.setText("DB offline");
        }
        accountType.getItems().addAll("Admin", "Customer");
    }

    //This method will take the inputs from the fields, verify that they are correct and will take them to their respective window, based on their account type
    @FXML
    public void login() {
        try {
            //this if statement uses the loginModel object created in the beginning of this class and uses the isLogin method.
            if (this.loginModel.isLogin(this.usernameField.getText(), this.passwordField.getText(), accountType.getValue())) {
                System.out.println("works");
                Stage stage = (Stage) this.loginButton.getScene().getWindow();
                stage.close();
                switch (accountType.getValue()) {
                    case "Admin":
                        adminLogin();
                        break;
                    case "Customer":
                        customerLogin();
                        break;
                }
            } else {
                credentialsLabel.setText("Invalid log in!");
            }
        } catch (Exception e) {
        }
    }

    //Launches the admin dashboard, the related files are in the Admin package
    public void adminLogin() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Admin/Admin.fxml").openStream());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Admin Dashboard");
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Launches the customer dashboard, the related files are in the Customer package
    public void customerLogin() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Customer/Customer.fxml").openStream());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Customer Dashboard");
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Launches the registration page to create a new User
    public void registerUser(){

    }

}
