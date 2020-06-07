package com.adp.tokobukubuka;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AdapterListBuku extends RecyclerView.Adapter<AdapterListBuku.ViewHolder>{
    Context context;
    ArrayList<HashMap<String, String>> list_buku;
    private Activity activity;
    private LayoutInflater inflater;

    public AdapterListBuku(PurchasedFragment purchasedFragment, ArrayList<HashMap<String, String>> list_buku) {
        this.context = purchasedFragment.getContext();
        this.list_buku = list_buku;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_topbuku, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final AdapterListBuku.ViewHolder holder, int position) {
        Glide.with(context)
                .load(Server.URL+list_buku.get(position).get("gambar"))
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgtop);
        holder.txtnama.setText(list_buku.get(position).get("nama_buku"));
        holder.txtpenulis.setText(list_buku.get(position).get("nama_penulis"));
        holder.txtharga.setText(list_buku.get(position).get("price"));
        holder.txtgenre.setText(list_buku.get(position).get("genre"));

//        final String image = PurchasedFragment.gambar.get(position);
//
//        final ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
//
//        final Handler handler = new Handler();
//
//        Thread th = new Thread(new Runnable() {
//            public void run() {
//
//                try {
//
//                    long imageLength = 0;
//
//                    ImageManager.GetImage(image, imageStream, imageLength);
//
//                    handler.post(new Runnable() {
//
//                        public void run() {
//                            byte[] buffer = imageStream.toByteArray();
//
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
//
//                            holder.imgtop.setImageBitmap(bitmap);
//                        }
//                    });
//                }
//                catch(Exception ex) {
//                    final String exceptionMessage = ex.getMessage();
//                    handler.post(new Runnable() {
//                        public void run() {
//                            Toast.makeText(activity.getApplicationContext(), exceptionMessage, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }});
//        th.start();
//
//        Picasso.with(activity).load(Server.URL+"/"+PurchasedFragment.gambar.get(position)).placeholder(R.drawable.logo).into(holder.imgtop);
    }



    @Override
    public int getItemCount() {
        return list_buku.size();
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
