package com.example.computermanage.ui.hoadonnhap;

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

import com.example.computermanage.adapter.AdapterHDNhap;
import com.example.computermanage.dao.DAOHoaDon;
import com.example.computermanage.model.HoaDon;
import com.example.computermanage.R;

import java.util.ArrayList;

public class ActivityHoadonNhap extends AppCompatActivity {
    RecyclerView rcv_hoadonnhap;
    DAOHoaDon daoHoaDon;
    ArrayList<HoaDon> listHDNhap;
    AdapterHDNhap adapterHDNhap;
    Toolbar toolbar;
    Spinner spn_SXHDNhap;
    SearchView searchView;
    String[] mang = new String[]{
            "Tất cả",
            "Hôm nay",
            "7 ngày",
            "30 ngày"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadonnhap);
        addControl();
        addEvent();

    }

    private void addEvent() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcv_hoadonnhap.setLayoutManager(layoutManager);
        listHDNhap = daoHoaDon.getAllNhap();
        adapterHDNhap = new AdapterHDNhap(ActivityHoadonNhap.this, listHDNhap);
        rcv_hoadonnhap.setAdapter(adapterHDNhap);
    }

    private void addControl() {

        rcv_hoadonnhap = findViewById(R.id.rcv_hoadonnhap);
        toolbar = findViewById(R.id.tb_HDNhap);
        spn_SXHDNhap = findViewById(R.id.spn_SXHDNhap);
        daoHoaDon = new DAOHoaDon(this);
        listHDNhap = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hóa đơn nhập");
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
        ArrayAdapter adapter = new ArrayAdapter(ActivityHoadonNhap.this, android.R.layout.simple_list_item_1, mang);
        spn_SXHDNhap.setAdapter(adapter);
        spn_SXHDNhap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        listHDNhap.clear();
                        listHDNhap.addAll(daoHoaDon.getAllNhap());
                        adapterHDNhap.notifyDataSetChanged();
                        break;
                    case 1:
                        listHDNhap.clear();
                        listHDNhap.addAll(daoHoaDon.getDayNhap());
                        adapterHDNhap.notifyDataSetChanged();
                        break;
                    case 2:
                        listHDNhap.clear();
                        listHDNhap.addAll(daoHoaDon.getWeekNhap());
                        adapterHDNhap.notifyDataSetChanged();
                        break;
                    case 3:
                        listHDNhap.clear();
                        listHDNhap.addAll(daoHoaDon.getMonthNhap());
                        adapterHDNhap.notifyDataSetChanged();
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterHDNhap.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterHDNhap.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        listHDNhap.clear();
        listHDNhap = daoHoaDon.getAllNhap();
        adapterHDNhap = new AdapterHDNhap(ActivityHoadonNhap.this, listHDNhap);
        rcv_hoadonnhap.setAdapter(adapterHDNhap);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(getApplicationContext(), ActivityAddHoaDonNhap.class));
        }
        return super.onOptionsItemSelected(item);
    }
}