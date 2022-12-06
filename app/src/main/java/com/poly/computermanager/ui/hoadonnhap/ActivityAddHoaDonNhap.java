package com.poly.computermanager.ui.hoadonnhap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.poly.computermanager.dao.DAOHoaDon;
import com.poly.computermanager.dao.DAOHoaDonCT;
import com.poly.computermanager.dao.DAONhanVien;
import com.poly.computermanager.dao.DAOSanPham;
import com.poly.computermanager.model.HoaDon;
import com.poly.computermanager.model.HoaDonChiTiet;
import com.poly.computermanager.model.SanPham;
import com.poly.computermanager.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityAddHoaDonNhap extends AppCompatActivity {
    TextInputEditText ed_maHD_nhap, ed_ngayHD_nhap, ed_soluongHD_nhap, ed_dongiaHD_nhap, ed_thanhtienHD_nhap;
    Spinner spn_sanpham;
    ArrayList<SanPham> listSanpham;
    ArrayList<HoaDon> listHoadon;
    DAOSanPham daoSanPham;
    DAOHoaDon daoHoaDon;
    DAONhanVien daoNhanVien;
    DAOHoaDonCT daoHoaDonCT;
    String maNV;
    double dongiasp;
    DecimalFormat decimalFormat;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hoa_don_nhap);
        addConTrol();
        eventDialog();
    }


    public void showDateTimePicker() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityAddHoaDonNhap.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                ed_ngayHD_nhap.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void eventDialog() {


        ed_ngayHD_nhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();

            }
        });
        ed_dongiaHD_nhap.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Thanhtien() != 0) {
                        ed_thanhtienHD_nhap.setText(decimalFormat.format(Thanhtien()) + "");
                    } else {
                        ed_thanhtienHD_nhap.setText("Null");
                    }
                }
            }
        });

        ed_soluongHD_nhap.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Thanhtien() != 0) {
                        ed_thanhtienHD_nhap.setText(decimalFormat.format(Thanhtien()) + "");
                    } else {
                        ed_thanhtienHD_nhap.setText("Null");
                    }
                }
            }
        });


    }

    private double Thanhtien() {
        double thanhtien = 0;
        try {
            double soluong = Double.parseDouble(ed_soluongHD_nhap.getText().toString());
            double dongia = Double.parseDouble(ed_dongiaHD_nhap.getText().toString());
            thanhtien = soluong * dongia;
        } catch (Exception e) {
            thanhtien = 0;
        }
        return thanhtien;
    }

    private void addConTrol() {

        ed_maHD_nhap = findViewById(R.id.ed_maHD_nhap);
        ed_ngayHD_nhap = findViewById(R.id.ed_ngayHD_nhap);
        spn_sanpham = findViewById(R.id.spn_sanpham);
        ed_soluongHD_nhap = findViewById(R.id.ed_soluongHD_nhap);
        ed_dongiaHD_nhap = findViewById(R.id.ed_dongiaHD_nhap);
        ed_thanhtienHD_nhap = findViewById(R.id.ed_thanhtienHD_nhap);
        toolbar = findViewById(R.id.tb_addHDNhap);
        daoSanPham = new DAOSanPham(this);
        daoHoaDon = new DAOHoaDon(this);
        daoHoaDonCT = new DAOHoaDonCT(this);
        daoNhanVien = new DAONhanVien(this);
        listSanpham = new ArrayList<>();
        listHoadon = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm HD nhập");
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
        listSanpham = daoSanPham.getAll();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listSanpham);
        spn_sanpham.setAdapter(adapter);
        spn_sanpham.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dongiasp = listSanpham.get(position).getGiatien();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        decimalFormat = new DecimalFormat("###,###,###");
    }

    public void getMaNV() {
        SharedPreferences preferences = getSharedPreferences("rememberLogin", MODE_PRIVATE);
        String taiKhoan = preferences.getString("user", "");
        if (daoNhanVien.getID(taiKhoan).getMsnv() == null) {
            Toast.makeText(getApplicationContext(), "Chua co nhan vien", Toast.LENGTH_SHORT).show();
            return;
        } else {
            maNV = daoNhanVien.getID(taiKhoan).getMsnv();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    public boolean check() {
        String mahd = ed_maHD_nhap.getText().toString();
        String ngay = ed_ngayHD_nhap.getText().toString();
        String soluong = ed_soluongHD_nhap.getText().toString();
        String dongia = ed_dongiaHD_nhap.getText().toString();
        Pattern p = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
        Matcher m = p.matcher(ed_maHD_nhap.getText().toString());
        if (mahd.isEmpty() || ngay.isEmpty() || soluong.isEmpty() || dongia.isEmpty()) {
            Toast.makeText(this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (m.find() == false) {
            Toast.makeText(this, "Kí tự đầu phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(soluong) < 0) {
            Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Double.parseDouble(dongia) < 0 || Double.parseDouble(dongia) > dongiasp) {
            Toast.makeText(this, "Đơn giá phải lớn hơn 0 và nhỏ hơn đơn giá sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_reset:

                return true;
            case R.id.menu_save:
                if (check()) {
                    String mahd = ed_maHD_nhap.getText().toString();
                    getMaNV();

                    String ngay = ed_ngayHD_nhap.getText().toString();
                    int soLuong = Integer.parseInt(ed_soluongHD_nhap.getText().toString());
                    double donGia = Double.parseDouble(ed_dongiaHD_nhap.getText().toString());
                    if (daoHoaDon.checkMaHD(mahd) > 0) {
                        Toast.makeText(this, "Mã hóa đơn đã tồn tại hệ thống", Toast.LENGTH_SHORT).show();
                    } else {
                        HoaDon hoaDon = new HoaDon();
                        hoaDon.setMshd(mahd);
                        hoaDon.setMsnv(maNV);
                        hoaDon.setPhanloaiHD(0);
                        hoaDon.setNgaymua(ngay);
                        hoaDon.setTrangthai(2);

                        long kq = daoHoaDon.insertHoaDon(hoaDon);
                        if (kq > 0) {
                            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                            SanPham sanPham = (SanPham) spn_sanpham.getSelectedItem();
                            hoaDonChiTiet.setMssp(String.valueOf(sanPham.getMssp()));
                            hoaDonChiTiet.setMshd(mahd);
                            hoaDonChiTiet.setSoluong(soLuong);
                            hoaDonChiTiet.setDongia(donGia);
                            long kqCT = daoHoaDonCT.insertHoaDonCT(hoaDonChiTiet);
                            if (kqCT > 0) {
                                SanPham sanPham1 = daoSanPham.getID(hoaDonChiTiet.getMssp());
                                sanPham1.setTrangthai(0);
                                long sp = daoSanPham.updateSanPham(sanPham1);
                                if (sp > 0) {
                                    Toast.makeText(getApplicationContext(), "Thêm thành công hóa đơn", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Cap nhat trang thai that bai", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Thêm thất bại hóa đơn", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}