/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import dao.TrangChuDao;
import entity.ThanhVien;
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

    private JLabel lblDoanhThu, lblDonHang, lblThanhVien;
    private JTable tblThanhVien;
    private DefaultTableModel model;
    private TrangChuDao dao = new TrangChuDao();

    public DashboardPanel() {
        setLayout(null);
        setBounds(0, 0, 800, 650);

        JLabel lblWelcome = new JLabel("Welcome back, " + Auth.user.getHoTen() + " 👋");
        lblWelcome.setFont(new Font("Baloo", Font.BOLD, 20));
        lblWelcome.setBounds(20, 20, 400, 30);
        add(lblWelcome);

        JLabel lblAvatar = new JLabel();
        lblAvatar.setBounds(700, 8, 45, 45);
        lblAvatar.setIcon(createRoundedIcon("/GYMBADAO/src/icon/hacker.png", 43, 43));
        add(lblAvatar);

        JPanel panelCards = new JPanel(new GridLayout(1, 3, 10, 20));
        panelCards.setBounds(20, 60, 740, 80);
        add(panelCards);

        lblDoanhThu = createCard("Doanh Thu", "0K", new Color(255, 220, 224));
        lblDonHang = createCard("Đơn Hàng", "0", new Color(224, 220, 224));
        lblThanhVien = createCard("Thành Viên", "0", new Color(235, 220, 255));

        panelCards.add(lblDoanhThu);
        panelCards.add(lblDonHang);
        panelCards.add(lblThanhVien);

        JLabel lblTitle = new JLabel("Thành viên mới:");
        lblTitle.setFont(new Font("Baloo", Font.ROMAN_BASELINE, 20));
        lblTitle.setBounds(20, 150, 200, 30);
        add(lblTitle);

        model = new DefaultTableModel(new String[]{"ID", "Tên", "Giới Tính", "SĐT", "Ngày ĐK", "Ngày KT", "Gói tập", "Trạng thái"}, 0);
        tblThanhVien = new JTable(model);
        JScrollPane scroll = new JScrollPane(tblThanhVien);
        scroll.setBounds(20, 190, 740, 400);
        add(scroll);

        loadDashboardData();
    }

    private void loadDashboardData() {
        try {
            double doanhThu = dao.getTongDoanhThu();
            int donHang = dao.getSoDonHang();
            int thanhVien = dao.getSoThanhVien();

            lblDoanhThu.setText(String.format("<html>Doanh Thu<br>%.0fK</html>", doanhThu / 1000));
            lblDonHang.setText(String.format("<html>Đơn Hàng<br>%d</html>", donHang));
            lblThanhVien.setText(String.format("<html>Thành Viên<br>%d</html>", thanhVien));

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

    private JLabel createCard(String title, String value, Color bg) {
        JLabel lblValue = new JLabel("<html><center>" + title + "<br>" + value + "</center></html>");
        lblValue.setOpaque(true);
        lblValue.setBackground(bg);
        lblValue.setFont(new Font("Arial", Font.BOLD, 14));
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblValue.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        return lblValue;
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
            return null;
        }
    }
}
