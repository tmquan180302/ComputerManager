package com.poly.computermanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.computermanager.dao.DAOHoaDon;
import com.poly.computermanager.dao.DAOHoaDonCT;
import com.poly.computermanager.dao.DAOKhachHang;
import com.poly.computermanager.dao.DAOSanPham;
import com.poly.computermanager.model.HoaDon;
import com.poly.computermanager.model.HoaDonChiTiet;
import com.poly.computermanager.model.KhachHang;
import com.poly.computermanager.model.SanPham;
import com.poly.computermanager.R;
import com.poly.computermanager.ui.hoadonxuat.ActivityChiTietHoaDonXuat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class AdapterHDXuat extends RecyclerView.Adapter<AdapterHDXuat.HDXuatViewHolder>implements Filterable {
    private Context context;
    ArrayList<HoaDon> listHDXuat;
    ArrayList<HoaDon> listHDXuatOld;
    DAOHoaDon daoHoaDon;
    DAOSanPham daoSanPham;
    DAOHoaDonCT daoHoaDonCT;
    DAOKhachHang daoKhachHang;
    Drawable drawable;

    public AdapterHDXuat(Context context, ArrayList<HoaDon> listHDXuat) {
        this.context = context;
        this.listHDXuat = listHDXuat;
        this.listHDXuatOld = listHDXuat;
        daoHoaDon = new DAOHoaDon(context);
        daoSanPham = new DAOSanPham(context);
        daoHoaDonCT = new DAOHoaDonCT(context);
        daoKhachHang = new DAOKhachHang(context);
    }

    @NonNull
    @Override
    public HDXuatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoadonxuat, parent, false);
        return new HDXuatViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull AdapterHDXuat.HDXuatViewHolder holder, int position) {
        HoaDon hoaDon = listHDXuat.get(position);
        if (hoaDon == null) {
            return;
        }

        HoaDonChiTiet hoaDonChiTiet = daoHoaDonCT.getID(hoaDon.getMshd());
        holder.tv_maHDxuat.setText("Mã HĐ: " + hoaDon.getMshd());
        holder.tv_ngayHDXuat.setText("Ngày: "+hoaDon.getNgaymua());

        if (hoaDonChiTiet.getBaohanh() == 0) {
            holder.tv_baohangHDXuat.setText("Không bảo hành");
            drawable = context.getDrawable(R.drawable.ic_kobaohanh);
            holder.img_baohanh_HDCTXuat.setImageDrawable(drawable);
        } else if (hoaDonChiTiet.getBaohanh() == 1) {
            holder.tv_baohangHDXuat.setText("6 tháng BH");
            drawable = context.getDrawable(R.drawable.ic_baohanh6t);
            holder.img_baohanh_HDCTXuat.setImageDrawable(drawable);
        } else if (hoaDonChiTiet.getBaohanh() == 2) {
            drawable = context.getDrawable(R.drawable.ic_baohanh12t);
            holder.img_baohanh_HDCTXuat.setImageDrawable(drawable);
            holder.tv_baohangHDXuat.setText("12 tháng BH");
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        double tien = hoaDonChiTiet.getSoluong() * hoaDonChiTiet.getDongia();
        switch (hoaDonChiTiet.getGiamgia()) {
            case 0:
                holder.tv_khuyenmaiHDXuat.setText("Khuyến mãi: không khuyến mãi");
                holder.tv_thanhtienHDXuat.setText("Thành tiền: " + decimalFormat.format(tien) + " VNĐ");
                break;
            case 1:
                holder.tv_khuyenmaiHDXuat.setText("Khuyến mãi: 5%");
                holder.tv_thanhtienHDXuat.setText("Thành tiền: " + decimalFormat.format(tien - tien * 0.05) + " VNĐ");
                break;
            case 2:
                holder.tv_khuyenmaiHDXuat.setText("Khuyến mãi: 10%");
                holder.tv_thanhtienHDXuat.setText("Thành tiền: " + decimalFormat.format(tien - tien * 0.1) + " VNĐ");
                break;
            case 3:
                holder.tv_khuyenmaiHDXuat.setText("Khuyến mãi: 15%");
                holder.tv_thanhtienHDXuat.setText("Thành tiền: " + decimalFormat.format(tien - tien * 0.15) + " VNĐ");
                break;
            case 4:
                holder.tv_khuyenmaiHDXuat.setText("Khuyến mãi: 20%");
                holder.tv_thanhtienHDXuat.setText("Thành tiền: " + decimalFormat.format(tien - tien * 0.2) + " VNĐ");
                break;
            case 5:
                holder.tv_khuyenmaiHDXuat.setText("Khuyến mãi: 25%");
                holder.tv_thanhtienHDXuat.setText("Thành tiền: " + decimalFormat.format(tien - tien * 0.25) + " VNĐ");
                break;
            case 6:
                holder.tv_khuyenmaiHDXuat.setText("Khuyến mãi: 30%");
                holder.tv_thanhtienHDXuat.setText("Thành tiền: " + decimalFormat.format(tien - tien * 0.3) + " VNĐ");
                break;
        }

        SanPham sanPham = daoSanPham.getID(hoaDonChiTiet.getMssp());
        holder.tv_tenHDSP_xuat.setText("Sản phẩm: " + sanPham.getTensp());
        holder.tv_dongiaHDXuat.setText("Đơn giá: " + decimalFormat.format(hoaDonChiTiet.getDongia()) + " VNĐ");
        holder.tv_soluongHDXuat.setText("Số lượng: " + hoaDonChiTiet.getSoluong());
        KhachHang khachHang = daoKhachHang.getID(hoaDon.getMskh());
        holder.tv_khachhangHDXuat.setText("Khách hàng: " + khachHang.getHoten());


        switch (hoaDon.getTrangthai()) {
            case 0:
                holder.tv_trangthaiHDXuat.setText("Chưa thanh toán");
                holder.tv_trangthaiHDXuat.setTextColor(Color.RED);
                break;
            case 1:
                holder.tv_trangthaiHDXuat.setText("Đã thanh toán");
                holder.tv_trangthaiHDXuat.setTextColor(Color.GREEN);
                break;
            default:
                holder.tv_trangthaiHDXuat.setText("Chưa thanh toán");
                holder.tv_trangthaiHDXuat.setTextColor(Color.RED);
                break;
        }


        holder.img_deleteHDX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kq = daoHoaDon.deleteHoaDon(hoaDon.getMshd());
                if (kq > 0) {
                    listHDXuat.clear();
                    listHDXuat.addAll(daoHoaDon.getAllXuat());
                    notifyDataSetChanged();
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.cv_chitietHDXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityChiTietHoaDonXuat.class);
                intent.putExtra("mahdxuat", hoaDon.getMshd());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listHDXuat.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search=constraint.toString();
                if (search.isEmpty()){
                    listHDXuat=listHDXuatOld;
                }else {
                    ArrayList<HoaDon> list=new ArrayList<>();
                    for (HoaDon hoaDon:listHDXuat){
                        HoaDonChiTiet hoaDonChiTiet=daoHoaDonCT.getID(hoaDon.getMshd());
                        SanPham sanPham=daoSanPham.getID(hoaDonChiTiet.getMssp());
                        if (hoaDon.getMshd().contains(search)){
                            list.add(hoaDon);
                        }else if (sanPham.getTensp().toLowerCase().contains(search.toLowerCase())){
                            list.add(hoaDon);
                        }
                    }
                    listHDXuat=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=listHDXuat;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listHDXuat= (ArrayList<HoaDon>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class HDXuatViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenHDSP_xuat;
        TextView tv_maHDxuat, tv_khachhangHDXuat, tv_soluongHDXuat, tv_dongiaHDXuat, tv_khuyenmaiHDXuat, tv_ngayHDXuat, tv_baohangHDXuat, tv_thanhtienHDXuat, tv_trangthaiHDXuat;
        CardView cv_chitietHDXuat;
        ImageView img_baohanh_HDCTXuat, img_deleteHDX;

        public HDXuatViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tenHDSP_xuat = itemView.findViewById(R.id.tv_tenHDSP_xuat);
            tv_maHDxuat = itemView.findViewById(R.id.tv_maHDxuat);
            tv_khachhangHDXuat = itemView.findViewById(R.id.tv_khachhangHDXuat);
            tv_soluongHDXuat = itemView.findViewById(R.id.tv_soluongHDXuat);
            tv_dongiaHDXuat = itemView.findViewById(R.id.tv_dongiaHDXuat);
            tv_khuyenmaiHDXuat = itemView.findViewById(R.id.tv_khuyenmaiHDXuat);
            tv_ngayHDXuat = itemView.findViewById(R.id.tv_ngayHDXuat);
            tv_baohangHDXuat = itemView.findViewById(R.id.tv_baohangHDXuat);
            tv_thanhtienHDXuat = itemView.findViewById(R.id.tv_thanhtienHDXuat);
            tv_trangthaiHDXuat = itemView.findViewById(R.id.tv_trangthaiHDXuat);
            cv_chitietHDXuat = itemView.findViewById(R.id.cv_chitietHDXuat);
            img_baohanh_HDCTXuat = itemView.findViewById(R.id.img_baohanh_HDCTXuat);
            img_deleteHDX = itemView.findViewById(R.id.img_deleteHDX);


        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
