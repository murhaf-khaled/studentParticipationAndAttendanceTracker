package com.example.studentparticipationandattendancetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.studentparticipationandattendancetracker.provider.UniContract.AttendanceEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.EnrollsEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.ParticipationEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.StudentEntry;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class StudentAttendanceActivity extends Activity {

	public static String CURRENT_COURSE_ID_PREFS = "current_course_id_prefs";
	public static String CURRENT_COURSE_ID_KEY = "current_course_id_key";

	public static String REPORT_PREFS = "report_prefs";
	public static String REPORT_KEY = "report_key";

	SharedPreferences prefs;

	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;

	private AttendanceViewAdapter mCursorAdapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_attendance);

		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getReadableDatabase();

		mCursorAdapter = new AttendanceViewAdapter(this, null, 0, mDbHelper);

		listView = (ListView) findViewById(R.id.attendance_LV);
		// Assign adapter to ListView
		listView.setAdapter(mCursorAdapter);

		
		prefs = getSharedPreferences(CURRENT_COURSE_ID_PREFS, MODE_PRIVATE);
		int currentCourseID = prefs.getInt(CURRENT_COURSE_ID_KEY, 0);

		String sql = "SELECT student_Name, Student.student_ID, course_ID FROM Student JOIN Enrolls ON Student.student_ID = Enrolls.student_ID WHERE course_ID = "
				+ currentCourseID;

		Cursor newCursor = mDB.rawQuery(sql, null);

		if (null != newCursor) {
			if (newCursor.moveToFirst()) {
				do {
					mCursorAdapter.addToList(getRecordFromCursor(newCursor));
					mCursorAdapter.notifyDataSetChanged();
					
				} while (newCursor.moveToNext() == true);
			}
		}

		// back Button
		
		final Button backButton = (Button) findViewById(R.id.back_student_attendance_button);

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent studentParticipationActivityIntent = new Intent(getApplicationContext(), StudentParticipationActivity.class);
				startActivity(studentParticipationActivityIntent);
				finish();
			}

		});
		
		// TODO adding to Attendance Table
		final Button doneButton = (Button) findViewById(R.id.done_student_attendance_button);

		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				long rowId = -1;

				Calendar c = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String currentDate = format.format(c.getTime());

				for (AttendanceRecord row : mCursorAdapter.getList()) {

					ContentValues initialValues = new ContentValues();
					initialValues.put(
							AttendanceEntry.COLUMN_NAME_ATTENDANCE_DATE,
							currentDate);
					initialValues.put(AttendanceEntry.COLUMN_NAME_STUDENT_ID,
							row.getStudentID());
					initialValues.put(AttendanceEntry.COLUMN_NAME_COURSE_ID,
							row.getCourseID());
					initialValues.put(
							AttendanceEntry.COLUMN_NAME_ATTENDANCE_STATUS,
							row.getAttendanceStatus());

					rowId = mDB.insert(AttendanceEntry.TABLE_NAME, null,
							initialValues);

				}
				if (rowId == -1) {
					Toast.makeText(getApplicationContext(),
							"You already saved the Attendance for Today",
							Toast.LENGTH_LONG).show();
				}

			}

		});
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("EEEE");
		String dayOfTheWeek = format.format(c.getTime());
		
		if(dayOfTheWeek.equals("Friday") || dayOfTheWeek.equals("Saturday")){
			doneButton.setEnabled(false);
		}

		final Button dailyButton = (Button) findViewById(R.id.daily_report_student_attendance_button);

		dailyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				prefs = getSharedPreferences(REPORT_PREFS, MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(REPORT_KEY, "Daily");
				editor.commit();

				Intent studentAttendanceReportIntent = new Intent(
						getApplicationContext(),
						StudentAttendanceReportActivity.class);
				startActivity(studentAttendanceReportIntent);
				finish();
			}

		});

		final Button finalButton = (Button) findViewById(R.id.final_report_student_attendance_button);

		finalButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				prefs = getSharedPreferences(REPORT_PREFS, MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(REPORT_KEY, "Final");
				editor.commit();

				Intent studentParticipationReportIntent = new Intent(
						getApplicationContext(),
						StudentAttendanceReportActivity.class);
				startActivity(studentParticipationReportIntent);
				finish();
			}

		});
	}

	private AttendanceRecord getRecordFromCursor(Cursor cursor) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = format.format(c.getTime());

		String studentName = cursor.getString(cursor
				.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_NAME));
		int studentID = cursor.getInt(cursor
				.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_ID));
		int courseID = cursor.getInt(cursor
				.getColumnIndex(EnrollsEntry.COLUMN_NAME_COURSE_ID));
		
		Date myDate = null;
		try {
			myDate = format.parse(currentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new AttendanceRecord(myDate, studentID, studentName, courseID,
				"Absence");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_attendance, menu);
		return true;
	}

}
