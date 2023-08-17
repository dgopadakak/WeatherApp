package com.example.weatherapp

data class Dates(
    val dates: ArrayList<String>,
    val avgTemp: ArrayList<Double>,
    val wind: ArrayList<Double>,
    val humidity: ArrayList<Double>,
    val condition: ArrayList<String>,
    val iconUrl: ArrayList<String>
)
