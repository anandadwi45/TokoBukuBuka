package com.adp.tokobukubuka;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AdapterListBukuAll extends BaseAdapter {
    private Activity activity;
    public AdapterListBukuAll(Activity act) {
        this.activity = act;
    }
    public int getCount() {
        return ListBukuFragment.id_buku.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        TextView txtnama,txtpenulis, txtharga, txtgenre;
        ImageView imgThumb;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list_topbuku, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtnama = (TextView) convertView.findViewById(R.id.txtName);
        holder.txtpenulis = (TextView) convertView.findViewById(R.id.txtPenulis);
        holder.txtharga = (TextView) convertView.findViewById(R.id.txtHarga);
        holder.txtgenre = (TextView) convertView.findViewById(R.id.txtGenre);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
        holder.txtnama.setText(ListBukuFragment.nama_buku.get(position));
        holder.txtpenulis.setText(ListBukuFragment.nama_penulis.get(position));
        holder.txtharga.setText(ListBukuFragment.price.get(position));
        holder.txtgenre.setText(ListBukuFragment.genre.get(position));
        Picasso.with(activity).load(Server.URL+"/"+ListBukuFragment.gambar.get(position)).placeholder(R.drawable.logo).into(holder.imgThumb);
        return convertView;
    }
}
