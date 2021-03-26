package Admin;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class userBookings {
    private int roomID;
    private int userID;
    private String username;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private String resources;
    private String refreshments;
    private String refreshmentsTime;

    public userBookings(int roomID, int userID, String username, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, String resources) {
        this.roomID = roomID;
        this.userID = userID;
        this.username = username;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resources = resources;
    }

    public userBookings(int roomID, int userID, String username, LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, String resources, String refreshments, String refreshmentsTime) {
        this.roomID = roomID;
        this.userID = userID;
        this.username = username;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resources = resources;
        this.refreshments = refreshments;
        this.refreshmentsTime = refreshmentsTime;
    }

    public int getTodayDuration(){
        LocalDate today = LocalDate.now();
        if(this.startDate.equals(today)){
            return getDuration();
        }
        return 0;
    }
    public int getDuration(){
        if(this.getStartDate().equals(this.getEndDate())){
            return (int) Duration.between(this.getStartTime(), this.getEndTime()).toMinutes();
        }else{
            //If the booking is over many days, we need to see how many hours they have booked for total, maximum booking time allowed is 24 hours, so they cannot pass 2 days.
            LocalTime end = LocalTime.of(0, 0, 0);
            int endDuration = (int)Duration.between(end, this.getEndTime()).toMinutes();
            int startDuration = (int)Duration.between(this.getStartTime(), end).toMinutes();
            return startDuration + endDuration;
        }
    }

    //<editor-fold desc="set-get">
    public String getRefreshments() {
        return refreshments;
    }

    public void setRefreshments(String refreshments) {
        this.refreshments = refreshments;
    }

    public String getRefreshmentsTime() {
        return refreshmentsTime;
    }

    public void setRefreshmentsTime(String refreshmentsTime) {
        this.refreshmentsTime = refreshmentsTime;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }
    //</editor-fold>
}
