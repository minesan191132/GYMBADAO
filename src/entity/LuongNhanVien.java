/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author A.Koi
 */

import java.time.LocalDate;


public class LuongNhanVien {
    private String maNhanVien;
    private String tenNhanVien;
    private LocalDate ngayLam;
    private String caLam;
    private String gioVao;
    private String gioRa;
    private double gioLam;
    private double gioTangCa;
    private boolean diTre;
    private String ghiChu;
    private int luong;

    // Constructor, Getters and Setters

    public LuongNhanVien(String maNhanVien, String tenNhanVien, LocalDate ngayLam, String caLam, 
                          String gioVao, String gioRa, double gioLam, double gioTangCa, 
                          boolean diTre, String ghiChu, int luong) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.ngayLam = ngayLam;
        this.caLam = caLam;
        this.gioVao = gioVao;
        this.gioRa = gioRa;
        this.gioLam = gioLam;
        this.gioTangCa = gioTangCa;
        this.diTre = diTre;
        this.ghiChu = ghiChu;
        this.luong = luong;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public LocalDate getNgayLam() {
        return ngayLam;
    }

    public void setNgayLam(LocalDate ngayLam) {
        this.ngayLam = ngayLam;
    }

    public String getCaLam() {
        return caLam;
    }

    public void setCaLam(String caLam) {
        this.caLam = caLam;
    }

    public String getGioVao() {
        return gioVao;
    }

    public void setGioVao(String gioVao) {
        this.gioVao = gioVao;
    }

    public String getGioRa() {
        return gioRa;
    }

    public void setGioRa(String gioRa) {
        this.gioRa = gioRa;
    }

    public double getGioLam() {
        return gioLam;
    }

    public void setGioLam(double gioLam) {
        this.gioLam = gioLam;
    }

    public double getGioTangCa() {
        return gioTangCa;
    }

    public void setGioTangCa(double gioTangCa) {
        this.gioTangCa = gioTangCa;
    }

    public boolean isDiTre() {
        return diTre;
    }

    public void setDiTre(boolean diTre) {
        this.diTre = diTre;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getLuong() {
        return luong;
    }

    public void setLuong(int luong) {
        this.luong = luong;
    }

}


