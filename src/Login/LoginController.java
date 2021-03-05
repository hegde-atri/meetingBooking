package Login;

import Admin.AdminController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
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
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<options> isAdminBox;

    public void initialize(URL url, ResourceBundle rb) {
        if (this.loginModel.isConnected()) {
            connectionLabel.setText("Connected to DB");
        } else {
            connectionLabel.setText("DB offline");
        }
        this.isAdminBox.setItems(FXCollections.observableArrayList(options.values()));
    }

    @FXML
    public void login() {
        try {
            if (this.loginModel.isLogin(this.usernameField.getText(), this.passwordField.getText(), ((options) this.isAdminBox.getValue()).toString())) {
                Stage stage = (Stage) this.loginButton.getScene().getWindow();
                stage.close();
                switch (((options) this.isAdminBox.getValue()).toString()) {
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
}
