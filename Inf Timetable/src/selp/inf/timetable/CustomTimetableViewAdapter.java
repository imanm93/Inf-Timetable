package selp.inf.timetable;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomTimetableViewAdapter extends BaseAdapter {

	public static final String TAG = CustomTimetableViewAdapter.class.getSimpleName();
	
	LayoutInflater inflater;
	List<TimetableDisplayItem> items;
	
    public CustomTimetableViewAdapter(Activity context, List<TimetableDisplayItem> items) {  
        
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

        TimetableDisplayItem item = items.get(position);
    	
    	View vi=convertView;
        final TimetableHolder timetableHolder;
    	
        if(vi==null) {
        	
            vi = inflater.inflate(R.layout.timetable_row, null);
        
            timetableHolder = new TimetableHolder();
            
            timetableHolder.txtDay = (TextView) vi.findViewById(R.id.day);
            timetableHolder.txtTimeSTART = (TextView) vi.findViewById(R.id.timeStart);
            timetableHolder.txtTimeFINISH = (TextView) vi.findViewById(R.id.timeFinish);
            timetableHolder.txtCourseACRONYM = (TextView) vi.findViewById(R.id.courseACR);
            timetableHolder.txtBuilding = (TextView) vi.findViewById(R.id.Building);
            timetableHolder.txtRoom = (TextView) vi.findViewById(R.id.room);
            timetableHolder.txtComment = (TextView) vi.findViewById(R.id.comment);
            
            vi.setTag(timetableHolder);
            
        	} else {
        	
        	timetableHolder = (TimetableHolder) vi.getTag();
        
        	}
        
        timetableHolder.txtDay.setId(position);
        timetableHolder.txtTimeSTART.setId(position);
        timetableHolder.txtTimeFINISH.setId(position);
        timetableHolder.txtCourseACRONYM.setId(position);
        timetableHolder.txtBuilding.setId(position);
        timetableHolder.txtRoom.setId(position);
        timetableHolder.txtComment.setId(position);
        
        timetableHolder.txtDay.setText(item.getDay());
        timetableHolder.txtTimeSTART.setText(item.getTimeSTART());
        timetableHolder.txtTimeFINISH.setText(item.getTimeFINISH());
        timetableHolder.txtCourseACRONYM.setText(item.getCourseACRONYM());
        timetableHolder.txtRoom.setText(item.getTimeROOM());
        timetableHolder.txtComment.setText(item.getComment());
        
        timetableHolder.txtBuilding.setClickable(true);
        timetableHolder.txtBuilding.setMovementMethod(LinkMovementMethod.getInstance());
        String mapLink = "<a href='" + item.getVenueMAP() + "'>" + item.getVenueDESCRIPTION() + "</a>";
        timetableHolder.txtBuilding.setText(Html.fromHtml(mapLink));
        
        Log.d(TAG, "Item Added: " + item.getCourseACRONYM());
        
        return vi;  
    
    }
	
    protected class TimetableHolder {
    	
    	TextView txtDay;
        TextView txtTimeSTART;
        TextView txtTimeFINISH;
        TextView txtCourseACRONYM;
        TextView txtBuilding;
        TextView txtRoom;
        TextView txtComment;
    	
    }
    
}
