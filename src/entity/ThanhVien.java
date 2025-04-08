/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class ThanhVien {
    int maTV;
    String hoTen, gioiTinh;
    Date ngayDK;
    String soDT;
    Date ngayKT;
    int maGoi;
    String TenGoi;

    public ThanhVien() {
    }

    public ThanhVien(int maTV, String hoTen, String gioiTinh, Date ngayDK, String soDT, Date ngayKT, int maGoi, String TenGoi) {
        this.maTV = maTV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngayDK = ngayDK;
        this.soDT = soDT;
        this.ngayKT = ngayKT;
        this.maGoi = maGoi;
        this.TenGoi = TenGoi;
    }

    public int getMaTV() {
        return maTV;
    }

    public void setMaTV(int maTV) {
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

    public int getMaGoi() {
        return maGoi;
    }

    public void setMaGoi(int maGoi) {
        this.maGoi = maGoi;
    }

    public String getTenGoi() {
        return TenGoi;
    }

    public void setTenGoi(String TenGoi) {
        this.TenGoi = TenGoi;
    }

}