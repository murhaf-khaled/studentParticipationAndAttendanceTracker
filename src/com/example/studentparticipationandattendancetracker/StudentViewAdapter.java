/**
 * 
 */
package com.example.studentparticipationandattendancetracker;

import java.util.ArrayList;

import com.example.studentparticipationandattendancetracker.provider.UniContract.AttendanceEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.CourseEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.EnrollsEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.ParticipationEntry;
import com.example.studentparticipationandattendancetracker.provider.UniContract.StudentEntry;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author UsEr
 *
 */
public class StudentViewAdapter extends CursorAdapter {

	private ArrayList<StudentRecord> list = new ArrayList<StudentRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;
	
	private SharedPreferences prefs;
	public static String CURRENT_COURSE_ID_PREFS = "current_course_id_prefs";
	public static String CURRENT_COURSE_ID_KEY = "current_course_id_key";
	
	/**
	 * @param context
	 * @param c
	 * @param autoRequery
	 */
	public StudentViewAdapter(Context context, Cursor c, int flags, DatabaseOpenHelper helper) {
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
	
	public void addToList(StudentRecord record){
		list.add(record);
	}

	public boolean add(StudentRecord listItem) {
		prefs = mContext.getSharedPreferences(CURRENT_COURSE_ID_PREFS, Context.MODE_PRIVATE);
		int currentCourseID =  prefs.getInt(CURRENT_COURSE_ID_KEY, 0);
		
		ContentValues values = new ContentValues();
		values.put(StudentEntry.COLUMN_NAME_STUDENT_ID,
				listItem.getStudentID());
		values.put(StudentEntry.COLUMN_NAME_STUDENT_NAME,
				listItem.getStudentName());
		values.put(StudentEntry.COLUMN_NAME_STUDENT_EMAIL,
				listItem.getStudentEmail());
		
		// Insert the new row, returning the primary key value of the new row
		long newRowId = mDB.insert(
				StudentEntry.TABLE_NAME,
		         null,
		         values);
		
		values = new ContentValues();
		values.put(EnrollsEntry.COLUMN_NAME_COURSE_ID,
				currentCourseID);
		values.put(EnrollsEntry.COLUMN_NAME_STUDENT_ID,
				listItem.getStudentID());
		
		long enrollsNewRowID = mDB.insert(
				EnrollsEntry.TABLE_NAME,
		         null,
		         values);
		
		if(newRowId != -1 || enrollsNewRowID != -1){
			// TODO add a new student to the list Adapter
			list.add(listItem);
			return true;
		}
		else{
			Toast.makeText(mContext,
					"The Student is already exist",
					Toast.LENGTH_LONG).show();
			return false;
		}

	}
	
	public ArrayList<StudentRecord> getList() {
		return list;
	}
	
	public boolean update(StudentRecord row){
		StudentRecord newRow = row;
		//TODO getsharedpreference for temp_studentName and Email
		newRow.setStudentName(StudentManagementActivity.temp_studentName);
		newRow.setStudentEmail(StudentManagementActivity.temp_studentEmail);
		
		ContentValues values = new ContentValues();
		values.put(StudentEntry.COLUMN_NAME_STUDENT_NAME, newRow.getStudentName());
		values.put(StudentEntry.COLUMN_NAME_STUDENT_EMAIL, newRow.getStudentEmail());
		
		mDB.update(StudentEntry.TABLE_NAME, values, StudentEntry.COLUMN_NAME_STUDENT_ID + " = "+row.getStudentID(), null);
		return ! list.set(list.indexOf(row), newRow).equals(null);
	}
	
	public boolean remove(StudentRecord row){
		prefs = mContext.getSharedPreferences(CURRENT_COURSE_ID_PREFS, Context.MODE_PRIVATE);
		int currentCourseID =  prefs.getInt(CURRENT_COURSE_ID_KEY, 0);
		mDB.delete(EnrollsEntry.TABLE_NAME, EnrollsEntry.COLUMN_NAME_STUDENT_ID + " = "+row.getStudentID() +" AND "+EnrollsEntry.COLUMN_NAME_COURSE_ID+" = "+currentCourseID, null);
		mDB.delete(ParticipationEntry.TABLE_NAME, ParticipationEntry.COLUMN_NAME_STUDENT_ID + " = "+row.getStudentID() +" AND "+ParticipationEntry.COLUMN_NAME_COURSE_ID+" = "+currentCourseID, null);
		mDB.delete(AttendanceEntry.TABLE_NAME, AttendanceEntry.COLUMN_NAME_STUDENT_ID + " = "+row.getStudentID() +" AND "+AttendanceEntry.COLUMN_NAME_COURSE_ID+" = "+currentCourseID, null);
		//mDB.delete(StudentEntry.TABLE_NAME, StudentEntry.COLUMN_NAME_STUDENT_ID + " = "+row.getStudentID(), null);
		return list.remove(row);
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
            vi = inflater.inflate(R.layout.row_student_listview_item, null);

        final CheckBox flag = (CheckBox) vi.findViewById(R.id.student_data_checkBox);
		TextView studentName = (TextView)vi.findViewById(R.id.student_name_data_tv);
		TextView studentID = (TextView)vi.findViewById(R.id.student_id_data_tv);
		TextView studentEmail = (TextView)vi.findViewById(R.id.student_email_data_tv);
		
		flag.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(flag.isChecked()){
					if(list.get(pos) != null){
						list.get(pos).setSelected(true);
						//TODO set sheredpreference
						StudentManagementActivity.temp_studentID = String.valueOf(list.get(pos).getStudentID());
						StudentManagementActivity.temp_studentName = list.get(pos).getStudentName();
						StudentManagementActivity.temp_studentEmail = list.get(pos).getStudentEmail();
						
						StudentManagementActivity.studentID_EditText.setText(String.valueOf(list.get(pos).getStudentID()));
						StudentManagementActivity.studentName_EditText.setText(list.get(pos).getStudentName());
						StudentManagementActivity.studentEmail_EditText.setText(list.get(pos).getStudentEmail());
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
			studentName.setText(list.get(position).getStudentName());
			studentID.setText(String.valueOf(list.get(position).getStudentID()));
			studentEmail.setText(list.get(position).getStudentEmail());
		}
		
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
