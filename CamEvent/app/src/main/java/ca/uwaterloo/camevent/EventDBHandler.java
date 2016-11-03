package ca.uwaterloo.camevent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sound on 2016/10/24.
 * C:\Users\sound\AppData\Local\Android\sdk\platform-tools
 adb -s emulator-5554 shell
 cd data/data/ca.uwaterloo.camevent/databases
 ls
 sqlite3 EventInfo
 .tables
 select* from event;
 */
public class EventDBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "EventInfo";

    //Contacts table name
    private static final String TABLE_EVENT_INFO = "event";

    //shops Table Columns Names
    private static final String KEY_ID = "id";
    private static final String KEY_EVENT_TITLE = "event_title";
    private static final String KEY_EVENT_LOCATION_NAME = "event_location_name";
    private static final String KEY_EVENT_LATITUDE = "event_latitude";
    private static final String KEY_EVENT_LONGITUDE = "event_longitude";
    private static final String KEY_EVENT_DESCRIPTION_ROW = "event_description_row";
    private static final String KEY_EVENT_LINK = "event_link";
    private static final String KEY_EVENT_DATE = "event_date";
    SQLiteDatabase db;


    public EventDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String TABLE_CREATE = "CREATE TABLE " + TABLE_EVENT_INFO + "("
            + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_EVENT_TITLE + " TEXT,"
            + KEY_EVENT_LOCATION_NAME + " TEXT," + KEY_EVENT_LATITUDE + " TEXT,"
            + KEY_EVENT_LONGITUDE + " TEXT," + KEY_EVENT_DESCRIPTION_ROW +" TEXT,"
            + KEY_EVENT_LINK + " TEXT,"  + KEY_EVENT_DATE + " TEXT" + ");";
        db.execSQL(TABLE_CREATE);
        //this.db = db;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,  int oldVersion,int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_INFO);

        // Create tables again
        onCreate(db);
    }
    // Add a new event
    public void addEventinfo(Eventinfo eventinfo) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //String countQuery = "SELECT * FROM " + TABLE_EVENT_INFO;
        //Cursor cursor = db.rawQuery(countQuery, null);
        //int count = cursor.getCount();
        //values.put(KEY_ID, count);
        values.put(KEY_EVENT_TITLE, eventinfo.getEventTitle());
        values.put(KEY_EVENT_LOCATION_NAME, eventinfo.getEventLocationName());
        values.put(KEY_EVENT_LATITUDE, eventinfo.getEventLatitude());
        values.put(KEY_EVENT_LONGITUDE, eventinfo.getEventLongitude());
        values.put(KEY_EVENT_DESCRIPTION_ROW, eventinfo.getEventDescriptionRow());
        values.put(KEY_EVENT_LINK, eventinfo.getEventLink());
        values.put(KEY_EVENT_DATE, eventinfo.getEventDate());
        db.insert(TABLE_EVENT_INFO, null, values);
        db.close();
    }
    //query a new event
    public Eventinfo getEvent(String eventtitle) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENT_INFO, new String[]{KEY_ID,
                KEY_EVENT_TITLE, KEY_EVENT_LOCATION_NAME,
                KEY_EVENT_LATITUDE,KEY_EVENT_LONGITUDE,
                KEY_EVENT_DESCRIPTION_ROW, KEY_EVENT_LINK,
                KEY_EVENT_DATE}, KEY_EVENT_TITLE + "=?",
                new String[]{String.valueOf(eventtitle)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Eventinfo eventinfo = new Eventinfo(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6),cursor.getString(7));
        db.close();
        return eventinfo;
    }
    public void deleteallEvents() {
        db = this.getWritableDatabase();
        String sqlStr = "DELETE FROM " + TABLE_EVENT_INFO;
        db.execSQL(sqlStr);
        db.close();
    }
    public List<Eventinfo> getAllEvents() {
        List<Eventinfo> eventinfos = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENT_INFO,null);
        if(cursor.moveToFirst()) {
            do {
                String event_title = cursor.getString(cursor.getColumnIndex(KEY_EVENT_TITLE));
                String event_location_name = cursor.getString(cursor.getColumnIndex(KEY_EVENT_LOCATION_NAME));
                String event_latitude = cursor.getString(cursor.getColumnIndex(KEY_EVENT_LATITUDE));
                String event_longitude = cursor.getString(cursor.getColumnIndex(KEY_EVENT_LONGITUDE));
                String event_description = cursor.getString(cursor.getColumnIndex(KEY_EVENT_DESCRIPTION_ROW));
                String event_link = cursor.getString(cursor.getColumnIndex(KEY_EVENT_LINK));
                String event_date = cursor.getString(cursor.getColumnIndex(KEY_EVENT_DATE));
                Eventinfo eventinfo = new Eventinfo(event_title, event_location_name,event_latitude,event_longitude,event_description,event_link,event_date);
                eventinfos.add(eventinfo);
            } while(cursor.moveToNext());
        }
        db.close();
        return eventinfos;
    }
}
