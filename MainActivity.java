package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;
    Bundle bundle;
    Object[] pdus;
    static SmsManager smsManager;
    Handler handler;
    static Runnable r;
    TextView textView;
    SmsMessage[] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.id_textview);

        smsManager=SmsManager.getDefault();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);


        //handler.postDelayed(r, 1000);

        //sendText("hello!");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                array=null;
                bundle = intent.getExtras();
                pdus = (Object[]) bundle.get("pdus");

                array = new SmsMessage[pdus.length];

                for(int i=0; i<pdus.length; i++){
                    byte[] a = (byte[]) pdus[i];
                    SmsMessage smsMessage = SmsMessage.createFromPdu(a);
                    array[i]=smsMessage;
                    textView.setText(array[i].getMessageBody());
                }

            }
        };
    }


    public static void sendText(final String message){
        r =new Runnable() {
            @Override
            public void run() {
                smsManager.sendTextMessage("", null, message, null, null);
            }
        };
    }
}
