package com.poly.computermanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.computermanager.R;
import com.poly.computermanager.adapter.AdapterTonKho;
import com.poly.computermanager.dao.DAOHoaDon;
import com.poly.computermanager.dao.DAOHoaDonCT;
import com.poly.computermanager.model.GetSLNhap;
import com.poly.computermanager.model.GetSLXuat;
import com.poly.computermanager.model.HoaDon;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentThongKe extends Fragment {
    TextView tv_tongTienNhap;
    TextView tv_tongTienXuat;
    TextView tv_soHDNhap,tv_slHDNhap,tv_soHDXuat,tv_slHDXuat,tv_doanhthu,tv_loinhuan;
    DAOHoaDonCT daoHoaDonCT;
    DAOHoaDon daoHoaDon;
    RecyclerView rcv_tonKho;
    ArrayList<HoaDon> listHDN;
    ArrayList<HoaDon> listHDX;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_thongke, container, false);

        tv_tongTienNhap = view.findViewById(R.id.tv_tongTienNhap);
        tv_tongTienXuat = view.findViewById(R.id.tv_tongTienXuat);
        tv_soHDNhap = view.findViewById(R.id.tv_soHDNhap);
        tv_slHDNhap = view.findViewById(R.id.tv_slHDNhap);
        tv_soHDXuat = view.findViewById(R.id.tv_soHDXuat);
        tv_slHDXuat = view.findViewById(R.id.tv_slHDXuat);
        tv_doanhthu = view.findViewById(R.id.tv_doanhthu);
        tv_loinhuan = view.findViewById(R.id.tv_loinhuan);
        rcv_tonKho = view.findViewById(R.id.rcv_tonKho);
        daoHoaDonCT = new DAOHoaDonCT(getContext());
        daoHoaDon = new DAOHoaDon(getContext());
        listHDN = new ArrayList<>();
        listHDX = new ArrayList<>();
        listHDN = daoHoaDon.getAllNhap();
        listHDX = daoHoaDon.getAllXuat();


        //tv_doanhthu.setText(daoHoaDonCT.slNhap()+"");
        ArrayList<GetSLNhap> slNhaps = daoHoaDonCT.slNhap();
        ArrayList<GetSLXuat> slXuats = daoHoaDonCT.slXuat();

        for(int i = 0 ; i < slNhaps.size() ; i++){
            for(int j = 0 ; j < slXuats.size() ; j++){
                if(slNhaps.get(i).mssp.equals(slXuats.get(j).mssp)){
                    slNhaps.get(i).soluongnhap -= slXuats.get(i).soluongxuat;
                }
            }
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcv_tonKho.setLayoutManager(layoutManager);
        AdapterTonKho adapterTonKho = new AdapterTonKho(getContext(),slNhaps);
        rcv_tonKho.setAdapter(adapterTonKho);
        if (listHDN.size()>0 && listHDX.size()>0){
            tv_loinhuan.setText((daoHoaDonCT.getTongXuat()-(daoHoaDonCT.getTongNhap()+daoHoaDonCT.tienTon()))+" đ");
        }else {
            tv_loinhuan.setText("0");
        }
        if (listHDN.size() > 0) {
            tv_tongTienNhap.setText(decimalFormat.format(daoHoaDonCT.getTongNhap()) + " đ");
            tv_soHDNhap.setText(daoHoaDon.getSoHDNhap()+"");
            tv_slHDNhap.setText(daoHoaDonCT.getSLNhap()+"");
        } else {
            tv_tongTienNhap.setText("0");
            tv_soHDNhap.setText("0");
            tv_slHDNhap.setText("0");
        }
        if (listHDX.size() > 0){
            tv_tongTienXuat.setText(decimalFormat.format(daoHoaDonCT.getTongXuat()) + " đ");
            tv_soHDXuat.setText(daoHoaDon.getSoHDXuat()+"");
            tv_slHDXuat.setText(daoHoaDonCT.getSLXuat()+"");
            tv_doanhthu.setText(decimalFormat.format(daoHoaDonCT.getTongXuat()) + " đ");
        }else {
            tv_tongTienXuat.setText("0");
            tv_soHDXuat.setText("0");
            tv_slHDXuat.setText("0");
        }
        return view;
    }
}
