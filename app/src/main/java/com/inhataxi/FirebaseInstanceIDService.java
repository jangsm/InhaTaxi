package com.inhataxi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inhataxi.activities.SplashActivity;

public class FirebaseInstanceIDService extends FirebaseMessagingService {
    String deviceToken1 = "eJ_0jTM7TdM:APA91bHtWfH_EvQHcZYgIJpF3Q1AGX8R6Fz-6zQQKd7uKwyXtDXj5uJ1Bv9-dOnB_3418M217ZdTGJdlpzvSxX094E4Itgot_Ak3j4Dt_B9iVuVDv-n6nlhHKvay7YAYd9iXfs2TNjqx";
    String deviceToken2 = "f0XIxR15GQ0:APA91bHmdG8XGZg2UqH5Y5DYYIBcnAiyZOq1flFF09dLJXHTy8XYPOT8Tj_u38ZAx55IwvnxCMlijqFvmgFy5AmK8B-1bZFkxFnRH2qQ1p3uioGqgzk78jFDIU4Bn5TB1i_CK4fDuRzT";
    Boolean warningMode  = false;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("Firebase", "FirebaseInstanceIDService : " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        System.out.println("onMessageReceived called");

        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage){
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String image = remoteMessage.getData().get("image");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channel = "채널";
            String channelName = "채널명";

            NotificationManager notichannel = (android.app.NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channelMessage = new NotificationChannel(channel,channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channelMessage.setDescription("채널에 대한 설명.");
            channelMessage.enableLights(true);
            channelMessage.enableVibration(true);
            channelMessage.setShowBadge(false);
            channelMessage.setVibrationPattern(new long[]{100, 200, 100, 200});
            notichannel.createNotificationChannel(channelMessage);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channel)
                            .setSmallIcon(R.drawable.search_icon)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setChannelId(channel)
                            .setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(9999, notificationBuilder.build());

            if(body.equals("경고모드입니다")) {
                Log.i("ICU", "경고모드");
                Intent intent = new Intent(this, SplashActivity.class);
                intent.putExtra("image", image);
                Log.i("ICU", image);
                warningMode = true;
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }else if(warningMode == true &&  body.equals("신고모드입니다")){
                Log.i("ICU", "신고모드");
                Intent intent = new Intent(this, SplashActivity.class);
                warningMode = false;
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }else {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(9999, notificationBuilder.build());
        }
    }
}