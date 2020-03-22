package com.adp.tokobukubuka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Map;

public class DetailBukuActivity extends AppCompatActivity {

    Toolbar mToolbar;

    SharedPreferences sharedPreferences;

    public static final String TAG_ID = "id_member";
    public static final String TAG_USERNAME = "username";
    private ImageView img;
    private TextView nm, kuot, desc, tmpt, tgl, hrg, sin, jud, pen, sto, ter, gen;
    String nama_buku, nama_penulis, stock, price, genre, sinopsis, tempat, gambar, terjual;
    String id_member, username;
    String url_detail;
    String DetailAPI;
    int IOConnect = 0;
    private String id_buku;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_buku);

//        mToolbar = findViewById(R.id.tugas_counter_toolbar);
//        //setting toolbar
//        if (getActionBar() != null){
//            setSupportActionBar(mToolbar);
//        }
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id_member = sharedPreferences.getString("id_member", "null");

        username = getIntent().getStringExtra(TAG_USERNAME);

        Intent iGet = getIntent();
        id_buku = iGet.getStringExtra("id_buku");


        Button btnBeliTiket = (Button) findViewById(R.id.belitiket);
        btnBeliTiket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                simpanData(id_member, id_buku);
            }
        });
        DetailAPI = Server.URL + "/getdetailbuku.php?id_buku=" + id_buku;

        img = (ImageView)findViewById(R.id.imageView);
        sin = (TextView)findViewById(R.id.sinopsis);
        jud = (TextView)findViewById(R.id.judul);
        pen = (TextView) findViewById(R.id.penulis);
        hrg = (TextView) findViewById(R.id.harga);
        sto = (TextView) findViewById(R.id.stok);
        gen = (TextView) findViewById(R.id.genre);
        ter = (TextView) findViewById(R.id.terjual);

        requestQueue = Volley.newRequestQueue(DetailBukuActivity.this);


        new getDataTask().execute();

    }

    private void simpanData(final String id_member, final String id_buku) {
        String url_simpan = Server.URL+"addcart.php";

        String tag_json = "tag_json";

//        pd.setCancelable(false);
//        pd.setMessage("Menyimpan...");
//        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_simpan, new Response.Listener<String>() {
            @Override
            public void onResponse(String respon) {
                Log.d("respon", respon.toString());
//                hideDialog();

                try {
                    JSONObject jObject = new JSONObject(respon);
                    String pesan = jObject.getString("pesan");
                    String hasil = jObject.getString("result");
                    if (hasil.equalsIgnoreCase("true")) {
                        Toast.makeText(DetailBukuActivity.this, pesan, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailBukuActivity.this, AddCartActivity.class));
                        finish();
                    } else {
                        Toast.makeText(DetailBukuActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailBukuActivity.this, "Error JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
                Toast.makeText(DetailBukuActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("id_member", id_member);
                param.put("id_buku", id_buku);
                return param;
            }
        };

        AppController.getAppController().addToRequestQueue(stringRequest, tag_json);
    }

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
                ter.setText(price);
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
                terjual = detail.getString("price");
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

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }
}
