package unsw.venues;
//import java.util.ArrayList;
//import java.util.List;
import java.util.*;
//import org.json.JSONArray;
import org.json.JSONObject;

public class Venue {
	private String venue;
	List<Room> roomList = new ArrayList<Room>();

	
	/**
	 * constructor
	 * @param venue
	 */
	
	public Venue(String venue) {
		this.venue = venue;
	}
	
	public String getVenue() {
		return this.venue;
	}
	
	public void setVenue(String venue) {
		this.venue = venue;
	}
	
	/**
	 * toJSON
	 * @return JSONObject for object instance of this class
	 */
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("venue", venue);		
		return jo;
	}
}