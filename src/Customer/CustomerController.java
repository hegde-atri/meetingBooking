package Customer;


import Admin.AdminController;
import Admin.userBookings;
import DBUtil.DBConnection;
import Login.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
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
import java.util.ArrayList;

public class CustomerController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button backButton;
    @FXML
    private TableView<CustomerBooking> myTable;
    @FXML
    private TableColumn<CustomerBooking, Integer> roomIDColumn;
    @FXML
    private TableColumn<CustomerBooking, String> timePeriodColumn;
    @FXML
    private TableColumn<CustomerBooking, String> dateColumn;
    @FXML
    private TableColumn<CustomerBooking, Integer> durationColumn;
    @FXML
    private TableColumn<CustomerBooking, String> equipmentColumn;
    @FXML
    private TableColumn<CustomerBooking, String> refreshmentsColumn;
    @FXML
    private TableColumn<CustomerBooking, String> refreshmentsTimeColumn;
    private ObservableList<CustomerBooking> data;

    public void initialize(){
        welcomeLabel.setText("Welcome back " + LoginController.currentUser.getFirstname());
        loadBookingData();
    }


    public void loadBookingData() {
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "SELECT * FROM Bookings WHERE UserID IS ?";
            Connection con = DBConnection.getConnection();
            this.data = FXCollections.observableArrayList();
            assert con != null;
            ps = con.prepareStatement(sql);
            ps.setString(1, String.valueOf(LoginController.currentUser.getUserID()));

            rs = ps.executeQuery();
            while (rs.next()) {
                //To make things complicated I've decided to use userBookings objects which contains 100% of the details and then use the data i get from it to then add the data to the observableList data i created.
                userBookings ub  = new userBookings(rs.getInt(1), rs.getInt(2), rs.getString(3), LocalTime.parse(rs.getString(4)), LocalTime.parse(rs.getString(5)),
                        LocalDate.parse(rs.getString(6)), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10));
                this.data.add(new CustomerBooking(ub.getRoomID(),(ub.getStartTime()+" to "+ub.getEndTime()), (ub.getStartDate()+" - "+ub.getEndDate()), ub.getDuration(), ub.getResources(), ub.getRefreshments(), ub.getRefreshmentsTime()));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }

        this.roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        this.timePeriodColumn.setCellValueFactory(new PropertyValueFactory<>("timePeriod"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("dates"));
        this.durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        this.equipmentColumn.setCellValueFactory(new PropertyValueFactory<>("equipment"));
        this.refreshmentsColumn.setCellValueFactory(new PropertyValueFactory<>("refreshments"));
        this.refreshmentsTimeColumn.setCellValueFactory(new PropertyValueFactory<>("refreshmentsTime"));
        //so that this doubles as a refresh function, i made it so that it removes existing data from the table and then adds them again.
        this.myTable.setItems(null);
        this.myTable.setItems(data);




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
