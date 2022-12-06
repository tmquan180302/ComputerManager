package com.poly.computermanager.ui.hoadonxuat;

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

import com.poly.computermanager.adapter.AdapterHDXuat;
import com.poly.computermanager.dao.DAOHoaDon;
import com.poly.computermanager.model.HoaDon;
import com.poly.computermanager.R;

import java.util.ArrayList;

public class ActivityHoadonXuat extends AppCompatActivity {
    RecyclerView rcv_hoadonxuat;
    DAOHoaDon daoHoaDon;
    ArrayList<HoaDon> listHDXuat;
    AdapterHDXuat adapterHDXuat;
    Toolbar toolbar;
    Spinner spn_SXHDXuat;
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
        setContentView(R.layout.activity_hoadonxuat);
        addControl();
        addEvent();

    }

    private void addEvent() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcv_hoadonxuat.setLayoutManager(layoutManager);
        listHDXuat = daoHoaDon.getAllXuat();
        adapterHDXuat = new AdapterHDXuat(ActivityHoadonXuat.this, listHDXuat);
        rcv_hoadonxuat.setAdapter(adapterHDXuat);
    }

    private void addControl() {
        toolbar = findViewById(R.id.tb_HDXuat);
        spn_SXHDXuat = findViewById(R.id.spn_SXHDXuat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hóa đơn xuất");
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
        rcv_hoadonxuat = findViewById(R.id.rcv_hoadonxuat);
        daoHoaDon = new DAOHoaDon(this);
        listHDXuat = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(ActivityHoadonXuat.this, android.R.layout.simple_list_item_1, mang);
        spn_SXHDXuat.setAdapter(adapter);
        spn_SXHDXuat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        listHDXuat.clear();
                        listHDXuat.addAll(daoHoaDon.getAllXuat());
                        adapterHDXuat.notifyDataSetChanged();
                        break;
                    case 1:
                        listHDXuat.clear();
                        listHDXuat.addAll(daoHoaDon.getDayXuat());
                        adapterHDXuat.notifyDataSetChanged();
                        break;
                    case 2:
                        listHDXuat.clear();
                        listHDXuat.addAll(daoHoaDon.getWeekXuat());
                        adapterHDXuat.notifyDataSetChanged();
                        break;
                    case 3:
                        listHDXuat.clear();
                        listHDXuat.addAll(daoHoaDon.getMonthXuat());
                        adapterHDXuat.notifyDataSetChanged();
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
                adapterHDXuat.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterHDXuat.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        listHDXuat.clear();
        listHDXuat = daoHoaDon.getAllXuat();
        adapterHDXuat = new AdapterHDXuat(ActivityHoadonXuat.this, listHDXuat);
        rcv_hoadonxuat.setAdapter(adapterHDXuat);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(getApplicationContext(), ActivityAddHoaDonXuat.class));
        }
        return super.onOptionsItemSelected(item);
    }
}