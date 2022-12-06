package com.example.computermanage.ui.loaisanpham;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.computermanage.dao.DAOHang;
import com.example.computermanage.dao.DAOLoaiSanPham;
import com.example.computermanage.model.Hang;
import com.example.computermanage.model.LoaiSanPham;
import com.example.computermanage.R;

import java.util.Random;

public class ActivityChiTietLoaiSP extends AppCompatActivity {
    ImageView img_anhChiTietLoaiSP;
    TextView  tv_chitietTenLoaiSP;
    int mslsp;
    DAOLoaiSanPham daoLoaiSanPham;
    LoaiSanPham loaiSanPham;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_loai_sp);
        addControl();
        Intent intent = getIntent();
        mslsp = intent.getIntExtra("mslsp",0);
        getSupportActionBar().setTitle(mslsp+"");
        loaiSanPham = daoLoaiSanPham.getID(String.valueOf(mslsp));
        tv_chitietTenLoaiSP.setText(loaiSanPham.getTenlsp());
        if (loaiSanPham.getHinhanh() == null) {
            TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(120).height(120).endConfig().buildRect(loaiSanPham.getTenlsp().substring(0, 1).toUpperCase(), getRandomColor());
            img_anhChiTietLoaiSP.setImageDrawable(textDrawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(loaiSanPham.getHinhanh(), 0, loaiSanPham.getHinhanh().length);
            img_anhChiTietLoaiSP.setImageBitmap(bitmap);
        }
    }


    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private void addControl() {
        toolbar = findViewById(R.id.tb_chitietLSP);
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

        img_anhChiTietLoaiSP = findViewById(R.id.img_anhChiTietLoaiSP);
        tv_chitietTenLoaiSP = findViewById(R.id.tv_chitietTenLoaiSP);
        daoLoaiSanPham = new DAOLoaiSanPham(this);
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
            case R.id.home:
                this.finish();
                return true;
            case R.id.menu_edit:
                Intent intent = new Intent(ActivityChiTietLoaiSP.this, ActivityEditLoaiSP.class);
                intent.putExtra("malsp", mslsp);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        mslsp = intent.getIntExtra("mslsp",0);
        getSupportActionBar().setTitle(mslsp+"");
        loaiSanPham = daoLoaiSanPham.getID(String.valueOf(mslsp));
        tv_chitietTenLoaiSP.setText(loaiSanPham.getTenlsp());
        if (loaiSanPham.getHinhanh() == null) {
            TextDrawable textDrawable = TextDrawable.builder().beginConfig().width(120).height(120).endConfig().buildRect(loaiSanPham.getTenlsp().substring(0, 1).toUpperCase(), getRandomColor());
            img_anhChiTietLoaiSP.setImageDrawable(textDrawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(loaiSanPham.getHinhanh(), 0, loaiSanPham.getHinhanh().length);
            img_anhChiTietLoaiSP.setImageBitmap(bitmap);
        }
        super.onResume();
    }
}
