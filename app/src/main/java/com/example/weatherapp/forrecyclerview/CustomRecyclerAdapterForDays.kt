package com.example.weatherapp.forrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Dates
import com.example.weatherapp.R

class CustomRecyclerAdapterForDays(private val dates: Dates):
    RecyclerView.Adapter<CustomRecyclerAdapterForDays.MyViewHolder>()
{
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val textViewTemp: TextView = itemView.findViewById(R.id.textViewTemp)
        val textViewCondition: TextView = itemView.findViewById(R.id.textViewCondition)
        val textViewMaxwind: TextView = itemView.findViewById(R.id.textViewMaxwind)
        val textViewHumidity: TextView = itemView.findViewById(R.id.textViewHumidity)
        val webViewClimate: WebView = itemView.findViewById(R.id.webView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dates.dates.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.textViewDate.text = dates.dates[position]
        holder.textViewTemp.text = dates.avgTemp[position].toString()
        holder.textViewMaxwind.text = dates.wind[position].toString()
        holder.textViewHumidity.text = dates.humidity[position].toString()
        holder.textViewCondition.text = dates.condition[position]
        holder.webViewClimate.loadUrl(dates.iconUrl[position])
    }
}