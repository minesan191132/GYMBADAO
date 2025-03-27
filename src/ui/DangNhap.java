package ui;

import dao.NhanVienDAO;
import entity.NhanVien;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utils.*;

public class DangNhap extends javax.swing.JDialog {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    private JLabel eyeIcon;
    private boolean isPasswordVisible = false;
    NhanVienDAO dao = new NhanVienDAO();

    public DangNhap(Frame owner, boolean modal) {
        super((Frame) owner, modal);
        setTitle("Gym Login");
        setSize(1116, 627);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLayout(null);

        JLabel background = new JLabel(new ImageIcon("/GYMBADAO/src/icon/backgroundDangNhap.jpg"));
        background.setBounds(0, 0, 1116, 627);
        add(background);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(499, 120, 200, 50);
        background.add(titleLabel);

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(null);
        int formWidth = 450;
        int formHeight = 250;
        formPanel.setBounds((1113 - formWidth) / 2, (624 - formHeight) / 2 + 50, formWidth, formHeight);
        background.add(formPanel);

        // Username - Panel bo tròn với viền bo tròn
        RoundedPanel userPanel = new RoundedPanel(25, new Color(70, 70, 110));
        userPanel.setLayout(new BorderLayout(10, 0));
        userPanel.setBounds(0, 0, formWidth, 50);
        userPanel.setBorder(new RoundedBorder(new Color(102, 0, 102), 2, 25)); // Viền bo tròn
        formPanel.add(userPanel);

        JLabel userIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/user.png"));
        userPanel.add(userIcon, BorderLayout.WEST);

        txtUsername = new JTextField();
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setBackground(new Color(70, 70, 110));
        txtUsername.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        txtUsername.setCaretColor(Color.WHITE);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsername.setOpaque(false);
        userPanel.add(txtUsername, BorderLayout.CENTER);

        // Password - Panel bo tròn với viền bo tròn
        RoundedPanel passPanel = new RoundedPanel(25, new Color(70, 70, 110));
        passPanel.setLayout(new BorderLayout(10, 0));
        passPanel.setBounds(0, 70, formWidth, 50);
        passPanel.setBorder(new RoundedBorder(new Color(102, 0, 102), 2, 25)); // Viền bo tròn
        formPanel.add(passPanel);

        JLabel passIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/password.png"));
        passPanel.add(passIcon, BorderLayout.WEST);

        txtPassword = new JPasswordField();
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setBackground(new Color(70, 70, 110));
        txtPassword.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setOpaque(false);
        passPanel.add(txtPassword, BorderLayout.CENTER);

        eyeIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/hidden.png"));
        eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JPanel eyePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        eyePanel.setOpaque(false);
        eyePanel.add(eyeIcon);

        passPanel.add(eyePanel, BorderLayout.EAST);

        eyeIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                isPasswordVisible = !isPasswordVisible;
                txtPassword.setEchoChar(isPasswordVisible ? (char) 0 : '•');
                eyeIcon.setIcon(new ImageIcon(isPasswordVisible
                        ? "/GYMBADAO/src/icon/view.png"
                        : "/GYMBADAO/src/icon/hidden.png"));
            }
        });

        // Buttons với viền bo tròn
        btnLogin = new RoundedButton("Đăng Nhập", 20);
        btnLogin.setBounds(70, 150, 150, 40);
        styleButton(btnLogin);
        formPanel.add(btnLogin);

        btnExit = new RoundedButton("Thoát", 20);
        btnExit.setBounds(250, 150, 150, 40);
        styleButton(btnExit);
        formPanel.add(btnExit);

        btnExit.addActionListener(e -> ketThuc());
        btnLogin.addActionListener(e -> xuLyDangNhap());
    }

    // Class RoundedPanel để tạo panel bo tròn
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int cornerRadius, Color backgroundColor) {
            super();
            this.cornerRadius = cornerRadius;
            this.backgroundColor = backgroundColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }
    }

    // Class RoundedBorder để tạo viền bo tròn
    class RoundedBorder implements javax.swing.border.Border {
        private Color color;
        private int thickness;
        private int radius;

        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.thickness, this.thickness, this.thickness, this.thickness);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    // Class RoundedButton để tạo nút bo tròn
    class RoundedButton extends JButton {
        private int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }
            
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }
    }

    private void ketThuc() {
        if (MsgBox.confirm(this, "Bạn muốn kết thúc ứng dụng?")) {
            System.exit(0);
        }
    }

    private void xuLyDangNhap() {
        String manv = txtUsername.getText().trim();
        String matKhau = new String(txtPassword.getPassword()).trim();

        if (manv.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập tên đăng nhập!");
            txtUsername.requestFocus();
            return;
        }

        if (matKhau.isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập mật khẩu!");
            txtPassword.requestFocus();
            return;
        }

        NhanVien nhanVien = dao.selectById(manv);

        if (nhanVien == null) {
            MsgBox.alert(this, "Sai Tên đăng nhập!");
        } else if (!matKhau.equals(nhanVien.getMatKhau())) {
            MsgBox.alert(this, "Sai mật khẩu!");
        } else {
            Auth.user = nhanVien;
            MsgBox.alert(this, "Đăng Nhập Thành Công!");
            this.dispose();
        }
    }

    private void styleButton(JButton btn) {
        Color baseColor = new Color(255, 71, 87);
        Color hoverColor = new Color(255, 99, 120);

        btn.setBackground(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorder(new RoundedBorder(Color.WHITE, 2, 20)); // Viền bo tròn cho nút
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            DangNhap dialog = new DangNhap(new javax.swing.JFrame(), true);
            dialog.setVisible(true);
        });
    }
}