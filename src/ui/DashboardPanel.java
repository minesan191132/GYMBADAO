/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import dao.TrangChuDao;
import entity.ThanhVien;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import utils.Auth;

/**
 *
 * @author Admin
 */
public class DashboardPanel extends JPanel {
    private JLabel lblWelcome, lblAvatar, lblDoanhThu, lblDonHang, lblThanhVien;
    private JTable tblThanhVien;
    private DefaultTableModel model;
    private TrangChuDao dao = new TrangChuDao();

    public DashboardPanel() {
        setLayout(null);
        setBounds(0, 0, 800, 650);

        lblWelcome = new JLabel("Welcome Back " + Auth.user.getHoTen() + "👋");
        lblWelcome.setFont(new Font("Baloo", Font.BOLD, 20));
        lblWelcome.setBounds(30, 35, 400, 30);
        add(lblWelcome);

        // Tạo JLabel hiển thị tên người dùng cạnh avatar
// Lấy tên (bỏ họ) từ họ tên đầy đủ
        String fullName = Auth.user.getHoTen();
        String[] nameParts = fullName.split(" ");
        String firstName = nameParts[nameParts.length - 1]; // Lấy từ cuối cùng

// Tạo JLabel hiển thị tên người dùng (chỉ tên, không họ)
        JLabel lblUserName = new JLabel(firstName);
        lblUserName.setFont(new Font("Arial", Font.BOLD, 16));
        lblUserName.setForeground(Color.BLACK);
        lblUserName.setBounds(700, 30, 100, 30); // Giảm chiều rộng để vừa vặn
        add(lblUserName);

        lblAvatar = new JLabel(); // Khởi tạo JLabel trước
        lblAvatar.setBounds(650, 20, 50, 50); // Đặt vị trí và kích thước
        lblAvatar.setIcon(createRoundedIcon("/GYMBADAO/src/icon/hacker.png", 40, 40));
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        lblAvatar.setVerticalAlignment(SwingConstants.CENTER);
        add(lblAvatar);

// Tạo panel chứa các thẻ (màu trắng, có viền bo tròn)
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(null);
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setBounds(30, 70, 720, 110); // Bao quanh thẻ
        wrapperPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2)); // Viền xám nhạt
        add(wrapperPanel);

// Panel chứa các card (bên trong wrapperPanel)
        JPanel panelCards = new JPanel(new GridLayout(1, 3, 10, 20));
        panelCards.setBounds(10, 10, 700, 90);
        panelCards.setOpaque(false); // Để nhìn thấy màu trắng của wrapperPanel
        wrapperPanel.add(panelCards);
        lblDoanhThu = new JLabel();
        lblDonHang = new JLabel();
        lblThanhVien = new JLabel();

        lblDoanhThu = new JLabel();
        lblDonHang = new JLabel();
        lblThanhVien = new JLabel();

        JPanel panelDoanhThu = createCard("Doanh thu", "0K", new Color(255, 220, 224), lblDoanhThu);
        JPanel panelDonHang = createCard("Đơn hàng", "0", new Color(224, 255, 224), lblDonHang);
        JPanel panelThanhVien = createCard("Thành viên", "0", new Color(235, 220, 255), lblThanhVien);

        panelCards.add(panelDoanhThu);
        panelCards.add(panelDonHang);
        panelCards.add(panelThanhVien);

        // Table
        JLabel lblTitle = new JLabel("Thành viên mới: ");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBounds(30, 185, 200, 30);
        add(lblTitle);

        model = new DefaultTableModel(new String[]{"ID", "Tên", "Giới Tính", "SĐT", "Ngày ĐK", "Ngày KT", "Gói tập", "Trạng thái"}, 0);
        tblThanhVien = new JTable(model);
        JScrollPane scroll = new JScrollPane(tblThanhVien);
        scroll.setBounds(30, 220, 720, 360);
        add(scroll);

        loadDashboardData();
    }

    private ImageIcon createRoundedIcon(String path, int width, int height) {
        try {
            // Load ảnh từ đường dẫn
            Image img = new ImageIcon(path).getImage();

            // Tạo ảnh mới với nền trong suốt
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bufferedImage.createGraphics();

            // Kích hoạt khử răng cưa
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Tạo hình tròn
            g2.setClip(new RoundRectangle2D.Float(0, 0, width, height, width, height));
            g2.drawImage(img, 0, 0, width, height, null);
            g2.dispose();

            return new ImageIcon(bufferedImage);
        } catch (Exception e) {
            System.out.println("Lỗi tải ảnh: " + e.getMessage());
            return null;
        }
    }

    private JPanel createCard(String title, String value, Color bg, JLabel lblValue) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Tiêu đề (Căn trái)
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));

        // Giá trị (Căn giữa)
        lblValue.setText(value);  // Gán giá trị ban đầu
        lblValue.setFont(new Font("Arial", Font.BOLD, 16));
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);

        // Thêm vào panel
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    private void loadDashboardData() {
        try {
            double doanhThu = dao.getTongDoanhThu();
            int donHang = dao.getSoDonHang();
            int thanhVien = dao.getSoThanhVien();

            lblDoanhThu.setText(String.format("%.0fK", doanhThu / 1000));
            lblDonHang.setText(String.valueOf(donHang));
            lblThanhVien.setText(String.valueOf(thanhVien));

            List<ThanhVien> list = dao.getThanhVienMoi();
            model.setRowCount(0);
            for (ThanhVien tv : list) {
                model.addRow(new Object[]{
                    tv.getMaTV(), tv.getHoTen(), tv.getGioiTinh(), tv.getSoDT(),
                    tv.getNgayDK(), tv.getNgayKT(), tv.getGoiTap(), "✔"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }
}