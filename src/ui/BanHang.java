/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ui;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
/**
 *
 * @author Admin
 */
public class BanHang extends JFrame {

    public BanHang() {
        // Cài đặt cửa sổ chính
        setTitle("Giao diện bán hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo panel chứa lớp lót màu xanh dương đậm
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 139)); // Màu xanh dương đậm
                g2d.fillRect(0, 0, getWidth(), 70); // Chiều cao 70px (thêm padding phía trên)
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70)); // Chiều cao 70px
        headerPanel.setLayout(new BorderLayout());

        // Tạo panel bo tròn cho thanh tìm kiếm
        JPanel roundedPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE); // Màu nền trắng
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bo tròn góc
            }
        };
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setOpaque(false); // Trong suốt để hiển thị nền bo tròn
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding bên trong

        // Tạo thanh tìm kiếm
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setBorder(BorderFactory.createEmptyBorder()); // Không viền
        searchField.setOpaque(false); // Trong suốt để hiển thị nền bo tròn của roundedPanel

        // Đặt thanh tìm kiếm vào panel bo tròn
        roundedPanel.add(searchField, BorderLayout.CENTER);

        // Đặt panel bo tròn vào góc trái với khoảng cách margin
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10)); // Căn trái, margin 20px, padding phía trên 10px
        searchPanel.setOpaque(false);
        searchPanel.add(roundedPanel);
        headerPanel.add(searchPanel, BorderLayout.WEST); // Đặt ở phía Tây (bên trái)

        // Thêm headerPanel vào cửa sổ chính
        add(headerPanel, BorderLayout.NORTH);

        // Hiển thị cửa sổ
        setVisible(true);
    }

    public static void main(String[] args) {
        // Chạy ứng dụng
        SwingUtilities.invokeLater(() -> new BanHang().setVisible(true));
    }
}
