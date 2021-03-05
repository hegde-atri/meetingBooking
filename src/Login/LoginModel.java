package Login;

import DBUtil.DBConnection;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginModel {

    Connection connection;

     public LoginModel(){
         try{
             this.connection = DBConnection.getConnection();
         }catch(SQLException e){
             e.printStackTrace();
         }
         if(this.connection==null){
             System.exit(1);
         }
     }

     public boolean isConnected(){
         return this.connection != null;
     }

     public void registerUser(String Username, String Firstname, String Lastname, String Password, String Email)throws SQLException{
         PreparedStatement ps = null;
         ResultSet rs = null;
         String sql = "";
     }

     public boolean isLogin(String username, String password, String Admin) throws SQLException {
         PreparedStatement ps = null;
         ResultSet rs = null;
         String sql = "SELECT * FROM Users where Username = ? and Password = ? and Admin = ?";
         try{
             ps = this.connection.prepareStatement(sql);
             ps.setString(1, username);
             ps.setString(2, password);
             ps.setString(3, Admin);

             rs = ps.executeQuery();
             if(rs.next()){
                 return true;
             }
             return false;
         }catch(SQLException e){
             return false;
         }finally{
             ps.close();
             rs.close();
         }
     }
}
