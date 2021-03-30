package RoomBooker;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TimeSlot {

    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlot(String startTime, String endTime){
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
    }

    //Although this method is not is use a the moment, it will be useful if i want to display the available time slots instead of the booked timeslots in RoomBooker
    public static ArrayList<TimeSlot> createTimeSlots()
    {
        ArrayList<TimeSlot> tempArr = new ArrayList<>();

        for(int x=0; x<22; x++){
            for(int y = 0; y <2; y++){
                if(x<10){
                    if(y%2 == 0){
                        if((x+1) == 10){
                            tempArr.add(new TimeSlot("0"+x+":00", "0" + x +":30"));
                        }else{
                            tempArr.add(new TimeSlot("0"+x+":00", "0"+x+":30"));
                        }

                    }else{
                        if((x+1) == 10){
                            tempArr.add(new TimeSlot("0"+x+":30", "10:00"));
                        }else{
                            tempArr.add(new TimeSlot("0"+x+":30", "0"+(x+1)+":00"));
                        }
                    }
                }else{
                    if(y%2 == 0){
                        tempArr.add(new TimeSlot(x+":00", x+":30"));
                    }else{
                        tempArr.add(new TimeSlot(x+":30", (x+1)+":00"));
                    }
                }
            }
        }

        return tempArr;
    }

    //This will create int slot number of 30 min timeslots from startTime
    public static ArrayList<TimeSlot> returnTimeSlots(LocalTime startTime, int slots)
    {
        ArrayList<TimeSlot> temp = new ArrayList<>();
        LocalTime endTime;
        for(int x = 0; x<slots; x++){
            endTime = startTime.plus(30, ChronoUnit.MINUTES);
            temp.add(new TimeSlot(startTime.toString(), endTime.toString()));
            startTime = startTime.plus(30, ChronoUnit.MINUTES);
        }
        return temp;
    }

    //This method will return the number of 30 minute slots between the given st and et
    public static int getSlotNumber(LocalTime st, LocalTime et)
    {
        long timeDifference = st.until(et, ChronoUnit.MINUTES);
        return (int)timeDifference/30 ;
    }

    //This method will compare the given timeslot object to every object in given arrayList
    public static boolean listContains(TimeSlot ts, ArrayList<TimeSlot> list){
        for(TimeSlot x: list){
            if(x.exists(ts)){
                return true;
            }
        }
        return false;
    }

    //This will compare the current timeslot and object to ts and check whether both objects hold the same values.
    public boolean exists(TimeSlot ts)
    {
        return (startTime.equals(ts.getStartTime())) && (endTime.equals(ts.getEndTime()));
    }

    //<editor-fold desc="get-set and toString method">
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

    @Override
    public String toString() {
        return startTime+" to "+endTime;
    }
    //</editor-fold>

}
