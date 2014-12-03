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
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StudentAttendanceReportActivity extends Activity {

	public static String REPORT_PREFS = "report_prefs";
	public static String REPORT_KEY = "report_key";

	public static String CURRENT_COURSE_ID_PREFS = "current_course_id_prefs";
	public static String CURRENT_COURSE_ID_KEY = "current_course_id_key";

	SharedPreferences prefs;

	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;

	private AttendanceReportViewAdapter mCursorAdapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_attendance_report);

		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getReadableDatabase();

		mCursorAdapter = new AttendanceReportViewAdapter(this, null, 0,
				mDbHelper);

		listView = (ListView) findViewById(R.id.attendance_report_LV);
		// Assign adapter to ListView
		listView.setAdapter(mCursorAdapter);

		// Done Button
		final Button doneButton = (Button) findViewById(R.id.done_student_attendance_report_button);

		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent studentAttendanceIntent = new Intent(
						getApplicationContext(),
						StudentAttendanceActivity.class);
				startActivity(studentAttendanceIntent);
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
			TextView status = (TextView)findViewById(R.id.student_rank_student_attendance_report_header);
			status.setText("Status");
			sql = "SELECT st.student_Name, att.student_ID, CASE WHEN att.attendance_Status = 'Absence' THEN 0 ELSE 1 END as Salable FROM Enrolls AS en LEFT JOIN Student AS st ON st.student_ID = en.student_ID LEFT JOIN Attendance AS att ON att.student_ID = en.student_ID WHERE att.attendance_Date = '"+ currentDate +"'"+" AND att.course_ID = "+ currentCourseID;
		} else {
			TextView status = (TextView)findViewById(R.id.student_rank_student_attendance_report_header);
			status.setText("Absence");
			sql = "SELECT st.student_Name, att.student_ID,COUNT(*) as Salable FROM Enrolls AS en LEFT JOIN Student AS st ON st.student_ID = en.student_ID LEFT JOIN Attendance AS att ON att.student_ID = en.student_ID WHERE att.attendance_Status = 'Absence' AND att.course_ID = "+ currentCourseID +" GROUP BY att.student_ID";
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

	private AttendanceRecord getRecordFromCursor(Cursor cursor) {
		String studentName = cursor.getString(cursor
				.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_NAME));
		int studentID = cursor.getInt(cursor
				.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_ID));
		int count = cursor.getInt(cursor
				.getColumnIndex("Salable"));

		return new AttendanceRecord(studentID, studentName, count);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_attendance_report, menu);
		return true;
	}

}
