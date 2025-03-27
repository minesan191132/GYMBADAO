package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import ui.BanHang;

public class TrangChu extends JFrame {

    private JPanel mainPanel;
    private List<JButton> menuButtons; // Danh sách các nút trong sidebar

    public TrangChu() {
        setTitle("Gym Management Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(new Color(33, 33, 61));
        sidebar.setBounds(0, 0, 200, 650);
        add(sidebar);

        JLabel lblLogo = new JLabel(new ImageIcon("/GYMBADAO/src/icon/logo.png"));
        lblLogo.setBounds(0, 10, 200, 60); // Kích thước phù hợp
        lblLogo.setOpaque(true);
        lblLogo.setBackground(new Color(25, 25, 50)); // Nền tối
        lblLogo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Khoảng cách giữa chữ và viền
        sidebar.add(lblLogo);

        String[] menu = {"Tổng Quan", "Khách Hàng", "Đơn Hàng", "Bán Hàng", "Báo Cáo"};
        String[] icons = {"/GYMBADAO/src/icon/home.png", "/GYMBADAO/src/icon/contact-list.png", "/GYMBADAO/src/icon/guest-list.png", "/GYMBADAO/src/icon/add-to-basket.png", "/GYMBADAO/src/icon/sales.png"};

        menuButtons = new ArrayList<>(); // Khởi tạo danh sách các nút

        int y = 80;
        for (int i = 0; i < menu.length; i++) {
            JButton btn = new JButton(menu[i]);

            // Tải hình ảnh và thay đổi kích thước cho tất cả các icon
            ImageIcon icon = new ImageIcon(icons[i]);
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Đổi kích thước thành 20x20
            icon = new ImageIcon(scaledImage);

            btn.setIcon(icon); // Gán icon đã thay đổi kích thước vào nút
            btn.setBounds(10, y, 180, 50);
            btn.setBackground(i == 0 ? new Color(255, 200, 0) : new Color(44, 44, 80));
            btn.setForeground(i == 0 ? Color.BLACK : Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 10)
            ));
            int index = i;
            btn.addActionListener(e -> {
                setSelectedButton(index); // Đặt nút được chọn
                switch (index) {
                    case 0 ->
                        showDashboardPanel();
                    case 1 ->
                        showKhachHangPanel();
                    case 2 ->
                        showDonHangPanel();
                    case 3 ->
                        showBanHangPanel();
                }
            });
            sidebar.add(btn);
            menuButtons.add(btn); // Thêm nút vào danh sách
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

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean confirm = utils.MsgBox.confirm(TrangChu.this, "Bạn có chắc chắn muốn đăng xuất?");
                if (confirm) {
                    // Clear thông tin người dùng
                    utils.Auth.clear();

                    // Đóng giao diện hiện tại (Trang chủ)
                    dispose();

                    // Quay về login và kiểm tra đăng nhập lại
                    showLoginAndOpenTrangChu();
                }
            }
        });

        mainPanel = new JPanel();
        mainPanel.setBounds(200, 0, 800, 650);
        mainPanel.setLayout(null);
        add(mainPanel);

        // Default view là Dashboard
        showDashboardPanel();
    }

    private void showLoginAndOpenTrangChu() {
        DangNhap dn = new DangNhap(null, true);
        dn.setVisible(true);

        if (utils.Auth.isLogin()) {
            // Sau khi login thành công sẽ mở lại TrangChu
            new TrangChu().setVisible(true);
        } else {
            // Nếu không đăng nhập thì thoát hẳn chương trình
            System.exit(0);
        }
    }

    private void setSelectedButton(int index) {
        for (int i = 0; i < menuButtons.size(); i++) {
            JButton btn = menuButtons.get(i);
            if (i == index) {
                btn.setBackground(new Color(255, 200, 0)); // Màu vàng cho nút được chọn
                btn.setForeground(Color.BLACK);
            } else {
                btn.setBackground(new Color(44, 44, 80)); // Màu mặc định cho các nút khác
                btn.setForeground(Color.WHITE);
            }
        }
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
        mainPanel.removeAll();
        mainPanel.add(new DonHang());
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showBanHangPanel() {
        this.setVisible(false);
        new BanHang().setVisible(true);
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManHinhChao(null, true);

            DangNhap dn = new DangNhap(null, true);
            dn.setVisible(true);

            TrangChu tc = new TrangChu();
            tc.setVisible(true);
        });
    }
}
