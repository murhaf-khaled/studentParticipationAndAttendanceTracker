package com.example.studentparticipationandattendancetracker;

public class StudentRecord {

	private int studentID;
	private String studentName;
	private String studentEmail;
	private boolean selected;
	
	/**
	 * @param studentID
	 * @param studentName
	 * @param studentEmail
	 * @param selected
	 */
	public StudentRecord(int studentID, String studentName,
			String studentEmail, boolean selected) {
		super();
		this.studentID = studentID;
		this.studentName = studentName;
		this.studentEmail = studentEmail;
		this.selected = selected;
	}
	/**
	 * @return the studentID
	 */
	public int getStudentID() {
		return studentID;
	}
	/**
	 * @param studentID the studentID to set
	 */
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	/**
	 * @return the studentName
	 */
	public String getStudentName() {
		return studentName;
	}
	/**
	 * @param studentName the studentName to set
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	/**
	 * @return the studentEmail
	 */
	public String getStudentEmail() {
		return studentEmail;
	}
	/**
	 * @param studentEmail the studentEmail to set
	 */
	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
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
