package Admin;

import DBUtil.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminController{
    private DBConnection db;

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


    //Initialises the window, so it will open with the table and pie chart populated
    public void initialize(){
        this.db = new DBConnection();
        this.chart.setData(getPieData());
        loadCustomerData();
    }

    public ObservableList<PieChart.Data> getPieData(){
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Meetings", 60),
                        new PieChart.Data("Cleaning", 25),
                        new PieChart.Data("Unoccupied", 15));
        return pieChartData;
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


}
