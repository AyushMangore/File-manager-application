package com.ayush.myfilemanager

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryViewholder(itemView : View) : RecyclerView.ViewHolder(itemView){
    var img : ImageView = itemView.findViewById(R.id.imgcategoryview)
    var textview : TextView = itemView.findViewById(R.id.textcategoryview)
}