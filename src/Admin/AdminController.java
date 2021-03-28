package Admin;

import DBUtil.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AdminController {

    //<editor-fold desc="variables">
    @FXML
    private Button cleanersDashboardButton;
    @FXML
    private Button caterersDashboardButton;
    @FXML
    private Button addAdminButton;
    @FXML
    private Button viewFullScheduleButton;
    @FXML
    private Label backgroundLabel;
    @FXML
    private Label dbSizeLabel;
    @FXML
    private Label DBOnlineLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label headingLabel;
    @FXML
    private Button clearDB;
    @FXML
    private Button reloadButton;
    @FXML
    private PieChart chart;
    @FXML
    private TableView<AccountData> myTable;
    @FXML
    private TableColumn<AccountData, String> IDColumn;
    @FXML
    private TableColumn<AccountData, String> usernameColumn;
    @FXML
    private TableColumn<AccountData, String> firstnameColumn;
    @FXML
    private TableColumn<AccountData, String> lastnameColumn;
    @FXML
    private TableColumn<AccountData, String> passwordColumn;
    @FXML
    private TableColumn<AccountData, String> emailColumn;
    @FXML
    private TableColumn<AccountData, String> accountTypeColumn;
    private ObservableList<AccountData> data;
    private ObservableList pieData;
    int duration = 0;
    //</editor-fold>


    //Initialises the window, so it will open with the table and pie chart populated
    public void initialize() {
        DBOnlineLabel.setText("DB online");
        setPieData();
        loadCustomerData();
        getDBSize("Meetings.sqlite");
    }

    //This method loads the data onto the pi chart
    public void setPieData() {
        try {
            duration = 0;
            String sql = "SELECT * FROM Bookings";
            Connection con = DBConnection.getConnection();
            pieData = FXCollections.observableArrayList();
            ArrayList<userBookings> ubList = new ArrayList<>();
            assert con != null;
            ResultSet rs = con.createStatement().executeQuery(sql);

            //Creates a new userBookings object while going through the table Bookings
            while (rs.next()) {
                userBookings ub = new userBookings(rs.getInt(1), rs.getInt(2), rs.getString(3), LocalTime.parse(rs.getString(4)), LocalTime.parse(rs.getString(5)),
                        LocalDate.parse(rs.getString(6)), LocalDate.parse(rs.getString(7)), rs.getString(8));
                ubList.add(ub);
            }

            //Goes through every single booking we have.
            for (userBookings x : ubList) {
                //To get the bookings we have for today.
                duration = duration + x.getTodayDuration();
                }

            //To get the duration when there are no bookings.

            int free = 1440 - duration;
            pieData = FXCollections.observableArrayList(new PieChart.Data("Bookings", duration),
                    new PieChart.Data("Unoccupied", free));
            //Sets it to null first and then adds the data so that this method can be used when the page is refreshed
            chart.setData(pieData);

        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }

    }

    //This method will read the Users table from the database and transfer all the details including their password on to the Tableview
    @FXML
    public void loadCustomerData() {
        try {
            String sql = "SELECT * FROM Users";
            Connection con = DBConnection.getConnection();
            this.data = FXCollections.observableArrayList();

            assert con != null;
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                this.data.add(new AccountData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }

        this.IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        this.usernameColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        this.firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
        this.lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
        this.passwordColumn.setCellValueFactory(new PropertyValueFactory<>("Password"));
        this.emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        this.accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("AccountType"));
        //so that this doubles as a refresh function, i made it so that it removes existing data from the table and then adds them again.
        this.myTable.setItems(null);
        this.myTable.setItems(data);


    }

    //ability to add an admin
    @FXML
    public void addAdmin() {
        try {
            Stage old = (Stage) addAdminButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Admin/adminCreation.fxml").openStream());
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Register.css").toExternalForm());
            stage.getIcons().add(new Image("/images/add.png"));
            stage.setScene(scene);
            stage.setTitle("Register admin");
            stage.setResizable(false);
            old.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //reloads the page using data from database. calls the initialise method.
    @FXML
    public void refreshPage() {
        initialize();
    }

    //displays the size of the database file.
    @FXML
    public void getDBSize(String fileName) {
        Path path = Paths.get(fileName);
        try {
            // size of a file (in bytes)
            long bytes = Files.size(path);
            dbSizeLabel.setText(String.format( "Database size: "+ "%,d KB", bytes / 1024));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Delete all booking details before today.
    @FXML
    private void clearUpDB(){
        try {
            //removes old data from bookings table
            String sql = "DELETE FROM Bookings WHERE EndDate <= date('now','-1 day')";
            Connection con = DBConnection.getConnection();
            assert con != null;
            con.createStatement().execute(sql);
            //removes old data from Refreshments table
            sql = "DELETE FROM Refreshments WHERE Date <= date('now','-1 day')";
            con.createStatement().execute(sql);
        }catch(SQLException e){
            System.out.println("Error: " + e);
        }

    }

    //<editor-fold desc="methods that open different windows">

    //redirects you back to login
    @FXML
    private void backToLogin() {
        try {
            Stage old = (Stage) cleanersDashboardButton.getScene().getWindow();
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

    //Closes the current window and opens the cleaners dashboard
    @FXML
    private void openCleanersDashboard() {
        try {
            Stage old = (Stage) cleanersDashboardButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Cleaners/Cleaners.fxml").openStream());
            Scene scene = new Scene(root, 640, 720);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Cleaners.css").toExternalForm());
            stage.getIcons().add(new Image("/images/cleaners.png"));
            stage.setScene(scene);
            stage.setTitle("Cleaners Dashboard");
            stage.setResizable(false);
            old.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Closes the current window and opens the caterers dashboard
    @FXML
    private void openCaterersDashboard() {
        try {
            Stage old = (Stage) caterersDashboardButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Caterers/Caterers.fxml").openStream());
            Scene scene = new Scene(root, 640, 360);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Caterers.css").toExternalForm());
            stage.getIcons().add(new Image("/images/caterers.png"));
            stage.setScene(scene);
            stage.setTitle("Caterers Dashboard");
            stage.setResizable(false);
            old.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //</editor-fold>


}
