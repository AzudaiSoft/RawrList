package com.azudaisoft.rawrlist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private Cursor mNoteCursor;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NoteSQLDbHelper helper = new NoteSQLDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        mNoteCursor = db.rawQuery("SELECT * FROM entry", null);


        mListView = (ListView) findViewById(R.id.main_list_view);
        SimpleCursorAdapter noteAdapter = new SimpleCursorAdapter(this,
                R.layout.content_main,
                mNoteCursor,
                new String[] {"entryid"},
                new int[] {R.id.text1});

        mListView.setAdapter(noteAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.add_button) {
            addNote();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mNoteCursor.requery(); //DIRTY, DEPRECATED, BAD
    }

    public void addNote() {
        Intent i = new Intent(this, NoteActivity.class);

        startActivity(i);

    }
}
