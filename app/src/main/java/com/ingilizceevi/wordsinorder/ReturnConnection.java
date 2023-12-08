package com.ingilizceevi.wordsinorder;


import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Locale;


public class ReturnConnection extends AsyncTask<String, String, Boolean> {

        Connection connection = null;
        String ConnectURL = null;
        Integer[] resultsArray;

        public ReturnConnection(Integer[] result_array) {
            this.resultsArray = result_array;
        }

        private String fillTheQuery(){

            String myQuery = String.format(
                    Locale.getDefault(),
                    "INSERT INTO Chapters(StudentID, ChapterNum, ChapterTime, ChapterClicks) VALUES ('%d', '%d', '%d',  '%d');"
                    , resultsArray[0]
                    , resultsArray[1]
                    , resultsArray[2]
                    , resultsArray[3]
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
                Log.i("   This is the query: ", myQuery);
                makeSQLQuery(myQuery);
                connection.close();
            } catch (Exception e) {
                Log.e("   A connection error:", e.getMessage());
            }
            return true;
        }

        private void makeSQLQuery(String myQuery) {
            try {
                Statement stmnt = connection.createStatement();
                stmnt.executeUpdate(myQuery);
                Log.i("   Made query: ", "This is query execution");
                stmnt.close();
            } catch (Exception e) {
                Log.e("Couldn't make query: ", e.getMessage());
            }
        }
    }

