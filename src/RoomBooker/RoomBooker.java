package RoomBooker;

import DBUtil.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


@SuppressWarnings("rawtypes")
public class RoomBooker {
    @FXML
    private Label errorLabel;
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
    private ListView<String> listView;
    @FXML
    private TextArea roomDescriptionBox;
    @FXML
    private TextArea refreshmentsArea;
    @FXML
    private javafx.scene.control.Spinner<Integer> roomSelector;

    public void initialize() {
        initializeComboBox();
        initializeSpinner();
        setDateBounds();

    }

    //This will adds a listener to the spinner that will be used to change the description of the room they have selected.
    public void initializeSpinner() {
        roomSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));
        roomDescriptionBox.setText("Room 1\nAccommodation size - 2 people\nDisabled access - false");
        roomSelector.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                roomDescriptionBox.setText("Room 1\nAccommodation size - 2 people\nDisabled access - false");
            } else if (newValue.intValue() == 2) {
                roomDescriptionBox.setText("Room 2\nAccommodation size - 4 people\nDisabled access - false");
            } else if (newValue.intValue() == 3) {
                roomDescriptionBox.setText("Room 3\nAccommodation size - 8 people\nDisabled access - false");
            } else if (newValue.intValue() == 4) {
                roomDescriptionBox.setText("Room 4\nAccommodation size - 15 people\nDisabled access - true");
            } else if (newValue.intValue() == 5) {
                roomDescriptionBox.setText("Room 5\nAccommodation size - 50 people\nDisabled access - false");
            }
        });
    }

    //This will add the time options into the respective combo boxes
    public void initializeComboBox() {
        ObservableList<String> hourOptions = FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22");
        startTimeHour.getItems().addAll(hourOptions);
        endTimeHour.getItems().addAll(hourOptions);
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
    public void setListView() {
        try {
            LocalDate selectedDate = datePicker.getValue();
            /*
            need to write a query to database getting all the bookings for the date selected
            and then i need to add a line to the list view every 30 minutes that it is not free.
            --> I also need to make sure that they can only book in 30 minute blocks and can't have booking like these 12:50-13:20
             */
            PreparedStatement ps;
            ResultSet rs;
            String sql = "SELECT * FROM Bookings WHERE RoomID IS ? AND StartDate IS ?";
            Connection con = DBConnection.getConnection();
            assert con !=null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, roomSelector.getValue());
            ps.setString(2, selectedDate.toString());

            rs = ps.executeQuery();
            while(rs.next()){

            }


        } catch (Exception e) {
            errorLabel.setText("Invalid date");
        }
    }

    @FXML
    private void bookRoom() {
        if(verifyFields()){

        }else{
            errorLabel.setText("Unable to book room!");
        }

    }



    public boolean verifyFields(){
        return verifyFieldsNull() && verifyBookTimes();
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
                if(startTimeHour.getValue()!=null && startTimeMin.getValue()!=null && endTimeHour.getValue()!=null && endTimeMin.getValue()!=null){
                    System.out.println("Hello");
                    //Checks refreshments field
                    if (refreshmentsArea.getText() != null) {
                        String[] refreshmentsList = refreshmentsArea.getText().split(", ");
                        if(refreshmentsTimeBox.getText() != null){
                            //I'm gonna parse the times into Local time so if they entered an invalid date, i can validate invalid time formats here (Exception will be raised so it will go to the catch block)
                            String[] refreshmentsTimeList = refreshmentsTimeBox.getText().split(", ");
                            for (String x : refreshmentsTimeList) {
                                LocalTime.parse(x);
                            }
                            if(refreshmentsList.length == refreshmentsTimeList.length){
                                errorLabel.setText("");
                                return true;
                            }else{
                                errorLabel.setText("Incorrect amount of refreshment times.");
                            }
                        }else{
                            errorLabel.setText("Mention the time for the refreshments");
                        }
                    }else{
                        return true;
                    }
                }else{
                    errorLabel.setText("Select time");
                }
            }else{
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
            String x = startTimeHour.getValue()+":"+startTimeMin.getValue();
            LocalTime st = LocalTime.parse(x);
            x = endTimeHour.getValue()+":"+endTimeMin.getValue();
            LocalTime et = LocalTime.parse(x);

            int timeCompare = st.compareTo(et);
            if (timeCompare < 0) {
                return false;
            } else if (timeCompare == 0) {
                errorLabel.setText("Start time cannot be end time.");
                return false;
            } else {
                errorLabel.setText("Invalid times");
                return false;
            }

        } catch (Exception e) {
            errorLabel.setText("Error entering time");
            return false;
        }
    }
}
