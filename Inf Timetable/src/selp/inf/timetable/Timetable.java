package selp.inf.timetable;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class Timetable extends Activity {

	public static final String TAG = Timetable.class.getSimpleName(); 
	
	DBHelper DbHelper;
	SQLiteDatabase db;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        Log.d(TAG, "Started Timetable Acticity");
        
        //Getting Intent extras
        Bundle Extras = getIntent().getExtras();      
		
        ArrayList<String> coursesTimetable = Extras.getStringArrayList("Selected Courses");
        
     	//Checking for duplicates in the array 
        for (int check = 0; check < coursesTimetable.size(); check++) {
        	for (int check1 = 0; check1 < coursesTimetable.size(); check1++) {  		
        		if (!(check == check1)) {
        			if (coursesTimetable.get(check).equals(coursesTimetable.get(check1))) {
        				coursesTimetable.remove(check1);
        			}
        		}
        	}
        }
        
        
        //Finds the list view to display the timetable
        ListView timetableView = (ListView) findViewById(R.id.timetable);    
        List<TimetableDisplayItem> TimetableList = new ArrayList<TimetableDisplayItem>();
        
        //Opens readable databse to extract the selected courses from it
        DbHelper = new DBHelper(this);
        db = DbHelper.getReadableDatabase();
        
		String[] Day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
		String[] TimeStart = {"09:00", "10:00", "11:10", "12:10", "14:10", "15:10", "16:10", "17:10"};
		String[] TimeFinish = {"09:50", "10:50", "12:00", "13:00", "15:00", "16:00", "17:00", "18:00"};
		
		//Checks which slot the course is to be displayed in the timetable
		for(int day = 0; day < 5; day++) {
								
			for (int time = 0; time < 8; time++) {
										
				for (int course = 0; course < coursesTimetable.size(); course++) {
										
					String CourseName = coursesTimetable.get(course).toString();
										
					String query = "SELECT * FROM " + DBHelper.DB_COURSE + " C INNER JOIN " + DBHelper.DB_TIME + " T ON T.Time_COURSEACRONYM = C.Course_ACRONYM WHERE T.Time_DAY = '" + Day[day] + "' AND T.Time_TIMESTART = '" + TimeStart[time] + "' AND T.Time_TIMEFINISH = '" + TimeFinish[time] + "' AND C.Course_NAME = '" + CourseName + "'";
					Cursor cursor = db.rawQuery(query, null); 
															
					Log.d(TAG, "Count: " + cursor.getCount());
					
					//Checking if course is scheduled at this time
					
					if (cursor.getCount() > 0) {
					
						cursor.moveToFirst();
						
						while (cursor.isAfterLast() == false) {
						
						Log.d(TAG, "Cursor at: " + cursor.getPosition());	
							
						// Adding timetable item to be displayed	
							
						TimetableDisplayItem timetableItem = new TimetableDisplayItem();
						
						timetableItem.setCourseACRONYM(cursor.getString(2));
						timetableItem.setDay(cursor.getString(19));
						timetableItem.setTimeSTART(cursor.getString(22));
						timetableItem.setTimeFINISH(cursor.getString(23));
						timetableItem.setTimeBUILDING(cursor.getString(20));
						
						//Checks if there is comment for a course
						if (!(cursor.getString(24).equals(""))) {
							timetableItem.setComment(cursor.getString(24)); //Gets the comment if it is not empty
						} else {
							timetableItem.setComment("None"); //Shows none when there is no comment
						}
						
						String mapQuery = "SELECT * FROM " + DBHelper.DB_VENUES + " WHERE Venue_CODE = '" + cursor.getString(20) + "'"; 
						String roomDescriptionQuery = "SELECT * FROM " + DBHelper.DB_VENUES + " WHERE Venue_CODE = '" + cursor.getString(21) + "'";
						
						Cursor map = db.rawQuery(mapQuery, null);
						Cursor roomDescription = db.rawQuery(roomDescriptionQuery, null);
						
						Log.d(TAG, "Map cursor count: " + map.getCount());
						Log.d(TAG, "Room Description cursor count: " + roomDescription.getCount());
						
						map.moveToFirst();
						
						timetableItem.setVenueDESCRIPTION(map.getString(2));
						timetableItem.setVenueMAP(map.getString(3));
						
						//Checks if there is a description for the room, then display the description
						//Otherwise just displays the room code
						if (roomDescription.getCount() > 0) {
							roomDescription.moveToFirst();
							timetableItem.setTimeROOM(roomDescription.getString(2));
						} else {
							timetableItem.setTimeROOM("Room: " + cursor.getString(21));
						}
												
						Log.d(TAG, "Map and Address for " + cursor.getString(2) + ": " + map.getString(2) + " " + map.getString(3));
						
						TimetableList.add(timetableItem);
						
						cursor.moveToNext();
						
						}
					
					}
				
					cursor.close();
				
				}	
			}
		}
		
		Log.d(TAG, "No. of Selected Courses being saved: " + coursesTimetable.size());
		
		for (int course = 0; course < coursesTimetable.size(); course++) {
						
			String CourseName = coursesTimetable.get(course).toString();
			String query = "SELECT * FROM " + DBHelper.DB_COURSE + " WHERE Course_NAME = '" + CourseName + "'";
			Cursor cursor = db.rawQuery(query, null); 
			
			if (cursor.getCount() > 0) {
			
				cursor.moveToFirst();
				
				// Putting selected course names into selectedCourses table
				ContentValues courseSelected = new ContentValues();
				courseSelected.put(DBHelper.KEY_SELECTEDCOURSES_COURSENAME, cursor.getString(1));
		
				db.insert(DBHelper.DB_SELECTEDCOURSE, null, courseSelected);
				Log.d(TAG, "Inserted value: " + cursor.getString(1));
			}
			
			cursor.close();
			
		}
		
		db.close();
		
		CustomTimetableViewAdapter adapter = new CustomTimetableViewAdapter(this, TimetableList);
		timetableView.setAdapter(adapter);
		
		Log.d(TAG, "Timetable List Populated");
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_timetable, menu);
        return true;
    }
}
