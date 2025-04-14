package entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author A.Koi
 */
public class chamCong {

    private String maNV;
    private String tenNV; 
    private LocalDate ngay;
    private String ca;
    private LocalTime checkIn;
    private LocalTime checkOut;

    public chamCong() {
    }

    public chamCong(String maNV, String tenNV, LocalDate ngay, String ca, LocalTime checkIn, LocalTime checkOut) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.ngay = ngay;
        this.ca = ca;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public LocalDate getNgay() {
        return ngay;
    }

    public void setNgay(LocalDate ngay) {
        this.ngay = ngay;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }
}
