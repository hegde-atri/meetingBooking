package Register;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {
    Connection connection;

    public void registerUser(String Username, String Firstname, String Lastname, String Password, String Email)throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
    }
}
