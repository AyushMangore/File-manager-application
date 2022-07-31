package com.ayush.myfilemanager

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class FileViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
    var filename : TextView = itemview.findViewById(R.id.filecontainerfilename)
    var filesize : TextView = itemview.findViewById(R.id.filecontainerfilesize)
    var container : CardView = itemview.findViewById(R.id.container)
    var imgfile : ImageView = itemView.findViewById(R.id.filecontainerfiletype)
}