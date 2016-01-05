package com.azudaisoft.rawrlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    private static final String TAG = "NoteActivity";

    private Button mSaveButton;
    private EditText mEditText;
    private String mNoteValue;
    private String mSaveToast = "Note saved!";
    private static NoteSQLDbHelper mNoteSQLDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSaveButton = (Button) findViewById(R.id.save_button);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToDatabase();
            }
        });
    }

    public void writeToDatabase() {
        mEditText = (EditText) findViewById(R.id.note_text);

        mNoteValue = mEditText.getText().toString(); //Value of whatever is in the EditText

        //Do super magical saving to database stuff
        mNoteSQLDbHelper = new NoteSQLDbHelper(this);
        SQLiteDatabase db = mNoteSQLDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteReaderContract.NoteEntry.COLUMN_NOTE_VALUE, mNoteValue);

        long newRowId;

        newRowId = db.insert(
                NoteReaderContract.NoteEntry.TABLE_NAME,
                null,
                contentValues
        );
        db.close();
        Toast.makeText(getApplicationContext(), mSaveToast, Toast.LENGTH_SHORT).show();
        readFromDatabase();
    }
    public void readFromDatabase() {
        SQLiteDatabase db = mNoteSQLDbHelper.getReadableDatabase();

        String[] projection = {
                NoteReaderContract.NoteEntry._ID,
                NoteReaderContract.NoteEntry.COLUMN_NOTE_VALUE,

        };

        String sortOrder =
                NoteReaderContract.NoteEntry.COLUMN_NOTE_VALUE + " ASC";

        Cursor c = db.query(
                NoteReaderContract.NoteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        c.moveToFirst();
        Log.v(TAG, c.getString(c.getColumnIndex("entryid")));
    }

}
