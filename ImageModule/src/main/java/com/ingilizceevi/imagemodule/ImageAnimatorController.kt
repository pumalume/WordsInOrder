package com.ingilizceevi.imagemodule
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer


class ImageAnimatorController(views: MutableList<View>,
                              val gameBrain: GameBrain,
                              val container: View) {
    val views = views
    val count = views.size
    val imageAnimatorsArray: MutableList<ImageFragmentAnimator> = ArrayList(0)
    private val animatorIsRunning: MutableList<Boolean> = arrayListOf(false,false)
//this is where the problem is imageAnimatorsArray
    init{
        setupTheAnimators()
    }
    fun setupTheAnimators(){ checkFragmentAnimator() }

    fun startMotion(i:Int){
        imageAnimatorsArray[i].startRandomMotion()
    }
    fun stopMotion(i:Int){
        checkFragmentAnimator()
        imageAnimatorsArray[i].stopAnimator()
        animatorIsRunning[i]=false
    }
    fun stopMotionAndReset(i:Int, endX:Float){
        checkFragmentAnimator()
        imageAnimatorsArray[i].stopAnimator()
        imageAnimatorsArray[i].initializeAnimator(endX,10F)
    }

    fun checkFragmentAnimator():Boolean {
        if(!imageAnimatorsArray.isEmpty())return true
        for(i in 0 until count){
            Log.i("view", container.x.toString()+"!!!!!!!!!!!!!!!!!!!!!!")
            Log.i("view", container.y.toString())
            Log.i("view", container.height.toString())
            Log.i("view", container.width.toString())
            imageAnimatorsArray.add( ImageFragmentAnimator(container, views[i], gameBrain, i))
            //imageAnimatorsArray[0].initializeAnimator(50F, 50F)
            }
            //imageAnimatorsArray.forEach { it.initializeAnimator(20F,20F) }
        return true
    }

    fun deleteAnimator(){
        imageAnimatorsArray.forEach { it.valueAnimator.removeAllUpdateListeners() }
    }
    fun getPosition(i:Int):Float{
        val x = views[i].x
        return x
    }

    fun checkPosition(view:Int):Int{
        //makes an array of all the words.x
        //makes an int with target word.x
        //compares target to all
        //returns position of word on screen

        val positionCounter:MutableList<Float> = ArrayList(0)
        val targetX = views[view].x
        for(i in 0 until count) {
            positionCounter.add(views[i].x)
        }
        positionCounter.sort()
        for(i in 0 until count) {
            if(positionCounter[i]==views[view].x)return i
        }
        return -1
    }

}

//}