package com.derek.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.derek.timingairplane.MainActivity;
import com.derek.utility.AirModeController;
/*
 * unused controlled by the AlarmReceiver
 */
public class CancelAlarmReceiver extends BroadcastReceiver{

	static final String FLAG = "air_plane";
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();  
        int id = intent.getIntExtra(MainActivity.ID, -1);  
        long alarmtime= intent.getLongExtra(MainActivity.TIME, -1);  
        Log.i(FLAG,"received action = "+action+", id = "+id+ ", alarmtime = "+alarmtime);  
        Toast.makeText(context, "received action = "+action+", id = "+id, Toast.LENGTH_SHORT).show();
       
        AirModeController.setOffAir(context);
        
	}

}
