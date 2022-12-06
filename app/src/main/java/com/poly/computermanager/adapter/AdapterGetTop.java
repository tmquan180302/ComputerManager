package com.poly.computermanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.poly.computermanager.R;
import com.poly.computermanager.model.GetTop;

import java.util.ArrayList;

public class AdapterGetTop extends RecyclerView.Adapter<AdapterGetTop.ViewHolderGetTop> {
    Context context;
    ArrayList<GetTop> listGT;

    public AdapterGetTop(Context context, ArrayList<GetTop> listGT) {
        this.context = context;
        this.listGT = listGT;
    }

    @Override
    public ViewHolderGetTop onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_gettop,parent,false);
        return new ViewHolderGetTop(view);
    }

    @Override
    public void onBindViewHolder(AdapterGetTop.ViewHolderGetTop holder, int position) {
        GetTop getTop = listGT.get(position);
        holder.tv_getTenSP.setText(getTop.tensp);
        holder.tv_getSLSPS.setText("SL: "+getTop.soluong);
    }

    @Override
    public int getItemCount() {
        return listGT.size();
    }

    public class ViewHolderGetTop extends RecyclerView.ViewHolder {
        TextView tv_getTenSP,tv_getSLSPS;
        public ViewHolderGetTop(View itemView) {
            super(itemView);
            tv_getTenSP = itemView.findViewById(R.id.tv_getTenSP);
            tv_getSLSPS = itemView.findViewById(R.id.tv_getSLSPS);
        }
    }
}
