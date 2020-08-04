package com.aos.app2.ui.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aos.app2.api.NavigationRepository
import com.aos.app2.bean.Navigation
import com.aos.life.base.BaseViewModel
import com.aos.life.model.bean.checkSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NavigationViewModel(private val navigationRepository: NavigationRepository) : BaseViewModel() {

    private val _uiState = MutableLiveData<List<Navigation>>()
    val uiState : LiveData<List<Navigation>>
        get() = _uiState

    fun getNavigation() {
        launchUI(block = {
            val result = withContext(Dispatchers.IO) { navigationRepository.getNavigation() }
            result.checkSuccess {
                _uiState.value = it
            }
        })
    }
}