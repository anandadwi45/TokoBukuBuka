package com.adp.tokobukubuka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminBukuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    SharedPreferences sharedpreferences;

    private RecyclerView top;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    TextView txt_id, txt_username;
    String id_member, username;

    public static final String TAG_ID = "id_member";
    public static final String TAG_USERNAME = "username";
    public static final String session_status = "session_status";
    public static final String isadmin_status = "isadmin_status";
    Boolean session = false;
    Boolean isadmin = false;

    ArrayList<HashMap<String, String>> list_topbuku;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_buku);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// mengganti isi container dengan fragment baru
        ft.replace(R.id.FrameFragment, new AdminHomeFragment());
        ft.commit();

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_member = sharedpreferences.getString("id_member", "null");
        username = getIntent().getStringExtra(TAG_USERNAME);
        isadmin = sharedpreferences.getBoolean(isadmin_status, false);

        navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(AdminBukuActivity.this, AdminBukuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
//            Intent intent = new Intent(AdminBukuActivity.this, ProfileMemberActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_list) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//// mengganti isi container dengan fragment baru
//            ft.replace(R.id.FrameFragment, new CartFragment());
//            ft.commit();
        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(LoginAdminActivity.session_status, false);
            editor.putString(TAG_ID, null);
            editor.putString(TAG_USERNAME, null);
            editor.putBoolean(LoginAdminActivity.isadmin_status, false);
            editor.apply();


            Intent intent = new Intent(AdminBukuActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
