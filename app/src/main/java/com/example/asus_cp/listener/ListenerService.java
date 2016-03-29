package com.example.asus_cp.listener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by asus-cp on 2016-03-06.
 */
public class ListenerService extends Service {
    private int myFlag;
    private int count;
    private TelephonyManager manager;
    private MediaRecorder recorder;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                manager= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                manager.listen(new MyListener(), PhoneStateListener.LISTEN_CALL_STATE);
                Log.d("ListenerService", "子线程奔跑状态" + Thread.currentThread().getName());
                Looper.loop();
            }
        }).start();



        Log.d("ListenerService", "主线程运行状态"+Thread.currentThread().getName());
        return Service.START_NOT_STICKY;
    }
    private class MyListener extends PhoneStateListener{
        String num=null;
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            recorder=new MediaRecorder();
            switch(state){
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("ListenerService","接通状态" );
                    //num=incomingNumber;
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    File path=new File(Environment.getExternalStorageDirectory()+File.separator+"abc");
                    if(!path.exists()){
                        path.mkdirs();
                    }
                    String file= null;
                    try {
                        file = path.getCanonicalPath()+File.separator+num+"第"+count+"次"+".3gp";
                        Log.d("ListenerService", "num=" + num + "incomingNumber=" + incomingNumber + "count=" + count);
                    } catch (IOException e) {
                        Log.d("ListenerService", e.toString());
                    }
                    recorder.setOutputFile(file);
                    try {
                        recorder.prepare();
                    } catch (IOException e) {
                        Log.d("ListenerService", e.toString());
                    }
                    recorder.start();
                    count++;
                    myFlag=1;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("ListenerService","空闲状态" );
                    Log.d("ListenerService", "myFlag="+myFlag );
                    if(myFlag!=0){
                        recorder.stop();
                        recorder.reset();
                        recorder.release();
                        myFlag=0;
                    }

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("ListenerService","来电状态" );
                    num=incomingNumber;
                    Log.d("ListenerService","num="+num+"incomingNumber="+incomingNumber);
                    break;


            }
        }
    }
}


