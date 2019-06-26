package unsw.venues;

import java.time.LocalDate;
import org.json.JSONObject;

class Reservation {
	private String id;
	private LocalDate start;
	private LocalDate end;
	
	
	/**
	 * Constructor
	 * @param id
	 * @param start
	 * @param end
	 */
	public Reservation (String id, LocalDate start, LocalDate end) {
		this.id = id;
		this.start = start;
		this.end = end;	
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	/**
	 * toJSON
	 * @return JSONObject for object instance of this class
	 */
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("id", this.id);
		jo.put("start", this.start);
		jo.put("end", this.end);
		return jo;
	}


}