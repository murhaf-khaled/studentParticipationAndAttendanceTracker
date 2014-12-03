package com.example.studentparticipationandattendancetracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.studentparticipationandattendancetracker.provider.UniContract.InstructorEntry;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignupActivity extends Activity {
	
	private EditText id;
	private EditText name;
	private EditText pass;
	private EditText re_pass;
	private EditText email;
	private Spinner department;
	
	public static String INSTRUCTOR_ID_PREFS = "instructor_id_prefs";
	public static String INSTRUCTOR_ID_KEY = "instructor_id_key";
	
	private DatabaseOpenHelper mDbHelper;
	private SQLiteDatabase mDB = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		id = (EditText) findViewById(R.id.prof_id_editText);
		name = (EditText) findViewById(R.id.prof_name_editText);
		pass = (EditText) findViewById(R.id.prof_password_editText);
		re_pass = (EditText) findViewById(R.id.prof_rewrite_password_editText);
		email = (EditText) findViewById(R.id.prof_email_editText);
		department = (Spinner) findViewById(R.id.department_spinner);
		
		final Button backButton = (Button) findViewById(R.id.back_signup_button);
		
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(loginIntent);
				finish();
			}
			
		});
		
		final Button okButton = (Button) findViewById(R.id.ok_signup_button);
		
		okButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(passValidation()){
					// write instructor info to the database
					// Create a new map of values, where column names are the keys
					ContentValues values = new ContentValues();
					values.put(InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID,
							Integer.valueOf(id.getText().toString()));
					values.put(InstructorEntry.COLUMN_NAME_INSTRUCTOR_NAME,
							name.getText().toString());
					values.put(InstructorEntry.COLUMN_NAME_INSTRUCTOR_PASSWORD,
							md5(pass.getText().toString()));
					values.put(InstructorEntry.COLUMN_NAME_INSTRUCTOR_EMAIL,
							email.getText().toString());
					values.put(
							InstructorEntry.COLUMN_NAME_INSTRUCTOR_DEPARTMENT,
							department.getSelectedItem().toString());
					
					// Insert the new row, returning the primary key value of the new row
					long newRowId = mDB.insert(
					         InstructorEntry.TABLE_NAME,
					         null,
					         values);
					if(newRowId != -1){
						
						// SharedPreferences
						final SharedPreferences prefs = getSharedPreferences(INSTRUCTOR_ID_PREFS, MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putInt(INSTRUCTOR_ID_KEY, (int) newRowId);
						editor.commit();
						
						Toast.makeText(SignupActivity.this,
								"Your ID is : ( "+newRowId+" )",
								Toast.LENGTH_LONG).show();
						Intent courseManagementIntent = new Intent(getApplicationContext(), CourseManagementActivity.class);
						startActivity(courseManagementIntent);
						finish();
					}
					else{
						Toast.makeText(SignupActivity.this,
								"Your ID is already selected",
								Toast.LENGTH_LONG).show();
					}
					
				}
				
			}
			
		});
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

	protected boolean passValidation() {
		String message ="";
		if (name.getText().toString().equals("")) {
			message += "Name, ";
		}
		if (pass.getText().toString().equals("")) {
			message += "Password, ";
		}
		if (re_pass.getText().toString().equals("")) {
			message += "Rewrite Password, ";
		}
		if (email.getText().toString().equals("")) {
			message += "Email, ";
		}
		if (department.getSelectedItem() == null) {
			message += "Department, ";
		}
		if(! pass.getText().toString().equals(re_pass.getText().toString())){
			message += "\n Passwords NOT match";
		}
		if(message.equals("")){
			return true;
		}
		else{
			Toast.makeText(SignupActivity.this,
					"plaese check the following : "+message,
					Toast.LENGTH_LONG).show();
			return false;
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getWritableDatabase();
	}

	@Override
	protected void onPause() {
		mDB.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
		return true;
	}

}
