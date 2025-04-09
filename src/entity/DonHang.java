package entity;

import java.util.Date;

public class DonHang {
    private int maDH;
    private int maKH;
    private Date ngayLap;
    private double thanhTien;

    public DonHang() {}

    public DonHang(int maDH, int maKH, Date ngayLap, double thanhTien) {
        this.maDH = maDH;
        this.maKH = maKH;
        this.ngayLap = ngayLap;
        this.thanhTien = thanhTien;
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
