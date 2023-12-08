package com.ingilizceevi.mylibrary

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ImageFragment : Fragment() {

    private var textId: Int? = null
    private var textTag: String? = null
    private lateinit var mainView: View
    private lateinit var textView: TextView
    private lateinit var textFrame: FrameLayout
    //private val gameBrain: LaneViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            textId = it.getInt(ARG_PARAM1)
            textTag=it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mainView = inflater.inflate(R.layout.fragment_image_frame, container, false)
        textView = mainView.findViewById(R.id.textView)
        textFrame = mainView.findViewById(R.id.imageFrame)
        if (textId != null) {
            textView.text = textTag
        }
        else textView.text = "SR"
        return mainView
    }

    fun handleOnImageFrame(): FrameLayout {
        return textFrame
    }

    fun handleOnImageView(): TextView {
        return textView
    }


    fun imageValuesAreReset() {
        textView.alpha = 1f
        textView.rotation = 0F
        textView.scaleX = 1.0f
        textView.scaleY = 1.0f

    }

    fun getCurrentCoordinatesOnScreen(): IntArray {
        val frame = mainView.findViewById<FrameLayout>(R.id.imageFrame)
        val location = IntArray(2)
        frame.getLocationInWindow(location)
        return location
    }

    fun alphaAnimatorReturnedToFull() {
        if (textView.alpha != 1f) {
            AnimatorInflater.loadAnimator(requireContext(), R.animator.alpha_animator)
                .apply {
                    setTarget(textView)
                    start()
                }
        }
    }

    fun enlargerAnimatorIsSetForView(): Animator {
        val myAnimator =
            AnimatorInflater.loadAnimator(requireContext(), R.animator.enlarger_animator)
        myAnimator.setTarget(textView)
        return myAnimator
    }

    fun shakerAnimatorIsSetForView(): Animator {
        val myAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.shaker_animator)
        myAnimator.setTarget(textView)
        return myAnimator
    }

    fun fadeAnimatorIsSetForView(): Animator {
        val myFader = AnimatorInflater.loadAnimator(requireContext(), R.animator.alpha_animator)
        myFader.setTarget(textView)
        return myFader
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2:String) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}