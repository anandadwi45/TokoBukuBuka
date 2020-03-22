package com.adp.tokobukubuka;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDetailBukuActivity extends AppCompatActivity {
    Toolbar mToolbar;

    SharedPreferences sharedPreferences;

    int success;

    private static final String TAG = AdminDetailBukuActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    public static final String TAG_ID = "id_member";
    public static final String TAG_USERNAME = "username";

    public DrawerLayout drawerLayout;
    public ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView img;
    private TextView nm, kuot, desc, tmpt, tgl, hrg, sin, jud, pen, sto, ter, gen;
    Button btn_edit, btn_update;
    String nama_buku, nama_penulis, stock, price, genre, sinopsis, tempat, gambar, terjual;
    String id_member, username;
    String url_detail, url_update;
    String DetailAPI;
    Spinner cmb_pen;
    ProgressDialog pDialog;
    int IOConnect = 0;
    private ArrayList<Category> categoriesList;
    private String id_buku;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private String URL_CATEGORIES = Server.URL+"getpenulis.php";

    ArrayList<HashMap<String, String>> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_buku);

//        sharedPreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
//
//        id_member = sharedPreferences.getString("id_member", "null");
//
//        username = getIntent().getStringExtra(TAG_USERNAME);



        Intent iGet = getIntent();
        id_buku = iGet.getStringExtra("id_buku");
        url_update = Server.URL+"updatedatabuku.php?id_buku="+id_buku;
        img = (ImageView)findViewById(R.id.imageView);
        sin = (TextView)findViewById(R.id.txt_sinopsis);
        jud = (TextView)findViewById(R.id.txt_judul);
        pen = (TextView) findViewById(R.id.txt_penulis);
        hrg = (TextView) findViewById(R.id.txt_harga);
        sto = (TextView) findViewById(R.id.txt_stok);
        gen = (TextView) findViewById(R.id.txt_genre);
        btn_edit = (Button) findViewById(R.id.edit);
        btn_update = (Button) findViewById(R.id.update);
        cmb_pen = (Spinner) findViewById(R.id.cmb_penulis);


        Button editBuku = (Button) findViewById(R.id.edit);
        editBuku.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sin.setEnabled(true);
                hrg.setEnabled(true);
                sto.setEnabled(true);
                gen.setEnabled(true);
                btn_edit.setVisibility(View.GONE);
                btn_update.setVisibility(View.VISIBLE);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sin.setEnabled(false);
                hrg.setEnabled(false);
                sto.setEnabled(false);
                gen.setEnabled(false);

                String sinop = sin.getText().toString();
                String harg = hrg.getText().toString();
                String stok = sto.getText().toString();
                String genr = gen.getText().toString();

                checkUpdate(id_buku, sinop, harg, stok, genr);
                btn_edit.setVisibility(View.VISIBLE);
                btn_update.setVisibility(View.GONE);
            }
        });
        DetailAPI = Server.URL + "getdetailbuku.php?id_buku=" + id_buku;



        requestQueue = Volley.newRequestQueue(AdminDetailBukuActivity.this);


        new getDataTask().execute();
    }
    private void checkUpdate(final String id_buku, final String sinop, final String harg, final String stok, final String genr){

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
                params.put("id_buku", id_buku);
                params.put("sinopsis", sinop);
                params.put("genre", genr);
                params.put("stock", stok);
                params.put("price", harg);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }



//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            //populateSpinner();
//        }



    /**
     * Adding spinner data
     * */

    public class getDataTask extends AsyncTask<Void, Void, Void> {
        // show progressbar first
        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            // parse json data from server in background
            parseJSONData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // when finish parsing, hide progressbar
            // if internet connection and data available show data
            // otherwise, show alert text
            if (IOConnect == 0) {
                Picasso.with(getApplicationContext()).load(Server.URL + gambar).placeholder(R.drawable.logo).into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                            }
                        });
                    }

                    @Override
                    public void onError() {
                    }
                });
                sin.setText(sinopsis);
                jud.setText(nama_buku);
                pen.setText(nama_penulis);
                sto.setText(stock);
                hrg.setText(price);
                gen.setText(genre);


            }
        }
    }

    public void parseJSONData() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
            HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
            HttpUriRequest request = new HttpGet(DetailAPI);
            HttpResponse response = client.execute(request);
            InputStream atomInputStream = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));
            String line;
            String str = "";
            while ((line = in.readLine()) != null) {
                str += line;
            }
            JSONObject json = new JSONObject(str);
            JSONArray data = json.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                JSONObject detail = object.getJSONObject("detail");
                gambar = detail.getString("gambar");
                nama_buku = detail.getString("nama_buku");
                genre = detail.getString("genre");
                sinopsis = detail.getString("sinopsis");
                stock = detail.getString("stock");
                price = detail.getString("price");
                nama_penulis = detail.getString("nama_penulis");
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            IOConnect = 1;
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
