package com.poly.computermanager.ui.khachhang;

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

public class ActivityAddKhachHang extends AppCompatActivity {
    TextInputEditText ed_maKhachHang, ed_tenKhachHang, ed_sdtKhachHang, ed_diaChiKhachHang;
    RadioButton rdo_nam, rdo_nu;
    RadioGroup rdg_gioiTinh;
    DAOKhachHang daoKhachHang;
    ArrayList<KhachHang> listKH;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_khach_hang);
        addControl();
    }

    private void addControl() {

        ed_maKhachHang = findViewById(R.id.ed_maKhachHang);
        ed_tenKhachHang = findViewById(R.id.ed_tenKhachHang);
        ed_sdtKhachHang = findViewById(R.id.ed_sdtKhachHang);
        ed_diaChiKhachHang = findViewById(R.id.ed_diaChiKhachHang);
        rdg_gioiTinh = findViewById(R.id.rdg_gioiTinh);
        rdo_nam = findViewById(R.id.rdo_nam);
        rdo_nu = findViewById(R.id.rdo_nu);
        toolbar = findViewById(R.id.tb_addKH);
        daoKhachHang = new DAOKhachHang(this);
        listKH = new ArrayList<>();
        rdo_nam.setChecked(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm khách hàng");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    public boolean check() {
        String makh = ed_maKhachHang.getText().toString();
        String tenkh = ed_tenKhachHang.getText().toString();
        String sdt = ed_sdtKhachHang.getText().toString();
        String diachi = ed_diaChiKhachHang.getText().toString();
        Pattern p1 = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
        Matcher m1 = p1.matcher(ed_maKhachHang.getText().toString());
        if (makh.isEmpty() || tenkh.isEmpty() || sdt.isEmpty() || diachi.isEmpty()) {
            Toast.makeText(this, "Không được bỏ trống thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (m1.find() == false) {
            Toast.makeText(this, "Kí tự đầu mã khách hàng phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ed_tenKhachHang.getText().toString().substring(0,1).toUpperCase().equals(ed_tenKhachHang.getText().toString().substring(0,1))) {
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
            case R.id.menu_reset:
                ed_maKhachHang.setText("");
                ed_tenKhachHang.setText("");
                ed_sdtKhachHang.setText("");
                ed_diaChiKhachHang.setText("");
                rdo_nam.setChecked(true);
                return true;
            case R.id.menu_save:
                if (check()) {
                    KhachHang khachHang = new KhachHang();
                    khachHang.setMskh(ed_maKhachHang.getText().toString());
                    khachHang.setHoten(ed_tenKhachHang.getText().toString());
                    khachHang.setSdt(ed_sdtKhachHang.getText().toString());
                    khachHang.setDiachi(ed_diaChiKhachHang.getText().toString());
                    int checkrdo = rdg_gioiTinh.getCheckedRadioButtonId();
                    if (checkrdo == R.id.rdo_nam) {
                        khachHang.setGioitinh(0);
                    } else if (checkrdo == R.id.rdo_nu) {
                        khachHang.setGioitinh(1);
                    }
                    long kq = daoKhachHang.insertKhachHang(khachHang);
                    if (kq > 0) {
                        listKH.clear();
                        listKH.addAll(daoKhachHang.getAll());
                        onBackPressed();
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}