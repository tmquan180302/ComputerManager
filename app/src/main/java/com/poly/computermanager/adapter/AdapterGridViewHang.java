package com.poly.computermanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.poly.computermanager.R;

public class AdapterGridViewHang extends BaseAdapter {
    Context context;
    int image[];

    public AdapterGridViewHang(Context context, int[] image) {
        this.context = context;
        this.image = image;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.gridview_hang,null);
        ImageView imageView = convertView.findViewById(R.id.img_gvHang);
        imageView.setImageResource(image[position]);
        return convertView;
    }
}
