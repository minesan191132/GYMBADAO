package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TrangChu extends JFrame {

    private JPanel mainPanel;
    private List<JButton> menuButtons; // Danh sách các nút trong sidebar

    // Lớp nút bo tròn
    class RoundedButton extends JButton {

        private int arcWidth = 20; // Độ bo tròn góc
        private int arcHeight = 20;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            g2.setColor(getForeground());
            g2.setFont(getFont());

            FontMetrics fm = g2.getFontMetrics();
            Rectangle textRect = new Rectangle(getWidth(), getHeight());

            // Vẽ icon nếu có
            if (getIcon() != null) {
                int iconWidth = getIcon().getIconWidth();
                int iconHeight = getIcon().getIconHeight();
                int iconX = 20; // Khoảng cách từ lề trái
                int iconY = (getHeight() - iconHeight) / 2;
                getIcon().paintIcon(this, g2, iconX, iconY);

                // Vẽ text sau icon
                int textX = iconX + iconWidth + getIconTextGap();
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), textX, textY);
            } else {
                // Vẽ text nếu không có icon
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), textX, textY);
            }

            g2.dispose();
        }
    }

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
            RoundedButton btn = new RoundedButton(menu[i]); // Sử dụng RoundedButton thay vì JButton

            // Tải hình ảnh và thay đổi kích thước cho tất cả các icon
            ImageIcon icon = new ImageIcon(icons[i]);
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Đổi kích thước thành 20x20
            icon = new ImageIcon(scaledImage);

            btn.setIcon(icon); // Gán icon đã thay đổi kích thước vào nút
            btn.setBounds(10, y, 180, 50);
            btn.setBackground(i == 0 ? new Color(255, 200, 0) : new Color(44, 44, 80));
            btn.setForeground(i == 0 ? Color.BLACK : Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);
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
                    case 4 ->
                        showBaoCaoPanel();

                }
            });
            sidebar.add(btn);
            menuButtons.add(btn); // Thêm nút vào danh sách
            y += 60;
        }
// Lương và Chấm Công
        RoundedButton roleSpecificButton;
        String buttonText;
        String iconPath;

        if (utils.Auth.isManager()) {
            buttonText = "Tính Lương";
            iconPath = "/GYMBADAO/src/icon/salary.png";
        } else {
            buttonText = "Chấm Công";
            iconPath = "/GYMBADAO/src/icon/check.png";
        }
        roleSpecificButton = new RoundedButton(buttonText);
        ImageIcon icon = new ImageIcon(iconPath);
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        roleSpecificButton.setIcon(icon);
        roleSpecificButton.setBounds(10, y, 180, 50);
        roleSpecificButton.setBackground(new Color(44, 44, 80));
        roleSpecificButton.setForeground(Color.WHITE);
        roleSpecificButton.setFont(new Font("Arial", Font.BOLD, 14));
        roleSpecificButton.setHorizontalAlignment(SwingConstants.LEFT);
        roleSpecificButton.setIconTextGap(15);

// Sự kiện click
        roleSpecificButton.addActionListener(e -> {
            setSelectedButton(menuButtons.indexOf(roleSpecificButton)); // Đổi màu khi chọn
            if (utils.Auth.isManager()) {
                showLuong();
            } else {
                showChamCong();
            }
        });
        sidebar.add(roleSpecificButton);
        menuButtons.add(roleSpecificButton); // Thêm vào danh sách nút

        // Log out
        RoundedButton btnLogout = new RoundedButton("Log out");
        btnLogout.setBounds(20, 550, 160, 50);
        btnLogout.setBackground(new Color(255, 153, 51));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setIcon(new ImageIcon("/GYMBADAO/src/icon/logout.png"));
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogout.setIconTextGap(15);
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
            new TrangChu().setVisible(true);
        } else {
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

    private void showBaoCaoPanel() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new BaoCao(), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showChamCong() {
        this.setVisible(false);
        new ChamCong().setVisible(true);
    }

    private void showLuong() {
        this.setVisible(false);
        new Luong().setVisible(true);
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