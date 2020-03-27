package com.example.chatbot2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;
    Bundle bundle;
    Object[] pdus;
    Handler handler = new Handler();
    TextView textView;
    SmsMessage[] array;
    String phoneNumber;
    String messageReceived;
    SmsManager smsManager=SmsManager.getDefault();
    int state=1;
    String[] greetingMessages = {"Welcome to Chipotle! Do you want a burrito or a bowl?", "Would you like a burrito or a bowl?", "Hi! Are you having a burrito or a bowl today?", "We offer burritos or bowls, which would you like?"};
    String[] toppingMessages = {"And what would you like on it?", "What would you like to add?", "Can you please select what else you want", "We have a deal on unlimited ingredients, please select what you want!"};
    String[] extraMessages = {"Can I interest you in guacamole for an extra $2?", "Would you like lemonade for an extra $2?", "Do you want a soda for an extra $2?", "Can I interest you in a drink for an extra $2?"};
    String[] paymentMessagesNo = {"Your total comes out to $7.13. Please come again!", "It's $7.13, see ya later!", "Thanks for coming!", "Please come again"};
    String[] paymentMessagesYes = {"Your total comes out to $9.13. Please come again!", "It's $9.13, see ya later!", "Thanks for coming!", "Please come again"};

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.id_textview);

        textView.setText(""+(int)(Math.random()*4));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 0);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 0);
        }


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("TAG", "POGGERS");
                array = null;
                bundle = intent.getExtras();

                if (bundle != null) {
                    pdus = (Object[]) bundle.get("pdus");

                    array = new SmsMessage[pdus.length];

                    for (int i = 0; i < pdus.length; i++) {
                        byte[] a = (byte[]) pdus[i];
                        SmsMessage smsMessage = SmsMessage.createFromPdu(a, bundle.getString("format"));
                        array[i] = smsMessage;
                        messageReceived=array[i].getMessageBody();
                        phoneNumber=array[i].getOriginatingAddress();
                        Log.d("TAG", messageReceived);
                        Log.d("TAG", phoneNumber);
                        runAi(messageReceived);
                    }

                }
            }
        };
    }


    public void sendText(final String messageSend) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                smsManager.sendTextMessage(phoneNumber, null, messageSend, null, null);
            }
        }, 3000);
    }

    public void runAi(String message) {
        if (state == 1 && (message.toLowerCase().contains("hi") || message.toLowerCase().contains("hello")) || message.toLowerCase().contains("hey")) {
            sendText(greetingMessages[(int)(Math.random()*4)]);
            textView.setText("GREETING STATE");
            state++;
        }
        else if (state == 2 && (message.toLowerCase().contains("burrito") || message.toLowerCase().contains("bowl"))) {
            sendText(toppingMessages[(int)(Math.random()*4)]);
            textView.setText("ASKS FOR INGREDIENTS STATE");
            state++;
        }
        else if (state == 3 && (message.toLowerCase().contains("chicken") || message.toLowerCase().contains("rice")) || message.toLowerCase().contains("beans") || message.toLowerCase().contains("corn") || message.toLowerCase().contains("cheese")) {
            sendText(extraMessages[(int)(Math.random()*4)]);
            textView.setText("ASKS FOR EXTRAS STATE");
            state++;
        }
        else if (state == 4 && (message.toLowerCase().contains("yes"))) {
            sendText(paymentMessagesYes[(int)(Math.random()*4)]);
            textView.setText("PAYMENT STATE");
            state++;
        }
        else if (state == 4 && (message.toLowerCase().contains("no"))) {
            sendText(paymentMessagesNo[(int)(Math.random()*4)]);
            textView.setText("PAYMENT STATE");
            state++;
        }
        else {
            sendText("Sorry, I don't understand that.");
        }
    }
}
