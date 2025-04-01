package dao;

import entity.ThanhToan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThanhToanDAO {

    private Connection conn;

    // Constructor không tham số (tạo dữ liệu giả lập)
    public ThanhToanDAO() {
        this.conn = null; // Không kết nối CSDL
    }

    // Constructor có Connection (nếu dùng DB sau này)
    public ThanhToanDAO(Connection conn) {
        this.conn = conn;
    }

    // Hàm lấy danh sách thanh toán giả lập
    public List<ThanhToan> layDanhSachThanhToanTheoPhuongThuc(String method) {
        // Trả về danh sách giả lập thay vì truy vấn DB
        return List.of(
                new ThanhToan(method, 100000),
                new ThanhToan(method, 200000)
        );
    }

    public List<ThanhToan> layDuLieuThongKe(String toString, String toString0) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
