package com.ayush.myfilemanager

import java.io.File

interface OnfilesselectedListener {
    fun onfileclick(file : File)
    fun onfilelongclick(file : File,position :Int)
}