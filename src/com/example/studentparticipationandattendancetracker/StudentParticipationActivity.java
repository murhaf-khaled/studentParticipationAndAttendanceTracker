package com.example.studentparticipationandattendancetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.example.studentparticipationandattendancetracker.provider.UniContract.CourseEntry;
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

public class StudentParticipationActivity extends Activity {

	public static String CURRENT_COURSE_ID_PREFS = "current_course_id_prefs";
	public static String CURRENT_COURSE_ID_KEY = "current_course_id_key";

	public static String REPORT_PREFS = "report_prefs";
	public static String REPORT_KEY = "report_key";

	SharedPreferences prefs;

	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;

	private ParticipationViewAdapter mCursorAdapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_participation);

		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getReadableDatabase();

		mCursorAdapter = new ParticipationViewAdapter(this, null, 0, mDbHelper);

		listView = (ListView) findViewById(R.id.studentParticipationListView);
		// Assign adapter to ListView
		listView.setAdapter(mCursorAdapter);
		prefs = getSharedPreferences(CURRENT_COURSE_ID_PREFS, MODE_PRIVATE);
		int currentCourseID = prefs.getInt(CURRENT_COURSE_ID_KEY, 0);

		String sql = "SELECT Student.student_ID, student_Name, course_ID FROM Student JOIN Enrolls ON Student.student_ID = Enrolls.student_ID WHERE course_ID = "+currentCourseID;

		Cursor newCursor = mDB.rawQuery(sql, null);
		
		if (null != newCursor) {
			if (newCursor.moveToFirst()) {
				do {
					mCursorAdapter.addToList(getRecordFromCursor(newCursor));
					mCursorAdapter.notifyDataSetChanged();
					
				} while (newCursor.moveToNext() == true);
			}
		}

		// Back Button
		final Button backButton = (Button) findViewById(R.id.back_student_participation_button);

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent coursesLoginIntent = new Intent(
						getApplicationContext(),
						CoursesLoginActivity.class);
				startActivity(coursesLoginIntent);
				finish();
			}

		});

		// Done Button
		final Button doneButton = (Button) findViewById(R.id.done_student_participation_button);

		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				long rowId = -1;
				
				Calendar c = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String currentDate = format.format(c.getTime());
			
				for (ParticipationRecord row : mCursorAdapter.getList()) {
					
					ContentValues initialValues = new ContentValues(); 
					initialValues.put(ParticipationEntry.COLUMN_NAME_PARTICIPATION_DATE, currentDate);
					initialValues.put(ParticipationEntry.COLUMN_NAME_STUDENT_ID, row.getStudentID());
					initialValues.put(ParticipationEntry.COLUMN_NAME_COURSE_ID, row.getCourseID());
					initialValues.put(ParticipationEntry.COLUMN_NAME_PARTICIPATION_MARK, row.getPartMark());
					
					rowId = mDB.insert(ParticipationEntry.TABLE_NAME, null, initialValues);
					
				}
				if(rowId == -1){
					Toast.makeText(getApplicationContext(), "You already saved the participation for Today", Toast.LENGTH_LONG).show();
				}
				
			}

		});
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("EEEE");
		String dayOfTheWeek = format.format(c.getTime());
		
		if(dayOfTheWeek.equals("Friday") || dayOfTheWeek.equals("Saturday")){
			doneButton.setEnabled(false);
		}

		final Button dailyButton = (Button) findViewById(R.id.daily_report_student_participation_button);

		dailyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				prefs = getSharedPreferences(REPORT_PREFS, MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(REPORT_KEY, "Daily");
				editor.commit();

				Intent studentParticipationReportIntent = new Intent(
						getApplicationContext(),
						StudentParticipationReportActivity.class);
				startActivity(studentParticipationReportIntent);
				finish();
			}

		});

		final Button finalButton = (Button) findViewById(R.id.final_report_student_participation_button);

		finalButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				prefs = getSharedPreferences(REPORT_PREFS, MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(REPORT_KEY, "Final");
				editor.commit();

				Intent studentParticipationReportIntent = new Intent(
						getApplicationContext(),
						StudentParticipationReportActivity.class);
				startActivity(studentParticipationReportIntent);
				finish();
			}

		});

		final Button randomButton = (Button) findViewById(R.id.random_student_participation_button);

		randomButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Random r = new Random();
				int pos = r.nextInt(mCursorAdapter.getCount());
				boolean isThereMoreStudent = true;
				while (isThereMoreStudent) {
					if (mCursorAdapter.getList().get(pos).getPartMark() == 0) {
						listView.smoothScrollToPosition(pos);
						Toast.makeText(
								getApplicationContext(),
								"The selected Student : "
										+ mCursorAdapter.getList().get(pos)
												.getStudentName(),
								Toast.LENGTH_LONG).show();
						break;
					}
				}
			}

		});

		final Button studentAttendanceButton = (Button) findViewById(R.id.student_attendance_student_participation_button);

		studentAttendanceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent studentAttendanceIntent = new Intent(
						getApplicationContext(),
						StudentAttendanceActivity.class);
				startActivity(studentAttendanceIntent);
				finish();
			}

		});
	}

	private ParticipationRecord getRecordFromCursor(Cursor cursor) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = format.format(c.getTime());
		
		int studentID = cursor.getInt(cursor
				.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_ID));
		String studentName = cursor.getString(cursor
				.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_NAME));
		int courseID = cursor.getInt(cursor
				.getColumnIndex(EnrollsEntry.COLUMN_NAME_COURSE_ID));

		Date myDate = null;
		try {
			myDate = format.parse(currentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ParticipationRecord(myDate, studentID, studentName,
				courseID, 0);
	}

	@Override
	protected void onPause() {
		mDB.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_participation, menu);
		return true;
	}

}
