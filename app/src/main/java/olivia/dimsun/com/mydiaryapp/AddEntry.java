package olivia.dimsun.com.mydiaryapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEntry extends AppCompatActivity {

    private EntryDbHelper mHelper;
    String intentText;
    Context context;
    boolean isAnUpdate;
    SimpleDateFormat dateFormat;
    Date date;
    String id, dateStr, entryStr, idRetrieved;

    private static final String TAG = "AddEntry :" ;

    EditText entry;
    TextView idEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        entry = (EditText) findViewById(R.id.ed_entry);
        idEdit = (TextView) findViewById(R.id.idEdit);

        mHelper = new EntryDbHelper(this);

        Intent retrieveIntent = getIntent();

        if (retrieveIntent != null){
            intentText = retrieveIntent.getStringExtra(Constantes.ENTRY);
            idRetrieved = retrieveIntent.getStringExtra(Constantes.ID);

            id = String.valueOf(idRetrieved);
            Log.i(TAG, "ID " +idRetrieved);
            entry.setText(intentText);
            idEdit.setText(id);
            if (id.contains("null")) {
                isAnUpdate = false;
            } else {
                isAnUpdate = true;
            }
            Log.i(TAG, "is An Update "+isAnUpdate);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)

    private void saveEntry() {

        Log.i(TAG, "saveENtry is an update "+isAnUpdate);

        if (!isAnUpdate) {
            newEntry();
            Toast.makeText(this, "Entry has been created", Toast.LENGTH_SHORT).show();
            idEdit.setText(id);
        } else {
            updateEntry();
            Toast.makeText(this, "Entry has been updated", Toast.LENGTH_SHORT).show();
            isAnUpdate = true;
        }

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateEntry(){

        init();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        long idLong = Long.valueOf(id);
        idEdit.setText(id);
        values.put(EntryContract.TaskEntry.COL_TASK_TITLE, entryStr);
        db.update(EntryContract.TaskEntry.TABLE, values, "_id=" +idLong, null);
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void newEntry(){

        init();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        idEdit.setText(id);
        values.put(EntryContract.TaskEntry.COL_TASK_DATE, dateStr);
        values.put(EntryContract.TaskEntry.COL_TASK_TITLE, entryStr);

        db.insertWithOnConflict(EntryContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {

        /*
        import java.text.SimpleDateFormat;
        import java.util.Date;
        et non import android.icu.text.SimpleDateFormat; -> crash
         */

        entryStr = entry.getText().toString();
        dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
        date = new Date();
        dateStr = dateFormat.format(date);
    }

}

