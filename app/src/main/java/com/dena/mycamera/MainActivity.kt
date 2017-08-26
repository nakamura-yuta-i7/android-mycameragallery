package com.dena.mycamera

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log.*
import kotlinx.android.synthetic.main.activity_main.*
import me.mattak.moment.Moment
import java.io.File
import android.view.Gravity
import android.util.TypedValue
import com.tmall.ultraviewpager.UltraViewPager
import android.util.Log
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer

class MainActivity : AppCompatActivity() {

    private lateinit var tmpImgsDir: ShattoTmpImgsDir

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tmpImgsDir = ShattoTmpImgsDir(this)

        // 最初に一時領域の写真は削除して初期化
        tmpImgsDir.getFiles().forEach { it -> tmpImgsDir.deleteFile(filename = it.name) }

        filesDirTextView.text = tmpImgsDir.absolutePath

        initViewPager()
    }

    fun initViewPager() {
        val app = (application as MyApplication)
        val adapter = UltraPagerAdapter(
            false, application = app, itemsCount = app.pictures.count())
        // adapter.notifyDataSetChanged()

        val ultraViewPager = ultra_viewpager
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
        ultraViewPager.adapter = adapter

        ultraViewPager.initIndicator()
        ultraViewPager.indicator
            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
            .setFocusColor(Color.GREEN)
            .setNormalColor(Color.WHITE)
            .setRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics).toInt())

        ultraViewPager.indicator.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
        ultraViewPager.indicator.build()

        // カバーフロー風に表示
        ultraViewPager.setPageTransformer(false, UltraDepthScaleTransformer())
        // 複数のアイテムを領域に表示するか
        // 0 ~ 1 : 大きければセンター画像を大きく表示
        ultraViewPager.setMultiScreen(0.65f)
        // 無限にスクロール可能か
        ultraViewPager.setInfiniteLoop(false)
        // 1.0: 縦横比を保つ
        ultraViewPager.setItemRatio(1.0)
        // 自動でスクロールするか
        ultraViewPager.setAutoScroll(0)
        //
        ultraViewPager.setAutoMeasureHeight(true)
    }


    private val RESULT_CAMERA: Int = 1001

    fun onClickCameraButton(v: View) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, RESULT_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        d("requestCode", requestCode.toString())
        d("resultCode", resultCode.toString())
        d("data", data.toString())

        if (requestCode == RESULT_CAMERA) {
            d("koko1", "!!!")
            if ( data != null ) {
                d("koko2", "!!!")

                val bitmap = data.getExtras().get("data") as Bitmap
                val filename = Moment().format("yyyyMMddHHmmss") + ".png"
                val fullpath = tmpImgsDir.getFullpath(filename)
                var byteArray = tmpImgsDir.createPngFromBitmap(
                    bitmap = bitmap, filename = filename)

                val app = (application as MyApplication)
                var tmpPics = app.pictures

                app.pictures = mutableListOf(MyApplication.Picture(
                    thumbnail = byteArray,
                    file = File(fullpath)
                ))
                tmpPics.forEach {
                    app.pictures.add(it)
                }

                var text = ""
                tmpImgsDir.getFiles().forEach { it ->
                    d("file", it.toString())
                    text += it.name + "\n"
                }
                textView.text = text

                Log.d("app.pictures.count", app.pictures.count().toString() )
                app.pictures.forEach {
                    d("app.picture", it.file.absoluteFile.name )
                }
                initViewPager()
            }
        }
    }
}