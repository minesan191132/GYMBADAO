package dao;

import entity.DonHang;
import utils.Xjdbc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonHangDAO {
    public void insert(DonHang dh) {
        String sql = "INSERT INTO DonHang (MaKH, NgayLap, ThanhTien) VALUES (?, ?, ?)";
        Xjdbc.update(sql,
                dh.getMaKH(),
                dh.getNgayLap(),
                dh.getThanhTien());
    } 

public int getLastInsertIdShort() {
        String sql = "SELECT IDENT_CURRENT('DonHang')";
        return Xjdbc.valueInt(sql);
    }
}

