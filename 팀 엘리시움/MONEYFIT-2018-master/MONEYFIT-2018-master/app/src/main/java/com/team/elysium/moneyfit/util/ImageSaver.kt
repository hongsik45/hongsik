package com.team.elysium.moneyfit.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import com.team.elysium.moneyfit.Util
import com.team.elysium.moneyfit.thread.HttpRequest
import java.io.ByteArrayOutputStream

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.zip.Deflater.BEST_COMPRESSION
import java.util.zip.Deflater


/**
 * Saves a JPEG [Image] into the specified [File].
 */

// TODO : need to capture continuously.
internal class ImageSaver(
        /**
         * The JPEG image
         */
        private val bitmap: Bitmap,
        private val number: Int,
        private val outputFolder: String,
        private val isFirstCapture: Boolean
) : Runnable {

    // TODO : 압축하기
    override fun run() {

        val bos = ByteArrayOutputStream()

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, true)

        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos)

        if(isFirstCapture)
            Log.e("isfirst", "true")
        else
            Log.e("isfirst", "false")

        HttpRequest.getInstance().sendImage(Util.getID(), bos.toByteArray(), number, outputFolder, isFirstCapture)

        bos.close()
//        val buffer = image.planes[0].buffer
//        val bytes = ByteArray(buffer.remaining())
//        buffer.get(bytes)
//
//        Log.e("압축전", "" + bytes.size)

//        if(Util.getID() != "") {
//
//        }

//        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//        val bos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos)
//
//        val resultByteArray = bos.toByteArray()
//        Log.e("압축후", "" + resultByteArray.size)
//
//        bos.close()

//        val tempFile = File.createTempFile("temps", ".jpeg")
//
//        var output: FileOutputStream? = null
//        try {
//            output = FileOutputStream(tempFile).apply {
//                write(bytes)
//            }
//        } catch (e: IOException) {
//            Log.e(TAG, e.toString())
//        } finally {
//            image.close()
//            output?.let {
//                try {
//                    it.close()
//                } catch (e: IOException) {
//                    Log.e(TAG, e.toString())
//                }
//            }
//        }
//
//        HttpRequest.getInstance().sendImage(Util.getID(), tempFile)
//        tempFile.deleteOnExit()

    }

}
