package com.aos.app2.di

import com.aos.app2.api.CRetrofitClient
import com.aos.app2.api.HomeRepository
import com.aos.app2.api.NavigationRepository
import com.aos.app2.api.SquareRepository
import com.aos.app2.ui.dashboard.DashboardViewModel
import com.aos.app2.ui.home.ListViewModel
import com.aos.app2.ui.main.App2MainViewModel
import com.aos.app2.ui.navigation.NavigationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by:  qiliantao on 2020.08.02
 * email     :  qiliantao@mockuai.com
 * Describe  :
 */

val repositoryModule = module {
    single { CRetrofitClient }
    single { HomeRepository() }
    single { SquareRepository() }
    single { NavigationRepository() }


}


val viewModelModule = module {
    viewModel { App2MainViewModel() }
    viewModel { ListViewModel(get(), get()) }
    viewModel { DashboardViewModel(get(), get()) }
    viewModel { NavigationViewModel(get()) }
}

val appModule = listOf(repositoryModule, viewModelModule)