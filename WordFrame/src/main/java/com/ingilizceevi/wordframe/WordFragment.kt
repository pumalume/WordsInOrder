package com.ingilizceevi.wordframe

import android.graphics.Typeface
import android.os.Bundle
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


    fun checkIfAnotherWord(word:IntArray, next_word:Int):Boolean{
        var i = 0
        while(word[i]!=-1){
            if(fieldOfPlay.getChildAt(word[i]).x < fieldOfPlay.getChildAt(next_word).x){
                word.drop(i)
                return true
            }
        }
        return false

    }

    fun wordPositionIsLessThanNext(word:Int, next_word:Int):Boolean{
        if(fieldOfPlay.getChildAt(word).x<fieldOfPlay.getChildAt(next_word).x)return true
        return false
    }
    fun wordPositionIsLessThanAllTheRest(word:Int, last_word:Int):Boolean{
        val len = last_word - word
        for(i in 1 until len){
            if(!wordPositionIsLessThanNext(word, word+i))return false
        }
        return true
    }

    fun isLastValueForX(word:Int, sequence_size:Int):Boolean{
        val x = fieldOfPlay.getChildAt(word).x
        for(i in word until word+sequence_size ){
            if(x<fieldOfPlay.getChildAt(i)){
                return false
            }
        }
        return true
    }

    fun isWordSegementInOrder(start_word:Int, finish_word:Int, avatars:MutableList<IntArray>):Boolean{
        (start_word..finish_word).toList().asReversed().forEach { word ->
            isLastValueForX(word,finish_word-start_word)
        }

        fieldOfPlay
        for(word in start_word until finish_word){
            if(!wordPositionIsLessThanAllTheRest(word, finish_word))return false
        }
        return true
    }

      fun sentenceOrderIsGood(number_of_words:Int, avatars:MutableList<IntArray>):Boolean{

        val number_of_rows = isTheNumberOfRows(number_of_words)
        if(number_of_rows==1){
            if(isWordSegementInOrder(0,number_of_words-1, avatars)){ return true }
        }
        else{
            val min_height = isMinAndMaxPositionOfWords(number_of_words)[0]
            val max_height = isMinAndMaxPositionOfWords(number_of_words)[1]
            val min_words = getWordsAtLevelOfY(min_height,number_of_words)
            val max_words = getWordsAtLevelOfY(max_height, number_of_words)
            if(min_words.size + max_words.size == number_of_words) {
                val x = 3
            }
            if(!isWordSegementInOrder(0, min_words.size,avatars))return false
            if(!isWordSegementInOrder(min_words.size, max_words.size, avatars))return false
            return true
        }
        return false
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