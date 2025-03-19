package ui;

import dao.NhanVienDAO;
import entity.NhanVien;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;
import utils.*;

public class DangNhap extends javax.swing.JDialog {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    private JLabel eyeIcon;
    private boolean isPasswordVisible = false;
    NhanVienDAO dao = new NhanVienDAO();

    public DangNhap(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Gym Login");
        setSize(1116,627);
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

        // Username
        JPanel userPanel = new JPanel(new BorderLayout(10, 0));
        userPanel.setBounds(0, 0, formWidth, 50);
        userPanel.setBackground(new Color(70, 70, 110));
        userPanel.setBorder(BorderFactory.createLineBorder(new Color(102, 0, 102), 2));
        formPanel.add(userPanel);

        JLabel userIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/user.png"));
        userPanel.add(userIcon, BorderLayout.WEST);

        txtUsername = new JTextField();
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setBackground(new Color(70, 70, 110));
        txtUsername.setBorder(null);
        txtUsername.setCaretColor(Color.WHITE);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsername.setOpaque(false);
        userPanel.add(txtUsername, BorderLayout.CENTER);

        // Password
        JPanel passPanel = new JPanel(new BorderLayout(10, 0));
        passPanel.setBounds(0, 70, formWidth, 50);
        passPanel.setBackground(new Color(70, 70, 110));
        passPanel.setBorder(BorderFactory.createLineBorder(new Color(102, 0, 102), 2));
        formPanel.add(passPanel);

        JLabel passIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/password.png"));
        passPanel.add(passIcon, BorderLayout.WEST);

        txtPassword = new JPasswordField();
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setBackground(new Color(70, 70, 110));
        txtPassword.setBorder(null);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setOpaque(false);
        passPanel.add(txtPassword, BorderLayout.CENTER);

        eyeIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/hidden.png"));
        eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        passPanel.add(eyeIcon, BorderLayout.EAST);

        eyeIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                isPasswordVisible = !isPasswordVisible;
                txtPassword.setEchoChar(isPasswordVisible ? (char) 0 : '•');
            }
        });

        // Buttons
        btnLogin = new JButton("Đăng Nhập");
        btnLogin.setBounds(70, 150, 150, 40);
        styleButton(btnLogin);
        formPanel.add(btnLogin);

        btnExit = new JButton("Thoát");
        btnExit.setBounds(250, 150, 150, 40);
        styleButton(btnExit);
        formPanel.add(btnExit);

        btnExit.addActionListener(e -> ketThuc());
        btnLogin.addActionListener(e -> xuLyDangNhap());
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
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
                btn.setBorder(BorderFactory.createLineBorder(new Color(255, 160, 160), 2));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
                btn.setBorder(BorderFactory.createEmptyBorder());
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