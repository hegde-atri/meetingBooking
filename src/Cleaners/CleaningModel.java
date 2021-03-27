package Cleaners;

//This class is what will generate cleaning schedules and make the rooms unavailable until they are cleaned.
//The cleaning slot for a room is added to the bookings by the user CLEANER

import DBUtil.DBConnection;
import Admin.userBookings;
import RoomBooker.TimeSlot;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class CleaningModel {

    private LocalTime startTime;
    private LocalDate startDate;

    private ArrayList<userBookings> bookedTimes = new ArrayList<>();

    public CleaningModel(String startTime, LocalDate startDate) {
        this.startTime = LocalTime.parse(startTime);
        this.startDate = startDate;

    }


    //This will look at the database to see when the cleaner has cleaning tasks/bookings
    public void getCleanerTimes() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            Connection con = DBConnection.getConnection();
            //Here user ID is sufficient, and need to make sure that the cleaner's account always has the ID 2
            String sql = "SELECT * FROM Bookings WHERE UserID IS ? AND StartDate is ?";
            assert con != null;

            ps = con.prepareStatement(sql);
            ps.setInt(1, 2);
            ps.setString(2, startDate.toString());
            rs = ps.executeQuery();

            while(rs.next()){
                bookedTimes.add(new userBookings(rs.getInt(1), rs.getInt(2), LocalTime.parse(rs.getString(4)), LocalDate.parse(rs.getString(6))));
            }

        }catch(Exception e){
            System.out.println("Error: " + e);

        }finally{
            assert ps != null;
            ps.close();
            assert rs != null;
            rs.close();
        }
    }

    //This method will see if the requested start time is appropriate for the cleaners
    public boolean isBooked(){
        for(userBookings x: bookedTimes){
            boolean condition1 = startTime.equals(x.getStartTime());
            if(condition1){
                return true;
            }
        }
        //Will return false if the cleaner is not booked for the slotting starting at st (variable st)
        return false;
    }

    //This will add the cleaner timeslot of 30 minutes with startTime as we passed
    public boolean addCleanerBooking(int roomID, LocalTime st, LocalDate date){
        LocalTime et = st.plus(30, ChronoUnit.MINUTES);
        String sql = "INSERT INTO Bookings(RoomID, UserID, Username, StartTime, EndTime, StartDate, EndDate, Resources, Refreshments, RefreshmentsTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement ps = null;
            Connection con = DBConnection.getConnection();
            assert con != null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, roomID);
            ps.setInt(2, 2);
            ps.setString(3, "");
            ps.setString(4, st.toString());
            ps.setString(5, et.toString());
            ps.setString(6, date.toString());
            ps.setString(7, date.toString());
            ps.setString(8, "");
            ps.setString(9, "");
            ps.setString(10, "");
            ps.execute();
            return true;
        }catch(Exception e){
            System.out.println("Error: "+e);
            return false;
        }

    }

    //This method will return the next closest booking of the room to see if the room can be cleaned before then.
    public LocalTime getNextBooking(int roomID, String sd){
        String sql = "SELECT * FROM Bookings WHERE RoomID = ? AND StartDate = ? ORDER BY StartTime ASC";
        try{
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ArrayList<userBookings> ub = new ArrayList<>();

            assert con != null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, roomID);
            rs = ps.executeQuery();
            while(rs.next()){
                ub.add(new userBookings(rs.getInt(1), rs.getInt(2), LocalTime.parse(rs.getString(4)), LocalDate.parse(rs.getString(6))));
            }

            //Since the result set is ordered we will get the immediate booking and not just a booking with a later time.
            if(ub.size()>1){
                return getNextCleanerFree(roomID, sd);
            }else{
                for(userBookings x: ub){
                    if(x.getStartTime().isAfter(startTime)){
                        return x.getStartTime();
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Error: " + e);
        }
        return null;

    }

    //If there are no booking for the room in the day, then we will just get whenever the cleaner is next free
    private LocalTime getNextCleanerFree(int roomID, String startDate){
        String sql = "SELECT * FROM Bookings WHERE RoomID = ? AND UserID = ? AND StartDate = ? ORDER BY EndTime DESC";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ArrayList<userBookings> ub = new ArrayList<>();

            assert con != null;
            ps = con.prepareStatement(sql);
            ps.setInt(1, roomID);
            ps.setInt(2, 2);
            ps.setString(3, startDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                ub.add(new userBookings(rs.getInt(1), rs.getInt(2), LocalTime.parse(rs.getString(4)), LocalDate.parse(rs.getString(6))));
            }
            //Now we need to get the end time of the last cleaner slot.
            for(userBookings x: ub){
                return x.getStartTime().plus(30, ChronoUnit.MINUTES);
            }



        }catch(Exception e){
                System.out.println("Error: " + e);
            }
        return null;
    }

}
