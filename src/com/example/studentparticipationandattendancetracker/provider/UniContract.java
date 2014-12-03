package com.example.studentparticipationandattendancetracker.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class UniContract {

	public static final String AUTHORITY = "com.example.studentparticipationandattendancetracker.provider";
	public static final Uri BASE_URI = Uri
			.parse("content://" + AUTHORITY + "/");
	
	private static final String VARCHAR_TYPE = " VARCHAR";
    private static final String COMMA_SEP = ",";
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
	public UniContract() {
	}
	
	/* Inner class that defines the Instructor table contents */
    public static abstract class InstructorEntry implements BaseColumns {
        public static final String TABLE_NAME = "Instructor";
        // The URI for this table.
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,TABLE_NAME);
    	
        public static final String COLUMN_NAME_INSTRUCTOR_ID = "instructor_ID";
        public static final String COLUMN_NAME_INSTRUCTOR_NAME = "instructor_Name";
        public static final String COLUMN_NAME_INSTRUCTOR_PASSWORD = "instructor_Password";
        public static final String COLUMN_NAME_INSTRUCTOR_EMAIL = "instructor_Email";
        public static final String COLUMN_NAME_INSTRUCTOR_DEPARTMENT = "Department";
        
		public static final String SQL_CREATE_ENTRIES =
		    "CREATE TABLE " + TABLE_NAME + " (" +
		    COLUMN_NAME_INSTRUCTOR_ID + " INTEGER PRIMARY KEY"+ COMMA_SEP +
		    COLUMN_NAME_INSTRUCTOR_NAME + VARCHAR_TYPE + COMMA_SEP +
		    COLUMN_NAME_INSTRUCTOR_PASSWORD + VARCHAR_TYPE + COMMA_SEP +
		    COLUMN_NAME_INSTRUCTOR_EMAIL + VARCHAR_TYPE + COMMA_SEP +
		    COLUMN_NAME_INSTRUCTOR_DEPARTMENT + VARCHAR_TYPE +
		    " )";
		
    }
    
    /* Inner class that defines the Course table contents */
    public static abstract class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "Course";
        // The URI for this table.
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,TABLE_NAME);
    	
        public static final String COLUMN_NAME_COURSE_ID = "course_ID";
        public static final String COLUMN_NAME_INSTRUCTOR_ID = "instructor_ID";
        public static final String COLUMN_NAME_COURSE_NAME = "course_Name";
        public static final String COLUMN_NAME_COURSE_DAYS = "course_days";
        
        public static final String SQL_CREATE_ENTRIES = 
				"CREATE TABLE "	+ TABLE_NAME + " ("
				+ COLUMN_NAME_COURSE_ID + " INTEGER PRIMARY KEY" + COMMA_SEP
				+ COLUMN_NAME_INSTRUCTOR_ID + " INTEGER" + COMMA_SEP
				+ COLUMN_NAME_COURSE_NAME + VARCHAR_TYPE + COMMA_SEP
				+ COLUMN_NAME_COURSE_DAYS + VARCHAR_TYPE + COMMA_SEP
				+ "FOREIGN KEY(" + COLUMN_NAME_INSTRUCTOR_ID + ") REFERENCES "
				+ InstructorEntry.TABLE_NAME + "("
				+ InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID + ")" + " )";
    }
    
    /* Inner class that defines the Student table contents */
    public static abstract class StudentEntry implements BaseColumns {
        public static final String TABLE_NAME = "Student";
        // The URI for this table.
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,TABLE_NAME);
    	
        public static final String COLUMN_NAME_STUDENT_ID = "student_ID";
        public static final String COLUMN_NAME_STUDENT_NAME = "student_Name";
        public static final String COLUMN_NAME_STUDENT_EMAIL = "student_Email";
        
        public static final String SQL_CREATE_ENTRIES =
		    "CREATE TABLE " + TABLE_NAME + " (" +
		    COLUMN_NAME_STUDENT_ID + " INTEGER PRIMARY KEY"+ COMMA_SEP +
		    COLUMN_NAME_STUDENT_NAME + VARCHAR_TYPE + COMMA_SEP +
		    COLUMN_NAME_STUDENT_EMAIL + VARCHAR_TYPE +
		    " )";
    }
    
    /* Inner class that defines the Enrolls table contents */
    public static abstract class EnrollsEntry implements BaseColumns {
        public static final String TABLE_NAME = "Enrolls";
        // The URI for this table.
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,TABLE_NAME);
    	
        public static final String COLUMN_NAME_STUDENT_ID = "student_ID";
        public static final String COLUMN_NAME_COURSE_ID = "course_ID";

        public static final String SQL_CREATE_ENTRIES = 
				"CREATE TABLE "	+ TABLE_NAME + " ("
				+ COLUMN_NAME_STUDENT_ID + " INTEGER NOT NULL" + COMMA_SEP
				+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL" + COMMA_SEP
				+ "FOREIGN KEY(" + COLUMN_NAME_STUDENT_ID + ") REFERENCES "
				+ StudentEntry.TABLE_NAME + "("
				+ StudentEntry.COLUMN_NAME_STUDENT_ID + ") ON DELETE CASCADE" + COMMA_SEP
				+ "FOREIGN KEY(" + COLUMN_NAME_COURSE_ID + ") REFERENCES "
				+ CourseEntry.TABLE_NAME + "("
				+ CourseEntry.COLUMN_NAME_COURSE_ID + ") ON DELETE CASCADE" + COMMA_SEP
				+ "PRIMARY KEY(" + COLUMN_NAME_STUDENT_ID + COMMA_SEP
				+ COLUMN_NAME_COURSE_ID +")"
				+ " )";
		
    }
    
    /* Inner class that defines the Attendance table contents */
    public static abstract class AttendanceEntry implements BaseColumns {
        public static final String TABLE_NAME = "Attendance";
        // The URI for this table.
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,TABLE_NAME);
    	
        public static final String COLUMN_NAME_ATTENDANCE_DATE = "attendance_Date";
        public static final String COLUMN_NAME_STUDENT_ID = "student_ID";
        public static final String COLUMN_NAME_COURSE_ID = "course_ID";
        public static final String COLUMN_NAME_ATTENDANCE_STATUS = "attendance_Status";
        
        public static final String SQL_CREATE_ENTRIES = 
				"CREATE TABLE "	+ TABLE_NAME + " ("
				+ COLUMN_NAME_ATTENDANCE_DATE + VARCHAR_TYPE + COMMA_SEP
				+ COLUMN_NAME_STUDENT_ID + " INTEGER NOT NULL" + COMMA_SEP
				+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL" + COMMA_SEP
				+ COLUMN_NAME_ATTENDANCE_STATUS + VARCHAR_TYPE+" NOT NULL" + COMMA_SEP
				+ "PRIMARY KEY(" + COLUMN_NAME_ATTENDANCE_DATE + COMMA_SEP
				+ COLUMN_NAME_STUDENT_ID + COMMA_SEP
				+ COLUMN_NAME_COURSE_ID +")"
				+ " )";
    }
    
    /* Inner class that defines the Participation table contents */
    public static abstract class ParticipationEntry implements BaseColumns {
        public static final String TABLE_NAME = "Participation";
        // The URI for this table.
    	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,TABLE_NAME);
    	
        public static final String COLUMN_NAME_PARTICIPATION_DATE = "participation_Date";
        public static final String COLUMN_NAME_STUDENT_ID = "student_ID";
        public static final String COLUMN_NAME_COURSE_ID = "course_ID";
        public static final String COLUMN_NAME_PARTICIPATION_MARK = "participation_Mark";
       
        public static final String SQL_CREATE_ENTRIES = 
				"CREATE TABLE "	+ TABLE_NAME + " ("
				+ COLUMN_NAME_PARTICIPATION_DATE + VARCHAR_TYPE + COMMA_SEP
				+ COLUMN_NAME_STUDENT_ID + " INTEGER NOT NULL" + COMMA_SEP
				+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL" + COMMA_SEP
				+ COLUMN_NAME_PARTICIPATION_MARK + " INTEGER NOT NULL" + COMMA_SEP
				+ "PRIMARY KEY(" + COLUMN_NAME_PARTICIPATION_DATE + COMMA_SEP
				+ COLUMN_NAME_STUDENT_ID + COMMA_SEP
				+ COLUMN_NAME_COURSE_ID +")"
				+ " )";
		
    }

}
