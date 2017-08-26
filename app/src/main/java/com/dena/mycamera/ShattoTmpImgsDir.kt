package com.dena.mycamera

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ShattoTmpImgsDir(context: Context) {

    var absolutePath: String

    init {
        absolutePath = context.getFilesDir().toString() + "/tmp/imgs/"
        // ディレクトリがまだなければ作る
        // http://android-note.open-memo.net/sub/file_and_dir__make_dir.html
        val dir = File(absolutePath)
        if (!dir.exists()) {
            dir.mkdirs()
            Log.d("File.mkdirs()", "SUCCESS. Create Temp Images Directory.  path: ${absolutePath}")
        }
    }
    // ファイル一覧を返す
    // http://thr3a.hatenablog.com/entry/20130902/1378107379
    fun getFiles(): Array<File> {
        return File(absolutePath).listFiles()
    }
    // ファイル削除
    // http://qiita.com/ns777/items/0e959a9c35753b178003
    fun deleteFile(filename: String) {
        val firstString: String = filename[0].toString()
        val file = if (firstString=="/") File(filename) else File(absolutePath + filename)
        file.delete()
    }
    // BitmapからPNGで保存
    fun createPngFromBitmap(filename: String, bitmap: Bitmap): ByteArray {
        val outStream = FileOutputStream(File(absolutePath + filename))
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.close()

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }
    fun getFullpath(filename: String): String {
        return absolutePath + "/" + filename
    }
}