package unsw.venues;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Room {
	private String room;
	private String size;
	List<Reservation> reservationList = new ArrayList<Reservation>();

	
	/**
	 * Constructor
	 * @param room
	 * @param size
	 */
	public Room(String room, String size) {
		this.room = room;
		this.size = size;

	}
	
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	
	
	/**
	 * toJArray
	 * @return JSONArray of all reservations for that room
	 */
	//convert Reservation List to JArray containing JObjects for each reservation
	public JSONArray toJArray() {
		JSONArray reservations = new JSONArray();
		for (int i = 0; i < this.reservationList.size(); i ++) {
			reservations.put(this.reservationList.get(i).toJSON());
		}
		
		return reservations;
	}
	
	/**
	 * toJSON
	 * @return JSONObject for object instance of this class
	 */
	
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		JSONArray JA = this.toJArray();
		jo.put("reservations", JA);
		jo.put("room", this.room);

		return jo;
	}

	public void addReservation(Reservation Res)
	{
		reservationList.add(Res);
	}
	
	public void deleteReservation(Reservation Res)
	{
		reservationList.remove(Res);
	}
	
} 
