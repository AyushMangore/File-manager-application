package com.ayush.myfilemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayush.myfilemanager.adapter.CategoryviewAdapter
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class HomeCategory : AppCompatActivity() {

    lateinit var category : String
    lateinit var categoryname : TextView
    lateinit var fileLink : String
    lateinit var categoryrecycler : RecyclerView
    lateinit var categoryviewAdapter : CategoryviewAdapter
    lateinit var datas : MutableList<File>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_category)

        category = intent.getStringExtra("category").toString()
        categoryname = findViewById<View>(R.id.homecategory) as TextView
        categoryrecycler = findViewById<View>(R.id.homecategoryrecycler) as RecyclerView
        categoryrecycler.layoutManager = LinearLayoutManager(this)
        datas = ArrayList()
        categoryviewAdapter = CategoryviewAdapter(this@HomeCategory,datas as ArrayList<File>,category)
        categoryrecycler.adapter = categoryviewAdapter


        categoryname.text = category
        fileLink = System.getenv("EXTERNAL_STORAGE")
        gettingFile(fileLink)
    }

    private fun gettingFile(path: String) {
        var file = File(path)
        if(file.isDirectory && file.canRead()){
            val file1 = file.listFiles()
            for(singlefile in file1){
                if(singlefile.isDirectory && singlefile.canRead()){
                    gettingFile(singlefile.absolutePath)
                }else if(singlefile.isFile && singlefile.canRead()){
                    displayFile(singlefile)
                }
            }
        }else if(file.isFile && file.canRead()){
            displayFile(file)
        }
    }

    private fun displayFile(singleFile : File) {
        when (category) {
            "Image" -> if(singleFile.name.toLowerCase(Locale.getDefault()).endsWith(".png") ||
                singleFile.name.toLowerCase(Locale.getDefault()).endsWith(".jpg") ||
                singleFile.name.toLowerCase(Locale.getDefault()).endsWith(".jpeg") ||
                singleFile.name.toLowerCase(Locale.getDefault()).endsWith(".gif")){
                datas.add(singleFile)
            }
            "Video" -> if(singleFile.name.toLowerCase(Locale.getDefault()).endsWith(".mp4") ||
                singleFile.name.toLowerCase(Locale.getDefault()).endsWith(".mkv")){
                datas.add(singleFile)
            }
            "Audio" -> if(singleFile.name.toLowerCase(Locale.getDefault()).endsWith(".mp3") ||
                singleFile.name.toLowerCase(Locale.getDefault()).endsWith(".wav")){
                datas.add(singleFile)
            }
            "Document" -> if(singleFile.name.toLowerCase(Locale.getDefault()).endsWith(".txt") ||
                singleFile.name.toLowerCase(Locale.getDefault()).endsWith(".pdf")){
                datas.add((singleFile))
            }
        }
    }
}