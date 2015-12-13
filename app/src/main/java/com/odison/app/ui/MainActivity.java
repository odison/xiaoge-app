package com.odison.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.odison.app.AppManager;
import com.odison.app.R;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TencentLocationListener {
    private MapView mapView = null;

    private long firstTime = 0;

    private DoubleClickExitHelper mDoubleClickExit ;

    private TencentLocation mLocation;
    private TencentLocationManager mLocationManager;
    private MarkerOptions mMarker = null;
    private String mRequestParams;

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if(i == TencentLocation.ERROR_OK){
            mLocation = tencentLocation;
            //Marker mMarker = new Marker();
            //Marker mMarker = new Marker();
            //mMarker.setPosition(Utils.of(mLocation));
            mapView.getMap().animateTo(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
            String tmp = "lat:"+mLocation.getLatitude()+" lng:"+mLocation.getLongitude();


            Toast.makeText(getApplicationContext(), tmp,
                    Toast.LENGTH_SHORT).show();



            if(mMarker == null){
                mMarker = new MarkerOptions().position(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()))
                        .title("Home")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.position_64));
                mapView.getMap().addMarker(mMarker);
            }else{
                mMarker.position(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()));
            }
            //mMarker = new Marker(new MarkerOptions().position(new LatLng(mLocation.getLatitude(),mLocation.getLongitude())))
            //BitmapDescriptorFactory.fromBitmap(R.drawable.position_22);
//            mapView.getMap().addMarker(new MarkerOptions()
//                                            .position(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()))
//                                            .title("Home")
//                    .icon( BitmapDescriptorFactory.fromResource(R.drawable.position_22))
//            );
            //mapView.getController().animateTo(Utils.of(mLocation));
            mapView.invalidate();

        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    private void startLocation() {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(5000);
        mLocationManager.requestLocationUpdates(request, this);

//        mRequestParams = request.toString() + ", 坐标系="
//                + DemoUtils.toString(mLocationManager.getCoordinateType());
    }

    private void stopLocation() {
        mLocationManager.removeUpdates(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //管理activity
        AppManager.getAppManager().addActivity(this);

        mapView = (MapView)findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        //mapView.setBuiltInZoomControls(true);
        mapView.getMap().setZoom(13);
        mLocationManager = TencentLocationManager.getInstance(this);
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        startLocation();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {//如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(MainActivity.this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
               // android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);//否则退出程序
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
