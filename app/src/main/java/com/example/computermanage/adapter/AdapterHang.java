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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.computermanage.dao.DAOHang;
import com.example.computermanage.dao.DAOLoaiSanPham;
import com.example.computermanage.model.Hang;
import com.example.computermanage.R;
import com.example.computermanage.model.LoaiSanPham;
import com.example.computermanage.ui.hang.ActivityChiTietHang;

import java.util.ArrayList;
import java.util.Random;

public class AdapterHang extends RecyclerView.Adapter<AdapterHang.HangViewHolder> implements Filterable {
    private Context context;
    ArrayList<Hang> hangArrayList;
    ArrayList<Hang> hangArrayListOld;
    TextDrawable textDrawable;
    DAOHang daoHang;
    DAOLoaiSanPham daoLoaiSanPham;

    public AdapterHang(Context context, ArrayList<Hang> hangArrayList) {
        this.context = context;
        this.hangArrayList = hangArrayList;
        this.hangArrayListOld = hangArrayList;
        daoHang = new DAOHang(context);
        daoLoaiSanPham=new DAOLoaiSanPham(context);
    }

    public AdapterHang() {
    }

    @NonNull
    @Override
    public HangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hang,parent, false);
        return new HangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHang.HangViewHolder holder, int position) {
        Hang hang = hangArrayList.get(position);
        holder.tvMaHang.setText("Mã hãng: " + hang.getMshang());
        holder.tvTenHang.setText("Tên hãng: " + hang.getTenhang());
        LoaiSanPham loaiSanPham=daoLoaiSanPham.getID(String.valueOf(hang.getMslsp()));
        holder.tv_tenloaiSPHang.setText("Loại sản phẩm:"+loaiSanPham.getTenlsp());
        String tenhang = hang.getTenhang();
        if (hang.getHinhanh() == null) {
            textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect(tenhang.substring(0, 1).toUpperCase(), getRandomColor());
            holder.imgAnhHang.setImageDrawable(textDrawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(hang.getHinhanh(), 0, hang.getHinhanh().length);
            holder.imgAnhHang.setImageBitmap(bitmap);
        }
        holder.cv_chitiethang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChiTietHang.class);
                intent.putExtra("mahang", hang.getMshang());
                context.startActivity(intent);
            }
        });
        holder.img_deleteHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_delete, null);
                builder1.setView(view1);
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog1.show();
                Button button = view1.findViewById(R.id.btn_delete);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<Hang> listHang = daoHang.checkGetIDHang(hang.getMshang());
                        if (listHang.size() > 0) {
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
                            int kq = daoHang.deleteHang(String.valueOf(hang.getMshang()));
                            if (kq > 0) {
                                hangArrayList.clear();
                                hangArrayList.addAll(daoHang.getAll());
                                notifyDataSetChanged();
                                Toast.makeText(view1.getContext(), "Xóa thành công ", Toast.LENGTH_SHORT).show();
                                alertDialog1.dismiss();
                            } else {
                                Toast.makeText(view1.getContext(), "Xóa thất bại ", Toast.LENGTH_SHORT).show();
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
        return hangArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search=constraint.toString();
                if (search.isEmpty()){
                    hangArrayList=hangArrayListOld;
                }else {
                    ArrayList<Hang> list=new ArrayList<>();
                    for (Hang hang:hangArrayListOld){
                        if (hang.getTenhang().toLowerCase().contains(search.toLowerCase())){
                            list.add(hang);
                        }
                    }
                    hangArrayList=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=hangArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                hangArrayList= (ArrayList<Hang>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class HangViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaHang;
        TextView tvTenHang;
        TextView tv_tenloaiSPHang;
        ImageView imgAnhHang, img_deleteHang;
        CardView cv_chitiethang;

        public HangViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaHang = itemView.findViewById(R.id.tvMaHang);
            tvTenHang = itemView.findViewById(R.id.tvTenHang);
            tv_tenloaiSPHang = itemView.findViewById(R.id.tv_tenloaiSPHang);
            imgAnhHang = itemView.findViewById(R.id.imgAnhHang);
            img_deleteHang = itemView.findViewById(R.id.img_deleteHang);
            cv_chitiethang = itemView.findViewById(R.id.cv_chitiethang);

        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
