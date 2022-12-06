package com.poly.computermanager.ui.sanpham;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.poly.computermanager.R;
import com.poly.computermanager.adapter.AdapterQuerySanPham;
import com.poly.computermanager.model.SanPham;

import java.util.ArrayList;

public class ActivityQuerySP extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_sp);
        recyclerView = findViewById(R.id.rcv_listQuery);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ActivityQuerySP.this);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        ArrayList<SanPham> listQ = (ArrayList<SanPham>) intent.getSerializableExtra("listsp");
        AdapterQuerySanPham adapterSanPham = new AdapterQuerySanPham(ActivityQuerySP.this,listQ);
        recyclerView.setAdapter(adapterSanPham);
    }
}