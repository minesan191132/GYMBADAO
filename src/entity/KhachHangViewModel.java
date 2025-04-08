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
public class KhachHangViewModel {

    int MaKH;
    String HoTen;
    String GioiTinh;
    String SoDienThoai;
    Date NgayDangKy;
    Date NgayKetThuc;
    int MaGoi;
    String TenGoi;

    public KhachHangViewModel() {
    }

    public KhachHangViewModel(int MaKH, String HoTen, String GioiTinh, String SoDienThoai, Date NgayDangKy, Date NgayKetThuc, int MaGoi, String TenGoi) {
        this.MaKH = MaKH;
        this.HoTen = HoTen;
        this.GioiTinh = GioiTinh;
        this.SoDienThoai = SoDienThoai;
        this.NgayDangKy = NgayDangKy;
        this.NgayKetThuc = NgayKetThuc;
        this.MaGoi = MaGoi;
        this.TenGoi = TenGoi;
    }

    public int getMaKH() {
        return MaKH;
    }

    public void setMaKH(int MaKH) {
        this.MaKH = MaKH;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String HoTen) {
        this.HoTen = HoTen;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String GioiTinh) {
        this.GioiTinh = GioiTinh;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String SoDienThoai) {
        this.SoDienThoai = SoDienThoai;
    }

    public Date getNgayDangKy() {
        return NgayDangKy;
    }

    public void setNgayDangKy(Date NgayDangKy) {
        this.NgayDangKy = NgayDangKy;
    }

    public Date getNgayKetThuc() {
        return NgayKetThuc;
    }

    public void setNgayKetThuc(Date NgayKetThuc) {
        this.NgayKetThuc = NgayKetThuc;
    }

    public int getMaGoi() {
        return MaGoi;
    }

    public void setMaGoi(int MaGoi) {
        this.MaGoi = MaGoi;
    }

    public String getTenGoi() {
        return TenGoi;
    }

    public void setTenGoi(String TenGoi) {
        this.TenGoi = TenGoi;
    }
}
