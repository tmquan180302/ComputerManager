package com.example.computermanage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.computermanage.dao.DAOHoaDon;
import com.example.computermanage.dao.DAOHoaDonCT;
import com.example.computermanage.dao.DAOSanPham;
import com.example.computermanage.model.Hang;
import com.example.computermanage.model.HoaDon;
import com.example.computermanage.model.HoaDonChiTiet;
import com.example.computermanage.R;
import com.example.computermanage.model.SanPham;
import com.example.computermanage.ui.hoadonnhap.ActivityChiTietHoaDonNhap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class AdapterHDNhap extends RecyclerView.Adapter<AdapterHDNhap.HDNhapViewHolder>implements Filterable {
    private Context context;
    ArrayList<HoaDon> listHDNhap;
    ArrayList<HoaDon> listHDNhapOld;
    DAOHoaDon daoHoaDon;
    DAOSanPham daoSanPham;
    DAOHoaDonCT daoHoaDonCT;

    public AdapterHDNhap(Context context, ArrayList<HoaDon> listHDNhap) {
        this.context = context;
        this.listHDNhap = listHDNhap;
        this.listHDNhapOld = listHDNhap;
        daoHoaDon = new DAOHoaDon(context);
        daoSanPham = new DAOSanPham(context);
        daoHoaDonCT = new DAOHoaDonCT(context);
    }

    @NonNull
    @Override
    public HDNhapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoadonnhap, parent, false);
        return new HDNhapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHDNhap.HDNhapViewHolder holder, int position) {
        HoaDon hoaDon = listHDNhap.get(position);
        if (hoaDon == null) {
            return;
        }
        holder.tv_maHDNhap.setText("Mã HĐ: " + hoaDon.getMshd());
        holder.tv_ngayHDNhap.setText("Ngày: "+hoaDon.getNgaymua());
        HoaDonChiTiet hoaDonChiTiet = daoHoaDonCT.getMaHD(hoaDon.getMshd());
        String maSP = hoaDonChiTiet.getMssp();


        String tensp = daoSanPham.getID(maSP).getTensp();
        holder.tv_tenHDSP.setText("Sản phẩm: " + tensp);
        int tinhtrangsp = daoSanPham.getID(maSP).getTinhtrang();
        if (daoSanPham.getID(maSP).getHinhanh() == null) {
            TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(48).height(48).endConfig().buildRect(tensp.substring(0, 1).toUpperCase(), getRandomColor());
            holder.img_viewAnhHDNhap.setImageDrawable(textDrawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(daoSanPham.getID(maSP).getHinhanh(), 0, daoSanPham.getID(maSP).getHinhanh().length);
            holder.img_viewAnhHDNhap.setImageBitmap(bitmap);

        }
        switch (tinhtrangsp) {
            case 0:
                holder.tv_tinhtrangHDNhap.setText("Tình trạng: Like new 99%");
                break;
            case 1:
                holder.tv_tinhtrangHDNhap.setText("Tình trạng: mới");
                break;
            case 2:
                holder.tv_tinhtrangHDNhap.setText("Tình trạng: cũ");
                break;
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tv_dongiaHDNhap.setText("Giá: " + decimalFormat.format(hoaDonChiTiet.getDongia()) + "đ");
        holder.tv_soluongHDNhap.setText("Số lượng: " + hoaDonChiTiet.getSoluong());
        double thanhtien = hoaDonChiTiet.getSoluong() * hoaDonChiTiet.getDongia();
        holder.tv_thanhtienHDNhap.setText("Thành tiền: " + decimalFormat.format(thanhtien) + "đ");
        holder.img_deleteHDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kq = daoHoaDon.deleteHoaDon(hoaDon.getMshd());
                if (kq > 0) {
                    SanPham sanPham=daoSanPham.getID(hoaDonChiTiet.getMssp());
                    sanPham.setTrangthai(1);
                    daoSanPham.updateSanPham(sanPham);
                    listHDNhap.clear();
                    listHDNhap.addAll(daoHoaDon.getAllNhap());
                    notifyDataSetChanged();
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.cv_chitietHDNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChiTietHoaDonNhap.class);
                intent.putExtra("mahd", hoaDon.getMshd());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listHDNhap.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search=constraint.toString();
                if (search.isEmpty()){
                    listHDNhap=listHDNhapOld;
                }else {
                    ArrayList<HoaDon> list=new ArrayList<>();
                    for (HoaDon hoaDon:listHDNhap){
                        HoaDonChiTiet hoaDonChiTiet=daoHoaDonCT.getID(hoaDon.getMshd());
                        SanPham sanPham=daoSanPham.getID(hoaDonChiTiet.getMssp());
                        if (hoaDon.getMshd().contains(search)){
                            list.add(hoaDon);
                        }else if (sanPham.getTensp().toLowerCase().contains(search.toLowerCase())){
                            list.add(hoaDon);
                        }
                    }
                    listHDNhap=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=listHDNhap;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listHDNhap= (ArrayList<HoaDon>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class HDNhapViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenHDSP;
        TextView tv_maHDNhap, tv_tinhtrangHDNhap, tv_ngayHDNhap, tv_soluongHDNhap, tv_dongiaHDNhap, tv_thanhtienHDNhap;
        ImageView img_viewAnhHDNhap, img_deleteHDN;
        CardView cv_chitietHDNhap;

        public HDNhapViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tenHDSP = itemView.findViewById(R.id.tv_tenHDSP);
            tv_maHDNhap = itemView.findViewById(R.id.tv_maHDNhap);
            tv_tinhtrangHDNhap = itemView.findViewById(R.id.tv_tinhtrangHDNhap);
            tv_ngayHDNhap = itemView.findViewById(R.id.tv_ngayHDNhap);
            tv_soluongHDNhap = itemView.findViewById(R.id.tv_soluongHDNhap);
            tv_dongiaHDNhap = itemView.findViewById(R.id.tv_dongiaHDNhap);
            tv_thanhtienHDNhap = itemView.findViewById(R.id.tv_thanhtienHDNhap);
            img_viewAnhHDNhap = itemView.findViewById(R.id.img_viewAnhHDNhap);
            img_deleteHDN = itemView.findViewById(R.id.img_deleteHDN);
            cv_chitietHDNhap = itemView.findViewById(R.id.cv_chitietHDNhap);

        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
