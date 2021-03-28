package Cleaners;

//I wanted to quickly make a class for the table view in CleanersController

public class CleaningSlot {
    private int roomID;
    private String startTime;
    private String endTime;

    public CleaningSlot(int roomID, String startTime, String endTime) {
        this.roomID = roomID;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
