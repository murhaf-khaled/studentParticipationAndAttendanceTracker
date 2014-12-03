package com.example.studentparticipationandattendancetracker;

import com.example.studentparticipationandattendancetracker.provider.UniContract.CourseEntry;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.LoaderManager;
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
import android.widget.ListView;

public class CoursesSignupActivity extends Activity {
	
	public static String INSTRUCTOR_ID_PREFS = "instructor_id_prefs";
	public static String INSTRUCTOR_ID_KEY = "instructor_id_key";
	
	private static String INSTRUCTOR_ID = "instructor_id";
	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;
	
	SharedPreferences prefs;
	private CourseSignupViewAdapter mCursorAdapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses_signup);

		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getReadableDatabase();
		
		
		mCursorAdapter = new CourseSignupViewAdapter(this, null, 0, mDbHelper);
		
		listView = (ListView) findViewById(R.id.Courses_signup_listView);
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
		  
		final Button finishButton = (Button) findViewById(R.id.finish_courses_signup_button);
		
		finishButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent coursesLogintIntent = new Intent(getApplicationContext(), CoursesLoginActivity.class);
				startActivity(coursesLogintIntent);
				finish();
			}
			
		});
		
		final Button updateButton = (Button) findViewById(R.id.update_courses_signup_button);
		
		updateButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent coursesManagementIntent = new Intent(getApplicationContext(), CourseManagementActivity.class);
				startActivity(coursesManagementIntent);
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
		getMenuInflater().inflate(R.menu.courses_signup, menu);
		return true;
	}

	/*@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// Create a new CursorLoader and return it

		Log.i("My Error", "onCreateLoader >>>>>>>>>>>>>");
		
		String[] DATA_ROWS = new String[] { CourseEntry.COLUMN_NAME_COURSE_ID,
				CourseEntry.COLUMN_NAME_INSTRUCTOR_ID,
				CourseEntry.COLUMN_NAME_COURSE_NAME,
				CourseEntry.COLUMN_NAME_COURSE_DAYS };
		prefs = getSharedPreferences(SignupActivity.INSTRUCTOR_ID_PREFS, MODE_PRIVATE);
		
		String selection = CourseEntry.COLUMN_NAME_INSTRUCTOR_ID + " = " + prefs.getInt(SignupActivity.INSTRUCTOR_ID_KEY, 0);
		
		
		Log.i("My Error", "onCreateLoader >>>>>>>>>>prefs: "+prefs.getInt(SignupActivity.INSTRUCTOR_ID_KEY, 0));
		
		String[] selectionArgs = {String.valueOf(prefs.getInt(SignupActivity.INSTRUCTOR_ID_KEY, 0))};

		Log.i("My Error", "onCreateLoader >>>>>>>>>>>>> " + Uri.withAppendedPath(CourseEntry.CONTENT_URI, CourseEntry.TABLE_NAME).toString());
		
		return new CursorLoader(getApplicationContext(),
				CourseEntry.CONTENT_URI, DATA_ROWS, selection, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor newCursor) {
		Log.i("My Error", "onLoadFinished before >>>>>>>>>>>>>");
		mCursorAdapter.swapCursor(newCursor);
		Log.i("My Error", "onLoadFinished After >>>>>>>>>>>>>");
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mCursorAdapter.swapCursor(null);
		
	}*/

}
