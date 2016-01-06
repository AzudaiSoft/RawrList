package com.azudaisoft.rawrlist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private FloatingActionButton mFloatingActionButton;
    private Cursor mNoteCursor;
    private String mItemId;
    private NoteSQLDbHelper mNoteSQLDbHelper;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor itemClickedCursor = (Cursor) mListView.getItemAtPosition(position);
                String itemId = itemClickedCursor.getString(itemClickedCursor.getColumnIndex("_id"));
                String itemValue = itemClickedCursor.getString(1);
                Intent i = new Intent(getApplicationContext(), NoteActivity.class);
                i.putExtra("valueOfListItem", itemValue);
                i.putExtra("valueOfRowId", itemId);
                startActivity(i);


            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
                        Cursor itemLongClickedCursor = (Cursor) mListView.getItemAtPosition(arg2);
                        mItemId = itemLongClickedCursor.getString(itemLongClickedCursor.getColumnIndex("_id")); //This is the ID in the database of the long clicked item
//                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("valueOfLongClickedItem", itemId).commit();

                        MainActivity.this.startActionMode(new ActionBarCallback());
                        return true;
            }
        });

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
    class ActionBarCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete_note:
                    mNoteSQLDbHelper = new NoteSQLDbHelper(getApplicationContext());
                    SQLiteDatabase db = mNoteSQLDbHelper.getWritableDatabase();
                    db.delete("entry", "_id=" + mItemId, null);
                    mNoteCursor.requery();
                    mode.finish();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    }
}
