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

        String query = "SELECT * FROM luong WHERE ma_nhan_vien = ? AND MONTH(ngay_lam) = ? AND YEAR(ngay_lam) = ?";

        try (PreparedStatement ps = Xjdbc.getStmt(query, maNhanVien, Integer.parseInt(thang), Integer.parseInt(nam))) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LuongNhanVien luong = new LuongNhanVien(
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ten_nhan_vien"),
                        rs.getDate("ngay_lam").toLocalDate(),
                        rs.getString("ca_lam"),
                        rs.getString("gio_vao"),
                        rs.getString("gio_ra"),
                        rs.getDouble("gio_lam"),
                        rs.getDouble("gio_tang_ca"),
                        rs.getBoolean("di_tre"),
                        rs.getString("ghi_chu"),
                        rs.getInt("luong")
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
