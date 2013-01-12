package com.derek.timingairplane;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{

	static final String FLAG = "air_plane";
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();  
        int id = intent.getIntExtra("id", -1);  
        long alarmtime= intent.getLongExtra("alarm_time", -1);  
        
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
