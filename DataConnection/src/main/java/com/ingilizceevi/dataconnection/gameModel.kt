package com.ingilizceevi.dataconnection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ingilizceevi.dataconnection.StudentInfo

class gameModel: ViewModel() {

    lateinit var myStudentList : MutableList<StudentInfo>
    lateinit var chosenStudent: StudentInfo
    var studentIsChosen = false
    var studentListIsInitialized = false

    fun setChosenStudent(id:String){
        val student = returnStudentFromId(id)
        if(student!=null) chosenStudent = student
    }

    fun studentArrayIsReturned():Array<String>?{
        if(studentIsChosen){
            return arrayOf(
                  chosenStudent.studentID
                , chosenStudent.studentFirstName
                , chosenStudent.studentLastName
            )
        }
        return null
    }
    fun returnStudentFromId(id:String): StudentInfo?{
        myStudentList.forEach { student->
            if(student.studentID==id)return student
        }
        return null
    }

    fun concatanateStudentName():String{
        return chosenStudent.studentFirstName+" "+ chosenStudent.studentLastName
    }

    fun fillTempList(){
        val temp1 = StudentInfo("-1", "Mario", "Bros")
        val temp2 = StudentInfo("-2", "Luigi", "Bros")
        val temp3 = StudentInfo("-3", "Bowser Boss", "Monster")
        val temp4 = StudentInfo("-4", "Princess", "Peach")
        val temp5 = StudentInfo("-5", "Toad", "Guy")
        val mutable_list = mutableListOf<StudentInfo>(temp1,temp2,temp3, temp4, temp5)
        myStudentList = mutable_list
    }
    fun studentListIsLoadedFrom(studentList : MutableList<StudentInfo>) {
        myStudentList = studentList
        studentListIsInitialized = true
    }
    fun flagIsRaisedForLoadedStudentList(filledStudentList:Boolean){
            dataIsLoaded.value = filledStudentList
    }
    val dataIsLoaded: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val nameIsSelected: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val quitSignal: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val startSignal: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val changeNameSignal: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

}