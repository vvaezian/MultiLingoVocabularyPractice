package com.example.vvaezian.multilingovocabularypractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(final Context context, final String dbName) {
        // for explanation of the extra argument `dbName` see getDataBaseHelper method in HelperFunctions java file
        super(context, dbName, null, 1);
    }

    // String used for programmatically generate SQL Create Table command
    public static String createString;

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String TABLE_NAME = "WordsTable";
        final String[] allLangs = {"fr", "de", "es", "it", "en"};

        createString = "CREATE TABLE " + TABLE_NAME + " (source TEXT PRIMARY KEY";
        for (String lang:allLangs)
            createString += ", " + lang + " Text";
        createString += ",status TINYINT );";
        Log.d("create query: ", createString);
        db.execSQL(createString);
    }
    // TODO: Implement onUpgrade properly
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String TABLE_NAME = "WordsTable";

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    final String TABLE_NAME = "WordsTable";

    // this function is used in 'EditTablePage'
    public boolean insertData(String sourceText, String[] langs, String[] translations){

        // TODO: call getWritableDatabase() or getReadableDatabase() in a background thread,
        // such as with AsyncTask or IntentService, because they make be 'long-running'
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put("source", sourceText);
        for (int i=0; i< langs.length; i++)
            contentValues.put(langs[i], translations[i]);
        contentValues.put("status", 0);

        // Insert the new row, returning the primary key value of the new row
        long result = db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        // On failure, 'insertWithOnConflict' returns -1. We use this to return True/False
        return result != -1;
    }

    public Cursor getDirtyData(String columnsConcated) {
        String [] langs = columnsConcated.split(" ");
        StringBuilder cols = new StringBuilder();
        for (String lang : langs)
            cols.append(lang).append(", ");
        String columns = cols.substring(0, cols.length() - 2); //to get rid of the last ", "
        Log.d("--- getData columns---", columns);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT source, " + columns + " FROM " + TABLE_NAME + " WHERE status = 0 ";
        Log.d("--- getData columns2---", query);
        return db.rawQuery(query, null);
    }

    public boolean wordsTableIsEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return NoOfRows == 0;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id} );
    }
}
