package com.dhruvdroid.notification

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_first.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var notificationBuilder: Notification? = null
    private lateinit var formattedTime: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            startTimer()
        }
    }

    private fun startTimer() {
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.setOnChronometerTickListener {
            Log.i("Timer--->", "Countdown---> " + chronometer.base)
            // formattedTime = getFormattedTime(chronometer.base)
        }
        chronometer.start()
    }

    override fun onPause() {
        super.onPause()
        val intent = Intent(activity, MyTimerService::class.java)
        intent.setAction(MyTimerService.START_FOREGROUND_SERVICE)
        intent.putExtra("timer", chronometer.base)
        activity?.startService(intent)
    }

    private fun getFormattedTime(base: Long): String {
        val time: Long = SystemClock.elapsedRealtime() - base
        val hour = (time / 3600000).toInt()
        val minutes = (time - hour * 3600000).toInt() / 60000
        val seconds = (time - hour * 3600000 - minutes * 60000).toInt() / 1000
        val hh = if (hour < 10) "0$hour" else hour.toString() + ""
        val mm = if (minutes < 10) "0$minutes" else minutes.toString() + ""
        val ss = if (seconds < 10) "0$seconds" else seconds.toString() + ""
        Log.i("Timer--->", "Countdown---> $hh:$mm:$ss")
        return "Countdown---> $hh:$mm:$ss"
    }

    /*private fun getPendingIntent(): PendingIntent? {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, 0)


    }

    private fun createNotification() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                name,
                importance
            ).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Get the layouts to use in the custom notification
        val notificationLayout = RemoteViews(activity?.packageName, R.layout.notification_layout)
        // val notificationLayoutExpanded = RemoteViews(packageName, R.layout.notification_large)

        // Apply the layouts to the notification
        notificationBuilder = context?.let {
            NotificationCompat.Builder(
                it, NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setContentText("Active Session")
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setContentTitle("Notification")
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
        }
    }

    private fun showNotification() {
        with(context?.let { NotificationManagerCompat.from(it) }) {
            // notificationId is a unique int for each notification that you must define
            notificationBuilder?.let { this?.notify(100, it) }
        }
    }*/
}