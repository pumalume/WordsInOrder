package com.ingilizceevi.wordframe

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.forEach
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import com.ingilizceevi.imagemodule.GameBrain
import com.ingilizceevi.imagemodule.ImageAnimatorController
import kotlin.math.abs

class WordFragment : Fragment() {
    lateinit var  animator_controller : ImageAnimatorController
    lateinit var main : View
    lateinit var fieldOfPlay: FrameLayout
    private val gameBrain: GameBrain by activityViewModels()
    private var local_sentence :List<String> = ArrayList(0)
    private var local_decoys:List<String> = ArrayList(0)
    private lateinit var submitButton: ImageView
    private lateinit var playSoundButton: ImageView
    //private lateinit var doorButton:ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main = inflater.inflate(R.layout.fragment_word, container, false)
        fieldOfPlay = main.findViewById(R.id.fieldOfPlay)
        submitButton = main.findViewById(R.id.submit_button)
        playSoundButton = main.findViewById(R.id.play_button)
        submitButton.setOnClickListener({
        })
        return main
    }

    fun handleOnFieldOfPlay():FrameLayout{
        return fieldOfPlay
    }
    fun submitButtonHandle():ImageView{
        return submitButton
    }
    fun decoysAreFilled(decoys:ArrayList<String>){
        local_decoys=decoys
    }
    fun setGameBrain(count:Int){
        gameBrain.setupBrain(count)
    }
    fun resetTheFieldOfPlay(sentence:List<String>, additional_list:List<String>){
        this.local_sentence=sentence
        this.local_decoys = additional_list
        fieldOfPlayIsResetWithNewSentence(local_sentence.size+local_decoys.size)
        viewsAreFilledWithNewText()
        setAnimator()
    }

    fun handleOnPlaySoundButton():ImageView{
        return playSoundButton
    }

    fun isIndexLessThanAllTheRest(index:Int, number_of_views: Int):Boolean{
        val a = fieldOfPlay.getChildAt(index).x

        for(i in 0 until number_of_views){
            val varX = fieldOfPlay.getChildAt(i).x
            val indexX = fieldOfPlay.getChildAt(index).x
            if(i>index) {
                if(fieldOfPlay.getChildAt(index).x < fieldOfPlay.getChildAt(i).x)continue
                else return false
            }
        }
        return true
    }

    //this checks the vertical alignment of
    //a word with all the other words
    fun isYAlignedWithAllTheRest(index:Int, number_of_views: Int):Boolean{
        val y_value = fieldOfPlay.getChildAt(index).y
        for(i in 0 until number_of_views){
            if(i!=index) {
                val anchor_y = fieldOfPlay.getChildAt(i).y
                if(y_value < anchor_y-100 || y_value > anchor_y+100) return false
            }
        }
        return true
    }
    fun isContinuedToTheNextRow(){}

    fun checktoseeiftherearetworowsofY(numberOfWords:Int) {
        val arrayOfYPosition = mutableListOf<Float>()
//        var temp = arrayOfYPosition
//        temp = temp.sort()
//        (arrayOfYPosition == temp)
        for(i in 0 until numberOfWords){
            val a = fieldOfPlay.getChildAt(i)
            arrayOfYPosition.add(a.y)
        }
        val max_height = arrayOfYPosition.max()
        val min_height = arrayOfYPosition.min()
        val nearMax = countHowManyAreWithinModerateDistanceFromPoint(max_height, numberOfWords)
        val nearMin = countHowManyAreWithinModerateDistanceFromPoint(min_height, numberOfWords)
        if(nearMax+nearMin<numberOfWords){
            Log.i("ALERT*@@@@@@@@@@", "max: ${nearMax} min: ${nearMin}")
        }
    }

    fun checkwherethelinestarts(position:Int, number_of_views: Int){
//        for(i in 0 until number_of_views){
//            if(wordIsInRangeOfPosition(i))
//        }
    }
    fun wordIsInRangeOfPosition(index:Int, position:Float, range:Float):Boolean{
        val y_value = fieldOfPlay.getChildAt(index).y
        val differenceInPositionFromPoint = abs(position - y_value)
        if(differenceInPositionFromPoint < range)return true
        return false
    }
    fun countHowManyAreWithinModerateDistanceFromPoint(position:Float, number_of_views: Int):Int{
        var count = 0
        for(i in 0 until number_of_views) {
            if(wordIsInRangeOfPosition(i, position, 15f))count++
        }
        return count
    }
    fun sentenceOrderIsGood(numberOfWords:Int):Boolean{
        checktoseeiftherearetworowsofY(numberOfWords)
        for(index in 0 until numberOfWords) {
            if(!isIndexLessThanAllTheRest(index, numberOfWords)) {
                return false
            }
        }
        for(index in 0 until numberOfWords){
            if(!isYAlignedWithAllTheRest(index, numberOfWords)){
                return false
            }
        }
        return true
    }
    fun viewsAreFilledWithNewText(){
        freshTextIsAddedToViews()
        decoyTextIsAddedToViews()
    }
    fun freshTextIsAddedToViews(){
        for(i in 0 until local_sentence.size) {
            val container = fieldOfPlay.getChildAt(i) as TextView
            container.text=local_sentence[i]
            container.textSize=60f
            container.setTypeface(container.getTypeface(), Typeface.BOLD);
        }
    }
    fun decoyTextIsAddedToViews(){
        val real_word_count = local_sentence.size
        val ready_view_count = fieldOfPlay.size
        var index=0
        for(i in real_word_count until ready_view_count) {
            val container = fieldOfPlay.getChildAt(i) as TextView
            container.text=local_decoys[index++]
            container.textSize=60f
            container.setTypeface(container.getTypeface(), Typeface.BOLD);
        }
    }

    fun setAnimator(){
        val views : MutableList<View> = ArrayList(0)
        fieldOfPlay.forEach {
            val view = it as TextView
            views.add(view)
        }
        animator_controller = ImageAnimatorController(views, gameBrain,fieldOfPlay)
    }
    fun fieldOfPlayIsResetWithNewSentence(word_count:Int){
        fieldOfPlay.removeAllViews()
        textViewsAreAddedToField(fieldOfPlay, 0, word_count)
        //extraViewsAreAddedToRink()
    }
    fun extraViewsAreAddedToRink(){
        textViewsAreAddedToField(fieldOfPlay, local_sentence.size, local_sentence.size+local_decoys.size)
    }


    fun isFieldOfPlayLayout():ViewGroup.LayoutParams{
        return   ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            150
        )
    }
    fun textViewsAreAddedToField(field: FrameLayout, startIndex:Int, number_of_views:Int){
        for(i in startIndex until number_of_views) {
            val frame_view = context?.let { TextView(it) }
            if (frame_view != null) {
                val id = i+1
                frame_view.id = id
                frame_view.layoutParams=isFieldOfPlayLayout()
            }
            field.addView(frame_view)
        }
    }
}