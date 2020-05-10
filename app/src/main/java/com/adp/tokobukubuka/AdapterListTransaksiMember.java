package com.adp.tokobukubuka;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterListTransaksiMember extends RecyclerView.Adapter<AdapterListTransaksiMember.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> list_trxmember;
    private Activity activity;
    private LayoutInflater inflater;

    public AdapterListTransaksiMember(HistoryFragment historyFragment, ArrayList<HashMap<String, String>> list_trxmember) {
        this.context = historyFragment.getContext();
        this.list_trxmember = list_trxmember;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_trxmember, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AdapterListTransaksiMember.ViewHolder holder, int position) {
        holder.txtId.setText(list_trxmember.get(position).get("id_transaksi"));
        holder.txtHarga.setText(list_trxmember.get(position).get("total_harga"));
        holder.txtTanggal.setText(list_trxmember.get(position).get("tanggal"));
        holder.txtStatus.setText(list_trxmember.get(position).get("status"));
//        holder.txtid.setText(list_topevent.get(position).get("id"));
    }



    @Override
    public int getItemCount() {
        return list_trxmember.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId,txtHarga,txtTanggal,txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            txtId = (TextView) itemView.findViewById(R.id.idHistoryMember);
            txtHarga = (TextView) itemView.findViewById(R.id.hargaHistoryMember);
            txtTanggal = (TextView) itemView.findViewById(R.id.tanggalHistoryMember);
            txtStatus = (TextView) itemView.findViewById(R.id.statusHistoryMember);
        }
    }
}
