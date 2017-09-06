package com.dimsun.simplenotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import olivia.dimsun.com.simplenotes.AddEntry;
import olivia.dimsun.com.simplenotes.EntryContract;
import olivia.dimsun.com.simplenotes.EntryDbHelper;
import olivia.dimsun.com.simplenotes.R;

/*
soucis find path + dex ./gradlew --stop + clean + rebuild
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EntryDbHelper mHelper;
    String date, text;
    long itemID;
    RecyclerView mRecyclerView;
    ArrayList <Entry> list;
    com.dimsun.simplenotes.MainAdapter adapter;
    ImageView tuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new EntryDbHelper(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
        tuto = (ImageView) findViewById(R.id.tutoIMG);

        swipe();
        updateUI();
    }

    private void swipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                deleteTask(viewHolder);
                updateUI();
            }
        }).attachToRecyclerView(mRecyclerView);

    }

    private void updateUI() {
        list = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(EntryContract.TaskEntry.TABLE,
                new String[]{EntryContract.TaskEntry._ID, EntryContract.TaskEntry.COL_TASK_DATE, EntryContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);

        while (cursor.moveToNext()) {

            date= cursor.getString(cursor.getColumnIndex(EntryContract.TaskEntry.COL_TASK_DATE));
            text= cursor.getString(cursor.getColumnIndex(EntryContract.TaskEntry.COL_TASK_TITLE));
            itemID = (int) cursor.getLong(cursor.getColumnIndex(EntryContract.TaskEntry._ID));

            Entry entry = new Entry(date, text, itemID);
            list.add(entry);
            Log.i(TAG, " date "+date+ " text " +text+ " id " +itemID);
        }


        initTuto(list);


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
        adapter = new com.dimsun.simplenotes.MainAdapter(MainActivity.this, list);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        cursor.close();
        db.close();
    }

    private void initTuto(ArrayList<Entry> list) {
        if (!list.isEmpty()) {
            tuto.setVisibility(View.GONE);
        } else {
            tuto.setVisibility(View.VISIBLE);
        }
    }

    public void deleteTask(RecyclerView.ViewHolder viewHolder) {

        TextView taskId = (TextView) viewHolder.itemView.findViewById(R.id.idTV);
        String task = String.valueOf(taskId.getText());

        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.delete(EntryContract.TaskEntry.TABLE,
                EntryContract.TaskEntry._ID + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    public void add(View view) {
        Intent i = new Intent(this, AddEntry.class);
        startActivity(i);
        finish();
    }
}
