package com.login.hofo;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


@SuppressLint("Registered")
public class FirebaseService extends FirebaseMessagingService {

    String TAG = "FirebaseService";

    //para notificaciones push
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";

    String api_token;
    private final int IDENTIDICADOR_NOTIFICACION = 916473;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//               //7 scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//               //7 handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        generateNotification();
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //7 sendRegistrationToServer(token);
    }


    //Funciones para notificaciones push

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void generateNotification()
    {
        setPendingIntent();
        createNotification();
        createNotificationChannel();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setPendingIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("notification_id", IDENTIDICADOR_NOTIFICACION);
        intent.setAction("daily_" + IDENTIDICADOR_NOTIFICACION); // para crear un intent unico y que no se sobreescriba por la etiqueta FLAG_UPDATE_CURRENT (probar con PendingIntent.FLAG_ONE_SHOT)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion_"+IDENTIDICADOR_NOTIFICACION;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);

        builder.setContentTitle("Tienes un nuevo servicio");

        builder.setContentText("da click y revisa!");
        builder.setColor(Color.BLACK);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(IDENTIDICADOR_NOTIFICACION, builder.build());
    }
    //fin finciones para notificaciones push

}
