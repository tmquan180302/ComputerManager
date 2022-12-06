package com.poly.computermanager.adapter;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.poly.computermanager.dao.DAOHang;
import com.poly.computermanager.dao.DAOLoaiSanPham;
import com.poly.computermanager.model.LoaiSanPham;
import com.poly.computermanager.R;
import com.poly.computermanager.ui.loaisanpham.ActivityChiTietLoaiSP;

import java.util.ArrayList;
import java.util.Random;

public class AdapterLoaiSanPham extends RecyclerView.Adapter<AdapterLoaiSanPham.ViewHolderLoaiSP>implements Filterable {
    Context context;
    ArrayList<LoaiSanPham> listLSP;
    ArrayList<LoaiSanPham> listLSPOld;
    DAOLoaiSanPham daoLoaiSanPham;
    DAOHang daoHang;
    TextDrawable textDrawable;
    public AdapterLoaiSanPham(Context context, ArrayList<LoaiSanPham> listLSP) {
        this.context = context;
        this.listLSP = listLSP;
        this.listLSPOld = listLSP;
        daoLoaiSanPham = new DAOLoaiSanPham(context);
        daoHang = new DAOHang(context);
    }

    @Override
    public ViewHolderLoaiSP onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loaisanpham,parent, false);
        return new ViewHolderLoaiSP(view);
    }

    @Override
    public void onBindViewHolder(AdapterLoaiSanPham.ViewHolderLoaiSP holder, int position) {
        LoaiSanPham loaiSanPham = listLSP.get(position);
        holder.tv_maLoaiSP.setText("Mã loại: " + loaiSanPham.getMslsp());
        holder.tv_tenLoaiSP.setText(loaiSanPham.getTenlsp());
        if (loaiSanPham.getHinhanh()==null){
            textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect(loaiSanPham.getTenlsp().substring(0, 1).toUpperCase(), getRandomColor());
            holder.img_viewAnhLoaiSP.setImageDrawable(textDrawable);
        }else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(loaiSanPham.getHinhanh(), 0, loaiSanPham.getHinhanh().length);
            holder.img_viewAnhLoaiSP.setImageBitmap(bitmap);
        }
        holder.img_deleteLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_delete, null);
                builder1.setView(view1);
                builder1.setCancelable(false);
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog1.show();
                Button button = view1.findViewById(R.id.btn_delete);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<LoaiSanPham> listloaiSP = daoLoaiSanPham.checkGetIDLoai(loaiSanPham.getMslsp());
                        if (listloaiSP.size() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View view11 = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_delete_child, null);
                            builder.setCancelable(false);
                            builder.setView(view11);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alertDialog.show();
                            Button btn_delete_child = view11.findViewById(R.id.btn_delete_child);
                            btn_delete_child.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                    alertDialog1.dismiss();
                                }
                            });
                            return;
                        } else {
                            int kq = daoLoaiSanPham.deleteLoaiSanPham(loaiSanPham.getMslsp());
                            if (kq > 0 ){
                                listLSP.clear();
                                listLSP.addAll(daoLoaiSanPham.getAll());
                                notifyDataSetChanged();
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                alertDialog1.dismiss();
                            }else {
                                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                alertDialog1.dismiss();
                            }
                        }

                    }
                });
            }
        });

        holder.cv_chitietLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChiTietLoaiSP.class);
                intent.putExtra("mslsp",loaiSanPham.getMslsp());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listLSP.size();
    }
    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search=constraint.toString();
                if (search.isEmpty()){
                    listLSP=listLSPOld;
                }else {
                    ArrayList<LoaiSanPham> list=new ArrayList<>();
                    for (LoaiSanPham loaiSanPham:listLSPOld){
                        if (loaiSanPham.getTenlsp().toLowerCase().contains(search.toLowerCase())){
                            list.add(loaiSanPham);
                        }
                    }
                    listLSP=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=listLSP;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listLSP= (ArrayList<LoaiSanPham>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolderLoaiSP extends RecyclerView.ViewHolder {
        TextView tv_tenLoaiSP, tv_maLoaiSP;
        ImageView img_viewAnhLoaiSP,img_deleteLoaiSP;
        CardView cv_chitietLoaiSP;
        public ViewHolderLoaiSP(View itemView) {
            super(itemView);
            tv_maLoaiSP = itemView.findViewById(R.id.tv_maLoaiSP);
            tv_tenLoaiSP = itemView.findViewById(R.id.tv_tenLoaiSP);
            img_viewAnhLoaiSP = itemView.findViewById(R.id.img_viewAnhLoaiSP);
            img_deleteLoaiSP = itemView.findViewById(R.id.img_deleteLoaiSP);
            cv_chitietLoaiSP = itemView.findViewById(R.id.cv_chitietLoaiSP);
        }
    }
}
