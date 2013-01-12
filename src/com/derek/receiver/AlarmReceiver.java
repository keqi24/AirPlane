package com.derek.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.derek.timingairplane.MainActivity;
import com.derek.utility.AirModeController;

public class AlarmReceiver extends BroadcastReceiver{

	static final String FLAG = "air_plane";
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();  
        int id = intent.getIntExtra("id", -1);  
        long alarmtime= intent.getLongExtra("alarm_time", -1);  
        
        Log.i(FLAG, "alarmReceiver:" + id);
        
        switch(id) {
	        case MainActivity.BROADCAST_AIRPLANE_ID:
	        	AirModeController.setOnAir(context);
	        	break;
	        case MainActivity.BROADCAST_CANCEL_ID:
	        	AirModeController.setOffAir(context);
	        	break;
        	default:
        		break;
        }
        
	}
	

}
