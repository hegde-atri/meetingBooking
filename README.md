# Books meeting rooms
- [ ] Catering Dashboard.
- [ ] Cleaners Dashboard.
- [ ] Automatically schedules cleaners after the room has been vacated. 
- [ ] Automatically does not allow you to add refreshments at the same time as others.  
- [ ] Customer cannot book 'occupied' rooms and cannot book unless room has been serviced.
- [X] Customer Page verifies email format.  
- [ ] ~~Customer requests for room, and Admins approve the requests.~~  
- [ ] ~~Greyed out buttons when room is unavailable.~~  
- [X] Stylize UI


### The specifications for this program are as follows:
- There are 5 different rooms, all with different sizes of how many people the room can accommodate.

Room number | Accommodation size | Disabled Access
------------|---------------------|-----------------
Room 1 | 2 people | false
Room 2 | 4 people | false
Room 3 | 8 people | false
Room 4 | 15 people | true
Room 5 | 50 people | false
- Users of this system will be able to book a meeting room for a certain date and time slot.
  Two different users should not be able to book the same room at the same time and date slot.
  (no double bookings). You can choose how long the times slots are (people might/might not need the room for the whole day)
- Along with the meeting room, a user can request for a selection of resources to be in the room for their meeting (e.g a projector, pens, paper)
- The user can also request refreshments to be delivered to their booked room at certain times(e.g pastry, sandwich, water, drinks).
  You can book multiple refreshments for the same room at different times(e.g pastry in the morning, coffee in the evening)
- The catering staff should be able to see a list of which rooms need which refreshments in time of order of when they need to deliver
  the refreshments. The catering staff have only 1 person who can deliver, therefore they cannot deliver to 2 rooms at the same time.
-The rooms need to be cleaned between bookings. There is only 1 cleaner. You should be able to generate a cleaning schedule for them(each room takes about 30 minutes to clean).
### Additional requirements
- Users will have to use an email to register a booking. Verify that the email is given in the correct format.
- Show the user the list of available rooms/ available timeslots in sorted order.
- A user should not be able to book 2 meeting rooms for the same time and date slot.