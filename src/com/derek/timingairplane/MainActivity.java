package com.derek.timingairplane;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.derek.data.PreferenceStorage;

public class MainActivity extends Activity {

	
	//log flag
	public static final String FLAG = "air_plane";
	
	//broadcast intent ID
	static final String ALARM_INTENT = "com.derek.alarm";
	
	//put extra content
	public static final String ID = "id";  
	public static final String TIME = "alarm_time";  
	
	//不同的broadcast 的ID
	public static final int BROADCAST_AIRPLANE_ID = 1;
	public static final int BROADCAST_CANCEL_ID = 2;
	
	
	//component
	TimePicker tp;							                        
	TextView current_airplane_time;				
	TextView current_cancel_time;
	ToggleButton current_toggle_airplane;
	ToggleButton current_toggle_cancel;
	
	/****************************************lift circle************************************************/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initComponent();
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		initCurrentTime();
		initToggleStatus();
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	/****************************************componet************************************************/
	
	//call in onCreate() to init all the component
	private void initComponent() {
		tp = (TimePicker) findViewById(R.id.timepicker_reset);
		current_airplane_time = (TextView) findViewById(R.id.time_current_airplane);
		current_cancel_time = (TextView) findViewById(R.id.time_current_cancel);
		
		current_toggle_airplane = (ToggleButton) findViewById(R.id.toggle_current_airplane);
		current_toggle_cancel = (ToggleButton) findViewById(R.id.toggle_current_cancel);
	}
	
	//check the exist pendingIntent to set the toggle status
	private void initToggleStatus() {
		  Intent intent = new Intent(ALARM_INTENT);  
//        intent.setClass(this, AlarmReceiver.class);  
//        intent.setData(Uri.parse("content://calendar/calendar_alerts/1"));  
        intent.putExtra(ID, BROADCAST_AIRPLANE_ID); 
        PendingIntent senderAirplane = PendingIntent.getBroadcast(this, BROADCAST_AIRPLANE_ID, intent, PendingIntent.FLAG_NO_CREATE);  
		PendingIntent senderCancel = PendingIntent.getBroadcast(this, BROADCAST_CANCEL_ID, intent, PendingIntent.FLAG_NO_CREATE);  
	       
        current_toggle_airplane.setChecked(senderAirplane!=null?true:false);
        current_toggle_cancel.setChecked(senderCancel!=null?true:false);
	}
	
	//set the airplane time and cancel time stored
	private void initCurrentTime() {
		PreferenceStorage ps = PreferenceStorage.shareInstance();
		current_airplane_time.setText(ps.getAirPlaneTime(this));
		current_cancel_time.setText(ps.getCancelTime(this));
	}
	
	
	/****************************************set button************************************************/
	
	/*
	 * set button onClick
	 * function:
	 * 1. set the current time text;
	 * 2. reset alarm broadcast
	 * */
	public void onSetAirplaneClick(View view) {
		current_airplane_time.setText(getTimePickerTime());
		
		if(!(current_toggle_airplane.isChecked())) {
			current_toggle_airplane.setChecked(true);
		}
		setAlarm(BROADCAST_AIRPLANE_ID, ALARM_INTENT, current_airplane_time);
	}
	
	public void onSetCancelClick(View view) {
		current_cancel_time.setText(getTimePickerTime());
		
		if(!(current_toggle_cancel.isChecked())) {
			current_toggle_cancel.setChecked(true);
		}
		setAlarm(BROADCAST_CANCEL_ID, ALARM_INTENT, current_cancel_time);
	}
	
	public String getTimePickerTime() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%02d", tp.getCurrentHour()));
		sb.append(":");
		sb.append(String.format("%02d", tp.getCurrentMinute()));
		Log.i(FLAG, sb.toString());
		return sb.toString();
	}
	
	
	/****************************************toggle button************************************************/
	
	//airPalne toggle is clicked
	public void onAirPlaneToggle(View view) {
		boolean on = ((ToggleButton)view).isChecked();
		if(on) {
			setAlarm(BROADCAST_AIRPLANE_ID, ALARM_INTENT, current_airplane_time);
		}
		else {
			cancelAlarm(BROADCAST_AIRPLANE_ID, ALARM_INTENT);
		}
	}
	//cancel toggle is clicked
	public void onCancelToggle(View view) {
		boolean on = ((ToggleButton)view).isChecked();
		if(on) {
			setAlarm(BROADCAST_CANCEL_ID, ALARM_INTENT, current_cancel_time);
		}
		else {
			cancelAlarm(BROADCAST_CANCEL_ID, ALARM_INTENT);
		}
	}
	
	
	/****************************************alarm************************************************/
	
	public void setAlarm(int id, String intetn_id, TextView tv) {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);  
		Intent intent = new Intent(intetn_id);
//		intent.setData(Uri.parse("content://calendar/calendar_alerts/1"));  
//      intent.setClass(this, AlarmReceiver.class);  
        
        intent.putExtra(ID, id);  
        long atTimeInMillis = getTimeFormTextView(tv);  
        intent.putExtra(TIME, atTimeInMillis);  
        
        PendingIntent sender = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //every day will repeat
        am.setRepeating(AlarmManager.RTC, atTimeInMillis, AlarmManager.INTERVAL_DAY, sender);
        Log.i(FLAG, "setAlarm");
        
        //set the alarm and store the time
        storeCurrentTime(id);
	}
	
	public void cancelAlarm(int id, String intetn_id) {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);  
        Intent intent = new Intent(intetn_id);  
//        intent.setClass(this, AlarmReceiver.class);  
//        intent.setData(Uri.parse("content://calendar/calendar_alerts/1"));  
        intent.putExtra(ID, id);  
        PendingIntent sender = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_NO_CREATE);  
        if (sender != null){  
            Log.i(FLAG,"cancel alarm");  
            am.cancel(sender);  
            sender.cancel();
        }else{  
            Log.i(FLAG,"sender == null");  
        }  
        
	}
	
	
	/****************************************common function************************************************/
	
	//get time content hour and minute 
	protected long getTimeFormTextView(TextView tv) {
		String str = tv.getText().toString();
		
		String[] num = str.split(":");
		
		if(num.length == 2 && num[0] !=null && num[1]!=null) {
			
			int hour = Integer.parseInt(num[0]);
			int minute = Integer.parseInt(num[1]);
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			
			if(cal.compareTo(Calendar.getInstance()) < 0) {
				cal.add(Calendar.DATE, 1);
			} 
			
			showSeprateTime(cal);
			
			return cal.getTimeInMillis();
			
		}
		Log.e(FLAG, "get The time from textview is error :" + str);
		return 0;
	}
	
	//storage the current time
	protected void storeCurrentTime(int id) {
		switch(id) {
			case BROADCAST_AIRPLANE_ID:
				PreferenceStorage.shareInstance().setAirPlaneTime(this, current_airplane_time.getText().toString());
				break;
			case BROADCAST_CANCEL_ID:
				PreferenceStorage.shareInstance().setCancelTime(this, current_cancel_time.getText().toString());
				break;
			default:
				break;
		}
	}
	
	/****************************************other function************************************************/
	protected void showSeprateTime(Calendar cal) {
		Calendar cur = Calendar.getInstance();
		StringBuilder sb = new StringBuilder(getResources().getString(R.string.text_time_delay));
		
		long diff = cal.getTimeInMillis() -  cur.getTimeInMillis();
		if(diff > 0) {
			long s = diff/1000;
			long day = s / (24*60*60);
			s = s - day*24*60*60;
			long hour = s / (60*60);
			s = s - hour*60*60;
			long minute = s/60;
			sb.append(day);
			sb.append(getResources().getString(R.string.text_day));
			sb.append(hour);
			sb.append(getResources().getString(R.string.text_hour));
			sb.append(minute);
			sb.append(getResources().getString(R.string.text_minute));
			Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
		} 
		
		
	}
	
}
