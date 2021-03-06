package com.example.teaching.systemservices;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        /**
         * WARNING: before getting device location from LocationManager
         * we need to make sure we have permission from the user to get the device's location.
         * This could be done in the Service itself
         * but the Activity is where the user interacts with the UI
         */
        //query the current permission state (has it already been granted? pending?)
        int permissionState = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionState != PackageManager.PERMISSION_GRANTED){
            //in this case, permission has not been granted, so request the permission
            System.out.println("We do not have ACCESS_FINE_LOCATION permission; requesting permission...");
            String [] requestedPermissions = new String[] {Manifest.permission.ACCESS_FINE_LOCATION};
            int requestCode = 1234;
            requestPermissions(requestedPermissions, requestCode);
        } else {
            //happy path
            System.out.println("ACCESS_FINE_LOCATION permission has been granted, starting service...");
            //we can start MyService with a explicit Intent
            Intent intent = new Intent(this, MyService.class);
            //start the service by calling Context.startService()
            startService(intent);
        }


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
        //alter the textview in the Activity to show the user that the intent was recieved
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(intent.toString());
        //TODO: process extras in the intent (if needed)
    }
}
