package com.groupproject.Controller;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.Query;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.DataBaseItem;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationService extends Service implements DataBaseCallBacks<String> {

    private PowerManager.WakeLock mWakeLock;
    private DataBaseAPI dataBaseAPI;

    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleIntent(Intent intent) {
        // obtain the wake lock
         PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
         mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wakelock");
         mWakeLock.acquire(10*60*1000L /*10 minutes*/);
         dataBaseAPI = DataBaseAPI.getDataBase();
         // check the global background data setting
         ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
         if (cm == null || cm.getActiveNetworkInfo() == null) {
             stopSelf();
             return;
         }
         // do the actual work, in a separate thread
         new PollTask().execute();
    }

    @Override
    public void getUser(User user, ViewHolder holder) {

    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {

    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {

    }

    @Override
    public void executeQuery(List<String> result, SearchType.Type type) {
        if(type == SearchType.Type.USERS) {
            for (String id : result) {
                dataBaseAPI.getUser(id, this, null);
            }
        } else if(type == SearchType.Type.EVENTS) {
            for (String id : result) {
                dataBaseAPI.getEvent(id, this, null);
            }
        } else if(type == SearchType.Type.GROUPS) {
            for (String id : result) {
                dataBaseAPI.getGroup(id, this, null);
            }
        }
    }

    private class PollTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override protected void onPostExecute(Void result) {
            sendNotification();
            stopSelf();
        }
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
    }

    private void sendNotification() {
//        Query query = dataBaseAPI.getmUserRef().child(dataBaseAPI.getCurrentUserID())
//                .child("unSeenNotifications");
        List<String> notifications = dataBaseAPI.getNotifications();
        for (int i = 0; i < notifications.size(); i++) {
            String a = notifications.get(i);
        }
//        dataBaseAPI.executeQuery(query, this, SearchType.Type.USERS);
//        dataBaseAPI.executeQuery(query, this, SearchType.Type.EVENTS);
//        dataBaseAPI.executeQuery(query, this, SearchType.Type.GROUPS);
    }

    private void buildNotification(String type, String name, int id) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.eventpic)
                        .setContentTitle(type)
                        .setContentText(name);
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(id, mBuilder.build());
    }


}
