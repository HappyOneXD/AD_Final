package com.example.testing.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testing.Adapters.ViewPagerAdapter;
import com.example.testing.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    Intent intent;
    Bundle bundle;
    private String username = "";
    private int userId = 0;
    Menu menu;

    @Override
    protected void onStart() {
        super.onStart();
        // check login user
        if (userId <= 0 || TextUtils.isEmpty(username)) {
            Intent intentLogin = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intentLogin);
            finish();
        }
        activeMenu(intent, bundle);
    }

    private void activeMenu(Intent myIntent, Bundle myBundle){
        myIntent = getIntent();
        myBundle = myIntent.getExtras();
        if (myBundle != null){
            String tabMenuName = myBundle.getString("MENU_TAB","");
            if (tabMenuName.equals("BUDGET_TAB")){
                viewPager.setCurrentItem(1);
            } else if (tabMenuName.equals("EXPENSES_TAB")){
                viewPager.setCurrentItem(2);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.drawer_navigation);
        // get data from LoginActivity
        intent = getIntent();
        bundle = intent.getExtras();
        SharedPreferences spf = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        username = spf.getString("USER_USERNAME", "");
        userId = spf.getInt("USER_ID", 0);

        // xu ly hien thi Drawer navigation menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle); // dong - mo menu
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setupViewPager();

        menu = navigationView.getMenu();
        MenuItem itemLogout = menu.findItem(R.id.menu_logout);
        itemLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawer(GravityCompat.START); // close drawer menu
                // remove data from LoginActivity
                if (bundle != null) {
                    intent.removeExtra("ID_ACCOUNT");
                    intent.removeExtra("ACCOUNT_USER");
                    intent.removeExtra("EMAIL_USER");
                }
                Intent intentLogin = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
                return false;
            }
        });
        // show username after login
        MenuItem itemUsername = menu.findItem(R.id.menu_account);
        if (!TextUtils.isEmpty(username)) {
            itemUsername.setTitle(username);
        }
        // xu ly click vao cac tab bottom navigation de hien thi cac fragment tuong ung
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_home) {
                viewPager.setCurrentItem(0);
            } else if (menuItem.getItemId() == R.id.menu_budget) {
                viewPager.setCurrentItem(1);
            } else if (menuItem.getItemId() == R.id.menu_expenses) {
                viewPager.setCurrentItem(2);
            } else if (menuItem.getItemId() == R.id.menu_setting) {
                viewPager.setCurrentItem(3);
            } else {
                viewPager.setCurrentItem(0);
            }
            return true;
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                } else if (position == 1) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_budget).setChecked(true);
                } else if (position == 2) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_expenses).setChecked(true);
                } else if (position == 3) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_setting).setChecked(true);
                } else {
                    bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_home) {
            viewPager.setCurrentItem(0);
        } else if (menuItem.getItemId() == R.id.menu_budget) {
            viewPager.setCurrentItem(1);
        } else if (menuItem.getItemId() == R.id.menu_expenses) {
            viewPager.setCurrentItem(2);
        } else if (menuItem.getItemId() == R.id.menu_setting) {
            viewPager.setCurrentItem(3);
        }
        drawerLayout.closeDrawer(GravityCompat.START); // dong menu
        return true;
    }
}
