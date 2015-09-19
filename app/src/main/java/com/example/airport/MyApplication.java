package com.example.airport;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

public class MyApplication extends Application {

    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = new BeaconManager(getApplicationContext());
        //beaconManager.setBackgroundScanPeriod(5000,1000);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {

                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    if(nearestBeacon.getMajor() == 41427) {
                        showNotification(
                                "Found beacon with colour","BLUE");
                        Log.d("Naval", "BLUE beacon");
                    }
                    if(nearestBeacon.getMajor() == 11461) {
                        showNotification(
                                "Found beacon with colour","GREEN");
                        Log.d("Naval", "GREEN beacon");
                    }
                    if(nearestBeacon.getMajor() == 59044) {
                        showNotification(
                                "Found beacon with colour","PURPLE");
                        Log.d("Naval", "PURPLE beacon");
                    }
                }
            }
            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.setBackgroundScanPeriod(5000,0);
                    beaconManager.startMonitoring(new Region("monitored region",
                            "B9407F30-F5F8-466E-AFF9-25556B57FE6D", null, null));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
