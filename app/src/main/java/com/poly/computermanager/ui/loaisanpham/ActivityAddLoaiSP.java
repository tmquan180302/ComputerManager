package com.poly.computermanager.ui.loaisanpham;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.poly.computermanager.dao.DAOHang;
import com.poly.computermanager.dao.DAOLoaiSanPham;
import com.poly.computermanager.model.Hang;
import com.poly.computermanager.model.LoaiSanPham;
import com.poly.computermanager.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityAddLoaiSP extends AppCompatActivity {
    ImageView img_anhLoaiSP;
    TextInputEditText ed_maLoaiSP, ed_tenLoaiSP;
    Bitmap bitmap, bitmap1;
    byte[] image;
    LinearLayout linearCamera, linearGallery;
    ActivityResultLauncher<Intent> launcherCamera;
    ActivityResultLauncher<Intent> launcherGallery;
    DAOLoaiSanPham daoLoaiSanPham;
    ArrayList<LoaiSanPham> listLSP;
    DAOHang daoHang;
    ArrayList<Hang> listH;
    LoaiSanPham loaiSanPham;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loai_sp);
        addControl();
        addEvent();
    }


    private void addControl() {

        img_anhLoaiSP = findViewById(R.id.img_anhLoaiSP);
        ed_maLoaiSP = findViewById(R.id.ed_maLoaiSP);
        ed_tenLoaiSP = findViewById(R.id.ed_tenLoaiSP);
        toolbar = findViewById(R.id.tb_addLSP);
        daoHang = new DAOHang(this);
        daoLoaiSanPham = new DAOLoaiSanPham(this);
        listLSP = new ArrayList<>();
        listH = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm loại sản phẩm");
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

    private void addEvent() {
        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    img_anhLoaiSP.setImageBitmap(bitmap);
                }
            }
        });
        launcherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        img_anhLoaiSP.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bitmap = ((BitmapDrawable) img_anhLoaiSP.getDrawable()).getBitmap();
        img_anhLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });
    }

    private void openBottomSheet() {
        View viewDialog = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);
        linearCamera = viewDialog.findViewById(R.id.linearCamera);
        linearGallery = viewDialog.findViewById(R.id.linearGallery);

        linearCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launcherCamera.launch(intent);
                bottomSheetDialog.cancel();
            }
        });
        linearGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcherGallery.launch(intent);
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    public void convertImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img_anhLoaiSP.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        image = byteArray.toByteArray();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }


    public boolean check() {
        String tenlsp = ed_tenLoaiSP.getText().toString();
        if (tenlsp.isEmpty()) {
            Toast.makeText(this, "Không được bỏ trống thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }  else if (!ed_tenLoaiSP.getText().toString().substring(0,1).toUpperCase().equals(ed_tenLoaiSP.getText().toString().substring(0,1))) {
            Toast.makeText(this, "Kí tự đầu tên loại phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_reset:
                ed_maLoaiSP.setText("");
                ed_tenLoaiSP.setText("");
                Drawable drawable = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    drawable = getDrawable(R.drawable.image_defaut);
                }
                img_anhLoaiSP.setImageDrawable(drawable);
                return true;
            case R.id.menu_save:
                if (check()) {
                    loaiSanPham = new LoaiSanPham();
                    loaiSanPham.setTenlsp(ed_tenLoaiSP.getText().toString());
                    bitmap1 = ((BitmapDrawable) img_anhLoaiSP.getDrawable()).getBitmap();
                    if (bitmap != bitmap1) {
                        convertImage();
                        loaiSanPham.setHinhanh(image);
                    }

                    long kq = daoLoaiSanPham.insertLoaiSanPham(loaiSanPham);
                    if (kq > 0) {
                        listLSP.clear();
                        listLSP.addAll(daoLoaiSanPham.getAll());
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
