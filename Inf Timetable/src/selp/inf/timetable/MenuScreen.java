package selp.inf.timetable;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuScreen extends Activity {

	public static final String TAG = MenuScreen.class.getSimpleName();
	
	Button showSavedTimetable;
	Button makeNewTimetable;
	
	DBHelper DbHelper;
	SQLiteDatabase db;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Log.d(TAG, "Started MenuScreen Activity");
        
        showSavedTimetable = (Button) findViewById(R.id.showSavedTimetable);
        showSavedTimetable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Getting DBHelper to read from database
				DbHelper = new DBHelper(MenuScreen.this);
				db = DbHelper.getReadableDatabase();
				
				String query = "SELECT * FROM " + DBHelper.DB_SELECTEDCOURSE;
				Cursor courseNames = db.rawQuery(query, null);
				
				courseNames.moveToFirst();
				
				ArrayList<String> coursesTimetable = new ArrayList<String>();
				
				//Loop till last row in cursor is reached
				while (courseNames.isAfterLast() == false) {
										
					coursesTimetable.add(courseNames.getString(1));
					
					courseNames.moveToNext();	
					
				}
				
				courseNames.close();
				db.close();
				
		        //Creating new intent to go to the timetable activity
				Intent timetable = new Intent(MenuScreen.this, Timetable.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				timetable.putExtra("Selected Courses", coursesTimetable);
				
				startActivity(timetable);
			
			}
		});
        
        makeNewTimetable = (Button) findViewById(R.id.btnFilterCourses);
        makeNewTimetable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Creating intent to go to the Filter Courses activity
				Intent filterCourses = new Intent(MenuScreen.this, FilterCourses.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(filterCourses);
				
			}
		});
        
    }

}
