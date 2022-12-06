package com.poly.computermanager.ui.nhanvien;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.poly.computermanager.dao.DAONhanVien;
import com.poly.computermanager.model.NhanVien;
import com.poly.computermanager.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ActivityEditNhanVien extends AppCompatActivity {
    TextInputEditText ed_editmaNhanVien, ed_editPasswordOLD, ed_editPasswordNEW, ed_editRepass;
    DAONhanVien daoNhanVien;
    ArrayList<NhanVien> listNhanVien;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nhan_vien);
        addControl();
        addEvent();
    }


    private void addControl() {

        ed_editmaNhanVien = findViewById(R.id.ed_editmaNhanVien);
        ed_editPasswordOLD = findViewById(R.id.ed_editPasswordOLD);
        ed_editPasswordNEW = findViewById(R.id.ed_editPasswordNEW);

        ed_editRepass = findViewById(R.id.ed_editRePasswordNEW);
        toolbar = findViewById(R.id.tb_editNV);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đổi mật khẩu");
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
        daoNhanVien = new DAONhanVien(ActivityEditNhanVien.this);
        listNhanVien = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addEvent() {
        SharedPreferences preferences = getSharedPreferences("rememberLogin", MODE_PRIVATE);
        String taiKhoan = preferences.getString("user", "");
        ed_editmaNhanVien.setText(taiKhoan);
        ed_editmaNhanVien.setEnabled(false);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:

                if (validate()) {
                    SharedPreferences preferences = getSharedPreferences("rememberLogin", MODE_PRIVATE);
                    String taiKhoan = preferences.getString("user", "");
                    NhanVien nhanVien1 = daoNhanVien.getID(taiKhoan);
                    nhanVien1.setPassword(ed_editPasswordNEW.getText().toString());

                    long result = daoNhanVien.updateNhanVien(nhanVien1);
                    if (result > 0) {
                        listNhanVien.clear();
                        listNhanVien.addAll(daoNhanVien.getAll());
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

    public boolean validate() {

        if (ed_editPasswordNEW.getText().toString().length() == 0 && ed_editPasswordOLD.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ed_editPasswordOLD.getText().toString().length() < 6) {
            Toast.makeText(this, "Mật khẩu ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ed_editPasswordNEW.getText().toString().length() < 6) {
            Toast.makeText(this, "Mật khẩu ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ed_editRepass.getText().toString().length() < 6) {
            Toast.makeText(this, "Mật khẩu ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            SharedPreferences preferences = getSharedPreferences("rememberLogin", MODE_PRIVATE);
            String matkhau = preferences.getString("pass", "");
            String mkmoi = ed_editPasswordNEW.getText().toString();
            String remk = ed_editRepass.getText().toString();
            if (!ed_editPasswordOLD.getText().toString().equals(matkhau)) {
                Toast.makeText(getApplicationContext(), "Mật khẩu cũ sai", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!mkmoi.equals(remk)) {
                Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        return true;
    }
}