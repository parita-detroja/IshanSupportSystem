package com.digidot.ishansupportsystem.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.fragment.CreateTicketFragment;
import com.digidot.ishansupportsystem.fragment.HomeFragment;
import com.digidot.ishansupportsystem.fragment.NotificationFragment;
import com.digidot.ishansupportsystem.fragment.SettingFragment;
import com.digidot.ishansupportsystem.util.Constant;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    private final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    private void initView(){
        mNavigationView =findViewById(R.id.navigation_view);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
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
                        Constant.FRAGMNET_HOME=item.getTitle();
                        loadFragment(new HomeFragment(),item.getTitle());
                        break;
                    case R.id.ticket:
                        Constant.FRAGMNET_TICKET=item.getTitle();
                        loadFragment(new CreateTicketFragment(),item.getTitle());
                        break;
                    case R.id.report:
                        Constant.FRAGMENT_REPORTS =item.getTitle();
                        //loadFragment(new HomeFragment(),item.getTitle());
                        break;
                    case R.id.notifications:
                        Constant.FRAGMNET_NOTIFICATIONS=item.getTitle();
                        loadFragment(new NotificationFragment(),item.getTitle());
                        break;
                    case R.id.settings:
                        Constant.FRAGMNET_SETTINGS=item.getTitle();
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
        if(fragment instanceof HomeFragment){
            fragmentTransaction.addToBackStack("");
        }
        fragmentTransaction.commit();
    }
}
