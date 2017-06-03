package com.example.kimhyun.solomon_go;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SolostopActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private GoogleMap map;

    SharedPreferences save_position;
    SharedPreferences.Editor editor_position;

    int count=0;
    float latitude_position,longitude_position;
    double d_latitude,d_longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solostop);
        SupportMapFragment fragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        save_position = getSharedPreferences("position",0);
        editor_position = save_position.edit();

        latitude_position = save_position.getFloat("latitude",37.4512074f);
        longitude_position = save_position.getFloat("longitude",127.1277899f);


        //LatLng nowPoint = new LatLng((double)latitude_position,(double)longitude_position);
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPoint,50));
        // 현재 위치의 지도를 보여주기 위해 정의한 메소드 호출


        //구글 맵 객체 참조
        fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "GoogleMap is ready.");
                float size = 3.6f;
                map = googleMap;


                map.addCircle(new CircleOptions()
                        .center(new LatLng(37.4512074, 127.1277899))
                        .radius(50)
                        .strokeColor(Color.parseColor("#884169e1"))
                        .fillColor(Color.parseColor("#5587cefa")));
                map.addCircle(new CircleOptions()
                        .center(new LatLng(37.4932593, 126.9793331))
                        .radius(50)
                        .strokeColor(Color.parseColor("#884169e1"))
                        .fillColor(Color.parseColor("#5587cefa")));



            }
        });//end of fragment.getMapAsync



        // 일부 초기버전 단말의 문제로 인해 초기화 코드 추가
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }//end of try

        // 위치 정보 확인을 위해 정의한 메소드 호출
        startLocationService();
        // 사용자 권한 확인 메서드 호출
        checkDangerousPermissions();
    }//end of onCreate

    // 위치 정보 확인을 위해 정의한 메소드
    private void startLocationService() {
        // 위치 관리자 객체 참조

        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 위치 리스너 객체 생성

        GPSListener gpsListener = new GPSListener();

        long minTime = 5000;//GPS정보 전달 시간 지정 - 10초마다 위치정보 전달
        float minDistance = 0;//이동거리 지정 - 이동하면 무조건 갱신

        //showCurrentLocation((double)latitude_position,(double)longitude_position);//위치이동
        //위치정보는 위치 프로바이더(Location Provider)를 통해 얻는다
        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, //위치 정보 확인 방법 설정
                    minTime, // 위치 정보 갱신 시간 설정
                    minDistance, //위치 정보 갱신을 위한 최소 이동거리 설정
                    gpsListener);//위치가 변동될 때마다 위치 정보 갱신을 위한 리스너 설정

            // 네트워크(기지국)를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();
                //Toast.makeText(getApplicationContext(), "Last Known Location : " + "Latitude : "
                //        + latitude + "\nLongitude:" + longitude, Toast.LENGTH_LONG).show();
                LatLng nowPoint = new LatLng(latitude,longitude);
            }

        } catch(SecurityException ex) {
            ex.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(), "위치 확인 시작함.", Toast.LENGTH_SHORT).show();
    }//end of startLocationService()

    //위치 정보 확인을 위한 위치 리스너 정의
    private class GPSListener implements LocationListener {
        //위치 정보가 확인(수신)될 때마다 자동 호출되는 메소드
        public void onLocationChanged(Location location) {
            map.clear();
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            latitude_position = (float)location.getLatitude();
            longitude_position = (float)location.getLongitude();
            if(location.getAccuracy()<100||count==0){
                String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
                Log.i("GPSLocationService", msg);

                latitude_position = (float)location.getLatitude();
                editor_position.putFloat("latitude",latitude_position);
                editor_position.putFloat("Longitude",longitude_position);

                LatLng nowPoint = new LatLng(latitude,longitude);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPoint,17));
                // 현재 위치의 지도를 보여주기 위해 정의한 메소드 호출
                showCurrentLocation(latitude, longitude);//위치이동
                count++;
                //editor.putString("ID", ID);

                MarkerOptions optFirst = new MarkerOptions();
                optFirst.position(nowPoint);// 위도 • 경도
                optFirst.icon(BitmapDescriptorFactory.fromResource(
                        R.mipmap.ic_launcher_round));

                map.addMarker(optFirst).showInfoWindow();
            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }// end of GPSListener

    /**
     * 현재 위치의 지도를 보여주기 위해 정의한 메소드
     * @param latitude
     * @param longitude
     */
    private void showCurrentLocation(Double latitude, Double longitude) {
        /* 현재 위치를 이용해 LatLng 객체 생성 - 위도와 경도로 좌표를 표시
           - 전달받은 위도와 경도 값으로 LatLng 객체를 만들면 지도 위에 표시될 수 있는
             새로운 포인트가 만들어짐(포인트 객체 생성)
         */
        LatLng curPoint = new LatLng(latitude, longitude);


        /* 구글맵 객체의 animateCamera() 메소드를 이용하여 그 위치(curPoint)를 중심으로 지도를 보여줌
            @param curPoint
            @param level(배율)
         */


        /* 지도 유형 설정
           - 일반지도인 경우 : GoogleMap.MAP_TYPE_NORMAL
           - 지형도인 경우 : GoogleMap.MAP_TYPE_TERRAIN,
           - 위성 지도인 경우 : GoogleMap.MAP_TYPE_SATELLITE
         */

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }// end of showCurrentLocation



    /* 사용자 권한 확인 메서드
       - import android.Manifest; 를 시킬 것
     */
    private void checkDangerousPermissions() {
        String[] permissions = {//import android.Manifest;
                Manifest.permission.ACCESS_FINE_LOCATION,   //GPS 이용권한
                Manifest.permission.ACCESS_COARSE_LOCATION, //네트워크/Wifi 이용 권한
                Manifest.permission.READ_EXTERNAL_STORAGE,  //읽기 권한
                Manifest.permission.WRITE_EXTERNAL_STORAGE  //쓰기 권한
        };

        //권한을 가지고 있는지 체크
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                //Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }//end of checkDangerousPermissions

    // 사용자의 권한 확인 후 사용자의 권한에 대한 응답 결과를 확인하는 콜백 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }//end of onRequestPermissionsResult

    @Override
    protected void onPause() {
        super.onPause();
    }
}//end of MainActivity
