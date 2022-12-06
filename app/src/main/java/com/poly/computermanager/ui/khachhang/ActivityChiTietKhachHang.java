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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.poly.computermanager.dao.DAOKhachHang;
import com.poly.computermanager.model.KhachHang;
import com.poly.computermanager.R;

public class ActivityChiTietKhachHang extends AppCompatActivity {
    DAOKhachHang daoKhachHang;
    KhachHang khachHang;
    TextView tv_tenKhachHangCT,tv_sdtKhachHangCT,tv_gtKhachHangCT,tv_diachiKhachHangCT;
    String ma;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_khach_hang);
        tv_diachiKhachHangCT = findViewById(R.id.tv_diachiKhachHangCT);
        tv_tenKhachHangCT = findViewById(R.id.tv_tenKhachHangCT);
        tv_sdtKhachHangCT = findViewById(R.id.tv_sdtKhachHangCT);
        tv_gtKhachHangCT = findViewById(R.id.tv_gtKhachHangCT);
        toolbar = findViewById(R.id.tb_chitietKH);
        daoKhachHang = new DAOKhachHang(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getData();

    }

    private void getData() {
        Intent intent = getIntent();
        ma = intent.getStringExtra("makh");
        toolbar.setTitle(ma);
        khachHang = daoKhachHang.getID(ma);
        tv_tenKhachHangCT.setText(khachHang.getHoten());
        tv_sdtKhachHangCT.setText(khachHang.getSdt());
        tv_diachiKhachHangCT.setText(khachHang.getDiachi());
        if (khachHang.getGioitinh()==0){
            tv_gtKhachHangCT.setText("Nam");
        }else if (khachHang.getGioitinh()==1){
            tv_gtKhachHangCT.setText("Nữ");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(ActivityChiTietKhachHang.this,ActivityEditKhachHang.class);
                intent.putExtra("ma",ma);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}