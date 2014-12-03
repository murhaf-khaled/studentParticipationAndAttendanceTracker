/**
 * 
 */
package com.example.studentparticipationandattendancetracker;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author UsEr
 *
 */
public class AttendanceViewAdapter extends CursorAdapter {

	private ArrayList<AttendanceRecord> list = new ArrayList<AttendanceRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;
	
	/**
	 * @param context
	 * @param c
	 * @param autoRequery
	 */
	public AttendanceViewAdapter(Context context, Cursor c, int flags, DatabaseOpenHelper helper) {
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

	public void addToList(AttendanceRecord attendanceRecord){
		list.add(attendanceRecord);
	}
	
	public ArrayList<AttendanceRecord> getList() {
		return list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("My Error", "StudentAttendanceActivity >>>> getVeiw() 1");
		final int pos = position;
		View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.row_student_attendance_listview_item, null);

        Log.i("My Error", "StudentAttendanceActivity >>>> getVeiw() 2");
        TextView studentName = (TextView)vi.findViewById(R.id.student_name_student_attendance_data);
		TextView studentID = (TextView)vi.findViewById(R.id.student_id_student_attendance_data);
		final ImageButton absence = (ImageButton)vi.findViewById(R.id.absenceImageButton);
		final ImageButton presence = (ImageButton)vi.findViewById(R.id.presenceImageButton);
		final ImageButton hand = (ImageButton)vi.findViewById(R.id.excuseImageButton);
		Log.i("My Error", "StudentAttendanceActivity >>>> getVeiw() 3");
		absence.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Log.i("My Error", "StudentAttendanceActivity >>>> getVeiw() 000");
				String tag = absence.getTag().toString().trim();
				if(tag.equals("off")){
					absence.setImageResource(R.drawable.absence2);
					presence.setImageResource(R.drawable.presence1);
					hand.setImageResource(R.drawable.hand1);
					absence.setTag("on");
					list.get(pos).setAttendanceStatus("Absence");
				}
				else{
					absence.setImageResource(R.drawable.absence1);
					presence.setImageResource(R.drawable.presence2);
					hand.setImageResource(R.drawable.hand1);
					absence.setTag("off");
					list.get(pos).setAttendanceStatus("Presence");
				}
				
				
			}
			
		});
		Log.i("My Error", "StudentAttendanceActivity >>>> getVeiw() 4");
		presence.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				String tag = presence.getTag().toString().trim();
				if(tag.equals("off")){
					presence.setImageResource(R.drawable.presence2);
					absence.setImageResource(R.drawable.absence1);
					hand.setImageResource(R.drawable.hand1);
					presence.setTag("on");
					list.get(pos).setAttendanceStatus("Presence");
				}
				else{
					presence.setImageResource(R.drawable.presence1);
					absence.setImageResource(R.drawable.absence2);
					hand.setImageResource(R.drawable.hand1);
					presence.setTag("off");
					list.get(pos).setAttendanceStatus("Absence");
				}
				
			}
			
		});
		Log.i("My Error", "StudentAttendanceActivity >>>> getVeiw() 5");
		hand.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				String tag = hand.getTag().toString().trim();
				if(tag.equals("off")){
					hand.setImageResource(R.drawable.hand2);
					absence.setImageResource(R.drawable.absence1);
					presence.setImageResource(R.drawable.presence1);
					hand.setTag("on");
					list.get(pos).setAttendanceStatus("Presence");
				}
				else{
					hand.setImageResource(R.drawable.hand1);
					absence.setImageResource(R.drawable.absence2);
					presence.setImageResource(R.drawable.presence1);
					hand.setTag("off");
					list.get(pos).setAttendanceStatus("Absence");
				}
			}
			
		});
		Log.i("My Error", "StudentAttendanceActivity >>>> getVeiw() 6");
		studentName.setText(list.get(position).getStudentName());
		studentID.setText(String.valueOf(list.get(position).getStudentID()));
		if(list.get(position).getAttendanceStatus().equals("Absence")){
			absence.setImageResource(R.drawable.absence2);
			presence.setImageResource(R.drawable.presence1);
			hand.setImageResource(R.drawable.hand1);
		}
		else if(list.get(position).getAttendanceStatus().equals("Presence")){
			absence.setImageResource(R.drawable.absence1);
			presence.setImageResource(R.drawable.presence2);
			hand.setImageResource(R.drawable.hand1);
		}
		else{
			absence.setImageResource(R.drawable.absence1);
			presence.setImageResource(R.drawable.presence1);
			hand.setImageResource(R.drawable.hand2);
		}
		//rank.setRating( (float) list.get(position).getPartMark());
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
