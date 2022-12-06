package com.poly.computermanager.ui.sanpham;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.poly.computermanager.adapter.AdapterSanPham;
import com.poly.computermanager.dao.DAOSanPham;
import com.poly.computermanager.model.SanPham;
import com.poly.computermanager.R;

import java.util.ArrayList;

public class ActivitySanPham extends AppCompatActivity {
    RecyclerView rcv_SanPham;
    DAOSanPham daoSanPham;
    ArrayList<SanPham> listSP;
    AdapterSanPham adapterSanPham;
    Toolbar toolbar;
    Spinner spn_SXSanPham;
    SearchView searchView;
    String[] mang = new String[]{
            "Mặc định",
            "Sắp xếp theo mã",
            "Sắp xếp theo tên",
            "Sắp xếp theo giá"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);
        addControl();
        addDataRecycleView();
    }

    private void addDataRecycleView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivitySanPham.this);
        rcv_SanPham.setLayoutManager(layoutManager);
        listSP = daoSanPham.getAll();
        adapterSanPham = new AdapterSanPham(ActivitySanPham.this, listSP);
        rcv_SanPham.setAdapter(adapterSanPham);
    }

    private void addControl() {

        rcv_SanPham = findViewById(R.id.rcv_SanPham);
        spn_SXSanPham = findViewById(R.id.spn_SXSanPham);
        toolbar = findViewById(R.id.tb_SP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sản phẩm");
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
        daoSanPham = new DAOSanPham(this);
        listSP = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(ActivitySanPham.this, android.R.layout.simple_list_item_1, mang);
        spn_SXSanPham.setAdapter(adapter);
        spn_SXSanPham.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        listSP.clear();
                        listSP.addAll(daoSanPham.getAll());
                        adapterSanPham.notifyDataSetChanged();
                        break;
                    case 1:
                        listSP.clear();
                        listSP.addAll(daoSanPham.getAllSXMa());
                        adapterSanPham.notifyDataSetChanged();
                        break;
                    case 2:
                        listSP.clear();
                        listSP.addAll(daoSanPham.getAllSXTen());
                        adapterSanPham.notifyDataSetChanged();
                        break;

                    case 3:
                        listSP.clear();
                        listSP.addAll(daoSanPham.getAllSXGia());
                        adapterSanPham.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        listSP.clear();
        listSP = daoSanPham.getAll();
        adapterSanPham = new AdapterSanPham(ActivitySanPham.this, listSP);
        rcv_SanPham.setAdapter(adapterSanPham);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterSanPham.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterSanPham.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(ActivitySanPham.this, ActivityAddSanPham.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}