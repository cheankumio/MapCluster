package com.example.c1103304.myapplication;

import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private ListView lv;
    private ClusterManager<MyItem> mClusterManager;
    ArrayList<HashMap<String, String>> contactList;
    private HashMap<String,String> contact;
    private MyItem offsetItem;
    private GoogleMap mMap;
    private String str3 ="";
    private ArrayList<MyItem> list;
    private ArrayList<MyItem> list2;
    private msqlite ms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactList = new ArrayList<>();
        list = new ArrayList<>();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new mThread().execute();

        Log.d("MYLOG","OnCreate: "+str3);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        moveMap();
    }

    private void moveMap(){
        mClusterManager = new ClusterManager<>(this,mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(25.13245911, 121.7822188))
                .zoom(5)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private void setUpClusterer() {
        mClusterManager.addItems(list);
        Log.d("MYLOG","PostExecute: "+list.get(1).toString());
    }


    private class mThread extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute(){
         super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            list2 = new ArrayList<>();
            ms = new msqlite(MainActivity.this);
            String url = "http://opendata.epa.gov.tw/ws/Data/OTH01104/?%24skip=0&%24top=1000&format=json";
            String jsonStr = sh.makeServiceCall(url);
            //Log.d("MYLOG", "Response from url: " + jsonStr);
            if(jsonStr != null){
                try{
                    //JSONObject jsonobj = new JSONObject(jsonStr);
                    JSONArray contacts =new JSONArray(jsonStr);
                    for(int i = 0;i<contacts.length();i++){
                        JSONObject cs = contacts.getJSONObject(i);
                        String address = cs.getString("Address");
                        String lon = cs.getString("Longitude");
                        String lat = cs.getString("Latitude");
                        String country = cs.getString("Country");
                        String name = cs.getString("Name");
//                        double d1 = Double.parseDouble(lon);
//                        double d2 = Double.parseDouble(lat);
//                        offsetItem = new MyItem(d2,d1);
                        ms.saveCategoryRecord(name,lat,lon,address,country);
                        //list2.add(offsetItem);
                        //Log.d("MYLOG",d1+", "+d2);
                    }
                }catch (final JSONException e) {
                    Log.e("MYLOG", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            list = list2;
            if(list==list2){
                Log.d("MYLOG2","Yes Right!!");
            }else{
                Log.d("MYLOG2","No!!!!");
            }
            //setUpClusterer();
        }

    }

    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }
    }
}
