package com.sirius.sample.utils

import android.content.Context
import java.io.FileOutputStream
import java.io.InputStream


class Utils {
companion object{
    fun copyRawFile(context : Context,rawRes : Int, filepath : String){
        val `in`: InputStream = context.getResources().openRawResource(rawRes)
        val out = FileOutputStream(filepath)
        val buff = ByteArray(1024)
        var read = 0

        try {
            while (`in`.read(buff).also { read = it } > 0) {
                out.write(buff, 0, read)
            }
        } finally {
            `in`.close()
            out.close()
        }
    }
}
}

