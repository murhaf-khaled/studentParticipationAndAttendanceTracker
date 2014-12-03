package com.example.studentparticipationandattendancetracker;

import java.util.ArrayList;

import com.example.studentparticipationandattendancetracker.provider.UniContract.CourseEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.StudentEntry;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class StudentManagementActivity extends Activity {

	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;

	private StudentViewAdapter mCursorAdapter;
	private ListView listView;

	public static String temp_studentID = "";
	public static String temp_studentName = "";
	public static String temp_studentEmail = "";

	public static EditText studentID_EditText;
	public static EditText studentName_EditText;
	public static EditText studentEmail_EditText;
	
	SharedPreferences prefs;
	public static String CURRENT_COURSE_ID_PREFS = "current_course_id_prefs";
	public static String CURRENT_COURSE_ID_KEY = "current_course_id_key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_management);

		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getReadableDatabase();

		studentID_EditText = (EditText) findViewById(R.id.student_id_editText);
		studentName_EditText = (EditText) findViewById(R.id.student_name_editText);
		studentEmail_EditText = (EditText) findViewById(R.id.student_email_editText);

		mCursorAdapter = new StudentViewAdapter(getApplicationContext(), null,
				0, mDbHelper);

		listView = (ListView) findViewById(R.id.studentListView);
		// Assign adapter to ListView
		listView.setAdapter(mCursorAdapter);
		
		prefs = getSharedPreferences(CURRENT_COURSE_ID_PREFS, MODE_PRIVATE);
		int currentCourseID = prefs.getInt(CURRENT_COURSE_ID_KEY, 0);
		
		String sql = "SELECT Student.student_ID, student_Name, student_Email FROM Student JOIN Enrolls ON Student.student_ID = Enrolls.student_ID WHERE course_ID = "+currentCourseID;

		
		Cursor newCursor = mDB.rawQuery(sql, null);
		
/*		String[] DATA_ROWS = new String[] { StudentEntry.COLUMN_NAME_STUDENT_ID,
				StudentEntry.COLUMN_NAME_STUDENT_NAME,
				StudentEntry.COLUMN_NAME_STUDENT_EMAIL };
		
		String selection = CourseEntry.COLUMN_NAME_INSTRUCTOR_ID + " =? ";
			
		String[] selectionArgs = {String.valueOf(prefs.getInt(CURRENT_COURSE_ID_KEY, 0))};
		
		Cursor newCursor = mDB.query(StudentEntry.TABLE_NAME, DATA_ROWS, selection, selectionArgs, null, null, null);
*/
		if (null != newCursor) {
			if (newCursor.moveToFirst()) {
				do {
					mCursorAdapter.addToList(getRecordFromCursor(newCursor));
					mCursorAdapter.notifyDataSetChanged();
				} while (newCursor.moveToNext() == true);
			}
		}  

		// Back Button
		final Button backButton = (Button) findViewById(R.id.back_student_management_button);

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent CoursesSignupIntent = new Intent(
						getApplicationContext(), CoursesSignupActivity.class);
				startActivity(CoursesSignupIntent);
				finish();
			}

		});

		// Finish Button
		final Button finishButton = (Button) findViewById(R.id.finish_student_management_button);

		finishButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent CoursesSignupIntent = new Intent(
						getApplicationContext(), CoursesSignupActivity.class);
				startActivity(CoursesSignupIntent);
				finish();
			}

		});

		// Add Student Button
		final Button addButton = (Button) findViewById(R.id.add_student_management_button);

		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (passValidation()) {
					StudentRecord row = new StudentRecord(
							Integer.valueOf(studentID_EditText.getText()
									.toString()), studentName_EditText
									.getText().toString(),
							studentEmail_EditText.getText().toString(), false);
					if (mCursorAdapter.add(row)) {
						mCursorAdapter.notifyDataSetChanged();
					}
				}

			}

		});

		// Remove Student Button
		final Button removeButton = (Button) findViewById(R.id.remove_student_management_button);

		removeButton.setOnClickListener(new OnClickListener() {

			private ArrayList<StudentRecord> list;

			@Override
			public void onClick(View v) {
				list = mCursorAdapter.getList();
				if (!list.isEmpty()) {

					for (StudentRecord row : list) {

						if (row.isSelected()) {
							if (mCursorAdapter.remove(row)) {
								// TODO add a new course to the list Adapter
								// listView.setAdapter(mCursorAdapter);
								mCursorAdapter.notifyDataSetChanged();
								break;
							}
						}

					}
				}

			}

		});

		// Update Student Button
		final Button updateButton = (Button) findViewById(R.id.update_student_management_button);

		updateButton.setOnClickListener(new OnClickListener() {

			private ArrayList<StudentRecord> list;

			@Override
			public void onClick(View v) {
				list = mCursorAdapter.getList();

				temp_studentName = studentName_EditText.getText().toString();
				temp_studentEmail = studentEmail_EditText.getText().toString();

				if (!list.isEmpty()) {

					for (StudentRecord row : list) {

						if (row.isSelected()) {

							if (!(row.getStudentName().equals(temp_studentName))
									|| !(row.getStudentEmail()
											.equals(temp_studentEmail))) {

								if (mCursorAdapter.update(row)) {
									mCursorAdapter.notifyDataSetChanged();
									break;
								}
							}

						}

					}
				}

			}

		});
	}
	
	protected boolean passValidation() {
		String message ="";
		if (studentID_EditText.getText().toString().equals("")) {
			message += "Student ID, ";
		}
		if (studentName_EditText.getText().toString().equals("")) {
			message += "Student Name, ";
		}
		if (studentEmail_EditText.getText().toString().equals("")) {
			message += "Student Email, ";
		}
		
		if(message.equals("")){
			return true;
		}
		else{
			Toast.makeText(StudentManagementActivity.this,
					"plaese enter the following : "+message,
					Toast.LENGTH_LONG).show();
			return false;
		}
		
	}
	
	private StudentRecord getRecordFromCursor(Cursor cursor) {
		int studentID = cursor.getInt(cursor.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_ID));
		String studentName = cursor.getString(cursor.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_NAME));
		String studentEmail = cursor.getString(cursor.getColumnIndex(StudentEntry.COLUMN_NAME_STUDENT_EMAIL));		

		return new StudentRecord(studentID, studentName, studentEmail, false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * mDbHelper = new DatabaseOpenHelper(getApplicationContext()); mDB =
		 * mDbHelper.getReadableDatabase();
		 */
	}

	@Override
	protected void onPause() {
		mDB.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_management, menu);
		return true;
	}

}
