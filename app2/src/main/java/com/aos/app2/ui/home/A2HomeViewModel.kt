package com.aos.app2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aos.app2.api.NavigationRepository
import com.aos.life.base.BaseViewModel
import com.aos.app2.bean.Navigation
import com.aos.app2.ui.home.tab.SectionsPagerAdapter
import com.aos.life.model.bean.CResult

class A2HomeViewModel(val repository: NavigationRepository) : BaseViewModel() {

    private val _uiState = MutableLiveData<List<Navigation>>()
    val uiState : LiveData<List<Navigation>>
        get() = _uiState

    val pagerAdapter  = MutableLiveData<SectionsPagerAdapter>()


    fun getNavigation() {
        launch(block = {
            repository.getNavigation()
        }, resultFail = {}, result = {
            _uiState.value = it.data
        })
//        launchOnUI {
//            val result = withContext(Dispatchers.IO) { navigationRepository.getNavigation() }
//            result.checkSuccess {
//                _uiState.value = it
//            }
//        }
    }
}