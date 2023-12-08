package com.ingilizceevi.imagemodule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData


class GameBrain: ViewModel() {
    private var count = 0
    private var zones = 8
    lateinit var xIndex : MutableList<Int>
    lateinit var yIndex : MutableList<Int>
    lateinit var startValueArrayX :MutableList<FloatArray>
    lateinit var startValueArrayY :MutableList<FloatArray>
//    val startValueArrayY = arrayOf(
//         floatArrayOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)
//        ,floatArrayOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)
//        ,floatArrayOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)
//    )

    fun setupBrain(count:Int){
        this.count = count
        xIndex = createIndexArray()
        yIndex = createIndexArray()
        startValueArrayX = createViewMatrix()
        startValueArrayY = createViewMatrix()
    }
    fun increaseIndexes(tag:Int, i:Int){
        xIndexIsChangedBy(tag, i)
        yIndexIsChangedBy(tag, i)
    }
    private fun createIndexArray():MutableList<Int>{
        val l :MutableList<Int> = ArrayList(0)
        for(i in 0 until count)l.add(0)
        return l
    }
    private fun createViewMatrix():MutableList<FloatArray>{
        val y = floatArrayOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)
        val x:MutableList<FloatArray> = ArrayList(0)
        for(i in 0 until count)x.add(y)
        return x
    }
    fun checkCollision(i:Int, x:Float, y:Float):Boolean{
        return xIsEqualToOtherX(i,x)&&yIsEqualToOtherY(i,y)
    }
    fun getValueForXArray(tag:Int):Float{
        return startValueArrayX[tag][xIndex[tag]]
    }
    fun getValueForBackwardXArray(tag:Int, i:Int):Float{
        if(i<xIndex[tag])return startValueArrayX[tag][xIndex[tag]-i]
        return startValueArrayX[tag][xIndex[tag]+(zones - i)]
    }

    fun putValueForXArray(tag:Int, x:Float){
        startValueArrayX[tag][xIndex[tag]]=x
        Log.i("X values","tag=$tag, x:$x")

    }
    fun getValueForBackwardYArray(tag: Int, i: Int): Float {
        if (i < yIndex[tag]) { startValueArrayY[tag][yIndex[tag] - i] }
        return startValueArrayY[tag][yIndex[tag] + (zones - i)]
    }
    fun getValueForYArray(tag:Int):Float{
        return startValueArrayY[tag][yIndex[tag]]

    }

    fun putValueForYArray(tag:Int, y:Float){
        startValueArrayY[tag][yIndex[tag]]=y
        Log.i("Putting Y", "tag=$tag, Y=$y")
    }
   fun xIndexIsChangedBy(tag:Int, i:Int){
       if(xIndex[tag]+i>=zones){
           xIndex[tag] = xIndex[tag]-(zones-i)
       }
       else if(xIndex[tag]+i<0){
           xIndex[tag]=xIndex[tag] + (i+zones)
       }
       else xIndex[tag]=xIndex[tag]+i
       Log.i("xIndex","tag:$tag, ${xIndex[tag]}")
   }
    fun yIndexIsChangedBy(tag:Int, i:Int){
        if(yIndex[tag]+i>=zones) {
            yIndex[tag] = yIndex[tag] - (zones - i)
        }
        else if(yIndex[tag]+i<0){
            yIndex[tag]=yIndex[tag] + (i+zones)
        }
        else yIndex[tag] = yIndex[tag]+i
        Log.i("yIndex", "tag:$tag ${yIndex[tag]}")
    }

    fun xIsEqualToOtherX(tag:Int, x:Float):Boolean{
        if(x<0)return false
        if(tag == 0)return checkRangeOfValues(x, getValueForXArray(1), 100F)
        else return checkRangeOfValues(x, getValueForXArray(0), 100F)
    }

    fun updateXYValues(tag:Int, x:Float, y:Float){
        increaseIndexes(tag, 1)
        putValueForXArray(tag, x)
        putValueForYArray(tag, y)
    }
    fun checkRangeOfValues(z1:Float, z2:Float, range:Float):Boolean{
        val upper_range = z2+range
        val lower_range = z2-range
        if(z1>lower_range && z1 < upper_range) return true
        else return false
    }

    fun yIsEqualToOtherY(i:Int, y:Float):Boolean{
        if(i==0)return checkRangeOfValues(y, getValueForYArray(1), 120F)
        else return checkRangeOfValues(y, getValueForYArray(0), 120F)
    }
    val animatorCollisionCheckLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

}