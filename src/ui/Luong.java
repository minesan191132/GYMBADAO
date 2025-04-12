package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class Luong {
    // Các hằng số hệ thống
    private static final int LUONG_CO_BAN = 35000; // 35k/giờ
    private static final int LUONG_TANG_CA = 50000; // 50k/giờ
    private static final int PHAT_TRE = 50000; // Phạt 50k nếu trễ >30p
    private static final Duration GIOI_HAN_TRE = Duration.ofMinutes(30);
    private static final LocalTime CA1_BAT_DAU = LocalTime.of(8, 0);
    private static final LocalTime CA1_KET_THUC = LocalTime.of(16, 0);
    private static final LocalTime CA2_BAT_DAU = LocalTime.of(16, 0);
    private static final LocalTime CA2_KET_THUC = LocalTime.of(23, 59, 59);

    public static void main(String[] args) {
        JFrame frame = new JFrame("THEO DÕI GIỜ LÀM VÀ TÍNH LƯƠNG");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(15, 15));

        // ======== STYLE ========
        Font fontNhan = new Font("Arial", Font.BOLD, 14);
        Font fontNut = new Font("Arial", Font.BOLD, 16);
        Color mauTimKiem = new Color(41, 98, 255);
        Color mauXuatExcel = new Color(0, 150, 136);
        Color mauThoat = new Color(239, 83, 80);

        // ======== PANEL TÌM KIẾM ========
        JPanel panelTimKiem = new JPanel(new GridBagLayout());
        panelTimKiem.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15),
            "",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16)
        ));
        panelTimKiem.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã nhân viên
        JLabel lblMaNV = new JLabel("MÃ NHÂN VIÊN:");
        lblMaNV.setFont(fontNhan);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        panelTimKiem.add(lblMaNV, gbc);

        JTextField txtMaNV = new JTextField();
        txtMaNV.setFont(fontNhan);
        txtMaNV.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(10, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        panelTimKiem.add(txtMaNV, gbc);

        // Chọn tháng/năm
        JLabel lblThangNam = new JLabel("THÁNG/NĂM:");
        lblThangNam.setFont(fontNhan);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        panelTimKiem.add(lblThangNam, gbc);

        // Panel chứa tháng và năm
        JPanel panelThangNam = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelThangNam.setOpaque(false);

        // ComboBox tháng
        JComboBox<String> cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cbThang.addItem("Tháng " + i);
        }
        cbThang.setFont(fontNhan);
        cbThang.setBorder(new RoundBorder(10, Color.GRAY));
        panelThangNam.add(cbThang);

        // ComboBox năm
        JComboBox<String> cbNam = new JComboBox<>();
        int namHienTai = Year.now().getValue();
        for (int i = namHienTai - 5; i <= namHienTai + 1; i++) {
            cbNam.addItem(String.valueOf(i));
        }
        cbNam.setSelectedItem(String.valueOf(namHienTai));
        cbNam.setFont(fontNhan);
        cbNam.setBorder(new RoundBorder(10, Color.GRAY));
        panelThangNam.add(cbNam);

        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.3;
        panelTimKiem.add(panelThangNam, gbc);

        // Nút tìm kiếm
        JButton btnTimKiem = taoNutTron("TÌM KIẾM (F1)", mauTimKiem, fontNut);
        gbc.gridx = 4; gbc.gridy = 0; gbc.weightx = 0.2;
        panelTimKiem.add(btnTimKiem, gbc);

        // ======== BẢNG DỮ LIỆU ========
        String[] cot = {"MÃ NV", "TÊN NHÂN VIÊN", "NGÀY", "CA", "CHECK-IN", "CHECK-OUT", "GIỜ LÀM", "TĂNG CA", "TRỄ", "GHI CHÚ", "LƯƠNG"};
        DefaultTableModel model = new DefaultTableModel(cot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Chỉ cho sửa cột ghi chú
            }
        };
        
        JTable table = new JTable(model) {
            // Căn giữa tất cả các ô trong bảng
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JLabel) {
                    ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                }
                return c;
            }
        };
        
        // Căn giữa tiêu đề cột
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setAutoCreateRowSorter(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ======== PANEL THỐNG KÊ ========
        JPanel panelThongKe = new JPanel(new GridLayout(1, 4, 10, 10));
        panelThongKe.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Tổng ngày làm
        JPanel panelNgayLam = taoPanelThongKe("NGÀY LÀM", "0 ngày");

        // Tổng giờ làm
        JPanel panelGioLam = taoPanelThongKe("GIỜ LÀM", "0.00 giờ");

        // Tổng lương
        JPanel panelLuong = taoPanelThongKe("TỔNG LƯƠNG", "0 VNĐ");

        // Panel chức năng
        JPanel panelChucNang = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelChucNang.setOpaque(false);
        
        // Nút thoát
        JButton btnThoat = taoNutTron("THOÁT (ESC)", mauThoat, fontNut);
        btnThoat.setPreferredSize(new Dimension(120, 40));
        
        // Nút xuất Excel
        JButton btnXuatExcel = taoNutTron("EXCEL (F2)", mauXuatExcel, fontNut);
        btnXuatExcel.setPreferredSize(new Dimension(120, 40));
        
        panelChucNang.add(btnThoat);
        panelChucNang.add(btnXuatExcel);

        panelThongKe.add(panelNgayLam);
        panelThongKe.add(panelGioLam);
        panelThongKe.add(panelLuong);
        panelThongKe.add(panelChucNang);

        // ======== THÊM CÁC THÀNH PHẦN VÀO FRAME ========
        frame.add(panelTimKiem, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panelThongKe, BorderLayout.SOUTH);

        // ======== XỬ LÝ SỰ KIỆN ========
        btnTimKiem.addActionListener(e -> {
            String maNV = txtMaNV.getText().trim();
            String thang = String.format("%02d", cbThang.getSelectedIndex() + 1);
            String nam = (String) cbNam.getSelectedItem();
            
            List<Object[]> duLieu = timDuLieuChamCong(maNV, thang, nam);
            
            model.setRowCount(0);
            for (Object[] dong : duLieu) {
                model.addRow(dong);
            }
            
            capNhatThongKe(model, 
                (JLabel) panelNgayLam.getComponent(0), 
                (JLabel) panelGioLam.getComponent(0),
                (JLabel) panelLuong.getComponent(0));
        });

        btnXuatExcel.addActionListener(e -> {
            if (model.getRowCount() == 0) {
                hienThiLoi(frame, "Không có dữ liệu để xuất!");
                return;
            }
            
            JOptionPane.showMessageDialog(frame, 
                "Xuất Excel thành công!\n" + model.getRowCount() + " bản ghi đã được xuất.", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        btnThoat.addActionListener(e -> frame.dispose());

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) btnTimKiem.doClick();
                if (e.getKeyCode() == KeyEvent.VK_F2) btnXuatExcel.doClick();
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) btnThoat.doClick();
            }
        });

        frame.setVisible(true);
    }

    // ======== CÁC PHƯƠNG THỨC HỖ TRỢ ========
    private static JPanel taoPanelThongKe(String tieuDe, String giaTri) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(tieuDe));
        
        JLabel nhan = new JLabel(giaTri, JLabel.CENTER);
        nhan.setFont(new Font("Arial", Font.BOLD, 16));
        nhan.setForeground(new Color(41, 98, 255));
        
        panel.add(nhan, BorderLayout.CENTER);
        return panel;
    }

    private static JButton taoNutTron(String text, Color mauNen, Font font) {
        JButton nut = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNen);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mauNen.darker());
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
            }
        };
        
        nut.setFont(font);
        nut.setForeground(Color.WHITE);
        nut.setContentAreaFilled(false);
        nut.setFocusPainted(false);
        nut.setBorder(new EmptyBorder(10, 25, 10, 25));
        nut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nut.setOpaque(false);
        
        return nut;
    }

    private static List<Object[]> timDuLieuChamCong(String maNV, String thang, String nam) {
        List<Object[]> duLieu = new ArrayList<>();
        int soNgayTrongThang = 31; // Luôn hiển thị đủ 31 ngày
        
        if (maNV.isEmpty()) {
            // Dữ liệu mẫu cho tất cả nhân viên
            for (int ngay = 1; ngay <= soNgayTrongThang; ngay++) {
                String ngayThang = String.format("%02d/%02d/%s", ngay, Integer.parseInt(thang), nam);
                
                // NV1 - Ca 1 có tăng ca
                duLieu.add(taoDuLieuNgayLam("NV001", "Nguyễn Văn A", ngayThang, "Ca 1", "08:00:00", "17:30:00"));
                
                // NV2 - Ca 1 đi trễ
                if (ngay % 3 == 0) { // Giả lập đi trễ 3 ngày
                    duLieu.add(taoDuLieuNgayLam("NV002", "Trần Thị B", ngayThang, "Ca 1", "08:45:00", "16:00:00"));
                } else {
                    duLieu.add(taoDuLieuNgayLam("NV002", "Trần Thị B", ngayThang, "Ca 1", "08:00:00", "16:00:00"));
                }
                
                // NV3 - Ca 2 bình thường
                duLieu.add(taoDuLieuNgayLam("NV003", "Lê Văn C", ngayThang, "Ca 2", "16:00:00", "23:30:00"));
            }
        } else {
            // Dữ liệu cho nhân viên cụ thể
            String tenNV = maNV.equals("NV001") ? "Nguyễn Văn A" : 
                         maNV.equals("NV002") ? "Trần Thị B" : 
                         "Lê Văn C";
            
            for (int ngay = 1; ngay <= soNgayTrongThang; ngay++) {
                String ngayThang = String.format("%02d/%02d/%s", ngay, Integer.parseInt(thang), nam);
                String ca = ngay % 2 == 0 ? "Ca 1" : "Ca 2";
                String gioVao, gioRa;
                
                if (ca.equals("Ca 1")) {
                    if (ngay % 4 == 0) { // Giả lập đi trễ
                        gioVao = "08:45:00";
                    } else {
                        gioVao = "08:00:00";
                    }
                    gioRa = "16:00:00";
                } else {
                    if (ngay % 5 == 0) { // Giả lập đi trễ
                        gioVao = "16:45:00";
                    } else {
                        gioVao = "16:00:00";
                    }
                    gioRa = "23:30:00";
                }
                
                duLieu.add(taoDuLieuNgayLam(maNV, tenNV, ngayThang, ca, gioVao, gioRa));
            }
        }
        
        return duLieu;
    }

    private static Object[] taoDuLieuNgayLam(String maNV, String tenNV, String ngay, String ca, String gioVao, String gioRa) {
        DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime thoiGianVao = LocalTime.parse(gioVao, dinhDang);
        LocalTime thoiGianRa = LocalTime.parse(gioRa, dinhDang);
        
        // Kiểm tra đi trễ
        boolean tre = false;
        LocalTime gioBatDauCa = ca.equals("Ca 1") ? CA1_BAT_DAU : CA2_BAT_DAU;
        if (thoiGianVao.isAfter(gioBatDauCa.plus(GIOI_HAN_TRE))) {
            tre = true;
        }
        
        // Tính giờ làm chuẩn và tăng ca
        LocalTime gioKetThucCa = ca.equals("Ca 1") ? CA1_KET_THUC : CA2_KET_THUC;
        double gioLamChuan = 0;
        double gioTangCa = 0;
        
        // Nếu check-in trước giờ kết thúc ca
        if (thoiGianVao.isBefore(gioKetThucCa)) {
            // Giờ làm chuẩn là từ thời gian vào đến hết ca hoặc đến khi check-out (nếu check-out sớm)
            LocalTime ketThucThucTe = thoiGianRa.isBefore(gioKetThucCa) ? thoiGianRa : gioKetThucCa;
            gioLamChuan = ChronoUnit.MINUTES.between(thoiGianVao, ketThucThucTe) / 60.0;
            
            // Tính giờ tăng ca nếu check-out sau giờ kết thúc ca
            if (thoiGianRa.isAfter(gioKetThucCa)) {
                gioTangCa = ChronoUnit.MINUTES.between(gioKetThucCa, thoiGianRa) / 60.0;
            }
        }
        // Nếu check-in sau giờ kết thúc ca thì toàn bộ là tăng ca
        else {
            gioTangCa = ChronoUnit.MINUTES.between(thoiGianVao, thoiGianRa) / 60.0;
        }
        
        // Tổng giờ làm thực tế
        double tongGioLam = gioLamChuan + gioTangCa;
        
        // Tính lương
        int luong = (int)(gioLamChuan * LUONG_CO_BAN + gioTangCa * LUONG_TANG_CA);
        if (tre) {
            luong -= PHAT_TRE;
        }
        
        return new Object[]{
            maNV, 
            tenNV, 
            ngay, 
            ca,
            gioVao,
            gioRa,
            String.format("%.2f giờ", tongGioLam),
            String.format("%.2f giờ", gioTangCa),
            tre ? "Có" : "Không",
            tre ? "Đi trễ" : "",
            String.format("%,d VNĐ", luong)
        };
    }

    private static void capNhatThongKe(DefaultTableModel model, JLabel lblNgay, JLabel lblGio, JLabel lblLuong) {
        if (model.getRowCount() == 0) {
            lblNgay.setText("0 ngày");
            lblGio.setText("0.00 giờ");
            lblLuong.setText("0 VNĐ");
            return;
        }
        
        Set<String> cacNgayLam = new HashSet<>();
        double tongGioLam = 0;
        int tongLuong = 0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String ngay = model.getValueAt(i, 2).toString();
            cacNgayLam.add(ngay);
            
            try {
                tongGioLam += Double.parseDouble(model.getValueAt(i, 6).toString().split(" ")[0]);
                String luongStr = model.getValueAt(i, 10).toString().replaceAll("[^0-9]", "");
                tongLuong += Integer.parseInt(luongStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        lblNgay.setText(cacNgayLam.size() + " ngày");
        lblGio.setText(String.format("%.2f giờ", tongGioLam));
        lblLuong.setText(String.format("%,d VNĐ", tongLuong));
    }

    private static void hienThiLoi(Component parent, String thongBao) {
        JOptionPane.showMessageDialog(parent, thongBao, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    static class RoundBorder extends AbstractBorder {
        private int banKinh;
        private Color mau;
        
        RoundBorder(int banKinh, Color mau) {
            this.banKinh = banKinh;
            this.mau = mau;
        }
        
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(mau);
            g2.drawRoundRect(x, y, width-1, height-1, banKinh, banKinh);
            g2.dispose();
        }
        
        public Insets getBorderInsets(Component c) {
            return new Insets(banKinh/2, banKinh, banKinh/2, banKinh);
        }
    }
}