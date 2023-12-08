package com.ingilizceevi.dataconnection;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Locale;


public class FatherConnection extends AsyncTask<String, String, Boolean> {

    StudentInfo studentInfo;
    Connection connection = null;
    String ConnectURL = null;
    String user = "pumal";
    String password = "Maylee08";
    String database = "EnglishHouse";
    String ipaddress = "192.168.1.4";
    String query = null;


    public FatherConnection(StudentInfo sInfo) {
        this.studentInfo = sInfo;
    }

    private String fillTheQuery(){

        String myQuery = String.format(
                Locale.getDefault(),
                "INSERT INTO VocabularyTimer(student_id, chapteer_number, chapter_time) VALUES ('%s', '%s', '%d');",
                studentInfo.getStudentID()
                //,
//                studentInfo.getChapterNum(),
//                studentInfo.getChapterTime()
        );
        return myQuery;
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String myQuery = fillTheQuery();

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectURL = "jdbc:jtds:sqlserver://192.168.1.74:1434;instanceName=SQL02;databaseName=EnglishHouse;user=pumal;password=Maylee08;ConnectionRetryCount=2;ConnectionRetryDelay=5";
            //ConnectURL = "jdbc:jtds:sqlserver://" + ipaddress + "/" + database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectURL);
            makeSQLQuery(myQuery);
            connection.close();
        } catch (Exception e) {
            Log.e("connection error:", e.getMessage());
            String i = "0";
        }
        return true;
    }

    private void makeSQLQuery(String myQuery) {
        try {
            Statement stmnt = connection.createStatement();
            stmnt.executeUpdate(myQuery);
            stmnt.close();
        } catch (Exception e) {
            Log.e("Couldn't make query: ", e.getMessage());
        }
    }
}
