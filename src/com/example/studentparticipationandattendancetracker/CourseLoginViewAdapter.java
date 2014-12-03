/**
 * 
 */
package com.example.studentparticipationandattendancetracker;

import java.util.ArrayList;

import com.example.studentparticipationandattendancetracker.provider.UniContract.CourseEntry;

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
import android.widget.CursorAdapter;

/**
 * @author UsEr
 *
 */
public class CourseLoginViewAdapter extends CursorAdapter {

	private ArrayList<CourseRecord> list = new ArrayList<CourseRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;
	
	//public static int currentCourseID;
	public static String CURRENT_COURSE_ID_PREFS = "current_course_id_prefs";
	public static String CURRENT_COURSE_ID_KEY = "current_course_id_key";

	/**
	 * @param context
	 * @param c
	 * @param autoRequery
	 */
	public CourseLoginViewAdapter(Context context, Cursor c, int flags, DatabaseOpenHelper helper) {
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
            vi = inflater.inflate(R.layout.row_course_login, null);

        
		Button courseBt = (Button)vi.findViewById(R.id.course_login_button);
		
		courseBt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				SharedPreferences prefs = mContext.getSharedPreferences(CURRENT_COURSE_ID_PREFS, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(CURRENT_COURSE_ID_KEY, list.get(pos).getCourseID());
				editor.commit();
				
				//currentCourseID = list.get(pos).getCourseID();
				Intent studentParticipationActivityIntent = new Intent(mContext, StudentParticipationActivity.class);
				mContext.startActivity(studentParticipationActivityIntent);
				((CoursesLoginActivity)mContext).finish();
				
			}
			
		});
		
		courseBt.setText(list.get(position).getCourseName());
		
		return vi;
	}
	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
