package dao;

import entity.DonHang;
import utils.Xjdbc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonHangDAO {

    public List<DonHang> getDonHangByDateRange(Date fromDate, Date toDate) {
        String sql = "SELECT dh.*, kh.HoTen FROM DonHang dh JOIN KhachHang kh ON dh.MaKH = kh.MaKH "
                + "WHERE dh.NgayLap BETWEEN ? AND ? ORDER BY dh.NgayLap DESC";
        List<DonHang> list = new ArrayList<>();

        try (ResultSet rs = Xjdbc.query(sql, fromDate, toDate)) {
            while (rs.next()) {
                DonHang dh = new DonHang();
                dh.setMaDH(rs.getInt("MaDH"));
                dh.setMaKH(rs.getInt("MaKH"));
                dh.setNgayLap(rs.getDate("NgayLap"));
                dh.setHoTen(rs.getString("HoTen"));
                dh.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(dh);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return list;
    }

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

    public List<DonHang> selectAllWithDetails() {
        String sql = "SELECT dh.MaDH, kh.HoTen, dh.NgayLap, COALESCE(tt.SoTien, 0) AS SoTien "
                + "FROM DonHang dh "
                + "JOIN KhachHang kh ON dh.MaKH = kh.MaKH "
                + "LEFT JOIN ThanhToan tt ON dh.MaDH = tt.MaDH";
        List<DonHang> list = new ArrayList<>(); // Sửa thành List<donHang>
        try {
            ResultSet rs = Xjdbc.query(sql);
            while (rs.next()) {
                // Tạo đối tượng donHang từ kết quả ResultSet
                DonHang dh = new DonHang();
                dh.setMaDH(rs.getInt("MaDH"));
                dh.setHoTen(rs.getString("HoTen")); // Giả sử có phương thức này
                dh.setNgayLap(rs.getDate("NgayLap"));
                dh.setThanhTien(rs.getDouble("SoTien")); // Giả sử có phương thức này
                list.add(dh);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // Sửa kiểu tham số từ java.util.Date thành java.sql.Date
    public List<Object[]> selectByDateRange(java.sql.Date startDate, java.sql.Date endDate) {
        String sql = "SELECT dh.MaDH, kh.HoTen, dh.NgayLap, COALESCE(tt.SoTien, 0) AS SoTien "
                + "FROM DonHang dh "
                + "JOIN KhachHang kh ON dh.MaKH = kh.MaKH "
                + "LEFT JOIN ThanhToan tt ON dh.MaDH = tt.MaDH "
                + "WHERE dh.NgayLap BETWEEN ? AND ?";
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
    
    public List<DonHang> selectByDateRangetest(java.sql.Date startDate, java.sql.Date endDate) {
        String sql = "SELECT dh.MaDH, kh.HoTen, dh.NgayLap, dh.ThanhTien "
                + "FROM DonHang dh "
                + "JOIN KhachHang kh ON dh.MaKH = kh.MaKH "
                + "WHERE dh.NgayLap BETWEEN ? AND ?";

        List<DonHang> list = new ArrayList<>();
        try {
            ResultSet rs = Xjdbc.query(sql, startDate, endDate);
            while (rs.next()) {
                DonHang dh = new DonHang();
                dh.setMaDH(rs.getInt("MaDH"));
                dh.setHoTen(rs.getString("HoTen"));
                dh.setNgayLap(rs.getDate("NgayLap"));
                dh.setThanhTien(rs.getDouble("ThanhTien")); // 🟢 lấy trực tiếp từ DonHang
                list.add(dh);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<DonHang> selectAll() {
        String sql = "SELECT MaDH, MaKH, NgayLap, ThanhTien FROM DonHang";
        List<DonHang> list = new ArrayList<>();

        try {
            ResultSet rs = Xjdbc.query(sql);
            while (rs.next()) {
                DonHang dh = new DonHang();
                dh.setMaDH(rs.getInt("MaDH"));
                dh.setMaKH(rs.getInt("MaKH"));
                dh.setNgayLap(rs.getDate("NgayLap"));
                dh.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(dh);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

}
