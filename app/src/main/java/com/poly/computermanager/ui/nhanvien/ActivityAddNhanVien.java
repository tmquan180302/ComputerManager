package com.poly.computermanager.ui.nhanvien;

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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.poly.computermanager.dao.DAONhanVien;
import com.poly.computermanager.LoginActivity;
import com.poly.computermanager.model.NhanVien;
import com.poly.computermanager.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityAddNhanVien extends AppCompatActivity {
    ImageView img_anhNhanVien;
    TextInputEditText ed_maNhanVien, ed_passwordNV, ed_tenNhanVien, ed_sdtNhanVien, ed_emailNhanVien;
    Bitmap bitmap, bitmap1;
    byte[] image;
    LinearLayout linearCamera, linearGallery;
    ActivityResultLauncher<Intent> launcherCamera;
    ActivityResultLauncher<Intent> launcherGallery;
    DAONhanVien daoNhanVien;
    ArrayList<NhanVien> listNhanVien;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nhan_vien);

        addControl();
        addEvent();
    }

    private void addControl() {
        img_anhNhanVien = findViewById(R.id.img_anhNhanVien);
        ed_maNhanVien = findViewById(R.id.ed_maNhanVien);
        ed_passwordNV = findViewById(R.id.ed_passwordNV);
        ed_tenNhanVien = findViewById(R.id.ed_tenNhanVien);
        ed_sdtNhanVien = findViewById(R.id.ed_sdtNhanVien);
        ed_emailNhanVien = findViewById(R.id.ed_emailNhanVien);
        toolbar = findViewById(R.id.tb_addNV);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm Nhân Viên");
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityAddNhanVien.this, LoginActivity.class));
            }
        });
        daoNhanVien = new DAONhanVien(ActivityAddNhanVien.this);
        listNhanVien = new ArrayList<>();
    }

    private void addEvent() {
        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    img_anhNhanVien.setImageBitmap(bitmap);
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
                        img_anhNhanVien.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bitmap = ((BitmapDrawable) img_anhNhanVien.getDrawable()).getBitmap();
        img_anhNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });
    }

    private void openBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        linearCamera = view.findViewById(R.id.linearCamera);
        linearGallery = view.findViewById(R.id.linearGallery);
        linearCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launcherCamera.launch(intent);
                bottomSheetDialog.cancel();
            }
        });
        linearGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcherGallery.launch(intent);
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    public void converImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img_anhNhanVien.getDrawable();
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

    public boolean checkDK() {
        Pattern p = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
        Matcher m = p.matcher(ed_maNhanVien.getText().toString());
        Pattern p1 = Pattern.compile("^\\w+@\\w+\\.\\w+$");
        Matcher m1 = p1.matcher(ed_emailNhanVien.getText().toString());
        String user = ed_maNhanVien.getText().toString();
        String password = ed_passwordNV.getText().toString();
        String tennv = ed_tenNhanVien.getText().toString();
        String email = ed_emailNhanVien.getText().toString();
        String sdt = ed_sdtNhanVien.getText().toString();
        if (user.isEmpty() || password.isEmpty() || tennv.isEmpty() || email.isEmpty() || sdt.isEmpty()) {
            Toast.makeText(this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (m.find() == false) {
            Toast.makeText(this, "Tài khoản có kí tự đầu in hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ed_tenNhanVien.getText().toString().substring(0, 1).toUpperCase().equals(ed_tenNhanVien.getText().toString().substring(0, 1))) {
            Toast.makeText(this, "Kí tự đầu phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (sdt.length() < 10 || sdt.length() > 10) {
            Toast.makeText(this, "Số điện thoại phải đủ 10 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        } else if (m1.find() == false) {
            Toast.makeText(this, "Email chưa đúng định dạng", Toast.LENGTH_SHORT).show();
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
                img_anhNhanVien.setImageDrawable(drawable);
                ed_maNhanVien.setText("");
                ed_passwordNV.setText("");
                ed_tenNhanVien.setText("");
                ed_sdtNhanVien.setText("");
                ed_emailNhanVien.setText("");
                return true;
            case R.id.menu_save:
                if (checkDK()) {
                    bitmap1 = ((BitmapDrawable) img_anhNhanVien.getDrawable()).getBitmap();
                    NhanVien nhanVien = new NhanVien();
                    nhanVien.setMsnv(ed_maNhanVien.getText().toString());
                    nhanVien.setPassword(ed_passwordNV.getText().toString());
                    nhanVien.setHoten(ed_tenNhanVien.getText().toString());
                    nhanVien.setSdt(ed_sdtNhanVien.getText().toString());
                    nhanVien.setEmail(ed_emailNhanVien.getText().toString());
                    if (bitmap != bitmap1) {
                        converImage();
                        nhanVien.setHinhanh(image);
                    }
                    long kq = daoNhanVien.insertNhanVien(nhanVien);
                    if (kq > 0) {
                        listNhanVien.clear();
                        listNhanVien.addAll(daoNhanVien.getAll());
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityAddNhanVien.this, LoginActivity.class));
                    } else {
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}