package com.example.weatherapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.forrecyclerview.CustomRecyclerAdapterForDays
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException

class MainActivity : AppCompatActivity()
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var editTextCity: EditText

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.progressBar)
        editTextCity = findViewById(R.id.editTextCity)
        findViewById<Button>(R.id.buttonOk).setOnClickListener { doRequest() }
    }

    private fun doRequest()
    {
        if (editTextCity.text.toString().trim() != "")
        {
            val httpThread: Thread = HttpThread(editTextCity.text.toString().trim())
            httpThread.start()
            progressBar.visibility = View.VISIBLE
        }
    }

    private inner class HttpThread(cityName: String) : Thread()
    {
        private val client = OkHttpClient()
        private val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.weatherapi.com")
            .addPathSegment("v1")
            .addPathSegment("forecast.json")
            .addQueryParameter("q", cityName)
            .addQueryParameter("days", "5")
            .addQueryParameter("key", "fe8e617c885e4cda976150431231708")
            .build()

        private val request: Request = Request.Builder()
            .url(url)
            .build()


        override fun run()
        {
            val response: Response = client.newCall(request).execute()
            try
            {
                progressBar.visibility = View.INVISIBLE
                if (!response.isSuccessful)
                {
                    throw IOException("Запрос к серверу не был успешен:" +
                            " ${response.code()} ${response.message()}")
                }

                // Получаем данные
                val dates = Dates(ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList())
                var root = JsonParser.parseString(response.body().string()).asJsonObject
                root = root["forecast"] as JsonObject
                val rootArray = root["forecastday"] as JsonArray
                for (i in 0 until 5)        // Парсим
                {

                    dates.dates.add(rootArray[i].asJsonObject["date"].asString)
                    dates.avgTemp.add(rootArray[i].asJsonObject["day"].asJsonObject["avgtemp_c"].asDouble)
                    dates.timeUp.add(rootArray[i].asJsonObject["astro"].asJsonObject["sunrise"].asString)
                    dates.timeDown.add(rootArray[i].asJsonObject["astro"].asJsonObject["sunset"].asString)
                    dates.iconUrl.add("https://" + rootArray[i].asJsonObject["day"].asJsonObject["condition"].asJsonObject["icon"].asString.substring(2))
                }
                runOnUiThread {
                    recyclerView.adapter = CustomRecyclerAdapterForDays(dates)
                }
            }
            catch (e: IOException)
            {
                Snackbar.make(findViewById(R.id.buttonOk),
                    "Ошибка!\n" +
                            "Проверьте правильность запроса",
                    Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.RED)
                    .show()
            }
            finally
            {
                response.body().close()
            }
        }
    }
}