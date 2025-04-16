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

    // Phương thức insertCheckIn - Thêm check-in
    public boolean insertCheckIn(String maNV, LocalDate ngay, String ca, LocalTime checkIn) {
        // Kiểm tra đã check-in chưa
        if (isCheckedIn(maNV, ngay)) {
            System.out.println("⚠️ Đã check-in hôm nay rồi: " + maNV + " - " + ngay);
            return false;
        }

        // Chỉ insert vào ChamCong, không động đến bảng Luong
        String sql = "INSERT INTO ChamCong (MaNV, Ngay, CaLamViec, CheckIn) VALUES (?, ?, ?, ?)";
        try {
            System.out.println("📥 INSERT check-in: " + maNV + " | " + ngay + " | " + ca + " | " + checkIn);
            int result = Xjdbc.update(sql, maNV, Date.valueOf(ngay), ca, Time.valueOf(checkIn));
            return result > 0;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi insertCheckIn: " + e.getMessage());
            throw new RuntimeException(e); // Ném ngoại lệ để xử lý ở tầng UI
        }
    }

    // Phương thức updateCheckOut - Cập nhật giờ ra
    public boolean updateCheckOut(String maNV, LocalDate ngay, LocalTime checkOut) {
        String updateSql = "UPDATE ChamCong SET CheckOut = ? WHERE MaNV = ? AND Ngay = ? AND CheckOut IS NULL";
        int updated = Xjdbc.update(updateSql, Time.valueOf(checkOut), maNV, Date.valueOf(ngay));

        if (updated > 0) {
            chamCong cc = findByMaNVAndNgay(maNV, ngay);
            if (cc != null && cc.getCheckIn() != null) {
                insertLuongFromChamCong(cc);  // Chỉ gọi nếu cập nhật CheckOut thành công
                return true;
            }
        }
        return false;
    }

    // Tìm bản ghi chấm công theo mã nhân viên và ngày
    public chamCong findByMaNVAndNgay(String maNV, LocalDate ngay) {
        String sql = "SELECT cc.MaNV, nv.HoTen AS TenNV, cc.Ngay, cc.CaLamViec, cc.CheckIn, cc.CheckOut "
                + "FROM ChamCong cc JOIN NhanVien nv ON cc.MaNV = nv.MaNV "
                + "WHERE cc.MaNV = ? AND cc.Ngay = ?";
        try (ResultSet rs = Xjdbc.query(sql, maNV, Date.valueOf(ngay))) {
            if (rs.next()) {
                chamCong cc = new chamCong();
                cc.setMaNV(rs.getString("MaNV"));
                cc.setTenNV(rs.getString("TenNV"));
                cc.setNgay(rs.getDate("Ngay").toLocalDate());
                cc.setCa(rs.getString("CaLamViec"));
                Time checkIn = rs.getTime("CheckIn");
                Time checkOut = rs.getTime("CheckOut");
                cc.setCheckIn(checkIn != null ? checkIn.toLocalTime() : null);
                cc.setCheckOut(checkOut != null ? checkOut.toLocalTime() : null);
                return cc;
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi tìm bản ghi ChamCong: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức insertLuongFromChamCong - Cập nhật lương từ chấm công
    public void insertLuongFromChamCong(chamCong cc) {
        if (cc.getCheckOut() == null) {
            System.err.println("⚠️ Không thể insert lương vì thiếu GioRa");
            return;
        }

        final int LUONG_CO_BAN = 35000;
        final int LUONG_TANG_CA = 50000;
        final int PHAT_DI_TRE = 50000;

        double gioLam = java.time.Duration.between(cc.getCheckIn(), cc.getCheckOut()).toMinutes() / 60.0;
        double gioTangCa = 0;
        boolean diTre = false;
        String ghiChu = "";

        LocalTime start = cc.getCa().equals("CA1") ? LocalTime.of(8, 0) : LocalTime.of(16, 0);
        LocalTime end = cc.getCa().equals("CA1") ? LocalTime.of(16, 0) : LocalTime.of(23, 59);

        if (cc.getCheckIn().isAfter(start.plusMinutes(30))) {
            diTre = true;
            ghiChu = "Đi trễ, phạt 50k";
        }

        if (cc.getCheckOut().isAfter(end)) {
            gioTangCa = java.time.Duration.between(end, cc.getCheckOut()).toMinutes() / 60.0;
        }

        int tongLuong = (int) (gioLam * LUONG_CO_BAN + gioTangCa * LUONG_TANG_CA - (diTre ? PHAT_DI_TRE : 0));

        // Kiểm tra xem bản ghi đã tồn tại chưa
        String checkSql = "SELECT COUNT(*) FROM Luong WHERE MaNV = ? AND NgayLam = ?";
        try (ResultSet rs = Xjdbc.query(checkSql, cc.getMaNV(), cc.getNgay())) {
            if (rs.next() && rs.getInt(1) > 0) {
                // Nếu đã tồn tại, cập nhật bản ghi
                String updateSql = "UPDATE Luong SET GioLam = ?, GioTangCa = ?, DiTre = ?, GhiChu = ?, Luong = ? WHERE MaNV = ? AND NgayLam = ?";
                Xjdbc.update(updateSql, gioLam, gioTangCa, diTre, ghiChu, tongLuong, cc.getMaNV(), cc.getNgay());
                System.out.println("✅ Đã cập nhật bản ghi lương cho " + cc.getMaNV());
            } else {
                // Nếu chưa tồn tại, thêm bản ghi mới
                String insertSql = "INSERT INTO Luong (MaNV, NgayLam, GioLam, GioTangCa, DiTre, GhiChu, Luong) VALUES (?, ?, ?, ?, ?, ?, ?)";
                Xjdbc.update(insertSql, cc.getMaNV(), cc.getNgay(), gioLam, gioTangCa, diTre, ghiChu, tongLuong);
                System.out.println("✅ Đã thêm bản ghi lương cho " + cc.getMaNV());
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi kiểm tra hoặc cập nhật bản ghi lương: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Kiểm tra xem nhân viên đã check-in chưa
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

    // Lấy danh sách chấm công theo mã nhân viên và tháng/năm
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

    // Lấy tất cả chấm công của nhân viên
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

    public List<chamCong> getAllCheckIns(String maNV) {
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

    // Cập nhật SQL thủ công nếu cần
    public static int update(String sql, Object... args) {
        try {
            PreparedStatement stmt = getStmt(sql, args);
            try {
                return stmt.executeUpdate();
            } finally {
                stmt.getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
