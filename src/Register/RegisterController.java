package Register;

import DBUtil.DBConnection;
import Login.LoginModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {

    //<editor-fold desc="variables">
    @FXML
    private Label Heading;
    @FXML
    private Label errorLabel;
    @FXML
    private Label backgroundLabel;
    @FXML
    private Button registerButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField verifyPasswordField;
    @FXML
    private TextField emailField;
    //</editor-fold>

    //This will take the following as parameters to add their details into the database. The ID column will be automatically incremented
    public boolean registerLogic(String Username, String Firstname, String Lastname, String Email) throws SQLException {
        String sql = "INSERT INTO Users(Username, Firstname, Lastname, Password, Email, Account) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection con = DBConnection.getConnection();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(sql);
            String hashedPass = LoginModel.getPassHash(passwordField.getText());

            statement.setString(1, Username);
            statement.setString(2, Firstname);
            statement.setString(3, Lastname);
            statement.setString(4, hashedPass);
            statement.setString(5, Email);
            statement.setString(6, "Customer");

            statement.execute();
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Error!\n" + e);
            return false;
        }
    }

    //This will make sure that the required format is entered returning true/false. For this project only verification of email is required.
    public boolean checkFormat() {
        String regex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailField.getText());
        int passLength = passwordField.getLength();
        if(passLength >= 6 && passLength <=12){
            return matcher.matches();
        }
        if(!matcher.matches()){
            errorLabel.setText("Invalid email address");
        }
        errorLabel.setText("Password must be between 6-12 characters long");
        return false;

    }

    //This will take you back to the login page.
    public void backToLogin() {
        try {
            //We need to get the old stage, so we can close it before we go back to the login page
            Stage old = (Stage) registerButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Login/LoginFXML.fxml").openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Login.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Register Page");
            stage.setResizable(false);
            old.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPasswordMatching(PasswordField pass, PasswordField verify){
        return pass.getText().equals(verify.getText());
    }

    //This method will return true if the given username is already taken/in use.
    public static boolean isUsernameTaken(String username) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try{
            Connection con = DBConnection.getConnection();
            assert con != null;
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            return rs.next();
        }catch(Exception e){
            e.printStackTrace();
            return true;
        }finally{
            ps.close();
            rs.close();
        }
    }

    @FXML
    public void registerUser() throws SQLException {
        try {

            if (!usernameField.getText().isEmpty()
                    && !firstNameField.getText().isEmpty()
                    && !lastNameField.getText().isEmpty()
                    && !emailField.getText().isEmpty()
                    && !passwordField.getText().isEmpty()
                    && !verifyPasswordField.getText().isEmpty()) {
                if (checkFormat()) {
                    //will return true if there is a duplicate user/ username already taken
                    if (isPasswordMatching(passwordField, verifyPasswordField)) {
                        boolean usernameTaken = isUsernameTaken(usernameField.getText());
                        if (!usernameTaken) {
                            if (registerLogic(usernameField.getText(), firstNameField.getText(), lastNameField.getText(), emailField.getText())) {
                                errorLabel.setText("");
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Message");
                                alert.setHeaderText(null);
                                alert.setContentText("Account created!");

                                alert.showAndWait().ifPresent((btnType) -> {
                                });
                                backToLogin();
                            }else{
                                errorLabel.setText("Account cannot be created with current details");
                            }
                        } else {
                            errorLabel.setText("Username already taken!");
                        }
                    } else {
                        errorLabel.setText("Passwords do not match!");
                    }

                } else {
                    //errorLabel set in checkFormat method
                }
            }else{
                errorLabel.setText("Missing information!");

            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }
}
