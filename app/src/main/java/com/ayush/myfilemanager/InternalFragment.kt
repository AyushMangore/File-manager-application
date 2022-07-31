package com.ayush.myfilemanager

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.format.Formatter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayush.myfilemanager.adapter.FileAdapter
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class InternalFragment : Fragment(), OnfilesselectedListener {

    lateinit var recyclerView : RecyclerView
    lateinit var showFilePath : TextView
    lateinit var back : ImageView
    lateinit var filelist : MutableList<File>
    lateinit var storage : File
    lateinit var data : String
    lateinit var fileadapter : FileAdapter
    var items = arrayOf("Details","Rename","Delete","Share")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_internal, container, false)
        showFilePath = view.findViewById(R.id.internalfragmentfilepath)
        back = view.findViewById(R.id.internalfragmentbackimg)
        recyclerView = view.findViewById(R.id.internalfragmentrecyclerview)
        val internalstorage = System.getenv("EXTERNAL_STORAGE")
        storage = File(internalstorage!!)
        showFilePath.text = storage.absolutePath
        try{
            data = requireArguments().getString("path").toString()
            val file = File(data)
            storage = file
            showFilePath.text = storage.absolutePath
        }catch (e :Exception){
            e.printStackTrace()
        }
        displayFiles()
        return view
    }

    private fun displayFiles(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        filelist = ArrayList()
        filelist.addAll(searchFile(storage))
        fileadapter = FileAdapter(requireContext(),filelist,this)
        recyclerView.adapter = fileadapter
    }

    private fun searchFile(file : File): ArrayList<File> {
        val fileArrayList = ArrayList<File>()
        val files = file.listFiles()
        for(singlefile in files){
            if(singlefile.isDirectory && !singlefile.isHidden){
                fileArrayList.add(singlefile)
            }
        }
        for(singlefile in files){
            if(singlefile.name.toLowerCase(Locale.getDefault()).endsWith(".jpeg") ||
                    singlefile.name.toLowerCase(Locale.getDefault()).endsWith(".jpg") ||
                    singlefile.name.toLowerCase(Locale.getDefault()).endsWith(".png") ||
                    singlefile.name.toLowerCase(Locale.getDefault()).endsWith(".gif") ||
                    singlefile.name.toLowerCase(Locale.getDefault()).endsWith(".mp3") ||
                    singlefile.name.toLowerCase(Locale.getDefault()).endsWith(".wav") ||
                    singlefile.name.toLowerCase(Locale.getDefault()).endsWith(".mp4") ||
                    singlefile.name.toLowerCase(Locale.getDefault()).endsWith(".txt") ||
                    singlefile.name.toLowerCase(Locale.getDefault()).endsWith(".pdf")){
                fileArrayList.add(singlefile)
            }
        }
        return fileArrayList
    }

    override fun onfileclick(file: File) {
        if(file.isDirectory){
            val bundle = Bundle()
            bundle.putString("path",file.absolutePath)
            val internalFragment = InternalFragment()
            internalFragment.arguments = bundle
            requireFragmentManager().beginTransaction().replace(R.id.framelayout,internalFragment)
                    .addToBackStack(null).commit()
        }else{
            try{
                Fileopener.openfile(requireContext(),file)
            }catch (e : IOException){
                e.printStackTrace()
            }
        }
    }

    override fun onfilelongclick(file: File, position: Int) {
        val optiondialogBox = Dialog(requireContext())
        optiondialogBox.setContentView(R.layout.option_dialog)
        optiondialogBox.setTitle("Select An Option")
        val optiondialogs = optiondialogBox.findViewById<View>(R.id.list) as ListView
        val customadapter = CustomAdapter()
        optiondialogs.adapter = customadapter
        optiondialogBox.show()
        optiondialogs.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when (parent.getItemAtPosition(position).toString()){
                "Details" -> {
//                    Toast.makeText(requireContext(),"Details Selected",Toast.LENGTH_SHORT).show()
                    val alertdialog = AlertDialog.Builder(context)
                    alertdialog.setTitle("Details")
                    val details = TextView(context)
                    alertdialog.setView(details)
                    val modifieddate = Date(file.lastModified())
                    val simpledateformat = SimpleDateFormat("dd/MM/yyyy")
                    val dateformatted = simpledateformat.format(modifieddate)
                    val size = Formatter.formatShortFileSize(context,file.length())
                    details.text = """
                        File Name : ${file.name}
                        Path : ${file.absolutePath}
                        File Size : $size
                        Last Modified Date : $dateformatted
                    """.trimIndent()
                    alertdialog.setPositiveButton("ok"){dialog,_-> dialog.dismiss()}
                    alertdialog.create()
                    alertdialog.show()
                }
                "Rename" -> {
//                    Toast.makeText(requireContext(),"Rename Selected",Toast.LENGTH_SHORT).show()
                    val renamealertdialog = AlertDialog.Builder(context)
                    renamealertdialog.setTitle("Rename File")
                    val name = EditText(context)
                    renamealertdialog.setView(name)
                    renamealertdialog.setPositiveButton("ok"){dialog,_ ->
                        val newfilename = name.editableText.toString()
                        val extension = file.absolutePath.substring(file.absolutePath.lastIndexOf(".")+1)
                        val current = File(file.absolutePath)
                        val destination = File(file.absolutePath.replace(file.name,newfilename) + extension)
                        if(current.renameTo(destination)){
                            filelist[position] = destination
                            fileadapter.notifyDataSetChanged()
                            Toast.makeText(requireContext(),"File Name Changed",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(),"File name can not be modified",Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                    }
                    renamealertdialog.setNegativeButton("Cancel"){dialog,_-> dialog.dismiss()}
                    renamealertdialog.create()
                    renamealertdialog.show()
                }

                "Delete" -> deletefile(file,position)
                "Share" -> sharefile(file)
            }
        }

    }

    fun deletefile(file : File,position : Int){
        val deletefile = AlertDialog.Builder(context)
        deletefile.setTitle("Delete "+file.name+" ?")
        deletefile.setPositiveButton("ok"){dialog,_ ->
            file.delete()
            filelist.removeAt(position)
            fileadapter.notifyDataSetChanged()
            dialog.dismiss()
            Toast.makeText(requireContext(),"File Deleted Successfully",Toast.LENGTH_LONG).show()
        }
        deletefile.setNegativeButton("Cancel"){dialog,_ -> dialog.dismiss()}
        deletefile.create()
        deletefile.show()
    }

    fun sharefile(file : File){
        val filename = file.name
        val share = Intent()
        share.action = Intent.ACTION_SEND
        share.type = "*/*"
        val shareURI = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName+".provider",
                file
        )
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        share.putExtra(Intent.EXTRA_STREAM,shareURI)
        startActivity(Intent.createChooser(share,"Send $filename ?"))
    }

    internal inner class CustomAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return items.size
        }

        override fun getItem(p0: Int): Any {
            return items[p0]
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val view = layoutInflater.inflate(R.layout.option_layout,null)
            val textoption = view.findViewById<TextView>(R.id.textoption)
            val img = view.findViewById<ImageView>(R.id.imageoption)
            textoption.text = items[p0]

            if(items[p0] == "Details"){
                img.setImageResource(R.drawable.details)
            }
            if(items[p0] == "Rename"){
                img.setImageResource(R.drawable.rename)
            }
            if(items[p0] == "Delete"){
                img.setImageResource(R.drawable.delete)
            }
            if(items[p0] == "Share"){
                img.setImageResource(R.drawable.share)
            }

            return view

        }

    }

}