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

    public List<Object[]> selectAllWithDetails() {
        String sql = "SELECT dh.MaDH, kh.HoTen, dh.NgayLap, COALESCE(tt.SoTien, 0) AS SoTien " +
                     "FROM DonHang dh " +
                     "JOIN KhachHang kh ON dh.MaKH = kh.MaKH " +
                     "LEFT JOIN ThanhToan tt ON dh.MaDH = tt.MaDH";
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = Xjdbc.query(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("MaDH"),
                    rs.getString("HoTen"),
                    rs.getDate("NgayLap"),
                    rs.getDouble("SoTien")
                };
                list.add(row);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Sửa kiểu tham số từ java.util.Date thành java.sql.Date
    public List<Object[]> selectByDateRange(java.sql.Date startDate, java.sql.Date endDate) {
        String sql = "SELECT dh.MaDH, kh.HoTen, dh.NgayLap, COALESCE(tt.SoTien, 0) AS SoTien " +
                     "FROM DonHang dh " +
                     "JOIN KhachHang kh ON dh.MaKH = kh.MaKH " +
                     "LEFT JOIN ThanhToan tt ON dh.MaDH = tt.MaDH " +
                     "WHERE dh.NgayLap BETWEEN ? AND ?";
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = Xjdbc.query(sql, startDate, endDate);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("MaDH"),
                    rs.getString("HoTen"),
                    rs.getDate("NgayLap"),
                    rs.getDouble("SoTien")
                };
                list.add(row);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}