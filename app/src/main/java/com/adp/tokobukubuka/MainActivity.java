package com.adp.tokobukubuka;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedpreferences;
    String id_member, username;
    public static final String TAG_ID = "id_member";
    public static final String TAG_USERNAME = "username";
    public static final String session_status = "session_status";
    public static final String isadmin_status = "isadmin_status";
    Boolean session = false;
    Boolean isadmin = false;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_member = sharedpreferences.getString("id_member", "null");
        username = getIntent().getStringExtra(TAG_USERNAME);
        isadmin = sharedpreferences.getBoolean(isadmin_status, false);

        check();
        check2();

//        if (session = true){
//            hideItem();
//        }else {
//            showItem();
//        }

        // Memulai transaksi
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// mengganti isi container dengan fragment baru
        ft.replace(R.id.FrameFragment, new HomeFragment());
        ft.commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.FrameFragment, new HomeFragment()).commit();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new HomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_home);
//        }
    }
    private boolean check() {
        if(session){
            setItemVisible(true);
            setItemVisible2(false);
        }else if (!session){
            setItemVisible(false);
            setItemVisible2(true);
        }
        return false;
    }
    private boolean check2() {
        if(isadmin){
            Intent intent = new Intent(MainActivity.this, AdminBukuActivity.class);
            intent.putExtra(isadmin_status, isadmin);
            intent.putExtra(TAG_ID, id_member);
            intent.putExtra(TAG_USERNAME, username);
            finish();
            startActivity(intent);
        }else if (!isadmin){

        }
        return false;
    }
    public void setItemVisible(boolean visible){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        toggleVisibility(navigationView.getMenu(), R.id.nav_profile, visible);
        toggleVisibility(navigationView.getMenu(), R.id.nav_logout, visible);
        toggleVisibility(navigationView.getMenu(), R.id.nav_list, visible);
    }
    public void setItemVisible2(boolean visible){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        toggleVisibility2(navigationView.getMenu(), R.id.nav_login, visible);
        toggleVisibility2(navigationView.getMenu(), R.id.nav_register, visible);
    }

    private void toggleVisibility(Menu menu, @IdRes int id, boolean visible){
        menu.findItem(id).setVisible(visible);
    }
    private void toggleVisibility2(Menu menu, @IdRes int id, boolean visible){
        menu.findItem(id).setVisible(visible);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()){
                case R.id.action_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.action_book:
//                    selectedFragment = new ScanFragment();
                    break;
                case R.id.action_list:
                    selectedFragment = new PurchasedFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.FrameFragment, selectedFragment).commit();

            return true;
        }
    };

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileMemberActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_register) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_list) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// mengganti isi container dengan fragment baru
            ft.replace(R.id.FrameFragment, new CartFragment());
            ft.commit();
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(LoginActivity.session_status, false);
            editor.putString(TAG_ID, null);
            editor.putString(TAG_USERNAME, null);
            editor.commit();

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}
