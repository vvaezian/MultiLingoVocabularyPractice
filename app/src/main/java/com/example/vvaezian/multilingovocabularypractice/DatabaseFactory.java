package com.example.vvaezian.multilingovocabularypractice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/* This file is needed because I wanted to used `loggedInUser` to define the name of database
 (i.e. each user has a database named after his/her username).
 I needed to access shared preferences in DatabaseHelper before initialising the database.  */

public final class DatabaseFactory {
    public static DatabaseHelper getDataBaseHelper(final Context context){
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final String loggedInUser = sp.getString("user","");
        final String databaseName = loggedInUser + ".db";
        return new DatabaseHelper(context, databaseName);
    }
}
