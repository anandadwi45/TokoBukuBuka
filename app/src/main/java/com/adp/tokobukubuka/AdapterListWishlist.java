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

public class AdapterListWishlist extends RecyclerView.Adapter<AdapterListWishlist.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> list_wishlist;
    private Activity activity;
    private LayoutInflater inflater;

    public AdapterListWishlist(WishlistFragment wishlistFragment, ArrayList<HashMap<String, String>> list_wishlist) {
        this.context = wishlistFragment.getContext();
        this.list_wishlist = list_wishlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_wishlist, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AdapterListWishlist.ViewHolder holder, int position) {
        Glide.with(context)
                .load(Server.URL+list_wishlist.get(position).get("gambar"))
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgThumb);
        holder.txtName.setText(list_wishlist.get(position).get("nama_buku"));
        holder.txtHarga.setText(list_wishlist.get(position).get("price"));
//        holder.txtid.setText(list_topevent.get(position).get("id"));
    }



    @Override
    public int getItemCount() {
        return list_wishlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtHarga;
        ImageView imgThumb;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtHarga = (TextView) itemView.findViewById(R.id.txtHarga);
            imgThumb = (ImageView) itemView.findViewById(R.id.imgThumb);
        }
    }
}
