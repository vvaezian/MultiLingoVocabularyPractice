package com.example.vvaezian.multilingovocabularypractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(final Context context, final String dbName) {
        // for explanation of the extra argument `dbName` see DatabaseFactory java file
        super(context, dbName, null, 1);
    }

    // String used for programmatically generate SQL Create Table command
    public static String createString;

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String TABLE_NAME = "WordsTable";
        final String[] allLangs = {"fr", "de", "es", "it", "en"};

        createString = "CREATE TABLE "
                + TABLE_NAME + " (source TEXT PRIMARY KEY";
        for (String lang:allLangs)
            createString += ", " + lang + " Text";
        createString += ");";
        Log.d("out", createString);
        db.execSQL(createString);
    }
    // TODO: Implement onUpgrade properly
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String TABLE_NAME = "WordsTable";

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // this function is used in 'EditTablePage'
    public boolean insertData(String sourceText, String[] langs, String[] translations){

        final String TABLE_NAME = "WordsTable";

        // TODO: call getWritableDatabase() or getReadableDatabase() in a background thread,
        // such as with AsyncTask or IntentService, because they make be 'long-running'
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put("source", sourceText);
        for (int i=0; i< langs.length; i++)
            contentValues.put(langs[i], translations[i]);

        // Insert the new row, returning the primary key value of the new row
        long result = db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        // On failure, 'insertWithOnConflict' returns -1. We use this to return True/False
        return result != -1;
    }
}
