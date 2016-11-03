package ca.uwaterloo.camevent;


import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class DisplayActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String strTitle;
    //private String strDes;
    private String strDate;
    private String strLoc;
    private String strLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        String eventtitle;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                eventtitle= null;
            } else {
                eventtitle= extras.getString("Database Data");
            }
        } else {
            eventtitle= (String) savedInstanceState.getSerializable("Database Data");
        }
        EventDBHandler eventDB=new EventDBHandler(this);
        Eventinfo eventinfo=eventDB.getEvent(eventtitle);
        //System.out.println(eventinfo.getEventTitle());
        strTitle=eventinfo.getEventTitle();
        //strDes=eventinfo.getEventDescriptionRow();
        strDate=eventinfo.getEventDate();
        strLoc=eventinfo.getEventLocationName();
        //strLink=Uri.parse(eventinfo.getEventLink());
        strLink= eventinfo.getEventLink();
        //set title and description!
        TextView title = (TextView) findViewById(R.id.title);
        TextView link = (TextView) findViewById(R.id.des);
        title.setText(strTitle);
        link.setText(strLink);
        Linkify.addLinks(link, Linkify.WEB_URLS);
        //des.setText(Html.fromHtml(strDes));
        //set date and location
        ListView list = (ListView) findViewById(R.id.MyListView);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map_date = new HashMap<String, String>();
        map_date.put("ItemTitle", "Date");
        map_date.put("ItemText", strDate);
        mylist.add(map_date);
        HashMap<String, String> map_location = new HashMap<String, String>();
        map_location.put("ItemTitle", "Location");
        map_location.put("ItemText", strLoc);
        mylist.add(map_location);
//        HashMap<String, Uri> map_link= new HashMap<String, Uri>();
//        map_link.put("ItemTitle","Link");
//        map_link.put("ItemText", strLink);
//        map_link.
//        mylist.add(map_link);


        SimpleAdapter mSchedule = new SimpleAdapter(this,
                mylist,
                R.layout.listview,
                new String[] {"ItemTitle", "ItemText"},

                new int[] {R.id.ItemTitle,R.id.ItemText});
        list.setAdapter(mSchedule);

    }
}

