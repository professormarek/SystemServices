package com.example.teaching.systemservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * in this code sample we're going to demonstrate
 * 1) how to start and stop a service.
 */
public class MainActivity extends AppCompatActivity {

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
}
