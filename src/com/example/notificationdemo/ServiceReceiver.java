package com.example.notificationdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ServiceReceiver extends BroadcastReceiver {
	public static final String NOTIFICATION_ITEM_BUTTON_LAST = "com.example.notificationdemo.ServiceReceiver.last";
	public static final String NOTIFICATION_ITEM_BUTTON_PLAY = "com.example.notificationdemo.ServiceReceiver.play";
	public static final String NOTIFICATION_ITEM_BUTTON_NEXT = "com.example.notificationdemo.ServiceReceiver.next";
	@Override
	public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(NOTIFICATION_ITEM_BUTTON_LAST)){
				Toast.makeText(context, "上一首", Toast.LENGTH_SHORT).show();
			} else if(action.equals(NOTIFICATION_ITEM_BUTTON_PLAY)){
				Toast.makeText(context, "播放／暂停", Toast.LENGTH_SHORT).show();
			} else  if(action.equals(NOTIFICATION_ITEM_BUTTON_NEXT)){
				Toast.makeText(context, "下一首", Toast.LENGTH_SHORT).show();
			}
			
	}
	
}
