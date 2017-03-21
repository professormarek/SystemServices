package com.example.teaching.systemservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * in this code sample we're going to demonstrate
 * 1) how to start and stop a service.
 */
public class MainActivity extends AppCompatActivity {

    //member field to store an reference to our MyBroadcastReceiver instance.. instantiate it onCreate()
    MyBroadcastReceiver myBroadcastReceiver = null;

    /**
     * BroadcastReciever is a class for receiving broadcasted Intents
     * We will instantiate and register an BroadcastReciever to listen for Intents
     * sent by our service.
     * Start listenting for Intents when the Activity starts
     * Stop listenting for Intents when the Activity is no longer at the foreground (onPause)
     */
    public class MyBroadcastReceiver extends BroadcastReceiver{
        //onRecieve will be called by the operating system when an Intent that matches the filter is received
        @Override
        public void onReceive(Context context, Intent intent) {
            //call a method defined in MainActivity...
            handleIntent(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create a handler for the start button
        Button startButton = (Button) findViewById(R.id.buttonStartService);
        startButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //call a method to start the service
                    startMyService();
                }
        });

        //create a handler for the stop button
        Button stopButton = (Button) findViewById(R.id.buttonStopService);
        stopButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //call a method to stop the service
                stopMyService();
            }
        });

        //instantiate MyBroadcastReceiver for later use...
        myBroadcastReceiver = new MyBroadcastReceiver();
    }

    /**
     * onResume is called when our activity comes back to the foreground
     * see android activity lifecycle
     */
    @Override
    protected void onResume() {
        super.onResume();
        /*
        register our BroadcastReceiver
        ... in order to recieve broadcast Intents we need to create an IntentFilter
        and then register the BroadcastReceiver
         */
        IntentFilter intentFilter = new IntentFilter("com.example.marek.NOTIFICATION_CLICKED");
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    /**
     * we need to also de-register the BroadcastReceiver when our activty is no longer in the foreground
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
        //unregister the BroadcastReceiver to stop listening for updates
        unregisterReceiver(myBroadcastReceiver);
    }

    /**
     * starts this application's service!
     */
    private void startMyService(){

        //we can start MyService with a explicit Intent
        Intent intent = new Intent(this, MyService.class);
        //start the service by calling Context.startService()
        System.out.println("Attempting to start MyService");
        startService(intent);
    }

    /**
     * stops this application's service!
     */
    private void stopMyService(){
        //create an explict intent that we will use to stop the service
        Intent intent = new Intent(this, MyService.class);
        //stop the service by calling Context.stopService()
        System.out.println("Attempting to stop MyService");
        stopService(intent);
    }

    private void handleIntent(Intent intent){
        System.out.println("Our activity received an intent: " + intent.toString());
    }
}
