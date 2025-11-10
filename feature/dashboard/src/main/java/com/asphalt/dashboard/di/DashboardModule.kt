package com.asphalt.dashboard.di

import com.asphalt.dashboard.repository.AdventureJourneyRepo
import com.asphalt.dashboard.repository.PerMonthRideStatsRepo
import com.asphalt.dashboard.repository.PlaceVisitedGraphRepo
import com.asphalt.dashboard.viewmodels.AdventureJourneyViewModel
import com.asphalt.dashboard.viewmodels.DashboardRideInviteViewModel
import com.asphalt.dashboard.viewmodels.NotificationViewModel
import com.asphalt.dashboard.viewmodels.PerMonthRideStatsViewModel
import com.asphalt.dashboard.viewmodels.PlacesVisitedGraphViewModel
import com.asphalt.dashboard.viewmodels.RidesDetailsViewModel
import com.asphalt.dashboard.viewmodels.RidesScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dashboardModule = module {
    single { PerMonthRideStatsRepo() }
    single { AdventureJourneyRepo() }
    single { PlaceVisitedGraphRepo() }
    viewModel { PerMonthRideStatsViewModel(get()) }
    viewModel { DashboardRideInviteViewModel(get(),get()) }
    viewModel { AdventureJourneyViewModel(get()) }
    viewModel { PlacesVisitedGraphViewModel(get()) }
    viewModel { NotificationViewModel() }
    viewModel { RidesScreenViewModel(get()) }
    viewModel { RidesDetailsViewModel() }

}