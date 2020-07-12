package com.aos.app.ui.origin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aos.app.databinding.AOriginVmBinding
import com.aos.app.dto.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OriginVmActivity : AppCompatActivity() {

//    companion object {
//        fun start(context: Context) {
//            Intent(context, OriginVmActivity::class.java).apply {
//                context.startActivity(this)
//            }
//        }
//    }

    lateinit var binding: AOriginVmBinding
    lateinit var viewModelProvider: ViewModelProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //不用viewModel
//        binding = AOriginVmBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        val user = User("吾王",28)
//        binding.user = user


        //用viewModel
        binding = AOriginVmBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding = DataBindingUtil.setContentView(this, R.layout.a_origin_vm)
        viewModelProvider = ViewModelProvider(this)
//        val viewModel: OriginViewModel by viewModels()
        binding.user = User().apply {
            name = "大王更新"
            age = 18
        }
        binding.viewModel = viewModelProvider[OriginViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel?.users?.observe(this, Observer {
            // update UI
//            it?.apply {
//                tvUser.text = it[0].name + it[0].age //和绑定2选1
//            }

            (binding.user as? User)?.name = "名字：" + it[0].name
            (binding.user as? User)?.age = it[0].age
        })

//        val result = getSharedPreferences(packageName, Context.MODE_PRIVATE).getInt("restore", -1)
//        if (result == 1) {
////            toast("恢复了")
//        }


    }


    override fun onDestroy() {
        super.onDestroy()
////        toast("被杀死了")
//        getSharedPreferences(packageName, Context.MODE_PRIVATE).edit(true) {
//            putInt("restore", 1)
//        }
    }


    private suspend fun getTextFromNet() = withContext(Dispatchers.IO) {
        request("github.com")
    }

    private fun request(url: String): Book {
        return Book("kotlin program technology", "qlt")
    }
}
