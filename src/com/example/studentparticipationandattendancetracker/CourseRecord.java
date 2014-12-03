/**
 * 
 */
package com.example.studentparticipationandattendancetracker;

public class CourseRecord {

	private int courseID;
	private int instructorID;
	private String courseName;
	private String courseDays;
	private boolean selected;
	
	public CourseRecord(int courseID, int instructorID, String courseName,
			String courseDays) {
		this.courseID = courseID;
		this.instructorID = instructorID;
		this.courseName = courseName;
		this.courseDays = courseDays;
	}

	public CourseRecord(int courseID, int instructorID, String courseName,
			String courseDays, boolean selected) {
		super();
		this.courseID = courseID;
		this.instructorID = instructorID;
		this.courseName = courseName;
		this.courseDays = courseDays;
		this.selected = selected;
	}

	public int getCourseID() {
		return courseID;
	}

	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}

	public int getInstructorID() {
		return instructorID;
	}

	public void setInstructorID(int instructorID) {
		this.instructorID = instructorID;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseDays() {
		return courseDays;
	}

	public void setCourseDays(String courseDays) {
		this.courseDays = courseDays;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}


}
