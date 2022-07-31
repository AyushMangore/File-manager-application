package com.ayush.myfilemanager.adapter

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.myfilemanager.FileViewHolder
import com.ayush.myfilemanager.OnfilesselectedListener
import com.ayush.myfilemanager.R
import java.io.File
import java.util.*

class FileAdapter(
        private val context: Context,
        private val files : List<File>,
        private val onfileselectedlisteners : OnfilesselectedListener
) : RecyclerView.Adapter<FileViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filecontainerlayout,parent,false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val temp = files[position]
        holder.filename.text = temp.name
        var items = 0
        if(temp.isDirectory){
            val files1 = temp.listFiles()
            for(singlefile in files1){
                if(!singlefile.isHidden){
                    items++;
                }
            }
            holder.filesize.text = "$items Files"
            holder.imgfile.setImageResource(R.drawable.folder)
        }else{
            holder.filesize.text = Formatter.formatShortFileSize(context,temp.length())
            getImg(temp,holder)
        }
        holder.container.setOnClickListener { onfileselectedlisteners.onfileclick(temp) }
        holder.container.setOnLongClickListener{
            onfileselectedlisteners.onfilelongclick(temp,position)
            true
        }
    }

    private fun getImg(temp: File, holder: FileViewHolder) {
        if(temp.name.toLowerCase(Locale.getDefault()).endsWith(".jpg") ||
                temp.name.toLowerCase(Locale.getDefault()).endsWith(".jpeg") ||
                temp.name.toLowerCase(Locale.getDefault()).endsWith(".png") ||
                temp.name.toLowerCase(Locale.getDefault()).endsWith(".gif")){
            holder.imgfile.setImageResource(R.drawable.images)
        }else if(temp.name.toLowerCase(Locale.getDefault()).endsWith(".mp3") ||
                temp.name.toLowerCase(Locale.getDefault()).endsWith(".wav")){
            holder.imgfile.setImageResource(R.drawable.audio)
        }else if(temp.name.toLowerCase(Locale.getDefault()).endsWith(".text") ||
                temp.name.toLowerCase(Locale.getDefault()).endsWith(".pdf") && !temp.isDirectory){
            holder.imgfile.setImageResource(R.drawable.file)
        }else if(temp.name.toLowerCase(Locale.getDefault()).endsWith(".mp4")){
            holder.imgfile.setImageResource(R.drawable.video)
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }

}