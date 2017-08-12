package olivia.dimsun.com.mydiaryapp;

/**
 * Created by hudon on 10/08/2017.
 */

public class Entry {

    long itemId;
    String entryDate;
    String entryText;

    public Entry(String entryDate, String entryText, long id) {
        this.entryDate = entryDate;
        this.entryText = entryText;
        this.itemId = id;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getEntryText() {
        return entryText;
    }

    public long getitemId() {
        return itemId;
    }
}