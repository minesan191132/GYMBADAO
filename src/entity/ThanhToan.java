package entity;

public class ThanhToan {
    private String phuongThuc;
    private double soTien;

    // ✅ Constructor không tham số
    public ThanhToan() {
    }

    // ✅ Constructor có tham số
    public ThanhToan(String phuongThuc, double soTien) {
        this.phuongThuc = phuongThuc;
        this.soTien = soTien;
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
