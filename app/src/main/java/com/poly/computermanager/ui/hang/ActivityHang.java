package com.poly.computermanager.ui.hang;

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

import com.poly.computermanager.adapter.AdapterHang;
import com.poly.computermanager.dao.DAOHang;
import com.poly.computermanager.model.Hang;
import com.poly.computermanager.R;

import java.util.ArrayList;

public class ActivityHang extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rcv_hang;
    AdapterHang adapterHang;
    ArrayList<Hang> hangArrayList;
    DAOHang daoHang;
    Spinner spn_SXHang;
    ArrayAdapter adapter;
    SearchView searchView;
    String[] mang = new String[]{
            "Mặc định",
            "Sắp xếp theo mã",
            "Sắp xếp theo tên"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hang);

        rcv_hang = findViewById(R.id.rcv_hang);
        spn_SXHang = findViewById(R.id.spn_SXHang);
        toolbar = findViewById(R.id.tb_Hang);
        hangArrayList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityHang.this);
        rcv_hang.setLayoutManager(layoutManager);
        daoHang = new DAOHang(ActivityHang.this);
        hangArrayList = daoHang.getAll();
        adapterHang = new AdapterHang(ActivityHang.this, hangArrayList);
        rcv_hang.setAdapter(adapterHang);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hãng");
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
        adapter = new ArrayAdapter(ActivityHang.this, android.R.layout.simple_list_item_1, mang);
        spn_SXHang.setAdapter(adapter);
        spn_SXHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        hangArrayList.clear();
                        hangArrayList.addAll(daoHang.getAll());
                        adapterHang.notifyDataSetChanged();
                        break;
                    case 1:
                        hangArrayList.clear();
                        hangArrayList.addAll(daoHang.getAllSXMa());
                        adapterHang.notifyDataSetChanged();
                        break;
                    case 2:
                        hangArrayList.clear();
                        hangArrayList.addAll(daoHang.getAllSXTen());
                        adapterHang.notifyDataSetChanged();
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
                adapterHang.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterHang.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        hangArrayList.clear();
        hangArrayList = daoHang.getAll();
        AdapterHang adapterHang = new AdapterHang(ActivityHang.this, hangArrayList);
        rcv_hang.setAdapter(adapterHang);

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(ActivityHang.this, ActivityAddHang.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}