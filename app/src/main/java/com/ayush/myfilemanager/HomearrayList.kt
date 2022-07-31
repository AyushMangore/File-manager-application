package com.ayush.myfilemanager

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomearrayList(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var img : ImageView = itemView.findViewById(R.id.showimghome)
    var txt : TextView = itemView.findViewById(R.id.showtexthome)
}