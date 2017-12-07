package com.groupproject.Controller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.groupproject.R;

public class NotificationService extends Service {
    private PowerManager.WakeLock mWakeLock;

    /**
     * Simply return null, since our Service will not be communicating with
     * any other components. It just does its work silently.     *
     */
    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * This is where we initialize. We call this when onStart/onStartCommand is
     * called by the system. We won't do anything with the intent here, and you
     * probably won't, either.
     */
    private void handleIntent(Intent intent) {
        // obtain the wake lock
         PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
         mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wakelock");
         mWakeLock.acquire(10*60*1000L /*10 minutes*/);
         // check the global background data setting
         ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
         if (cm == null || cm.getActiveNetworkInfo() == null) {
             stopSelf();
             return;
         }
         // do the actual work, in a separate thread
         new PollTask().execute();
    }

    private class PollTask extends AsyncTask<Void, Void, Void> {
        /**
         * This is where YOU do YOUR work. There's nothing for me to write here
         * you have to fill this in. Make your HTTP request(s) or whatever it is
         * you have to do to get your updates in here, because this is run in a
         * separate thread
         */
        @Override protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * In here you should interpret whatever you fetched in doInBackground
         * and push any notifications you need to the status bar, using the
         * NotificationManager. I will not cover this here, go check the docs on
         * NotificationManager.
         * What you HAVE to do is call stopSelf() after you've pushed your
         * notification(s). This will:
         * 1) Kill the service so it doesn't waste precious resources
         * 2) Call onDestroy() which will release the wake lock, so the device
         * can go to sleep again and save precious battery.
         */
        @Override protected void onPostExecute(Void result) {
            sendNotification();
            stopSelf();
        }
    }

     /**
      * This is called on 2.0+ (API level 5 or higher). Returning
      * START_NOT_STICKY tells the system to not restart the service if it is
      * killed because of poor resource (memory/cpu) conditions.
      */
     @Override public int onStartCommand(Intent intent, int flags, int startId) {
         handleIntent(intent);
         return START_NOT_STICKY;
     }

     /**
      * In onDestroy() we release our wake lock. This ensures that whenever the
      * Service stops (killed for resources, stopSelf() called, etc.), the wake
      * lock will be released.
      */
     public void onDestroy() {
         super.onDestroy();
         mWakeLock.release();
     }

     private void sendNotification() {
         NotificationCompat.Builder mBuilder =
                 new NotificationCompat.Builder(this)
                         .setContentTitle("My notification")
                         .setContentText("Hello World!");
         // Sets an ID for the notification
         int mNotificationId = 1;
         // Gets an instance of the NotificationManager service
         NotificationManager mNotifyMgr =
                 (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
         // Builds the notification and issues it.
         mNotifyMgr.notify(mNotificationId, mBuilder.build());

     }
}
