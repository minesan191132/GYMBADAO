/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.Product;
import java.util.*;
import java.sql.*;
import utils.Xjdbc;
/**
 *
 * @author trong
 */

public class SanPhamDAO extends GymDAO<Product, String> {

    @Override
    public void insert(Product product) {
        String sql = "INSERT INTO SanPham (MaSP, TenSP, Gia, SoLuong, HinhAnh) VALUES (?, ?, ?, ?, ?)";
        Xjdbc.update(sql,
                product.getMaSanPham(),
                product.getTenSanPham(),
                product.getGia(),
                product.getSoluong(),
                product.getHinhanh());
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE SanPham SET TenSP=?, Gia=?, SoLuong=?, HinhAnh=?, WHERE MaSP=?";
        Xjdbc.update(sql,
                product.getTenSanPham(),
                product.getGia(),
                product.getSoluong(),
                product.getHinhanh(),
                product.getMaSanPham());
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM SanPham WHERE MaSP=?";
        Xjdbc.update(sql, id);
    }

    @Override
    public Product selectById(String id) {
        String sql = "SELECT * FROM SanPham WHERE MaSP=?";
        List<Product> list = this.selectBySql(sql, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Product> selectAll() {
        String sql = "SELECT * FROM SanPham";
        return this.selectBySql(sql);
    }

    public List<Product> searchByName(String keyword) {
        String sql = "SELECT * FROM SanPham WHERE TenSP LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%");
    }

    @Override
    protected List<Product> selectBySql(String sql, Object... args) {
        List<Product> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = Xjdbc.query(sql, args);
                while (rs.next()) {
                    Product product = new Product();
                    product.setMaSanPham(rs.getInt("MaSP"));
                    product.setTenSanPham(rs.getString("TenSP"));
                    product.setGia(rs.getDouble("Gia"));
                    product.setSoluong(rs.getInt("SoLuong"));
                    product.setHinhanh(rs.getString("HinhAnh"));
                    list.add(product);
                }
            } finally {
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return list;
    }
}