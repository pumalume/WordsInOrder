package com.ingilizceevi.wordsinorder


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.ingilizceevi.wordframe.WordFragment


class WordOrderGame : AppCompatActivity() {

    lateinit var soundPlayer :SoundPlayer
    var viewCount = 0
    private val wordBrain: WordBrain by viewModels()
    lateinit var clicks:TextView
    lateinit var time:Chronometer
    lateinit var doorButton:ImageView
    private var clickCounter= 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chapter: String = intent.getStringExtra("chapter").toString()
        val name: String = intent.getStringExtra("name").toString()
        setContentView(R.layout.activity_word_order_game)
        val text = findViewById<TextView>(R.id.nameTextView)
        text.text = name
        soundPlayer = SoundPlayer(this)
        wordBrain.fillTheSoundMap(chapter)
        time = findViewById(R.id.timeTextView)
        clicks = findViewById(R.id.clickTextView)
        doorButton = findViewById(R.id.wordDoorButton)
        val nameview = findViewById<TextView>(R.id.nameTextView)
        nameview.text = name
        val wordpanel = findViewById<FragmentContainerView>(R.id.word_panel)
        wordpanel.setOnClickListener({
           wordpanel.setOnClickListener(null)
            startGame()
        })

    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    fun onFinish(time:String, clicks:String){
        val intent = Intent()
        intent.putExtra("time", time)
        intent.putExtra("clicks", clicks)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun setOnPlaySoundButtonClick(){
        val word_fragment = supportFragmentManager.findFragmentById(R.id.word_panel) as WordFragment
        word_fragment.handleOnPlaySoundButton().setOnClickListener {
            playCurrentSentence()
        }
    }
    fun sentenceIsChecked(){
        if(checkSentenceOrder()){
            if(!pressToSetThemUpAndGetThemGoing()){
                time.stop()
                onFinish(time.text.toString(),clicks.text.toString())
            }
        }
        else{

        }
    }

    fun playCurrentSentence(){
        val u = wordBrain.returnCurrentUri()
        if (u != null) { soundPlayer.startSound(u).start() }
    }
    fun animatorsAreInitialized(){
        val f = supportFragmentManager.findFragmentById(R.id.word_panel) as WordFragment
        for(i in 0 until viewCount) {
            val xy = getRandomXY(i)
            f.animator_controller.imageAnimatorsArray[i].initializeAnimator(xy[0], xy[1])
        }
    }
    fun getRandomXY(vue:Int):List<Float>{
        var x = findViewById<FragmentContainerView>(R.id.word_panel).width-150
        var y = findViewById<FragmentContainerView>(R.id.word_panel).height-150
        val xx = (0..x.toInt()).random().toFloat()
        val yy = (0..y.toInt()).random().toFloat()
        when(vue) {
            0,4,8,12,16 -> return listOf((0+xx), (0+yy))
            1,5,9,13,17 -> return listOf((x-xx), (0+yy))
            2,6,10,14,18 -> return listOf((x-xx), (y-yy))
            3,7,11,15,19-> return listOf((0+xx), (y-yy))
            else -> return listOf((0+xx), (0+yy))
        }

    }
    fun pressToSetThingsUp(){
        if(!fieldIsSetForSentence()){
            finish()
        }
        //animatorsAreInitialized()
        //startRandomWander()
    }
    fun startGame(){
        time.setBase(SystemClock.elapsedRealtime())
        time.start()

        val wordFragment = supportFragmentManager.findFragmentById(R.id.word_panel) as WordFragment
        doorButton.setOnClickListener({
            onPressed()
//            val intent = Intent()
//            setResult(Activity.RESULT_CANCELED, intent)
//            finish()
        })
        wordFragment.submitButtonHandle().setOnClickListener({
            clicks.text = (++clickCounter).toString()
            sentenceIsChecked()
        })
        wordFragment.handleOnPlaySoundButton().setOnClickListener({playCurrentSentence()})
        pressToSetThemUpAndGetThemGoing()
    }
    fun pressToSetThemUpAndGetThemGoing():Boolean{
        if(!fieldIsSetForSentence())return false
        pressToGetThingsGoing()
        return true
    }
    fun pressToGetThingsGoing(){
        animatorsAreInitialized()
        explodeRandomWander()
    }
    fun explodeRandomWander(){
        val f = supportFragmentManager.findFragmentById(R.id.word_panel) as WordFragment
        for(i in 0 until viewCount) {
            f.animator_controller.imageAnimatorsArray[i].animatorExplodesRandomly()
 //           val mValues = f.animator_controller.imageAnimatorsArray[i].valueAnimator.values
 //           val a = mValues[0]
   //         val b = mValues[1]
 //           val handler = Handler()
//            handler.postDelayed(Runnable { // Do something after 5s = 5000ms
//                f.animator_controller.imageAnimatorsArray[i].stopAnimator()
//            }, 1000)


        }
    }
    fun fieldIsSetForSentence():Boolean{
        if(!wordBrain.updateCurrentSentence())return false
        val word_list = wordBrain.parsCurrentSentence()
        val word_additions = wordBrain.parsCurrentEkler()
        val f = supportFragmentManager.findFragmentById(R.id.word_panel) as WordFragment
        viewCount = word_list.size + word_additions.size
        f.setGameBrain(viewCount)
        //f.decoysAreFilled(arrayListOf("am", "are", "I"))
        f.resetTheFieldOfPlay(word_list, word_additions)
        wordBrain.returnCurrentUri()?.let { soundPlayer.startSound(it).start() }
        return true
    }

    fun checkSentenceOrder():Boolean{
        val wordFragment = supportFragmentManager.findFragmentById(R.id.word_panel) as WordFragment
        val wordAvatars = wordBrain.createPossibleSentenceOrders()
        return wordFragment.sentenceOrderIsGood(wordBrain.sentence_size, wordAvatars )
    }


    fun onPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Exit?")
        builder.setTitle("")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _,_ ->
            time.stop()
            onFinish(time.text.toString(),clicks.text.toString())
        }

        builder.setNegativeButton("No") {
            // If user click no then dialog box is canceled.
                dialog, which -> dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}
