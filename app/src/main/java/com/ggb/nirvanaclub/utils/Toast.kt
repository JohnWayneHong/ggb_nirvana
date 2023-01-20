package com.ggb.nirvanaclub.utils

import android.widget.Toast
import com.ggb.nirvanaclub.App

//对于Toast的一个简单封装，也用到了扩展函数
fun String.showToast(duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(App.context,this,duration).show()
}

fun Int.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(App.context,this,duration).show()
}