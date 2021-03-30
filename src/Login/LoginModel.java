package Login;

import DBUtil.DBConnection;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginModel {

    //Global variable to reduce redundancy
    Connection connection;

    //Constructor that establishes connection to database
     public LoginModel(){
         try{
             this.connection = DBConnection.getConnection();
         }catch(SQLException e){
             e.printStackTrace();
         }
     }

     //This will tell use whether the connection was established or not
     public boolean isConnected(){
         try{
             Connection con = DBConnection.getConnection();
             if(con != null){
                 return true;
             }else{
                 return false;
             }

         }catch(Exception e){
             e.printStackTrace();
             return false;
         }
     }

    //This will take the following parameters and will verify it with the database.
    // It will reject you if you try and login as a customer from and admin account and vice versa
     public boolean isLogin(String username, String password, String AccountType)throws SQLException{
         PreparedStatement ps = null;
         ResultSet rs = null;
         String sql = "SELECT * FROM Users where Username = ? and Password = ? and Account = ?";
         try{
             ps = this.connection.prepareStatement(sql);
             ps.setString(1, username);
             ps.setString(2, password);
             ps.setString(3, AccountType);

             rs = ps.executeQuery();
             if(rs.next()){
                 LoginController.currentUser = new User(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getString(6), rs.getString(2));
                 return true;
             }
             return false;
         }catch(SQLException e){
             return false;
         }finally{
             assert ps != null;
             assert rs != null;
             ps.close();
             rs.close();
         }
     }
}
