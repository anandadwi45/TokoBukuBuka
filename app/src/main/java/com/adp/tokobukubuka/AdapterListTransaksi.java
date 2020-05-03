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

public class AdapterListTransaksi extends RecyclerView.Adapter<AdapterListTransaksi.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> list_transaksi;
    private Activity activity;
    private LayoutInflater inflater;

    public AdapterListTransaksi(AdminHistoryFragment adminHistoryFragment, ArrayList<HashMap<String, String>> list_transaksi) {
        this.context = adminHistoryFragment.getContext();
        this.list_transaksi = list_transaksi;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_transaksi, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AdapterListTransaksi.ViewHolder holder, int position) {
        holder.txtName.setText(list_transaksi.get(position).get("fullname"));
        holder.txtHarga.setText(list_transaksi.get(position).get("total_harga"));
        holder.txtTanggal.setText(list_transaksi.get(position).get("tanggal"));
        holder.txtStatus.setText(list_transaksi.get(position).get("status"));
//        holder.txtid.setText(list_topevent.get(position).get("id"));
    }



    @Override
    public int getItemCount() {
        return list_transaksi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtHarga,txtTanggal,txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.memberHistory);
            txtHarga = (TextView) itemView.findViewById(R.id.hargaHistory);
            txtTanggal = (TextView) itemView.findViewById(R.id.tanggalHistory);
            txtStatus = (TextView) itemView.findViewById(R.id.statusHistory);
        }
    }
}
