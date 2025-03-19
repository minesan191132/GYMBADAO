package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class KhachHang extends JFrame {
    public KhachHang() {
        setTitle("Thành viên Dashboard");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(new Color(33, 33, 61));
        sidebar.setBounds(0, 0, 200, 650);
        add(sidebar);

        JLabel lblLogo = new JLabel("TFC", SwingConstants.CENTER);
        lblLogo.setForeground(Color.ORANGE);
        lblLogo.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));
        lblLogo.setBounds(0, 20, 200, 30);
        sidebar.add(lblLogo);

        String[] menu = {"Tổng quan", "Khách hàng", "Đơn hàng", "Báo Cáo"};
        String[] icons = {"/GYMBADAO/src/icon/Home.png", "/GYMBADAO/src/icon/group-users.png", "/GYMBADAO/src/icon/shopping-bag.png", "/GYMBADAO/src/icon/report.png"};

        int y = 80;
        for (int i = 0; i < menu.length; i++) {
            JButton btn = new JButton(menu[i]);
            btn.setBounds(10, y, 180, 50);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setIcon(new ImageIcon(icons[i]));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);

            if (i == 1) {
                btn.setBackground(new Color(255, 200, 0));
                btn.setForeground(Color.BLACK);
            } else {
                btn.setBackground(new Color(44, 44, 80));
                btn.setForeground(Color.WHITE);
            }

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
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(210, 20, 750, 600);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        add(mainPanel);

        JLabel lblTitle = new JLabel("Quản Lý Khách Hàng", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(33, 33, 61));
        lblTitle.setBounds(0, 10, 750, 40);
        mainPanel.add(lblTitle);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBounds(50, 60, 650, 450);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        mainPanel.add(formPanel);

        // Các trường nhập
        JTextField txtMaTV = new RoundTextField("Mã thành viên");
        JTextField txtTenTV = new RoundTextField("Tên thành viên");
        JTextField txtNgayDK = new RoundTextField("Ngày đăng ký");

        txtMaTV.setBounds(50, 40, 250, 40);
        txtTenTV.setBounds(50, 100, 250, 40);
        txtNgayDK.setBounds(50, 160, 250, 40);

        // Giới tính (Radio Button)
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setBounds(350, 40, 100, 30);
        JRadioButton rbNam = new JRadioButton("Nam");
        JRadioButton rbNu = new JRadioButton("Nữ");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(rbNam);
        genderGroup.add(rbNu);
        rbNam.setBounds(420, 40, 70, 30);
        rbNu.setBounds(500, 40, 70, 30);

        // Dropdown Gói Tập
        JLabel lblGoiTap = new JLabel("Gói tập:");
        lblGoiTap.setBounds(350, 100, 100, 30);
        String[] goiTap = {"Chọn gói tập", "A", "B", "C"};
        JComboBox<String> cboGoiTap = new JComboBox<>(goiTap);
        cboGoiTap.setBounds(420, 100, 150, 35);
        cboGoiTap.setSelectedIndex(0);

        // Ghi chú
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setBounds(50, 220, 100, 30);
        JTextArea txtGhiChu = new JTextArea();
        txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        scrollGhiChu.setBounds(50, 250, 520, 80);
        scrollGhiChu.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true));

        // Nút Lưu có màu gradient
        JButton btnLuu = new JButton("Lưu") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 153, 51), getWidth(), getHeight(), new Color(255, 94, 58));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLuu.setBounds(270, 350, 120, 45);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 14));
        btnLuu.setContentAreaFilled(false);
        btnLuu.setBorder(BorderFactory.createEmptyBorder());
        
        // Thêm các thành phần vào form
        formPanel.add(txtMaTV);
        formPanel.add(txtTenTV);
        formPanel.add(txtNgayDK);
        formPanel.add(lblGioiTinh);
        formPanel.add(rbNam);
        formPanel.add(rbNu);
        formPanel.add(lblGoiTap);
        formPanel.add(cboGoiTap);
        formPanel.add(lblGhiChu);
        formPanel.add(scrollGhiChu);
    }

    class RoundTextField extends JTextField {
        public RoundTextField(String text) {
            super(text);
            setOpaque(false);
            setFont(new Font("Arial", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setColor(new Color(180, 180, 180));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KhachHang().setVisible(true));
    }
}