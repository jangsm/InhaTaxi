package com.inhataxi.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.inhataxi.HttpConnection;
import com.inhataxi.R;
import com.inhataxi.adapter.MapAdapter;
import com.inhataxi.model.MapSearchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MapSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backTv;
    private EditText searchEt;
    private ImageView searchTv;
    private TextView presentTv, logoTv;
    private ListView listView;
    public ListView listview;
    public MapAdapter adapter;
    public Context context;
    private HttpConnection httpConn = HttpConnection.getInstance();
    private LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION=2;
    private Activity activity;
    int state=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        setContentView(R.layout.activity_map_search);

//        getHashKey();

        context = this;
        activity = this;

        backTv = findViewById(R.id.map_search_back_tv);
        searchEt = findViewById(R.id.map_search_input_et);
        searchTv = findViewById(R.id.map_search_finish_tv);
        presentTv = findViewById(R.id.map_search_present_tv);
        listView = findViewById(R.id.map_search_list_view);
        logoTv = findViewById(R.id.map_search_logo_tv);

        backTv.setOnClickListener(this);
        searchTv.setOnClickListener(this);
        presentTv.setOnClickListener(this);

        listview = findViewById(R.id.map_search_list_view);
        adapter = new MapAdapter(this);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        state = intent.getIntExtra("select", 1);
        if(state ==1) {
            logoTv.setText("출발지를 선택하세요");
        }
        else if(state ==2){
            logoTv.setText("목적지를 선택하세요");
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MapSearchData item = (MapSearchData) adapterView.getItemAtPosition(position);

                String name = adapter.itemList.get(position).getName();
                String address = adapter.itemList.get(position).getAddress();
                double x = adapter.itemList.get(position).getX();
                double y = adapter.itemList.get(position).getY();

                Intent intent = new Intent(context, MapResultActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                intent.putExtra("x", x);
                intent.putExtra("y", y);
                intent.putExtra("select",state);
                startActivity(intent);
                finish();
            }
        });
        //Send ListView Item information to MapResultActivity Class
    }

//    private void getHashKey(){
//        PackageInfo packageInfo = null;
//        try {
//            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (packageInfo == null)
//            Log.e("KeyHash", "KeyHash:null");
//
//        for (Signature signature : packageInfo.signatures) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            } catch (NoSuchAlgorithmException e) {
//                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_search_back_tv:
                finish();
                break;
            case R.id.map_search_finish_tv:
                adapter.itemList.clear();
                httpConn.mapSearchList(searchEt.getText().toString(), callback);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                break;
            case R.id.map_search_present_tv:
                locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            //사용자의 현재 위치
                Location userLocation = getMyLocation();
                double latitude =0;
                double longitude =0;
                if( userLocation != null ) {
                    latitude = userLocation.getLatitude();
                    longitude = userLocation.getLongitude();
                    Log.i("////////////현재 내 위치값 : ", String.valueOf(latitude));
                }
                Log.i("Vsdv", String.valueOf(latitude));
                //Get present latitude, longitude

                Geocoder geo = new Geocoder(this, Locale.getDefault());
                List<Address> names = null;
                try {
                    names = geo.getFromLocation( 37.504487,127.0467683,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(names.size()!=0) {
                    Intent intent = new Intent(context, MapResultActivity.class);
                    intent.putExtra("name", names.get(0).getAddressLine(0));
                    intent.putExtra("address", names.get(0).getAddressLine(0));
                    intent.putExtra("x", latitude);
                    intent.putExtra("y", longitude);
                    Log.i("HELOOWFDS", String.valueOf(latitude));
                    intent.putExtra("select", state);
                    startActivity(intent);
                    finish();
                }
                //Get Address Line for geocoder coordinate
                break;
                //get present location coordinate
        }
    }

    private Location getMyLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("////////////사용자에게 권한을 요청해야함");
            Log.i("DVsdv","YES");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getMyLocation(); //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        }
        else {
            System.out.println("////////////권한요청 안해도됨");
            Log.i("DVsdv","NO");
            // 수동으로 위치 구하기
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
                Log.i("VDsdv","DV");
            }
            else {
                Log.i("VDsdv","DVdsv");
            }
        }
        return currentLocation;
    }
    //Function to get present location

    private final Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("Error", "콜백오류:"+e.getMessage());
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray arr = jsonObject.getJSONArray("documents");

                for(int i=0;i<arr.length();i++){
                    String name = arr.getJSONObject(i).getString("place_name");
                    double x = arr.getJSONObject(i).getDouble("y");
                    double y = arr.getJSONObject(i).getDouble("x");
                    String addr = arr.getJSONObject(i).getString("address_name");
                    adapter.addList(name,addr,x,y);

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }
                    });
                    // changing ui must use ui thread
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //JSON Passing
        }
    };
    //use kakao rest api for to get search list (Kakao Rest API Keyword)
}
