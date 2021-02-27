package com.learning.pushnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.learning.pushnotification.data.Config
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = MyFirebaseMessagingService::class.java.simpleName

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG,"New Token:::: ${p0}")
        sendRegistrationTokenToServer(p0)
    }

    private fun sendRegistrationTokenToServer(token: String){
        Log.d(TAG,"Refreshed Token ::: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if(remoteMessage.data!=null){
            getImage(remoteMessage)
        }
    }

     fun sendNotification(bitmap: Bitmap){
        val style = NotificationCompat.BigPictureStyle()
         style.bigPicture(bitmap)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "101"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.description = "Game Notifications"
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Config.title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(Config.content)
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setLargeIcon(bitmap)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)

        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun getImage(remoteMessage: RemoteMessage){
        val data = remoteMessage.data
        Config.title =  data.get("title")!!
        Config.content = data.get("content")!!
        Config.imageUrl = data.get("imageUrl")!!
        Config.gameUrl = data.get("gameUrl")!!

        if(remoteMessage.data!=null ){
            Log.d(TAG,"message data: ${remoteMessage.data}")
            val uiHandler = Handler(Looper.getMainLooper())
            val bitmap = Picasso.get()
                    .load(Config.imageUrl).get()
            uiHandler.post{
                sendNotification(bitmap)
            }

        }
    }

}