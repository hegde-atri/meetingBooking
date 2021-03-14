package RoomBooker;

import DBUtil.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private TextField endTimeField;
    @FXML
    private TextField startTimeField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField refreshmentsTimeBox;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextArea roomDescriptionBox;
    @FXML
    private javafx.scene.control.Spinner<Integer> roomSelector;

    public void initialize(){
        initializeSpinner();
        setDateBounds();

    }

    //This will adds a listener to the spinner that will be used to change the description of the room they have selected.
    public void initializeSpinner(){
        roomSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));
        roomDescriptionBox.setText("Room 1\nAccommodation size - 2 people\nDisabled access - false");
        roomSelector.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue()==1){
                    roomDescriptionBox.setText("Room 1\nAccommodation size - 2 people\nDisabled access - false");
                }else if(newValue.intValue()==2){
                    roomDescriptionBox.setText("Room 2\nAccommodation size - 4 people\nDisabled access - false");
                }else if(newValue.intValue()==3){
                    roomDescriptionBox.setText("Room 3\nAccommodation size - 8 people\nDisabled access - false");
                }else if(newValue.intValue()==4){
                    roomDescriptionBox.setText("Room 4\nAccommodation size - 15 people\nDisabled access - true");
                }else if(newValue.intValue()==5){
                    roomDescriptionBox.setText("Room 5\nAccommodation size - 50 people\nDisabled access - false");
                }
            }
        });
    }

    //This sets it so that you can't book on days that have already passed
    public void setDateBounds(){
        LocalDate minDate = LocalDate.now();
        datePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(minDate));
                    }});
    }

    public void setListView(){

    }
    //This will make sure that the user has entered valid values into the fields, this is different from checking for double bookings.(Where a room is booked for 2 people at the same time.)
    @FXML
    private boolean verifyFields(){
        try{
            String[] timeListSTR = refreshmentsTimeBox.getText().split(", ");
            ArrayList<LocalTime> timeList = new ArrayList<>();
            for(String x: timeListSTR){
                timeList.add(LocalTime.parse(x));
            }
            if(datePicker.getValue() == null){
                errorLabel.setText("Date cannot be empty!");
            }

            return true;
        }catch(Exception e){
            return false;
        }
    }

    @FXML
    private void setRoomDescriptionText(){

    }
}
