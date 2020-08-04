package com.aos.app2.di

import com.aos.app2.api.CRetrofitClient
import com.aos.app2.api.NavigationRepository
import com.aos.app2.ui.dashboard.DashboardViewModel
import com.aos.app2.ui.home.HomeViewModel
import com.aos.app2.ui.main.App2MainViewModel
import com.aos.app2.ui.notifications.NotificationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by:  qiliantao on 2020.08.02
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */

val repositoryModule = module {
    single { CRetrofitClient }
    single { NavigationRepository() }


}


val viewModelModule = module {
    viewModel { App2MainViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { DashboardViewModel() }
    viewModel { NotificationsViewModel() }
}

val appModule = listOf(repositoryModule, viewModelModule)