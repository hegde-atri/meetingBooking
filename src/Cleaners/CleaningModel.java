package Cleaners;

//This class is what will generate cleaning schedules and make the rooms unavailable until they are cleaned.
//The cleaning slot for a room is added to the bookings by the user CLEANER

import DBUtil.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalTime;

public class CleaningModel {

    private String endTime;

    public CleaningModel(String endTime, String startDate) {
        this.endTime = endTime;

    }

    public void addCleaning(){
        try{
            if(isCleanerFree()){

            }else{
                String closestTime = getCleanerFreeTime();
            }

        }catch(Exception e){

        }
    }

    public boolean isCleanerFree(){
        try{
            Connection con = DBConnection.getConnection();
            PreparedStatement ps;
            String sql = "SELECT * FROM Bookings WHERE UserID IS ? AND StartTime IS ? AND StartDate is ?";

        }catch(Exception e){

        }
        return false;
    }

    public String getCleanerFreeTime(){

        return null;
    }
}
