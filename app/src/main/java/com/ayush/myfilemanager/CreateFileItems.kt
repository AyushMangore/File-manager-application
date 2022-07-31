package com.ayush.myfilemanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.ayush.myfilemanager.adapter.CreateFileAdapter
import java.io.*
import java.lang.StringBuilder

class CreateFileItems : AppCompatActivity() {
    lateinit var etName : EditText
    lateinit var btCreate : Button
    lateinit var btOpen : Button
    lateinit var sName : String
    lateinit var fileArray : ArrayList<String>
    lateinit var listView : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_file_items)

        etName = findViewById(R.id.et_name)
        btCreate = findViewById(R.id.button_create)
        btOpen = findViewById(R.id.button_open)
        fileArray = ArrayList()
        listView = findViewById(R.id.filelist)

        btCreate.setOnClickListener{
            sName = etName.text.toString().trim(' ')

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
             == PackageManager.PERMISSION_GRANTED){
                createFolder()
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),100)
            }
        }

        btOpen.setOnClickListener {
            sName = etName.text.toString().trim(' ')
            val url = Uri.parse(Environment.getExternalStorageDirectory().toString()+"/"+sName+"/")
            startActivity(Intent(Intent.ACTION_GET_CONTENT).setDataAndType(url,"*/*"))
        }

    }

    private fun createFolder(){
        val folder = File(Environment.getExternalStorageDirectory(),sName)
        if(folder.exists()){
            Toast.makeText(applicationContext,"Folder already exists",Toast.LENGTH_LONG).show()
        }else{
            folder.mkdir()
            if(folder.isDirectory){
                Toast.makeText(applicationContext,"Folder created successfully",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext,"Folder can not be created",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun createFile(view : View){
        val layout = layoutInflater.inflate(R.layout.create_files,null,false)
        AlertDialog.Builder(this)
                .setTitle("Create Files")
                .setView(layout)
                .setPositiveButton("Create"){dialog,_ ->
                    val myfilename = layout.findViewById<EditText>(R.id.filename)
                    val myfilecontent = layout.findViewById<EditText>(R.id.filecontent)
                    addFile(myfilename.text.toString(),myfilecontent.text.toString())
                    dialog.dismiss()
                }
                .setNegativeButton("Close"){dialog,_ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun addFile(fileName : String,fileContent : String){
        try{
            var reader = openFileOutput(fileName, MODE_PRIVATE)
            reader.write(fileContent.toByteArray())
            reader.close()
            loadFiles()
        }catch (e : FileNotFoundException){
            e.printStackTrace()
        }catch (e : IOException){
            e.printStackTrace()
        }
    }

    private fun loadFiles() {
        val currentDir = filesDir
        fileArray.clear()
        fileArray.addAll(listOf(*currentDir.list()))
        val adapter = CreateFileAdapter(fileArray,this)
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener{parent,view,position,id ->
            showFileContent(fileArray[position])
        }
        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            AlertDialog.Builder(this)
                    .setTitle("Do You Really Want To Delete This File ?")
                    .setPositiveButton("Yes"){dialog,_->
                        delete_file(fileArray[position])
                        dialog.dismiss()
                    }
                    .setNegativeButton("No"){dialog,_ ->
                        dialog.dismiss()
                    }
                    .show()
            true
        }
    }

    private fun showFileContent(s: String) {
        val sb = StringBuilder()
        val file = File(filesDir,s)
        try{
            var br = BufferedReader(FileReader(file))
            var line : String?
            while (br.readLine().also { line = it } != null){
                sb.append(line)
                sb.append("\n")
            }
        }catch (e : FileNotFoundException){
            e.printStackTrace()
        }catch (e : IOException){
            e.printStackTrace()
        }
        val layout = layoutInflater.inflate(R.layout.create_files,null,false)
        val myfilename = layout.findViewById<EditText>(R.id.filename)
        val myfilecontent = layout.findViewById<EditText>(R.id.filecontent)
        myfilename.setText(s)
        myfilecontent.setText(sb)
        AlertDialog.Builder(this)
                .setTitle("Update / Make Changes To The File")
                .setView(layout)
                .setPositiveButton("Update"){dialog,_->
                    updatefiles(myfilename.text.toString(),myfilecontent.text.toString())
                    dialog.dismiss()
                }
                .setNegativeButton("Close"){dialog,_ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun updatefiles(filename: String, filecontent: String) {
        try{
            var reader = openFileOutput(filename, MODE_PRIVATE)
            reader.write(filecontent.toByteArray())
            reader.close()
            loadFiles()
        }catch (e : FileNotFoundException){
            e.printStackTrace()
        }catch (e : IOException){
            e.printStackTrace()
        }
    }

    private fun delete_file(s: String) {
        val file = File(filesDir,s)
        file.delete()
        loadFiles()
    }

}