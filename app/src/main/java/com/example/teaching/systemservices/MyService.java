package com.example.teaching.systemservices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    int notifcationCount;

    /**
     * default constructor is called when the service is first started
     * AVOID CALLING ANY ANDROID APIs in this constructor
     * you can do other setup work though.
     */
    public MyService() {
        System.out.println("MyService ctor");
        notifcationCount = 0;
    }

    @Override
    /**
     * this is a better place to do initialization work.
     * it's called whenever an Intent is broadcast that would start this service
     */
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Service was started successfully!");
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        //instead of a toast, show a notification to the user when the service starts
        //use the Notification.Builder class to construct a notification
        Notification.Builder myBuilder = new Notification.Builder(this).
                setSmallIcon(R.mipmap.ic_launcher). //ic_launcher is one of the built in icons
                setContentTitle("My service was started!").
                setContentText("This is the body of my notification, blah blah blah");
        //create an implicit intent that will be broadcast when the user clicks on the notification
        Intent myNotificationIntent = new Intent("com.example.marek.NOTIFICATION_CLICKED");
        //create a PendingIntent to package the Intent for later use (save it for later)
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(this, 1234, myNotificationIntent, 0);
        //associate the pending intent with the notification above using Notification.Builder
        myBuilder.setContentIntent(myPendingIntent);
        //get an instance of NotificationManager so we can use it to send the Notification!
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //use the NotificationManager instance to notify the user (fire off the notification)
        notificationManager.notify(notifcationCount++, myBuilder.build()); //myBuilder.build() creates a Notification



        //pay attention: the framework requires this. but you can put it LAST
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        System.out.println("Service was stopped successfully!");
        Toast.makeText(this, "service stopping", Toast.LENGTH_SHORT).show();

        /**
         * WARNING: if another android component (ex. a system service) still has a reference
         * to the service, OR if it is sending Intents to this service, this service won't actually be stopped!
         *
         * MAKE SURE that you de-register from any System Services here ex. AlarmManager, LocationManager etc.
         * also if you have created a BroadcastReciever deregister those here as well!
         */

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
