package com.adp.tokobukubuka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText txtUsername, txtPassword, txtNama, txtEmail, txtAlamat, txtTelp;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUsername = (EditText) findViewById(R.id.username);
        txtPassword = (EditText) findViewById(R.id.password);
        txtNama = (EditText) findViewById(R.id.nama);
        txtAlamat = (EditText) findViewById(R.id.alamat);
        txtEmail = (EditText) findViewById(R.id.email);
        txtTelp = (EditText) findViewById(R.id.telp);

        pd = new ProgressDialog(this);

        TextView textSignIn = (TextView) findViewById(R.id.textSignUp);
        textSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnSignUp = (Button) findViewById(R.id.buttonSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String fullname = txtNama.getText().toString().trim();
                String address = txtAlamat.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String number_phone = txtTelp.getText().toString().trim();
                if (!username.isEmpty() && !password.isEmpty()) {
                    simpanData(username, password, fullname, address, email, number_phone);
                } else if (username.isEmpty()) {
                    txtUsername.setError("username tidak boleh kosong");
                    txtUsername.requestFocus();
                } else if (password.isEmpty()) {
                    txtPassword.setError("password tidak boleh kosong");
                    txtPassword.requestFocus();
                }

            }
        });

    }
    private void simpanData(final String username, final String password, final String fullname, final String address, final String email, final String number_phone) {
        String url_simpan = Server.URL+"createuser.php";

        String tag_json = "tag_json";

        pd.setCancelable(false);
        pd.setMessage("Menyimpan...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_simpan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response.toString());
                hideDialog();

                try {
                    JSONObject jObject = new JSONObject(response);
                    String pesan = jObject.getString("pesan");
                    String hasil = jObject.getString("result");
                    if (hasil.equalsIgnoreCase("true")) {
                        Toast.makeText(RegisterActivity.this, pesan, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Error JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username", username);
                param.put("password", password);
                param.put("fullname", fullname);
                param.put("address", address);
                param.put("email", email);
                param.put("number_phone", number_phone);
                return param;
            }
        };

        AppController.getAppController().addToRequestQueue(stringRequest, tag_json);
    }
    private void showDialog() {
        if (!pd.isShowing())
            pd.show();
    }

    private void hideDialog() {
        if (pd.isShowing())
            pd.dismiss();
    }
}
