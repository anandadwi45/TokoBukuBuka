package com.adp.tokobukubuka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminBookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminBookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedPreferences sharedpreferences;

    private RecyclerView top;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    ProgressBar prgLoading;
    SwipeRefreshLayout swipeRefreshLayout = null;
    TextView txt_id, txt_username;
    String id_member, username;
    DatabukuAdapter mAdapter;
    ConnectivityManager conMgr;
    AppDatabase mDb;
    AdapterListBukuAdmin cla;

    String ListAPI;
    int IOConnect = 0;

    public static final String TAG_ID = "id_member";
    public static final String TAG_USERNAME = "username";

    ArrayList<HashMap<String, String>> list_buku;

    private OnFragmentInteractionListener mListener;

    public AdminBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminBookFragment newInstance(String param1, String param2) {
        AdminBookFragment fragment = new AdminBookFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_book, null);

//        sharedpreferences = getContext().getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
//
//        id_member = getActivity().getIntent().getStringExtra(TAG_ID);
//        username = getActivity().getIntent().getStringExtra(TAG_USERNAME);

        ConnectivityManager cm;
        mDb = AppDatabase.getDatabase(getContext().getApplicationContext());
        String url = Server.URL+"getdatatopbuku.php";

        top = (RecyclerView) view.findViewById(R.id.rv_list_top_buku);
        top.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        Button add_buku = (Button) view.findViewById(R.id.addBook);
        add_buku.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminTambahBukuActivity.class);
                startActivity(intent);
            }
        });



        CardView cdTop = (CardView) view.findViewById(R.id.cdTop);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        list_buku = new ArrayList<>();


        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("top");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id_buku", json.getString("id_buku"));
                        map.put("nama_buku", json.getString("nama_buku"));
                        map.put("nama_penulis", json.getString("nama_penulis"));
                        map.put("price", json.getString("price"));
                        map.put("genre", json.getString("genre"));
                        map.put("gambar", json.getString("gambar"));
                        list_buku.add(map);
                        AdapterListBukuAdmin adapter = new AdapterListBukuAdmin(AdminBookFragment.this, list_buku);
                        top.setAdapter(adapter);

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

        mAdapter = new DatabukuAdapter(getActivity());
        top.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), top, new ClickListener() {
            @Override
            public void onBukuClick(View view, int position) {
                Intent intent = new Intent(getActivity(), AdminDetailBukuActivity.class);
                intent.putExtra("id_buku", list_buku.get(position).get("id_buku"));
                startActivity(intent);
            }

            @Override
            public void onBukuLongClick(View view, int position) {
                Toast.makeText(getActivity(),"Kepencet Lama "+position,Toast.LENGTH_LONG).show();
            }
        }));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector mGestureDetector;
        private ClickListener mClickListener;


        public RecyclerTouchListener(final Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.mClickListener = clickListener;
            mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if (child!=null && clickListener!=null){
                        clickListener.onBukuLongClick(child,recyclerView.getChildAdapterPosition(child));
                    }
                    super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child!=null && mClickListener!=null && mGestureDetector.onTouchEvent(e)){
                mClickListener.onBukuClick(child,rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    public static interface ClickListener{
        public void onBukuClick(View view, int position);
        public void onBukuLongClick(View view, int position);
    }
}
