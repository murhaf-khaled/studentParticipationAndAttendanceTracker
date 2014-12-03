package com.example.studentparticipationandattendancetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.studentparticipationandattendancetracker.provider.UniContract.EnrollsEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.ParticipationEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.StudentEntry;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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

public class StudentParticipationReportActivity extends Activity {

	public static String CURRENT_COURSE_ID_PREFS = "current_course_id_prefs";
	public static String CURRENT_COURSE_ID_KEY = "current_course_id_key";

	public static String REPORT_PREFS = "report_prefs";
	public static String REPORT_KEY = "report_key";

	SharedPreferences prefs;

	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;

	private ParticipationReportViewAdapter mCursorAdapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_participation_report);

		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getReadableDatabase();

		mCursorAdapter = new ParticipationReportViewAdapter(this, null, 0,
				mDbHelper);

		listView = (ListView) findViewById(R.id.participation_report_LV);
		// Assign adapter to ListView
		listView.setAdapter(mCursorAdapter);

		// Done Button
		final Button doneButton = (Button) findViewById(R.id.done_student_participation_report_button);

		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent studentParticipationIntent = new Intent(
						getApplicationContext(),
						StudentParticipationActivity.class);
				startActivity(studentParticipationIntent);
				finish();
			}

		});

		prefs = getSharedPreferences(CURRENT_COURSE_ID_PREFS, MODE_PRIVATE);
		int currentCourseID = prefs.getInt(CURRENT_COURSE_ID_KEY, 0);
		prefs = getSharedPreferences(REPORT_PREFS, MODE_PRIVATE);

		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = format.format(c.getTime());
		String sql = null;
		if (prefs.getString(REPORT_KEY, "").equals("Daily")) {
			sql = "SELECT st.student_Name, part.student_ID, part.participation_Mark AS Mark FROM Enrolls AS en LEFT JOIN Student AS st ON st.student_ID = en.student_ID LEFT JOIN Participation AS part ON part.student_ID = en.student_ID WHERE part.participation_Date = '"
					+ currentDate
					+ "'"
					+ " AND part.course_ID = "
					+ currentCourseID;
		} else {
			sql = "SELECT st.student_Name, part.student_ID,AVG(part.participation_Mark)*4 AS Mark FROM Enrolls AS en LEFT JOIN Student AS st ON st.student_ID = en.student_ID LEFT JOIN Participation AS part ON part.student_ID = en.student_ID WHERE part.course_ID = "
					+ currentCourseID + " GROUP BY part.student_ID";
		}

		Cursor newCursor = mDB.rawQuery(sql, null);
		
		if (null != newCursor) {
			if (newCursor.moveToFirst()) {
				do {
					mCursorAdapter.addToList(getRecordFromCursor(newCursor));

					mCursorAdapter.notifyDataSetChanged();
					
				} while (newCursor.moveToNext() == true);
			}
		}

	}

	private ParticipationRecord getRecordFromCursor(Cursor cursor) {
		String studentName = cursor.getString(cursor
				.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_NAME));
		int studentID = cursor.getInt(cursor
				.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_ID));
		
		int count = cursor.getInt(cursor.getColumnIndex("Mark"));
		
		return new ParticipationRecord(studentID, studentName, count);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_participation_report, menu);
		return true;
	}

}
