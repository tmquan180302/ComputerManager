package com.poly.computermanager.ui.hang;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.poly.computermanager.dao.DAOHang;
import com.poly.computermanager.dao.DAOLoaiSanPham;
import com.poly.computermanager.model.Hang;
import com.poly.computermanager.R;
import com.poly.computermanager.model.LoaiSanPham;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityAddHang extends AppCompatActivity {
    ImageView img_anhHang, img_cameraHang;
    TextInputEditText ed_mahang, ed_tenhang;
    Bitmap bitmap, bitmap1;
    Spinner spn_LoaiSP;
    byte[] image;
    LinearLayout linearCamera, linearGallery;
    ActivityResultLauncher<Intent> launcherCamera;
    ActivityResultLauncher<Intent> launcherGallery;
    DAOHang daoHang;
    DAOLoaiSanPham daoLoaiSanPham;
    ArrayList<Hang> listHang;
    ArrayList<LoaiSanPham> listLoaiSP;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hang);

        addConTrol();
        GetDateSpinner();
        addEvent();
    }
    private void GetDateSpinner(){
        listLoaiSP=daoLoaiSanPham.getAll();
        ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,listLoaiSP);
        spn_LoaiSP.setAdapter(adapter);

    }
    private void addEvent() {
        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    img_anhHang.setImageBitmap(bitmap);
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
                        img_anhHang.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bitmap = ((BitmapDrawable) img_anhHang.getDrawable()).getBitmap();
        img_anhHang.setOnClickListener(new View.OnClickListener() {
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


    private void addConTrol() {
        img_anhHang = findViewById(R.id.img_anhHang);
        ed_mahang = findViewById(R.id.ed_mahang);
        ed_tenhang = findViewById(R.id.ed_tenhang);
        spn_LoaiSP = findViewById(R.id.spn_LoaiSP);
        toolbar = findViewById(R.id.tb_addHang);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm hãng");
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
        daoHang = new DAOHang(ActivityAddHang.this);
        daoLoaiSanPham=new DAOLoaiSanPham(this);
        listHang = new ArrayList<>();
        listLoaiSP=new ArrayList<>();
    }

    public void convertImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img_anhHang.getDrawable();
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
        String ten = ed_tenhang.getText().toString();
        Pattern p1 = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
        Matcher m1 = p1.matcher(ed_mahang.getText().toString());
        if (listLoaiSP.size() < 0) {
            Toast.makeText(this, "Chưa có loại sản phẩm , vui lòng thêm loại sản phẩm", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ten.isEmpty()) {
            Toast.makeText(this, "Không được bỏ trống thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }else if (m1.find() == false) {
            Toast.makeText(this, "Kí tự đầu mã hãng phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ed_tenhang.getText().toString().substring(0,1).toUpperCase().equals(ed_tenhang.getText().toString().substring(0,1))) {
            Toast.makeText(this, "Kí tự đầu tên hãng phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                Drawable drawable = getDrawable(R.drawable.image_defaut);
                img_anhHang.setImageDrawable(drawable);
                ed_mahang.setText("");
                ed_tenhang.setText("");
                return true;
            case R.id.menu_save:
                if (check()) {
                    bitmap1 = ((BitmapDrawable) img_anhHang.getDrawable()).getBitmap();
                    Hang hang = new Hang();
                    hang.setTenhang(ed_tenhang.getText().toString());
                    hang.setMshang(ed_mahang.getText().toString());
                    LoaiSanPham loaiSanPham= (LoaiSanPham) spn_LoaiSP.getSelectedItem();
                    hang.setMslsp(loaiSanPham.getMslsp());
                    if (bitmap != bitmap1) {
                        convertImage();
                        hang.setHinhanh(image);
                    }
                    long kq = daoHang.insertHang(hang);
                    if (kq > 0) {
                        listHang.clear();
                        listHang.addAll(daoHang.getAll());
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
