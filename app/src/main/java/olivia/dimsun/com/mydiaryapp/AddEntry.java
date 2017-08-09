package olivia.dimsun.com.mydiaryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/*
https://www.sitepoint.com/starting-android-development-creating-todo-app/
 */

public class AddEntry extends AppCompatActivity {

    private EntryDbHelper mHelper;
    String intentText;

    private static final String TAG = "AddEntry :" ;
    EditText entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        entry = (EditText) findViewById(R.id.ed_entry);

        mHelper = new EntryDbHelper(this);

        Intent retrieveIntent = getIntent();
        if (retrieveIntent!= null){
            intentText = retrieveIntent.getStringExtra("ENTRY");
            entry.setText(intentText);
//            saveWhenEdit(intentText);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_task:
                Log.d(TAG, "Save");
                saveEntry();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveEntry() {

        String entryStr = entry.getText().toString();

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

        values.put(EntryContract.TaskEntry.COL_TASK_TITLE, entryStr);
        db.insertWithOnConflict(EntryContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void saveWhenEdit(String intentText) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        Toast.makeText(this, "Ready to edit", Toast.LENGTH_SHORT).show();

        values.put(EntryContract.TaskEntry.COL_TASK_TITLE, intentText);
        db.insertWithOnConflict(EntryContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
}
