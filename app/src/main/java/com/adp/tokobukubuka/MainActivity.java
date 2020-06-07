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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static MainActivity mainActivity;
    public static Boolean isVisible = false;
    private static final String TAG = "MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
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

        mainActivity = this;
        registerWithNotificationHubs();
        FirebaseService.createChannelAndHandleNotifications(getApplicationContext());

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
        toggleVisibility(navigationView.getMenu(), R.id.nav_wishlist, visible);
        toggleVisibility(navigationView.getMenu(), R.id.nav_historym, visible);
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
                    selectedFragment = new ListBukuFragment();
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
        } else if (id == R.id.nav_wishlist) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// mengganti isi container dengan fragment baru
            ft.replace(R.id.FrameFragment, new WishlistFragment());
            ft.commit();
        } else if (id == R.id.nav_historym) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// mengganti isi container dengan fragment baru
            ft.replace(R.id.FrameFragment, new HistoryFragment());
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
    public void registerWithNotificationHubs()
    {
        if (checkPlayServices()) {
            // Start IntentService to register this application with FCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported by Google Play Services.");
                ToastNotify("This device is not supported by Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
    }

    public void ToastNotify(final String notificationMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, notificationMessage, Toast.LENGTH_LONG).show();
                TextView helloText = (TextView) findViewById(R.id.text_hello);
                helloText.setText(notificationMessage);
            }
        });
    }
}
