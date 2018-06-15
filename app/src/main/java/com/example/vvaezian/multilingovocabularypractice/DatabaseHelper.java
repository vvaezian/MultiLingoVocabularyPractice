package com.example.vvaezian.multilingovocabularypractice;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "vocab3.db";
    public static final String TABLE_NAME = "test";
    public static final String COL_1 = "source";
    public static final String COL_2 = "fr";
    public static final String COL_3 = "de";

    //this is apparently needed to access shared prefs in this class because what it extends doesn't include 'Context'
    private Context appContext;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.appContext = context;
    }

    // String used for programmatically generate SQL Create Table command
    public static String createString;

    @Override
    public void onCreate(SQLiteDatabase db) {
        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(appContext);
        String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");
        final String[] langs = langsConcated.split(" ");

        createString = "CREATE TABLE "
                + TABLE_NAME + " (source TEXT PRIMARY KEY";
        for (String lang:langs)
            createString += ", " + lang + " Text";
        createString += ");";
        Log.d("out", createString);
        db.execSQL(createString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // this function is used in 'EditTablePage'
    public boolean insertData(String sourceText, String frText, String deText){

        // TODO: call getWritableDatabase() or getReadableDatabase() in a background thread,
        // such as with AsyncTask or IntentService, because they make be 'long-running'
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, sourceText);
        contentValues.put(COL_2, frText);
        contentValues.put(COL_3, deText);

        // Insert the new row, returning the primary key value of the new row
        long result = db.insert(TABLE_NAME, null, contentValues);
        // of failure, 'insert' returns -1. We use this to return True/False
        return result != -1;
    }
/*
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id} );
    }
*/
}
