package com.poly.computermanager.ui.nhanvien;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amulyakhare.textdrawable.TextDrawable;
import com.poly.computermanager.dao.DAONhanVien;
import com.poly.computermanager.model.NhanVien;
import com.poly.computermanager.R;

import java.util.Random;

public class ActivityChiTietNhanVien extends AppCompatActivity {
    TextView tv_tennhanvienCT, tv_passnhanvienCT, tv_sdtnhanvienCT, tv_emailnhanvienCT;
    ImageView img_anhChiTietNhanVien;
    DAONhanVien daoNhanVien;
    NhanVien nhanVien;
    String manv;
    Button btn_doimatkhau;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_nhan_vien);
        tv_tennhanvienCT = findViewById(R.id.tv_tennhanvienCT);
        tv_passnhanvienCT = findViewById(R.id.tv_passnhanvienCT);
        tv_sdtnhanvienCT = findViewById(R.id.tv_sdtnhanvienCT);
        tv_emailnhanvienCT = findViewById(R.id.tv_emailnhanvienCT);
        btn_doimatkhau = findViewById(R.id.btn_doimatkhau);
        img_anhChiTietNhanVien = findViewById(R.id.img_anhChiTietNhanVien);
        toolbar = findViewById(R.id.tb_chitietNV);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin nhân viên");
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
        daoNhanVien = new DAONhanVien(this);
        getData();
        btn_doimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), ActivityEditNhanVien.class);
                startActivity(intent1);
            }
        });
    }
    private void getData(){
        SharedPreferences preferences = getSharedPreferences("rememberLogin", MODE_PRIVATE);
        String taiKhoan = preferences.getString("user", "");
        nhanVien = daoNhanVien.getID(taiKhoan);
        tv_tennhanvienCT.setText(nhanVien.getHoten());
        tv_passnhanvienCT.setText(nhanVien.getPassword());
        tv_sdtnhanvienCT.setText(nhanVien.getSdt());
        tv_emailnhanvienCT.setText(nhanVien.getEmail());
        if (nhanVien.getHinhanh() == null) {
            TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(120).height(120).endConfig().buildRect(nhanVien.getHoten().substring(0, 1).toUpperCase(), getRandomColor());
            img_anhChiTietNhanVien.setImageDrawable(textDrawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(nhanVien.getHinhanh(), 0, nhanVien.getHinhanh().length);
            img_anhChiTietNhanVien.setImageBitmap(bitmap);
        }
    }
    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(ActivityChiTietNhanVien.this, ActivityEditNhanVien.class);
                intent.putExtra("manv",manv );
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}