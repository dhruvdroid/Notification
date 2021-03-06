package com.dhruvdroid.notification

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


//
// Created by  on 23/09/20.
//

class MyTimerService : Service() {

    private lateinit var notificationView: RemoteViews
    private lateinit var notiBuilder: Notification

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "100"
        const val NOTIFICATION_ID = 10
        const val START_FOREGROUND_SERVICE = "start_foreground_service"
        const val STOP_FOREGROUND_SERVICE = "stop_foreground_service"
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationView = RemoteViews(packageName, R.layout.notification_layout)
        createNotification()
    }

    private fun getPendingIntent(): PendingIntent? {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        // intent.putExtra("timer", notificationView.)
        return PendingIntent.getActivity(this, 0, intent, 0)


    }

    private fun createNotification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelForOreo()
        } else {
            createNotificationBelowOreo()
        }
    }

    private fun createNotificationBelowOreo() {
        // Create notification builder.
        val builder = NotificationCompat.Builder(this)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationView)
            .setContentIntent(getPendingIntent())
            .setContentTitle(baseContext.getString(R.string.app_name))
            .setContentText("Active Session")
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_MAX)

        // Build the notification.
        notiBuilder = builder.build()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannelForOreo() {
//        val resultIntent = Intent(this, MainActivity::class.java)
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
//        val stackBuilder = TaskStackBuilder.create(this)
//        stackBuilder.addNextIntentWithParentStack(resultIntent)
//        val resultPendingIntent =
//            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val chan =
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                baseContext.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
        // chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notiBuilder = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setContentTitle(baseContext.getString(R.string.app_name))
            .setContentText("Active Session")
            .setAutoCancel(true)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationView)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(getPendingIntent())
            .build()
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }


    private fun showNotification() {
        // Start foreground service.
        startForeground(NOTIFICATION_ID, notiBuilder)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent?.extras != null) {
            when (intent.action) {

                START_FOREGROUND_SERVICE -> {
                    val timestamp = intent.getLongExtra("timer", 0)
                    // set chronometer
                    notificationView.setChronometer(R.id.chronometer, timestamp, null, true)
                    // show notification
                    showNotification()
                }

                STOP_FOREGROUND_SERVICE -> {
                    stopForeground(true)
                    stopSelf()
                }
            }
        }
        return START_STICKY
    }
}