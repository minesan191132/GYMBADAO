package dao;

import utils.Xjdbc;
import entity.ChiTietDonHang;

public class ChiTietDonHangDAO {
    public void insert(ChiTietDonHang ctdh) {
        String sql = "INSERT INTO ChiTietDonHang (MaDH, MaSP, SoLuong, Gia) VALUES (?, ?, ?, ?)";
        Xjdbc.update(sql,
                ctdh.getMaDH(),
                ctdh.getMaSP(),
                ctdh.getSoLuong(),
                ctdh.getGia());
    }  
}