package com.adp.tokobukubuka;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DatabukuAdapter extends BaseAdapter {
    Context context;
    List<DataBuku> mBukuList;
    public static String ADMIN_PANEL_URL = "http://krisnabayu19.000webhostapp.com/";
    private Activity activity;
    public DatabukuAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setTasks(List<DataBuku> bukuList) {
        this.mBukuList = bukuList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mBukuList == null) {
            return 0;
        }
        return mBukuList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list_topbuku, null);
            holder = new ViewHolder();
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Log.e("NAMA", mBukuList.get(0).getNama_buku());
        holder.mNamaBuku = view.findViewById(R.id.txtName);
        holder.mPenulis = view.findViewById(R.id.txtPenulis);
        holder.mHarga = view.findViewById(R.id.txtHarga);
        holder.mGenre= view.findViewById(R.id.txtGenre);
//        holder.imgThumb = (ImageView) view.findViewById(R.id.imgThumb);
        holder.mNamaBuku.setText(mBukuList.get(i).getNama_buku());
//        holder.mPenulis.setText(mBukuList.get(i).getId_penulis());
        holder.mHarga.setText(String.valueOf(mBukuList.get(i).getPrice()));
        holder.mGenre.setText(mBukuList.get(i).getGenre());
//        Picasso.with(activity).load(ADMIN_PANEL_URL+"/"+ListEventSeminar.photo.get(i)).placeholder(R.drawable.udayanaprofil).into(holder.imgThumb);
        return view;
    }

    static class ViewHolder {
        TextView mNamaBuku, mPenulis, mHarga, mGenre, mPhoto;
//        ImageView imgThumb;
    }
}
