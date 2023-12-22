package com.ingilizceevi.wordframe

import android.view.ViewGroup
import androidx.core.view.forEach
import kotlin.math.abs

class ScreenTracker(number_of_views:Int) {

    val number_of_views = number_of_views
    val array_of_x_coordinates : MutableList<Float> = ArrayList(number_of_views)
    val array_of_y_coordinates : MutableList<Float> = ArrayList(number_of_views)
    val current_sequence_of_views : MutableList<Int> = ArrayList(number_of_views)
    val the_x_factor_of_views: MutableMap<Float, Int> = mutableMapOf()
    val sentence_rows:MutableList<IntArray> = ArrayList(0)
    var number_of_rows = 0
    lateinit var fieldOfPlay:ViewGroup


    fun x_factor_from_smallest_to_biggest(){
        val sorted_array = array_of_x_coordinates.sorted()
    }
    fun fieldOfPlayIsMeasured(field_of_view: ViewGroup){
        for(i in 0 until number_of_views){
            array_of_x_coordinates[i] = field_of_view.getChildAt(i).x
            array_of_y_coordinates[i] = field_of_view.getChildAt(i).y
        }
    }

    fun howManyWordsInRangeOfY(index:Int):Int{
        val y_value = array_of_y_coordinates[index]
        var count = 0
        for(i in 0 until number_of_views){
            if(wordIsInRangeOfALevel(i, y_value, 20f )){

                count++
            }
        }
        return count
    }
    fun findTheNumberOfSimiliarYValuesForEachY(index:Int):MutableList<Set<Int>> {
        val y_value_statistics: MutableList<Set<Int>> = ArrayList(number_of_views)
        for (i in 0 until number_of_views) {
            val level_sets = emptySet<Int>()
            val y_value = array_of_y_coordinates[i]
            for(ii in 0 until number_of_views){
                if(wordIsInRangeOfALevel(ii, y_value, 20F)) level_sets+ii
            }
            y_value_statistics[i] = level_sets
        }
        return y_value_statistics
    }

    fun isTheNumberOfRows(number_of_words: Int):Int{

        val min_max_position = isMinAndMaxPositionOfWords(number_of_words)
        val count_on_min_max = isCountOfWordsNearMinAndMax(min_max_position,number_of_words)
        val count_on_min = count_on_min_max[0]
        val count_on_max = count_on_min_max[1]
        if(count_on_min == count_on_max) { if((min_max_position[1]-min_max_position[0])<50)return 1 }
        return 2
    }


    fun sentence_rows_are_filled(row:IntArray):Boolean{
        if(sentence_rows.add(row)) {
            number_of_rows++
            return true
        }
        return false
    }
    fun isHeightOfTheWords(numberOfWords: Int): FloatArray {
        val position_of_y_values = FloatArray(numberOfWords)
        for (i in 0 until numberOfWords) {
            val wordBox = fieldOfPlay.getChildAt(i)
            position_of_y_values[i] = wordBox.y
        }
        return position_of_y_values
    }
    fun isMinAndMaxPositionOfWords(numberOfWords:Int):Array<Float> {
        val position_of_words = isHeightOfTheWords(numberOfWords)
        val max_height_of_Y = position_of_words.max()
        val min_height_of_Y = position_of_words.min()
        return arrayOf(min_height_of_Y,max_height_of_Y)
    }
    fun isCountOfWordsNearMinAndMax(y_heights:Array<Float>, numberOfWords: Int):Array<Int>{
        val near_min = countHowManyAreWithinModerateDistanceFromPoint(y_heights[0], numberOfWords)
        val near_max = countHowManyAreWithinModerateDistanceFromPoint(y_heights[1], numberOfWords)
        return arrayOf(near_min, near_max)
    }

    fun getWordsAtLevelOfY(y_value:Float): IntArray {
        val words_on_level = mutableListOf<Int>()
        for(i in 0 until number_of_views) {
            if(wordIsInRangeOfALevel(i, y_value, 40f )){ words_on_level.add(i) }
        }
        return words_on_level.toIntArray()
    }


    fun wordIsInRangeOfALevel(index:Int, position:Float, range:Float):Boolean{
        val y_value = fieldOfPlay.getChildAt(index).y
        val differenceInPositionFromPoint = abs(position - y_value)
        if(differenceInPositionFromPoint < range)return true
        return false
    }
    fun countHowManyAreWithinModerateDistanceFromPoint(position:Float, number_of_views: Int):Int{
        var count = 0
        for(i in 0 until number_of_views) {
            if(wordIsInRangeOfALevel(i, position, 50f))count++
        }
        return count
    }



}