package com.ayush.myfilemanager.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ayush.myfilemanager.HomeCategory
import com.ayush.myfilemanager.HomearrayList
import com.ayush.myfilemanager.Model1
import com.ayush.myfilemanager.R
import java.util.ArrayList

class HomearraylistAdapter(var model1ArrayList : ArrayList<Model1>, var context : Context) : RecyclerView.Adapter<HomearrayList>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomearrayList {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homesingle,parent,false)
        return HomearrayList(view)
    }

    override fun onBindViewHolder(holder: HomearrayList, position: Int) {
        val temp = model1ArrayList[position];
        holder.img.setImageResource(temp.image)
        holder.txt.text = temp.text1
        holder.itemView.setOnClickListener {
//            Toast.makeText(context,"Fetching ${holder.txt.text}s",Toast.LENGTH_SHORT).show()
            val intent = Intent(context,HomeCategory::class.java)
            intent.putExtra("category",temp.text1)
//            Toast.makeText(context,temp.text1,Toast.LENGTH_SHORT).show()
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return model1ArrayList.size
    }

}