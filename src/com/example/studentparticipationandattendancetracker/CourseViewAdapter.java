/**
 * 
 */
package com.example.studentparticipationandattendancetracker;

import java.util.ArrayList;

import com.example.studentparticipationandattendancetracker.provider.UniContract.CourseEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.InstructorEntry;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author UsEr
 *
 */
public class CourseViewAdapter extends CursorAdapter {
	
	private ArrayList<CourseRecord> list = new ArrayList<CourseRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;
	private int count = 0;

	/**
	 * @param context
	 * @param c
	 * @param flags
	 */
	public CourseViewAdapter(Context context, Cursor c, int flags, DatabaseOpenHelper helper) {
		super(context, c, flags);
		
		mContext = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mDbHelper = helper;
		mDB = helper.getWritableDatabase();
	}

	
	public CourseViewAdapter(Context context, Cursor c,
			ArrayList<CourseRecord> list, int flags,
			DatabaseOpenHelper helper) {
		super(context, c, flags);
		this.list = list;
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
	}
	
	static class CourseViewHolder {

		CheckBox flag;
		TextView courseName;
		TextView courseID;
		TextView courseDays;

	}

	@Override
	public Cursor swapCursor(Cursor newCursor) {
		super.swapCursor(newCursor);

		
		if (null != newCursor) {

			// clear the ArrayList list so it contains
			// the current set of CourseRecords. Use the
			// getCourseRecordFromCursor() method to add the
			// current record to the list
			list.clear();
			if (newCursor.moveToFirst()) {
				do {
					list.add(getCourseRecordFromCursor(newCursor));
				} while (newCursor.moveToNext() == true);
			}

			// Set the NotificationURI for the new cursor
			
			/*newCursor.setNotificationUri(mContext.getContentResolver(),
					CourseEntry.CONTENT_URI);*/

		}
		return newCursor;

	}

	private CourseRecord getCourseRecordFromCursor(Cursor cursor) {
		int courseID = cursor.getInt(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_ID));
		int instructorID = cursor.getInt(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_INSTRUCTOR_ID));
		String courseName = cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_NAME));		
		String courseDays = cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_DAYS));
		boolean selected = list.get(cursor.getPosition()).isSelected();

		return new CourseRecord(courseID, instructorID, courseName, courseDays, selected);
	}
	
	public boolean add(CourseRecord listItem) {
		
		ContentValues values = new ContentValues();
		values.put(CourseEntry.COLUMN_NAME_COURSE_ID,
				listItem.getCourseID());
		values.put(CourseEntry.COLUMN_NAME_INSTRUCTOR_ID,
				listItem.getInstructorID());
		values.put(CourseEntry.COLUMN_NAME_COURSE_NAME,
				listItem.getCourseName());
		values.put(CourseEntry.COLUMN_NAME_COURSE_DAYS,
				listItem.getCourseDays());
		
		
		// Insert the new row, returning the primary key value of the new row
		long newRowId = mDB.insert(
		         CourseEntry.TABLE_NAME,
		         null,
		         values);
		
		if(newRowId != -1){
			// TODO add a new course to the list Adapter
			list.add(listItem);
			return true;
		}
		else{
			Toast.makeText(mContext,
					"this course is already exist, please try diffrent Course ID",
					Toast.LENGTH_LONG).show();
			return false;
		}

		
		
		
		/*ContentResolver contentResolver = mContext.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(CourseEntry.COLUMN_NAME_COURSE_ID, listItem.getCourseID());
		values.put(CourseEntry.COLUMN_NAME_INSTRUCTOR_ID, listItem.getInstructorID());
		values.put(CourseEntry.COLUMN_NAME_COURSE_NAME, listItem.getCourseName());
		values.put(CourseEntry.COLUMN_NAME_COURSE_DAYS, listItem.getCourseDays());
		
		contentResolver.insert(CourseEntry.CONTENT_URI, values);*/

	}
	
	public ArrayList<CourseRecord> getList() {
		return list;
	}
	
	public boolean update(CourseRecord row){
		CourseRecord newRow = row;
		newRow.setCourseName(CourseManagementActivity.temp_courseName);
		newRow.setCourseDays(CourseManagementActivity.temp_courseDays);
		
		ContentValues values = new ContentValues();
		values.put(CourseEntry.COLUMN_NAME_COURSE_NAME, newRow.getCourseName());
		values.put(CourseEntry.COLUMN_NAME_COURSE_DAYS, newRow.getCourseDays());
		
		mDB.update(CourseEntry.TABLE_NAME, values, CourseEntry.COLUMN_NAME_COURSE_ID + " = "+row.getCourseID(), null);
		return ! list.set(list.indexOf(row), newRow).equals(null);
	}
	
	public boolean remove(CourseRecord row){
		mDB.delete(CourseEntry.TABLE_NAME, CourseEntry.COLUMN_NAME_COURSE_ID + " = "+row.getCourseID(), null);
		return list.remove(row);
	}
	
	/*public boolean remove(){
		Log.i("My Error", "Removing 5");
		boolean isRemoved= false;
		if(! list.isEmpty()){
			Log.i("My Error", "Removing 6");
			for(CourseRecord row : list){
				Log.i("My Error", "Removing 7");
				if(row.isSelected()){
					Log.i("My Error", "Removing 8");
					int i = mDB.delete(CourseEntry.TABLE_NAME, CourseEntry.COLUMN_NAME_COURSE_ID + " = "+row.getCourseID(), null);
					Log.i("My Error", "Removing 9   "+ i);
					list.remove(row);
					Log.i("My Error", "Removing 10");
					isRemoved= true;
				}
				
			}
		}
		return isRemoved;
	}*/

	public void removeAllViews() {

		list.clear();
		
		mDB.delete(CourseEntry.TABLE_NAME, null, null);

		// delete all records in the ContentProvider
		/*mContext.getContentResolver().delete(CourseEntry.CONTENT_URI, null, null);
		mContext.getContentResolver().notifyChange(CourseEntry.CONTENT_URI, null);*/
		
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//Log.i("My Error", String.valueOf(count++)+" __>"+list.size());
		final int pos = position;
		View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.row_course_listview_item, null);

        final CheckBox flag = (CheckBox) vi.findViewById(R.id.course_data_checkBox);
		TextView courseName = (TextView)vi.findViewById(R.id.course_name_data_tv);
		TextView courseID = (TextView)vi.findViewById(R.id.course_id_data_tv);
		TextView courseDays = (TextView)vi.findViewById(R.id.course_days_data_tv);
		
		flag.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(flag.isChecked()){
					if(list.get(pos) != null){
						list.get(pos).setSelected(true);
						CourseManagementActivity.temp_courseID = String.valueOf(list.get(pos).getCourseID());
						CourseManagementActivity.temp_courseName = list.get(pos).getCourseName();
						CourseManagementActivity.temp_courseDays = list.get(pos).getCourseDays();
						
						CourseManagementActivity.courseID_EditText.setText(String.valueOf(list.get(pos).getCourseID()));
						CourseManagementActivity.courseName_EditText.setText(list.get(pos).getCourseName());
						CourseManagementActivity.courseDays_Spinner.setSelection(list.get(pos).getCourseDays().equals("Su/Tu/Th")? 0 : 1);
					}
				}
				else{
					if(list.get(pos) != null){
						list.get(pos).setSelected(false);
					}
					
				}
			}
			
		});
		
		if(list.get(position) != null){
			flag.setChecked(list.get(position).isSelected());
			courseName.setText(list.get(position).getCourseName());
			courseID.setText(String.valueOf(list.get(position).getCourseID()));
			courseDays.setText(list.get(position).getCourseDays());
		}
		
		//courseName.setText(list.get(position).getCourseName());
		//courseID.setText(list.get(position).getCourseID());
		//courseDays.setText(list.get(position).getCourseDays());
		
		return vi;
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		/*CourseViewHolder holder = (CourseViewHolder) view.getTag();

		holder.flag = (CheckBox) view.findViewById(R.id.course_data_checkBox);
		holder.courseName = (TextView) view.findViewById(R.id.course_name_data_tv);
		holder.courseID = (TextView) view.findViewById(R.id.course_id_data_tv);
		holder.courseDays = (TextView) view.findViewById(R.id.course_days_data_tv);
		
		holder.flag.setChecked(false);*/
		//holder.courseID.setText(cursor.getInt(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_ID)));
		//holder.courseName.setText(cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_NAME)));
		//holder.courseDays.setText(cursor.getString(cursor.getColumnIndex(CourseEntry.COLUMN_NAME_COURSE_DAYS)));
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		return inflater.inflate(R.layout.row_course_listview_item, parent, false);
		
		/*View newView;
		CourseViewHolder holder = new CourseViewHolder();

		newView = inflater.inflate(R.layout.row_course_listview_item, null);
		holder.flag = (CheckBox) newView.findViewById(R.id.course_data_checkBox);
		holder.courseName = (TextView) newView.findViewById(R.id.course_name_data_tv);
		holder.courseID = (TextView) newView.findViewById(R.id.course_id_data_tv);
		holder.courseDays = (TextView) newView.findViewById(R.id.course_days_data_tv);

		newView.setTag(holder);

		return newView;*/
	}

}
