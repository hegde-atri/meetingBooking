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

    public static ArrayList<TimeSlot> createTimeSlots(){
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

    public static ArrayList<TimeSlot> returnTimeSlots(LocalTime startTime, LocalTime endTime){
        ArrayList<TimeSlot> temp = new ArrayList<>();

        long timeDifference = startTime.until(endTime, ChronoUnit.MINUTES);
        long slots = timeDifference / 30;
        for(int x = 0; x<slots; x++){
            endTime = startTime.plus(30, ChronoUnit.MINUTES);
            temp.add(new TimeSlot(startTime.toString(), endTime.toString()));
            startTime = startTime.plus(30, ChronoUnit.MINUTES);
        }


        return temp;
    }

    public boolean exists(TimeSlot ts)
    {
        if ((startTime.equals(ts.getStartTime())) && (endTime.equals(ts.getEndTime()))){
            return true;
        }
        return false;
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

    @Override
    public String toString() {
        return startTime+" to "+endTime;
    }
}
