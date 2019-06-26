package unsw.venues;

import java.time.LocalDate;
import java.util.*;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Venue Hire System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a venue hire system. Input
 * and output is in JSON format.
 *
 * @author William Lin
 *
 */
public class VenueHireSystem {
	
	List<Venue> venueList = new ArrayList<Venue>();
	
	
    /**
     * Constructs a venue hire system. Initially, the system contains no venues,
     * rooms, or bookings.
     */
    public VenueHireSystem() {
    	//Constructor stub
    }
    /**
     * processCommand  determines which command is read
     * 
     * @param json
     */

    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {

        case "room":

            String venue = json.getString("venue");
            String room = json.getString("room");
            String size = json.getString("size");
            addRoom(venue, room, size);
            break;

        case "request":
            String id = json.getString("id");
            LocalDate start = LocalDate.parse(json.getString("start"));
            LocalDate end = LocalDate.parse(json.getString("end"));
            int small = json.getInt("small");
            int medium = json.getInt("medium");
            int large = json.getInt("large");

            JSONObject result = request(id, start, end, small, medium, large);

            System.out.println(result);
            break;

        case "change":
    		String changeId = json.getString("id");
    		LocalDate changeSt = LocalDate.parse(json.getString("start"));
    		LocalDate changeEnd = LocalDate.parse(json.getString("end"));
    		int changeSmall = json.getInt("small");
    		int changeMed = json.getInt("medium");
    		int changeLar = json.getInt("large");
    		
    		JSONObject changing = change(changeId, changeSt, changeEnd, changeSmall, changeMed, changeLar);
    		System.out.println(changing);
        	break; 
            
        case "cancel":
        	String cancelId = json.getString("id");
        	
        	JSONObject cancelResult = cancelRoom(cancelId);
        	System.out.println(cancelResult);
        	
        	break;
        	
        case "list":
        	String listVenue = json.getString("venue"); 
        	
        	JSONArray listResult = list(listVenue);
        	
        	System.out.println(listResult.toString(2));
        	break;
       
        }
    }

    /** 
     * addRoom creates a room and adds to appropiate venue
     * @param venue
     * @param room
     * @param size
     * 
     */
    private void addRoom(String venue, String room, String size) {
    	Room temp = new Room(room,size);
    	//If Venue exists add to venue
    	for (int i = 0; i < venueList.size(); i ++) {
    		if (venueList.get(i).getVenue().equals(venue)) {		
    	    	venueList.get(i).roomList.add(temp);
    	    	return;
    		}
    	}
    	
    	//otherwise create venue and add to venue
    	Venue newVenue = new Venue(venue); 	
    	newVenue.roomList.add(temp);
    	venueList.add(newVenue);
    	return;
    }

/**
 * request  
 * @param id
 * @param start
 * @param end
 * @param small
 * @param medium
 * @param large
 * @return JSON Object of reservation
 */
    public JSONObject request(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {
        
    	JSONObject result = new JSONObject();

    	for (int i = 0; i < venueList.size(); i++) {
    		//Check all small rooms, medium room and large rooms and gauge total number
    		List<Room> smallList = canFit(small, "small", venueList.get(i).roomList, start, end);
    		List<Room> mediumList = canFit(medium, "medium", venueList.get(i).roomList, start, end);
    		List<Room> largeList = canFit(large, "large", venueList.get(i).roomList, start, end);

    		//if the venue cant fit reservation
    		if ((small > 0 && smallList.size() < small) 
    				|| (medium > 0 && mediumList.size() < medium)
    				|| (large > 0 && largeList.size() < large)) {

    		}
    		
    		//else book reservation copying request method
    		else {
    			result.put("status", "success");
    			result.put("venue", venueList.get(i).getVenue());			
    			JSONArray rooms = new JSONArray();
    			Reservation R = new Reservation(id, start, end);
    			
    			
    			for (int j = 0; j < small; j++) {	
    				smallList.get(j).reservationList.add(R);
    				rooms.put(smallList.get(j).getRoom());
    			}
    			
    			for (int j = 0; j < medium; j++) {
    				mediumList.get(j).reservationList.add(R);
    				rooms.put(mediumList.get(j).getRoom());
    			}
    			
    			for (int j = 0; j < large; j++) {
    				largeList.get(j).reservationList.add(R);
    				rooms.put(largeList.get(j).getRoom());
    			}
    			
    			result.put("rooms", rooms);
    			return result;
    		}
    	}
  	
    	//If was not successful return rejected
    	result.put("status", "rejected");
    	
        return result;
    }

    /**
     * Helper function for request and change
     * @param num
     * @param size
     * @param venueRooms
     * @param start
     * @param end
     * @return List of rooms which are unoccupied on a date
     */
    public List<Room> canFit(int num, String size, 
    		List<Room> venueRooms, LocalDate start, LocalDate end) {
		List<Room> Good = new ArrayList<Room>();
		for (int i = 0; i < venueRooms.size() && Good.size() < num; i++) {
			
			if (venueRooms.get(i).getSize().equals(size)) {				
				boolean occupied = false;
				
				for (int j = 0; j < venueRooms.get(i).reservationList.size() && occupied == false; j++) {
					
					LocalDate thisStart = venueRooms.get(i).reservationList.get(j).getStart();
					LocalDate thisEnd = venueRooms.get(i).reservationList.get(j).getEnd();
					
					if (thisStart.compareTo(start) >= 0 && thisStart.compareTo(end) <= 0) {
						occupied = true;
					}
					else if (thisEnd.compareTo(start) >= 0 && thisEnd.compareTo(end) <= 0) {
						occupied = true;
					}
					else if (thisStart.compareTo(start) <= 0 && thisEnd.compareTo(end) >= 0) {
						occupied = true;
					}
				}
				
				if (occupied == false) {			
					Good.add(venueRooms.get(i));	
				}
			}
		}	
		return Good;
	}
    
    /**
     * changeRooms
     * @param id
     * @param start
     * @param end
     * @param small
     * @param medium
     * @param large
     * @return JSON Object of changed reservation
     */
    public JSONObject changeRoom(String id, LocalDate start, LocalDate end, 
    		int small, int medium, int large) {
    	JSONObject result = new JSONObject();
    	
    	return result;
    }
    public JSONObject change(String id, LocalDate start, LocalDate end, int small, 
    		int medium, int large)
    {
    	JSONObject result = new JSONObject();
    	//Back up of old list incase change fails
    	List<Venue> tempVenueList = venueList;
    	
    	//loop through all possible reservations and cancel any matching ID
	    for (int i = 0; i < venueList.size(); i ++) {
	    	Venue tempV = venueList.get(i);
	    		
	    	for (int j = 0; j < tempV.roomList.size(); j ++) {
	    		Room tempR = tempV.roomList.get(j);
	    			
	    		for (int k = 0; k < tempR.reservationList.size(); k ++) {
	    			Reservation tempRes = tempR.reservationList.get(k); 
	    			
					if (tempRes.getId().equals(id)) {
						tempR.reservationList.remove(k);
					}
				}
			}
		}
    	
    	
    	// Rebook reservation
    	for (int i = 0; i < venueList.size(); i++) {

    		List<Room> smallList = canFit(small, "small", venueList.get(i).roomList, start, end);
    		List<Room> mediumList = canFit(medium, "medium", venueList.get(i).roomList, start, end);
    		List<Room> largeList = canFit(large, "large", venueList.get(i).roomList, start, end);
    	
    		if ((small > 0 && smallList.size() < small) 
    				|| (medium > 0 && mediumList.size() < medium) || (large > 0 && largeList.size() < large)) {
	
    		} else {
    			
    			result.put("status", "success");
    			result.put("venue", venueList.get(i).getVenue());
    			
				Reservation R = new Reservation(id, start, end);
				JSONArray rooms = new JSONArray();
    			for (int j = 0; j < small; j++) {
    				smallList.get(j).reservationList.add(R);
    				rooms.put(smallList.get(j).getRoom());
    			}
    			for (int j = 0; j < medium; j++) {
    				mediumList.get(j).reservationList.add(R);
    				rooms.put(mediumList.get(j).getRoom());
    			}
    			for (int j = 0; j < large; j++) {
    				largeList.get(j).reservationList.add(R);
    				rooms.put(largeList.get(j).getRoom());
    			}
    			result.put("rooms", rooms);
    			return result;
    		}
    	}
		
    	//if rejected restore previous reservation and output rejected
    	venueList = tempVenueList;
    	result.put("status", "rejected");
    	
    	
    	return result;
    }
    
    /**
     * cancelRoom
     * @param id
     * @return status of cancellation
     */
    public JSONObject cancelRoom(String id) {
    	JSONObject status = new JSONObject();
    	String stat = "rejected"; //status is initially rejected if successful switch status to success
    	
    	//Loop through all Venues
    	for (int i = 0; i < venueList.size(); i ++) {
    		Venue tempV = venueList.get(i);
    		
    		//Loop through all rooms in a venue
    		for (int j = 0; j < tempV.roomList.size(); j ++) {
    			Room tempR = tempV.roomList.get(j);
    			
    			//Loop through all reservations in a room and match id, if matches remove
    			for (int k = 0; k < tempR.reservationList.size(); k ++) {
    				Reservation tempRes = tempR.reservationList.get(k); 
    				if (tempRes.getId().equals(id)) {
    					tempR.reservationList.remove(k);
    					stat = "success";
    				}
    			} 			
    		}
    	}
    	status.put("status", stat);
    	return status;
    }	 

    /**
     * 
     * @param venue
     * @return JSONArray of all reservation for a venue
     */
    public JSONArray list(String venue) {
    	JSONArray result = new JSONArray();
    	
    	//loop through all venue lists to check each venue if venues match up do
    	for (int i = 0; i < venueList.size(); i++) {
    		
    		if (venueList.get(i).getVenue().equals(venue)) {
    			Venue tempV = venueList.get(i);		
    			
    			for (int j = 0; j < tempV.roomList.size(); j++) {
    				
    				Room tempR = tempV.roomList.get(j);
    				result.put(tempR.toJSON()); 		
    			}  			
    		}
    	} 	
    	
    	return result;
    }
   
    /**
     * Main
     * @param args
     */
    public static void main(String[] args) {
        VenueHireSystem system = new VenueHireSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }

}
