package com.otototor.wordofday

import android.content.Context
import android.widget.Toast

class Helpers {
    fun showToast (context:Context, text: String, duration:Int){
        val myToast =
            Toast.makeText(context, text, duration)
        myToast.show()
    }
}