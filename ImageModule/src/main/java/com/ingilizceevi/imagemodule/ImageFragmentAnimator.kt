package com.ingilizceevi.imagemodule

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt
import kotlin.random.Random

class ImageFragmentAnimator(main: View, view:View, gb:GameBrain, tag:Int):View.OnTouchListener {
    private var main = main
    private var view = view
    val tag = tag
    val endPointX:Float = 0F
    val endPointY:Float = 0F
    lateinit var valueAnimator: ValueAnimator
    var gameBrain : GameBrain = gb
    var dX: Float = 0F
    var dY: Float = 0F


    fun startRandomMotion() {
        if (valueAnimator.isRunning) valueAnimator.pause()
        animatorIsRunningOnRandomGenerator()
        valueAnimator.start()
    }


    fun initializeAnimator(endX:Float, endY:Float) {
        view.setOnTouchListener(this)
        animatorIsSetWithoutBorderControl(endX, endY)
        valueAnimator.start()
    }
    fun stopAnimator(){
        if(valueAnimator.isRunning) { valueAnimator.pause() }
    }

    fun animatorExplodesRandomly(){
        val xx = valueAnimator.getAnimatedValue("x") as Float
        val yy = valueAnimator.getAnimatedValue("y") as Float
        val endPointX = randomEndValueForAnimator(xx, 0)
        val endPointY = randomEndValueForAnimator( yy, 1)
        //val duration = calculateDuration(view.x,endPointX,view.y, endPointY)
        animatorIsSetWithoutBorderControl(endPointX, endPointY)
        valueAnimator.duration = 200
        //addListenerToAnimation()
    }

    fun animatorIsRunningOnRandomGenerator(){
        val xx = valueAnimator.getAnimatedValue("x") as Float
        val yy = valueAnimator.getAnimatedValue("y") as Float
        val endPointX = randomEndValueForAnimator(xx, 0)
        val endPointY = randomEndValueForAnimator( yy, 1)
        val duration = calculateDuration(view.x,endPointX,view.y, endPointY)
        animatorIsSetWithoutBorderControl(endPointX, endPointY)
        valueAnimator.duration = duration.toLong()
        //addListenerToAnimation()
    }
    private fun randomEndValueForAnimator(start_position:Float, dimension:Int):Float{

        var length = 0
        if(dimension == 0) length = main.width
        if(dimension == 1) length = main.height

        val upper_bound = length - start_position
        val rg = Random(start_position.toInt())
        val rl = rg.nextDouble(-300.00, 300.00)
        val randomLength = rl.toFloat()
        var end_position = randomLength+start_position
        if(end_position<0)end_position = 0F
        if(end_position>length-220) end_position = end_position-(end_position - length)-220
         return end_position
    }
    private fun calculateDuration(x1:Float, x2:Float, y1:Float, y2:Float):Float{
        val x = x2-x1
        val y = y2-y1
        val c = sqrt(x.pow(2)+y.pow(2))
        val a = sqrt(c)
        return (a/.007F)
    }

    private fun animatorIsSetWithoutBorderControl(endPointX:Float, endPointY:Float) {
        val startPointX = view.x
        val startPointY = view.y
        val pvX = PropertyValuesHolder.ofFloat("x", startPointX, endPointX)
        val pvY = PropertyValuesHolder.ofFloat("y", startPointY, endPointY)
        valueAnimator = ValueAnimator.ofPropertyValuesHolder(pvX, pvY)
        valueAnimator.duration = 0
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener {
            val valueX = it.getAnimatedValue("x") as Float
            val valueY = it.getAnimatedValue("y") as Float
            view.translationY = valueY
            view.translationX = valueX
            gameBrain.increaseIndexes(tag,1)
            gameBrain.startValueArrayX[tag][gameBrain.xIndex[tag]]=valueX
            gameBrain.startValueArrayY[tag][gameBrain.yIndex[tag]]=valueY
        }
    }

    private fun animatorIsSetAndRegisteredToThereFromBrainWith(endPointX:Float, endPointY:Float){
        val startPointX = gameBrain.startValueArrayX[tag][gameBrain.xIndex[tag]]
        val startPointY = gameBrain.startValueArrayY[tag][gameBrain.yIndex[tag]]
        val pvX = PropertyValuesHolder.ofFloat("x", startPointX, endPointX)
        val pvY = PropertyValuesHolder.ofFloat("y", startPointY, endPointY)
        valueAnimator = ValueAnimator.ofPropertyValuesHolder(pvX, pvY)
        valueAnimator.duration = 0
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener {
            val valueX = it.getAnimatedValue("x") as Float
            val valueY = it.getAnimatedValue("y") as Float
            view.translationY = valueY
            view.translationX = valueX
            gameBrain.increaseIndexes(tag,1)
            gameBrain.startValueArrayX[tag][gameBrain.xIndex[tag]]=valueX
            gameBrain.startValueArrayY[tag][gameBrain.yIndex[tag]]=valueY
        }
    }
    fun addListenerToAnimation(){
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                animatorIsRunningOnRandomGenerator()
                valueAnimator.start()
            }
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val CLICK_ACTION_THRESHOLD = 200L
        var duration = 0L
        if(event!=null)duration = event.eventTime-event.downTime
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                stopAnimator()
                if (v != null) {
                    dX = v.x - event.rawX
                }
                if (v != null) {
                    dY = v.y - event.rawY
                }
            }
            MotionEvent.ACTION_MOVE -> if (v != null) {
                val location = IntArray(2)
                main.getLocationInWindow(location)
                var dX = event.rawX + dX
                var dY = event.rawY + dY
                if(event.rawX < location[0]){dX = 0f}
                if(event.rawY < location[1] ){ dY= -2f }
                if(valueAnimator.isRunning) { valueAnimator.pause() }
                animatorIsSetAndRegisteredToThereFromBrainWith(dX,dY)
                valueAnimator.start()
            }
            MotionEvent.ACTION_UP -> if(v!=null){
//                if(duration<CLICK_ACTION_THRESHOLD){
//                    startRandomMotion()
//                }
            }

            else -> return false
        }
        return true
    }

//    private val myValueAnimatorWithBorderControl = ValueAnimator.AnimatorUpdateListener {
//        val valueX = it.getAnimatedValue("x") as Float
//        val valueY = it.getAnimatedValue("y") as Float
//        //if (!gameBrain.checkCollision(tag, valueX, valueY)) {
//                view.translationY = valueY
//                view.translationX = valueX
//               gameBrain.increaseIndexes(tag,1)
//               gameBrain.startValueArrayX[tag][gameBrain.xIndex[tag]]=valueX
//               gameBrain.startValueArrayY[tag][gameBrain.yIndex[tag]]=valueY
//        // }
//        //else gameBrain.animatorCollisionCheckLiveData.value=tag
//    }

}