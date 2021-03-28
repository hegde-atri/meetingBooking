package Cleaners;

import Admin.AccountData;
import DBUtil.DBConnection;
import Login.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
import java.util.ArrayList;

public class CleanersController {
    @FXML
    private Button backButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<CleaningSlot> myTable;
    @FXML
    private TableColumn<CleaningSlot, String> roomIDColumn;
    @FXML
    private TableColumn<CleaningSlot, String> startTimeColumn;
    @FXML
    private TableColumn<CleaningSlot, String> endTimeColumn;
    private ObservableList<CleaningSlot> data;

    public void initialize() throws SQLException {
        initializeDatePicker();
        loadTable();

    }

    //This method will set default date to current date and will add a listener to the datePicker so that the table can update
    //automatically when we change the selected date instead of having a button for it.
    private void initializeDatePicker(){
        LocalDate today = LocalDate.now();
        datePicker.setValue(today);

        datePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            try {
                loadTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }


    //This will load the data onto the table for the selected date
    @FXML
    private void loadTable() throws SQLException {
        try{
            data = FXCollections.observableArrayList();

            String date = datePicker.getValue().toString();
            String sql = "SELECT * FROM Bookings WHERE UserID = ? AND StartDate = ?";
            Connection con = DBConnection.getConnection();

            assert con != null;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, 2);
            System.out.println(date);
            ps.setString(2, date);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                data.add(new CleaningSlot(rs.getString(1), rs.getString(4), rs.getString(5)));
            }

            for(CleaningSlot x: data){
                System.out.println(x.toString());
            }

        }catch(Exception e){
            e.printStackTrace();
        }
            this.roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
            this.startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            this.endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            //this will make sure that the data doesn't just get added onto the table as we check different dates
            //but will display the times for each day only
            this.myTable.setItems(null);
            this.myTable.setItems(data);

    }


    //This will take us back to the admin panel
    @FXML
    public void returnToAdmin() {
        try {
            Stage old = (Stage) this.backButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Admin/Admin.fxml").openStream());
            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Admin.css").toExternalForm());
            stage.getIcons().add(new Image("/images/admin.png"));
            stage.setScene(scene);
            stage.setTitle("Admin Dashboard");
            stage.setResizable(false);
            old.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
