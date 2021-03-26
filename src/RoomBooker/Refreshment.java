package RoomBooker;

import java.time.LocalDate;
import java.time.LocalTime;

public class Refreshment {
    private int RoomID;
    private LocalDate date;
    private LocalTime time;
    private String refreshment;

    public Refreshment(int roomID, LocalDate date, LocalTime time, String refreshment) {
        RoomID = roomID;
        this.date = date;
        this.time = time;
        this.refreshment = refreshment;
    }

    public Refreshment(int roomID, String date, String time, String refreshment) {
        RoomID = roomID;
        this.date = LocalDate.parse(date);
        this.time = LocalTime.parse(time);
        this.refreshment = refreshment;
    }

    //<editor-fold desc="set-get">
    public int getRoomID() {
        return RoomID;
    }

    public void setRoomID(int roomID) {
        RoomID = roomID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getRefreshment() {
        return refreshment;
    }

    public void setRefreshment(String refreshment) {
        this.refreshment = refreshment;
    }
    //</editor-fold>
}
