package com.example.studentparticipationandattendancetracker;

import com.example.studentparticipationandattendancetracker.provider.UniContract.AttendanceEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.CourseEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.EnrollsEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.InstructorEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.ParticipationEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.StudentEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database6.db";

	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(InstructorEntry.SQL_CREATE_ENTRIES);
		db.execSQL(CourseEntry.SQL_CREATE_ENTRIES);
		db.execSQL(StudentEntry.SQL_CREATE_ENTRIES);
		db.execSQL(EnrollsEntry.SQL_CREATE_ENTRIES);
		db.execSQL(AttendanceEntry.SQL_CREATE_ENTRIES);
		db.execSQL(ParticipationEntry.SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
