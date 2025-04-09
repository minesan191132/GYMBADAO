package dao;

import entity.DonHang;
import utils.Xjdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DonHangDAO {

    // Thêm mới đơn hàng
    public void insert(DonHang dh) {
        String sql = "INSERT INTO DonHang (MaKH, NgayLap, ThanhTien) VALUES (?, ?, ?)";
        Xjdbc.update(sql, dh.getMaKH(), dh.getNgayLap(), dh.getThanhTien());
    }

    // Cập nhật đơn hàng
    public void update(DonHang dh) {
        String sql = "UPDATE DonHang SET MaKH = ?, NgayLap = ?, ThanhTien = ? WHERE MaDH = ?";
        Xjdbc.update(sql, dh.getMaKH(), dh.getNgayLap(), dh.getThanhTien(), dh.getMaDH());
    }

    // Xoá đơn hàng
    public void delete(int maDH) {
        String sql = "DELETE FROM DonHang WHERE MaDH = ?";
        Xjdbc.update(sql, maDH);
    }

    // Lấy toàn bộ đơn hàng
    public List<DonHang> selectAll() {
        String sql = "SELECT * FROM DonHang";
        return selectBySql(sql);
    }

    // Lấy đơn theo ID
    public DonHang selectById(int maDH) {
        String sql = "SELECT * FROM DonHang WHERE MaDH = ?";
        List<DonHang> list = selectBySql(sql, maDH);
        return list.isEmpty() ? null : list.get(0);
    }

    // Lọc theo khoảng ngày
    public List<DonHang> selectByDateRange(Date tuNgay, Date denNgay) {
        String sql = "SELECT * FROM DonHang WHERE NgayLap BETWEEN ? AND ?";
        return selectBySql(sql, tuNgay, denNgay);
    }

    // Lấy dữ liệu từ DB
    private List<DonHang> selectBySql(String sql, Object... args) {
        List<DonHang> list = new ArrayList<>();
        try {
            ResultSet rs = Xjdbc.query(sql, args);
            while (rs.next()) {
                DonHang dh = new DonHang();
                dh.setMaDH(rs.getInt("MaDH"));
                dh.setMaKH(rs.getInt("MaKH"));
                dh.setNgayLap(rs.getDate("NgayLap"));
                dh.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(dh);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi truy vấn DonHang: " + e.getMessage(), e);
        }
        return list;
    }
}
