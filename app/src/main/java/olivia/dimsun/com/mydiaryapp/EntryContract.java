package olivia.dimsun.com.mydiaryapp;

import android.provider.BaseColumns;

/**
 * Created by hudon on 08/08/2017.
 * https://github.com/Clans/FloatingActionButton
 */

public class EntryContract {

    public static final String DB_NAME = "olivia.dimsun.com.mydiaryapp";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "entry";

        public static final String COL_TASK_ID = "id";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DATE = "date";
    }
}
