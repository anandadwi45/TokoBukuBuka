package com.adp.tokobukubuka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    SharedPreferences sharedpreferences;

    private RecyclerView crt;


    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    TextView txt_id, txt_username;
    String id_member, username;

    public static final String TAG_ID = "id_member";
    public static final String TAG_USERNAME = "username";

    ArrayList<HashMap<String, String>> list_cart;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        View view = inflater.inflate(R.layout.fragment_cart, null);

        sharedpreferences = getContext().getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        id_member = sharedpreferences.getString("id_member", "null");
        username = getActivity().getIntent().getStringExtra(TAG_USERNAME);

        String url = Server.URL+"getdatacart.php?id_member="+id_member;

        crt = (RecyclerView) view.findViewById(R.id.rv_list_cart);
        crt.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        CardView cdTop = (CardView) view.findViewById(R.id.cdTop);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        list_cart = new ArrayList<>();

        Button btnCheckout = (Button) view.findViewById(R.id.checkout);
        btnCheckout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(BeliTiketActivity.this, CheckoutActivity.class);
//                startActivity(intent);
                checkoutBuku(id_member);
            }
        });


        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("crt");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id_buku", json.getString("id_buku"));
                        map.put("nama_buku", json.getString("nama_buku"));
                        map.put("price", json.getString("price"));
                        list_cart.add(map);
                        AdapterListCart adapter = new AdapterListCart(CartFragment.this, list_cart);
                        crt.setAdapter(adapter);

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void checkoutBuku(final String id_member) {
        String url_simpan = "https://tokobukubuka.000webhostapp.com/checkout.php";

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
                        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), AddCartActivity.class));
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("id_member", id_member);
                return param;
            }
        };

        AppController.getAppController().addToRequestQueue(stringRequest, tag_json);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
