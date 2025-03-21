package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class DonHang extends JFrame {

    public DonHang() {
        setTitle("Theo dõi đơn hàng Dashboard");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(new Color(33, 33, 61));
        sidebar.setBounds(0, 0, 200, 650);
        add(sidebar);

        JLabel lblLogo = new JLabel(new ImageIcon("/GYMBADAO/src/icon/logo.png"));
        lblLogo.setForeground(Color.ORANGE);
        lblLogo.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));
        lblLogo.setBounds(0, 20, 200, 30);
        sidebar.add(lblLogo);

        String[] menu = {"Tổng Quan", "Khách Hàng", "Đơn Hàng", "Bán Hàng" ,"Báo Cáo"};
        String[] icons = {"/GYMBADAO/src/icon/Home.png", "/GYMBADAO/src/icon/group-users.png", "/GYMBADAO/src/icon/shopping-bag.png", "/GYMBADAO/src/icon/cart.png", "/GYMBADAO/src/icon/report.png"};

        int y = 80;
        for (int i = 0; i < menu.length; i++) {
            JButton btn = new JButton(menu[i]);
            btn.setBounds(10, y, 180, 50);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setIcon(new ImageIcon(icons[i]));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);

            if (i == 2) {
                btn.setBackground(new Color(255, 200, 0));
                btn.setForeground(Color.BLACK);
            } else {
                btn.setBackground(new Color(44, 44, 80));
                btn.setForeground(Color.WHITE);
            }

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

        // Main content panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(200, 0, 800, 650);
        add(formPanel);

        // Tiêu đề và các trường nhập
        JLabel lblTitle = new JLabel("Theo dõi đơn hàng");
        lblTitle.setBounds(50, 20, 200, 30);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        formPanel.add(lblTitle);

        JLabel lblHelpIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/question-sign.png"));
        lblHelpIcon.setBounds(260, 20, 30, 30);
        formPanel.add(lblHelpIcon);

        JLabel lblHelpText = new JLabel("Trợ giúp");
        lblHelpText.setBounds(300, 20, 80, 30);
        lblHelpText.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(lblHelpText);

        JLabel lblFeedbackIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/heart.png"));
        lblFeedbackIcon.setBounds(390, 20, 30, 30);
        formPanel.add(lblFeedbackIcon);

        JLabel lblFeedbackText = new JLabel("Góp ý");
        lblFeedbackText.setBounds(430, 20, 80, 30);
        lblFeedbackText.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(lblFeedbackText);

        // Tạo panel chứa thanh tìm kiếm, nút bộ lọc và nút xem theo ngày tháng
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(50, 70, 700, 40);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setLayout(new GridLayout(1, 3, 10, 10));

        // Thanh tìm kiếm bo tròn và có thể nhập liệu
        RoundTextField txtTimKiem = new RoundTextField("Tìm kiếm", 20);
        txtTimKiem.setBounds(50, 70, 500, 40); // Kéo dài thanh tìm kiếm
        searchPanel.add(txtTimKiem);

        txtTimKiem.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtTimKiem.getText().equals("Tìm kiếm")) {
                    txtTimKiem.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtTimKiem.getText().isEmpty()) {
                    txtTimKiem.setText("Tìm kiếm");
                }
            }
        });
        
        // Nút bộ lọc
        JButton btnLoc = new JButton("Bộ lọc") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient màu xanh
                GradientPaint gradient = new GradientPaint(0, 0, new Color(44, 44, 80), getWidth(), getHeight(), new Color(33, 33, 61));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLoc.setFont(new Font("Arial", Font.BOLD, 14));
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setFocusPainted(false);
        btnLoc.setContentAreaFilled(false);
        btnLoc.setBorderPainted(false);
        btnLoc.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLoc.setPreferredSize(new java.awt.Dimension(100, 40)); // Thu ngắn lại
        btnLoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Chức năng Bộ lọc đang được phát triển!");
            }
        });
        searchPanel.add(btnLoc);

        // Nút xem theo ngày
        JButton btnXemNgay = new JButton("Xem theo ngày") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient màu xanh
                GradientPaint gradient = new GradientPaint(0, 0, new Color(44, 44, 80), getWidth(), getHeight(), new Color(33, 33, 61));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnXemNgay.setFont(new Font("Arial", Font.BOLD, 14));
        btnXemNgay.setForeground(Color.WHITE);
        btnXemNgay.setFocusPainted(false);
        btnXemNgay.setContentAreaFilled(false);
        btnXemNgay.setBorderPainted(false);
        btnXemNgay.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnXemNgay.setPreferredSize(new java.awt.Dimension(120, 40)); // Thu ngắn lại
        btnXemNgay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dateDialog = new JDialog();
                dateDialog.setTitle("Chọn khoảng thời gian");
                dateDialog.setSize(300, 150);
                dateDialog.setLayout(new GridLayout(3, 2, 10, 10));
                dateDialog.setLocationRelativeTo(null);

                // Ngày bắt đầu
                JLabel lblNgayBatDau = new JLabel("Ngày bắt đầu:");
                JSpinner spnNgayBatDau = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor editorNgayBatDau = new JSpinner.DateEditor(spnNgayBatDau, "dd/MM/yyyy");
                spnNgayBatDau.setEditor(editorNgayBatDau);
                dateDialog.add(lblNgayBatDau);
                dateDialog.add(spnNgayBatDau);

                // Ngày kết thúc
                JLabel lblNgayKetThuc = new JLabel("Ngày kết thúc:");
                JSpinner spnNgayKetThuc = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor editorNgayKetThuc = new JSpinner.DateEditor(spnNgayKetThuc, "dd/MM/yyyy");
                spnNgayKetThuc.setEditor(editorNgayKetThuc);
                dateDialog.add(lblNgayKetThuc);
                dateDialog.add(spnNgayKetThuc);

                // Nút Xác nhận
                JButton btnXacNhan = new JButton("Xác nhận");
                btnXacNhan.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Date ngayBatDau = (Date) spnNgayBatDau.getValue();
                        Date ngayKetThuc = (Date) spnNgayKetThuc.getValue();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String ngayBD = sdf.format(ngayBatDau);
                        String ngayKT = sdf.format(ngayKetThuc);

                        JOptionPane.showMessageDialog(null, "Đã chọn từ ngày " + ngayBD + " đến ngày " + ngayKT);
                        dateDialog.dispose();
                    }
                });
                dateDialog.add(btnXacNhan);

                // Nút Hủy
                JButton btnHuy = new JButton("Hủy");
                btnHuy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dateDialog.dispose();
                    }
                });
                dateDialog.add(btnHuy);

                dateDialog.setVisible(true);
            }
        });
        searchPanel.add(btnXemNgay);

        // Thêm searchPanel vào formPanel
        formPanel.add(searchPanel);

        // Thêm bảng đơn hàng
        String[] columnNames = {"Mã đơn", "Tên khách hàng", "Ngày tạo", "Trạng thái", "Khách trả"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0); // Bảng trống ban đầu
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 130, 700, 450);
        formPanel.add(scrollPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DonHang().setVisible(true));
    }

    // Lớp RoundTextField
    static class RoundTextField extends JTextField {
        public RoundTextField(String placeholder, int columns) {
            super(placeholder, columns); // Sử dụng placeholder và số cột
            setOpaque(false); // Làm nền trong suốt
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Thêm padding
            setFont(new Font("Arial", Font.PLAIN, 14));
            setBackground(new Color(240, 240, 240)); // Màu nền
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Vẽ nền bo góc
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bo góc 20px

            // Vẽ text
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Không vẽ viền mặc định
        }
    }
}

//bây giờ bạn chỉnh cho thanh tìm kiếm dài ra thêm chút nữa nút bộ lọc và xem theo ngày ngắn lại và tôi muốn khi click vào ô nhập của thanh tìm kiếm thì chữ tìm kiếm sẽ tư động ẩn đi