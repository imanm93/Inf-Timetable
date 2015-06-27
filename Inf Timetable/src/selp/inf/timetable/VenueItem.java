package selp.inf.timetable;

public class VenueItem {

	//Variable to be retrieved from venues.xml
	private String venuename;
	private String description;
	private String map;
		
	public String getVenuename() {
		return venuename;
	}
	public void setVenuename(String venuename) {
		this.venuename = venuename;
	}	
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		this.map = map;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
