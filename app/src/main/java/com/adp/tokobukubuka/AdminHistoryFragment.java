package com.adp.tokobukubuka;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SharedPreferences sharedpreferences;

    private RecyclerView trx;


    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    TextView txt_id, txt_username;
    String id_member, username;

    public static final String TAG_ID = "id_member";
    public static final String TAG_USERNAME = "username";

    ArrayList<HashMap<String, String>> list_transaksi;

    public AdminHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHistoryFragment newInstance(String param1, String param2) {
        AdminHistoryFragment fragment = new AdminHistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_history, null);

//        sharedpreferences = getContext().getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
//
//        id_member = sharedpreferences.getString("id_member", "null");
//        username = getActivity().getIntent().getStringExtra(TAG_USERNAME);

        String url = Server.URL+"getdatahistory.php";

        trx = (RecyclerView) view.findViewById(R.id.rvHistory);
        trx.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        CardView cdTop = (CardView) view.findViewById(R.id.cdTop);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        list_transaksi = new ArrayList<>();


        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("trx");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id_transaksi", json.getString("id_transaksi"));
                        map.put("fullname", json.getString("fullname"));
                        map.put("total_harga", json.getString("total_harga"));
                        map.put("tanggal", json.getString("tanggal"));
                        map.put("status", json.getString("status"));
                        list_transaksi.add(map);
                        AdapterListTransaksi adapter = new AdapterListTransaksi(AdminHistoryFragment.this, list_transaksi);
                        trx.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(HomeFragment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

        return view;
    }
}
