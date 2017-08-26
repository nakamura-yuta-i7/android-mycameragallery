package com.dena.mycamera

import android.app.Application
import android.content.SharedPreferences
import me.mattak.moment.Moment
import java.io.File

class MyApplication : Application() {
    lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences( packageName + "_preferences", MODE_PRIVATE)
    }

    var pictures: MutableList<MyApplication.Picture> = mutableListOf()

    class Picture(
        var thumbnail: ByteArray,
        var file: File,
        var createdAt: Moment = Moment()
    )
}