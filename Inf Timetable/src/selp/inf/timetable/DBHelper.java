package selp.inf.timetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String TAG = DBHelper.class.getSimpleName();
	
	// Database and table names
	public static final String DB_NAME = "timetable.db";
	public static final String DB_COURSE = "courses";
	public static final String DB_TIME = "time";
	public static final String DB_VENUES = "venues";
	public static final String DB_SELECTEDCOURSE = "selectedCourses";
	
	//Database table field names for courses table
	public static final String KEY_COURSE_ROWID = BaseColumns._ID;
	public static final String KEY_COURSE_NAME = "Course_NAME";
	public static final String KEY_COURSE_ACRONYM = "Course_ACRONYM";
	public static final String KEY_COURSE_YEAR = "Course_YEAR";
	public static final String KEY_COURSE_LEVEL = "Course_LEVEL";
	public static final String KEY_COURSE_POINTS = "Course_POINTS";
	public static final String KEY_COURSE_DELIVERYPERIOD = "Course_DP";
	public static final String KEY_COURSE_LECTURER = "Course_LECTURER";
	public static final String KEY_COURSE_DRPS = "Course_DPRS";
	public static final String KEY_COURSE_URL = "Course_URL";
	public static final String KEY_COURSE_EUCLID = "Course_EUCLID";
	public static final String KEY_COURSE_AI = "Course_AI";
	public static final String KEY_COURSE_CG = "Course_CG";
	public static final String KEY_COURSE_CS = "Course_CS";
	public static final String KEY_COURSE_SE = "Course_SE";
	
	//Database table field names for time table
	public static final String KEY_TIME_ROWID = BaseColumns._ID;
	public static final String KEY_TIME_COURSEACRONYM = "Time_COURSEACRONYM";
	public static final String KEY_TIME_YEAR = "Time_YEAR";
	public static final String KEY_TIME_SEMESTER = "Time_SEMESTER";
	public static final String KEY_TIME_DAY = "Time_DAY";
	public static final String KEY_TIME_BUILDING = "Time_BUILDING";
	public static final String KEY_TIME_ROOM = "Time_ROOM";
	public static final String KEY_TIME_TIMESTART = "Time_TIMESTART";
	public static final String KEY_TIME_TIMEFINISH = "Time_TIMEFINISH";
	public static final String KEY_TIME_COMMENT = "Time_COMMENT";
	
	//Database table field names for venues table
	public static final String KEY_VENUE_ROWID = BaseColumns._ID;
	public static final String KEY_VENUE_CODE = "Venue_CODE";
	public static final String KEY_VENUE_DESCRIPTION = "Venue_DESCRIPTION";
	public static final String KEY_VENUE_MAP = "Venue_MAP";
	
	//Database table field names for selected courses table
	public static final String KEY_SELECTEDCOURSES_ROWID = BaseColumns._ID;
	public static final String KEY_SELECTEDCOURSES_COURSENAME = "SelectedCourses_COURSENAME";
	
	//Database Version
	public static final int DB_VERSION = 1;		
	
	//Create table queries
	public static final String createTableSQL1 = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", 
			DB_COURSE, KEY_COURSE_ROWID, KEY_COURSE_NAME, KEY_COURSE_ACRONYM, KEY_COURSE_DELIVERYPERIOD, KEY_COURSE_LECTURER, KEY_COURSE_YEAR, KEY_COURSE_LEVEL, KEY_COURSE_POINTS, KEY_COURSE_URL, KEY_COURSE_DRPS, KEY_COURSE_EUCLID, KEY_COURSE_AI, KEY_COURSE_CG, KEY_COURSE_CS, KEY_COURSE_SE);
	public static final String createTableSQL2 = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", 
			DB_TIME, KEY_TIME_ROWID, KEY_TIME_COURSEACRONYM, KEY_TIME_YEAR, KEY_TIME_SEMESTER, KEY_TIME_DAY, KEY_TIME_BUILDING, KEY_TIME_ROOM, KEY_TIME_TIMESTART, KEY_TIME_TIMEFINISH, KEY_TIME_COMMENT);
	public static final String createTableSQL3 = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT)", 
			DB_VENUES, KEY_VENUE_ROWID, KEY_VENUE_CODE, KEY_VENUE_DESCRIPTION, KEY_VENUE_MAP);
	public static final String createtableSQL4 = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT)", 
			DB_SELECTEDCOURSE, KEY_SELECTEDCOURSES_ROWID, KEY_SELECTEDCOURSES_COURSENAME);
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Log.d(TAG, "Creating Tables");
		
		// Creates tables for course, time and venues
		db.execSQL(createTableSQL1);
		db.execSQL(createTableSQL2);
		db.execSQL(createTableSQL3);
		db.execSQL(createtableSQL4);
		
		Log.d(TAG, "Created Tables");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + DB_COURSE);
		db.execSQL("DROP TABLE IF EXISTS " + DB_TIME);
		db.execSQL("DROP TABLE IF EXISTS " + DB_VENUES);
		db.execSQL("DROP TABLE IF EXISTS " + DB_SELECTEDCOURSE);
		
		// create new tables
		this.onCreate(db);
	
	}
	
}
