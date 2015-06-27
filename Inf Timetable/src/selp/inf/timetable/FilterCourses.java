package selp.inf.timetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class FilterCourses extends Activity {

	public static final String TAG = FilterCourses.class.getSimpleName();
	
	Spinner selectSemester;
	Spinner selectYear;
	EditText courseName;
	EditText courseAcronym;
	
	Button searchCourses;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_courses);
        
        Log.d(TAG, "Started FilterCourses Activity");
        
        selectSemester = (Spinner) findViewById(R.id.chosenSemester);
        selectYear = (Spinner) findViewById(R.id.chosenYear);
        courseName = (EditText) findViewById(R.id.usertxtCourseName);
        courseAcronym = (EditText) findViewById(R.id.usertxtCourseAcronym);
        
        ArrayAdapter<CharSequence> adapterSem = ArrayAdapter.createFromResource(FilterCourses.this, R.array.semester, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterYear = ArrayAdapter.createFromResource(FilterCourses.this, R.array.year, android.R.layout.simple_spinner_item);
        
        Log.d(TAG, "Assigned spinner text");
        
        selectSemester.setAdapter(adapterSem);
        selectYear.setAdapter(adapterYear);
        
        selectSemester.setPrompt("Choose Semeseter ...");
        selectYear.setPrompt("Choose Year ...");
        
        Log.d(TAG, "Selected Filters");
        
        searchCourses = (Button) findViewById(R.id.searchCourses);        
        searchCourses.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent showCourses = new Intent(FilterCourses.this, SelectCourses.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				showCourses.putExtra("Sem", selectSemester.getSelectedItem().toString());
				showCourses.putExtra("Year", selectYear.getSelectedItem().toString());
				showCourses.putExtra("CName", courseName.getText().toString());
				showCourses.putExtra("CAcronym", courseAcronym.getText().toString());
				
				startActivity(showCourses);
				
			}
			
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_filter_courses, menu);
        return true;
    }
}
