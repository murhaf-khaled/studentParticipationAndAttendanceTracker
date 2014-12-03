package com.example.studentparticipationandattendancetracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.studentparticipationandattendancetracker.provider.UniContract.InstructorEntry;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileManagementActivity extends Activity {

	public static String INSTRUCTOR_ID_PREFS = "instructor_id_prefs";
	public static String INSTRUCTOR_ID_KEY = "instructor_id_key";

	SharedPreferences prefs;

	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;

	private EditText id;
	private EditText name;
	private EditText pass;
	private EditText re_pass;
	private EditText email;
	private Spinner department;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_management);

		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		mDB = mDbHelper.getWritableDatabase();
		
		prefs = getSharedPreferences(INSTRUCTOR_ID_PREFS, MODE_PRIVATE);
		int instructorID = prefs.getInt(INSTRUCTOR_ID_KEY, 0);

		id = (EditText) findViewById(R.id.prof_id_profile_management_editText);
		name = (EditText) findViewById(R.id.prof_name_profile_management_editText);
		pass = (EditText) findViewById(R.id.prof_new_password_profile_management_editText);
		re_pass = (EditText) findViewById(R.id.prof_rewrite_new_password_profile_management_editText);
		email = (EditText) findViewById(R.id.prof_email_profile_management_editText);
		department = (Spinner) findViewById(R.id.department_profile_management_spinner);

		Log.i("My Error", "profile 1");
		String[] DATA_ROWS = new String[] {
				InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID,
				InstructorEntry.COLUMN_NAME_INSTRUCTOR_NAME,
				InstructorEntry.COLUMN_NAME_INSTRUCTOR_PASSWORD,
				InstructorEntry.COLUMN_NAME_INSTRUCTOR_EMAIL,
				InstructorEntry.COLUMN_NAME_INSTRUCTOR_DEPARTMENT };

		Log.i("My Error", "profile 2");
		String selection = InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID + " = "+ prefs.getInt(
				INSTRUCTOR_ID_KEY, 0);

		Log.i("My Error", "profile 3");
		/*String[] selectionArgs = { String.valueOf(prefs.getInt(
				INSTRUCTOR_ID_KEY, 0)) };*/
		Log.i("My Error", "profile 4 : "+String.valueOf(prefs.getInt(
				INSTRUCTOR_ID_KEY, 0)));

		Cursor newCursor = mDB.query(InstructorEntry.TABLE_NAME, DATA_ROWS,
				selection, null, null, null, null);
		Log.i("My Error", "profile 5");

		if (null != newCursor) {
			if (newCursor.moveToFirst()) {
				Log.i("My Error", "profile 6");
				id.setText(String.valueOf(newCursor.getString(newCursor
						.getColumnIndex(InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID))));
				Log.i("My Error", "profile 7");
				name.setText(newCursor.getString(newCursor
						.getColumnIndex(InstructorEntry.COLUMN_NAME_INSTRUCTOR_NAME)));
				Log.i("My Error", "profile 8");
				String password = newCursor
						.getString(newCursor
								.getColumnIndex(InstructorEntry.COLUMN_NAME_INSTRUCTOR_PASSWORD));
				Log.i("My Error", "profile 9");
				pass.setText(password);
				re_pass.setText(password);
				Log.i("My Error", "profile 10");
				email.setText(newCursor.getString(newCursor
						.getColumnIndex(InstructorEntry.COLUMN_NAME_INSTRUCTOR_EMAIL)));
				Log.i("My Error", "profile 11");
				department
						.setSelection(newCursor
								.getString(
										newCursor
												.getColumnIndex(InstructorEntry.COLUMN_NAME_INSTRUCTOR_DEPARTMENT))
								.equals("CIS") ? 0 : 1);
				Log.i("My Error", "profile 12");
			}
		}
		// Save Button
		final Button saveButton = (Button) findViewById(R.id.save_profile_management_button);

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (passValidation()) {
					// write instructor info to the database
					// Create a new map of values, where column names are the
					// keys
					ContentValues values = new ContentValues();
					values.put(InstructorEntry.COLUMN_NAME_INSTRUCTOR_NAME,
							name.getText().toString());
					values.put(InstructorEntry.COLUMN_NAME_INSTRUCTOR_PASSWORD,
							md5(pass.getText().toString()));
					values.put(InstructorEntry.COLUMN_NAME_INSTRUCTOR_EMAIL,
							email.getText().toString());
					values.put(
							InstructorEntry.COLUMN_NAME_INSTRUCTOR_DEPARTMENT,
							department.getSelectedItem().toString());

					String selection = InstructorEntry.COLUMN_NAME_INSTRUCTOR_ID
							+ " = "
							+ String.valueOf(prefs.getInt(INSTRUCTOR_ID_KEY, 0));

					// Insert the new row, returning the primary key value of
					// the new row
					mDB.update(InstructorEntry.TABLE_NAME, values, selection,
							null);
					Intent coursesLoginIntent = new Intent(
							getApplicationContext(), CoursesLoginActivity.class);
					startActivity(coursesLoginIntent);
					finish();

				}

			}

		});

		// Cancel Button
		final Button cancelButton = (Button) findViewById(R.id.cancel_profile_management_button);

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent coursesLoginIntent = new Intent(getApplicationContext(),
						CoursesLoginActivity.class);
				startActivity(coursesLoginIntent);
				finish();
			}

		});
	}

	protected boolean passValidation() {
		String message = "";
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
		if (!pass.getText().toString().equals(re_pass.getText().toString())) {
			message += "\n Passwords NOT match";
		}
		if (message.equals("")) {
			return true;
		} else {
			Toast.makeText(ProfileManagementActivity.this,
					"plaese check the following : " + message,
					Toast.LENGTH_LONG).show();
			return false;
		}

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_management, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	protected void onPause() {
		mDB.close();
		super.onPause();
	}

}
