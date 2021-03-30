package RoomBooker;

import Cleaners.CleaningModel;
import DBUtil.DBConnection;
import Login.LoginController;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class RoomBooker {

    //<editor-fold desc="variables">
    @FXML
    private Label backgroundLabel;
    @FXML
    private Label tableLabel;
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

    private ArrayList<TimeSlot> bookedTimeSlots = new ArrayList<>();
    //</editor-fold>

    public void initialize() throws SQLException {
        LocalDate today = LocalDate.now();
        datePicker.setValue(today);
        infoLabel.setWrapText(true);
        initializeComboBox();
        initializeSpinner();
        setDateBounds();
        setTableView();

    }

    //This will adds a listener to the spinner that will be used to change the description of the room they have selected.
    public void initializeSpinner() {
        roomSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));
        roomDescriptionBox.setText("Room 1\nAccommodation size - 2 people\nDisabled access - false");
        roomSelector.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                errorLabel.setText("");
                infoLabel.setText("");
                roomDescriptionBox.setText("Room 1\nAccommodation size - 2 people\nDisabled access - false");
                try {
                    setTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (newValue.intValue() == 2) {
                errorLabel.setText("");
                infoLabel.setText("");
                roomDescriptionBox.setText("Room 2\nAccommodation size - 4 people\nDisabled access - false");
                try {
                    setTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (newValue.intValue() == 3) {
                errorLabel.setText("");
                infoLabel.setText("");
                roomDescriptionBox.setText("Room 3\nAccommodation size - 8 people\nDisabled access - false");
                try {
                    setTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (newValue.intValue() == 4) {
                errorLabel.setText("");
                infoLabel.setText("");
                roomDescriptionBox.setText("Room 4\nAccommodation size - 15 people\nDisabled access - true");
                try {
                    setTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (newValue.intValue() == 5) {
                errorLabel.setText("");
                infoLabel.setText("");
                roomDescriptionBox.setText("Room 5\nAccommodation size - 50 people\nDisabled access - false");
                try {
                    setTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
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
        try {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate == null) {
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
            while (rs.next()) {
                startTimes.add(LocalTime.parse(rs.getString(4)));
                endTimes.add(LocalTime.parse(rs.getString(5)));
            }


            ArrayList<TimeSlot> template = TimeSlot.createTimeSlots();
            ArrayList<TimeSlot> bookedTimeSlots = new ArrayList<>();
            ArrayList<TimeSlot> out = new ArrayList<>();

            //The methods below me could've easily been made into methods of the timeslot class, but TIME :/
            for (int x = 0; x < startTimes.size(); x++) {
                int length = TimeSlot.getSlotNumber(startTimes.get(x), endTimes.get(x));
                bookedTimeSlots.addAll(TimeSlot.returnTimeSlots(startTimes.get(x), length));

            }

            /*
            The method list contains returns true if the object ts is already existing in bookedTimeSlots. If it does not exist
            then we can add it to the out arrayList which will be displayed to the user
            */
            for(TimeSlot ts: template){
                if(!TimeSlot.listContains(ts, bookedTimeSlots)){
                    out.add(ts);
                }
            }

            this.bookedTimeSlots = bookedTimeSlots; // This list will later be used to verify if the user has booked when the room is free or not

            ObservableList<TimeSlot> data = FXCollections.observableArrayList(out);

            startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

            myTable.setItems(null);
            myTable.setItems(data);


        } catch (Exception e) {
            errorLabel.setText("xd");

        } finally {
            ps.close();
            rs.close();
        }

    }

    //This will validate all details, compare them for any conflicts before finally booking the room
    @FXML
    private void bookRoom() throws SQLException {
        if (verifyFields()) {
            if (checkBookings()) {
                //here
                if (checkDouble()) {
                    if (checkRefreshments()) {
                        //Even after checking for overlapping bookings, we need to make sure that the cleaners are free at this time and can clean the room before the next booking
                        if (roomCleaned()) {
                            if (addBooking()) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Message");
                                alert.setHeaderText(null);
                                alert.setContentText("Booking created!");

                                alert.showAndWait().ifPresent((btnType) -> {
                                    if (btnType == ButtonType.OK) {
                                        backToDashboard();
                                    }
                                });
                            } else {
                                infoLabel.setText("Note that we need to clean the rooms once you are done\n" +
                                        "Therefore some booking me be unavailable depending on\n" +
                                        "our cleaners!");
                            }
                        }else{
                            errorLabel.setText("Error with cleaners");
                        }
                    } else {
                        errorLabel.setText("Time selected for refreshments is unfortunately busy");
                        infoLabel.setText("Please make sure that the refreshments times are within selected time frame!");
                    }
                    //here
                } else {
                    errorLabel.setText("Error");
                    infoLabel.setText("User cannot make bookings in 2 rooms at the same time!");
                }
            } else {
                errorLabel.setText("Room busy at given time!");
            }

        } else {
            errorLabel.setText("Field verification error!");
        }

    }

    //This will take our details and add it to the Bookings table in our database
    private boolean addBooking() throws SQLException {
        String st = startTimeHour.getValue() + ":" + startTimeMin.getValue();
        String et = endTimeHour.getValue() + ":" + endTimeMin.getValue();


        PreparedStatement ps = null;
        String sql = "INSERT INTO Bookings(RoomID, UserID, Username, StartTime, EndTime, StartDate, EndDate, Resources, Refreshments, RefreshmentsTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = DBConnection.getConnection();
            assert con != null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, roomSelector.getValue());
            ps.setInt(2, LoginController.currentUser.getUserID());
            ps.setString(3, LoginController.currentUser.getUsername());
            ps.setString(4, st);
            ps.setString(5, et);
            ps.setString(6, datePicker.getValue().toString());
            ps.setString(7, datePicker.getValue().toString());
            ps.setString(8, resourcesTextField.getText());
            ps.setString(9, refreshmentsArea.getText());
            ps.setString(10, refreshmentsTimeBox.getText());
            ps.execute();

            errorLabel.setText("");
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        } finally {
            assert ps != null;
            ps.close();
        }
    }

    //This method will check for any overlapping bookings from the already booked slots to the time slots we requested
    public boolean checkBookings() {
        LocalTime selectedST = LocalTime.parse(startTimeHour.getValue() + ":" + startTimeMin.getValue());
        LocalTime selectedET = LocalTime.parse(endTimeHour.getValue() + ":" + endTimeMin.getValue());

        ArrayList<TimeSlot> requestingTS = TimeSlot.returnTimeSlots(selectedST, TimeSlot.getSlotNumber(selectedST, selectedET));

        //this.bookedTimeSlots is set data in the table view method
        for (TimeSlot bs : this.bookedTimeSlots) {
            for (TimeSlot rs : requestingTS) {
                if (rs.exists(bs)) {
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

                    if (!refreshmentsArea.getText().isEmpty() && !refreshmentsTimeBox.getText().isEmpty()) {
                        String[] refreshments = refreshmentsArea.getText().split("[,] ", 0);
                        String[] refreshmentTimes = refreshmentsTimeBox.getText().split("[,] ", 0);
                        for (String x : refreshmentTimes) {
                            LocalTime.parse(x);
                        }
                        if (refreshments.length == refreshmentTimes.length) {
                            errorLabel.setText("");
                            return true;
                        } else {
                            errorLabel.setText("Specify time for each refreshment");
                        }
                    } else if (refreshmentsArea.getText().isEmpty() && refreshmentsTimeBox.getText().isEmpty()) {
                        errorLabel.setText("");
                        return true;
                    } else {
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
            } else if (timeCompare == -1) {
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
    private boolean checkRefreshments() throws SQLException {

        if (refreshmentsTimeBox.getText().isEmpty()) {
            errorLabel.setText("");
            return true;
        }

        String[] refreshmentsTimes = refreshmentsTimeBox.getText().split("[,] ", 0);

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Refreshments WHERE RoomID = ? AND Date = ?";
        ArrayList<LocalTime> results = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();
            assert con != null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, roomSelector.getValue());
            ps.setString(2, datePicker.getValue().toString());

            rs = ps.executeQuery();
            while (rs.next()) {
                results.add(LocalTime.parse(rs.getString(3)));
            }

            for (String x : refreshmentsTimes) {
                for (LocalTime existingRTS : results) {
                    LocalTime rts = LocalTime.parse(x);
                    if (rts.compareTo(existingRTS) == 0) {
                        return false;
                    }
                }
            }

            //To make sure that the time they give is actually withing the booked time;
            for (String x : refreshmentsTimes) {
                LocalTime st = LocalTime.parse(startTimeHour.getValue() + ":" + startTimeMin.getValue());
                LocalTime et = LocalTime.parse(endTimeHour.getValue() + ":" + endTimeMin.getValue());
                LocalTime rt = LocalTime.parse(x);

                boolean condition1 = rt.isAfter(st);
                boolean condition2 = rt.isBefore(et);

                if (condition1 && condition2) {
                    errorLabel.setText("");
                    return addRefreshment();
                }
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            //To close the connection
            assert ps != null;
            ps.close();
            assert rs != null;
            rs.close();
        }
    }

    public boolean addRefreshment() throws SQLException {
        String[] refreshments = refreshmentsArea.getText().split("[,] ", 0);
        String[] refreshmentTimes = refreshmentsTimeBox.getText().split("[,] ", 0);
        PreparedStatement ps = null;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO Refreshments(RoomID, Date, Time, Refreshment) VALUES (?, ?, ?, ?)";
            assert con != null;
            for (int x = 0; x < refreshments.length; x++) {
                ps = con.prepareStatement(sql);
                ps.setInt(1, roomSelector.getValue());
                ps.setString(2, datePicker.getValue().toString());
                ps.setString(3, refreshmentTimes[x]);
                ps.setString(4, refreshments[x]);
                ps.execute();
            }
            errorLabel.setText("");
            return true;
        } catch (Exception e) {
            errorLabel.setText("Error adding refreshment details");
            System.out.println("Error: " + e);
            return false;
        } finally {
            assert ps != null;
            ps.close();
        }


    }

    //This will return use to the customer dashboard.
    @FXML
    private void backToDashboard() {
        try {
            //We need to get the old stage, so we can close it before we go back to the login page
            Stage old = (Stage) backButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/Customer/Customer.fxml").openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Stylesheets/Customer.css").toExternalForm());
            stage.getIcons().add(new Image("/images/customer.png"));
            stage.setScene(scene);
            stage.setTitle("Customer Dashboard");
            stage.setResizable(false);
            old.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean checkDouble() throws SQLException {
        PreparedStatement ps =null;
        ResultSet rs=null;
        String sql = "SELECT * FROM Bookings WHERE UserID = ? and StartDate = ?";
        String date = datePicker.getValue().toString();
        LocalTime startTime = LocalTime.parse(startTimeHour.getValue() + ":" + startTimeMin.getValue());
        LocalTime endTime = LocalTime.parse(endTimeHour.getValue() + ":" + endTimeMin.getValue());
        ArrayList<TimeSlot> results = new ArrayList<>();
        try{
            Connection con = DBConnection.getConnection();
            assert con != null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, LoginController.currentUser.getUserID());
            ps.setString(2, date);

            ArrayList<TimeSlot> userBookings = new ArrayList<>();
            ArrayList<TimeSlot> selectedTimeSlots = new ArrayList<>(TimeSlot.returnTimeSlots(startTime, TimeSlot.getSlotNumber(startTime, endTime)));


            rs = ps.executeQuery();
            while(rs.next()){
                results.add(new TimeSlot(rs.getString(4), rs.getString(5)));
            }

            //Now we are going to split all objects in the userBookings arraylist into 30 min timeslots from the results we got from the database
            for(TimeSlot x: results){
                userBookings.addAll(TimeSlot.returnTimeSlots(x.getStartTime(), TimeSlot.getSlotNumber(x.getStartTime(), x.getEndTime())));
            }

            //rq as in requesting slot
            for(TimeSlot ub: userBookings){
                for(TimeSlot rq: selectedTimeSlots){
                    if(rq.exists(ub)){
                        return false;
                    }
                }

//                System.out.println(x.toString());
//                boolean condition1 = startTime.isAfter(x.getStartTime());
//                boolean condition2 = startTime.isBefore(x.getEndTime());
//                boolean condition3 = endTime.isBefore(x.getEndTime());
//                boolean condition4 = endTime.isAfter(x.getStartTime());
//                System.out.println(startTime.toString());
//                System.out.println(endTime.toString());
//                if((condition1 && condition2) || (condition3 && condition4)){
//                    return false;
//                }
//                if(startTime.equals(x.getStartTime()) || endTime.equals(x.getEndTime())){
//                    return false;
//                }
            }

            return true;

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            assert ps != null;
            assert rs != null;
            ps.close();
            rs.close();
        }
        return false;
    }

    /*
    To make sure that the rooms are always cleaned before someone books them we can do 2 things :
    1. Make sure the room is cleaned before someone books the room
    2.Clean the room after the end time and before someone else books the room
    For this project I'm going to implement a simple system where it checks whether the room can be cleaned
    either right after the endTime or right before the next booking of that room.
     */

    public boolean roomCleaned() throws SQLException {
        String et = endTimeHour.getValue() + ":" + endTimeMin.getValue();
        CleaningModel cleaner = new CleaningModel(et, datePicker.getValue());
        cleaner.getCleanerTimes();
        if(cleaner.isBooked()){
            int roomID = roomSelector.getValue();
            String date = datePicker.getValue().toString();
            //Cleaner is booked, and we will try to see if the cleaner is busy even before the next room booking
            if(cleaner.getNextBooking(date) != null){
                cleaner.addCleanerBooking(roomID, (cleaner.getNextBooking(date)), datePicker.getValue());
                return true;
            }else{
                return false;
            }
        }else{
            //Cleaner is not booked, so we can add a cleaner booking for this time.
            return cleaner.addCleanerBooking(roomSelector.getValue(), LocalTime.parse(et), datePicker.getValue());
        }
    }

    //bad coding as its over 500 lines :/
}
