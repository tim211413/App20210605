package com.study

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import com.study.model.OpenWeather
import com.study.service.OpenWeatherService
import com.study.viewmodel.OpenWeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: OpenWeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(OpenWeatherViewModel::class.java)


        // 觀察者模式
        viewModel.currentImageURL.observe(this, Observer {
            // 若 viewModel.currentNumber 發生變化, 要如何處理 ?
            Picasso.get().load(it).into(iv_icon) // it 就是發生變化的值
        })

        viewModel.currentLog.observe(this, Observer {
            tv_log.text = it.toString()
        })
    }

    fun changeOpenWeather(view: View) {
        val q = view.tag.toString()
        Toast.makeText(applicationContext, q, Toast.LENGTH_SHORT).show()

        GlobalScope.launch {
            val appid = resources.getString(R.string.appid)
            val path = resources.getString(R.string.path)
            viewModel.ow = OpenWeatherService(appid, path).getOpenWeather(q)

            runOnUiThread {
                viewModel.currentImageURL.value = viewModel.ow!!.weather_icon_url.toString()
                viewModel.currentLog.value = viewModel.ow.toString()
            }
        }


    }
}