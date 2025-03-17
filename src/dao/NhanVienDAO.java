/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.*;
import java.sql.*;
import entity.NhanVien;
import utils.Xjdbc;

/**
 *
 * @author Admin
 */
public class NhanVienDAO extends GymDAO<NhanVien, String> {

    @Override
    public void insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (MaNV, MatKhau, HoTen, VaiTro) VALUES (?, ?, ?, ?)";
        Xjdbc.update(sql,
                nv.getMaNV(),
                nv.getMatKhau(),
                nv.getHoTen(),
                nv.getVaiTro());
    }

    @Override
    public void update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET MatKhau=?, HoTen=?, VaiTro=? WHERE MaNV=?";
        Xjdbc.update(sql,
                nv.getMatKhau(),
                nv.getHoTen(),
                nv.getVaiTro(),
                nv.getMaNV());
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        Xjdbc.update(sql, id);
    }

    @Override
    public NhanVien selectById(String id) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV=?";
        List<NhanVien> list = this.selectBySql(sql, id);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    @Override
    public List<NhanVien> selectAll() {
        String sql = "SELECT * FROM NhanVien";
        return this.selectBySql(sql);
    }

    @Override
    protected List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try (ResultSet rs = Xjdbc.query(sql, args)) {
            while (rs != null && rs.next()) {
                NhanVien entity = new NhanVien();
                entity.setMaNV(rs.getString(1)); // Sửa lại index bắt đầu từ 1
                entity.setMatKhau(rs.getString(2));
                entity.setHoTen(rs.getString(3));
                entity.setVaiTro(rs.getBoolean(4));
                list.add(entity);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return list;
    }
}
