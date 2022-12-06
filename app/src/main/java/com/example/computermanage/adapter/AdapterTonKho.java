package com.example.computermanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.computermanage.R;
import com.example.computermanage.dao.DAOSanPham;
import com.example.computermanage.model.GetSLNhap;
import com.example.computermanage.model.SanPham;

import java.util.ArrayList;

public class AdapterTonKho extends RecyclerView.Adapter<AdapterTonKho.ViewHolderGetTop> {
    Context context;
    ArrayList<GetSLNhap> listTonKho;
    DAOSanPham daoSanPham;

    public AdapterTonKho(Context context, ArrayList<GetSLNhap> listTonKho) {
        this.context = context;
        this.listTonKho = listTonKho;
        daoSanPham= new DAOSanPham(context);
    }

    @Override
    public ViewHolderGetTop onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_gettop,parent,false);
        return new ViewHolderGetTop(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderGetTop holder, int position) {
        GetSLNhap getSLNhap = listTonKho.get(position);
        SanPham sanPham = daoSanPham.getID(getSLNhap.mssp);
        holder.tv_getTenSP.setText("Tên : "+ sanPham.getTensp());
        holder.tv_getSLSPS.setText("SL: "+getSLNhap.soluongnhap);
    }

    @Override
    public int getItemCount() {
        return listTonKho.size();
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
