package com.dena.mycamera

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_top_page.*

class TopPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_page)

        button.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
        button3.setOnClickListener { startActivity(Intent(this,BitmapImageActivity::class.java)) }
    }
}
