package com.adp.tokobukubuka;

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

public class ConfirmActivity extends AppCompatActivity {

    Toolbar mToolbar;

    SharedPreferences sharedPreferences;

    public static final String TAG_ID = "id_member";
    public static final String TAG_USERNAME = "username";
    private ImageView img;
    private TextView pem, tot;
    String gambar,pembeli,total;
    String id_member, username;
    String url_confirm;
    String DetailAPI;
    int IOConnect = 0;
    private String id_transaksi;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    ArrayList<HashMap<String, String>> list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Intent iGet = getIntent();
        id_transaksi = iGet.getStringExtra("id_transaksi");

        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                confirmTransaksi(id_transaksi);
            }
        });

        DetailAPI = Server.URL + "getdetailtransaksi.php?id_transaksi=" + id_transaksi;

        img = (ImageView)findViewById(R.id.imageView);
        pem = (TextView) findViewById(R.id.pembeliConfirm);
        tot = (TextView) findViewById(R.id.totalConfirm);

        requestQueue = Volley.newRequestQueue(ConfirmActivity.this);


        new getDataTask().execute();
    }

    private void confirmTransaksi(final String id_transaksi) {
        String url_confirm = Server.URL+"confirmtransaksi.php";

        String tag_json = "tag_json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_confirm, new Response.Listener<String>() {
            @Override
            public void onResponse(String respon) {
                Log.d("respon", respon.toString());
//                hideDialog();

                try {
                    JSONObject jObject = new JSONObject(respon);
                    String pesan = jObject.getString("pesan");
                    String hasil = jObject.getString("result");
                    if (hasil.equalsIgnoreCase("true")) {
                        Toast.makeText(ConfirmActivity.this, pesan, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ConfirmActivity.this, AdminBukuActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ConfirmActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ConfirmActivity.this, "Error JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
                Toast.makeText(ConfirmActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("id_transaksi", id_transaksi);
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
                pem.setText(pembeli);
                tot.setText(total);


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
                gambar = detail.getString("bukti_transaksi");
                pembeli = detail.getString("fullname");
                total = detail.getString("total_harga");
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
