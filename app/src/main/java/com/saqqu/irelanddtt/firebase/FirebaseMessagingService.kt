package com.saqqu.irelanddtt.firebase

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.saqqu.irelanddtt.ui._main.MainActivity
import android.net.Uri;
import androidx.annotation.RequiresApi


class FirebaseMessagingService: FirebaseMessagingService() {

    private val TAG = "FirebaseMessagingService"

    override fun onCreate() {
        super.onCreate()

        //TODO: Add functions to communicate through broadcast when the app is open or have a running service

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("Firebase Token", token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.getData().size > 0) {
            Log.d(TAG, "Message data payload: " + message.getData())
            if ( /* Check if data needs to be processed by long running job */false) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                //handleNow()
                sendNotification(message.data["body"]!!, message.data["title"]!!)
            }
        }

        // Check if message contains a notification payload.

        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + message.getNotification()!!.getBody())
            val notificationBody: String = message.getNotification()!!.getBody()!!
            if (message.getNotification()!!.getBody() != null) {
                sendNotification(notificationBody, message.notification?.title!!)
            }
        }

    }

    private fun scheduleJob() {
        // [START dispatch_job]
        //val work: OneTimeWorkRequest = Notification.Builder(MyWorker::class.java)
          //  .build()
        //WorkManager.getInstance(this).beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(messageBody: String, title: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = getString(com.saqqu.irelanddtt.R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: Notification.Builder = Notification.Builder(this, channelId)
            .setSmallIcon(com.saqqu.irelanddtt.R.drawable.ic_home)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(false)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}