package com.adp.tokobukubuka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminListBukuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminListBukuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView ListBuku;
    ProgressBar prgLoading;
    SwipeRefreshLayout swipeRefreshLayout = null;
    EditText edtKeyword;
    ImageButton btnSearch;
    TextView txtAlert;
    AdapterListBukuHomeAdmin cla;
    DatabukuAdapter mAdapter;
    ConnectivityManager conMgr;
    SharedPreferences sharedpreferences;
    AppDatabase mDb;

    public static ArrayList<String> id_buku = new ArrayList<String>();
    public static ArrayList<String> nama_buku = new ArrayList<String>();
    public static ArrayList<String> price = new ArrayList<String>();
    public static ArrayList<String> genre = new ArrayList<String>();
    public static ArrayList<String> gambar = new ArrayList<String>();
    ArrayList<HashMap<String, String>> list_buku;
    String ListAPI;
    int IOConnect = 0;

    public AdminListBukuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminListBukuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminListBukuFragment newInstance(String param1, String param2) {
        AdminListBukuFragment fragment = new AdminListBukuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_list_buku, null);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

//        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        ConnectivityManager cm;
        mDb = AppDatabase.getDatabase(getActivity().getApplicationContext());
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        prgLoading = view.findViewById(R.id.prgLoading);
        ListBuku = view.findViewById(R.id.ListBuku);
        edtKeyword = view.findViewById(R.id.edtKeyword);
        btnSearch = view.findViewById(R.id.btnSearch);
        txtAlert = view.findViewById(R.id.txtAlert);
        cla = new AdapterListBukuHomeAdmin(this.getActivity());
        mAdapter = new DatabukuAdapter(this.getActivity());
        ListAPI = Server.URL+"getlistbukuadmin.php";
        new getDataTask().execute();


        ListBuku.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                // go to menu detail page
                Intent iDetail = new Intent(getActivity(), AdminDetailBukuActivity.class);
                iDetail.putExtra("id_buku", id_buku.get(position));
                startActivity(iDetail);
            }
        });

        ListBuku.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (ListBuku != null && ListBuku.getChildCount() > 0) {
                    boolean firstItemVisible = ListBuku.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = ListBuku.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        IOConnect = 0;
                        ListBuku.invalidateViews();
                        clearData();
                        new getDataTask().execute();
                    }
                }, 3000);
            }
        });


        return view;
    }

    void clearData(){
        id_buku.clear();
        nama_buku.clear();
        price.clear();
        genre.clear();
        gambar.clear();
    }
    // asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {
        // show progressbar first
        getDataTask(){
            conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            {
                if (conMgr.getActiveNetworkInfo()!= null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    if(!prgLoading.isShown()){
                        prgLoading.setVisibility(View.VISIBLE);
                        txtAlert.setVisibility(View.GONE);
                    }

                } else {

                }
            }

        }
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
            prgLoading.setVisibility(View.GONE);
            // if internet connection and data available show data on list
            // otherwise, show alert text
            if((id_buku.size() > 0) && (IOConnect == 0)){
                ListBuku.setVisibility(View.VISIBLE);
                ListBuku.setAdapter(mAdapter);
                ListBuku.setAdapter(cla);
            }else{
                ListBuku.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
                ListBuku.setAdapter(mAdapter);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<DataBuku> data = mDb.dataBukuDao().loadAllBuku();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setTasks(data);
                            }
                        });
                    }
                });
//                txtAlert.setVisibility(View.VISIBLE);


            }
        }
    }



    public void parseJSONData(){
        clearData();


//        list_topevent = new ArrayList<>();

        try {
            // request data from Category API

            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
            HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
            HttpUriRequest request = new HttpGet(ListAPI);
            HttpResponse response = client.execute(request);
            InputStream atomInputStream = response.getEntity().getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));
            String line;
            String str = "";
            while ((line = in.readLine()) != null){
                str += line;
            }
            // parse json data and store into arraylist variables
            JSONObject json = new JSONObject(str);
            JSONArray data = json.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                JSONObject buku = object.getJSONObject("buku");


                id_buku.add(buku.getString("id_buku"));
                nama_buku.add(buku.getString("nama_buku"));
//                nama_penulis.add(buku.getString("nama_penulis"));
                price.add(buku.getString("price"));
                genre.add(buku.getString("genre"));
                gambar.add(buku.getString("gambar"));

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
