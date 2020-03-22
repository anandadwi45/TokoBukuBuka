package com.adp.tokobukubuka;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterListPenulis extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Category> item;

    public AdapterListPenulis(Activity activity, List<Category> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_penulis, null);

        TextView penulis = (TextView) convertView.findViewById(R.id.penulis);

        Category category;
        category = item.get(position);

        penulis.setText(category.getNama_penulis());

        return convertView;
    }
}
