package com.example.mystore.Admin.containers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mystore.Admin.Fragments.AdminHomeFragment;
import com.example.mystore.Client.Fragments.CartFragment;
import com.example.mystore.Client.Fragments.FavoritesFragment;
import com.example.mystore.Client.Fragments.HomeFragment;
import com.example.mystore.Client.Fragments.ProfileFragment;
import com.example.mystore.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);

        BottomNavigationView buttomNavBar = findViewById(R.id.admin_nav_view);
        buttomNavBar.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_frame_layout,
                    new HomeFragment()).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.admin_nav_home:
                            selectedFragment = new AdminHomeFragment();
                            break;
//                        case R.id.admin_nav_unchecked_orders:
//                            selectedFragment = new FavoritesFragment();
//                            break;
//                        case R.id.admin_nav_checked_orders:
//                            selectedFragment = new CartFragment();
//                            break;
//                        case R.id.admin_nav_profil:
//                            selectedFragment = new ProfileFragment();
//                            break;
                    }
                    assert selectedFragment != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.admin_frame_layout,
                            selectedFragment).commit();
                    return true;
                }
            };
}
