package Cleaners;

//I wanted to quickly make a class for the table view in CleanersController

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CleaningSlot {
    private final String roomID;
    private final String startTime;
    private final String endTime;

    public CleaningSlot(String roomID, String startTime, String endTime) {
        this.roomID = (roomID);
        this.startTime = (startTime);
        this.endTime = (endTime);
    }

    @Override
    public String toString(){
        return this.roomID + " "+ this.startTime + " " + this.endTime;
    }
}
