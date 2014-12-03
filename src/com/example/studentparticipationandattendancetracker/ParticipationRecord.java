package com.example.studentparticipationandattendancetracker;

import java.util.Date;

public class ParticipationRecord {

	private Date partDate;
	private int studentID;
	private String studentName;
	private int courseID;
	private int partMark;
	/**
	 * @param partDate
	 * @param studentID
	 * @param studentName
	 * @param courseID
	 * @param partMark
	 */
	public ParticipationRecord(Date partDate, int studentID,
			String studentName, int courseID, int partMark) {
		super();
		this.partDate = partDate;
		this.studentID = studentID;
		this.studentName = studentName;
		this.courseID = courseID;
		this.partMark = partMark;
	}
	/**
	 * @param studentID
	 * @param studentName
	 * @param partMark
	 */
	public ParticipationRecord(int studentID, String studentName, int partMark) {
		super();
		this.studentID = studentID;
		this.studentName = studentName;
		this.partMark = partMark;
	}
	/**
	 * @return the partDate
	 */
	public Date getPartDate() {
		return partDate;
	}
	/**
	 * @param partDate the partDate to set
	 */
	public void setPartDate(Date partDate) {
		this.partDate = partDate;
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
	 * @return the courseID
	 */
	public int getCourseID() {
		return courseID;
	}
	/**
	 * @param courseID the courseID to set
	 */
	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}
	/**
	 * @return the partMark
	 */
	public int getPartMark() {
		return partMark;
	}
	/**
	 * @param partMark the partMark to set
	 */
	public void setPartMark(int partMark) {
		this.partMark = partMark;
	}
	
	
}
