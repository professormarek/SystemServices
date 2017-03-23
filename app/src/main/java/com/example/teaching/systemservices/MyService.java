package com.example.teaching.systemservices;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    int notifcationCount;

    //this instance of LocationListener will be userd to recieve location updates
    LocationListener myLocationListener = null;

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
        //a string to hold the notifaction message for the user
        String message = "Location not being tracked...";

        /**
         * request location updates from the Network or GPS provider
         * (note: this will only work if we have permission - otherwise the app would crash)
         */
        //verify that we have permission
        int permissionState = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        //request location only if PERMISSION_GRANTED
        if(permissionState == PackageManager.PERMISSION_GRANTED){
            //let's instantiate myLocationListener and override its behaviour
            //i.e. anonymous class pattern
            myLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //call a handler method in our Service class
                    updateLocation(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    //TODO: handle provider change status
                }

                @Override
                public void onProviderEnabled(String provider) {
                    //TODO: handle provider becoming enabled
                }

                @Override
                public void onProviderDisabled(String provider) {
                    //TODO: handle provider becoming disabled
                }
            };

            //get a reference to LocationManager
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //use myLocationListener to recieve updates from LocationManager
            //request updates from locationManager
            //arguments are: location provider, update interval in ms, minimum distance in m, a LocationListener
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1, myLocationListener);
            message = "This app is tracking your location mwa ha ha ha";
        }

        //instead of a toast, show a notification to the user when the service starts
        //use the Notification.Builder class to construct a notification
        Notification.Builder myBuilder = new Notification.Builder(this).
                setSmallIcon(R.mipmap.ic_launcher). //ic_launcher is one of the built in icons
                setContentTitle("My service was started!").
                setContentText(message);
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
        //TODO: deregister from LocationManager!

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateLocation(Location location){
        //TODO: do something with the location - i.e. broadcast the intent as needed
        System.out.println("Recieved location update " + location.getLatitude() + " " + location.getLongitude());
    }
}
