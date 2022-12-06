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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.computermanage.dao.DAOSanPham;
import com.example.computermanage.dao.DAOSanPhamCT;
import com.example.computermanage.model.Hang;
import com.example.computermanage.model.SanPham;
import com.example.computermanage.model.SanPhamChiTiet;
import com.example.computermanage.R;
import com.example.computermanage.ui.sanpham.ActivityChiTietSanPham;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class AdapterSanPham extends RecyclerView.Adapter<AdapterSanPham.ViewHolderSanPham> implements Filterable {
    Context context;
    ArrayList<SanPham> listSP;
    ArrayList<SanPham> listSPOld;
    DAOSanPham daoSanPham;
    DAOSanPhamCT daoSanPhamCT;
    TextDrawable textDrawable;


    public AdapterSanPham(Context context, ArrayList<SanPham> listSP) {
        this.context = context;
        this.listSP = listSP;
        this.listSPOld = listSP;
        daoSanPham = new DAOSanPham(context);
        daoSanPhamCT = new DAOSanPhamCT(context);
    }

    @Override
    public ViewHolderSanPham onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sanpham,parent, false);
        return new ViewHolderSanPham(view);
    }

    @Override
    public void onBindViewHolder(AdapterSanPham.ViewHolderSanPham holder, int position) {
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
        holder.cv_chitietSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChiTietSanPham.class);
                intent.putExtra("mssp",sanPham.getMssp());
                context.startActivity(intent);
            }
        });
        holder.img_deleteSP.setOnClickListener(new View.OnClickListener() {
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
                        ArrayList<SanPham> listSP1 = daoSanPham.checkGetIDSpham(sanPham.getMssp());
                        if (listSP1.size() > 0) {
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
                            int kq = daoSanPham.deleteSanPham(sanPham.getMssp());
                            if (kq>0){
                                listSP.clear();
                                listSP.addAll(daoSanPham.getAll());
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
        if (listSP == null) {
            return 0;
        }
        return listSP.size();
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
                    listSP=listSPOld;
                }else {
                    ArrayList<SanPham> list=new ArrayList<>();
                    for (SanPham sanPham:listSP){
                        if (sanPham.getTensp().toLowerCase().contains(search.toLowerCase())){
                            list.add(sanPham);
                        }
                    }
                    listSP=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=listSP;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listSP= (ArrayList<SanPham>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolderSanPham extends RecyclerView.ViewHolder {
        TextView tv_tenSP, tv_maSP, tv_cpuSP, tv_ramSP, tv_oCungSP, tv_giaSP;
        ImageView img_viewAnhSP, img_deleteSP;
        CardView cv_chitietSP;
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
            cv_chitietSP = itemView.findViewById(R.id.cv_chitietSP);
        }
    }
}
