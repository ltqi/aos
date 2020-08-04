package com.aos.app2.ui.dashboard

import com.aos.app2.api.HomeRepository
import com.aos.app2.api.SquareRepository
import com.aos.app2.ui.home.ListViewModel

class DashboardViewModel(homeRepository: HomeRepository, squareRepository: SquareRepository) : ListViewModel(homeRepository, squareRepository)