package com.ingilizceevi.dataconnection

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.ingilizceevi.datacontroller.PanelController

class ConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        val button = findViewById<ImageView>(R.id.mainbutton)
        val exit = findViewById<ImageView>(R.id.mainExit)
        exit.setOnClickListener({
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        })
        button.setOnClickListener {
            val x = supportFragmentManager.findFragmentByTag("chapterSelection")
            if(x==null){}
            val f =
                supportFragmentManager.findFragmentById(R.id.connectionPanelControllerView) as PanelController
            val s = f.studentIsChosen()
            if (s != null) {
                val intent = Intent()
                intent.putExtra("id", s[0])
                intent.putExtra("first_name", s[1])
                intent.putExtra("last_name", s[2])
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}