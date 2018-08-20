package com.dena.mycamera

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_bitmap_image.*

class BitmapImageActivity : AppCompatActivity() {

    private lateinit var shattoCamera: ShattoCamera
    private lateinit var tmpImgsDir: ShattoTmpImgsDir

    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap_image)

        // 一時的なファイルは削除
        tmpImgsDir = ShattoTmpImgsDir(this)
        tmpImgsDir.getFiles().forEach {
            tmpImgsDir.deleteFile(it.absolutePath)
        }

        // カメラボタンクリック時
        camera.setOnClickListener {
            i++
            shattoCamera = ShattoCamera(this, photoName = "photo${i}.jpg")
            shattoCamera.startActivityForResult()
        }
        // デフォルト表示写真
        imageView.setImageDrawable(resources.getDrawable(R.drawable.sky_wall))

        // リサイズボタン
        resizeButton.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()
        textView2.text = tmpImgsDir.getFiles().joinToString("\n")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // カメラで取った写真を表示
        var bitmapData = shattoCamera.getBitmapFromActivityResult(requestCode, resultCode, data)
        if (bitmapData!=null) {
            imageView.setImageBitmap(shattoCamera.applyReflection(bitmapData.bitmap))

            resizeButton.visibility = View.VISIBLE
            resizeButton.setOnClickListener {
                // リサイズボタンを押したら
                // 画像をリサイズして保存＆表示
                var resizedBitmap = shattoCamera.resize(bitmapData.bitmap, 500, 500)
                imageView.setImageBitmap(shattoCamera.applyReflection(resizedBitmap))
                tmpImgsDir.createJpgFromBitmap("photo_resized${i}.jpg", resizedBitmap)
            }
        }
    }
}
