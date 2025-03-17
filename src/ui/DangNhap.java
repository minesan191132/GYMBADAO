/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author trong
 */
public class DangNhap extends JFrame{

    /**
     * @param args the command line arguments
     */
    
    public DangNhap() {
        // Cấu hình cửa sổ chính
        setTitle("Gym Login");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Màu nền
        getContentPane().setBackground(new Color(15, 15, 30));

        // Tiêu đề LOGIN
        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 30, 200, 50);
        add(titleLabel);

        // Label Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(50, 100, 100, 30);
        add(userLabel);

        // Ô nhập Username
        JTextField userField = new JTextField();
        userField.setBounds(150, 100, 250, 30);
        add(userField);

        // Label Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(50, 150, 100, 30);
        add(passLabel);

        // Ô nhập Password
        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 150, 250, 30);
        add(passField);

        // Nút Đăng nhập
        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setBounds(150, 200, 120, 30);
        add(loginButton);
        
        // Hình ảnh tạ
        ImageIcon dumbbellIcon = new ImageIcon("C:\\GYMBADAO\\src\\icon\\bell.png"); // Đường dẫn hình ảnh
        JLabel dumbbellLabel = new JLabel(dumbbellIcon);
        dumbbellLabel.setBounds(240, 30, 350, 220); // Điều chỉnh vị trí cho phù hợp
        add(dumbbellLabel);

        setVisible(true);
    }
    public static void main(String[] args) {
        // TODO code application logic here
        new DangNhap();
    }
    
}
