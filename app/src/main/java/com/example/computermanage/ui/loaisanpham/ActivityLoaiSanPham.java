package com.example.computermanage.ui.loaisanpham;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.computermanage.adapter.AdapterLoaiSanPham;
import com.example.computermanage.dao.DAOLoaiSanPham;
import com.example.computermanage.model.LoaiSanPham;
import com.example.computermanage.R;

import java.util.ArrayList;

public class ActivityLoaiSanPham extends AppCompatActivity {
    RecyclerView rcv_loaisp;
    DAOLoaiSanPham daoLoaiSanPham;
    ArrayList<LoaiSanPham> listLSP;
    AdapterLoaiSanPham adapterLoaiSanPham;
    Spinner spn_SXLoaiSP;
    Toolbar toolbar;
    SearchView searchView;
    String[] mang=new String[]{
            "Mặc định",
            "Sắp xếp theo mã",
            "Sắp xếp theo tên"
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_san_pham);
        addControl();
        addEvent();
    }
    private void addEvent() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityLoaiSanPham.this);
        rcv_loaisp.setLayoutManager(layoutManager);
        listLSP = daoLoaiSanPham.getAll();
        adapterLoaiSanPham = new AdapterLoaiSanPham(ActivityLoaiSanPham.this,listLSP);
        rcv_loaisp.setAdapter(adapterLoaiSanPham);
    }

    private void addControl() {
        rcv_loaisp =findViewById(R.id.rcv_loaisp);
        spn_SXLoaiSP =findViewById(R.id.spn_SXLoaiSP);
        toolbar =findViewById(R.id.tb_LSP);
        daoLoaiSanPham = new DAOLoaiSanPham(this);
        listLSP = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Loại sản phẩm");
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
        ArrayAdapter adapter=new ArrayAdapter(ActivityLoaiSanPham.this, android.R.layout.simple_list_item_1,mang);
        spn_SXLoaiSP.setAdapter(adapter);
        spn_SXLoaiSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        listLSP.clear();
                        listLSP.addAll(daoLoaiSanPham.getAll());
                        adapterLoaiSanPham.notifyDataSetChanged();
                        break;
                    case 1:
                        listLSP.clear();
                        listLSP.addAll(daoLoaiSanPham.getAllSXMa());
                        adapterLoaiSanPham.notifyDataSetChanged();
                        break;
                    case 2:
                        listLSP.clear();
                        listLSP.addAll(daoLoaiSanPham.getAllSXTen());
                        adapterLoaiSanPham.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterLoaiSanPham.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterLoaiSanPham.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        if (!searchView.isIconified()){
//            searchView.setIconified(true);
//            return;
//        }
//        super.onBackPressed();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                startActivity(new Intent(ActivityLoaiSanPham.this,ActivityAddLoaiSP.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        listLSP.clear();
        listLSP.addAll(daoLoaiSanPham.getAll());
        adapterLoaiSanPham = new AdapterLoaiSanPham(ActivityLoaiSanPham.this,listLSP);
        rcv_loaisp.setAdapter(adapterLoaiSanPham);
        super.onResume();
    }
}
