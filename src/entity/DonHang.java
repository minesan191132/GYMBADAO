package entity;

import java.util.Date;

public class DonHang {

    private int maDH;
    private int maKH;
    private String hoTen;
    private Date ngayLap;
    private double thanhTien;

    public DonHang() {
    }

    public DonHang(int maDH, int maKH, String hoTen, Date ngayLap, double thanhTien) {
        this.maDH = maDH;
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.ngayLap = ngayLap;
        this.thanhTien = thanhTien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getMaDH() {
        return maDH;
    }

    public void setMaDH(int maDH) {
        this.maDH = maDH;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
}
