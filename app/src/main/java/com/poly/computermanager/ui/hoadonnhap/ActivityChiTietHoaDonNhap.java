package com.poly.computermanager.ui.hoadonnhap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.poly.computermanager.dao.DAOHoaDon;
import com.poly.computermanager.dao.DAOHoaDonCT;
import com.poly.computermanager.dao.DAONhanVien;
import com.poly.computermanager.dao.DAOSanPham;
import com.poly.computermanager.model.HoaDon;
import com.poly.computermanager.model.HoaDonChiTiet;
import com.poly.computermanager.model.NhanVien;
import com.poly.computermanager.model.SanPham;
import com.poly.computermanager.R;

import java.text.DecimalFormat;

public class ActivityChiTietHoaDonNhap extends AppCompatActivity {
    TextView tv_ngayCTHDNhap, tv_nguoitaoCTHDNhap, tv_tenSPCTHDNhap,
            tv_soluongCTHDNhap, tv_dongiaCTHDNhap, tv_thanhtienCTHDNhap, tv_tinhtrangCTHDNhap;
    DAOHoaDon daoHoaDon;
    DAOHoaDonCT daoHoaDonCT;
    DAOSanPham daoSanPham;
    String maNV;
    DAONhanVien daoNhanVien;
    String mahd;
    Toolbar toolbar;
    HoaDonChiTiet hoaDonChiTiet;
    HoaDon hoaDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don_nhap);
        tv_ngayCTHDNhap = findViewById(R.id.tv_ngayCTHDNhap);
        tv_nguoitaoCTHDNhap = findViewById(R.id.tv_nguoitaoCTHDNhap);
        tv_tenSPCTHDNhap = findViewById(R.id.tv_tenSPCTHDNhap);
        tv_tinhtrangCTHDNhap = findViewById(R.id.tv_tinhtrangCTHDNhap);
        tv_soluongCTHDNhap = findViewById(R.id.tv_soluongCTHDNhap);
        tv_dongiaCTHDNhap = findViewById(R.id.tv_dongiaCTHDNhap);
        tv_thanhtienCTHDNhap = findViewById(R.id.tv_thanhtienCTHDNhap);
        toolbar = findViewById(R.id.tb_chitietHDNhap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        daoHoaDon = new DAOHoaDon(this);
        daoHoaDonCT = new DAOHoaDonCT(this);
        daoSanPham = new DAOSanPham(this);
        daoNhanVien = new DAONhanVien(this);
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        mahd = intent.getStringExtra("mahd");
        getSupportActionBar().setTitle(mahd);

        hoaDon = daoHoaDon.getID(mahd);
        hoaDonChiTiet = daoHoaDonCT.getID(mahd);
        NhanVien nhanVien = daoNhanVien.getID(hoaDon.getMsnv());

        SanPham sanPham = daoSanPham.getID(hoaDonChiTiet.getMssp());
        tv_tenSPCTHDNhap.setText(sanPham.getTensp());
        tv_ngayCTHDNhap.setText(hoaDon.getNgaymua());
        tv_nguoitaoCTHDNhap.setText(nhanVien.getHoten());

        switch (sanPham.getTinhtrang()) {
            case 0:
                tv_tinhtrangCTHDNhap.setText("Like new 99%");
                break;
            case 1:
                tv_tinhtrangCTHDNhap.setText("Mới");
                break;
            case 2:
                tv_tinhtrangCTHDNhap.setText("Cũ");
                break;
        }


        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tv_soluongCTHDNhap.setText(hoaDonChiTiet.getSoluong() + "");
        tv_dongiaCTHDNhap.setText(decimalFormat.format(hoaDonChiTiet.getDongia()) + "đ");
        double thanhtien = hoaDonChiTiet.getDongia() * hoaDonChiTiet.getSoluong();
        tv_thanhtienCTHDNhap.setText(decimalFormat.format(thanhtien) + "đ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_edit:
                Intent intent = new Intent(ActivityChiTietHoaDonNhap.this, ActivityEditHoaDonNhap.class);
                intent.putExtra("mahdnhap", mahd);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}