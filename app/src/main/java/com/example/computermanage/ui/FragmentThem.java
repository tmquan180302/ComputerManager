package com.example.computermanage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.computermanage.LoginActivity;
import com.example.computermanage.R;
import com.example.computermanage.ui.TonKho.ActivityTonKho;
import com.example.computermanage.ui.hang.ActivityHang;
import com.example.computermanage.ui.hoadonnhap.ActivityHoadonNhap;
import com.example.computermanage.ui.hoadonxuat.ActivityHoadonXuat;
import com.example.computermanage.ui.khachhang.ActivityKhachHang;
import com.example.computermanage.ui.loaisanpham.ActivityLoaiSanPham;
import com.example.computermanage.ui.nhanvien.ActivityChiTietNhanVien;
import com.example.computermanage.ui.sanpham.ActivitySanPham;

public class FragmentThem extends Fragment {
    TextView tv_hang,tvKhachHang_Them,tvLoaiSanPham_Them,tvThoat_Them,tvSanPham_Them,tvHoaDonNhap_Them,tvThongTin_Them;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them, container, false);
        tv_hang = view.findViewById(R.id.tv_hang);
        tvKhachHang_Them = view.findViewById(R.id.tvKhachHang_Them);
        tvLoaiSanPham_Them = view.findViewById(R.id.tvLoaiSanPham_Them);
        tvThoat_Them = view.findViewById(R.id.tvThoat_Them);
        tvSanPham_Them = view.findViewById(R.id.tvSanPham_Them);
        tvHoaDonNhap_Them = view.findViewById(R.id.tvHoaDonNhap_Them);
        tvThongTin_Them = view.findViewById(R.id.tvThongTin_Them);

        tv_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getContext(), ActivityHang.class));
            }
        });
        tvKhachHang_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivityKhachHang.class));
            }
        });
        tvLoaiSanPham_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivityLoaiSanPham.class));
            }
        });

        tvSanPham_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivitySanPham.class));
            }
        });
        tvHoaDonNhap_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivityHoadonNhap.class));
            }
        });
        tvThongTin_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getContext(), ActivityChiTietNhanVien.class);
                startActivity(intent);
            }
        });
        tvThoat_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
