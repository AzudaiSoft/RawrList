package com.azudaisoft.rawrlist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    private static final String TAG = "NoteActivity";

    private Button mSaveButton;
    private EditText mEditText;
    private String mNoteValue;
    private String mItemId;
    private String mSaveToast = "Note saved!";
    private static NoteSQLDbHelper mNoteSQLDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mEditText = (EditText) findViewById(R.id.note_text);
        if (intent.hasExtra("valueOfListItem")) {
            String listItemValue = intent.getExtras().getString("valueOfListItem"); //Value of the list item, passed from the main activity :D
            mItemId = intent.getExtras().getString("valueOfRowId"); //Value of the ID in the database
            mEditText.append(listItemValue);
        }
//        if (intent.hasExtra("valueOfRowId")) {
//            updateDatabase();
//        } else {
//            mDatabaseUpdated = false;
//        }

        mSaveButton = (Button) findViewById(R.id.save_button);

        if (!intent.hasExtra("valueOfRowId")) {
            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    writeToDatabase();
                }
            });
        } else {
            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDatabase();
                }
            });

        }
    }

    public void writeToDatabase() {
        mEditText = (EditText) findViewById(R.id.note_text);
        mNoteValue = mEditText.getText().toString(); //Value of whatever is in the EditText
        if (!TextUtils.isEmpty(mNoteValue)) {
            //Do super magical saving to database stuff, but only if the EditText is not empty.
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
        } else {
            Toast.makeText(this, "Note cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateDatabase() {
        mNoteValue = mEditText.getText().toString();
        if (!TextUtils.isEmpty(mNoteValue)) {
            mNoteSQLDbHelper = new NoteSQLDbHelper(this);
            SQLiteDatabase db = mNoteSQLDbHelper.getWritableDatabase();
            mEditText = (EditText) findViewById(R.id.note_text);
            ContentValues cv = new ContentValues();
            cv.put(NoteReaderContract.NoteEntry.COLUMN_NOTE_VALUE, mNoteValue);
            db.update("entry", cv, "_id=" + mItemId, null);
            Toast.makeText(getApplicationContext(), mSaveToast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }

}
