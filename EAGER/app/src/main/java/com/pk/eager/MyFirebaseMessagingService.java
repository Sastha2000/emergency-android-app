package com.pk.eager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by kimpham on 7/17/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    final static String KEY = "key";
    final static String TAG = MyFirebaseMessagingService.class.getSimpleName();
    final static String EAGER = "EAGER";
    DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("UserNotification");


    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG, "onMessageReceived Called");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){

            //push the notification to user's list of notifications
            DatabaseReference newNode = notificationRef.child(user.getUid()).push();
            newNode.setValue(remoteMessage.getData());
            Log.d(TAG, "remort msg "+ remoteMessage);
            Log.d(TAG, "data " + remoteMessage.getData());

            //issue a notification to user
            String body = remoteMessage.getData().get("body");
            String key = remoteMessage.getData().get("key");

            Log.d(TAG, "Body " + remoteMessage.getData().get("body"));
            Log.d(TAG, "Key " + remoteMessage.getData().get("key"));


            String clickAction = "OPEN_VIEW_NOTIFICATION";
            Intent intent = new Intent(clickAction);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(KEY, key);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setContentTitle(EAGER);
            notificationBuilder.setContentText(body);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSmallIcon(R.drawable.ic_notification);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }


        /*
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String clickAction = remoteMessage.getNotification().getClickAction();
        Intent intent = new Intent(clickAction);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(KEY, title);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(body);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.ic_notification);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());*/
    }


}
