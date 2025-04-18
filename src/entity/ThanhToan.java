package entity;

import java.util.Date;

public class ThanhToan {
    private int MaTT;
    private int MaDH;
    private String phuongThuc;
    private double soTien;
    private Date ngaythanhtoan;

    // ✅ Constructor không tham số
    public ThanhToan() {
    }

    // ✅ Constructor có tham số

    public ThanhToan(int MaTT, int MaDH, String phuongThuc, double soTien, Date ngaythanhtoan) {
        this.MaTT = MaTT;
        this.MaDH = MaDH;
        this.phuongThuc = phuongThuc;
        this.soTien = soTien;
        this.ngaythanhtoan = ngaythanhtoan;
    }

    public int getMaTT() {
        return MaTT;
    }

    public void setMaTT(int MaTT) {
        this.MaTT = MaTT;
    }

    public int getMaDH() {
        return MaDH;
    }

    public void setMaDH(int MaDH) {
        this.MaDH = MaDH;
    }

    public Date getNgaythanhtoan() {
        return ngaythanhtoan;
    }

    public void setNgaythanhtoan(Date ngaythanhtoan) {
        this.ngaythanhtoan = ngaythanhtoan;
    }
    

    // ✅ Getter và Setter
    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public double getSoTien() {
        return soTien;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    // ✅ Ghi đè phương thức `toString()` để dễ debug
    @Override
    public String toString() {
        return "ThanhToan{" +
                "phuongThuc='" + phuongThuc + '\'' +
                ", soTien=" + soTien +
                '}';
    }
}
