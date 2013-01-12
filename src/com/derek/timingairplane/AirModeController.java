package com.derek.timingairplane;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class AirModeController {
	
	public static void setOnAir(Context context) {
		if(!isOnAir(context) ) {
			setAir(context, true);
		}
	}
	
	public static void setOffAir(Context context) {
		if(isOnAir(context)) {
			setAir(context, false);
		}
	}
	
	public static void setAir(Context context, boolean enable) {
		Settings.System.putInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, enable ? 1 :0);
		Intent localIntent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		localIntent.putExtra("state", enable);
		context.sendBroadcast(localIntent);
	}
	
	public static boolean isOnAir(Context context) {
		return Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) 
				== 1 ? true : false;
	}
}
