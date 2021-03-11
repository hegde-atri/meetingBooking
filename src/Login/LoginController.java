package Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    //This stores the information of the current user
    public static User currentUser;
    //creates a LoginModel
    private final LoginModel loginModel = new LoginModel();
    //If any variable are not used in the methods, then are probably are used for styling in the stylesheet (Login.css) which is in the stylesheet folder
    @FXML
    private Label connectionLabel;
    @FXML
    private Label credentialsLabel;
    @FXML
    private Label meetingBookerLabel;
    @FXML
    private Label backgroundLabel;
    @FXML
    private ImageView background;
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
        Image image = new Image("/images/login.jpg");
        background = new ImageView(image);
        background.setFitHeight(400);
        background.setPreserveRatio(true);
        backgroundLabel.setGraphic(background);
    }

    //This method will take the inputs from the fields, verify that they are correct and will take them to their respective window, based on their account type
    @FXML
    public void login() {
        try {
            //this if statement uses the loginModel object created in the beginning of this class and uses the isLogin method.
            if (this.loginModel.isLogin(this.usernameField.getText(), this.passwordField.getText(), accountType.getValue())) {
                //This deletes the current stage (the current stage needs to be reached from the button) and opens the new stage
                Stage stage = (Stage) this.loginButton.getScene().getWindow();
                stage.close();
                switch (accountType.getValue()) {
                    case "Admin":
                        adminLogin(currentUser);
                        break;
                    case "Customer":
                        customerLogin(currentUser);
                        break;
                }
            } else {
                credentialsLabel.setText("Invalid log in!");
            }
        } catch (Exception ignored) {
        }
    }

    //Launches the admin dashboard, the related files are in the Admin package
    public void adminLogin(User currentUser) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Admin/Admin.fxml").openStream());
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Admin.css").toExternalForm());
            stage.getIcons().add(new Image("/images/admin.png"));
            stage.setScene(scene);
            stage.setTitle("Admin Dashboard");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Launches the customer dashboard, the related files are in the Customer package
    public void customerLogin(User currentUser) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Customer/Customer.fxml").openStream());
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Customer.css").toExternalForm());
            stage.getIcons().add(new Image("/images/customer.png"));
            stage.setScene(scene);
            stage.setTitle("Customer Dashboard");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Launches the registration page to create a new User
    public void registerUser(){
        try {
            //We need to get the old stage, so we can close it before we open the Register page
            Stage old = (Stage)registerButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Register/Register.fxml").openStream());
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Register.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Register Page");
            stage.setResizable(false);
            old.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
