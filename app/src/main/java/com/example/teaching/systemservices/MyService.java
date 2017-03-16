package com.example.teaching.systemservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

    /**
     * default constructor is called when the service is first started
     * AVOID CALLING ANY ANDROID APIs in this constructor
     * you can do other setup work though.
     */
    public MyService() {
        System.out.println("MyService ctor");
    }

    @Override
    /**
     * this is a better place to do initialization work.
     * it's called whenever an Intent is broadcast that would start this service
     */
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Service was started successfully!");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();


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
