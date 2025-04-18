package dao;

import entity.ThanhToan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.Xjdbc;

public class ThanhToanDAO {

    // Thêm thanh toán mới
    public void insert(ThanhToan tt) {
        String sql = "INSERT INTO ThanhToan (MaDH, HinhThuc, SoTien, NgayThanhToan) VALUES (?, ?, ?, ?)";
        Xjdbc.update(sql,
                tt.getMaDH(),
                tt.getPhuongThuc(),
                tt.getSoTien(),
                tt.getNgaythanhtoan()
        );
    }

    // Lấy tất cả bản ghi thanh toán
    public List<ThanhToan> selectAll() {
        String sql = "SELECT * FROM ThanhToan";
        return selectBySql(sql);
    }

    // Lấy theo mã đơn hàng
    public ThanhToan selectByMaDH(int maDH) {
        String sql = "SELECT * FROM ThanhToan WHERE MaDH = ?";
        List<ThanhToan> list = selectBySql(sql, maDH);
        return list.size() > 0 ? list.get(0) : null;
    }

    // Lấy danh sách theo phương thức (tiền mặt / chuyển khoản)
    public List<ThanhToan> selectByHinhThuc(String hinhThuc) {
        String sql = "SELECT * FROM ThanhToan WHERE HinhThuc = ?";
        return selectBySql(sql, hinhThuc);
    }

    // Hàm chung xử lý SELECT
    protected List<ThanhToan> selectBySql(String sql, Object... args) {
        List<ThanhToan> list = new ArrayList<>();
        try {
            ResultSet rs = Xjdbc.query(sql, args);
            while (rs.next()) {
                ThanhToan tt = new ThanhToan();
                tt.setMaTT(rs.getInt("MaTT"));
                tt.setMaDH(rs.getInt("MaDH"));
                tt.setPhuongThuc(rs.getString("HinhThuc"));
                tt.setSoTien(rs.getDouble("SoTien"));
                tt.setNgaythanhtoan(rs.getDate("NgayThanhToan"));
                list.add(tt);
            }
            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }
}
