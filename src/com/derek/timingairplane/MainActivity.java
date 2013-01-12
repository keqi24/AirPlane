package com.derek.timingairplane;

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
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	
	//log flag
	public static final String FLAG = "air_plane";
	
	//broadcast intent ID
	static final String AIRPLANE_ALARM_INTENT = "com.derek.alarm.airplane";
	static final String CANCEL_ALARM_INTENT = "com.derek.alarm.cancel";
	//test broadcast intent ID
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initComponent();
	}
	
	//call in onCreate() to init all the component
	private void initComponent() {
		tp = (TimePicker) findViewById(R.id.timepicker_reset);
		current_airplane_time = (TextView) findViewById(R.id.time_current_airplane);
		current_cancel_time = (TextView) findViewById(R.id.time_current_cancel);
		
		current_toggle_airplane = (ToggleButton) findViewById(R.id.toggle_current_airplane);
		current_toggle_cancel = (ToggleButton) findViewById(R.id.toggle_current_cancel);
	}
	
	
	/*
	 * set button onClick
	 * function:
	 * 1. set the current time text;
	 * 2. reset alarm broadcast
	 * */
	public void onSetAirplaneClick(View view) {
		String str = tp.getCurrentHour() + ":" + tp.getCurrentMinute();
		current_airplane_time.setText(str);
		
		/*if(!(current_toggle_airplane.isChecked())) {
			current_toggle_airplane.setChecked(true);
		}
		setAlarm(BROADCAST_AIRPLANE_ID, AIRPLANE_ALARM_INTENT, current_airplane_time);*/
	
		
	}
	
	public void onSetCancelClick(View view) {
		String str = tp.getCurrentHour() + ":" + tp.getCurrentMinute();
		current_cancel_time.setText(str);
		
		/*if(!(current_toggle_cancel.isChecked())) {
			current_toggle_cancel.setChecked(true);
		}
		setAlarm(BROADCAST_CANCEL_ID, CANCEL_ALARM_INTENT, current_cancel_time);*/
	}
	
	
	/*
	 * toggle button 
	 */
	//airPalne toggle is clicked
	public void onAirPlaneToggle(View view) {
		boolean on = ((ToggleButton)view).isChecked();
		if(on) {
			//setAlarm(BROADCAST_AIRPLANE_ID, AIRPLANE_ALARM_INTENT, current_airplane_time);
			//onAirPlane();
			setAlarm(BROADCAST_AIRPLANE_ID, ALARM_INTENT, current_airplane_time);
		}
		else {
			//cancelAlarm(BROADCAST_AIRPLANE_ID, AIRPLANE_ALARM_INTENT);
			cancelAlarm(BROADCAST_AIRPLANE_ID, ALARM_INTENT);
		}
	}
	//cancel toggle is clicked
	public void onCancelToggle(View view) {
		boolean on = ((ToggleButton)view).isChecked();
		if(on) {
			//setAlarm(BROADCAST_CANCEL_ID, CANCEL_ALARM_INTENT, current_cancel_time);
			setAlarm(BROADCAST_CANCEL_ID, ALARM_INTENT, current_cancel_time);
		}
		else {
			//cancelAlarm(BROADCAST_CANCEL_ID, CANCEL_ALARM_INTENT);
			cancelAlarm(BROADCAST_CANCEL_ID, ALARM_INTENT);
		}
	}
	
	public void setAlarm(int id, String intetn_id, TextView tv) {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);  
		Intent intent = new Intent(intetn_id);
//		intent.setData(Uri.parse("content://calendar/calendar_alerts/1"));  
//        intent.setClass(this, AlarmReceiver.class);  
        
        
        intent.putExtra(ID, id);  
        long atTimeInMillis = getTimeFormTextView(tv);  
//        long atTimeInMillis = Calendar.getInstance().getTimeInMillis() + 30000;
        intent.putExtra(TIME, atTimeInMillis);  
        
        
        PendingIntent sender = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);
        Log.i(FLAG, "setAlarm");
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
        }else{  
            Log.i(FLAG,"sender == null");  
        }  
	}
	
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
			
			
			long result = cal.getTimeInMillis();
			
			if(result < Calendar.getInstance().getTimeInMillis()) {
				cal.add(Calendar.DATE, 1);
			} 
			
			return cal.getTimeInMillis();
			
		}
		Log.e(FLAG, "get The time from textview is error :" + str);
		return 0;
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
