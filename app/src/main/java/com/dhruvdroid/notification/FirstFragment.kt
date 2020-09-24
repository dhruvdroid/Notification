package com.dhruvdroid.notification

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
            startTimer(SystemClock.elapsedRealtime())
        }
    }

    private fun startTimer(startTime: Long) {
        chronometer.base = startTime
        chronometer.setOnChronometerTickListener {
            Log.i("Timer--->", "Countdown---> " + chronometer.base)
            // formattedTime = getFormattedTime(chronometer.base)
        }
        chronometer.start()
    }

    override fun onResume() {
        super.onResume()
        if (MainActivity.appPauseTime > 0L) {
            Log.i(FirstFragment::class.java.name, "App Resume Time == ${MainActivity.appPauseTime}")
            Log.i(
                FirstFragment::class.java.name,
                "App Resume formatted time == ${getFormattedTime(MainActivity.appPauseTime)}"
            )
            startTimer(MainActivity.appPauseTime + (MainActivity.appResumeTime - MainActivity.appPauseTime))
            val intent = Intent(activity, MyTimerService::class.java)
            intent.action = MyTimerService.STOP_FOREGROUND_SERVICE
            activity?.stopService(intent)
        }
    }

    private fun getTimeInSeconds(appResumeTime: Long): Int {
        //val time: Long = SystemClock.elapsedRealtime() - appResumeTime
        val hour = (appResumeTime / 3600000).toInt()
        val minutes = (appResumeTime - hour * 3600000).toInt() / 60000
        return (appResumeTime - hour * 3600000 - minutes * 60000).toInt() / 1000
    }

    override fun onPause() {
        super.onPause()
        MainActivity.appPauseTime = SystemClock.elapsedRealtime()
        Log.i(FirstFragment::class.java.name, "App Pause Time == $MainActivity.appPauseTime")
        val intent = Intent(activity, MyTimerService::class.java)
        intent.action = MyTimerService.START_FOREGROUND_SERVICE
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

}