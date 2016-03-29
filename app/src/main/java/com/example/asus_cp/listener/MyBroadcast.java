package com.example.asus_cp.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by asus-cp on 2016-03-06.
 */
public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1=new Intent(context, ListenerService.class);
        context.startService(intent1);
        Log.d("MyReceiver", "广播已经接收到");

    }
}
