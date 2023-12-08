package com.ingilizceevi.chapterchoice

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import java.io.File

class ChapterSelection : AppCompatActivity() {

    private lateinit var scroller : LinearLayout
    private var scrollSize:Int = 0
    lateinit var fileView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_selection)
        scroller = findViewById(R.id.list_of_items)
        fileView = findViewById(R.id.filePath)
        loadNamesIntoView()
        setupTheListeners()
        val escape_button = findViewById<ImageView>(R.id.escapeButton)

        escape_button.setOnClickListener({
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        })
    }

    fun getFileList():Array<String>{
        val myPath = Environment.getExternalStorageDirectory().path + "/Translations/SoundFiles/"
        fileView.text = myPath
        val fileList = File(myPath).list()
        return fileList
    }
    fun loadNamesIntoView():Boolean{
        emptyTheScroll()
        val fileList = getFileList()
        for(f in fileList){
            addStringToView(f, f)
        }
        return true
    }
    fun addStringToView(displayString:String, stringID:String) {
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(15)
        val tempText = TextView(this.applicationContext)
        tempText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tempText.layoutParams = lp
        tempText.setPadding(10)
        tempText.text = displayString
        tempText.tag = stringID
        tempText.textSize = 40f
        tempText.setTextColor(Color.BLACK)
        tempText.setTypeface(null, Typeface.BOLD);
        scroller.addView(tempText)
        scrollSize++
    }

    fun emptyTheScroll(){
        scroller.removeAllViews()
    }
    fun setupTheListeners(){
        for(i in 0 until scrollSize){
            val tempText = scroller.getChildAt(i) as TextView
            tempText.setOnClickListener(myOnChoiceClickListener)
        }
    }
    val myOnChoiceClickListener = View.OnClickListener{
        val textView = it as TextView
        val chapter = textView.tag.toString()
        val intent = Intent()
        intent.putExtra("chapter", chapter)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}