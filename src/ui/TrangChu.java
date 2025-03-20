package ui;

import javax.swing.*;
import java.awt.*;


public class TrangChu extends JFrame {

    private JPanel mainPanel;


    public TrangChu() {
       setTitle("Gym Management Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        new ManHinhChao(this, true).setVisible(true);
        new DangNhap(this, true).setVisible(true);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(new Color(33, 33, 61));
        sidebar.setBounds(0, 0, 200, 650);
        add(sidebar);

        JLabel lblLogo = new JLabel("<html><span style='color:orange; font-size:35px;'>TF</span>" + "<span style='color:white; font-size:35px;'>C</span></html>", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Agbalumo", Font.BOLD, 35));
        lblLogo.setBounds(0, 10, 200, 60); // Kích thước phù hợp
        lblLogo.setOpaque(true);
        lblLogo.setBackground(new Color(25, 25, 50)); // Nền tối
        lblLogo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Khoảng cách giữa chữ và viền
        sidebar.add(lblLogo);

        String[] menu = {"Tổng Quan", "Khách Hàng", "Đơn Hàng", "Báo Cáo"};
        String[] icons = {"/GYMBADAO/src/icon/Home.png", "/GYMBADAO/src/icon/group-users.png", "/GYMBADAO/src/icon/shopping-bag.png", "/GYMBADAO/src/icon/report.png"};

        int y = 80;
        for (int i = 0; i < menu.length; i++) {
            JButton btn = new JButton(menu[i]);
            btn.setBounds(10, y, 180, 50);
            btn.setBackground(i == 0 ? new Color(255, 200, 0) : new Color(44, 44, 80));
            btn.setForeground(i == 0 ? Color.BLACK : Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setIcon(new ImageIcon(icons[i]));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 10)
            ));
            int index = i;
            btn.addActionListener(e -> {
                switch (index) {
                    case 0 -> showDashboardPanel();
                    case 1 -> showKhachHangPanel();
                    case 2 -> showDonHangPanel();
                    case 3 -> showBaoCaoPanel();
                }
            });
            sidebar.add(btn);
            y += 60;
        }

        JButton btnLogout = new JButton("Log out");
        btnLogout.setBounds(20, 550, 160, 50);
        btnLogout.setBackground(new Color(255, 153, 51));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setIcon(new ImageIcon("/GYMBADAO/src/icon/logout.png"));
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogout.setIconTextGap(15);
        btnLogout.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 10)
        ));
        sidebar.add(btnLogout);
            
        mainPanel = new JPanel();
        mainPanel.setBounds(200, 0, 800, 650);
        mainPanel.setLayout(null);
        add(mainPanel);

        // Default view là Dashboard
        showDashboardPanel();
    }

    private void showDashboardPanel() {
        mainPanel.removeAll();
        mainPanel.add(new DashboardPanel());
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showKhachHangPanel() {
        mainPanel.removeAll();
        mainPanel.add(new KhachHang());
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showDonHangPanel() {
        // Tạm thời bạn có thể để trống
    }

    private void showBaoCaoPanel() {
        // Tạm thời bạn có thể để trống
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrangChu().setVisible(true));
    }
}