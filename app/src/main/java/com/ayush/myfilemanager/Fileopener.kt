package com.ayush.myfilemanager

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.util.*

object Fileopener {
    fun openfile(context : Context, file : File){
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName+".provider",
            file
        )
        var intent = Intent(Intent.ACTION_VIEW)
        if(uri.toString().contains(".pdf") || uri.toString().contains(".txt")){
            intent.setDataAndType(uri,"text/plain")
        }else if(uri.toString().contains(".mp3") || uri.toString().contains(".wav")) {
            intent.setDataAndType(uri, "audio/x-wav")
        }else if(uri.toString().toLowerCase(Locale.getDefault()).contains(".png") ||
            uri.toString().toLowerCase(Locale.getDefault()).contains(".jpg") ||
            uri.toString().toLowerCase(Locale.getDefault()).contains(".jpeg") ||
            uri.toString().toLowerCase(Locale.getDefault()).contains(".gif")){
            intent.setDataAndType(uri, "image/jpeg")
        }else if(uri.toString().toLowerCase(Locale.getDefault()).endsWith(".mp4")){
            intent.setDataAndType(uri,"video/*")
        }else{
            intent.setDataAndType(uri,"*/*")
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }
}