package com.azudaisoft.rawrlist;

import android.provider.BaseColumns;

/**
 * Created by Cody on 1/4/16.
 */
public final class NoteReaderContract {
    public NoteReaderContract() {

    }

    public static abstract class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME="entry";
        public static final String COLUMN_NOTE_VALUE="entryid";

        public static final String TEXT_TYPE =" TEXT";

        public static String getSqlCreateEntries() {
            return SQL_CREATE_ENTRIES;
        }

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + NoteEntry.TABLE_NAME + "( " +
                        NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        NoteEntry.COLUMN_NOTE_VALUE + TEXT_TYPE + " )";
    }
}
