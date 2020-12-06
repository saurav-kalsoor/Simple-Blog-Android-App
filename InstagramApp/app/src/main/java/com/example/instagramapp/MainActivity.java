package com.example.instagramapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagramapp.Fragments.HomeFragment;
import com.example.instagramapp.Fragments.NotificationFragment;
import com.example.instagramapp.Fragments.ProfileFragment;
import com.example.instagramapp.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNav;
    private Fragment mSelectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNav = (BottomNavigationView)findViewById(R.id.bottomNavigation);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        mSelectorFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        mSelectorFragment = new SearchFragment();
                        break;
                    case R.id.nav_add:
                        mSelectorFragment = null;
                        startActivity(new Intent(MainActivity.this,PostActivity.class));
                        break;
                    case R.id.nav_heart:
                        mSelectorFragment = new NotificationFragment();
                        break;
                    case R.id.nav_profile:
                        mSelectorFragment = new ProfileFragment();
                        break;
                }
                if(mSelectorFragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,mSelectorFragment).commit();
                }
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomeFragment()).commit();
    }
}
