package com.poly.computermanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.computermanager.adapter.AdapterGetTop;
import com.poly.computermanager.adapter.AdapterGridViewHang;
import com.poly.computermanager.adapter.AdapterSlider;
import com.poly.computermanager.R;
import com.poly.computermanager.dao.DAOHoaDonCT;
import com.poly.computermanager.dao.DAOSanPham;
import com.poly.computermanager.model.GetTop;
import com.poly.computermanager.model.SanPham;
import com.poly.computermanager.ui.sanpham.ActivityQuerySP;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class FragmentTrangChu extends Fragment {
    SliderView sliderView;
    GridView gridView;
    RecyclerView rcv_spBanChay;
    DAOSanPham daoSanPham;
    DAOHoaDonCT daoHoaDonCT;
    AdapterGetTop adapterGetTop;
    ArrayList<GetTop> listGT;
    ArrayList<SanPham> listDell;
    ArrayList<SanPham> listHP;
    ArrayList<SanPham> listThinkPad;
    ArrayList<SanPham> listMac;
    ArrayList<SanPham> listLenovo;
    ArrayList<SanPham> listAsus;
    ArrayList<SanPham> listAcer;
    ArrayList<SanPham> listMsi;
    int images[] = {
            R.drawable.dell,
            R.drawable.msi,
            R.drawable.lenovo,
            R.drawable.thinkpad,
            R.drawable.mac,
            R.drawable.hp,
            R.drawable.asus,
            R.drawable.aser};

    int[] image = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_trangchu, container, false);
        sliderView = view.findViewById(R.id.image_slider);
        gridView = view.findViewById(R.id.grv_Hang);
        rcv_spBanChay = view.findViewById(R.id.rcv_spBanChay);
        daoSanPham = new DAOSanPham(getContext());
        daoHoaDonCT = new DAOHoaDonCT(getContext());
        listGT = new ArrayList<>();
        listDell = new ArrayList<>();
        listDell = daoSanPham.getAllDell();
        listAcer = new ArrayList<>();
        listAcer = daoSanPham.getAllAcer();
        listAsus = new ArrayList<>();
        listAsus = daoSanPham.getAllAsus();
        listHP = new ArrayList<>();
        listHP = daoSanPham.getAllHP();
        listLenovo = new ArrayList<>();
        listLenovo = daoSanPham.getAllLenovo();
        listMac = new ArrayList<>();
        listMac = daoSanPham.getAllMac();
        listMsi = new ArrayList<>();
        listMsi = daoSanPham.getAllMSI();
        listThinkPad = new ArrayList<>();
        listThinkPad = daoSanPham.getAllThinkPad();
        AdapterSlider adapterSlider = new AdapterSlider(image);
        sliderView.setSliderAdapter(adapterSlider);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
        AdapterGridViewHang gridViewHang = new AdapterGridViewHang(getContext(), images);
        gridView.setAdapter(gridViewHang);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getContext(), ActivityQuerySP.class);
                        intent.putExtra("listsp", listDell);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getContext(), ActivityQuerySP.class);
                        intent1.putExtra("listsp", listMsi);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getContext(), ActivityQuerySP.class);
                        intent2.putExtra("listsp", listLenovo);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getContext(), ActivityQuerySP.class);
                        intent3.putExtra("listsp", listThinkPad);
                        startActivity(intent3);
                        break;

                    case 4:
                        Intent intent4 = new Intent(getContext(), ActivityQuerySP.class);
                        intent4.putExtra("listsp", listMac);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(getContext(), ActivityQuerySP.class);
                        intent5.putExtra("listsp", listHP);
                        startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(getContext(), ActivityQuerySP.class);
                        intent6.putExtra("listsp", listAsus);
                        startActivity(intent6);
                        break;
                    case 7:
                        Intent intent7 = new Intent(getContext(), ActivityQuerySP.class);
                        intent7.putExtra("listsp", listAcer);
                        startActivity(intent7);
                        break;
                }
            }
        });
        listGT = daoHoaDonCT.getTop();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcv_spBanChay.setLayoutManager(layoutManager);
        adapterGetTop = new AdapterGetTop(getContext(),listGT);
        rcv_spBanChay.setAdapter(adapterGetTop);

        return view;
    }
}
