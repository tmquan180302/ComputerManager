package com.poly.computermanager.ui.khachhang;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.poly.computermanager.dao.DAOKhachHang;
import com.poly.computermanager.model.KhachHang;
import com.poly.computermanager.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityEditKhachHang extends AppCompatActivity {
    TextInputEditText ed_editMaKhachHang, ed_editTenKhachHang, ed_editSdtKhachHang, ed_editDiaChiKhachHang;
    RadioGroup rdg_gioitinh;
    RadioButton rdo_editNam, rdo_editNu;
    DAOKhachHang daoKhachHang;
    ArrayList<KhachHang> listKH;
    String ma;
    KhachHang khachHang;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_khach_hang);
        addControl();
    }

    private void addControl() {
        ed_editMaKhachHang = findViewById(R.id.ed_editMaKhachHang);
        ed_editTenKhachHang = findViewById(R.id.ed_editTenKhachHang);
        ed_editSdtKhachHang = findViewById(R.id.ed_editSdtKhachHang);
        ed_editDiaChiKhachHang = findViewById(R.id.ed_editDiaChiKhachHang);
        rdg_gioitinh = findViewById(R.id.rdg_gioitinh);
        rdo_editNam = findViewById(R.id.rdo_editNam);
        rdo_editNu = findViewById(R.id.rdo_editNu);
        toolbar = findViewById(R.id.tb_editKH);
        daoKhachHang = new DAOKhachHang(this);
        listKH = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cập nhật khách hàng");
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
        Intent intent = getIntent();
        ma = intent.getStringExtra("ma");
        khachHang = daoKhachHang.getID(ma);
        ed_editMaKhachHang.setText(ma);
        ed_editTenKhachHang.setText(khachHang.getHoten());
        ed_editSdtKhachHang.setText(khachHang.getSdt());
        ed_editDiaChiKhachHang.setText(khachHang.getDiachi());
        if (khachHang.getGioitinh() == 0) {
            rdo_editNam.setChecked(true);
        } else if (khachHang.getGioitinh() == 1) {
            rdo_editNu.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    public boolean check() {
        String makh = ed_editMaKhachHang.getText().toString();
        String tenkh = ed_editTenKhachHang.getText().toString();
        String sdt = ed_editSdtKhachHang.getText().toString();
        String diachi = ed_editDiaChiKhachHang.getText().toString();
        Pattern p1 = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
        Matcher m1 = p1.matcher(ed_editMaKhachHang.getText().toString());
        if (makh.isEmpty() || tenkh.isEmpty() || sdt.isEmpty() || diachi.isEmpty()) {
            Toast.makeText(this, "Không được bỏ trống thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }else if (m1.find() == false) {
            Toast.makeText(this, "Kí tự đầu mã khách hàng phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ed_editTenKhachHang.getText().toString().substring(0,1).toUpperCase().equals(ed_editTenKhachHang.getText().toString().substring(0,1))) {
            Toast.makeText(this, "Kí tự đầu tên khách hàng phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (sdt.length() < 10 || sdt.length() > 10) {
            Toast.makeText(this, "Số điện thoại phải đủ 10 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                if (check()) {
                    khachHang.setMskh(ed_editMaKhachHang.getText().toString());
                    khachHang.setHoten(ed_editTenKhachHang.getText().toString());
                    khachHang.setSdt(ed_editSdtKhachHang.getText().toString());
                    int rdog = rdg_gioitinh.getCheckedRadioButtonId();
                    if (rdog == R.id.rdo_editNam) {
                        khachHang.setGioitinh(0);
                    } else if (rdog == R.id.rdo_editNu) {
                        khachHang.setGioitinh(1);
                    }
                    khachHang.setDiachi(ed_editDiaChiKhachHang.getText().toString());
                    long result = daoKhachHang.updateKhachHang(khachHang);
                    if (result > 0) {
                        listKH.clear();
                        listKH.addAll(daoKhachHang.getAll());
                        Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}