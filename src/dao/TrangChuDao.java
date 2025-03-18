/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.ThanhVien;
import java.util.ArrayList;
import java.util.List;
import utils.Xjdbc;
import java.sql.ResultSet;

/**
 *
 * @author Admin
 */
public class TrangChuDao {

    public double getTongDoanhThu() {
        String sql = "SELECT SUM(ThanhTien) FROM DonHang";
        Object result = Xjdbc.value(sql);
        return result == null ? 0 : ((Number) result).doubleValue();
    }

    // Đếm số đơn hàng
    public int getSoDonHang() {
        String sql = "SELECT COUNT(*) FROM DonHang";
        Object value = Xjdbc.value(sql);
        return value == null ? 0 : Integer.parseInt(value.toString());
    }

    // Đếm số thành viên
    public int getSoThanhVien() {
        String sql = "SELECT COUNT(*) FROM ThanhVien";
        Object value = Xjdbc.value(sql);
        return value == null ? 0 : Integer.parseInt(value.toString());
    }

    public List<ThanhVien> getThanhVienMoi() {
        List<ThanhVien> list = new ArrayList<>();
        // Code SQL để lấy dữ liệu từ DB
        // VD:
        String sql = "SELECT * FROM ThanhVien WHERE MONTH(GETDATE()) = MONTH(NgayDangKy)";
        try {
            ResultSet rs = Xjdbc.query(sql);
            while (rs.next()) {
                ThanhVien tv = new ThanhVien();
                tv.setMaTV(rs.getString("MaTV"));
                tv.setHoTen(rs.getString("HoTen"));
                tv.setGioiTinh(rs.getString("GioiTinh"));
                tv.setNgayDK(rs.getDate("NgayDangKy"));
                tv.setTuoi(rs.getInt("Tuoi"));
                list.add(tv);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
