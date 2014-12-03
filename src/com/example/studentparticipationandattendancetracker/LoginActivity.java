package com.example.studentparticipationandattendancetracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.studentparticipationandattendancetracker.provider.UniContract.InstructorEntry;

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
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static String INSTRUCTOR_ID = "instructor_id";
	private DatabaseOpenHelper mDbHelper;
	private SQLiteDatabase mDB = null;
	
	public static String INSTRUCTOR_ID_PREFS = "instructor_id_prefs";
	public static String INSTRUCTOR_ID_KEY = "instructor_id_key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		
		final EditText userName = (EditText) findViewById(R.id.user_name_value);

		final EditText userPassword = (EditText) findViewById(R.id.pasword_value);
		
		final Button loginButton = (Button) findViewById(R.id.loginButton);
		
		loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				//mDB.query("Instructor", new String[] {"instructor_ID","instructor_Name"}, null, new String[]{}, null, null, null);
				Cursor cursor = InstructorIsExist(userName.getText().toString());
				if(cursor != null){
					cursor.moveToFirst();
					int instructorId = cursor.getInt(
					    cursor.getColumnIndexOrThrow(InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID)
					);
					
					String instructorPassword = cursor.getString(
						    cursor.getColumnIndexOrThrow(InstructorEntry.COLUMN_NAME_INSTRUCTOR_PASSWORD)
						);
					
					if(instructorPassword.equals(md5(userPassword.getText().toString()))){
						// SharedPreferences
						final SharedPreferences prefs = getSharedPreferences(INSTRUCTOR_ID_PREFS, MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putInt(INSTRUCTOR_ID_KEY, instructorId);
						editor.commit();
						
						/*final SharedPreferences prefs = getPreferences(MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putInt(INSTRUCTOR_ID, instructorId);
						editor.commit();*/
						
						Intent coursesLogintIntent = new Intent(getApplicationContext(), CoursesLoginActivity.class);
						startActivity(coursesLogintIntent);
						finish();
					}
					else{
						Toast.makeText(LoginActivity.this,
								"please check your password",
								Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(LoginActivity.this,
							"your user name is NOT Exist",
							Toast.LENGTH_LONG).show();
				}
				
			}
			
		});
		
		final Button signupButton = (Button) findViewById(R.id.signupButton);
		
		signupButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent signupIntent = new Intent(getApplicationContext(), SignupActivity.class);
				startActivity(signupIntent);
				finish();
			}
			
		});
		
	}
	
	private Cursor InstructorIsExist(String id) {
		Cursor c =null;
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID,
		    InstructorEntry.COLUMN_NAME_INSTRUCTOR_NAME,
		    InstructorEntry.COLUMN_NAME_INSTRUCTOR_PASSWORD,
		    InstructorEntry.COLUMN_NAME_INSTRUCTOR_EMAIL,
		    InstructorEntry.COLUMN_NAME_INSTRUCTOR_DEPARTMENT,
		    };
		
		String selection = InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID + "=?";
		
		String[] selectionArgs = {id};
		
		c = mDB.query(
				InstructorEntry.TABLE_NAME, 		// The table to query
				projection, 						// The columns to return
				selection, 							// The columns for the WHERE clause
				selectionArgs, 						// The values for the WHERE clause
				null, 								// don't group the rows
				null, 								// don't filter by row groups
				null 								// don't sort by order
				);
		if(c.getCount() == 0){
			c=null;
		}
		return c;
	}
	
	public String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getReadableDatabase();
	}

	@Override
	protected void onPause() {
		mDB.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}

}
