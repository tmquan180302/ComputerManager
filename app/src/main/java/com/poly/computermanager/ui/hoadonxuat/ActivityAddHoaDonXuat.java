package com.poly.computermanager.ui.hoadonxuat;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.poly.computermanager.dao.DAOHoaDon;
import com.poly.computermanager.dao.DAOHoaDonCT;
import com.poly.computermanager.dao.DAOKhachHang;
import com.poly.computermanager.dao.DAONhanVien;
import com.poly.computermanager.dao.DAOSanPham;
import com.poly.computermanager.model.HoaDon;
import com.poly.computermanager.model.HoaDonChiTiet;
import com.poly.computermanager.model.KhachHang;
import com.poly.computermanager.model.SanPham;
import com.poly.computermanager.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityAddHoaDonXuat extends AppCompatActivity {
    TextInputEditText ed_maHD_xuat, ed_ngayHD_xuat, ed_soluongHD_xuat,
            ed_dongiaHD_xuat, ed_thanhtienHD_xuat;
    Spinner spn_sanphamxuat, spn_khachhangxuat, spn_khuyenmaixuat;
    RadioButton rdo_khongbaohanh_HD_xuat, rdo_6thang_HD_xuat, RDO_12thang_HD_xuat, rdo_dathanhtoan_HD, rdo_chuathanhtoan_HD;
    RadioGroup rdg_editbaohanh;
    ArrayList<SanPham> listSanpham;
    ArrayList<HoaDon> listHoadon;
    ArrayList<KhachHang> listKhachhang;
    ArrayList<HoaDonChiTiet> listHoadonCT;
    DAOSanPham daoSanPham;
    DAOHoaDon daoHoaDon;
    DAONhanVien daoNhanVien;
    DAOHoaDonCT daoHoaDonCT;
    DAOKhachHang daoKhachHang;
    private String maNV;
    private int khuyenmai;
    private double thanhtien;
    private double dongia;
    private int soluong;
    private String maSP;
    private Spinner spnKM;
    ArrayAdapter sanphamadapter;
    ArrayAdapter khachhangadapter;
    DecimalFormat decimalFormat;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hoa_don_xuat);
        addConTrol();
        getMaNV();
        setDataSpinner();

    }

    private void addConTrol() {
        ed_maHD_xuat = findViewById(R.id.ed_maHD_xuat);
        ed_ngayHD_xuat = findViewById(R.id.ed_ngayHD_xuat);
        ed_soluongHD_xuat = findViewById(R.id.ed_soluongHD_xuat);
        ed_dongiaHD_xuat = findViewById(R.id.ed_dongiaHD_xuat);
        spn_khuyenmaixuat = findViewById(R.id.spn_khuyenmaixuat);
        ed_thanhtienHD_xuat = findViewById(R.id.ed_thanhtienHD_xuat);
        spn_sanphamxuat = findViewById(R.id.spn_sanphamxuat);
        spn_khachhangxuat = findViewById(R.id.spn_khachhangxuat);
        rdo_khongbaohanh_HD_xuat = findViewById(R.id.rdo_khongbaohanh_HD_xuat);
        rdo_6thang_HD_xuat = findViewById(R.id.rdo_6thang_HD_xuat);
        RDO_12thang_HD_xuat = findViewById(R.id.RDO_12thang_HD_xuat);
        rdo_dathanhtoan_HD = findViewById(R.id.rdo_dathanhtoan_HD);
        rdo_chuathanhtoan_HD = findViewById(R.id.rdo_chuathanhtoan_HD);
        rdg_editbaohanh = findViewById(R.id.rdg_editbaohanh);
        toolbar = findViewById(R.id.tb_addHDXuat);
        spnKM = findViewById(R.id.spn_khuyenmaixuat);
        daoSanPham = new DAOSanPham(this);
        daoHoaDon = new DAOHoaDon(this);
        daoHoaDonCT = new DAOHoaDonCT(this);
        daoNhanVien = new DAONhanVien(this);
        daoKhachHang = new DAOKhachHang(this);
        listSanpham = new ArrayList<>();
        listHoadon = new ArrayList<>();
        listKhachhang = new ArrayList<>();
        listHoadonCT = new ArrayList<>();
        listKhachhang = daoKhachHang.getAll();
        listSanpham = daoSanPham.getAllSPBan();
        ed_dongiaHD_xuat.setEnabled(false);
        rdo_6thang_HD_xuat.setChecked(true);
        rdo_khongbaohanh_HD_xuat.setChecked(true);
        rdo_dathanhtoan_HD.setChecked(true);
        sanphamadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listSanpham);
        spn_sanphamxuat.setAdapter(sanphamadapter);
        khachhangadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listKhachhang);
        spn_khachhangxuat.setAdapter(khachhangadapter);
        decimalFormat = new DecimalFormat("###,###,###");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm HD Xuất");
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

    public void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityAddHoaDonXuat.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                ed_ngayHD_xuat.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void eventDialog() {
        ed_ngayHD_xuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
        ed_soluongHD_xuat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Thanhtien();
                if (!b) {
                    if (Thanhtien() != 0) {
                        ed_thanhtienHD_xuat.setText(Thanhtien() + "");
                    } else {
                        ed_thanhtienHD_xuat.setText("Null");
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    private double Thanhtien() {
        try {
            double soluong = Double.parseDouble(ed_soluongHD_xuat.getText().toString());
            double dongia = Double.parseDouble(ed_dongiaHD_xuat.getText().toString());
            thanhtien = soluong * dongia;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thanhtien;
    }

    private void setDataSpinner() {
        spn_sanphamxuat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maSP = String.valueOf(listSanpham.get(position).getMssp());
                dongia = listSanpham.get(position).getGiatien();
                try {
                    soluong = Integer.parseInt(ed_soluongHD_xuat.getText().toString());
                } catch (NumberFormatException ex) { // handle your exception
                }
                ed_soluongHD_xuat.setText(String.valueOf(1));
                ed_dongiaHD_xuat.setText(dongia + "");
                thanhtien = dongia * soluong;
                ed_thanhtienHD_xuat.setText(thanhtien + "");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        eventDialog();
        setSpinnerKM();
    }
    private void setSpinnerKM() {
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(this, R.array.khuyenmai, R.layout.custom_item_sp);
        spAdapter.setDropDownViewResource(R.layout.custom_item_sp_drop_down);
        spnKM.setAdapter(spAdapter);
        spnKM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        khuyenmai = 1;
                        ed_thanhtienHD_xuat.setText(decimalFormat.format(thanhtien - (thanhtien * 0.05)) + " VNĐ");
                        break;
                    case 2:
                        khuyenmai = 2;
                        ed_thanhtienHD_xuat.setText(decimalFormat.format(thanhtien - (thanhtien * 0.1)) + " VNĐ");
                        break;
                    case 3:
                        khuyenmai = 3;
                        ed_thanhtienHD_xuat.setText(decimalFormat.format(thanhtien - (thanhtien * 0.15)) + " VNĐ");
                        break;
                    case 4:
                        khuyenmai = 4;
                        ed_thanhtienHD_xuat.setText(decimalFormat.format(thanhtien - (thanhtien * 0.2)) + " VNĐ");
                        break;
                    case 5:
                        khuyenmai = 5;
                        ed_thanhtienHD_xuat.setText(decimalFormat.format(thanhtien - (thanhtien * 0.25)) + " VNĐ");
                        break;
                    case 6:
                        khuyenmai = 6;
                        ed_thanhtienHD_xuat.setText(decimalFormat.format(thanhtien - (thanhtien * 0.3)) + " VNĐ");
                        break;
                    case 0:
                        ed_thanhtienHD_xuat.setText(decimalFormat.format(thanhtien * 1) + " VNĐ");
                        khuyenmai = 0;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean check() {
        String mahd = ed_maHD_xuat.getText().toString();
        String ngay = ed_ngayHD_xuat.getText().toString();
        String soluong = ed_soluongHD_xuat.getText().toString();
        Pattern p = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
        Matcher m = p.matcher(ed_maHD_xuat.getText().toString());
        if (mahd.isEmpty() || ngay.isEmpty() || soluong.isEmpty()) {
            Toast.makeText(this, "Không được bỏ trống thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (m.find() == false) {
            Toast.makeText(this, "Kí tự đầu mã hóa đơn phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (listSanpham.size() == 0) {
            Toast.makeText(this, "Chưa có sản phẩm!.\nVui lòng thêm sản phẩm trước!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (listKhachhang.size() == 0) {
            Toast.makeText(this, "Chưa có khách hàng, vui lòng thêm khách hàng trước", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(soluong) < 0) {
            Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
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
                    String mahd = ed_maHD_xuat.getText().toString();
                    String ngay = ed_ngayHD_xuat.getText().toString();
                    if (daoHoaDon.checkMaHD(mahd) > 0) {
                        Toast.makeText(this, "Mã hóa đơn đã tồn tại hệ thống", Toast.LENGTH_SHORT).show();
                    } else {
                        HoaDon hoaDon = new HoaDon();
                        hoaDon.setMshd(mahd);
                        hoaDon.setMsnv(maNV);
                        KhachHang khachHang = (KhachHang) spn_khachhangxuat.getSelectedItem();
                        hoaDon.setMskh(String.valueOf(khachHang.getMskh()));
                        hoaDon.setPhanloaiHD(1);
                        hoaDon.setNgaymua(ngay);
                        if (rdo_dathanhtoan_HD.isChecked()) {
                            hoaDon.setTrangthai(1);
                        } else if (rdo_chuathanhtoan_HD.isChecked()) {
                            hoaDon.setTrangthai(0);
                        }
                        long kq = daoHoaDon.insertHoaDon(hoaDon);
                        if (kq > 0) {
                            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                            SanPham sanPham1 = (SanPham) spn_sanphamxuat.getSelectedItem();
                            hoaDonChiTiet.setMssp(String.valueOf(sanPham1.getMssp()));
                            hoaDonChiTiet.setDongia(Double.parseDouble(ed_dongiaHD_xuat.getText().toString()));
                            hoaDonChiTiet.setSoluong(Integer.parseInt(ed_soluongHD_xuat.getText().toString()));
                            hoaDonChiTiet.setMshd(ed_maHD_xuat.getText().toString());
                            int rdgbaohanh = rdg_editbaohanh.getCheckedRadioButtonId();
                            if (rdgbaohanh == R.id.rdo_editkhongbaohanh_DH) {
                                hoaDonChiTiet.setBaohanh(0);
                            } else if (rdgbaohanh == R.id.rdo_6thang_HD_xuat) {
                                hoaDonChiTiet.setBaohanh(1);
                            } else if (rdgbaohanh == R.id.RDO_12thang_HD_xuat) {
                                hoaDonChiTiet.setBaohanh(2);
                            }
                            hoaDonChiTiet.setGiamgia(khuyenmai);
                            long kqCT = daoHoaDonCT.insertHoaDonCT(hoaDonChiTiet);
                            if (kqCT > 0) {
                                Toast.makeText(getApplicationContext(), "Thêm thành công hóa đơn", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(getApplicationContext(), "Thêm chi tiết hóa đơn thất bại ", Toast.LENGTH_SHORT).show();
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