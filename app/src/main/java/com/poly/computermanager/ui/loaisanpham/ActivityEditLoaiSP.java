package com.poly.computermanager.ui.loaisanpham;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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

public class ActivityEditLoaiSP extends AppCompatActivity {
    TextInputEditText ed_editMaLoaiSP, ed_editTenLoaiSP;
    ImageView img_editAnhLoaiSP;
    DAOLoaiSanPham daoLoaiSanPham;
    DAOHang daoHang;
    ArrayList<LoaiSanPham> listLSP;
    ArrayList<Hang> listH;
    LoaiSanPham loaiSanPham;
    int malsp;
    Bitmap bitmap, bitmap1;
    byte[] image;
    LinearLayout linearCamera, linearGallery;
    ActivityResultLauncher<Intent> launcherCamera;
    ActivityResultLauncher<Intent> launcherGallery;
    Drawable drawable;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_loai_sp);
        addControl();
        addEvent();
    }
    private void addEvent() {
        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    img_editAnhLoaiSP.setImageBitmap(bitmap);
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
                        img_editAnhLoaiSP.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Intent intent = getIntent();
        malsp = intent.getIntExtra("malsp",0);
        loaiSanPham = daoLoaiSanPham.getID(String.valueOf(malsp));
        ed_editMaLoaiSP.setText(loaiSanPham.getMslsp()+"");
        ed_editTenLoaiSP.setText(loaiSanPham.getTenlsp());
        if (loaiSanPham.getHinhanh() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getDrawable(R.drawable.image_defaut);
            }
            img_editAnhLoaiSP.setImageDrawable(drawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(loaiSanPham.getHinhanh(), 0, loaiSanPham.getHinhanh().length);
            img_editAnhLoaiSP.setImageBitmap(bitmap);
        }
        bitmap = ((BitmapDrawable) img_editAnhLoaiSP.getDrawable()).getBitmap();
        img_editAnhLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });

    }

    private void addControl() {
        ed_editMaLoaiSP = findViewById(R.id.ed_editMaLoaiSP);
        ed_editTenLoaiSP = findViewById(R.id.ed_editTenLoaiSP);
        img_editAnhLoaiSP = findViewById(R.id.img_editAnhLoaiSP);
        toolbar = findViewById(R.id.tb_editLSP);
        daoLoaiSanPham = new DAOLoaiSanPham(this);
        daoHang = new DAOHang(this);
        listH = new ArrayList<>();
        listLSP = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cập nhật loại sản phẩm");
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
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img_editAnhLoaiSP.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        image = byteArray.toByteArray();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    public boolean check() {
        String tenlsp = ed_editTenLoaiSP.getText().toString();

        if (tenlsp.isEmpty()) {
            Toast.makeText(this, "Không được bỏ trống thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ed_editTenLoaiSP.getText().toString().substring(0, 1).toUpperCase().equals(ed_editTenLoaiSP.getText().toString().substring(0, 1))) {
            Toast.makeText(this, "Kí tự đầu tên loại phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                if (check()) {
                    bitmap1 = ((BitmapDrawable) img_editAnhLoaiSP.getDrawable()).getBitmap();
                    if (bitmap != bitmap1) {
                        convertImage();
                        loaiSanPham.setHinhanh(image);
                    }
                    loaiSanPham.setTenlsp(ed_editTenLoaiSP.getText().toString());
                    int kq = daoLoaiSanPham.updateLoaiSanPham(loaiSanPham);
                    if (kq > 0) {
                        listLSP.clear();
                        listLSP.addAll(daoLoaiSanPham.getAll());
                        Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
