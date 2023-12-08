package com.ingilizceevi.dataconnection

class StudentInfo(id: String, fname:String, lname:String){
    var studentID:String = ""
    var studentFirstName:String = ""
    var studentLastName:String= ""

    init{
        studentID = id
        studentFirstName = fname
        studentLastName = lname
    }
}

