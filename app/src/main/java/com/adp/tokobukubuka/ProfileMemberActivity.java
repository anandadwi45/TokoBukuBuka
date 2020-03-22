package com.adp.tokobukubuka;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Map;

public class ProfileMemberActivity extends AppCompatActivity {

    private EditText fullname, username, address, number_phone, email;

    String id_member, url_update;
    SharedPreferences sharedpreferences;
    Button edit, update;
    int success;

    private static final String TAG = ProfileMemberActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_member);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id_member = sharedpreferences.getString("id_member", "null");
        String url = Server.URL+"getdataprofile.php?id_member="+id_member;
        url_update = Server.URL+"updatedataprofile.php?id_member="+id_member;

        username = (EditText) findViewById(R.id.txt_username);
        fullname = (EditText) findViewById(R.id.txt_fullname);
        email = (EditText) findViewById(R.id.txt_email);
        number_phone = (EditText) findViewById(R.id.txt_phonenumber);
        address = (EditText) findViewById(R.id.txt_address);

        requestQueue = Volley.newRequestQueue(ProfileMemberActivity.this);

        list_data = new ArrayList<HashMap<String, String>>();

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("user");
                    for (int a = 0; a < jsonArray.length(); a ++){
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map  = new HashMap<String, String>();
                        map.put("id_member", json.getString("id_member"));
                        map.put("username", json.getString("username"));
                        map.put("fullname", json.getString("fullname"));
                        map.put("email", json.getString("email"));
                        map.put("number_phone", json.getString("number_phone"));
                        map.put("address", json.getString("address"));
                        list_data.add(map);
                    }
//                    Glide.with(getApplicationContext())
//                            .load("http://krisnabayu19.000webhostapp.com/" + list_data.get(0).get("img"))
//                            .crossFade()
//                            .placeholder(R.mipmap.ic_launcher)
//                            .into(imghp);
                    fullname.setText(list_data.get(0).get("fullname"));
                    username.setText(list_data.get(0).get("username"));
                    email.setText(list_data.get(0).get("email"));
                    number_phone.setText(list_data.get(0).get("number_phone"));
                    address.setText(list_data.get(0).get("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileMemberActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

        edit = findViewById(R.id.edit);
        update = findViewById(R.id.update);
        // fungsi floating action button memanggil form biodata
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname.setEnabled(true);
                email.setEnabled(true);
                number_phone.setEnabled(true);
                address.setEnabled(true);

                edit.setVisibility(View.GONE);
                update.setVisibility(View.VISIBLE);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname.setEnabled(true);
                email.setEnabled(true);
                number_phone.setEnabled(true);
                address.setEnabled(true);

                String fn = fullname.getText().toString();
                String em = email.getText().toString();
                String np = number_phone.getText().toString();
                String ad = address.getText().toString();

                checkUpdate(id_member, fn, em, np, ad);

                fullname.setEnabled(false);
                email.setEnabled(false);
                number_phone.setEnabled(false);
                address.setEnabled(false);

                edit.setVisibility(View.VISIBLE);
                update.setVisibility(View.GONE);
            }
        });
    }

    private void checkUpdate(final String id_member, final String fn, final String em, final String np, final String ad){

        StringRequest strReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Successfully Register!", jObj.toString());

//                        Toast.makeText(getActivity().getApplicationContext(),
//                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();


                    } else {
//                        Toast.makeText(getActivity().getApplicationContext(),
//                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
//                Toast.makeText(getActivity().getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_member", id_member);
                params.put("fullname", fn);
                params.put("email", em);
                params.put("number_phone", np);
                params.put("address", ad);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }
}
