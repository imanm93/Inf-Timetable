package selp.inf.timetable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class WelcomeScreen extends Activity implements OnTouchListener {

	public static final String TAG = WelcomeScreen.class.getSimpleName();
	
	DBHelper DbHelper;
	SQLiteDatabase db;
	
    @Override
	public void onBackPressed() {
    	finish();
    }

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        
        Log.d(TAG, "Started WelcomScreen Activity");
                        
        //Creates database the first time application is run
        DbHelper = new DBHelper(this);
        	
        //Runs AsyncTask
        new XMLParsing().execute();
        
        View fullScreen = (View) findViewById(R.id.screen);
        fullScreen.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				//Opens readable database
				DbHelper = new DBHelper(WelcomeScreen.this);
				db = DbHelper.getReadableDatabase();
				
				String query = "SELECT * FROM " + DBHelper.DB_SELECTEDCOURSE;
				Cursor selectedCoursesCount = db.rawQuery(query, null);
				
				Log.d(TAG, selectedCoursesCount.getCount() + "");
				
				if (selectedCoursesCount.getCount() > 0) {
					//Creates New Intent to go the Menu Screen Activity
					Intent menuScreen = new Intent(WelcomeScreen.this, MenuScreen.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(menuScreen);
				} else {
					//Creates New Intent to go the Filter Courses Activity
					Intent filterScreen = new Intent(WelcomeScreen.this, FilterCourses.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(filterScreen);
				}
				
				selectedCoursesCount.close();
				db.close();
				
				return false;
				
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_welcome_screen, menu);
        return true;
    }
    
    /*
     * Parses all three files and inserts data into the database tables
     *Only runs the first time the application is run
     */
    class XMLParsing extends AsyncTask<Void, Void, Void> {

    	HandlingXMLData hxd;
    	ProgressDialog loading;
    	
		@Override
		protected Void doInBackground(Void... params) {

			//Creates new instance to parse data
			hxd = new HandlingXMLData();
			
			//Calls get method to parse the files
			hxd.get(HandlingXMLData.coursesxml);
			hxd.get(HandlingXMLData.timexml);
			hxd.get(HandlingXMLData.venuesxml);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			//Getting writable database to put data into the database 
			db = DbHelper.getWritableDatabase();
			
			//Inserting items for courses.xml into database
			for (CourseItem itemCourse : hxd.itemsCourse) {
				
				Log.d(TAG, "Inserting Course Elements");
				
				ContentValues courseValues = new ContentValues();
				
				courseValues.put(DBHelper.KEY_COURSE_NAME, itemCourse.getName());
				courseValues.put(DBHelper.KEY_COURSE_ACRONYM, itemCourse.getCouresacronym());
				courseValues.put(DBHelper.KEY_COURSE_YEAR, itemCourse.getYear());
				courseValues.put(DBHelper.KEY_COURSE_LEVEL, itemCourse.getLevel());
				courseValues.put(DBHelper.KEY_COURSE_POINTS, itemCourse.getPoints());
				courseValues.put(DBHelper.KEY_COURSE_DELIVERYPERIOD, itemCourse.getDeliveryperiod());
				courseValues.put(DBHelper.KEY_COURSE_LECTURER, itemCourse.getLecturer());
				courseValues.put(DBHelper.KEY_COURSE_AI, itemCourse.getAi());
				courseValues.put(DBHelper.KEY_COURSE_CG, itemCourse.getCg());
				courseValues.put(DBHelper.KEY_COURSE_CS, itemCourse.getCs());
				courseValues.put(DBHelper.KEY_COURSE_SE, itemCourse.getSe());
				courseValues.put(DBHelper.KEY_COURSE_DRPS, itemCourse.getDrps());
				courseValues.put(DBHelper.KEY_COURSE_EUCLID, itemCourse.getEuclid());
				courseValues.put(DBHelper.KEY_COURSE_URL, itemCourse.getUrl());
				
				db.insert(DBHelper.DB_COURSE, null, courseValues);
				
				Log.d(TAG, "Inserted Course Values");
				
			}
			
			//Inserting items for timetable.xml into database
			for (TimeItem itemTime : hxd.itemsTime) {
				
				ContentValues timeValues = new ContentValues();
				
				timeValues.put(DBHelper.KEY_TIME_COURSEACRONYM, itemTime.getCourseacronym());
				timeValues.put(DBHelper.KEY_TIME_YEAR, itemTime.getYear());
				timeValues.put(DBHelper.KEY_TIME_SEMESTER, itemTime.getSemester());
				timeValues.put(DBHelper.KEY_TIME_DAY, itemTime.getDay());
				timeValues.put(DBHelper.KEY_TIME_ROOM, itemTime.getRoom());
				timeValues.put(DBHelper.KEY_TIME_BUILDING, itemTime.getBuilding());
				timeValues.put(DBHelper.KEY_TIME_TIMESTART, itemTime.getTimestart());
				timeValues.put(DBHelper.KEY_TIME_TIMEFINISH, itemTime.getTimefinish());
				timeValues.put(DBHelper.KEY_TIME_COMMENT, itemTime.getComment());
				
				db.insert(DBHelper.DB_TIME, null, timeValues);
				
			}
			
			//Inserting items for venues.xml into database
			for (VenueItem itemVenue : hxd.itemsVenue) {
				
				ContentValues venueValues = new ContentValues();
				
				venueValues.put(DBHelper.KEY_VENUE_CODE, itemVenue.getVenuename());
				venueValues.put(DBHelper.KEY_VENUE_DESCRIPTION, itemVenue.getDescription());
				venueValues.put(DBHelper.KEY_VENUE_MAP, itemVenue.getMap());
				
				db.insert(DBHelper.DB_VENUES, null, venueValues);
				
			}
			
			db.close();
			loading.dismiss();

			Log.d(TAG, "Parsing Completed");
			
		}

		@Override
		protected void onPreExecute() {

			Log.d(TAG, "Parseing Started");
			loading = ProgressDialog.show(WelcomeScreen.this, "Parsing", "Loading Data ...");
			
		}
    	
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		return false;
		
	}
    
}
