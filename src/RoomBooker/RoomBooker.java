package RoomBooker;

import DBUtil.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


@SuppressWarnings("rawtypes")
public class RoomBooker {
    @FXML
    private Label errorLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Button refreshButton;
    @FXML
    private Button bookButton;
    @FXML
    private Button backButton;
    @FXML
    private TextField resourcesTextField;
    @FXML
    private ComboBox startTimeHour;
    @FXML
    private ComboBox startTimeMin;
    @FXML
    private ComboBox endTimeHour;
    @FXML
    private ComboBox endTimeMin;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField refreshmentsTimeBox;
    @FXML
    private TextArea roomDescriptionBox;
    @FXML
    private TextArea refreshmentsArea;
    @FXML
    private javafx.scene.control.Spinner<Integer> roomSelector;
    @FXML
    private TableView<TimeSlot> myTable;
    @FXML
    private TableColumn<TimeSlot, String> startTimeColumn;
    @FXML
    private TableColumn<TimeSlot, String> endTimeColumn;

    ArrayList<TimeSlot> bookedTimeSlots = new ArrayList<>();

    public void initialize() throws SQLException {
        LocalDate today = LocalDate.now();
        datePicker.setValue(today);
        initializeComboBox();
        initializeSpinner();
        setDateBounds();
        setTableView();

    }

    //This will adds a listener to the spinner that will be used to change the description of the room they have selected.
    public void initializeSpinner(){
        roomSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));
        roomDescriptionBox.setText("Room 1\nAccommodation size - 2 people\nDisabled access - false");
        roomSelector.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                roomDescriptionBox.setText("Room 1\nAccommodation size - 2 people\nDisabled access - false");
                try {
                    setTableView();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else if (newValue.intValue() == 2) {
                roomDescriptionBox.setText("Room 2\nAccommodation size - 4 people\nDisabled access - false");
                try {
                    setTableView();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else if (newValue.intValue() == 3) {
                roomDescriptionBox.setText("Room 3\nAccommodation size - 8 people\nDisabled access - false");
                try {
                    setTableView();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else if (newValue.intValue() == 4) {
                roomDescriptionBox.setText("Room 4\nAccommodation size - 15 people\nDisabled access - true");
                try {
                    setTableView();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else if (newValue.intValue() == 5) {
                roomDescriptionBox.setText("Room 5\nAccommodation size - 50 people\nDisabled access - false");
                try {
                    setTableView();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    //This will add the time options into the respective combo boxes
    public void initializeComboBox() {
        ObservableList<String> startHourOptions = FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21");
        ObservableList<String> endHourOptions = FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22");
        startTimeHour.getItems().addAll(startHourOptions);
        endTimeHour.getItems().addAll(endHourOptions);
        ObservableList<String> minuteOptions = FXCollections.observableArrayList("00", "30");
        startTimeMin.getItems().addAll(minuteOptions);
        endTimeMin.getItems().addAll(minuteOptions);

    }

    //This sets it so that you can't book on days that have already passed
    public void setDateBounds() {
        LocalDate minDate = LocalDate.now();
        datePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(minDate));
                    }
                });
    }

    //This code does nothing but display the free times for the date selected in 30 minute intervals and adds them to the list view
    @FXML
    public void setTableView() throws SQLException {
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "SELECT * FROM Bookings WHERE RoomID = ? and StartDate = ?";
            try{
                LocalDate selectedDate = datePicker.getValue();
                if(selectedDate==null){
                    return;
                }
                Connection con = DBConnection.getConnection();
                assert con != null;
                ps = con.prepareStatement(sql);

                int selectedRoom = roomSelector.getValue();

                ps.setInt(1, selectedRoom);
                ps.setString(2, selectedDate.toString());


                ArrayList<LocalTime> startTimes = new ArrayList<>();
                ArrayList<LocalTime> endTimes = new ArrayList<>();

                rs = ps.executeQuery();
                while(rs.next()){
                    startTimes.add(LocalTime.parse(rs.getString(4)));
                    endTimes.add(LocalTime.parse(rs.getString(5)));
                }


                ArrayList<TimeSlot> bookedTimeSlots = new ArrayList<>();

                //The methods below me could've easily been made into methods of the timeslot class, but TIME :/
                for(int x=0; x<startTimes.size(); x++)
                {
                    int length = TimeSlot.getSlotNumber(startTimes.get(x), endTimes.get(x));
                    bookedTimeSlots.addAll(TimeSlot.returnTimeSlots(startTimes.get(x), length));

                }

                this.bookedTimeSlots = bookedTimeSlots;
                //I was going to make it so that they can see the free times, but for now I've made it so that they can see the times that are not free.
                ObservableList<TimeSlot> data = FXCollections.observableArrayList(this.bookedTimeSlots);

                startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
                endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

                myTable.setItems(null);
                myTable.setItems(data);


            }catch(Exception e){
                errorLabel.setText("xd");

            }finally{
                ps.close();
                rs.close();
            }

    }

    //This will validate all details, compare them for any conflicts before finally booking the room
    @FXML
    private void bookRoom() {
        if (verifyFields()) {
            if(checkBookings()){
                if(checkRefreshments()){
                    //Even after checking for overlapping bookings, we need to make sure that the cleaners are free at this time and can clean the room before the next booking
                    if(addBooking()){

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Booking created!");

                        alert.showAndWait().ifPresent((btnType) -> {
                            if (btnType == ButtonType.OK) {
                                backToDashboard();
                            }
                        });
                    }else{
                        infoLabel.setText("Note that we need to clean the rooms once you are done\nTherefore some booking me be unavailable depending on\nour cleaners!");
                    }
                }else{
                    errorLabel.setText("Time selected for refreshments is unfortunately busy");
                }
            }else{
                errorLabel.setText("Room busy at given time!");
            }

        } else {
            errorLabel.setText("Field verification error!");
        }

    }

    //This will take our details and add it to the Bookings table in our database
    private boolean addBooking(){
        return true;
    }

    //This method will check for any overlapping bookings from the already booked slots to the time slots we requested
    public boolean checkBookings(){
        LocalTime selectedST = LocalTime.parse(startTimeHour.getValue() + ":" + startTimeMin.getValue());
        LocalTime selectedET = LocalTime.parse(endTimeHour.getValue() + ":" + endTimeMin.getValue());

        ArrayList<TimeSlot> requestingTS = TimeSlot.returnTimeSlots(selectedST, TimeSlot.getSlotNumber(selectedST, selectedET));

        for(TimeSlot bs: this.bookedTimeSlots){
            for(TimeSlot rs: requestingTS){
                if(rs.exists(bs)){
                    return false;
                }
            }
        }

        return true;
    }

    public boolean verifyFields() {
        if (verifyFieldsNull()) {
            return verifyBookTimes();
        }
        return false;
    }

    //This will make sure that the user has entered valid values into the fields, this is different from checking for double bookings.(Where a room is booked for 2 people at the same time.)
    private boolean verifyFieldsNull() {
        try {

            /*
            The next block of code has multiple nested if statements, and its confusing to read so I've explained it here
            It checks whether all the fields are null, except resources field, refreshments and refreshments time field. That is the reason
            you can see "else return true" if refreshments area is null; If it is not null I need to make sure they enter a time that i need to deliver it at.
            They need to mention the time for each refreshments, e.g if they ask for 2 refreshments i require 2 refreshment time values.
            All the other else statements just add text to the error label the user know where they have gone wrong.
             */

            //Checks datePicker
            if (datePicker.getValue() != null) {
                //This will check if something has been selected in the time combo boxes
                if (startTimeHour.getValue() != null && startTimeMin.getValue() != null && endTimeHour.getValue() != null && endTimeMin.getValue() != null) {
                    //Checks resources field
                    if(!refreshmentsArea.getText().isEmpty() && !refreshmentsTimeBox.getText().isEmpty()){
                        String[] refreshments = refreshmentsArea.getText().split("[,] ", 0);
                        String[] refreshmentTimes = refreshmentsTimeBox.getText().split("[,] ", 0);
                        if(refreshments.length == refreshmentTimes.length){
                            errorLabel.setText("");
                            return true;
                        }else{
                            errorLabel.setText("Specify time for each refreshment");
                        }
                    }else if(refreshmentsArea.getText().isEmpty() && refreshmentsTimeBox.getText().isEmpty()){
                        errorLabel.setText("");
                        return true;
                    }else{
                        errorLabel.setText("Enter both refreshment fields");
                    }
                } else {
                    errorLabel.setText("Select time");
                }
            } else {
                errorLabel.setText("Date cannot be empty!");
            }

            return false;

        } catch (Exception e) {
            errorLabel.setText("Invalid details");
            return false;
        }
    }

    //This will take the values entered by the user and compares to the database to see if there are any clashes with other bookings.
    private boolean verifyBookTimes() {
        try {
            String x = startTimeHour.getValue() + ":" + startTimeMin.getValue();
            LocalTime st = LocalTime.parse(x);
            x = endTimeHour.getValue() + ":" + endTimeMin.getValue();
            LocalTime et = LocalTime.parse(x);

            int timeCompare = st.compareTo(et);

            if (timeCompare == 0) {
                errorLabel.setText("End time cannot be start time");
            } else if (timeCompare == 1) {
                errorLabel.setText("End time cannot be before start time");
            } else if (endTimeHour.getValue().equals("22") && endTimeMin.getValue().equals("30")) {
                errorLabel.setText("End time out of bounds!");
            }else if (timeCompare == -1) {
                errorLabel.setText("");
                return true;
            }
            return false;

        } catch (Exception e) {
            errorLabel.setText("Error entering time");
            return false;
        }
    }

    //This method will make sure that the caterers are free to deliver the refreshments for the selected time.
    private boolean checkRefreshments(){
        return false;
    }

    //This will return use to the customer dashbaord.
    private void backToDashboard(){

    }

}
