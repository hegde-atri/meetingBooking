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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdminController{
    private DBConnection db;
    private double duration =0;

    @FXML
    private Button cleanersDashboardButton;
    @FXML
    private Button caterersDashboardButton;
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


    //Initialises the window, so it will open with the table and pie chart populated
    public void initialize(){
        this.db = new DBConnection();
        setPieData();
//        this.chart.setData(getPieData());
        loadCustomerData();
    }

    //This method returns the data to be represented by the chart
    public void setPieData(){
        try{
            String sql = "SELECT * FROM Bookings";
            Connection con = DBConnection.getConnection();
            pieData = FXCollections.observableArrayList();
            ArrayList<userBookings> ubList = new ArrayList<userBookings>();
            assert con!=null;
            ResultSet rs = con.createStatement().executeQuery(sql);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            //Creates a new userBookings object while going through the table Bookings
            while(rs.next()){
                userBookings ub = new userBookings(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getTime(4).toLocalTime(), rs.getTime(5).toLocalTime(), rs.getDate(6).toLocalDate(), rs.getDate(7).toLocalDate(), rs.getString(8));
                ubList.add(ub);
            }
            //Goes through every single booking we have.
            for(userBookings x: ubList){
                LocalDate currentDate = LocalDate.now();
                //To get the bookings we have for today.
//                if(x.getStartDate() == currentDate){
                    if(x.getStartDate() == x.getEndDate()){
                        duration = duration + Duration.between(x.getStartTime(), x.getEndTime()).toMinutes();
                    }else{
                        //If the booking is over many days, we need to see how many hours they have booked for today.
                        LocalTime end = LocalTime.of(0, 0, 0);
                        duration = duration + Duration.between(end, x.getStartTime()).toMinutes();
//                    }
                }
            }
            //To get the duration when there are no bookings.
            int intDuration = (int)duration;
            int free = 3600 - intDuration;
            System.out.println(intDuration + "  " + free);
            pieData = FXCollections.observableArrayList(new PieChart.Data("Bookings", intDuration),
                                                        new PieChart.Data("Unoccupied", free));
            chart.setData(pieData);

        }catch(SQLException e){
            System.err.println("Error: " + e);
        }

//        ObservableList<PieChart.Data> pieChartData =
//                FXCollections.observableArrayList(
//                        new PieChart.Data("Meetings", 60),
//                        new PieChart.Data("Cleaning", 25),
//                        new PieChart.Data("Unoccupied", 15));
    }

    //This method will read the Users table from the database and transfer all the details including their password on to the Tableview
    @FXML
    public void loadCustomerData(){
        try{
            String sql = "SELECT * FROM Users";
            Connection con = DBConnection.getConnection();
            this.data = FXCollections.observableArrayList();

            assert con != null;
            ResultSet rs = con.createStatement().executeQuery(sql);
            while(rs.next()){
                this.data.add(new AccountData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            }
        }catch(SQLException e){
            System.err.println("Error: " + e);
        }

        this.IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        this.usernameColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        this.firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
        this.lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
        this.passwordColumn.setCellValueFactory(new PropertyValueFactory<>("Password"));
        this.emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        this.accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("AccountType"));

        this.myTable.setItems(null);
        this.myTable.setItems(data);



    }

    //Closes the current window and opens the cleaners dashboard
    public void openCleanersDashboard(){
        try {
            Stage old = (Stage) cleanersDashboardButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Cleaners/Cleaners.fxml").openStream());
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Cleaners.css").toExternalForm());
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
    public void openCaterersDashboard(){
        try {
            Stage old = (Stage) caterersDashboardButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Caterers/Caterers.fxml").openStream());
            Scene scene = new Scene(root, 640, 360);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Caterers.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Caterers Dashboard");
            stage.setResizable(false);
            old.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
