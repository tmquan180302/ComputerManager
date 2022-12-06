package com.example.computermanage.ui.hang;

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

import com.example.computermanage.adapter.AdapterHang;
import com.example.computermanage.dao.DAOHang;
import com.example.computermanage.dao.DAOLoaiSanPham;
import com.example.computermanage.model.Hang;
import com.example.computermanage.R;
import com.example.computermanage.model.LoaiSanPham;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityEditHang extends AppCompatActivity {
    ImageView img_editAnhHang, img_editCameraHang;
    TextInputEditText ed_editMaHang, ed_editTenHang;
    Spinner spn_editLoaiSP;
    byte[] image;
    LinearLayout linearCamera, linearGallery;
    ActivityResultLauncher<Intent> launcherCamera;
    ActivityResultLauncher<Intent> launcherGallery;
    DAOHang daoHang;
    DAOLoaiSanPham daoLoaiSanPham;
    Bitmap bitmap, bitmap1;
    Hang hang;
    LoaiSanPham loaiSanPham;
    Drawable drawable;
    ArrayList<Hang> listH;
    ArrayList<LoaiSanPham> listLoaiSP;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hang);

        img_editAnhHang = findViewById(R.id.img_editAnhHang);
        ed_editMaHang = findViewById(R.id.ed_editMaHang);
        spn_editLoaiSP = findViewById(R.id.spn_editLoaiSP);
        ed_editTenHang = findViewById(R.id.ed_editTenHang);
        toolbar = findViewById(R.id.tb_editHang);
        daoHang = new DAOHang(this);
        daoLoaiSanPham = new DAOLoaiSanPham(this);
        listH = new ArrayList<>();
        listLoaiSP = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cập nhật hãng");
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
        GetDateSpinner();
        addEvent();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addEvent() {
        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    img_editAnhHang.setImageBitmap(bitmap);
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
                        img_editAnhHang.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Intent intent = getIntent();
        String mahang = intent.getStringExtra("mahang");
        hang = daoHang.getID(mahang);
        ed_editMaHang.setText("" + hang.getMshang());
        ed_editTenHang.setText(hang.getTenhang());
        loaiSanPham = daoLoaiSanPham.getID(String.valueOf(hang.getMslsp()));
        listLoaiSP = daoLoaiSanPham.getAll();
        int pos = -1;
        for (int i = 0; i < listLoaiSP.size(); i++) {
            if (listLoaiSP.get(i).getMslsp() == hang.getMslsp()) {
                pos = i;
                break;
            }
        }
        spn_editLoaiSP.setSelection(pos);
        if (hang.getHinhanh() == null) {
            drawable = getDrawable(R.drawable.image_defaut);
            img_editAnhHang.setImageDrawable(drawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(hang.getHinhanh(), 0, hang.getHinhanh().length);
            img_editAnhHang.setImageBitmap(bitmap);
        }
        bitmap = ((BitmapDrawable) img_editAnhHang.getDrawable()).getBitmap();
        img_editAnhHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });
    }

    private void GetDateSpinner() {
        listLoaiSP = daoLoaiSanPham.getAll();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listLoaiSP);
        spn_editLoaiSP.setAdapter(adapter);

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
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img_editAnhHang.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        image = byteArray.toByteArray();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean check() {
        String ten = ed_editTenHang.getText().toString();
        Pattern p1 = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
        Matcher m1 = p1.matcher(ed_editMaHang.getText().toString());
        if (ten.isEmpty()) {
            Toast.makeText(this, "Không được bỏ trống thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (m1.find() == false) {
            Toast.makeText(this, "Kí tự đầu mã hãng phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ed_editTenHang.getText().toString().substring(0, 1).toUpperCase().equals(ed_editTenHang.getText().toString().substring(0, 1))) {
            Toast.makeText(this, "Kí tự đầu tên hãng phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                if (check()) {
                    bitmap1 = ((BitmapDrawable) img_editAnhHang.getDrawable()).getBitmap();
                    if (bitmap != bitmap1) {
                        convertImage();
                        hang.setHinhanh(image);
                    }
                    hang.setMshang(ed_editMaHang.getText().toString());
                    hang.setTenhang(ed_editTenHang.getText().toString());
                    loaiSanPham = (LoaiSanPham) spn_editLoaiSP.getSelectedItem();
                    hang.setMslsp(loaiSanPham.getMslsp());
                    long result = daoHang.updateHang(hang);
                    if (result > 0) {
                        listH.clear();
                        listH.addAll(daoHang.getAll());
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
}