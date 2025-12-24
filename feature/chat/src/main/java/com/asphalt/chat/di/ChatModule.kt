package com.asphalt.chat.di

import com.asphalt.chat.viewmodel.ChatListViewModel
import com.asphalt.chat.viewmodel.ChatScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {

    viewModel { ChatListViewModel() }
    viewModel { ChatScreenViewModel() }
}