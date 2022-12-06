package com.poly.computermanager.ui.hoadonxuat;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.poly.computermanager.dao.DAOHoaDon;
import com.poly.computermanager.dao.DAOHoaDonCT;
import com.poly.computermanager.dao.DAOKhachHang;
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

public class ActivityEditHoaDonXuat extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText ed_editmaHD_xuat, ed_editngayHD,
            ed_editsoluongHD_xuat, ed_editdongiaHD_xuat, ed_editthanhtienHD;
    Spinner spn_editSPXuat, spn_editKHXuat, spn_editKMXuat;
    RadioButton rdo_editdathanhtoan_HD, rdo_editchuathanhtoan_HD, rdo_editkhongbaohanh_DH, rdo_edit6thang_HD, rdo_edit12thang_HD;
    DAOHoaDon daoHoaDon;
    DAOHoaDonCT daoHoaDonCT;
    DAOSanPham daoSanPham;
    DAOKhachHang daoKhachHang;
    ArrayList<SanPham> listSanpham = new ArrayList<>();
    ArrayList<KhachHang> listKhachhang;
    String mahdxuat;
    double thanhtien;
    int khuyenmai;
    private String maSP;
    private double dongia;
    private int soluong;
    HoaDon hoaDon;
    SanPham sanPham;
    KhachHang khachHang;
    HoaDonChiTiet hoaDonChiTiet;
    DecimalFormat decimalFormat;
    private ArrayList<HoaDonChiTiet> listHDCT;
    private ArrayList<HoaDon> listHD;
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hoa_don_xuat);
        addControl();
        setData();
        setDataSpinner();
    }


    private void addControl() {
        toolbar = findViewById(R.id.tb_editHDXuat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Cập nhật HD Xuất");
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ed_editmaHD_xuat = findViewById(R.id.ed_editmaHD_xuat);
        ed_editngayHD = findViewById(R.id.ed_editngayHD);
        ed_editsoluongHD_xuat = findViewById(R.id.ed_editsoluongHD_xuat);
        ed_editdongiaHD_xuat = findViewById(R.id.ed_editdongiaHD_xuat);
        ed_editthanhtienHD = findViewById(R.id.ed_editthanhtienHD);
        spn_editSPXuat = findViewById(R.id.spn_editSPXuat);
        spn_editKHXuat = findViewById(R.id.spn_editKHXuat);
        spn_editKMXuat = findViewById(R.id.spn_editKMXuat);
        rdo_editdathanhtoan_HD = findViewById(R.id.rdo_editdathanhtoan_HD);
        rdo_editchuathanhtoan_HD = findViewById(R.id.rdo_editchuathanhtoan_HD);
        rdo_editkhongbaohanh_DH = findViewById(R.id.rdo_editkhongbaohanh_DH);
        rdo_edit6thang_HD = findViewById(R.id.rdo_edit6thang_HD);
        rdo_edit12thang_HD = findViewById(R.id.rdo_edit12thang_HD);
        daoHoaDon = new DAOHoaDon(this);
        daoHoaDonCT = new DAOHoaDonCT(this);
        daoKhachHang = new DAOKhachHang(this);
        daoSanPham = new DAOSanPham(this);
        listHDCT = new ArrayList<>();
        listHD = new ArrayList<>();
        listSanpham = daoSanPham.getAllSPBan();
        ArrayAdapter adapterSanpham = new ArrayAdapter(ActivityEditHoaDonXuat.this, android.R.layout.simple_spinner_dropdown_item, listSanpham);
        spn_editSPXuat.setAdapter(adapterSanpham);

        listKhachhang = daoKhachHang.getAll();
        ArrayAdapter adapterKhachhang = new ArrayAdapter(ActivityEditHoaDonXuat.this, android.R.layout.simple_spinner_dropdown_item, listKhachhang);
        spn_editKHXuat.setAdapter(adapterKhachhang);
        decimalFormat = new DecimalFormat("###,###,###");

    }

    private double Thanhtien() {
        try {
            double soluong = Double.parseDouble(ed_editsoluongHD_xuat.getText().toString());
            double dongia = Double.parseDouble(ed_editdongiaHD_xuat.getText().toString());
            thanhtien = soluong * dongia;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thanhtien;
    }

    public void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityEditHoaDonXuat.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                ed_editngayHD.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void addEvent() {
        ed_editngayHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
        ed_editsoluongHD_xuat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Thanhtien();
                if (!b) {
                    if (Thanhtien() != 0) {
                        ed_editthanhtienHD.setText(decimalFormat.format(Thanhtien()) + "");
                    } else {
                        ed_editthanhtienHD.setText("Null");
                    }
                }
            }
        });
    }

    private void setDataSpinner() {
        spn_editSPXuat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maSP = String.valueOf(listSanpham.get(position).getMssp());
                dongia = listSanpham.get(position).getGiatien();
                try {
                    soluong = Integer.parseInt(ed_editsoluongHD_xuat.getText().toString());
                } catch (NumberFormatException ex) { // handle your exception

                }
                ed_editsoluongHD_xuat.setText(soluong + "");
                ed_editdongiaHD_xuat.setText(dongia + "");
                thanhtien = dongia * soluong;
                ed_editthanhtienHD.setText(thanhtien + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addEvent();

    }

    private void setData() {
        setSpinnerKM();
        Intent intent = getIntent();
        mahdxuat = intent.getStringExtra("mahoaddonxuat");
        hoaDon = daoHoaDon.getID(mahdxuat);
        ed_editmaHD_xuat.setText(hoaDon.getMshd());
        ed_editngayHD.setText(hoaDon.getNgaymua());
        hoaDonChiTiet = daoHoaDonCT.getID(hoaDon.getMshd());
        sanPham = daoSanPham.getID(hoaDonChiTiet.getMssp());
        int vitri = -1;
        for (int i = 0; i < listSanpham.size(); i++) {
            if (listSanpham.get(i).getMssp().equals(hoaDonChiTiet.getMssp())) {
                vitri = i;
                break;
            }
        }
        spn_editSPXuat.setSelection(vitri);
        khachHang = daoKhachHang.getID(hoaDon.getMskh());
        int vitri1 = -1;
        for (int i = 0; i < listKhachhang.size(); i++) {
            if (listKhachhang.get(i).getMskh().equals(hoaDon.getMskh())) {
                vitri1 = i;
                break;
            }
        }
        spn_editKHXuat.setSelection(vitri1);
        ed_editsoluongHD_xuat.setText(hoaDonChiTiet.getSoluong() + "");
        ed_editdongiaHD_xuat.setText(decimalFormat.format(hoaDonChiTiet.getDongia()) + "");
        switch (hoaDonChiTiet.getGiamgia()) {
            case 0:
                spn_editKMXuat.setSelection(0);
                break;
            case 1:
                spn_editKMXuat.setSelection(1);
                break;
            case 2:
                spn_editKMXuat.setSelection(2);
                break;
            case 3:
                spn_editKMXuat.setSelection(3);
                break;
            case 4:
                spn_editKMXuat.setSelection(4);
                break;
            case 5:
                spn_editKMXuat.setSelection(5);
                break;
            case 6:
                spn_editKMXuat.setSelection(6);
                break;
        }
        switch (hoaDonChiTiet.getBaohanh()) {
            case 0:
                rdo_editkhongbaohanh_DH.setChecked(true);
                break;
            case 1:
                rdo_edit6thang_HD.setChecked(true);
                break;
            case 2:
                rdo_edit12thang_HD.setChecked(true);
                break;
        }
        switch (hoaDon.getTrangthai()) {
            case 0:
                rdo_editchuathanhtoan_HD.setChecked(true);
                break;
            case 1:
                rdo_editdathanhtoan_HD.setChecked(true);
                break;

        }


    }

    private void setSpinnerKM() {
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(this, R.array.khuyenmai, R.layout.custom_item_sp);
        spAdapter.setDropDownViewResource(R.layout.custom_item_sp_drop_down);
        spn_editKMXuat.setAdapter(spAdapter);
        spn_editKMXuat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        khuyenmai = 1;
                        ed_editthanhtienHD.setText(decimalFormat.format(thanhtien - (thanhtien * 0.05)));
                        break;
                    case 2:
                        khuyenmai = 2;
                        ed_editthanhtienHD.setText(decimalFormat.format(thanhtien - (thanhtien * 0.1)));
                        break;
                    case 3:
                        khuyenmai = 3;
                        ed_editthanhtienHD.setText(decimalFormat.format(thanhtien - (thanhtien * 0.15)));
                        break;
                    case 4:
                        khuyenmai = 4;
                        ed_editthanhtienHD.setText(decimalFormat.format(thanhtien - (thanhtien * 0.2)));
                        break;
                    case 5:
                        khuyenmai = 5;
                        ed_editthanhtienHD.setText(decimalFormat.format(thanhtien - (thanhtien * 0.25)));
                        break;
                    case 6:
                        khuyenmai = 6;
                        ed_editthanhtienHD.setText(decimalFormat.format(thanhtien - (thanhtien * 0.3)));
                        break;
                    case 0:
                        ed_editthanhtienHD.setText(decimalFormat.format(thanhtien * 1));
                        khuyenmai = 0;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public boolean check() {
        String mahd = ed_editmaHD_xuat.getText().toString();
        String ngay = ed_editngayHD.getText().toString();
        String soluong = ed_editsoluongHD_xuat.getText().toString();
        Pattern p = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
        Matcher m = p.matcher(ed_editmaHD_xuat.getText().toString());
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
            case R.id.menu_done:
                if (check()) {
                    String mahd = ed_editmaHD_xuat.getText().toString();
                    String ngay = ed_editngayHD.getText().toString();
                    hoaDon.setMshd(mahd);
                    khachHang = (KhachHang) spn_editKHXuat.getSelectedItem();
                    hoaDon.setMskh(String.valueOf(khachHang.getMskh()));
                    hoaDon.setNgaymua(ngay);
                    if (rdo_editdathanhtoan_HD.isChecked()) {
                        hoaDon.setTrangthai(1);
                    }
                    if (rdo_editchuathanhtoan_HD.isChecked()) {
                        hoaDon.setTrangthai(0);
                    }
                    long kq = daoHoaDon.updateHoaDon(hoaDon);

                    if (kq > 0) {
                        sanPham = (SanPham) spn_editSPXuat.getSelectedItem();
                        hoaDonChiTiet.setMssp(String.valueOf(sanPham.getMssp()));
                        hoaDonChiTiet.setDongia(Double.parseDouble(ed_editdongiaHD_xuat.getText().toString()));
                        hoaDonChiTiet.setSoluong(Integer.parseInt(ed_editsoluongHD_xuat.getText().toString()));
                        hoaDonChiTiet.setMshd(hoaDon.getMshd());
                        if (rdo_editkhongbaohanh_DH.isChecked()) {
                            hoaDonChiTiet.setBaohanh(0);
                        } else if (rdo_edit6thang_HD.isChecked()) {
                            hoaDonChiTiet.setBaohanh(1);
                        } else if (rdo_edit12thang_HD.isChecked()) {
                            hoaDonChiTiet.setBaohanh(2);
                        }
                        hoaDonChiTiet.setGiamgia(khuyenmai);
                        long kqCT = daoHoaDonCT.updateHoaDonCT(hoaDonChiTiet);
                        if (kqCT > 0) {
                            Toast.makeText(getApplicationContext(), "Cập nhật  thành công hóa đơn", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(getApplicationContext(), "Cập nhật chi tiết hóa đơn thất bại ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
        }


        return super.onOptionsItemSelected(item);

    }

}