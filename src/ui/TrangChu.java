package ui;

import dao.TrangChuDao;
import entity.ThanhVien;
import utils.Auth;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class TrangChu extends JFrame {

    private JLabel lblWelcome, lblAvatar, lblDoanhThu, lblDonHang, lblThanhVien;
    private JTable tblThanhVien;
    private DefaultTableModel model;
    private TrangChuDao dao = new TrangChuDao();

    public TrangChu() {
        setTitle("Gym Management Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        new ManHinhChao(this, true).setVisible(true);
        new DangNhap(this, true).setVisible(true);

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
            btn.setBackground(i == 0 ? new Color(255, 200, 0) : new Color(44, 44, 80));
            btn.setForeground(i == 0 ? Color.BLACK : Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setIcon(new ImageIcon(icons[i]));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 10)
            ));
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

        // Header
        lblWelcome = new JLabel("Welcome back, " + Auth.user.getHoTen() + " 👋");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setBounds(220, 20, 400, 30);
        add(lblWelcome);

        // Avatar
        lblAvatar = new JLabel();
        lblAvatar.setBounds(900, 10, 50, 50);
        lblAvatar.setIcon(new ImageIcon("/GYMBADAO/src/icon/user.png"));
        add(lblAvatar);

        // Cards Panel
        JPanel panelCards = new JPanel(new GridLayout(1, 3, 10, 0));
        panelCards.setBounds(220, 60, 700, 80);
        add(panelCards);

        lblDoanhThu = createCard("Tổng Quan", "0K", new Color(255, 224, 224));
        lblDonHang = createCard("Khách Hàng", "0", new Color(224, 255, 224));
        lblThanhVien = createCard("Đơn Hàng", "0", new Color(235, 224, 255));

        panelCards.add(lblDoanhThu);
        panelCards.add(lblDonHang);
        panelCards.add(lblThanhVien);

        // Table
        JLabel lblTitle = new JLabel("Thành viên mới");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(220, 150, 200, 30);
        add(lblTitle);

        model = new DefaultTableModel(new String[]{"ID", "Tên", "Giới Tính", "SĐT", "Ngày ĐK", "Ngày KT", "Gói tập", "Trạng thái"}, 0);
        tblThanhVien = new JTable(model);
        JScrollPane scroll = new JScrollPane(tblThanhVien);
        scroll.setBounds(220, 190, 750, 400);
        add(scroll);

        loadDashboardData();
    }

    private ImageIcon createRoundedIcon(String path, int width, int height) {
        try {
            Image img = new ImageIcon(getClass().getResource(path)).getImage();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bufferedImage.createGraphics();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new RoundRectangle2D.Float(0, 0, width, height, 50, 50));
            g2.drawImage(img, 0, 0, width, height, null);
            g2.dispose();

            return new ImageIcon(bufferedImage);
        } catch (Exception e) {
            System.out.println("Lỗi tải ảnh: " + e.getMessage());
            return null;
        }
    }

    private JLabel createCard(String title, String value, Color bg) {
        JLabel lblValue = new JLabel("<html><center>" + title + "<br>" + value + "</center></html>");
        lblValue.setOpaque(true);
        lblValue.setBackground(bg);
        lblValue.setFont(new Font("Arial", Font.BOLD, 14));
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblValue.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        return lblValue;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrangChu().setVisible(true));
    }
}
