package com.adp.tokobukubuka;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminTambahBukuActivity extends AppCompatActivity {
    EditText judul, penulis, sinop, genr, stok, harga;
    Spinner spin_penulis;
    Button submit, viewList;
    String nm, kuot, desc, tmpt, tgl, hrg, sin, jud, pen, sto, ter, gen;

    AppDatabase mDb;
    ImageView ivCamera, imgView;
    private int GALLERY = 1, CAMERA = 2;
    Bitmap bitmap, decoded;
    private String UPLOAD_URL = Server.URL+"createbuku.php";
    int success;
    private static final String TAG = AdminTambahBukuActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_JUDUL = "nama_buku";
    private String KEY_GENRE = "genre";
    private String KEY_SINOPSIS = "sinopsis";
    private String KEY_STOK = "stock";
    private String KEY_PRICE = "price";
    private String KEY_GAMBAR = "gambar";
    private String KEY_IDPEN = "id_penulis";
    private String nama_buku, genre, sinopsis, stock, price, gambar;
    String tag_json_obj = "json_obj_req";

    public static final String url = Server.URL+"penulis.php";

    public static final String TAG_IDPEN = "id_penulis";
    public static final String TAG_PENULIS = "nama_penulis";

    TextView txt_hasil;
    ProgressDialog pDialog;
    AdapterListPenulis adapter;
    List<Category> listPenulis = new ArrayList<Category>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tambah_buku);

        mDb = AppDatabase.getDatabase(getApplicationContext());
        judul = findViewById(R.id.txt_judul);
        penulis = findViewById(R.id.txt_penulis);
        sinop = findViewById(R.id.txt_sinopsis);
        genr = findViewById(R.id.txt_genre);
        stok= findViewById(R.id.txt_stok);
        harga= findViewById(R.id.txt_harga);
        ivCamera = findViewById(R.id.ivCamera);
        imgView = findViewById(R.id.imgPreview);
        submit = findViewById(R.id.submit);

        spin_penulis = (Spinner) findViewById(R.id.cmb_penulis);

        spin_penulis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                penulis.setText("" + listPenulis.get(position).getId_penulis());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        adapter = new AdapterListPenulis(AdminTambahBukuActivity.this, listPenulis);
        spin_penulis.setAdapter(adapter);

        callData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jud = judul.getText().toString();
                sin = sinop.getText().toString();
                gen = genr.getText().toString();
                sto = stok.getText().toString();
                hrg = harga.getText().toString();
                pen = penulis.getText().toString();

                final DataBuku data = new DataBuku(
                        jud, gen, sin, Integer.parseInt(sto), Integer.parseInt(hrg), Integer.parseInt(pen)
                );
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDb.dataBukuDao().insertBuku(data);
                            }
                        });
                    }
                });
                uploadCRUD();
                startActivity(new Intent(AdminTambahBukuActivity.this, AdminBukuActivity.class));
            }
        });
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

    }

    private void callData() {
        listPenulis.clear();

        pDialog = new ProgressDialog(AdminTambahBukuActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        showDialog();

        // Creating volley request obj
        JsonArrayRequest jArr = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                Category item = new Category();

                                item.setId_penulis(obj.getInt(TAG_IDPEN));
                                item.setNama_penulis(obj.getString(TAG_PENULIS));

                                listPenulis.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();

                        hideDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(AdminTambahBukuActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Image");
        String[] pictureDialogItems = {
                "From gallery",
                "From camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        imgView.setImageBitmap(decoded);
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == CAMERA && resultCode == RESULT_OK){
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
            imgView.setImageBitmap(decoded);
            //Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            //setToImageView(getResizedBitmap(bitmap,1080));
            // imgView.setImageURI(image_uri);
        }
    }
    private void uploadCRUD() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                Log.e("v Add", jObj.toString());
                                Toast.makeText(AdminTambahBukuActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                finish();
                                Intent intent = new Intent(AdminTambahBukuActivity.this, AdminBukuActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AdminTambahBukuActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();
                        //menampilkan toast
                        Toast.makeText(AdminTambahBukuActivity.this,"Please Complete Form & Image".toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();
                //menambah parameter yang di kirim ke web servis
                params.put(KEY_JUDUL, judul.getText().toString().trim());
//                params.put(penulis, penulis.getText().toString().trim());
                params.put(KEY_SINOPSIS, sinop.getText().toString().trim());
                params.put(KEY_GENRE, genr.getText().toString().trim());
                params.put(KEY_STOK, stok.getText().toString().trim());
                params.put(KEY_PRICE, harga.getText().toString().trim());
                params.put(KEY_IDPEN, penulis.getText().toString().trim());
                params.put(KEY_GAMBAR, getStringImage(decoded));
                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
