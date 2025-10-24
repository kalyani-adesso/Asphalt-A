package com.asphalt.resetpassword.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asphalt.commonui.util.CustomTimer
import com.asphalt.resetpassword.model.ResetConstants

class VerifyCodeViewModel : ViewModel() {
    private val _verificationCodeMutable = mutableStateOf("")
    val verificationCode: State<String> = _verificationCodeMutable

    val isShowError = mutableStateOf(false)

    val showTime = mutableStateOf("")

    val showResend = mutableStateOf(false)
    var countDownTimer: CountDownTimer? = null

    fun startTimer() {
        countDownTimer?.cancel()
        showResend.value = false
        countDownTimer = CustomTimer.createCountDownTimer(
            ResetConstants.Total_Timer_Time,
            ResetConstants.Time_Interval, onTick = { count ->
                showTime.value = count.toString()
                //Log.d("Countdown", "Time remaining: $count seconds")
            }, onFinish = {
                showResend.value = true
            }).start()
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
    }

    fun updateCode(code: String) {
        isShowError.value = false
        _verificationCodeMutable.value = code

    }

    fun verifyCode(): Boolean {
        if (validation()) {
            return true
        }
        return false
    }

    fun validation(): Boolean {
        if (_verificationCodeMutable.value.isNullOrEmpty()) {
            isShowError.value = true
            return false;
        }
        isShowError.value = false
        return true
    }
}