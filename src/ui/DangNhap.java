/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author trong
 */
public class DangNhap extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    private JLabel eyeIcon;
    private boolean isPasswordVisible = false;

    public DangNhap() {
        setTitle("Gym Login");
        setSize(1113, 624);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background
        JLabel background = new JLabel(new ImageIcon("/GYMBADAO/src/icon/backgroundDangNhap.jpg")); // đổi đường dẫn ảnh
        background.setBounds(0, 0, 1113, 624);
        add(background);

        // Panel form căn giữa
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(null);
        int formWidth = 450;
        int formHeight = 300;
        formPanel.setBounds((1113 - formWidth) / 2, (624 - formHeight) / 2 + 70, formWidth, formHeight);
        background.add(formPanel);

        // Username
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBounds(0, 0, formWidth, 50);
        userPanel.setBackground(new Color(35, 34, 59));
        userPanel.setBorder(BorderFactory.createLineBorder(new Color(102, 0, 102), 2));
        userPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        formPanel.add(userPanel);

        JLabel userIcon = new JLabel(new ImageIcon("D:/Gym/GYMBADAO/src/icon/user.png"));
        userPanel.add(userIcon, BorderLayout.WEST);

        txtUsername = new JTextField();
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setBackground(new Color(35, 34, 59));
        txtUsername.setBorder(null);
        txtUsername.setCaretColor(Color.WHITE);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsername.setText("");
        txtUsername.setOpaque(false);
        userPanel.add(txtUsername, BorderLayout.CENTER);

        // Password
        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.setBounds(0, 70, formWidth, 50);
        passPanel.setBackground(new Color(35, 34, 59));
        passPanel.setBorder(BorderFactory.createLineBorder(new Color(102, 0, 102), 2));
        passPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        formPanel.add(passPanel);

        JLabel passIcon = new JLabel(new ImageIcon("D:/Gym/GYMBADAO/src/icon/lock.png"));
        passPanel.add(passIcon, BorderLayout.WEST);

        txtPassword = new JPasswordField();
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setBackground(new Color(35, 34, 59));
        txtPassword.setBorder(null);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setText("");
        txtPassword.setOpaque(false);
        passPanel.add(txtPassword, BorderLayout.CENTER);

        eyeIcon = new JLabel(new ImageIcon("D:/Gym/GYMBADAO/src/icon/eye.png"));
        eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        passPanel.add(eyeIcon, BorderLayout.EAST);

        // Toggle password visibility
        eyeIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                isPasswordVisible = !isPasswordVisible;
                txtPassword.setEchoChar(isPasswordVisible ? (char) 0 : '•');
            }
        });

        // Buttons
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(0, 150, 200, 50);
        styleButton(btnLogin);
        formPanel.add(btnLogin);

        btnExit = new JButton("Thoát");
        btnExit.setBounds(formWidth - 200, 150, 200, 50);
        styleButton(btnExit);
        formPanel.add(btnExit);

        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
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

        // Hover effect
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
        new DangNhap();
    }
}
