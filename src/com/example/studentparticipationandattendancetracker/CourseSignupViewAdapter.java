package com.example.studentparticipationandattendancetracker;

import java.util.ArrayList;

import com.example.studentparticipationandattendancetracker.provider.UniContract.CourseEntry;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CourseSignupViewAdapter extends CursorAdapter {
	
	private ArrayList<CourseRecord> list = new ArrayList<CourseRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;
	
	//public static int currentCourseID;
	public static String CURRENT_COURSE_ID_PREFS = "current_course_id_prefs";
	public static String CURRENT_COURSE_ID_KEY = "current_course_id_key";
	

	public CourseSignupViewAdapter(Context context, Cursor c, int flags, DatabaseOpenHelper helper) {
		super(context, c, flags);
		
		mContext = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mDbHelper = helper;
		mDB = helper.getWritableDatabase();
		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addToList(CourseRecord courseRecord){
		list.add(courseRecord);
		//posList[countIdx++] = 
	}
	
	/*public void swapCursorToList(Cursor newCursor) {
		super.swapCursor(newCursor);

		Log.i("My Error", "swapCursor 1 >>>>>>>>>>>>>");
		
		if (null != newCursor) {

			Log.i("My Error", "swapCursor 2 >>>>>>>>>>>>>");
			// clear the ArrayList list so it contains
			// the current set of CourseRecords. Use the
			// getCourseRecordFromCursor() method to add the
			// current record to the list
			list.clear();
			Log.i("My Error", "swapCursor 3 >>>>>>>>>>>>>");
			if (newCursor.moveToFirst()) {
				Log.i("My Error", "swapCursor 4 >>>>>>>>>>>>>");
				do {
					Log.i("My Error", "swapCursor 5 >>>>>>>>>>>>>");
					list.add(getCourseRecordFromCursor(newCursor));
					Log.i("My Error", "swapCursor 6 >>>>>>>>>>>>>");
					this.notifyDataSetChanged();
					Log.i("My Error", "swapCursor 7 >>>>>>>>>>>>>");
				} while (newCursor.moveToNext() == true);
			}

			Log.i("My Error", "swapCursor 8 >>>>>>>>>>>>>");
			// Set the NotificationURI for the new cursor
			
			newCursor.setNotificationUri(mContext.getContentResolver(),
					CourseEntry.CONTENT_URI);

		}

	}*/

	private CourseRecord getCourseRecordFromCursor(Cursor cursor) {
		int courseID = cursor.getInt(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_ID));
		int instructorID = cursor.getInt(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_INSTRUCTOR_ID));
		String courseName = cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_NAME));		
		//String courseDays = cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_DAYS));
		//boolean selected = list.get(cursor.getPosition()).isSelected();

		return new CourseRecord(courseID, instructorID, courseName, "", false);
	}
	
	
	
	public ArrayList<CourseRecord> getList() {
		return list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//Log.i("My Error", String.valueOf(count++)+" __>"+list.size());
		final int pos = position;
		View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.row_courses_signup, null);

        
		Button courseBt = (Button)vi.findViewById(R.id.course_signup_button);
		
		courseBt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				final SharedPreferences prefs = mContext.getSharedPreferences(CURRENT_COURSE_ID_PREFS, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(CURRENT_COURSE_ID_KEY, list.get(pos).getCourseID());
				editor.commit();
				//currentCourseID = list.get(pos).getCourseID();
				Intent studentManagementIntent = new Intent(mContext, StudentManagementActivity.class);
				mContext.startActivity(studentManagementIntent);
				((CoursesSignupActivity)mContext).finish();
			}
			
		});
		
		courseBt.setText(list.get(position).getCourseName());
		
		return vi;
	}
	
	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
