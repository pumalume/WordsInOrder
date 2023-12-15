package com.ingilizceevi.wordsinorder

import android.net.Uri
import androidx.lifecycle.ViewModel

class WordBrain(): ViewModel() {

    var count_extra:Int = 0
    var sentence_size = 0
    lateinit var sentencesEkler : MutableMap<String, List<String>>
    lateinit var sentences : MutableMap<String, List<String>>
    var sentenceList : MutableList<String> = ArrayList(0)
    private var sentenceKey = ""
    lateinit var currentSentenceList : List<String>
    lateinit var currentSentenceEkler : List<String>
    lateinit var sounds : MutableMap<String, Uri>
    val decoyWords : MutableMap<String,String> = mutableMapOf()

    fun fillTheSoundMap(chapter:String){
        val fileLoader = ConceptLoader(chapter)
        fileLoader.loadAudio()
        fileLoader.loadTextFile(chapter)
        sentences = fileLoader.buildSentenceMap()
        sentencesEkler = fileLoader.buildSentenceEkler()
        sentences.forEach { entry -> sentenceList.add(entry.key) }
        sounds = fileLoader.theSounds
    }
    fun getLocationOfRepeatedWords(word:String):IntArray {
        val index_of_word = mutableListOf<Int>()
        for (i in 0 until sentenceList.size) {
            if (word == sentenceList[i]) index_of_word.add(i)
        }
        return index_of_word.toIntArray()
    }

    fun createPossibleSentenceOrders():MutableList<IntArray>{
        val possible_word_index : MutableList<IntArray> = ArrayList(sentenceList.size)
        for(i in 0 until sentenceList.size) {
            possible_word_index[i]= getLocationOfRepeatedWords(sentenceList[i])
        }
        return possible_word_index


    }
    fun checkDoubleWordsInList(token:String):Boolean{
        val doubles = sentenceList.groupingBy { it }.eachCount().filter { it.value >1 }
        if(doubles.containsKey(token)) return true
        else return false

    }

    fun isDecoySentence():ArrayList<String>?{return null}
    fun returnCurrentUri(): Uri? {
        return sounds[sentenceKey]
    }

//why
    fun updateCurrentSentence():Boolean{
        if(sentenceList.isEmpty())return false
        sentenceKey = sentenceList.removeAt(0)
        currentSentenceList = sentences[sentenceKey]!!
        var checkValue : List<String>? = null
        checkValue = sentencesEkler[sentenceKey]
        if(checkValue==null)currentSentenceEkler = listOf("")
        else currentSentenceEkler = sentencesEkler[sentenceKey]!!
        sentence_size = currentSentenceList.size

        return true
    }
    fun parsCurrentSentence():List<String>{
        return currentSentenceList
    }
    fun parsCurrentEkler():List<String>{
        return currentSentenceEkler
    }

//    fun wordViewAssociationIsMade(word:String, viewId:Int){
//        wordViewAssociation[word]=viewId
//    }
//    fun wordViewAssociationIsGotten(viewId:Int):String{
//        wordViewAssociation.forEach{
//            if(it.value==viewId)return it.key
//        }
//        return ""
//    }
//    fun wordViewAssociationIsGotten(word:String): Int?? {
//        return wordViewAssociation[word]
//    }
}