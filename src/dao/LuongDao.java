package dao;

import entity.LuongNhanVien;
import utils.Xjdbc;
import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.time.LocalDate;

public class LuongDao {

    public List<LuongNhanVien> getLuongNhanVien(String maNhanVien, String thang, String nam) {
        List<LuongNhanVien> list = new ArrayList<>();
        String query;
        PreparedStatement ps = null;

        try {
            // Kiểm tra nếu mã nhân viên là null hoặc rỗng
            if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
                // Lọc theo tháng và năm
                query = "SELECT lu.MaNV, nv.HoTen, lu.NgayLam, lu.GioLam, lu.GioTangCa, lu.DiTre, lu.GhiChu, lu.Luong, cc.CaLamViec, cc.CheckIn, cc.CheckOut "
                        + "FROM Luong lu "
                        + "JOIN NhanVien nv ON lu.MaNV = nv.MaNV "
                        + "JOIN ChamCong cc ON lu.MaNV = cc.MaNV AND lu.NgayLam = cc.Ngay "
                        + "WHERE MONTH(lu.NgayLam) = ? AND YEAR(lu.NgayLam) = ?";
                ps = Xjdbc.getStmt(query, Integer.parseInt(thang), Integer.parseInt(nam));
            } else {
                // Lọc theo tháng, năm và mã nhân viên
                query = "SELECT lu.MaNV, nv.HoTen, lu.NgayLam, lu.GioLam, lu.GioTangCa, lu.DiTre, lu.GhiChu, lu.Luong, cc.CaLamViec, cc.CheckIn, cc.CheckOut "
                        + "FROM Luong lu "
                        + "JOIN NhanVien nv ON lu.MaNV = nv.MaNV "
                        + "JOIN ChamCong cc ON lu.MaNV = cc.MaNV AND lu.NgayLam = cc.Ngay "
                        + "WHERE MONTH(lu.NgayLam) = ? AND YEAR(lu.NgayLam) = ? AND LOWER(lu.MaNV) = LOWER(?)";
                ps = Xjdbc.getStmt(query, Integer.parseInt(thang), Integer.parseInt(nam), maNhanVien.toLowerCase());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LuongNhanVien luong = new LuongNhanVien(
                        rs.getString("MaNV"),
                        rs.getString("HoTen"),
                        rs.getDate("NgayLam").toLocalDate(),
                        rs.getString("CaLamViec"),
                        rs.getString("CheckIn"),
                        rs.getString("CheckOut"),
                        rs.getDouble("GioLam"),
                        rs.getDouble("GioTangCa"),
                        rs.getBoolean("DiTre"),
                        rs.getString("GhiChu"),
                        rs.getInt("Luong")
                );
                list.add(luong);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm thông tin lương cho nhân viên
    public boolean addLuongNhanVien(LuongNhanVien luongNhanVien) {
        String query = "INSERT INTO luong (ma_nhan_vien, ten_nhan_vien, ngay_lam, ca_lam, gio_vao, gio_ra, gio_lam, gio_tang_ca, di_tre, ghi_chu, luong) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = Xjdbc.getStmt(query,
                luongNhanVien.getMaNhanVien(),
                luongNhanVien.getTenNhanVien(),
                Date.valueOf(luongNhanVien.getNgayLam()),
                luongNhanVien.getCaLam(),
                luongNhanVien.getGioVao(),
                luongNhanVien.getGioRa(),
                luongNhanVien.getGioLam(),
                luongNhanVien.getGioTangCa(),
                luongNhanVien.isDiTre(),
                luongNhanVien.getGhiChu(),
                luongNhanVien.getLuong())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật lương của nhân viên
    public boolean updateLuongNhanVien(LuongNhanVien luongNhanVien) {
        String query = "UPDATE luong SET gio_vao = ?, gio_ra = ?, gio_lam = ?, gio_tang_ca = ?, di_tre = ?, ghi_chu = ?, luong = ? "
                + "WHERE ma_nhan_vien = ? AND ngay_lam = ?";

        try (PreparedStatement ps = Xjdbc.getStmt(query,
                luongNhanVien.getGioVao(),
                luongNhanVien.getGioRa(),
                luongNhanVien.getGioLam(),
                luongNhanVien.getGioTangCa(),
                luongNhanVien.isDiTre(),
                luongNhanVien.getGhiChu(),
                luongNhanVien.getLuong(),
                luongNhanVien.getMaNhanVien(),
                Date.valueOf(luongNhanVien.getNgayLam()))) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa thông tin lương nhân viên
    public boolean deleteLuongNhanVien(String maNhanVien, LocalDate ngayLam) {
        String query = "DELETE FROM luong WHERE ma_nhan_vien = ? AND ngay_lam = ?";

        try (PreparedStatement ps = Xjdbc.getStmt(query, maNhanVien, Date.valueOf(ngayLam))) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
