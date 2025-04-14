/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.math.BigDecimal;

/**
 *
 * @author trong
 */
public class ChiTietDonHang {

    private int maCTDH;  // Sử dụng int thay vì Integer vì IDENTITY bắt đầu từ 1
    private int maDH;
    private int maSP;
    private int soLuong;
    private double gia;  // Sử dụng BigDecimal cho DECIMAL(18,2)

    // Constructors
    public ChiTietDonHang() {
        // Constructor mặc định cần thiết cho JPA/Hibernate
    }

    public ChiTietDonHang(int maDH, int maSP, int soLuong, double gia) {
        this.maDH = maDH;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.gia = gia;
    }

    // Getters and Setters
    public int getMaCTDH() {
        return maCTDH;
    }

    public void setMaCTDH(int maCTDH) {
        this.maCTDH = maCTDH;
    }

    public int getMaDH() {
        return maDH;
    }

    public void setMaDH(int maDH) {
        if (maDH <= 0) {
            throw new IllegalArgumentException("Mã đơn hàng phải là số dương");
        }
        this.maDH = maDH;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        if (maSP <= 0) {
            throw new IllegalArgumentException("Mã sản phẩm phải là số dương");
        }
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        this.soLuong = soLuong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        if (gia <= 0) {
            throw new IllegalArgumentException("Giá phải lớn hơn 0");
        }
        this.gia = Math.round(gia * 100.0) / 100.0; // Làm tròn 2 chữ số thập phân
    }

// Helper method
    public double getThanhTien() {
        return gia * soLuong;
    }

    @Override
    public String toString() {
        return String.format(
                "ChiTietDonHang [maCTDH=%d, maDH=%d, maSP=%d, soLuong=%d, gia=%,.0f]",
                maCTDH, maDH, maSP, soLuong, gia
        );
    }
}
