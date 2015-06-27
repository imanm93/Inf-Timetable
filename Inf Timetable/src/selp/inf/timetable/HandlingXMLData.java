package selp.inf.timetable;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class HandlingXMLData extends DefaultHandler {

	public static final String TAG = HandlingXMLData.class.getSimpleName();
	
	// Web sites to be parsed
	public static final String coursesxml = "http://www.inf.ed.ac.uk/teaching/courses/selp/xml/courses.xml";
	public static final String timexml = "http://www.inf.ed.ac.uk/teaching/courses/selp/xml/timetable.xml";
	public static final String venuesxml = "http://www.inf.ed.ac.uk/teaching/courses/selp/xml/venues.xml";
	 
	boolean currTag = false;
	String currTagVal = "";
	
	//Objects that the web sites is going to be parsed into
	public CourseItem itemCourse = null;
	public TimeItem itemTime = null;
	public VenueItem itemVenue = null;
	
	//Lists containing all parsed information from all three files
	public ArrayList<CourseItem> itemsCourse = new ArrayList<CourseItem>();
	public ArrayList<TimeItem> itemsTime = new ArrayList<TimeItem>();
	public ArrayList<VenueItem> itemsVenue = new ArrayList<VenueItem>();
	
	public String tempDAY = "";
	public String tempSemester = "";
	public String tempStartTime = "";
	public String tempFinishTime = "";
	
	// Chooses which xml to be parsed
	public int XMLChooser;
	
	public void get(String xml) {

		if (xml == coursesxml) {
			XMLChooser = 1;
		}else if (xml == timexml) {
			XMLChooser = 2;
		}else if (xml == venuesxml) {
			XMLChooser = 3;
		}
		
		try {
			
			//Creating Parser
			
			SAXParserFactory pxf = SAXParserFactory.newInstance();
			SAXParser px = pxf.newSAXParser();
			XMLReader xmr = px.getXMLReader();
			xmr.setContentHandler(this);
			
			//Opening Stream
			
			InputStream courseStream = new URL(xml).openStream();			
			xmr.parse(new InputSource(courseStream));
			
		}catch(Exception e) {
			
			Log.e("Error", e.getMessage());
			Log.d("Parser", "Failed Course");
			
		}
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		
		if (currTag) {
			currTagVal = currTagVal + new String(ch, start, length);
			currTag = false;
		}
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currTag = false;
		
		// Retrieving attributes from XML and putting it into an ArrayList		
		if (XMLChooser == 1) { 								// courses.xml
			if (localName.equals("name")) {
				Log.d(TAG, "Reached");
				itemCourse.setName(currTagVal);
			} else if (localName.equalsIgnoreCase("level")) {
				itemCourse.setLevel(currTagVal);
			} else if (localName.equalsIgnoreCase("points")) {
				itemCourse.setPoints(currTagVal);
			} else if (localName.equalsIgnoreCase("year")) {
				itemCourse.setYear(currTagVal);
			} else if (localName.equalsIgnoreCase("acronym")) {
				itemCourse.setCouresacronym(currTagVal);
			} else if (localName.equalsIgnoreCase("ai")) {
				if (currTagVal == null) {
					itemCourse.setAi("");
				} else {
					itemCourse.setAi(currTagVal);
				}
			} else if (localName.equalsIgnoreCase("cg")) {
				if (currTagVal == null) {
					itemCourse.setCg("");
				} else {
					itemCourse.setCg(currTagVal);
				}
			} else if (localName.equalsIgnoreCase("cs")) {
				if (currTagVal == null) {
					itemCourse.setCs("");
				} else {
					itemCourse.setCs(currTagVal);
				}
			} else if (localName.equalsIgnoreCase("se")) {
				if (currTagVal == null) {
					itemCourse.setSe("");
				} else {
					itemCourse.setSe(currTagVal);
				}
			} else if (localName.equalsIgnoreCase("drps")) {
				itemCourse.setDrps(currTagVal);
			} else if (localName.equalsIgnoreCase("euclid")) {
				itemCourse.setEuclid(currTagVal);
			} else if (localName.equalsIgnoreCase("url")) {
				itemCourse.setUrl(currTagVal);
			} else if (localName.equalsIgnoreCase("deliveryperiod")){
				itemCourse.setDeliveryperiod(currTagVal);
			} else if (localName.equalsIgnoreCase("lecturer")) {
				itemCourse.setLecturer(currTagVal);
			} else if (localName.equalsIgnoreCase("course")) {
				itemsCourse.add(itemCourse);
				Log.d(TAG, "Added Item");
			}
		} else if (XMLChooser == 2) { 						// timetable.xml
			if (localName.equalsIgnoreCase("course")) {
				itemTime.setCourseacronym(currTagVal);
			} else if (localName.equalsIgnoreCase("year")) {
				if (itemTime.getYear().equals("")) {
					itemTime.setYear(currTagVal);
				} else {
					itemTime.setYear(itemTime.getYear() + ", " + currTagVal);
				}
			} else if (localName.equalsIgnoreCase("room")) {
				itemTime.setRoom(currTagVal);
			} else if (localName.equalsIgnoreCase("building")) {
				itemTime.setBuilding(currTagVal);
			} else if (localName.equalsIgnoreCase("comment")) {
				if (currTagVal==null){
					itemTime.setComment("");
				}
				else {
					itemTime.setComment(currTagVal);
				}	
			} else if (localName.equalsIgnoreCase("lecture")) {
				itemsTime.add(itemTime);
			}	
		} else if (XMLChooser == 3) {
			if (localName.equalsIgnoreCase("name")) { 			// venues.xml
				itemVenue.setVenuename(currTagVal);
			} else if (localName.equalsIgnoreCase("description")) {
				itemVenue.setDescription(currTagVal);
			} else if (localName.equalsIgnoreCase("map")) {
				itemVenue.setMap(currTagVal);
			} else if (localName.equalsIgnoreCase("building")) {
				itemsVenue.add(itemVenue);
			} else if (localName.equalsIgnoreCase("room")) {
				itemsVenue.add(itemVenue);
			}
		}
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			org.xml.sax.Attributes attributes) throws SAXException {

		currTag = true;
		currTagVal = "";

		if (XMLChooser == 1) {
			if (localName.equals("course")) {
				itemCourse = new CourseItem();
			}
		} else if (XMLChooser == 2) {
			if (localName.equals("semester")) {
				tempSemester = "S" + attributes.getValue("number");
			}
			
			if (localName.equals("day")) {
				tempDAY = attributes.getValue("name");				
			}
			
			if (localName.equals("time")) {
				tempStartTime = attributes.getValue("start");
				tempFinishTime = attributes.getValue("finish");
			}
			
			if (localName.equals("lecture")) {
			
				itemTime = new TimeItem();
				itemTime.setYear("");
				itemTime.setTimestart(tempStartTime);
				itemTime.setTimefinish(tempFinishTime);
				itemTime.setDay(tempDAY);
				itemTime.setSemester(tempSemester);
			
			}
		} else if (XMLChooser == 3) {
			if (localName.equals("building")) {
				itemVenue = new VenueItem();
			}
			if (localName.equals("room")) {
				itemVenue = new VenueItem();
				itemVenue.setMap("");
			}
		}
	}
	
}
