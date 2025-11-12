package com.asphalt.android.viewmodel

import androidx.lifecycle.ViewModel
import com.asphalt.android.network.KtorClient

class SharedViewModel(
    private val ktorClient: KtorClient
)  : ViewModel() {

//    suspend fun Registration() = ktorClient.Registration()

}