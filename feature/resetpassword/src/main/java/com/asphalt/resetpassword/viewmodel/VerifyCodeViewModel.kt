package com.asphalt.resetpassword.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import com.asphalt.commonui.util.CustomTimer
import com.asphalt.resetpassword.model.ResetConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VerifyCodeViewModel : ViewModel() {
    private val _verificationCodeMutable = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCodeMutable

    private val _isShowError = MutableStateFlow(false)
    val isShowError: StateFlow<Boolean> = _isShowError

    private val _showTime = MutableStateFlow("")
    val showTime: StateFlow<String> = _showTime

    private val _showResend = MutableStateFlow(false)
    val showResend: StateFlow<Boolean> = _showResend

    var countDownTimer: CountDownTimer? = null

    fun startTimer() {
        countDownTimer?.cancel()
        _showResend.value = false
        countDownTimer = CustomTimer.createCountDownTimer(
            ResetConstants.Total_Timer_Time,
            ResetConstants.Time_Interval, onTick = { count ->
                _showTime.value = count.toString()
                //Log.d("Countdown", "Time remaining: $count seconds")
            }, onFinish = {
                _showResend.value = true
            }).start()
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
    }

    fun updateCode(code: String) {
        _isShowError.value = false
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
            _isShowError.value = true
            return false;
        }
        _isShowError.value = false
        return true
    }
}
