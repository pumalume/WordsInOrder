package com.ingilizceevi.wordsinorder

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.ingilizceevi.dataconnection.FatherConnection

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        val time =findViewById<TextView>(R.id.resultTime)
        val name =findViewById<TextView>(R.id.resultName)
        val clicks = findViewById<TextView>(R.id.resultClick)
        val logo = findViewById<ImageView>(R.id.resultsDoor)

        var tempTime = intent.getStringExtra("time")
        val tempClicks= intent.getStringExtra("clicks")
        val tempName = intent.getStringExtra("name")

        name.text = getString(R.string.studentName, tempName)
        clicks.text = getString(R.string.studentClick, tempClicks)
        time.text = getString(R.string.studentTime, tempTime)

        //this turns time into int of seconds
        //val timeInt = (tempTime?.split(":")?.get(1)?.toInt() ?: 0)
        //+ ((tempTime?.split(":")?.get(0)?.toInt()?:0)*60)
        //val father = ReturnConnection(tempClicks?.let { arrayOf(timeInt, it.toInt()) })
        //father.execute()


        logo.setOnClickListener({
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()

        })

    }
}