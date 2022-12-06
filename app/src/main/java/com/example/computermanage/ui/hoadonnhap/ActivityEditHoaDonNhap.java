package com.example.computermanage.ui.hoadonnhap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.computermanage.dao.DAOHoaDon;
import com.example.computermanage.dao.DAOHoaDonCT;
import com.example.computermanage.dao.DAOSanPham;
import com.example.computermanage.model.HoaDon;
import com.example.computermanage.model.HoaDonChiTiet;
import com.example.computermanage.model.SanPham;
import com.example.computermanage.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityEditHoaDonNhap extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText ed_editmaHD_nhap, ed_editngayHD_nhap, ed_editsoluongHD_nhap, ed_editdongiaHD_nhap, ed_editthanhtienHD_nhap;
    Spinner spn_editsanpham;
    DAOHoaDon daoHoaDon;
    DAOHoaDonCT daoHoaDonCT;
    DAOSanPham daoSanPham;
    ArrayList<SanPham> listSanpham;
    DecimalFormat decimalFormat;
    String mahdnhap;
    double dongiahd;
    HoaDon hoaDon;
    SanPham sanPham;
    HoaDonChiTiet hoaDonChiTiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hoa_don_nhap);
        addControl();
        eventDialog();
        addEvent();
    }

    private void addControl() {
        toolbar = findViewById(R.id.tb_editHDNhap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cập nhật HD nhập");
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
        ed_editmaHD_nhap = findViewById(R.id.ed_editmaHD_nhap);
        ed_editngayHD_nhap = findViewById(R.id.ed_editngayHD_nhap);
        spn_editsanpham = findViewById(R.id.spn_editsanpham);
        ed_editsoluongHD_nhap = findViewById(R.id.ed_editsoluongHD_nhap);
        ed_editdongiaHD_nhap = findViewById(R.id.ed_editdongiaHD_nhap);
        ed_editthanhtienHD_nhap = findViewById(R.id.ed_editthanhtienHD_nhap);
        daoHoaDon = new DAOHoaDon(this);
        daoHoaDonCT = new DAOHoaDonCT(this);
        daoSanPham = new DAOSanPham(this);
        decimalFormat = new DecimalFormat("###,###,###");
        listSanpham = new ArrayList<>();
        listSanpham = daoSanPham.getAll();
        ArrayAdapter adapter = new ArrayAdapter(ActivityEditHoaDonNhap.this, android.R.layout.simple_spinner_dropdown_item, listSanpham);
        spn_editsanpham.setAdapter(adapter);
        spn_editsanpham.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dongiahd = listSanpham.get(position).getGiatien();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void addEvent() {

        Intent intent = getIntent();
        mahdnhap = intent.getStringExtra("mahdnhap");
        hoaDon = daoHoaDon.getID(mahdnhap);
        hoaDonChiTiet = daoHoaDonCT.getID(hoaDon.getMshd());
        sanPham = daoSanPham.getID(hoaDonChiTiet.getMssp());
        int vitri = -1;
        for (int i = 0; i < listSanpham.size(); i++) {
            if (listSanpham.get(i).getMssp().equals(hoaDonChiTiet.getMssp())) {
                vitri = i;
                break;
            }
        }
        spn_editsanpham.setSelection(vitri);
        ed_editmaHD_nhap.setText(hoaDon.getMshd());
        ed_editngayHD_nhap.setText(hoaDon.getNgaymua());
        ed_editsoluongHD_nhap.setText(hoaDonChiTiet.getSoluong() + "");
        ed_editdongiaHD_nhap.setText(hoaDonChiTiet.getDongia() + "");
        ed_editthanhtienHD_nhap.setText((hoaDonChiTiet.getSoluong() * hoaDonChiTiet.getDongia()) + "");

    }

    private double Thanhtien() {
        double thanhtien = 0;
        try {
            double soluong = Double.parseDouble(ed_editsoluongHD_nhap.getText().toString());
            double dongia = Double.parseDouble(ed_editdongiaHD_nhap.getText().toString());
            thanhtien = soluong * dongia;
        } catch (Exception e) {
            thanhtien = 0;
        }
        return thanhtien;
    }

    public void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityEditHoaDonNhap.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                ed_editngayHD_nhap.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void eventDialog() {
        ed_editngayHD_nhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
        ed_editdongiaHD_nhap.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Thanhtien() != 0) {
                        ed_editthanhtienHD_nhap.setText(Thanhtien() + "");
                    } else {
                        ed_editthanhtienHD_nhap.setText("Null");
                    }
                }
            }
        });

        ed_editsoluongHD_nhap.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (Thanhtien() != 0) {
                        ed_editthanhtienHD_nhap.setText(Thanhtien() + "");
                    } else {
                        ed_editthanhtienHD_nhap.setText("Null");
                    }
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean check() {
        String mahd = ed_editmaHD_nhap.getText().toString();
        String ngay = ed_editngayHD_nhap.getText().toString();
        String soluong = ed_editsoluongHD_nhap.getText().toString();
        String dongia = ed_editdongiaHD_nhap.getText().toString();
        Pattern p = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
        Matcher m = p.matcher(ed_editmaHD_nhap.getText().toString());
        if (mahd.isEmpty() || ngay.isEmpty() || soluong.isEmpty() || dongia.isEmpty()) {
            Toast.makeText(this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (m.find() == false) {
            Toast.makeText(this, "Kí tự đầu phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(soluong) < 0) {
            Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Double.parseDouble(dongia) < 0 || Double.parseDouble(dongia) > dongiahd) {
            Toast.makeText(this, "Đơn giá phải lớn hơn 0 và nhỏ hơn đơn giá sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                if (check()) {
                    String mahd = ed_editmaHD_nhap.getText().toString();
                    String ngay = ed_editngayHD_nhap.getText().toString();
                    int soLuong = Integer.parseInt(ed_editsoluongHD_nhap.getText().toString());
                    double donGia = Double.parseDouble(ed_editdongiaHD_nhap.getText().toString());

                    hoaDon.setMshd(mahd);
                    hoaDon.setNgaymua(ngay);

                    long kq = daoHoaDon.updateHoaDon(hoaDon);
                    if (kq > 0) {
                        sanPham = (SanPham) spn_editsanpham.getSelectedItem();
                        hoaDonChiTiet.setMssp(String.valueOf(sanPham.getMssp()));
                        hoaDonChiTiet.setMshd(mahd);
                        hoaDonChiTiet.setSoluong(soLuong);
                        hoaDonChiTiet.setDongia(donGia);
                        long kqCT = daoHoaDonCT.updateHoaDonCT(hoaDonChiTiet);
                        if (kqCT > 0) {
                            SanPham sanPham1 = daoSanPham.getID(hoaDonChiTiet.getMssp());
                            sanPham1.setTrangthai(0);
                            long sp = daoSanPham.updateSanPham(sanPham1);
                            if (sp > 0) {
                                Toast.makeText(getApplicationContext(), "Cập nhật thành công hóa đơn", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(getApplicationContext(), "Cap nhat trang thai that bai", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Cập nhật thất bại hóa đơn", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }


        }
        return super.onOptionsItemSelected(item);
    }
}