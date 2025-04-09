package entity;

import java.util.Date;

public class KhachHang {
    String maTV, hoTen, gioiTinh;
    Date ngayDK;
    String soDT;
    Date ngayKT;
    String goiTap;

    public KhachHang() {
    }

    public KhachHang(String maTV, String hoTen, String gioiTinh, Date ngayDK) {
        this.maTV = maTV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngayDK = ngayDK;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public Date getNgayKT() {
        return ngayKT;
    }

    public void setNgayKT(Date ngayKT) {
        this.ngayKT = ngayKT;
    }
    public String getGoiTap() {
        return goiTap;
    }

    public void setGoiTap(String goiTap) {
        this.goiTap = goiTap;
    }

    public String getMaTV() {
        return maTV;
    }

    public void setMaTV(String maTV) {
        this.maTV = maTV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgayDK() {
        return ngayDK;
    }

    public void setNgayDK(Date ngayDK) {
        this.ngayDK = ngayDK;
    }
}