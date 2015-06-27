package selp.inf.timetable;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SelectCourses extends Activity {

	public static final String TAG = SelectCourses.class.getSimpleName();
	
	DBHelper DbHelper;
	SQLiteDatabase db;

	ListView courseList;
	ArrayList<CourseListDisplayItem> courseItems;
	ArrayList<CourseListDisplayItem> selectedCourseList;
	
	Button createTT;
	Button showSelected;
	Button showFiltered;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_courses);
        
        Log.d(TAG, "Started SelectCourses Activity");
        
        //Getting Extras from Intent
        Bundle extras = getIntent().getExtras();
        
        String semester = extras.getString("Sem");
        String year = extras.getString("Year");
        String cName = extras.getString("CName");
        String cAcronym = extras.getString("CAcronym");
        
        Log.d(TAG, "Extras: " + semester + ", " + year + ", " + cName+ ", " + cAcronym);        
        
        populateList(semester, year, cName, cAcronym);
    	             
        Log.d(TAG, "All courses added to arraylist");
    	
        courseList = (ListView)findViewById(R.id.coursesList); 
        
    	final CustomCourseListViewAdapter adapter = new CustomCourseListViewAdapter(this, courseItems);
    	courseList.setAdapter(adapter);
    	
        Log.d(TAG, "List Populated");
    	
        createTT = (Button) findViewById(R.id.createTT);
        createTT.setOnClickListener(new OnClickListener() {
		        	
			@Override
			public void onClick(View v) {
				
				DbHelper = new DBHelper(SelectCourses.this);
				db = DbHelper.getWritableDatabase();

				String query = "SELECT * FROM " + DBHelper.DB_SELECTEDCOURSE;
				Cursor rows = db.rawQuery(query, null);
				
				//If the Selected Courses table is not empty then empty the table 
				if (rows.getCount() > 0) {
					db.delete(DBHelper.DB_SELECTEDCOURSE, null, null);
				}
				
				rows.close();
				db.close();

		        ArrayList<String> coursesChosen = adapter.selectedCourses;	

		        Log.d(TAG, coursesChosen.size() + "");
		        
				Intent timetable = new Intent(SelectCourses.this, Timetable.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				timetable.putExtra("Selected Courses", coursesChosen);
				
				startActivity(timetable);
				
			}
		});
        
        showSelected = (Button) findViewById(R.id.showSelected);
        showFiltered = (Button) findViewById(R.id.btnFilteredCourses);
        
        showFiltered.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				courseList.setAdapter(adapter);
				
			}
		});
        
        showSelected.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ArrayList<String> onlySelectedCourses = adapter.selectedCourses;
				
				Log.d(TAG, "No. of Selected Courses got from adapter: " + onlySelectedCourses.size());
			
				populateSelected(onlySelectedCourses);
				
			}
		});
 
    }
    
    private void populateList(String sem, String year, String cName, String cAcr) {
	    	
    	//Getting readable database to populate list
    	DbHelper = new DBHelper(this);
    	db = DbHelper.getReadableDatabase();
    	
    	Log.d(TAG, "Choosing correct query for database");
    	
    	String query = null;
    	
    	boolean sembool = sem.equals("All");
    	boolean yearbool = year.equals("All");
    	boolean cNamebool = cName.equals("");
    	boolean cAcrbool = cAcr.equals("");
    	
    	//Creating correct query 
    	if (sembool && cNamebool && cAcrbool && !(yearbool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " C INNER JOIN " + DBHelper.DB_TIME + " T ON T.Time_COURSEACRONYM = C.Course_ACRONYM WHERE T.Time_YEAR LIKE '%" + year +"%' GROUP BY C.Course_NAME";
    	} else if (sembool && yearbool && cAcrbool && !(cNamebool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " WHERE Course_NAME = '" + cName +"'";
    	} else if (sembool && yearbool && cNamebool && !(cAcrbool)) {
        	query = "SELECT * FROM " + DBHelper.DB_COURSE + " WHERE Course_ACRONYM = '" + cAcr + "'";
    	} else if (yearbool && cNamebool && cAcrbool && !(sembool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " WHERE Course_DP = '" + sem + "'";
    	} else if (sembool && cAcrbool && !(yearbool) && !(cNamebool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " C INNER JOIN " + DBHelper.DB_TIME + " T ON T.Time_COURSEACRONYM = C.Course_ACRONYM WHERE T.Time_YEAR LIKE '%" + year + "%' AND C.Course_NAME = '" + cName + "'" + " GROUP BY C.Course_NAME";
    	} else if (sembool && cNamebool && !(yearbool) && !(cAcrbool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " C INNER JOIN " + DBHelper.DB_TIME + " T ON T.Time_COURSEACRONYM = C.Course_ACRONYM WHERE T.Time_YEAR LIKE '%" + year +"%' AND C.Course_ACRONYM = '" + cAcr +"'" + " GROUP BY C.Course_NAME";
    	} else if (sembool && yearbool && !(cAcrbool) && !(cNamebool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " WHERE Course_NAME = '" + cName + "' AND Course_ACRONYM = '" + cAcr +"'";
    	} else if (cNamebool && cAcrbool && !(sembool) && !(yearbool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " C INNER JOIN " + DBHelper.DB_TIME + " T ON T.Time_COURSEACRONYM = C.Course_ACRONYM WHERE T.Time_YEAR LIKE '%" + year + "%' AND C.Course_DP = '" + sem +"'"  + " GROUP BY C.Course_NAME";
    	} else if (yearbool && cAcrbool && !(sembool) && !(cNamebool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " WHERE Course_DP = '" + sem + "' AND Course_NAME = '" + cName + "'";
    	} else if (yearbool && cNamebool && !(sembool) && !(cAcrbool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " WHERE Course_DP = '" + sem + "' AND Course_ACRONYM = '" + cAcr + "'";
    	} else if (cAcrbool && !(sembool) && !(yearbool) && !(cNamebool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " C INNER JOIN " + DBHelper.DB_TIME + " T ON T.Time_COURSEACRONYM = C.Course_ACRONYM WHERE T.Time_YEAR LIKE '%" + year + "%' AND C.Course_DP = '" + sem + "' AND C.Course_NAME = '" + cName + "'" + " GROUP BY C.Course_NAME";
    	} else if (cNamebool && !(sembool) && !(yearbool) && !(cAcrbool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " C INNER JOIN " + DBHelper.DB_TIME + " T ON T.Time_COURSEACRONYM = C.Course_ACRONYM WHERE T.Time_YEAR LIKE '%" + year + "%' AND C.Course_DP = '" + sem + "' AND C.Course_ACRONYM = '" + cAcr + "'" + " GROUP BY C.Course_NAME";
    	} else if (yearbool && !(sembool) && !(cNamebool) && !(cAcrbool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " WHERE Course_DP = '" + sem + "' AND Course_NAME = '" + cName + "' AND Course_ACRONYM = '" + cAcr + "'";
    	} else if (sembool && !(yearbool) && !(cNamebool) && !(cAcrbool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " C INNER JOIN " + DBHelper.DB_TIME + " T ON T.Time_COURSEACRONYM = C.Course_ACRONYM WHERE T.Time_YEAR LIKE '%" + year + "%' AND C.Course_NAME = '" + cName + "' AND C.Course_ACRONYM = '" + cAcr + "'" + " GROUP BY C.Course_NAME";
    	} else if (!(sembool) && !(yearbool) && !(cNamebool) && !(cAcrbool)) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE + " C INNER JOIN " + DBHelper.DB_TIME + " T ON T.Time_COURSEACRONYM = C.Course_ACRONYM WHERE T.Time_YEAR LIKE '%" + year + "%' AND C.Course_NAME = '" + cName + "' AND C.Course_ACRONYM = '" + cAcr + "'" + " GROUP BY C.Course_NAME";
    	} else if (sembool && yearbool && cNamebool && cAcrbool) {
    		query = "SELECT * FROM " + DBHelper.DB_COURSE;
    	}
    
    	Log.d(TAG, "Query: " + query);
    	
    	//Retrieving all courses matching the query
    	Cursor foundCourses = db.rawQuery(query, null);
    	
    	Log.d(TAG, "Records retrieved: " + foundCourses.getCount());
    	    	
    	//New ArrayList to store all filtered courses
    	courseItems = new ArrayList<CourseListDisplayItem>();
    	
    	foundCourses.moveToFirst();
    	
    	while (foundCourses.isAfterLast() == false) {
    		    			
	    		CourseListDisplayItem item = new CourseListDisplayItem();
	    		
	    		item.setCourseName(foundCourses.getString(1));
	    		Log.d(TAG, foundCourses.getString(1));	    			    		
	    		item.setCourseDP(foundCourses.getString(3));
	    		Log.d(TAG, foundCourses.getString(3));
	    		item.setCourseLecturer(foundCourses.getString(4));
	    		Log.d(TAG, foundCourses.getString(4));
	    		item.setCourseLevel(foundCourses.getString(6));
	    		Log.d(TAG, foundCourses.getString(6));
	    		item.setCourseCredits(foundCourses.getString(7));
	    		Log.d(TAG, foundCourses.getString(7));
	    		item.setCourseURL(foundCourses.getString(8));
	    		Log.d(TAG, foundCourses.getString(8));
	    		item.setCourseDRPS(foundCourses.getString(9));
	    		Log.d(TAG, foundCourses.getString(9));
	    		
	    		courseItems.add(item);
	    		    	
	    		foundCourses.moveToNext();	
    		
    	}
    	
    	foundCourses.close();
    	db.close();
    	
    }
        

    private void populateSelected(ArrayList<String> selected) {
    	
    	DbHelper = new DBHelper(this);
    	db = DbHelper.getReadableDatabase();
    	
    	//New ArrayList to store selected course
        selectedCourseList = new ArrayList<CourseListDisplayItem>();
    	
    	for (int item = 0; item < selected.size(); item++) {
    		
    		String query = "SELECT * FROM " + DBHelper.DB_COURSE + " WHERE Course_Name = '" + selected.get(item) + "'";
    		Cursor selectedRows = db.rawQuery(query, null);
    		
    		selectedRows.moveToFirst();
    		
    		CourseListDisplayItem courseSelected = new CourseListDisplayItem();
    		
    		courseSelected.setCourseName(selectedRows.getString(1));
    		Log.d(TAG, selectedRows.getString(1));
    		courseSelected.setCourseDP(selectedRows.getString(3));
    		Log.d(TAG, selectedRows.getString(3));
    		courseSelected.setCourseLecturer(selectedRows.getString(4));
    		Log.d(TAG, selectedRows.getString(4));
    		courseSelected.setCourseLevel(selectedRows.getString(6));
    		Log.d(TAG, selectedRows.getString(6));
    		courseSelected.setCourseCredits(selectedRows.getString(7));
    		Log.d(TAG, selectedRows.getString(7));
    		courseSelected.setCourseURL(selectedRows.getString(8));
    		Log.d(TAG, selectedRows.getString(8));
    		courseSelected.setCourseDRPS(selectedRows.getString(9));
    		Log.d(TAG, selectedRows.getString(9));
    		
    		selectedCourseList.add(courseSelected);

        	selectedRows.close();
    		
    	}
    	
    	db.close();
    	    	
    	ListView CourseList = (ListView) findViewById(R.id.coursesList);
    	
    	//Set new adapter to display only selected courses
    	CustomSelectedCourseListViewAdapter adapterSelected = new CustomSelectedCourseListViewAdapter(SelectCourses.this, selectedCourseList);
    	CourseList.setAdapter(adapterSelected);
    	
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_courses, menu);
        return true;
    }
	
}
