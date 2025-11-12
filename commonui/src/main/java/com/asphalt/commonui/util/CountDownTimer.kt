package com.asphalt.commonui.util

import android.os.CountDownTimer
import android.util.Log

object CustomTimer {
    fun createCountDownTimer(
        totalTime: Long,
        interval: Long = 1000L,
        onTick: (Long) -> Unit,
        onFinish: () -> Unit
    ): CountDownTimer {

        val countdownTimer = object : CountDownTimer(totalTime, interval) {

            // Called after every 'intervalMillis'
            override fun onTick(millisUntilFinished: Long) {
                // Calculate seconds remaining
                val secondsRemaining = millisUntilFinished / 1000
                Log.d("Countdown", "Time remaining: $secondsRemaining seconds")
                onTick.invoke(secondsRemaining)

                // Update your UI (e.g., a TextView) here

            }

            // Called when the countdown finishes
            override fun onFinish() {
                onFinish.invoke()
                // Update your UI to show it's finished
                Log.d("Countdown", "DONE!")
            }
        }//.start()
        return countdownTimer;
    }
}