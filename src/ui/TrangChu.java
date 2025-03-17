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
public class TrangChu extends JFrame{

    /**
     * @param args the command line arguments
     */
    
    
    
        public TrangChu() {
        // Cấu hình JFrame
        setTitle("Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Menu (bên trái)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(7, 1, 10, 10));
        menuPanel.setPreferredSize(new Dimension(200, getHeight()));
        menuPanel.setBackground(new Color(30, 30, 50)); // Màu nền menu

        String[] menuItems = {"🏠 Tổng quan", "👥 Khách hàng", "📝 Đăng ký", "📦 Đơn hàng", "📊 Báo Cáo", "⚙️ Cài Đặt"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(50, 50, 70));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            menuPanel.add(button);
        }

        // Nút Logout
        JButton logoutButton = new JButton("🔓 Log out");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.ORANGE);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        menuPanel.add(logoutButton);

        // Panel chính (bên phải)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);

        // Tiêu đề chào mừng
        JLabel welcomeLabel = new JLabel("Welcome back Tuấn Anh 👋", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Panel Thống kê
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setPreferredSize(new Dimension(800, 100));
        
        statsPanel.add(createStatPanel("💰 Doanh thu", "9.000K", Color.PINK));
        statsPanel.add(createStatPanel("📦 Đơn hàng", "65", Color.GREEN));
        statsPanel.add(createStatPanel("🧑‍🤝‍🧑 Thành viên", "235", Color.MAGENTA));

        mainPanel.add(statsPanel, BorderLayout.CENTER);

        // Panel bảng danh sách thành viên
        String[] columnNames = {"NO", "DATE", "ID", "NAME", "AGE", "GENDER"};
        Object[][] data = {
            {"01", "12/3/2025", "TS01135", "Nguyễn Văn A", "19", "Nam"},
            {"02", "13/3/2025", "TS01255", "Nguyễn Văn B", "22", "Nam"},
            {"03", "15/3/2025", "TS01236", "Nguyễn Văn C", "25", "Nam"}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // Thêm các panel vào JFrame
        add(menuPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Phương thức tạo ô thống kê
    private JPanel createStatPanel(String title, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setPreferredSize(new Dimension(100, 100));
        panel.setBackground(color);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        new TrangChu();
    }
}
