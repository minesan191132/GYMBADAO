package dao;

import entity.chamCong;
import utils.Xjdbc;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static utils.Xjdbc.getStmt;

public class ChamCongDao {

    public boolean updateCheckOut(String maNV, LocalDate ngay, LocalTime checkOut) {
        String sql = "UPDATE ChamCong SET CheckOut = ? WHERE MaNV = ? AND Ngay = ?";
        try {
            int result = Xjdbc.update(sql, Time.valueOf(checkOut), maNV, Date.valueOf(ngay));
            return result > 0;
        } catch (Exception e) { // <-- dùng Exception để an toàn hơn
            e.printStackTrace();
            return false;
        }
    }

    // Lấy lịch sử chấm công theo mã nhân viên + tháng + năm
    public List<chamCong> findByNhanVien_Thang_Nam(String maNV, int thang, int nam) {
        List<chamCong> list = new ArrayList<>();
        String sql = "SELECT cc.MaNV, nv.TenNV, cc.Ngay, cc.CaLamViec, cc.CheckIn, cc.CheckOut "
                + "FROM ChamCong cc JOIN NhanVien nv ON cc.MaNV = nv.MaNV "
                + "WHERE cc.MaNV = ? AND MONTH(cc.Ngay) = ? AND YEAR(cc.Ngay) = ? ORDER BY cc.Ngay";

        try (ResultSet rs = Xjdbc.query(sql, maNV, thang, nam)) {
            while (rs.next()) {
                chamCong cc = new chamCong();
                cc.setMaNV(rs.getString("MaNV"));
                cc.setTenNV(rs.getString("TenNV"));
                cc.setNgay(rs.getDate("Ngay").toLocalDate());
                cc.setCa(rs.getString("CaLamViec"));
                Time in = rs.getTime("CheckIn");
                Time out = rs.getTime("CheckOut");
                cc.setCheckIn(in != null ? in.toLocalTime() : null);
                cc.setCheckOut(out != null ? out.toLocalTime() : null);
                list.add(cc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Check xem hôm nay đã có chấm công chưa
    public boolean isCheckedIn(String maNV, LocalDate ngay) {
        String sql = "SELECT COUNT(*) FROM ChamCong WHERE MaNV = ? AND Ngay = ?";
        try (ResultSet rs = Xjdbc.query(sql, maNV, Date.valueOf(ngay))) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ghi nhận Check-In
    public boolean insertCheckIn(String maNV, LocalDate ngay, String ca, LocalTime checkIn) {
        // Check if record already exists
        if (isCheckedIn(maNV, ngay)) {
            System.out.println("Chấm công đã tồn tại cho nhân viên này vào ngày " + ngay);
            return false; // Skip insert if already checked in
        }

        String sql = "INSERT INTO ChamCong (MaNV, Ngay, CaLamViec, CheckIn) VALUES (?, ?, ?, ?)";
        try {
            int result = Xjdbc.update(sql, maNV, Date.valueOf(ngay), ca, Time.valueOf(checkIn));
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật Check-Out
    public static int update(String sql, Object... args) {
        try {
            PreparedStatement stmt = getStmt(sql, args);
            try {
                return stmt.executeUpdate(); // ✅ Trả về số dòng bị ảnh hưởng
            } finally {
                stmt.getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Lấy toàn bộ lịch sử chấm công theo mã nhân viên (dùng cho UI load lại bảng)
    public List<chamCong> findAllByNhanVien(String maNV) {
        List<chamCong> list = new ArrayList<>();
        String sql = "SELECT cc.MaNV, nv.TenNV, cc.Ngay, cc.CaLamViec, cc.CheckIn, cc.CheckOut "
                + "FROM ChamCong cc JOIN NhanVien nv ON cc.MaNV = nv.MaNV WHERE cc.MaNV = ? ORDER BY cc.Ngay DESC";
        try (ResultSet rs = Xjdbc.query(sql, maNV)) {
            while (rs.next()) {
                chamCong cc = new chamCong();
                cc.setMaNV(rs.getString("MaNV"));
                cc.setTenNV(rs.getString("TenNV"));
                cc.setNgay(rs.getDate("Ngay").toLocalDate());
                cc.setCa(rs.getString("CaLamViec"));
                Time in = rs.getTime("CheckIn");
                Time out = rs.getTime("CheckOut");
                cc.setCheckIn(in != null ? in.toLocalTime() : null);
                cc.setCheckOut(out != null ? out.toLocalTime() : null);
                list.add(cc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
