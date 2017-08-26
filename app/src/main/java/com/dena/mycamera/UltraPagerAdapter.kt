package com.dena.mycamera

import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent



class UltraPagerAdapter(
    private val isMultiScr: Boolean,
    private var application: MyApplication,
    private var itemsCount: Int) : PagerAdapter() {

    override fun getCount(): Int {
        return itemsCount
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var pictures = application.pictures
        var file = pictures[position].file

        var imageView = ImageView(container.context)
        // imageView.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
        var byteArray = pictures[position].thumbnail
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))

        imageView.setBackgroundColor(Color.BLACK)
        imageView.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)

        imageView.setOnClickListener {
            val intent = Intent(application.applicationContext, PictureDetailActivity::class.java)
            intent.putExtra("position", position)
            application.applicationContext.startActivity(intent)
        }

        container.addView(imageView)
        return imageView

        //val linearLayout = LayoutInflater.from(container.context).inflate(R.layout.layout_child, null) as LinearLayout
        // val linearLayout = LinearLayout(container.getContext())
        //       val textView = linearLayout.findViewById(R.id.pager_textview) as TextView
        //       textView.text = position.toString() + ""
        //       linearLayout.id = R.id.item_id
        // when (position) {
        //     0 -> linearLayout.setBackgroundColor(Color.parseColor("#2196F3"))
        //     1 -> linearLayout.setBackgroundColor(Color.parseColor("#673AB7"))
        //     2 -> linearLayout.setBackgroundColor(Color.parseColor("#009688"))
        //     3 -> linearLayout.setBackgroundColor(Color.parseColor("#607D8B"))
        //     4 -> linearLayout.setBackgroundColor(Color.parseColor("#F44336"))
        // }
        // container.addView(linearLayout)
        // //        linearLayout.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, container.getContext().getResources().getDisplayMetrics());
        // //        linearLayout.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, container.getContext().getResources().getDisplayMetrics());
        // return linearLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}