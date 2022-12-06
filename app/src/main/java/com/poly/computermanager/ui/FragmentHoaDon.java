package com.poly.computermanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.computermanager.adapter.AdapterHDXuat;
import com.poly.computermanager.dao.DAOHoaDon;
import com.poly.computermanager.model.HoaDon;
import com.poly.computermanager.R;
import com.poly.computermanager.ui.hoadonxuat.ActivityAddHoaDonXuat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentHoaDon extends Fragment {
    FloatingActionButton flb_addHDXuat;
    RecyclerView rcv_hoadonXuat;
    ArrayList<HoaDon> listHDXuat;
    AdapterHDXuat adapterHDXuat;
    DAOHoaDon daoHoaDon;

    public FragmentHoaDon() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_hoadon, container, false);
        flb_addHDXuat = view.findViewById(R.id.flb_addHDXuat);
        rcv_hoadonXuat = view.findViewById(R.id.rcv_hoadonXuat);
        daoHoaDon = new DAOHoaDon(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcv_hoadonXuat.setLayoutManager(layoutManager);
        listHDXuat = daoHoaDon.getAllXuat();
        adapterHDXuat = new AdapterHDXuat(getContext(), listHDXuat);
        rcv_hoadonXuat.setAdapter(adapterHDXuat);

        flb_addHDXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivityAddHoaDonXuat.class));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        listHDXuat.clear();
        listHDXuat = daoHoaDon.getAllXuat();
        adapterHDXuat = new AdapterHDXuat(getContext(), listHDXuat);
        rcv_hoadonXuat.setAdapter(adapterHDXuat);
        super.onResume();
    }
}
