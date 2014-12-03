/**
 * 
 */
package com.example.studentparticipationandattendancetracker;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;

/**
 * @author UsEr
 *
 */
public class ParticipationReportViewAdapter extends CursorAdapter {

	private ArrayList<ParticipationRecord> list = new ArrayList<ParticipationRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	private DatabaseOpenHelper mDbHelper;
	public SQLiteDatabase mDB = null;
	
	/**
	 * @param context
	 * @param c
	 * @param autoRequery
	 */
	public ParticipationReportViewAdapter(Context context, Cursor c, int flags, DatabaseOpenHelper helper) {
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

	public void addToList(ParticipationRecord participationRecord){
		list.add(participationRecord);
	}
	
	public ArrayList<ParticipationRecord> getList() {
		return list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.row_student_participation_report_listview_item, null);

        
        TextView studentName = (TextView)vi.findViewById(R.id.student_name_student_participation_report_data);
		TextView studentID = (TextView)vi.findViewById(R.id.student_id_student_participation_report_data);
		TextView rank = (TextView)vi.findViewById(R.id.student_rank_student_participation_report_data);
		
		studentName.setText(list.get(position).getStudentName());
		studentID.setText(String.valueOf(list.get(position).getStudentID()));
		rank.setText(String.valueOf(list.get(position).getPartMark()));
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
