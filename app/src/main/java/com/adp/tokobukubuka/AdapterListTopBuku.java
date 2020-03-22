package com.adp.tokobukubuka;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterListTopBuku extends RecyclerView.Adapter<AdapterListTopBuku.ViewHolder>{
    Context context;
    ArrayList<HashMap<String, String>> list_topbuku;
    private Activity activity;
    private LayoutInflater inflater;

    public AdapterListTopBuku(HomeFragment homeFragment, ArrayList<HashMap<String, String>> list_topbuku) {
        this.context = homeFragment.getContext();
        this.list_topbuku = list_topbuku;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_topbuku, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AdapterListTopBuku.ViewHolder holder, int position) {
        Glide.with(context)
                .load(Server.URL+list_topbuku.get(position).get("gambar"))
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgtop);
        holder.txtnama.setText(list_topbuku.get(position).get("nama_buku"));
        holder.txtpenulis.setText(list_topbuku.get(position).get("nama_penulis"));
        holder.txtharga.setText(list_topbuku.get(position).get("price"));
        holder.txtgenre.setText(list_topbuku.get(position).get("genre"));
//        holder.txtid.setText(list_topevent.get(position).get("id"));
    }



    @Override
    public int getItemCount() {
        return list_topbuku.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtnama,txtpenulis, txtharga, txtgenre;
        ImageView imgtop;

        public ViewHolder(View itemView) {
            super(itemView);

            txtnama = (TextView) itemView.findViewById(R.id.txtName);
            txtpenulis = (TextView) itemView.findViewById(R.id.txtPenulis);
            txtharga = (TextView) itemView.findViewById(R.id.txtHarga);
            txtgenre = (TextView) itemView.findViewById(R.id.txtGenre);
            imgtop = (ImageView) itemView.findViewById(R.id.imgThumb);
        }
    }
}
