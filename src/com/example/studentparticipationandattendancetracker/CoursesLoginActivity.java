package com.example.studentparticipationandattendancetracker;

import com.example.studentparticipationandattendancetracker.provider.UniContract.CourseEntry;

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

public class CoursesLoginActivity extends Activity {

	public static String INSTRUCTOR_ID_PREFS = "instructor_id_prefs";
	public static String INSTRUCTOR_ID_KEY = "instructor_id_key";
	
	private static String INSTRUCTOR_ID = "instructor_id";
	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;
	
	SharedPreferences prefs;
	private CourseLoginViewAdapter mCursorAdapter;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses_login);
		
		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getReadableDatabase();
		
		
		mCursorAdapter = new CourseLoginViewAdapter(this, null, 0, mDbHelper);
		
		listView = (ListView) findViewById(R.id.Courses_login_listView);
		  // Assign adapter to ListView
		listView.setAdapter(mCursorAdapter);
		
		prefs = getSharedPreferences(INSTRUCTOR_ID_PREFS, MODE_PRIVATE);
		
		String[] DATA_ROWS = new String[] { CourseEntry.COLUMN_NAME_COURSE_ID,
				CourseEntry.COLUMN_NAME_INSTRUCTOR_ID,
				CourseEntry.COLUMN_NAME_COURSE_NAME,
				CourseEntry.COLUMN_NAME_COURSE_DAYS };
		
		String selection = CourseEntry.COLUMN_NAME_INSTRUCTOR_ID + " =? ";
			
		String[] selectionArgs = {String.valueOf(prefs.getInt(INSTRUCTOR_ID_KEY, 0))};
		
		Cursor newCursor = mDB.query(CourseEntry.TABLE_NAME, DATA_ROWS, selection, selectionArgs, null, null, null);

		if (null != newCursor) {
			if (newCursor.moveToFirst()) {
				do {
					mCursorAdapter.addToList(getCourseRecordFromCursor(newCursor));
					mCursorAdapter.notifyDataSetChanged();
				} while (newCursor.moveToNext() == true);
			}
		}
		  
		final Button manageButton = (Button) findViewById(R.id.manage_courses_login_button);
		
		manageButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent coursesSignuptIntent = new Intent(getApplicationContext(), CoursesSignupActivity.class);
				startActivity(coursesSignuptIntent);
				finish();
			}
			
		});
		
		
		final Button profileButton = (Button) findViewById(R.id.profile_courses_login_button);
		
		profileButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent profileManagementIntent = new Intent(getApplicationContext(), ProfileManagementActivity.class);
				startActivity(profileManagementIntent);
				finish();
			}
			
		});
		
		final Button logoutButton = (Button) findViewById(R.id.logout_courses_login_button);
		
		logoutButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
	}

	private CourseRecord getCourseRecordFromCursor(Cursor cursor) {
		int courseID = cursor.getInt(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_ID));
		int instructorID = cursor.getInt(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_INSTRUCTOR_ID));
		String courseName = cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_NAME));		
		//String courseDays = cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_DAYS));
		//boolean selected = list.get(cursor.getPosition()).isSelected();

		return new CourseRecord(courseID, instructorID, courseName, "", false);
	}
	
	@Override
	protected void onPause() {
		mDB.close();
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.courses_login, menu);
		return true;
	}

}
