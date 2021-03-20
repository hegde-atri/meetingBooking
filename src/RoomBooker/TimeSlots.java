package RoomBooker;

import java.util.ArrayList;

public class TimeSlots {

    private String startTime;
    private String endTime;
    public ArrayList<TimeSlots> timeSlots = new ArrayList<>();

    public TimeSlots(String startTime, String endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public TimeSlots(){

    }

    public void createTimeSlots(){
        for(int x=0; x<22; x++){
            for(int y = 0; y <2; y++){
                if(x<10){
                    if(y%2 == 0){
                        if((x+1) == 10){
                            this.timeSlots.add(new TimeSlots("0"+x+":00", "0" + x +":30"));
                        }else{
                            this.timeSlots.add(new TimeSlots("0"+x+":00", "0"+x+":30"));
                        }

                    }else{
                        if((x+1) == 10){
                            this.timeSlots.add(new TimeSlots("0"+x+":30", "10:00"));
                        }else{
                            this.timeSlots.add(new TimeSlots("0"+x+":30", "0"+(x+1)+":00"));
                        }
                    }
                }else{
                    if(y%2 == 0){
                        this.timeSlots.add(new TimeSlots(x+":00", x+":30"));
                    }else{
                        this.timeSlots.add(new TimeSlots(x+":30", (x+1)+":00"));
                    }
                }
            }
        }
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return startTime+" to "+endTime;
    }
}
