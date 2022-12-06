package com.example.computermanage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.computermanage.R;
import com.example.computermanage.dao.DAOSanPham;
import com.example.computermanage.dao.DAOSanPhamCT;
import com.example.computermanage.model.SanPham;
import com.example.computermanage.model.SanPhamChiTiet;
import com.example.computermanage.ui.sanpham.ActivityChiTietSanPham;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class AdapterQuerySanPham extends RecyclerView.Adapter<AdapterQuerySanPham.ViewHolderSanPham> {
    Context context;
    ArrayList<SanPham> listSP;
    DAOSanPham daoSanPham;
    DAOSanPhamCT daoSanPhamCT;
    TextDrawable textDrawable;


    public AdapterQuerySanPham(Context context, ArrayList<SanPham> listSP) {
        this.context = context;
        this.listSP = listSP;
        daoSanPham = new DAOSanPham(context);
        daoSanPhamCT = new DAOSanPhamCT(context);
    }

    @Override
    public ViewHolderSanPham onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_query_sanpham,parent, false);
        return new ViewHolderSanPham(view);
    }

    @Override
    public void onBindViewHolder(AdapterQuerySanPham.ViewHolderSanPham holder, int position) {
        SanPham sanPham = listSP.get(position);
        holder.tv_tenSP.setText(sanPham.getTensp());
        holder.tv_maSP.setText("Mã sản phẩm: "+sanPham.getMssp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tv_giaSP.setText("Giá: "+decimalFormat.format(sanPham.getGiatien())+" VNĐ");
        if (sanPham.getHinhanh() == null) {
            textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect(sanPham.getTensp().substring(0, 1).toUpperCase(), getRandomColor());
            holder.img_viewAnhSP.setImageDrawable(textDrawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(sanPham.getHinhanh(), 0, sanPham.getHinhanh().length);
            holder.img_viewAnhSP.setImageBitmap(bitmap);
        }
        if (daoSanPhamCT.checkTTMaSP(sanPham.getMssp())>0){
            SanPhamChiTiet sanPhamChiTiet = daoSanPhamCT.getID(sanPham.getMssp());
            holder.tv_cpuSP.setText("CPU: "+ sanPhamChiTiet.getCpu());
            holder.tv_oCungSP.setText("Ổ cứng: "+ sanPhamChiTiet.getOcung());
            holder.tv_ramSP.setText("Ram: "+sanPhamChiTiet.getRam());
        }else {
            holder.tv_cpuSP.setText("Chưa cập nhật");
            holder.tv_oCungSP.setText("Chưa cập nhật");
            holder.tv_ramSP.setText("Chưa cập nhật");
        }




    }

    @Override
    public int getItemCount() {
        if (listSP == null) {
            return 0;
        }
        return listSP.size();
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    class ViewHolderSanPham extends RecyclerView.ViewHolder {
        TextView tv_tenSP, tv_maSP, tv_cpuSP, tv_ramSP, tv_oCungSP, tv_giaSP;
        ImageView img_viewAnhSP, img_deleteSP;
        public ViewHolderSanPham(View itemView) {
            super(itemView);
            tv_maSP = itemView.findViewById(R.id.tv_maSP);
            tv_tenSP = itemView.findViewById(R.id.tv_tenSP);
            tv_cpuSP = itemView.findViewById(R.id.tv_cpuSP);
            tv_ramSP = itemView.findViewById(R.id.tv_ramSP);
            tv_oCungSP = itemView.findViewById(R.id.tv_oCungSP);
            tv_giaSP = itemView.findViewById(R.id.tv_giaSP);
            img_viewAnhSP = itemView.findViewById(R.id.img_viewAnhSP);
            img_deleteSP = itemView.findViewById(R.id.img_deleteSP);
        }
    }
}
