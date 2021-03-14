package Customer;


import Admin.userBookings;
import DBUtil.DBConnection;
import Login.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class CustomerController {
    @FXML
    private Label currentBookingsLabel;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button newBookingButton;
    @FXML
    private TableView<userBookings> myTable;
    @FXML
    private TableColumn<userBookings, Integer> roomIDColumn;
    @FXML
    private TableColumn<userBookings, String> startTimeColumn;
    @FXML
    private TableColumn<userBookings, String> endTimeColumn;
    @FXML
    private TableColumn<userBookings, String> startDateColumn;
    @FXML
    private TableColumn<userBookings, String> endDateColumn;
    @FXML
    private TableColumn<userBookings, String> resourcesColumn;
    @FXML
    private TableColumn<userBookings, String> refreshmentsColumn;
    @FXML
    private TableColumn<userBookings, String> refreshmentsTimeColumn;


    private ObservableList<userBookings> data;

    public void initialize(){
        welcomeLabel.setText("Welcome back " + LoginController.currentUser.getFirstname());
        loadBookingData();
    }


    public void loadBookingData() {
        try {
            PreparedStatement ps;
            ResultSet rs;
            String sql = "SELECT * FROM Bookings WHERE UserID IS ?";
            Connection con = DBConnection.getConnection();
            this.data = FXCollections.observableArrayList();
            assert con != null;
            ps = con.prepareStatement(sql);
            ps.setString(1, String.valueOf(LoginController.currentUser.getUserID()));

            rs = ps.executeQuery();
            while (rs.next()) {
                //To make things complicated I've decided to use userBookings objects which contains 100% of the details and then use the data i get from it to then add the data to the observableList data i created as a userBooking object.
                userBookings ub  = new userBookings(rs.getInt(1), rs.getInt(2), rs.getString(3), LocalTime.parse(rs.getString(4)), LocalTime.parse(rs.getString(5)),
                        LocalDate.parse(rs.getString(6)), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10));
                this.data.add(ub);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }

        this.roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        this.startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        this.endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        this.startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        this.endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        this.resourcesColumn.setCellValueFactory(new PropertyValueFactory<>("resources"));
        this.refreshmentsColumn.setCellValueFactory(new PropertyValueFactory<>("refreshments"));
        this.refreshmentsTimeColumn.setCellValueFactory(new PropertyValueFactory<>("refreshmentsTime"));
        //so that this doubles as a refresh function, i made it so that it removes existing data from the table and then adds them again.
        this.myTable.setItems(null);
        this.myTable.setItems(data);

    }

    @FXML
    public void deleteBooking(){
        try {
            PreparedStatement ps;
            String sql = "DELETE FROM Bookings WHERE RoomID is ? AND UserID is ? AND StartTime is ? AND StartDate is ?";
            Connection con = DBConnection.getConnection();
            assert con != null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, myTable.getSelectionModel().getSelectedItem().getRoomID());
            ps.setInt(2, LoginController.currentUser.getUserID());
            ps.setString(3, String.valueOf(myTable.getSelectionModel().getSelectedItem().getStartTime()));
            ps.setString(4, String.valueOf(myTable.getSelectionModel().getSelectedItem().getStartDate()));
            ps.execute();
            myTable.getItems().remove(myTable.getSelectionModel().getSelectedItem());
        }catch(SQLException e){
            System.out.println("Error: " + e);
        }
    }

    @FXML
    private void newBooking(){
        try {
            Stage old = (Stage)newBookingButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/RoomBooker/RoomBooker.fxml").openStream());
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/RoomBooker.css").toExternalForm());
            stage.getIcons().add(new Image("/images/booking.png"));
            stage.setScene(scene);
            stage.setTitle("Register Page");
            stage.setResizable(false);
            old.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backToLogin(){
        try {
            Stage old = (Stage) backButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Login/LoginFXML.fxml").openStream());
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Login.css").toExternalForm());
            stage.getIcons().add(new Image("/images/mainIcon.jpeg"));
            stage.setScene(scene);
            stage.setTitle("Meeting booker login");
            stage.setResizable(false);
            old.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
