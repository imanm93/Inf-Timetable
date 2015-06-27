package selp.inf.timetable;

public class TimeItem {

		//Variables to be retrieved from timetable.xml
		private String courseacronym;
		private String year;
		private String semester;
		private String day;
		private String building;
		private String room;
		private String timestart;
		private String timefinish;
		private String comment;
		
		public String getCourseacronym() {
			return courseacronym;
		}
		public void setCourseacronym(String courseacronym) {
			this.courseacronym = courseacronym;
		}
		
		public String getYear() {
			return year;
		}
		public void setYear(String year) {
			this.year = year;
		}
		
		public String getSemester() {
			return semester;
		}
		public void setSemester(String semester) {
			this.semester = "S" + semester;
		}
		
		public String getDay() {
			return day;
		}
		public void setDay(String day) {
			this.day = day;
		}
		
		public String getBuilding() {
			return building;
		}
		public void setBuilding(String building) {
			this.building = building;
		}
		
		public String getRoom() {
			return room;
		}
		public void setRoom(String room) {
			this.room = room;
		}
		
		public String getTimestart() {
			return timestart;
		}
		public void setTimestart(String timestart) {
			this.timestart = timestart;
		}
		
		public String getTimefinish() {
			return timefinish;
		}
		public void setTimefinish(String timefinish) {
			this.timefinish = timefinish;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
	
}
