package com.poly.computermanager.ui.hang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.poly.computermanager.dao.DAOHang;
import com.poly.computermanager.dao.DAOLoaiSanPham;
import com.poly.computermanager.model.Hang;
import com.poly.computermanager.R;
import com.poly.computermanager.model.LoaiSanPham;

import java.util.Random;

public class ActivityChiTietHang extends AppCompatActivity {
    TextView tv_tenHangCT,tv_maHangCT,tv_tenLoaiHangCT;
    ImageView img_anhHangCT;
    DAOHang daoHang;
    DAOLoaiSanPham daoLoaiSanPham;
    Hang hang;
    String mahang;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hang);
        tv_tenHangCT = findViewById(R.id.tv_tenHangCT);
        tv_maHangCT = findViewById(R.id.tv_maHangCT);
        tv_tenLoaiHangCT = findViewById(R.id.tv_tenLoaiHangCT);
        toolbar = findViewById(R.id.tb_chitietHang);
        img_anhHangCT = findViewById(R.id.img_anhHangCT);
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
        daoHang = new DAOHang(this);
        daoLoaiSanPham=new DAOLoaiSanPham(this);

        Intent intent = getIntent();
        mahang = intent.getStringExtra("mahang");
        getSupportActionBar().setTitle(mahang);
        hang = daoHang.getID(mahang);
        LoaiSanPham loaiSanPham=daoLoaiSanPham.getID(String.valueOf(hang.getMslsp()));
        tv_tenLoaiHangCT.setText(loaiSanPham.getTenlsp());

        if (hang.getHinhanh() == null){
            TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(120).height(120).endConfig().buildRect(hang.getTenhang().substring(0, 1).toUpperCase(), getRandomColor());
            img_anhHangCT.setImageDrawable(textDrawable);
        }else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(hang.getHinhanh(), 0, hang.getHinhanh().length);
            img_anhHangCT.setImageBitmap(bitmap);
        }
        tv_tenHangCT.setText(hang.getTenhang());
        tv_maHangCT.setText(hang.getMshang());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit,menu);
        return true;
    }
    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        mahang = intent.getStringExtra("mahang");
        getSupportActionBar().setTitle(mahang);
        hang = daoHang.getID(mahang);
        LoaiSanPham loaiSanPham=daoLoaiSanPham.getID(String.valueOf(hang.getMslsp()));
        tv_tenLoaiHangCT.setText(loaiSanPham.getTenlsp());

        if (hang.getHinhanh() == null){
            TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(120).height(120).endConfig().buildRect(hang.getTenhang().substring(0, 1).toUpperCase(), getRandomColor());
            img_anhHangCT.setImageDrawable(textDrawable);
        }else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(hang.getHinhanh(), 0, hang.getHinhanh().length);
            img_anhHangCT.setImageBitmap(bitmap);
        }
        tv_tenHangCT.setText(hang.getTenhang());
        tv_maHangCT.setText(hang.getMshang());
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit:
                Intent intent = new Intent(ActivityChiTietHang.this,ActivityEditHang.class);
                intent.putExtra("mahang",mahang);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}