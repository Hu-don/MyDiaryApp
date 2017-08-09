package olivia.dimsun.com.mydiaryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hudon on 08/08/2017.
 */

public class EntryDbHelper extends SQLiteOpenHelper {

    public EntryDbHelper(Context context) {
        super(context, EntryContract.DB_NAME, null, EntryContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + EntryContract.TaskEntry.TABLE + " ( " +
                EntryContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EntryContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EntryContract.TaskEntry.TABLE);
        onCreate(db);
    }

}
