/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Admin
 */
public class GoiTap {

    int MaGoi;
    String TenGoi;
    int ThoiHan;
    double Gia;
    
     @Override
    public String toString() {
        return TenGoi; // Hiển thị tên gói trong ComboBox
    }
    
    public GoiTap() {
    }

    public GoiTap(int MaGoi, String TenGoi, int ThoiHan, double Gia) {
        this.MaGoi = MaGoi;
        this.TenGoi = TenGoi;
        this.ThoiHan = ThoiHan;
        this.Gia = Gia;
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

    public int getThoiHan() {
        return ThoiHan;
    }

    public void setThoiHan(int ThoiHan) {
        this.ThoiHan = ThoiHan;
    }

    public double getGia() {
        return Gia;
    }

    public void setGia(double Gia) {
        this.Gia = Gia;
    }
}
