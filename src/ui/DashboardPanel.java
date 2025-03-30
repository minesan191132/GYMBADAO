package ui;

import dao.TrangChuDao;
import entity.ThanhVien;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utils.Auth;



public class DashboardPanel extends JPanel {
    private JLabel lblWelcome, lblAvatar, lblDoanhThu, lblDonHang, lblThanhVien;
    private JTable tblThanhVien;
    private DefaultTableModel model;
    private TrangChuDao dao = new TrangChuDao();

    public DashboardPanel() {
        setLayout(null);
        setBackground(new Color(239, 236, 236)); // Màu nền nhẹ cho toàn bộ panel
        setBounds(0, 0, 800, 650);

        // Header section
        JPanel headerPanel = new RoundedPanel(20, new Color(255, 255, 255));
        headerPanel.setLayout(null);
        headerPanel.setBounds(20, 20, 760, 80);
        add(headerPanel);

        // Welcome label
        lblWelcome = new JLabel("Welcome Back " + Auth.user.getHoTen());
        ImageIcon wavingHandIcon = new ImageIcon("/GYMBADAO/src/icon/hi.png");
        Image image = wavingHandIcon.getImage();
        Image scaledImage = image.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        wavingHandIcon = new ImageIcon(scaledImage);
        lblWelcome.setIcon(wavingHandIcon);
        lblWelcome.setFont(new Font("Baloo", Font.BOLD, 20));
        lblWelcome.setBounds(30, 25, 400, 30);
        lblWelcome.setHorizontalTextPosition(SwingConstants.LEADING);
        lblWelcome.setIconTextGap(10);
        headerPanel.add(lblWelcome);

        // User info section
        String fullName = Auth.user.getHoTen();
        String[] nameParts = fullName.split(" ");
        String firstName = nameParts[nameParts.length - 1];

        lblAvatar = new JLabel();
        lblAvatar.setBounds(670, 15, 50, 50);
        lblAvatar.setIcon(createRoundedIcon("/GYMBADAO/src/icon/userguys.png", 50, 50));
        headerPanel.add(lblAvatar);

        JLabel lblUserName = new JLabel(firstName);
        lblUserName.setFont(new Font("Arial", Font.BOLD, 16));
        lblUserName.setForeground(new Color(70, 70, 70));
        lblUserName.setBounds(620, 25, 70, 30);
        headerPanel.add(lblUserName);

        // Cards section
        JPanel cardsWrapper = new RoundedPanel(20, new Color(255, 255, 255));
        cardsWrapper.setLayout(null);
        cardsWrapper.setBounds(20, 120, 760, 120);
        add(cardsWrapper);

        JPanel panelCards = new JPanel(new GridLayout(1, 3, 15, 0));
        panelCards.setBounds(20, 15, 720, 90);
        panelCards.setOpaque(false);
        cardsWrapper.add(panelCards);

        lblDoanhThu = new JLabel();
        lblDonHang = new JLabel();
        lblThanhVien = new JLabel();

        JPanel panelDoanhThu = createCard("Doanh thu", "0VND", new Color(255, 220, 224), lblDoanhThu);
        JPanel panelDonHang = createCard("Đơn hàng", "0", new Color(224, 255, 224), lblDonHang);
        JPanel panelThanhVien = createCard("Thành viên", "0", new Color(235, 220, 255), lblThanhVien);

        panelCards.add(panelDoanhThu);
        panelCards.add(panelDonHang);
        panelCards.add(panelThanhVien);

        // Table section
        JPanel tableWrapper = new RoundedPanel(20, new Color(255, 255, 255));
        tableWrapper.setLayout(null);
        tableWrapper.setBounds(20, 260, 760, 360);
        add(tableWrapper);

        JLabel lblTitle = new JLabel("Thành viên mới ");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(95, 139, 76));
        lblTitle.setBounds(20, 15, 200, 30);
        tableWrapper.add(lblTitle);

          model = new DefaultTableModel(new String[]{"ID", "Tên", "Giới Tính", "SĐT", "Ngày ĐK", "Ngày KT", "Gói tập"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return Object.class; // Cho phép căn giữa tất cả các cột
            }
        };
        
        tblThanhVien = new JTable(model);
        
        // Căn giữa tất cả các ô trong bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tblThanhVien.getColumnCount(); i++) {
            tblThanhVien.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        tblThanhVien.setRowHeight(30);
        tblThanhVien.setShowGrid(false);
        tblThanhVien.setIntercellSpacing(new java.awt.Dimension(0, 0));
        
        JScrollPane scroll = new JScrollPane(tblThanhVien);
        scroll.setBounds(20, 50, 720, 290);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableWrapper.add(scroll);

        loadDashboardData();
    }

    private ImageIcon createRoundedIcon(String path, int width, int height) {
        try {
            Image img = new ImageIcon(path).getImage();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
        RoundedPanel panel = new RoundedPanel(15, bg);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(new Color(70, 70, 70));
        lblTitle.setHorizontalAlignment(SwingConstants.LEFT);

        lblValue.setText(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 18));
        lblValue.setForeground(new Color(50, 50, 50));
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);

        return panel;
    }

    private void loadDashboardData() {
        try {
            double doanhThu = dao.getTongDoanhThu();
            int donHang = dao.getSoDonHang();
            int thanhVien = dao.getSoThanhVien();

            lblDoanhThu.setText(String.format("%.0fVND", doanhThu / 1000));
            lblDonHang.setText(String.valueOf(donHang));
            lblThanhVien.setText(String.valueOf(thanhVien));

            List<ThanhVien> list = dao.getThanhVienMoi();
            model.setRowCount(0);
            for (ThanhVien tv : list) {
                model.addRow(new Object[]{
                    tv.getMaTV(), tv.getHoTen(), tv.getGioiTinh(), tv.getSoDT(),
                    tv.getNgayDK(), tv.getNgayKT(), tv.getGoiTap()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    // Custom RoundedPanel class
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
}