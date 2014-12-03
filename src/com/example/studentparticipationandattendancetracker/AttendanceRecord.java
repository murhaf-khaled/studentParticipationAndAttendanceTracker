package com.example.studentparticipationandattendancetracker;

import java.util.Date;

public class AttendanceRecord {

	private Date attendanceDate;
	private int studentID;
	private String studentName;
	private int courseID;
	private String attendanceStatus;
	private int count;
	/**
	 * @param attendanceDate
	 * @param studentID
	 * @param studentName
	 * @param courseID
	 * @param attendanceStatus
	 */
	public AttendanceRecord(Date attendanceDate, int studentID,
			String studentName, int courseID, String attendanceStatus) {
		super();
		this.attendanceDate = attendanceDate;
		this.studentID = studentID;
		this.studentName = studentName;
		this.courseID = courseID;
		this.attendanceStatus = attendanceStatus;
	}
	/**
	 * @param attendanceDate
	 * @param studentID
	 * @param studentName
	 * @param courseID
	 * @param count
	 */
	public AttendanceRecord(Date attendanceDate, int studentID,
			String studentName, int courseID, int count) {
		super();
		this.attendanceDate = attendanceDate;
		this.studentID = studentID;
		this.studentName = studentName;
		this.courseID = courseID;
		this.count = count;
	}
	/**
	 * @param studentID
	 * @param studentName
	 * @param count
	 */
	public AttendanceRecord(int studentID, String studentName, int count) {
		super();
		this.studentID = studentID;
		this.studentName = studentName;
		this.count = count;
	}
	/**
	 * @return the attendanceDate
	 */
	public Date getAttendanceDate() {
		return attendanceDate;
	}
	/**
	 * @param attendanceDate the attendanceDate to set
	 */
	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
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
	 * @return the attendanceStatus
	 */
	public String getAttendanceStatus() {
		return attendanceStatus;
	}
	/**
	 * @param attendanceStatus the attendanceStatus to set
	 */
	public void setAttendanceStatus(String attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
}
