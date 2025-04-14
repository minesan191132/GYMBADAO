package dao;

import entity.LuongNhanVien;
import utils.Xjdbc;
import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.time.LocalDate;

public class LuongDao {

    // Lấy danh sách nhân viên lương theo mã nhân viên, tháng và năm
    public List<LuongNhanVien> getLuongNhanVien(String maNhanVien, String thang, String nam) {
        List<LuongNhanVien> list = new ArrayList<>();

        String query = "SELECT * FROM Luong WHERE MONTH(NgayLam) = ? AND YEAR(NgayLam) = ?";

        // Nếu mã nhân viên không rỗng, thêm điều kiện vào query
        if (!maNhanVien.isEmpty()) {
            query += " AND MaNV = ?";
        }

        try (PreparedStatement ps = Xjdbc.getStmt(query, Integer.parseInt(thang), Integer.parseInt(nam), maNhanVien.isEmpty() ? null : maNhanVien)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LuongNhanVien luong = new LuongNhanVien(
                        rs.getString("MaNV"), // Chắc chắn cột này trong SQL là MaNV
                        rs.getString("TenNV"), // Tên cột đúng trong cơ sở dữ liệu
                        rs.getDate("NgayLam").toLocalDate(),
                        rs.getString("CaLam"),
                        rs.getString("GioVao"),
                        rs.getString("GioRa"),
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
