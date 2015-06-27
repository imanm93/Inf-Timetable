package selp.inf.timetable;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomCourseListViewAdapter extends BaseAdapter {

	public static final String TAG = CustomCourseListViewAdapter.class.getSimpleName();
	
	LayoutInflater inflater;
	List<CourseListDisplayItem> items;
	SparseBooleanArray mCheckCourseStates;
	ArrayList<String> selectedCourses;
	Context context;
	
    public CustomCourseListViewAdapter(Activity context, List<CourseListDisplayItem> items) {  
        
    	super();
		
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        mCheckCourseStates = new SparseBooleanArray(items.size());
     	selectedCourses = new ArrayList<String>();	
        
    }
    
    @Override  
    public int getCount() {  
        return items.size();  
    }  
  
    @Override  
    public Object getItem(int position) {   
        return null;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return 0;  
    }
      
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  

        CourseListDisplayItem item = items.get(position);
        
    	View vi=convertView;
        final CourseViewHolder courseHolder;
    	        
        if(vi == null) {    
        		
        	vi = inflater.inflate(R.layout.course_list_row, null);
            
        	courseHolder = new CourseViewHolder();
            
        	courseHolder.txtCourseLevel = (TextView) vi.findViewById(R.id.courseLevel);
        	courseHolder.txtCourseCredits = (TextView) vi.findViewById(R.id.courseCredits);
        	courseHolder.txtCourseLecturer = (TextView) vi.findViewById(R.id.courseLecturer);
        	courseHolder.txtCourseDP = (TextView) vi.findViewById(R.id.courseDeliveryPeriod);
        	courseHolder.linkCourseURL = (TextView) vi.findViewById(R.id.txtCourseURL);
        	courseHolder.linkCourseDRPS = (TextView) vi.findViewById(R.id.txtcourseDRPS);
        	courseHolder.courseSelect = (CheckBox) vi.findViewById(R.id.courseSelected);
            
        	vi.setTag(courseHolder);
            
        	} else {
        	
        		courseHolder = (CourseViewHolder) vi.getTag(); 
        	
        	}
         	
         	courseHolder.courseSelect.setId(position);
         	courseHolder.txtCourseCredits.setId(position);
         	courseHolder.txtCourseDP.setId(position);
         	courseHolder.txtCourseLecturer.setId(position);
         	courseHolder.txtCourseLevel.setId(position);
         	courseHolder.linkCourseURL.setId(position);
         	courseHolder.linkCourseDRPS.setId(position);
         	
         	//Assigning values to all the relevant text views
         	courseHolder.txtCourseLevel.setText(item.getCourseLevel());
         	courseHolder.txtCourseCredits.setText(item.getCourseCredits());
         	courseHolder.txtCourseLecturer.setText(item.getCourseLecturer());
         	courseHolder.txtCourseDP.setText(item.getCourseDP());
         	courseHolder.courseSelect.setText(item.getCourseName());
         	
         	//Creating a link to the course web site
         	courseHolder.linkCourseURL.setClickable(true);
         	courseHolder.linkCourseURL.setMovementMethod(LinkMovementMethod.getInstance());
         	String url = "<a href='" + item.getCourseURL() + "'>Homepage</a>";        
         	courseHolder.linkCourseURL.setText(Html.fromHtml(url));
        
         	//Creating a link to the course DRPS page
         	courseHolder.linkCourseDRPS.setClickable(true);
         	courseHolder.linkCourseDRPS.setMovementMethod(LinkMovementMethod.getInstance());
         	String drps = "<a href='" + item.getCourseDRPS() + "'>DRPS</a>";        
         	courseHolder.linkCourseDRPS.setText(Html.fromHtml(drps));
            
         	final LinearLayout course_row = (LinearLayout) vi.findViewById(R.id.course_row_layout);
         	         	
         	courseHolder.courseSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            	
         		@Override
         		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			
         			if (isChecked) {
					 	//Manually check item selected and change background colour
					 	courseHolder.courseSelect.setChecked(true);
         	         	mCheckCourseStates.put(position, true);
	                    selectedCourses.add(courseHolder.courseSelect.getText().toString());
         	         	Log.d(TAG, "Course Seleceted: " + courseHolder.courseSelect.getText().toString());
	                    course_row.setBackgroundColor(Color.YELLOW);
	                }else{
	                	//Manually uncheck item selected and reset background colour to original 
	                	courseHolder.courseSelect.setChecked(false);
         				mCheckCourseStates.put(position, false);
	                	selectedCourses.remove(courseHolder.courseSelect.getText().toString());
	                	course_row.setBackgroundColor(Color.WHITE);
	                }				
         		}
         	});
                  	
         	courseHolder.courseSelect.setChecked(mCheckCourseStates.get(position));
         	
         	//Checking for duplicates in the array 
	        //Because sometimes an item is registered twice when only clicked on once
	        for (int check = 0; check < selectedCourses.size(); check++) {
	        	for (int check1 = 0; check1 < selectedCourses.size(); check1++) {  		
	        		if (!(check == check1)) {
	        			if (selectedCourses.get(check).equals(selectedCourses.get(check1))) {
	        				selectedCourses.remove(check1);
	        			}
	        		}
	        	}
	        }
         	
         	Log.d(TAG, selectedCourses.size() + "");
         	
        return vi;  
    
    }
	
    protected class CourseViewHolder {
    	
    	TextView txtCourseLevel;
        TextView txtCourseCredits;
        TextView txtCourseLecturer;
        TextView txtCourseDP;
        TextView linkCourseURL;
        TextView linkCourseDRPS;
        CheckBox courseSelect;
        
    }
    
}
