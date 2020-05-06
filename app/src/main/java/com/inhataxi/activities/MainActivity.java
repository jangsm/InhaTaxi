package com.inhataxi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.demono.AutoScrollViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inhataxi.R;
import com.inhataxi.model.FineDust;
import com.inhataxi.model.MainEventBanner;
import com.inhataxi.model.Sky;
import com.inhataxi.model.Station;
import com.inhataxi.model.Temperature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView finedust, finedust2, finedustgrade, finedustgrade2, cmi, temp, max_min_temp;

    private ImageView weatherimg;

    private DrawerLayout mDrawerLayout;
    private View mDrawerView;

    double latitude, longitude;

    private LocationManager locationManager;

    Geocoder geocoder = new Geocoder(this);

    OkHttpClient client = new OkHttpClient();

    ChangeLocation changeLocation;
    LocationToStationName locationToStationName;
    SNameToFineDust sNameToFineDust;
    GetWeather getWeather;

    private static final int REQUEST_CODE_LOCATION = 2;


    ArrayList<MainEventBanner> mArrayListMainEventBanner = new ArrayList<>();
    AutoScrollViewPager mAutoViewPagerEventBanner;
    AutoBannerAdapter mAutoBannerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }


        setContentView(R.layout.activity_main);
        init();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getMyLocation();
//        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        getMyLocation();
        mArrayListMainEventBanner.add(new MainEventBanner("다문화융합연구소“글로컬다문화교육포럼 국내학자 초청특강” 개최", "http://www.inha.ac.kr/CrossEditor/binary/images/000006/%EB%8B%A4%EB%AC%B8%ED%99%94.jpg", "http://www.inha.ac.kr/kr/952/subview.do?&enc=Zm5jdDF8QEB8JTJGYmJzJTJGa3IlMkYxMSUyRjIxMzAyJTJGYXJ0Y2xWaWV3LmRvJTNGcGFnZSUzRDElMjZzcmNoQ29sdW1uJTNEJTI2c3JjaFdyZCUzRCUyNmJic0NsU2VxJTNEJTI2YmJzT3BlbldyZFNlcSUzRCUyNnJnc0JnbmRlU3RyJTNEJTI2cmdzRW5kZGVTdHIlM0QlMjZpc1ZpZXdNaW5lJTNEZmFsc2UlMjZwYXNzd29yZCUzRCUyNg=="));
        mArrayListMainEventBanner.add(new MainEventBanner("인천 섬으로 떠나는 봉사활동 ‘섬 프로젝트’ 본격 가동","http://www.inha.ac.kr/CrossEditor/binary/images/000006/%EC%82%AC%EC%A7%84_2.jpg","http://www.inha.ac.kr/kr/952/subview.do?&enc=Zm5jdDF8QEB8JTJGYmJzJTJGa3IlMkYxMSUyRjIxMjkxJTJGYXJ0Y2xWaWV3LmRvJTNGcGFnZSUzRDElMjZzcmNoQ29sdW1uJTNEJTI2c3JjaFdyZCUzRCUyNmJic0NsU2VxJTNEJTI2YmJzT3BlbldyZFNlcSUzRCUyNnJnc0JnbmRlU3RyJTNEJTI2cmdzRW5kZGVTdHIlM0QlMjZpc1ZpZXdNaW5lJTNEZmFsc2UlMjZwYXNzd29yZCUzRCUyNg=="));
        mArrayListMainEventBanner.add(new MainEventBanner("100회 전국체전 필승 각오 다져", "http://www.inha.ac.kr/CrossEditor/binary/images/000005/%EC%82%AC%EC%A7%84_1_9.jpg", "http://www.inha.ac.kr/kr/952/subview.do?&enc=Zm5jdDF8QEB8JTJGYmJzJTJGa3IlMkYxMSUyRjIxMjY4JTJGYXJ0Y2xWaWV3LmRvJTNGcGFnZSUzRDElMjZzcmNoQ29sdW1uJTNEJTI2c3JjaFdyZCUzRCUyNmJic0NsU2VxJTNEJTI2YmJzT3BlbldyZFNlcSUzRCUyNnJnc0JnbmRlU3RyJTNEJTI2cmdzRW5kZGVTdHIlM0QlMjZpc1ZpZXdNaW5lJTNEZmFsc2UlMjZwYXNzd29yZCUzRCUyNg=="));
        mArrayListMainEventBanner.add(new MainEventBanner("한국공학교육학회가 인정한 우수강의 교수로 선정", "http://www.inha.ac.kr/CrossEditor/binary/images/000005/%EA%B6%8C%EA%B5%AC%EC%9D%B8.jpg", "http://www.inha.ac.kr/kr/952/subview.do?&enc=Zm5jdDF8QEB8JTJGYmJzJTJGa3IlMkYxMSUyRjIxMjQ4JTJGYXJ0Y2xWaWV3LmRvJTNGcGFnZSUzRDElMjZzcmNoQ29sdW1uJTNEJTI2c3JjaFdyZCUzRCUyNmJic0NsU2VxJTNEJTI2YmJzT3BlbldyZFNlcSUzRCUyNnJnc0JnbmRlU3RyJTNEJTI2cmdzRW5kZGVTdHIlM0QlMjZpc1ZpZXdNaW5lJTNEZmFsc2UlMjZwYXNzd29yZCUzRCUyNg=="));

        mAutoBannerAdapter = new AutoBannerAdapter(mArrayListMainEventBanner, this);
        mAutoViewPagerEventBanner.setAdapter(mAutoBannerAdapter);
        mAutoViewPagerEventBanner.startAutoScroll();
        mAutoViewPagerEventBanner.setSlideDuration(2000);
//       뷰페이저 미리보기 설정//
        mAutoViewPagerEventBanner.setClipToPadding(false);
        int dpValue = 30;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        mAutoViewPagerEventBanner.setPadding(margin, 0, margin, 0);
        mAutoViewPagerEventBanner.setPageMargin(margin / 2);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_go_school:
                //등교 액티비티로 이동
                Intent intentSchool = new Intent(this, MapSearchActivity.class);
                intentSchool.putExtra("select", 1);
                startActivity(intentSchool);
                break;
            case R.id.rl_go_home:
                //하교 액티비티로 이동
                Intent intentHome = new Intent(this, MapSearchActivity.class);
                intentHome.putExtra("select", 2);
                startActivity(intentHome);
                break;
        }
    }

    void init(){
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerView = findViewById(R.id.drawer);
        finedust = findViewById(R.id.finedust);
        finedust2 = findViewById(R.id.finedust2);
        finedustgrade = findViewById(R.id.finedustGrade);
        finedustgrade2 = findViewById(R.id.finedustGrade2);
        cmi = findViewById(R.id.cmi);
        temp = findViewById(R.id.temp);
        max_min_temp = findViewById(R.id.max_min_temp);
        weatherimg = findViewById(R.id.weatherimg);
        mAutoViewPagerEventBanner = findViewById(R.id.home_vp_event_banner);
    }

    private void getMyLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("////////////사용자에게 권한을 요청해야함");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getMyLocation(); //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        } else {
            System.out.println("////////////권한요청 안해도됨");

            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
//                double lng = currentLocation.getLongitude();
//                double lat = currentLocation.getLatitude();

                Location userLocation = currentLocation;
                if (userLocation != null) {
                    latitude = userLocation.getLatitude();
                    longitude = userLocation.getLongitude();
                    System.out.println(latitude + " " + longitude);
                    List<Address> list = null;
                    try {
                        list = geocoder.getFromLocation(latitude, longitude, 5);
                        if (list.size() > 0) {
                            android.location.Address address = list.get(0);
                            String adds = address.getSubLocality();
//                            pos.setText(adds);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String lat = Double.toString(latitude);
                    String lon = Double.toString(longitude);
                    String url = "https://apis.openapi.sk.com/weather/current/minutely?version=2&lat="
                            + lat + "&lon=" + lon;
                    getWeather = new GetWeather();
                    getWeather.execute(url);

                    String url4 = "https://dapi.kakao.com/v2/local/geo/transcoord.json?x=" + lon
                            + "&y=" + lat + "&input_coord=WGS84&output_coord=TM";
                    changeLocation = new ChangeLocation();
                    changeLocation.execute(url4);


                }
            }
        }
//        return currentLocation;
    }

    private class ChangeLocation extends AsyncTask<String, Void, String> {

        String nexturl = null;

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String url = strings[0];

            try {
                Request request = new Request.Builder()
                        .addHeader("Authorization", "KakaoAK 8ca7465464a6683d186a3a0df5f9ab61")
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();

                System.out.println(result);

                JSONObject jsonObject = new JSONObject(result);
                String documents = jsonObject.getString("documents");
                JSONArray jsonArray = new JSONArray(documents);
                JSONObject subJsonObject = jsonArray.getJSONObject(0);
                double x = subJsonObject.getDouble("x");
                double y = subJsonObject.getDouble("y");
                String tmx = Double.toString(x);
                String tmy = Double.toString(y);

                nexturl = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?tmX=" + tmx
                        + "&tmY=" + tmy
                        + "&ServiceKey=loAKiZyH1dSAChT9zqtXADC%2Fq4PTs6FAApku5zs9J9ozySaGoJqXCaw478Q8S5aXJVGTG4otPiIp4LQJZKF3pQ%3D%3D"
                        + "&_returnType=json";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            locationToStationName = new LocationToStationName();
            locationToStationName.execute(nexturl);
            super.onPostExecute(s);
        }
    }

    private class LocationToStationName extends AsyncTask<String, Void, String> {

        String nexturl = null;

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String url = strings[0];


            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();
                System.out.println(result);

                JSONObject jsonObject = new JSONObject(result);
                String list = jsonObject.getString("list");
                JSONArray jsonArray = new JSONArray(list);
                JSONObject subJsonObject = jsonArray.getJSONObject(0);
                String stationName = subJsonObject.getString("stationName");

                nexturl = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName="
                        + stationName + "&dataTerm=daily"
                        + "&pageNo=1&numOfRows=1"
                        + "&ServiceKey=loAKiZyH1dSAChT9zqtXADC%2Fq4PTs6FAApku5zs9J9ozySaGoJqXCaw478Q8S5aXJVGTG4otPiIp4LQJZKF3pQ%3D%3D&ver=1.3&_returnType=json";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            sNameToFineDust = new SNameToFineDust();
            sNameToFineDust.execute(nexturl);
            super.onPostExecute(s);
        }
    }

    private class SNameToFineDust extends AsyncTask<String, Void, String> {

        ArrayList<FineDust> fineDusts = new ArrayList<>();
        String tmp = null;

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String url = strings[0];

            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();
                System.out.println(result);

                Gson gson = new Gson();

                JSONObject jsonObject = new JSONObject(result);
                String list = jsonObject.getString("list");

                fineDusts = gson.fromJson(list, new TypeToken<ArrayList<FineDust>>() {
                }.getType());

                System.out.println(fineDusts.get(0).getKhaiValue());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            finedust.setText(fineDusts.get(0).getPm10Value() + "㎍/㎥");
            System.out.println("수치: " + fineDusts.get(0).getPm10Grade1h());
            if (fineDusts.get(0).getPm10Grade1h().equals("1")) {
                finedustgrade.setText("좋음");
                finedustgrade.setBackground(getDrawable(R.drawable.grade1));
            } else if (fineDusts.get(0).getPm10Grade1h().equals("2")) {
                finedustgrade.setText("보통");
                finedustgrade.setBackground(getDrawable(R.drawable.grade2));
            } else if (fineDusts.get(0).getPm10Grade1h().equals("3")) {
                finedustgrade.setText("나쁨");
                finedustgrade.setBackground(getDrawable(R.drawable.grade3));
            } else if (fineDusts.get(0).getPm10Grade1h().equals("4")) {
                finedustgrade.setText("매우나쁨");
                finedustgrade.setBackground(getDrawable(R.drawable.grade4));
            }
            finedust2.setText(fineDusts.get(0).getPm25Value() + "㎍/㎥");
            if (fineDusts.get(0).getPm25Grade1h().equals("1")) {
                finedustgrade2.setText("좋음");
                finedustgrade2.setBackground(getDrawable(R.drawable.grade1));
            } else if (fineDusts.get(0).getPm25Grade1h().equals("2")) {
                finedustgrade2.setText("보통");
                finedustgrade2.setBackground(getDrawable(R.drawable.grade2));
            } else if (fineDusts.get(0).getPm25Grade1h().equals("3")) {
                finedustgrade2.setText("나쁨");
                finedustgrade2.setBackground(getDrawable(R.drawable.grade3));
            } else if (fineDusts.get(0).getPm25Grade1h().equals("4")) {
                finedustgrade2.setText("매우나쁨");
                finedustgrade2.setBackground(getDrawable(R.drawable.grade4));
            }

            if (fineDusts.get(0).getKhaiGrade().equals("1")) {
                tmp = "좋음";
                cmi.setText(fineDusts.get(0).getKhaiValue() + " / " + tmp);
                cmi.setBackgroundResource(R.drawable.grade1);
            } else if (fineDusts.get(0).getKhaiGrade().equals("2")) {
                tmp = "보통";
                cmi.setText(fineDusts.get(0).getKhaiValue() + " / " + tmp);
                cmi.setBackgroundResource(R.drawable.grade2);
            } else if (fineDusts.get(0).getKhaiGrade().equals("3")) {
                tmp = "나쁨";
                cmi.setText(fineDusts.get(0).getKhaiValue() + " / " + tmp);
                cmi.setBackgroundResource(R.drawable.grade3);
            } else {
                tmp = "매우나쁨";
                cmi.setText(fineDusts.get(0).getKhaiValue() + " / " + tmp);
                cmi.setBackgroundResource(R.drawable.grade4);
            }

            super.onPostExecute(s);
        }
    }


    private class GetWeather extends AsyncTask<String, Void, String> {

        Sky _sky;
        Temperature _temperature;
        Station _station;

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String url = strings[0];

            try {
                Request request = new Request.Builder()
                        .addHeader("appKey", "331b3c08-cb30-48da-9ac5-b4c80299ee76")
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();
                System.out.println("날씨: " + result);

                Gson gson = new Gson();

                JSONObject jsonObject = new JSONObject(result);
                String weather = jsonObject.getString("weather");
                JSONObject jsonObject1 = new JSONObject(weather);
                String minutely = jsonObject1.getString("minutely");
                JSONArray jsonArray = new JSONArray(minutely);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject subJsonObject = jsonArray.getJSONObject(i);
                    String sky = subJsonObject.getString("sky");
                    _sky = gson.fromJson(sky, new TypeToken<Sky>() {
                    }.getType());
                    String temperature = subJsonObject.getString("temperature");
                    _temperature = gson.fromJson(temperature, new TypeToken<Temperature>() {
                    }.getType());
                    String station = subJsonObject.getString("station");
                    _station = gson.fromJson(station, new TypeToken<Station>() {
                    }.getType());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (_sky.getCode().equals("SKY_A01")) {
                weatherimg.setImageResource(R.drawable.sunny);
            } else if (_sky.getCode().equals("SKY_A02")) {
                weatherimg.setImageResource(R.drawable.weather02);
            } else if (_sky.getCode().equals("SKY_A03")) {
                weatherimg.setImageResource(R.drawable.weather03);
            } else if (_sky.getCode().equals("SKY_A04")) {
                weatherimg.setImageResource(R.drawable.weather04);
            } else if (_sky.getCode().equals("SKY_A05")) {
                weatherimg.setImageResource(R.drawable.weather05);
            } else if (_sky.getCode().equals("SKY_A06")) {
                weatherimg.setImageResource(R.drawable.weather06);
            } else if (_sky.getCode().equals("SKY_A07")) {
                weatherimg.setImageResource(R.drawable.weather07);
            } else if (_sky.getCode().equals("SKY_A08")) {
                weatherimg.setImageResource(R.drawable.weather04);
            } else if (_sky.getCode().equals("SKY_A09")) {
                weatherimg.setImageResource(R.drawable.weather05);
            } else if (_sky.getCode().equals("SKY_A10")) {
                weatherimg.setImageResource(R.drawable.weather06);
            } else {
                weatherimg.setImageResource(R.drawable.weather08);
            }
            int a = (int) Double.parseDouble(_temperature.getTc());
            System.out.println(a);
            String tc = Integer.toString(a);
            temp.setText(tc + "°C");
            int max = (int) Double.parseDouble(_temperature.getTmax());
            String tmax = Integer.toString(max);
            int min = (int) Double.parseDouble(_temperature.getTmin());
            String tmin = Integer.toString(min);
            max_min_temp.setText("최고온도: " + tmax + "°C" + "\n"
                    + "최저온도: " + tmin + "°C");
            super.onPostExecute(s);
        }
    }

//    void init() {
//        mDrawerLayout = findViewById(R.id.drawer_layout);
//        mDrawerView = findViewById(R.id.drawer);
//        finedust = findViewById(R.id.finedust);
//        finedust2 = findViewById(R.id.finedust2);
//        finedustgrade = findViewById(R.id.finedustGrade);
//        finedustgrade2 = findViewById(R.id.finedustGrade2);
//        cmi = findViewById(R.id.cmi);
//        temp = findViewById(R.id.temp);
//        max_min_temp = findViewById(R.id.max_min_temp);
//        weatherimg = findViewById(R.id.weatherimg);
//    }

    public void customOnClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_home_btn_hamburger:
                mDrawerLayout.openDrawer(mDrawerView);
                break;
            case R.id.custom_drawable_layout_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("logout", true);
                startActivity(intent);
                finish();
                break;
            case R.id.custom_drawable_iv_cancel:
                mDrawerLayout.closeDrawer(mDrawerView);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(mDrawerView);
        } else {
            super.onBackPressed();
        }
    }
}
