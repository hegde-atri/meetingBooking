package Customer;

import Admin.AccountData;
import Admin.userBookings;
import DBUtil.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CustomerController {
    @FXML
    private TableView<CustomerBooking> myTable;
    @FXML
    private TableColumn<CustomerBooking, String> roomIDColumn;
    @FXML
    private TableColumn<CustomerBooking, String> timePeriodColumn;
    @FXML
    private TableColumn<CustomerBooking, String> dateColumn;
    @FXML
    private TableColumn<CustomerBooking, String> equipmentColumn;
    @FXML
    private TableColumn<CustomerBooking, String> refreshmentsColumn;
    @FXML
    private TableColumn<CustomerBooking, String> refreshmentsTimeColumn;
    private ObservableList<CustomerBooking> data;

    public void initialize(){
        loadBookingData();
    }

    @FXML
    public void loadBookingData() {
        try {
            String sql = "SELECT * FROM Bookings WHERE UserID IS 1";
            Connection con = DBConnection.getConnection();
            this.data = FXCollections.observableArrayList();
            ArrayList<CustomerBooking> cb = new ArrayList<>();

            assert con != null;
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                userBookings ub  = new userBookings(rs.getInt(1), rs.getInt(2), rs.getString(3), LocalTime.parse(rs.getString(4)), LocalTime.parse(rs.getString(5)),
                        LocalDate.parse(rs.getString(6)), LocalDate.parse(rs.getString(7)), rs.getString(8), rs.getString(9), rs.getString(10));
                cb.add(new CustomerBooking(ub.getRoomID(),(ub.getStartTime()+" - "+ub.getEndTime()), (ub.getStartDate()+" - "+ub.getEndDate()), ub.getDuration(), ub.getResources(), ub.getRefreshments(), ub.getRefreshmentsTime()));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }

        this.roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("Room ID"));
        this.timePeriodColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        this.equipmentColumn.setCellValueFactory(new PropertyValueFactory<>("Equipment"));
        this.refreshmentsColumn.setCellValueFactory(new PropertyValueFactory<>("Refreshments"));
        this.refreshmentsTimeColumn.setCellValueFactory(new PropertyValueFactory<>("Refreshments Time"));
        //so that this doubles as a refresh function, i made it so that it removes existing data from the table and then adds them again.
        this.myTable.setItems(null);
        this.myTable.setItems(data);


    }
}
