package Customer;

public class CustomerBooking {
    private int roomID;
    private String timePeriod;
    private String dates;
    private int duration;
    private String equipment;
    private String refreshments;
    private String refreshmentsTime;

    public CustomerBooking(int roomID, String timePeriod, String dates, int duration, String equipment, String refreshments, String refreshmentsTime) {
        this.roomID = roomID;
        this.timePeriod = timePeriod;
        this.dates = dates;
        this.duration = duration;
        this.equipment = equipment;
        this.refreshments = refreshments;
        this.refreshmentsTime = refreshmentsTime;
    }

    //<editor-fold desc="set-get">
    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

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
    //</editor-fold>
}
