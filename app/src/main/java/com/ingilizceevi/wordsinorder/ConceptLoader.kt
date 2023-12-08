package com.ingilizceevi.wordsinorder
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.InputStream

class ConceptLoader (location:String) {
    val location = location
    val lineList:MutableList<String> = ArrayList(0)
    val theSounds : MutableMap<String,Uri> = mutableMapOf()

    fun loadAudio(){
        val myPath = Environment.getExternalStorageDirectory().path + "/Translations/SoundFiles/" + location + "/"
        //val myPath = Environment.getExternalStorageDirectory().path + "/Music/MySounds/" + myChapter + "/"
        File(myPath).walkBottomUp().forEach {
            if (it.isFile) {
                val u = Uri.parse(it.toString())
                val s = it.toString().substringAfterLast("/").dropLast(4)
                theSounds[s]=u
            }
        }
    }

    fun loadTextFile(location:String){
        val path = Environment.getExternalStorageDirectory().path + "/Translations/TextFiles/"+location+".txt"
        val inputStream: InputStream = File(path).inputStream()
        val inputString = inputStream.bufferedReader().useLines{
                lines -> lines.forEach{ lineList.add(it) }
        }
    }


    fun buildSentenceEkler():MutableMap<String, List<String>>{
        val sentenceEkler : MutableMap<String, List<String>>  = mutableMapOf()
        sentenceEkler[""]= listOf("")
        lineList.forEach {
            val currentString = it
            val currentParts = currentString.split(":")
            val currentKey = currentParts[0]
            if(currentParts.size>2){
                sentenceEkler[currentKey] = currentParts[2].split(",")
            }
        }
        return sentenceEkler
    }


    fun buildSentenceMap():MutableMap<String, List<String>>{
        val sentenceMap : MutableMap<String, List<String>>  = mutableMapOf()
        lineList.forEach {
            val currentString = it
            val currentParts = currentString.split(":")
            val currentKey = currentParts[0]
            val currentSentenceList = currentParts[1].split(",")
            sentenceMap[currentKey] = currentSentenceList
        }
        return sentenceMap
    }
}