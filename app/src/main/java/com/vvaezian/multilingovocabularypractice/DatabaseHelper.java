package com.vvaezian.multilingovocabularypractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    DatabaseHelper(final Context context, final String dbName) {
        // for explanation of the extra argument `dbName` see getDataBaseHelper method in HelperFunctions java file
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String TABLE_NAME = "WordsTable";
        // this is used in HelperFunction as well. If you wanted to change this, make sure to do the same change there as well.
        final String[] allLangs = {"fr", "de", "es", "it", "en", "ar", "ch", "tw", "nl", "hi", "fa", "pt", "ru", "ja", "tr", "sv"};

        StringBuilder createString = new StringBuilder("CREATE TABLE " + TABLE_NAME + " (source TEXT PRIMARY KEY");
        for (String lang:allLangs)
            createString.append(", ").append(lang).append(" Text");
        createString.append(", status TINYINT );");
        Log.d("create query: ", createString.toString());
        db.execSQL(createString.toString());
    }
    // TODO: Implement onUpgrade properly
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String TABLE_NAME = "WordsTable";

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private final String TABLE_NAME = "WordsTable";

    // this function is used in 'EditTablePage'
    public boolean insertData(String sourceText, String[] langs, String[] translations, int status){

        // TODO: call getWritableDatabase() or getReadableDatabase() in a background thread,
        // such as with AsyncTask or IntentService, because they may be 'long-running'
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put("source", sourceText);
        for (int i=0; i< langs.length; i++)
            contentValues.put(langs[i] , translations[i]);
        contentValues.put("status", status);

        // Insert the new row, returning the primary key value of the new row
        long result = db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        // On failure, 'insertWithOnConflict' returns -1. We use this to return True/False
        return result != -1;
    }

    public Cursor getAllData(String[] langs){
        StringBuilder cols = new StringBuilder();
        for (String lang : langs)
            cols.append(lang).append(", ");
        String columns = cols.substring(0, cols.length() - 2); //to get rid of the last ", "
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT source, "  + columns + " FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public Cursor getDirtyData(String[] langs) {
        // retrieves rows with status 0 to be sent to the remote server

        StringBuilder cols = new StringBuilder();
        for (String lang : langs)
            cols.append(lang).append(", ");
        String columns = cols.substring(0, cols.length() - 2); //to get rid of the last ", "
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT source, " + columns + " FROM " + TABLE_NAME + " WHERE status = 0 ";
        return db.rawQuery(query, null);
    }

    public long getRowCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }

    public void cleanData(){
        // change all status to 1
        // this is invoked when syncUp has been successful
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET status=1");
    }

    public boolean wordsTableIsEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return NoOfRows == 0;
    }

    public Integer deleteRow(String source) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "source = ?", new String[] {source} );
    }
}
