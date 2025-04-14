/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.GoiTap;
import entity.KhachHangViewModel;
import entity.ThanhVien;
import java.util.ArrayList;
import java.util.List;
import utils.Xjdbc;
import java.sql.*;

/**
 *
 * @author Admin
 */
public class KhachHangDao {

    public void insert(ThanhVien kh) {
        String sql = "INSERT INTO KhachHang (HoTen, GioiTinh, SoDienThoai, NgayDangKy, MaGoi) VALUES (?, ?, ?, ?, ?)";
        Xjdbc.update(sql,
                kh.getHoTen(),
                kh.getGioiTinh(),
                kh.getSoDT(),
                kh.getNgayDK(),
                kh.getMaGoi()); // Lưu mã gói dạng số
    }

    public void update(ThanhVien kh) {
        String sql = "UPDATE KhachHang SET HoTen=?, GioiTinh=?, SoDienThoai=?, NgayDangKy=?, MaGoi=? WHERE MaKH=?";
        Xjdbc.update(sql,
                kh.getHoTen(),
                kh.getGioiTinh(),
                kh.getSoDT(),
                kh.getNgayDK(),
                kh.getMaGoi(),
                kh.getMaTV());
    }

    public ThanhVien selectByHoTen(String hoTen) {
        String sql = "SELECT * FROM KhachHang WHERE HoTen = ?";
        List<ThanhVien> list = this.selectBySql(sql, hoTen);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<ThanhVien> selectAll() {
        String sql = "SELECT * FROM KhachHang";
        return this.selectBySql(sql);
    }

    protected List<ThanhVien> selectBySql(String sql, Object... args) {
        List<ThanhVien> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = Xjdbc.query(sql, args);
                while (rs.next()) {
                    ThanhVien entity = new ThanhVien();
                    entity.setMaTV(rs.getInt("MaKH"));
                    entity.setHoTen(rs.getString("HoTen"));
                    entity.setGioiTinh(rs.getString("GioiTinh"));
                    entity.setSoDT(rs.getString("SoDienThoai"));
                    entity.setNgayDK(rs.getDate("NgayDangKy"));
                    entity.setNgayKT(rs.getDate("NgayKetThuc"));
                    entity.setMaGoi(rs.getInt("MaGoi"));
                    list.add(entity);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return list;
    }

    public List<KhachHangViewModel> getAllForDisplay() {
        String sql = "SELECT kh.*, gt.TenGoi FROM KhachHang kh JOIN GoiTap gt ON kh.MaGoi = gt.MaGoi";
        List<KhachHangViewModel> list = new ArrayList<>();

        try (ResultSet rs = Xjdbc.query(sql)) {
            while (rs.next()) {
                KhachHangViewModel vm = new KhachHangViewModel();
                vm.setMaKH(rs.getInt("MaKH"));
                vm.setHoTen(rs.getString("HoTen"));
                vm.setGioiTinh(rs.getString("GioiTinh"));
                vm.setSoDienThoai(rs.getString("SoDienThoai"));
                vm.setNgayDangKy(rs.getDate("NgayDangKy"));
                vm.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                vm.setMaGoi(rs.getInt("MaGoi"));
                vm.setTenGoi(rs.getString("TenGoi")); // Lấy tên gói để hiển thị
                list.add(vm);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return list;
    }

    // 3. Phương thức lấy danh sách gói tập cho ComboBox
    public List<GoiTap> getAllGoiTap() {
        String sql = "SELECT * FROM GoiTap";
        List<GoiTap> list = new ArrayList<>();

        try (ResultSet rs = Xjdbc.query(sql)) {
            while (rs.next()) {
                GoiTap gt = new GoiTap();
                gt.setMaGoi(rs.getInt("MaGoi"));
                gt.setTenGoi(rs.getString("TenGoi"));
                gt.setThoiHan(rs.getInt("ThoiHan"));
                gt.setGia(rs.getDouble("Gia"));
                list.add(gt);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public GoiTap getGoiTapById(int maGoi) {
        String sql = "SELECT * FROM GoiTap WHERE MaGoi=?";
        try {
            ResultSet rs = Xjdbc.query(sql, maGoi);
            if (rs.next()) {
                GoiTap gt = new GoiTap();
                gt.setMaGoi(rs.getInt("MaGoi"));
                gt.setTenGoi(rs.getString("TenGoi"));
                gt.setThoiHan(rs.getInt("ThoiHan"));
                gt.setGia(rs.getDouble("Gia"));
                return gt;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<ThanhVien> searchCustomers(String keyword) {
    List<ThanhVien> list = new ArrayList<>();
    String sql = "SELECT * FROM KhachHang WHERE HoTen LIKE ? OR SoDienThoai LIKE ?";
    
    try {
        // Thêm % vào từ khóa để tìm kiếm phần trùng khớp
        String searchPattern = "%" + keyword + "%";
        ResultSet rs = Xjdbc.query(sql, searchPattern, searchPattern);
        
        while (rs.next()) {
            ThanhVien kh = new ThanhVien();
            kh.setMaTV(rs.getInt("MaKH"));
            kh.setHoTen(rs.getString("HoTen"));
            kh.setSoDT(rs.getString("SoDienThoai"));
            kh.setGioiTinh(rs.getString("GioiTinh"));
            list.add(kh);
        }
        
        rs.getStatement().getConnection().close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    
    return list;
}

    public void capNhatTrangThaiHetHan() {
        String sql = "{call sp_CapNhatTrangThai}";
        Xjdbc.update(sql); // dùng class Xjdbc giống như các hàm khác
    }
}
