package com.azudaisoft.rawrlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by Cody on 1/4/16.
 */
public class NoteSQLDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NoteReader.db";
    private static final int DATABASE_VERSION = 1;
    public Context context;



    public NoteSQLDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NoteReaderContract.NoteEntry.getSqlCreateEntries());
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void open() throws SQLException {
        SQLiteDatabase database = getWritableDatabase();
    }
}
