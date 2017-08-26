package com.dena.mycamera

import android.app.Activity
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.github.chrisbanes.photoview.PhotoView

class PictureDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_detail)

        var position = intent.getIntExtra("position", 0)
        var app = (application as MyApplication)
        var file = app.pictures[position].file
        var photoView = findViewById(R.id.photo_view) as PhotoView
        photoView.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
    }
}
