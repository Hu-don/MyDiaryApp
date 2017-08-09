package olivia.dimsun.com.mydiaryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/*
soucis find path + dex ./gradlew --stop + clean + rebuild
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EntryDbHelper mHelper;
    private ListView mEntryListView;
    private ArrayAdapter<String> mAdapter;
    TextView entryTextView;
    long positionItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new EntryDbHelper(this);
        entryTextView = (TextView) findViewById(R.id.entry_title);
        mEntryListView = (ListView) findViewById(R.id.list_entry);

       mEntryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               positionItem = id;
               Intent editEntry = new Intent(MainActivity.this, AddEntry.class);
               editEntry.putExtra("ENTRY", mAdapter.getItem(position));
               startActivity(editEntry);
               deleteTask(view);
               finish();

               Log.i("OnClick---> ", "Position : " +position+ " Id : " +id);


           }
       });

        mEntryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String intentTitle = "Envoi de MyDiaryApp";
                String intTentText = mAdapter.getItem(position) + " -sent from MyDiaryApp";

                Intent onLongClick = new Intent(Intent.ACTION_SEND);
                onLongClick.setType("plain/text");
                onLongClick.putExtra(Intent.EXTRA_SUBJECT, intentTitle);
                onLongClick.putExtra(Intent.EXTRA_TEXT, intTentText);
                startActivity(onLongClick);

                return false;
            }
        });

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_entry:
                add_new_entry();
                Log.d(TAG, "Add a new task");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void add_new_entry() {
        Intent i = new Intent(this, AddEntry.class);
        startActivity(i);
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(EntryContract.TaskEntry.TABLE,
                new String[]{EntryContract.TaskEntry._ID, EntryContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(EntryContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.row_list,
                    R.id.entry_title,
                    taskList);
            mEntryListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    public void deleteTask(View view) {

        View parent = (View) view.getParent();

        TextView taskTextView = (TextView) parent.findViewById(R.id.entry_title);
        String task = String.valueOf(taskTextView.getText());

        SQLiteDatabase db = mHelper.getWritableDatabase();

        db.delete(EntryContract.TaskEntry.TABLE,
                EntryContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }
}
