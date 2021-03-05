package Register;

import DBUtil.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {
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
    private TextField emailField;

    //This will take the following as parameters to add their details into the database. The ID column will be automatically incremented
    public void registerUser(String Username, String Firstname, String Lastname, String Password, String Email)throws SQLException {
        String sql = "INSERT INTO Users(Username, Firstname, Lastname, Password, Email) VALUES (?, ?, ?, ?, ?)";
        try{
            Connection con = DBConnection.getConnection();
            assert con != null;
            PreparedStatement statement = con.prepareStatement(sql);

            statement.setString(1, Username);
            statement.setString(2, Firstname);
            statement.setString(3, Lastname);
            statement.setString(4, Password);
            statement.setString(5, Email);

            statement.execute();
            con.close();
        }catch(SQLException e){
            System.err.println("Error!\n" + e);
        }
    }

    //This will make sure that the required format is entered returning true/false. For this project only verification of email is required.
    public boolean checkFormat(){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailField.getText());
        return matcher.matches();
    }

}
