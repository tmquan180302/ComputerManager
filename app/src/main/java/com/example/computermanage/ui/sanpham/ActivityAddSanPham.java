package com.example.computermanage.ui.sanpham;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.computermanage.dao.DAOHang;
import com.example.computermanage.dao.DAOLoaiSanPham;
import com.example.computermanage.dao.DAOSanPham;
import com.example.computermanage.dao.DAOSanPhamCT;
import com.example.computermanage.model.Hang;
import com.example.computermanage.model.LoaiSanPham;
import com.example.computermanage.model.SanPham;
import com.example.computermanage.model.SanPhamChiTiet;
import com.example.computermanage.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityAddSanPham extends AppCompatActivity {
    ImageView img_anhSanPham;
    TextInputEditText ed_maSanPham, ed_tenSanPham, ed_heDHSP, ed_giaSP, ed_cpuSP, ed_ramSP, ed_ocungSP, ed_manHinhSP, ed_cardMHSP, ed_pinSP, ed_trongLuongSP, ed_moTaSP;
    RadioGroup rdg_tinhTrang, rdg_TrangThai;
    RadioButton rdo_cu, rdo_likeNew, rdo_moi, rdo_conhang, rdo_hethang;
    Spinner spn_HangSP;
    DAOSanPham daoSanPham;
    DAOHang daoHang;
    DAOSanPhamCT daoSanPhamCT;
    ArrayList<SanPham> listSP;
    ArrayList<Hang> listH;
    ArrayList<SanPhamChiTiet> listSPCT;
    Bitmap bitmap, bitmap1;
    byte[] image;
    LinearLayout linearCamera, linearGallery;
    ActivityResultLauncher<Intent> launcherCamera;
    ActivityResultLauncher<Intent> launcherGallery;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_san_pham);
        addControl();
        addClickCamera();
        getDataSpinner();
    }

    private void addControl() {
        ed_maSanPham = findViewById(R.id.ed_maSanPham);
        ed_tenSanPham = findViewById(R.id.ed_tenSanPham);
        ed_giaSP = findViewById(R.id.ed_giaSP);
        ed_cpuSP = findViewById(R.id.ed_cpuSP);
        ed_ramSP = findViewById(R.id.ed_ramSP);
        ed_ocungSP = findViewById(R.id.ed_ocungSP);
        ed_manHinhSP = findViewById(R.id.ed_manHinhSP);
        ed_cardMHSP = findViewById(R.id.ed_cardMHSP);
        ed_pinSP = findViewById(R.id.ed_pinSP);
        ed_trongLuongSP = findViewById(R.id.ed_trongLuongSP);
        ed_moTaSP = findViewById(R.id.ed_moTaSP);
        rdg_tinhTrang = findViewById(R.id.rdg_tinhTrang);
        rdg_TrangThai = findViewById(R.id.rdg_TrangThai);
        rdo_cu = findViewById(R.id.rdo_cu);
        rdo_likeNew = findViewById(R.id.rdo_likeNew);
        rdo_moi = findViewById(R.id.rdo_moi);
        rdo_hethang = findViewById(R.id.rdo_hethang);
        rdo_conhang = findViewById(R.id.rdo_conhang);
        spn_HangSP = findViewById(R.id.spn_HangSP);
        img_anhSanPham = findViewById(R.id.img_anhSanPham);
        ed_heDHSP = findViewById(R.id.ed_heDHSP);
        toolbar = findViewById(R.id.tb_addSP);
        daoSanPham = new DAOSanPham(this);
        daoHang = new DAOHang(this);
        daoSanPhamCT = new DAOSanPhamCT(this);
        listSP = new ArrayList<>();
        listH = new ArrayList<>();
        listSPCT = new ArrayList<>();
        rdo_moi.setChecked(true);
        rdo_hethang.setChecked(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm sản phẩm");
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

    private void addClickCamera() {
        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    img_anhSanPham.setImageBitmap(bitmap);
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
                        img_anhSanPham.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bitmap = ((BitmapDrawable) img_anhSanPham.getDrawable()).getBitmap();
        img_anhSanPham.setOnClickListener(new View.OnClickListener() {
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
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img_anhSanPham.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        image = byteArray.toByteArray();
    }


    private void getDataSpinner() {
        listH = daoHang.getAll();
        ArrayAdapter adapter = new ArrayAdapter(ActivityAddSanPham.this, android.R.layout.simple_spinner_dropdown_item, listH);
        spn_HangSP.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    public boolean check() {
        String masp = ed_maSanPham.getText().toString();
        String tensp = ed_tenSanPham.getText().toString();
        String giatien = ed_giaSP.getText().toString();
        String cpu = ed_cpuSP.getText().toString();
        String ram = ed_ramSP.getText().toString();
        String ocung = ed_ocungSP.getText().toString();
        String hdh = ed_heDHSP.getText().toString();
        String manhinh = ed_manHinhSP.getText().toString();
        String cardMH = ed_cardMHSP.getText().toString();
        String pin = ed_pinSP.getText().toString();
        String trongluong = ed_trongLuongSP.getText().toString();
        String mota = ed_moTaSP.getText().toString();
        Pattern p = Pattern.compile("^[A-Z]+[a-zA-Z0-9]+$");
        Matcher m = p.matcher(ed_maSanPham.getText().toString());
        if (listH.size() < 0) {
            Toast.makeText(this, "Chưa có hãng , vui lòng thêm hãng", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (masp.isEmpty() || tensp.isEmpty() ||
                giatien.isEmpty() || cpu.isEmpty() ||
                ram.isEmpty() || ocung.isEmpty() || hdh.isEmpty() ||
                manhinh.isEmpty() || cardMH.isEmpty() || pin.isEmpty() || trongluong.isEmpty() || mota.isEmpty()) {
            Toast.makeText(this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        } else if (m.find() == false) {
            Toast.makeText(this, "Kí tự đầu mã sản phẩm phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ed_tenSanPham.getText().toString().substring(0,1).toUpperCase().equals(ed_tenSanPham.getText().toString().substring(0,1))) {
            Toast.makeText(this, "Kí tự đầu tên sản phẩm phải in hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                ed_maSanPham.setText("");
                ed_tenSanPham.setText("");
                ed_giaSP.setText("");
                ed_cpuSP.setText("");
                ed_cardMHSP.setText("");
                ed_manHinhSP.setText("");
                ed_trongLuongSP.setText("");
                ed_pinSP.setText("");
                ed_moTaSP.setText("");
                ed_ocungSP.setText("");
                ed_heDHSP.setText("");
                ed_ramSP.setText("");
                rdo_moi.setChecked(true);
                rdo_conhang.setChecked(true);
                Drawable drawable = getDrawable(R.drawable.image_defaut);
                img_anhSanPham.setImageDrawable(drawable);
                return true;
            case R.id.menu_save:
                if (check()) {
                    bitmap1 = ((BitmapDrawable) img_anhSanPham.getDrawable()).getBitmap();
                    //add sanpham
                    SanPham sanPham = new SanPham();
                    if (bitmap != bitmap1) {
                        convertImage();
                        sanPham.setHinhanh(image);
                    }

                    sanPham.setMssp(ed_maSanPham.getText().toString());
                    sanPham.setTensp(ed_tenSanPham.getText().toString());
                    Hang hang = (Hang) spn_HangSP.getSelectedItem();
                    sanPham.setMshang(hang.getMshang());
                    sanPham.setGiatien(Double.parseDouble(ed_giaSP.getText().toString()));
                    int checkRadio = rdg_tinhTrang.getCheckedRadioButtonId();
                    if (checkRadio == R.id.rdo_cu) {
                        sanPham.setTinhtrang(0);
                    } else if (checkRadio == R.id.rdo_likeNew) {
                        sanPham.setTinhtrang(1);
                    } else if (checkRadio == R.id.rdo_moi) {
                        sanPham.setTinhtrang(2);
                    }
                    if (rdo_conhang.isChecked()) {
                        sanPham.setTrangthai(0);
                    } else if (rdo_hethang.isChecked()) {
                        sanPham.setTrangthai(1);
                    }
                    sanPham.setMota(ed_moTaSP.getText().toString());
                    long kq = daoSanPham.insertSanPham(sanPham);
                    if (kq > 0) {
//add san pham chi tiet
                        SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
                        sanPhamChiTiet.setMssp(ed_maSanPham.getText().toString());
                        sanPhamChiTiet.setCpu(ed_cpuSP.getText().toString());
                        sanPhamChiTiet.setRam(ed_ramSP.getText().toString());
                        sanPhamChiTiet.setOcung(ed_ocungSP.getText().toString());
                        sanPhamChiTiet.setHedieuhanh(ed_heDHSP.getText().toString());
                        sanPhamChiTiet.setManhinh(ed_manHinhSP.getText().toString());
                        sanPhamChiTiet.setCardmh(ed_cardMHSP.getText().toString());
                        sanPhamChiTiet.setPin(ed_pinSP.getText().toString());
                        sanPhamChiTiet.setTrongluong(Float.parseFloat(ed_trongLuongSP.getText().toString()));
                        long kq1 = daoSanPhamCT.insertSanPhamCT(sanPhamChiTiet);
                        if (kq1 > 0) {
                            listSP.clear();
                            listSPCT.clear();
                            listSP.addAll(daoSanPham.getAll());
                            listSPCT.addAll(daoSanPhamCT.getAll());
                            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(this, "Thêm thuộc tính sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}