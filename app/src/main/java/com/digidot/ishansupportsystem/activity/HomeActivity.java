package com.digidot.ishansupportsystem.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.fragment.HomeFragment;
import com.digidot.ishansupportsystem.fragment.NotificationFragment;
import com.digidot.ishansupportsystem.fragment.SettingFragment;
import com.digidot.ishansupportsystem.fragment.TicketListFragment;
import com.digidot.ishansupportsystem.fragment.ViewTicketFragment;
import com.digidot.ishansupportsystem.service.NotificationService;
import com.digidot.ishansupportsystem.util.Constant;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    private final String TAG = "HomeActivity";
    private ProgressBar mProgressBar;
    private SharedPreferences pref;
    int count =0;

    private Location mLocation;

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref=getApplicationContext().getSharedPreferences("IffcoPref",0);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        } else{
            getCurrentLocationFromLocationManager();
        }
        initView();
    }

    private void initView(){
        Intent intent=new Intent(HomeActivity.this, NotificationService.class);
        startService(intent);
        mNavigationView =findViewById(R.id.navigation_view);
        mProgressBar = findViewById(R.id.progress_bar);
        toolbar = findViewById(R.id.tool_bar);
        mDrawerLayout=findViewById(R.id.drawer);
        loadFragment(new HomeFragment(),this.getResources().getString(
                R.string.drawer_menu_home));
        setListener();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,mDrawerLayout,toolbar,
                R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void setListener(){
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                int id = item.getItemId();
                switch (id){
                    case R.id.home:
                        Constant.FRAGMNET_HOME=item.getTitle().toString();
                        loadFragment(new HomeFragment(),item.getTitle());
                        break;
                    case R.id.ticket:
                        Constant.FRAGMNET_TICKET_LIST=item.getTitle().toString();
                        TicketListFragment mTicketListFragment = new TicketListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.TICKET_STATUS, Constant.TICKET_STATUS_OPEN);
                        mTicketListFragment.setArguments(bundle);
                        loadFragment(mTicketListFragment,item.getTitle());
                        break;
                    case R.id.notifications:
                        Constant.FRAGMNET_NOTIFICATIONS=item.getTitle().toString();
                        loadFragment(new NotificationFragment(),item.getTitle());
                        break;
                    case R.id.settings:
                        Constant.FRAGMNET_SETTINGS=item.getTitle().toString();
                        loadFragment(new SettingFragment(),item.getTitle());
                        break;
                    default:
                        Log.e(TAG,"No match found");
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment,CharSequence title){
        toolbar.setTitle(title);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(Constant.CURRENT_LOADED_FRAGMENT.equals(Constant.FRAGMNET_HOME)){
            if(count==1){
                count=0;
                this.finish();
                pref.edit().putBoolean(Constant.PREF_KEY_LOGIN,false).commit();
            }else{
                Toast.makeText(this,"Touch again to exit",Toast.LENGTH_LONG).show();
                count++;
            }
        }else {
            super.onBackPressed();
        }
    }

    public void viewProgress(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void getCurrentLocationFromLocationManager() {
        try {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert mLocationManager != null;
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("Location","Fetched");
        mLocation = location;
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(HomeActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 0) {

            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    Log.e("denied", permission);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("allowed", permission);
                        getCurrentLocationFromLocationManager();
                    } else {
                        Log.e("set to never ask again", permission);
                    }
                }
            }
        }
    }

    private void checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();

        } else {
            getCurrentLocationFromLocationManager();
            Log.i("Location",
                    "CAMERA permission has already been granted. Displaying camera preview.");
        }
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA},
                0);
    }

    public Location getLocation(){
        if(mLocation != null){
            return mLocation;
        }
        Toast.makeText(getApplicationContext(),"Location not available",Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    protected void onDestroy() {
        pref.edit().putBoolean(Constant.PREF_KEY_LOGIN,false).commit();
        super.onDestroy();
     }

    @Override
    protected void onStop() {
        pref.edit().putBoolean(Constant.PREF_KEY_LOGIN,false).commit();
        super.onStop();
    }

    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }
}
