/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ui;

import dao.GymDAO;
import dao.TrangChuDao;
import entity.ThanhVien;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import utils.Auth;
import java.util.List;
import ui.*;

/**
 *
 * @author trong
 */
public class TrangChu extends javax.swing.JFrame {

    private JLabel lblWelcome, lblDoanhThu, lblDonHang, lblThanhVien;
    private JTable tblThanhVien;
    private DefaultTableModel model;
    TrangChuDao dao = new TrangChuDao();


    public TrangChu() {
        setTitle("Gym Management Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        new ManHinhChao(this, true).setVisible(true);
        new DangNhap(this, true).setVisible(true);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(new Color(33, 33, 61));
        sidebar.setBounds(0, 0, 200, 650);
        add(sidebar);

        JLabel lblLogo = new JLabel("Logo", SwingConstants.CENTER);
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setBounds(0, 20, 200, 30);
        sidebar.add(lblLogo);

        String[] menu = {"Tổng quan", "Khách hàng", "Đơn hàng", "Báo Cáo", "Cài Đặt"};
        int y = 80;
        for (String item : menu) {
            JButton btn = new JButton(item);
            btn.setBounds(10, y, 180, 40);
            btn.setBackground(item.equals("Tổng quan") ? new Color(255, 200, 0) : new Color(44, 44, 80));
            btn.setForeground(item.equals("Tổng quan") ? Color.BLACK : Color.WHITE);
            sidebar.add(btn);
            y += 50;
        }

        JButton btnLogout = new JButton("Log out");
        btnLogout.setBounds(20, 550, 160, 40);
        btnLogout.setBackground(new Color(255, 153, 51));
        sidebar.add(btnLogout);

        // Header
        lblWelcome = new JLabel("Welcome back " + Auth.user.getHoTen() + " 👋");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 22));
        lblWelcome.setBounds(220, 20, 400, 30);
        add(lblWelcome);

        // Cards Panel
        JPanel panelCards = new JPanel();
        panelCards.setLayout(new GridLayout(1, 3, 10, 0));
        panelCards.setBounds(220, 60, 500, 80);
        add(panelCards);

        lblDoanhThu = createCard("Doanh thu", "0K", new Color(255, 224, 224));
        lblDonHang = createCard("Đơn hàng", "0", new Color(224, 255, 224));
        lblThanhVien = createCard("Thành viên", "0", new Color(235, 224, 255));

        panelCards.add(lblDoanhThu);
        panelCards.add(lblDonHang);
        panelCards.add(lblThanhVien);

        // Table title
        JLabel lblTitle = new JLabel("Thành viên mới");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(220, 150, 200, 30);
        add(lblTitle);

        // Table
        model = new DefaultTableModel(new String[]{"No", "Date", "ID", "Name", "Age", "Gender"}, 0);
        tblThanhVien = new JTable(model);
        JScrollPane scroll = new JScrollPane(tblThanhVien);
        scroll.setBounds(220, 190, 750, 400);
        add(scroll);

        loadDashboardData();
    }

    private JLabel createCard(String title, String value, Color bg) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(bg);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 20));
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        JLabel wrapper = new JLabel();
        wrapper.setLayout(new BorderLayout());
        wrapper.add(card, BorderLayout.CENTER);

        return lblValue;
    }

    private void loadDashboardData() {
    try {
        // Lấy dữ liệu từ database
        double doanhThu = dao.getTongDoanhThu();
        int donHang = dao.getSoDonHang();
        int thanhVien = dao.getSoThanhVien();

        lblDoanhThu.setText(String.format("%.0fK", doanhThu / 1000));
        lblDonHang.setText(String.valueOf(donHang));
        lblThanhVien.setText(String.valueOf(thanhVien));

        // Load bảng thành viên mới
        List<ThanhVien> list = dao.getThanhVienMoi();

        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có thành viên mới.");
            return; // Thoát sớm nếu danh sách trống
        }

        model.setRowCount(0);  // Xóa dữ liệu cũ

        int i = 1;
        for (ThanhVien tv : list) {
            if (tv != null) {  // Kiểm tra tv có null không
                model.addRow(new Object[]{
                    String.format("%02d", i++),
                    (tv.getNgayDK() != null) ? tv.getNgayDK().toString() : "N/A",
                    (tv.getMaTV() != null) ? tv.getMaTV() : "N/A",
                    (tv.getHoTen() != null) ? tv.getHoTen() : "N/A",
                    tv.getTuoi(),
                    (tv.getGioiTinh() != null) ? tv.getGioiTinh() : "N/A"
                });
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        e.printStackTrace(); // In lỗi chi tiết ra console
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TrangChu().setVisible(true);
        });
    }
}
