package com.aos.app.arch

import android.view.View
import androidx.lifecycle.*
import com.aos.app.ui.base.AppViewModel

class LiveDataFragViewModel : AppViewModel() {

    val postalCode1 = liveData<String> {
//        getPostalCode1("1") // no suspend

        val pcode = getPostalCode1("1")
        pcode.value?.apply { emit(this) }
    }

    suspend fun getPostalCode1(address: String): LiveData<String> {
        // DON'T DO THIS
        return getPostCode(address)
    }

    private fun getPostCode(address: String)
        = when(address) {
            "1" -> MutableLiveData<String>().apply { value = "100" }
            "2" -> MutableLiveData<String>().apply { value = "200" }
            else -> MutableLiveData<String>().apply { value = "000" }
        }


    fun update(view: View) {
//        content.value = "setValue按钮更新的内容"
    }

    //DO THIS
    private val addressInput = MutableLiveData<String>()
    val postalCode: LiveData<String> = Transformations.switchMap(addressInput) {
            address -> /*repository.*/getPostCode(address) }

    private fun setInput(address: String) {
        addressInput.value = address
    }

}