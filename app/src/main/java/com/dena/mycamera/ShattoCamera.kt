package com.dena.mycamera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File

@SuppressLint("LongLogTag")
class ShattoCamera(
    val activity: Activity,
    val photoName: String
) {
    private val CAMERA_PHOTO: Int = 111
    private var tmpImgsDir = ShattoTmpImgsDir(activity)
    private var photoUrl: Uri? = null
    private var photoFile: File? = null

    fun startActivityForResult() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = File(tmpImgsDir.absolutePath, "${photoName}")
        if (!photoFile!!.exists()) {
            photoFile!!.parentFile.mkdirs()
            photoFile!!.createNewFile()
        }
        photoUrl = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUrl)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        // KitKat以前は、暗黙的IntentにFLAG_GRANT_*_PERMISSIONが付与されない問題の回避
        // http://qiita.com/ushi3_jp/items/115e3c725a9ffa34aed0
        var resolves = activity.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resolves) {
            Log.d("resolveInfo.activityInfo.packageName", resolveInfo.activityInfo.packageName)
            activity.grantUriPermission(resolveInfo.activityInfo.packageName, photoUrl, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        activity.startActivityForResult(intent, CAMERA_PHOTO)
    }
    fun getBitmapFromActivityResult(requestCode: Int, resultCode: Int, data: Intent?): BitmapData? {

        Log.d("getBitmapFromActivityResult.requestCode", requestCode.toString())
        Log.d("getBitmapFromActivityResult.resultCode", resultCode.toString())
        Log.d("getBitmapFromActivityResult.data", data.toString())

        if (requestCode == CAMERA_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("getBitmapFromActivityResult", "koko1")
                if ( photoUrl != null ) {
                    Log.d("getBitmapFromActivityResult!!.path", photoUrl!!.path)
                    var data = BitmapData(
                        photoUrl!!.path,
                        MediaStore.Images.Media.getBitmap(activity.contentResolver, photoUrl!!)
                    )
                    return data
                }
            } else {
                Log.d("getBitmapFromActivityResult", "koko2")
                tmpImgsDir.deleteFile(photoFile!!.absolutePath)
            }
        }
        return null
    }
    class BitmapData(var absolutePath: String, var bitmap: Bitmap) {

    }

    /**
     * 画像リサイズ
     * @param bitmap 変換対象ビットマップ
     * @param newWidth 変換サイズ横
     * @param newHeight 変換サイズ縦
     * @return 変換後Bitmap
     */
    fun resize(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {

        val oldWidth = bitmap.width
        val oldHeight = bitmap.height

        if (oldWidth < newWidth && oldHeight < newHeight) {
            // 縦も横も指定サイズより小さい場合は何もしない
            return bitmap
        }

        val scaleWidth = newWidth.toFloat() / oldWidth
        val scaleHeight = newHeight.toFloat() / oldHeight
        val scaleFactor = Math.min(scaleWidth, scaleHeight)

        val scale = Matrix()
        scale.postScale(scaleFactor, scaleFactor)

        val resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, oldWidth, oldHeight, scale, false)
        bitmap.recycle()

        return resizeBitmap

    }


    fun applyReflection(originalImage: Bitmap): Bitmap {
        // gap space between original and reflected
        val reflectionGap = 4
        // get image size
        val width = originalImage.width
        val height = originalImage.height

        // this will not scale but will flip on the Y axis
        val matrix = Matrix()
        matrix.preScale(1f, -1f)

        // create a Bitmap with the flip matrix applied to it.
        // we only want the bottom half of the image
        val reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false)

        // create a new bitmap with same width but taller to fit reflection
        val bitmapWithReflection = Bitmap.createBitmap(width, height + height / 2, Bitmap.Config.ARGB_8888)

        // create a new Canvas with the bitmap that's big enough for
        // the image plus gap plus reflection
        val canvas = Canvas(bitmapWithReflection)
        // draw in the original image
        canvas.drawBitmap(originalImage, 0f, 0f, null)

        // draw in the gap
        val defaultPaint = Paint()
        canvas.drawRect(0f, height.toFloat(), width.toFloat(), (height + reflectionGap).toFloat(), defaultPaint)

        // draw in the reflection
        canvas.drawBitmap(reflectionImage, 0f, (height + reflectionGap).toFloat(), null)

        // create a shader that is a linear gradient that covers the reflection
        val paint = Paint()
        val shader = LinearGradient(0f, originalImage.height.toFloat(), 0f,
            (bitmapWithReflection.height + reflectionGap).toFloat(), 0x70ffffff, 0x00ffffff,
            Shader.TileMode.CLAMP)
        // set the paint to use this shader (linear gradient)
        paint.shader = shader
        // set the Transfer mode to be porter duff and destination in
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        // draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0f, height.toFloat(), width.toFloat(), (bitmapWithReflection.height + reflectionGap).toFloat(), paint)

        return bitmapWithReflection
    }
}