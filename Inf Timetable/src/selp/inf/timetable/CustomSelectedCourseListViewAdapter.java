package selp.inf.timetable;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomSelectedCourseListViewAdapter extends BaseAdapter {

	public static final String TAG = CustomSelectedCourseListViewAdapter.class.getSimpleName();
	
	LayoutInflater inflater;
	List<CourseListDisplayItem> items;
	Context context;
	
    public CustomSelectedCourseListViewAdapter(Activity context, List<CourseListDisplayItem> items) {  
        
    	super();
		
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
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
        final SelectedCourseViewHolder courseHolder;
    	        
        if(vi == null) {    
        		
        	vi = inflater.inflate(R.layout.selected_course_list_row, null);
            
        	courseHolder = new SelectedCourseViewHolder();
            
        	courseHolder.txtCourseLevel = (TextView) vi.findViewById(R.id.courseLevelSelected);
        	courseHolder.txtCourseCredits = (TextView) vi.findViewById(R.id.courseCreditsSelected);
        	courseHolder.txtCourseLecturer = (TextView) vi.findViewById(R.id.courseLecturerSelected);
        	courseHolder.txtCourseDP = (TextView) vi.findViewById(R.id.courseDeliveryPeriodSelected);
        	courseHolder.linkCourseURL = (TextView) vi.findViewById(R.id.txtURLSelected);
        	courseHolder.linkCourseDRPS = (TextView) vi.findViewById(R.id.txtDRPSSelected);
        	courseHolder.courseSelect = (TextView) vi.findViewById(R.id.courseNameSelected);
            
        	vi.setTag(courseHolder);
            
        	} else {
        	
        		courseHolder = (SelectedCourseViewHolder) vi.getTag(); 
        	
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
         	String url = "<a href='" + item.getCourseURL() + "'> Homepage </a>";        
         	courseHolder.linkCourseURL.setText(Html.fromHtml(url));
        
         	//Creating a link to the course DRPS page
         	courseHolder.linkCourseDRPS.setClickable(true);
         	courseHolder.linkCourseDRPS.setMovementMethod(LinkMovementMethod.getInstance());
         	String drps = "<a href='" + item.getCourseDRPS() + "'> DRPS </a>";        
         	courseHolder.linkCourseDRPS.setText(Html.fromHtml(drps));
         	
        return vi;  
    
    }
	
    protected class SelectedCourseViewHolder {
    	
    	TextView txtCourseLevel;
        TextView txtCourseCredits;
        TextView txtCourseLecturer;
        TextView txtCourseDP;
        TextView linkCourseURL;
        TextView linkCourseDRPS;
		TextView courseSelect;
        
    }

	
}
