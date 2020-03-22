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

public class AdapterListCart extends RecyclerView.Adapter<AdapterListCart.ViewHolder>{
    Context context;
    ArrayList<HashMap<String, String>> list_cart;
    private Activity activity;
    private LayoutInflater inflater;

    public AdapterListCart(CartFragment cartFragment, ArrayList<HashMap<String, String>> list_cart) {
        this.context = cartFragment.getContext();
        this.list_cart = list_cart;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_cart, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AdapterListCart.ViewHolder holder, int position) {
        holder.judul_buku.setText(list_cart.get(position).get("nama_buku"));
        holder.harga_buku.setText(list_cart.get(position).get("price"));
//        holder.txtid.setText(list_topevent.get(position).get("id"));
    }



    @Override
    public int getItemCount() {
        return list_cart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView judul_buku,harga_buku;

        public ViewHolder(View itemView) {
            super(itemView);

            judul_buku = (TextView) itemView.findViewById(R.id.judul_buku);
            harga_buku = (TextView) itemView.findViewById(R.id.harga_buku);
        }
    }
}
