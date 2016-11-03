package ca.uwaterloo.camevent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.ArrayList;
import Core.APIResult;
import Core.JSONDownloader;
import Core.UWOpenDataAPI;
import Events.Event;
import Events.EventTime;
import Events.EventsParser;

public class InitializeActivity extends AppCompatActivity implements JSONDownloader.onDownloadListener {
    //String apiKey = null;
    final String LOGCAT_TAG = "InitializeActivity";
    final String LOGCAT_DOWNLOAD="It has been downloaded ";
    EventDBHandler eventDB= new EventDBHandler(this);
    private EventsParser[] eventparser;
    private ArrayList<Event> events=null;
    private Initialize initilize = new Initialize();
    private String[] url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);
        //...
        // Step 1, do not redownload on screen rotation
        if(savedInstanceState == null) {
            //initilize.create();
            //clear all the data in the databases
            eventDB.deleteallEvents();
            events =initilize.getEvents();
            eventparser= new EventsParser[100];
            url= new String[100];
            System.out.println(events.size());
            String apiKey = getString(R.string.api_key); // store your key in strings.xml
            for(int i=0;i<100;i++){
                EventsParser parser= new EventsParser();
                parser.setParseType(EventsParser.ParseType.EVENTS_SITE_ID);
                eventparser[i]=parser;
                String URL=UWOpenDataAPI.buildURL(eventparser[i].getEndPoint(events.get(i).getSite(), String.valueOf(events.get(i).getEventId())), apiKey);
                url[i] =URL;
            }
            // Step 3
            JSONDownloader downloader = new JSONDownloader(url);
            downloader.setOnDownloadListener(this);
            downloader.start();

        }
    }

    // ...

    @Override
    public void onDownloadFail(String givenURL, int index) {
        // this method is called if the download fails (No internet connection, timeout, bad url, missing permission etc).
        Log.i(LOGCAT_TAG, "Download failed.. url = " + givenURL);
    }
    @Override

    public void onDownloadComplete(APIResult apiResult) {
        // Step 4
        // parseJSON() will do different types of parsing depending on what ParseType you give it.
        // Each Parser has their own ParseTypes
        String givenUrl = apiResult.getUrl();
        for(int i=0;i<100;i++){
            //Log.d(LOGCAT_DOWNLOAD, String.valueOf(i));
            if(givenUrl.equals(url[i])){
            eventparser[i].setAPIResult(apiResult);
            eventparser[i].parseJSON();
            Event eventdisplay= eventparser[i].getSpecificEvent();
            ArrayList<EventTime> eventTimes= eventdisplay.getTimes();
            Eventinfo eventinfo=new Eventinfo(eventdisplay.getEventTitle(),eventdisplay.getLocationName(),String.valueOf(eventdisplay.getLatitude()),String.valueOf(eventdisplay.getLongitude()),eventdisplay.getEventDescriptionRaw(),eventdisplay.getLink(),eventTimes.get(0).getStartDate());
            eventDB.addEventinfo(eventinfo);
            }
        }
    }
}

