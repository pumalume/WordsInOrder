package com.ingilizceevi.datacontroller

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ingilizceevi.dataconnection.R
import com.ingilizceevi.dataconnection.MotherConnection
import com.ingilizceevi.dataconnection.gameModel
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PanelController : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var main : View
    private val gameBrain: gameModel by activityViewModels()
    private lateinit var nameView : FragmentContainerView
    private lateinit var pressToStartButton :Button


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
    ): View? {
        // Inflate the layout for this fragment
        main = inflater.inflate(R.layout.fragment_panel_controller, container, false)
        return main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameView = main.findViewById(R.id.boxForNameFragment)
        //pressToStartButton.setOnClickListener(onPressToStartListener)
    }

    override fun onResume() {
        super.onResume()
        if (gameBrain.studentListIsInitialized && !gameBrain.studentIsChosen) {
            putListOfStudentsInBox()
            placeListenerOnAllTheNamesInTheList()
        }
        else if(gameBrain.studentIsChosen) {
            putChosenStudentIntoBox()
            placeListenerOnAllTheNamesInTheList()
        }
        else spawnThreadToFetchNameDataFromServer()
    }
    //add a list of string for Chapter choice


    //add a list of strings for Name choice
    fun putListOfStudentsInBox() {
        childFragmentManager.beginTransaction()
            .replace(R.id.boxForNameFragment, ElementPanel(), "elementPanel")
            .commitNow()
        val studentBox = childFragmentManager.findFragmentById(R.id.boxForNameFragment) as ElementPanel
        studentBox.emptyTheScroll()
        val studentList = gameBrain.myStudentList
        val size = studentList.size
        studentList.forEach{ student ->
            val name = student.studentFirstName+" "+student.studentLastName
            val id = student.studentID
            studentBox.addElementToView(name,id)
        }
        gameBrain.studentIsChosen = false
        placeListenerOnAllTheNamesInTheList()
    }
    //add a single string to Name choice
    fun putChosenStudentIntoBox(){
        val studentBox = childFragmentManager.findFragmentById(R.id.boxForNameFragment) as ElementPanel
        val student = gameBrain.chosenStudent
        val name = student.studentFirstName+" "+student.studentLastName
        val id = student.studentID
        studentBox.loadFreshElement(name, id)
    }

    val onNameClickListener = View.OnClickListener {
        if(!gameBrain.studentIsChosen) {
            processTheNameFromClicker(it as TextView)
            putChosenStudentIntoBox()
            gameBrain.studentIsChosen = true
            placeListenerOnAllTheNamesInTheList()

        }
        else putListOfStudentsInBox()
    }


    fun placeListenerOnAllTheNamesInTheList(){
        val studentBox = childFragmentManager.findFragmentById(R.id.boxForNameFragment) as ElementPanel
        for(i in 0 until studentBox.sizeOfScroll())
            studentBox.handleOnScrollElement(i).setOnClickListener(onNameClickListener)
    }

    fun studentIsChosen():Array<String>?{
        return gameBrain.studentArrayIsReturned()
    }


    val onPressToStartListener = View.OnClickListener {

 //       parentFragmentManager.beginTransaction().remove(R.id.fragmentContainerView)
//        if(gameBrain.chapterIsChosen and gameBrain.studentIsChosen)
//                gameBrain.startSignal.value = true
        }

    //puts the name choice into the game's brain
    private fun processTheNameFromClicker(t : TextView) {
        val id = t.tag.toString()
        gameBrain.setChosenStudent(id)
        gameBrain.studentIsChosen=true

    }


    private fun spawnThreadToFetchNameDataFromServer(){
        val myConnection = MotherConnection(gameBrain)
        myConnection.execute("nameConnection")
        observerIsEstablishedForConnectionComplete()
    }


    fun observerIsEstablishedForConnectionComplete() {
        val dataIsLoadedAction = Observer<Boolean> {
            if(it != true)gameBrain.fillTempList()
            putListOfStudentsInBox()
            placeListenerOnAllTheNamesInTheList()
        }
        gameBrain.dataIsLoaded.observe(viewLifecycleOwner, dataIsLoadedAction)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PanelController().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}