package com.example.computermanage.adapter;

import android.content.Context;
import android.content.Intent;
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

import com.example.computermanage.dao.DAOKhachHang;
import com.example.computermanage.model.Hang;
import com.example.computermanage.model.KhachHang;
import com.example.computermanage.R;
import com.example.computermanage.ui.khachhang.ActivityChiTietKhachHang;

import java.util.ArrayList;

public class AdapterKhachHang extends RecyclerView.Adapter<AdapterKhachHang.ViewHolderKhachHang>implements Filterable {
    Context context;
    ArrayList<KhachHang> listKH;
    ArrayList<KhachHang> listKHOld;
    DAOKhachHang daoKhachHang;

    public AdapterKhachHang(Context context, ArrayList<KhachHang> listKH) {
        this.context = context;
        this.listKH = listKH;
        this.listKHOld = listKH;
        daoKhachHang = new DAOKhachHang(context);
    }

    @Override
    public AdapterKhachHang.ViewHolderKhachHang onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_khachhang, parent, false);
        return new ViewHolderKhachHang(view);
    }

    public void onBindViewHolder(AdapterKhachHang.ViewHolderKhachHang holder, int position) {
        KhachHang khachHang = listKH.get(position);
        holder.tv_tenKhachHang.setText("Tên: " + khachHang.getHoten());
        holder.tv_sdtKhachHang.setText("SĐT: " + khachHang.getSdt());
        holder.tv_diachiKhachHang.setText("Địa chỉ: " + khachHang.getDiachi());
        if (khachHang.getGioitinh() == 0) {
            holder.tv_gtKhachHang.setText("Giới tính: Nam");
        } else if (khachHang.getGioitinh() == 1) {
            holder.tv_gtKhachHang.setText("Giới tính: Nữ");
        }
        holder.cv_chitietKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChiTietKhachHang.class);
                intent.putExtra("makh",khachHang.getMskh());
                context.startActivity(intent);
            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
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
                        ArrayList<KhachHang> listKHang = daoKhachHang.checkGetIDKHang(khachHang.getMskh());
                        if (listKHang.size() > 0) {
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
                            int kq = daoKhachHang.deleteKhachHang(khachHang.getMskh());
                            if (kq>0){
                                listKH.clear();
                                listKH.addAll(daoKhachHang.getAll());
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

    }

    @Override
    public int getItemCount() {
        return listKH.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search=constraint.toString();
                if (search.isEmpty()){
                    listKH=listKHOld;
                }else {
                    ArrayList<KhachHang> list=new ArrayList<>();
                    for (KhachHang khachHang:listKHOld){
                        if (khachHang.getHoten().toLowerCase().contains(search.toLowerCase())){
                            list.add(khachHang);
                        }
                    }
                    listKH=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=listKH;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listKH= (ArrayList<KhachHang>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolderKhachHang extends RecyclerView.ViewHolder {
        TextView tv_tenKhachHang,tv_sdtKhachHang,tv_gtKhachHang,tv_diachiKhachHang;
        ImageView img_delete;
        CardView cv_chitietKH;
        public ViewHolderKhachHang( View itemView) {
            super(itemView);
            tv_tenKhachHang = itemView.findViewById(R.id.tv_tenKhachHang);
            tv_sdtKhachHang = itemView.findViewById(R.id.tv_sdtKhachHang);
            tv_gtKhachHang = itemView.findViewById(R.id.tv_gtKhachHang);
            tv_diachiKhachHang = itemView.findViewById(R.id.tv_diachiKhachHang);
            img_delete = itemView.findViewById(R.id.img_deleteKhachHang);
            cv_chitietKH = itemView.findViewById(R.id.cv_chitietKH);
        }
    }
}
