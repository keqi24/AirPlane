package com.derek.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/*
 * user SharePreference to storage the 
 * airplane time and cancel time which 
 * was set by user last time.
 * Defaultly, the time result is "00:00"
 */
public class PreferenceStorage {
	
	static final String FLAG = "air_plane";
	
	public static PreferenceStorage storage = null;
	
	public static final String PREFS_NAME = "TimePreference";
	
	static final String KEY_AIRPLANE = "key_airplane";
	static final String KEY_CANCEL = "key_cancel";
	
	public static PreferenceStorage shareInstance() {
		if(storage == null) {
			storage = new PreferenceStorage();
		}
		return storage;
	}
	
	
	public void setAirPlaneTime(Context context, String value) {
		setContent(context,KEY_AIRPLANE, value);
	}
	
	public void setCancelTime(Context context, String value) {
		setContent(context,KEY_CANCEL,value);
	}
	
	public String getAirPlaneTime(Context context ) {
		return getContent(context,KEY_AIRPLANE);
	}
	
	public String getCancelTime(Context context ) {
		return getContent(context,KEY_CANCEL);
	}
	
	protected void setContent(Context context, String key, String value) {
		Log.i(FLAG, key + ":" + value);
		SharedPreferences timeStorage = context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = timeStorage.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	protected String getContent(Context context, String key) {
		SharedPreferences timeStorage = context.getSharedPreferences(PREFS_NAME, 0);
		String result = timeStorage.getString(key, "00:00");
		return result;
	}
	
	
}
