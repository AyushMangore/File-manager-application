package com.ayush.myfilemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayush.myfilemanager.CategoryViewholder
import com.ayush.myfilemanager.Fileopener.openfile
import com.ayush.myfilemanager.R
import java.io.File
import java.io.IOException

class CategoryviewAdapter(
    private val context : Context,
    private val fileList : List<File>,
    private val category : String
) : RecyclerView.Adapter<CategoryViewholder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categoryviewholdersingle,parent,false)
        return CategoryViewholder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewholder, position: Int) {
        val temp = fileList[position]
        holder.textview.text = temp.name
        setImage(holder,temp)
        holder.itemView.setOnClickListener {
            try{
                openfile(context,temp)
            }catch (e : IOException){
                e.printStackTrace()
            }
        }
    }

    private fun setImage(categoryViewholder: CategoryViewholder, singlefile: File) {
        when(category){
            "Image" -> categoryViewholder.img.setImageResource(R.drawable.images)
            "Video" -> categoryViewholder.img.setImageResource(R.drawable.video)
            "Audio" -> categoryViewholder.img.setImageResource(R.drawable.audio)
            "Document" -> categoryViewholder.img.setImageResource(R.drawable.file)
        }
    }


    override fun getItemCount(): Int {
        return fileList.size
    }

}