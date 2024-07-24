package com.example.chmovie.shared.widget.dialogManager

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.chmovie.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class QRCodeDialog(context: Context, private val bitmap: Bitmap) : Dialog(context) {

    init {
        initView()
    }

    private fun initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.qr_code_dialog)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageView: ImageView = findViewById(R.id.imgQRCode)
        val btnDownloadQR: ImageButton = findViewById(R.id.btnDownloadQR)
        val btnShareQR: ImageButton = findViewById(R.id.btnShareQR)

        // Đặt bitmap cho ImageView
        imageView.setImageBitmap(bitmap)

        btnDownloadQR.setOnClickListener {
            saveImageToGallery()
        }

        btnShareQR.setOnClickListener {
            val filename = "share_image_${System.currentTimeMillis()}.png"
            val file = File(context.cacheDir, filename)
            try {
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.startActivity(Intent.createChooser(intent, "Share QR Code"))
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to share image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageToGallery() {
        val filename = "QRCode_${System.currentTimeMillis()}.png"
        val fos: OutputStream?
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = context.contentResolver.openOutputStream(imageUri!!)
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                val image = File(imagesDir, filename)
                fos = FileOutputStream(image)
            }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                Toast.makeText(context, "QR Code saved to gallery", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
}