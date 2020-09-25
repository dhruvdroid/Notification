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
            startTimer(MainActivity.appPauseTime)
            val intent = Intent(activity, MyTimerService::class.java)
            intent.action = MyTimerService.STOP_FOREGROUND_SERVICE
            activity?.stopService(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        MainActivity.appPauseTime = chronometer.base
        Log.i(FirstFragment::class.java.name, "App Pause Time == $MainActivity.appPauseTime")
        val intent = Intent(activity, MyTimerService::class.java)
        intent.action = MyTimerService.START_FOREGROUND_SERVICE
        intent.putExtra("timer", chronometer.base)
        activity?.startService(intent)
    }
}