package com.ingilizceevi.datacontroller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isNotEmpty
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.ingilizceevi.dataconnection.R


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ElementPanel : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var main : View
    private lateinit var scroller : LinearLayout
    private var scrollSize:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        main = inflater.inflate(R.layout.fragment_element_panel, container, false)
        scroller = main.findViewById(R.id.scroll_view)
        return main
    }

    fun loadFreshElement(chosenElement:String, id:String){
        emptyTheScroll()
        addElementToView(chosenElement, id)
    }

    fun handleOnScrollElement(index:Int):TextView{
        val textview = scroller.getChildAt(index) as TextView
        return textview
    }
    fun sizeOfScroll():Int{return scrollSize}


    fun addElementToView(displayString:String, stringID:String) {
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lp.setMargins(15)
        val tempText = TextView(context)
        tempText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tempText.layoutParams = lp
        tempText.setPadding(10)
        tempText.text = displayString
        tempText.tag = stringID
        //tempText.textSize = 30f
        scroller.addView(tempText)
        scrollSize++
    }

    fun emptyTheScroll(){
        if(scroller.isNotEmpty()) {
            scroller.removeAllViews()
            scrollSize=0
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ElementPanel().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}