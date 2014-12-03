package com.example.studentparticipationandattendancetracker;

import java.util.ArrayList;

import com.example.studentparticipationandattendancetracker.provider.UniContract.CourseEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.InstructorEntry;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

public class CourseManagementActivity extends Activity {
	
	public static String INSTRUCTOR_ID_PREFS = "instructor_id_prefs";
	public static String INSTRUCTOR_ID_KEY = "instructor_id_key";
	
	private static String INSTRUCTOR_ID = "instructor_id";
	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;
	
	SharedPreferences prefs;
	private CourseViewAdapter mCursorAdapter;
	private ListView listView;
	
	public static EditText courseID_EditText;
	public static EditText courseName_EditText;
	public static Spinner courseDays_Spinner;
	
	public static String temp_courseID = "";
	public static String temp_courseName = "";
	public static String temp_courseDays = "";
	
	//final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignupActivity);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_management);
		
		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getReadableDatabase();
		
		mCursorAdapter = new CourseViewAdapter(getApplicationContext(), null, 0, mDbHelper);
		
		listView = (ListView) findViewById(R.id.courseListView);
		// Assign adapter to ListView
		listView.setAdapter(mCursorAdapter);
		  
		prefs = getSharedPreferences(INSTRUCTOR_ID_PREFS, MODE_PRIVATE);
		
		String[] DATA_ROWS = new String[] { CourseEntry.COLUMN_NAME_COURSE_ID,
				CourseEntry.COLUMN_NAME_INSTRUCTOR_ID,
				CourseEntry.COLUMN_NAME_COURSE_NAME,
				CourseEntry.COLUMN_NAME_COURSE_DAYS };
		
		String selection = CourseEntry.COLUMN_NAME_INSTRUCTOR_ID + " =? ";
			
		String[] selectionArgs = {String.valueOf(prefs.getInt(SignupActivity.INSTRUCTOR_ID_KEY, 0))};
		
		Cursor newCursor = mDB.query(CourseEntry.TABLE_NAME, DATA_ROWS, selection, selectionArgs, null, null, null);

		if (null != newCursor) {
			if (newCursor.moveToFirst()) {
				do {
					mCursorAdapter.addToList(getRecordFromCursor(newCursor));
					mCursorAdapter.notifyDataSetChanged();
				} while (newCursor.moveToNext() == true);
			}
		}  
		  
		  
		//==========================================================
		
		courseID_EditText = (EditText) findViewById(R.id.course_id_editText);
		courseName_EditText = (EditText) findViewById(R.id.course_name_editText);
		courseDays_Spinner = (Spinner) findViewById(R.id.course_days);
		
		
		// back Button
		final Button backButton = (Button) findViewById(R.id.back_course_management_button);
		
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(loginIntent);
				finish();
			}
			
		});
		
		// Finish Button
		final Button finishButton = (Button) findViewById(R.id.finish_course_management_button);
		
		finishButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent CoursesSignupIntent = new Intent(getApplicationContext(), CoursesSignupActivity.class);
				startActivity(CoursesSignupIntent);
				finish();
			}
			
		});
		
		// Add Course Button
		final Button addButton = (Button) findViewById(R.id.add_course_management_button);
		
		addButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
							
				if (passValidation()) {
					// write instructor info to the database
					// Create a new map of values, where column names are the keys
					CourseRecord row = new CourseRecord(
							Integer.valueOf(courseID_EditText.getText()
									.toString()), Integer.valueOf(prefs.getInt(
									SignupActivity.INSTRUCTOR_ID_KEY, 0)),
							courseName_EditText.getText().toString(),
							courseDays_Spinner.getSelectedItem().toString(),
							false);
					if (mCursorAdapter.add(row)) {
						mCursorAdapter.notifyDataSetChanged();
					}
				}
				
			}
			
		});
		
		// Remove Course Button
		final Button removeButton = (Button) findViewById(R.id.remove_course_management_button);
		
		removeButton.setOnClickListener(new OnClickListener(){

			private ArrayList<CourseRecord> list;

			@Override
			public void onClick(View v) {
				list = mCursorAdapter.getList();
				if(! list.isEmpty()){
					
					for(CourseRecord row : list){
						
						if(row.isSelected()){
							if(mCursorAdapter.remove(row)){
								// TODO add a new course to the list Adapter	
								//listView.setAdapter(mCursorAdapter);
								mCursorAdapter.notifyDataSetChanged();
								break;
							}
						}
						
					}
				}
					
			}
			
		});
		
		// Update Course Button
		final Button updateButton = (Button) findViewById(R.id.update_course_management_button);

		updateButton.setOnClickListener(new OnClickListener() {

			private ArrayList<CourseRecord> list;

			@Override
			public void onClick(View v) {
				list = mCursorAdapter.getList();
				
				temp_courseName = courseName_EditText.getText().toString();
				temp_courseDays = courseDays_Spinner.getSelectedItem().toString();

				
				if (!list.isEmpty()) {
					
					
					for (CourseRecord row : list) {
						
						if (row.isSelected()) {
							
							if (!(row.getCourseName().equals(temp_courseName)) || !(row
									.getCourseDays().equals(temp_courseDays))) {
								

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
		if (courseID_EditText.getText().toString().equals("")) {
			message += "Course ID, ";
		}
		if (courseName_EditText.getText().toString().equals("")) {
			message += "Course Name, ";
		}
		
		if(message.equals("")){
			return true;
		}
		else{
			Toast.makeText(CourseManagementActivity.this,
					"plaese enter the following : "+message,
					Toast.LENGTH_LONG).show();
			return false;
		}
		
	}
	
	private CourseRecord getRecordFromCursor(Cursor cursor) {
		int courseID = cursor.getInt(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_ID));
		int instructorID = cursor.getInt(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_INSTRUCTOR_ID));
		String courseName = cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_NAME));		
		String courseDays = cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_DAYS));
		//boolean selected = list.get(cursor.getPosition()).isSelected();

		return new CourseRecord(courseID, instructorID, courseName, courseDays, false);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		/*mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getReadableDatabase();*/
	}

	@Override
	protected void onPause() {
		mDB.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course_management, menu);
		return true;
	}

	/*@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// Create a new CursorLoader and return it		
		
		String[] DATA_ROWS = new String[] { CourseEntry.COLUMN_NAME_COURSE_ID,
				CourseEntry.COLUMN_NAME_INSTRUCTOR_ID,
				CourseEntry.COLUMN_NAME_COURSE_NAME,
				CourseEntry.COLUMN_NAME_COURSE_DAYS };
		
		return new CursorLoader(getApplicationContext(),
				CourseEntry.CONTENT_URI, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor newCursor) {
		
		mCursorAdapter.swapCursor(newCursor);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
		mCursorAdapter.swapCursor(null);
		
	}*/

}
