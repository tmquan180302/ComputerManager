package com.poly.computermanager.ui.khachhang;

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

import com.poly.computermanager.adapter.AdapterKhachHang;
import com.poly.computermanager.dao.DAOKhachHang;
import com.poly.computermanager.model.KhachHang;
import com.poly.computermanager.R;

import java.util.ArrayList;

public class ActivityKhachHang extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterKhachHang adapterKhachHang;
    DAOKhachHang daoKhachHang;
    ArrayList<KhachHang> listKH;
    Toolbar toolbar;
    Spinner spn_SXKHang;
    SearchView searchView;
    String[] mang = new String[]{
            "Mặc định",
            "Sắp xếp theo mã",
            "Sắp xếp theo tên"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang);
        addControl();
        addEvent();
    }

    private void addEvent() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listKH = daoKhachHang.getAll();
        adapterKhachHang = new AdapterKhachHang(ActivityKhachHang.this, listKH);
        recyclerView.setAdapter(adapterKhachHang);
    }

    private void addControl() {
        recyclerView = findViewById(R.id.rcv_khachhang);
        toolbar = findViewById(R.id.tb_KhachHang);
        spn_SXKHang = findViewById(R.id.spn_SXKHang);
        daoKhachHang = new DAOKhachHang(this);
        listKH = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Khách hàng");
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
        ArrayAdapter adapter = new ArrayAdapter(ActivityKhachHang.this, android.R.layout.simple_list_item_1, mang);
        spn_SXKHang.setAdapter(adapter);
        spn_SXKHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        listKH.clear();
                        listKH.addAll(daoKhachHang.getAll());
                        adapterKhachHang.notifyDataSetChanged();
                        break;
                    case 1:
                        listKH.clear();
                        listKH.addAll(daoKhachHang.getAllSXMa());
                        adapterKhachHang.notifyDataSetChanged();
                        break;
                    case 2:
                        listKH.clear();
                        listKH.addAll(daoKhachHang.getAllSXTen());
                        adapterKhachHang.notifyDataSetChanged();
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
        inflater.inflate(R.menu.menu_add, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterKhachHang.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterKhachHang.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        listKH.clear();
        listKH = daoKhachHang.getAll();
        adapterKhachHang = new AdapterKhachHang(ActivityKhachHang.this, listKH);
        recyclerView.setAdapter(adapterKhachHang);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(ActivityKhachHang.this, ActivityAddKhachHang.class));
        }
        return super.onOptionsItemSelected(item);
    }
}