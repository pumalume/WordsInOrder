package com.ingilizceevi.wordsinorder

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.ingilizceevi.chapterchoice.ChapterSelection
import com.ingilizceevi.dataconnection.ConnectActivity


class MainActivity : AppCompatActivity() {
    private var name="Mario Bros"
    private var last_name=""
    private var first_name=""
    private var id = 0
    private var chapter = 0
    val LAUNCH_CHAPTER_SELECTION = 1
    val LAUNCH_WORD_GAME = 2
    val RESULTS_ACTIVITY = 3
    val DATA_CONNECTION_ACTIVITY = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        implementDataControl()
//        implementsChapterSelection()
        val game_intent = Intent(this, WordOrderGame::class.java)
        game_intent.putExtra("chapter", "chapter04")
        game_intent.putExtra("name", "Spencer")
        startActivityForResult(game_intent, 2)
    }

    fun implementDataControl(){
        val game_intent = Intent(this, ConnectActivity::class.java)
        //game_intent.putExtra("chapter", chapter);
        startActivityForResult(game_intent, 4)
    }

    fun implementWordGame(data:Intent){
        val chapter = data.getStringExtra("chapter")
        val game_intent = Intent(this, WordOrderGame::class.java)
        game_intent.putExtra("chapter", chapter)
        game_intent.putExtra("name", name)
        startActivityForResult(game_intent, 2)
    }
    fun implementResultsActivity(data:Intent, name:String){
        val time = data.getStringExtra("time")
        val clicks = data.getStringExtra("clicks")
        val results_intent = Intent(this, ResultsActivity::class.java)
        results_intent.putExtra("name", name);
        results_intent.putExtra("clicks", clicks);
        results_intent.putExtra("time", time);
        //////
        val seconds = convertTimeIntoSeconds(time!!)
        val returnConnection = ReturnConnection(arrayOf(id!!, chapter!!, seconds.toInt(), clicks!!.toInt()))
        returnConnection.execute()
        ///////
        startActivityForResult(results_intent, 3)
    }
    fun implementsChapterSelection(){
        val intent = Intent(this, ChapterSelection::class.java)
        intent.putExtra("name", name);
        startActivityForResult(intent, 1)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_CHAPTER_SELECTION) {
            if (resultCode == Activity.RESULT_OK) {
                val c = data!!.getStringExtra("chapter")
                val b = c!!.substringAfterLast('r')
                chapter = b!!.toInt()
                implementWordGame(data!!)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                implementDataControl()
            }
        }
        if (requestCode == LAUNCH_WORD_GAME){
            implementResultsActivity(data!!, name)

        }
        if(requestCode==RESULTS_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                implementsChapterSelection()
            }
        }
        if(requestCode == DATA_CONNECTION_ACTIVITY){
            if(resultCode==Activity.RESULT_OK){
                id = data!!.getStringExtra("id")!!.toInt()
                first_name = data!!.getStringExtra("first_name")!!
                last_name = data!!.getStringExtra("last_name")!!
                name = first_name + " " + last_name
                implementsChapterSelection()
            }
            if(resultCode==Activity.RESULT_CANCELED){
                finish()
            }

        }


        if (resultCode == Activity.RESULT_CANCELED) { }
    }

    fun convertTimeIntoSeconds(time:String):Int{
        val timeSets = time.split(":")
        if(timeSets.size>2||timeSets.isEmpty())return -1
        val minutes = timeSets[0].toInt()*60
        val seconds = timeSets[1].toInt()
        return minutes+seconds
    }
} //onActivityResult
