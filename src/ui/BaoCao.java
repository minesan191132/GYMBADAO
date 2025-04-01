package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

public class BaoCao extends JFrame {

    public BaoCao() {
        // 1. Cấu hình JFrame chính
        setupMainFrame();
        
        // 2. Tạo sidebar
        add(createSidebar());
        
        // 3. Tạo header panel với nút xem theo ngày
        add(createHeaderPanel());
        
        // 4. Tạo main panel chứa biểu đồ
        add(createMainPanel());
    }

    private void setupMainFrame() {
        setTitle("Báo Cáo Doanh Thu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(new Color(33, 33, 61));
        sidebar.setBounds(0, 0, 200, 650);

        // Logo
        JLabel lblLogo = new JLabel("GYM BADAO", SwingConstants.CENTER);
        lblLogo.setBounds(0, 10, 200, 60);
        lblLogo.setOpaque(true);
        lblLogo.setBackground(new Color(25, 25, 50));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 18));
        sidebar.add(lblLogo);

        // Các nút menu
        String[] menuItems = {"Tổng Quan", "Khách Hàng", "Đơn Hàng", "Bán Hàng", "Báo Cáo"};
        String[] icons = {
            "D:/GYMBADAO/src/icon/home.png",
            "D:/GYMBADAO/src/icon/contact-list.png",
            "D:/GYMBADAO/src/icon/guest-list.png",
            "D:/GYMBADAO/src/icon/add-to-basket.png",
            "D:/GYMBADAO/src/icon/sales.png"
        };

        int y = 80;
        for (int i = 0; i < menuItems.length; i++) {
            RoundedButton btn = new RoundedButton(menuItems[i]);
            
            // Thiết lập icon
            ImageIcon icon = new ImageIcon(icons[i]);
            Image img = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
            
            btn.setBounds(10, y, 180, 45);
            btn.setBackground(i == 4 ? new Color(255, 200, 0) : new Color(44, 44, 80));
            btn.setForeground(i == 4 ? Color.BLACK : Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);
            btn.setBorder(new EmptyBorder(0, 15, 0, 0));
            
            sidebar.add(btn);
            y += 55;
        }

        // Nút đăng xuất
        RoundedButton btnLogout = new RoundedButton("Đăng xuất");
        btnLogout.setBounds(20, 550, 160, 45);
        btnLogout.setBackground(new Color(255, 153, 51));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogout.setIcon(new ImageIcon("D:/GYMBADAO/src/icon/logout.png"));
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogout.setIconTextGap(15);
        btnLogout.setBorder(new EmptyBorder(0, 15, 0, 0));
        sidebar.add(btnLogout);

        return sidebar;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBounds(200, 0, 800, 80);

        // Nút xem theo ngày
        JButton btnXemNgay = new JButton("Xem theo ngày") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(44, 44, 80), 
                    getWidth(), getHeight(), new Color(33, 33, 61)
                );
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
        btnXemNgay.setOpaque(false);
        btnXemNgay.setBorderPainted(false);
        btnXemNgay.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnXemNgay.setBounds(20, 20, 180, 40);
        btnXemNgay.addActionListener(e -> showDateRangeDialog());
        headerPanel.add(btnXemNgay);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(200, 80, 800, 570);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());

        // Tạo và thêm biểu đồ vào main panel
        mainPanel.add(createChartPanel(), BorderLayout.CENTER);
        
        return mainPanel;
    }

private JPanel createChartPanel() {
    // 1. Tạo panel chứa biểu đồ với layout BorderLayout
    JPanel chartContainer = new JPanel(new BorderLayout());
    chartContainer.setBackground(Color.WHITE);
    chartContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // 2. Tạo dataset với dữ liệu mẫu
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(35, "Tiền mặt", "");
    dataset.addValue(20, "Chuyển khoản", "");

    // 3. Tạo biểu đồ không tiêu đề
    JFreeChart chart = ChartFactory.createBarChart(
        null, null, null, dataset,
        PlotOrientation.VERTICAL,
        true, false, false
    );

    // 4. Cấu hình màu sắc và style
    Color niceBlue = new Color(70, 130, 180); // Màu xanh đậm hơn
    BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
    
    // Cấu hình renderer
    renderer.setSeriesPaint(0, niceBlue);
    renderer.setSeriesPaint(1, niceBlue);
    renderer.setShadowVisible(false);
    renderer.setDrawBarOutline(false);
    renderer.setBarPainter(new StandardBarPainter());
    
    // 5. Cấu hình plot
    CategoryPlot plot = chart.getCategoryPlot();
    plot.setBackgroundPaint(Color.WHITE); // Nền trắng
    plot.setRangeGridlinePaint(Color.WHITE); // Ẩn đường lưới
    plot.setOutlineVisible(false); // Ẩn đường viền
    
    // 6. Tăng chiều cao các cột
    plot.getDomainAxis().setLowerMargin(0.15); // Giảm khoảng trống 2 bên
    plot.getDomainAxis().setUpperMargin(0.15);
    renderer.setItemMargin(0.1); // Giảm khoảng cách giữa các cột
    
    // 7. Tạo ChartPanel với kích thước dài hơn
    ChartPanel chartPanel = new ChartPanel(chart) {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(550, 230); 
        }
    };
    
    // 8. Căn giữa và đẩy lên trên
    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTH; // Căn lên trên
    gbc.insets = new Insets(0, 0, 0, 0); // Không padding
    centerPanel.add(chartPanel, gbc);
    
    chartContainer.add(centerPanel, BorderLayout.NORTH); // Đặt ở phía trên
    
    return chartContainer;
}

    private void showDateRangeDialog() {
        JDialog dialog = new JDialog(this, "Chọn khoảng thời gian", true);
        dialog.setSize(400, 220);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);

        // Panel chính
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Ngày bắt đầu
        JLabel lblStart = new JLabel("Từ ngày:");
        lblStart.setFont(new Font("Arial", Font.PLAIN, 14));
        JSpinner spnStart = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(spnStart, "dd/MM/yyyy");
        spnStart.setEditor(startEditor);
        spnStart.setValue(new Date());

        // Ngày kết thúc
        JLabel lblEnd = new JLabel("Đến ngày:");
        lblEnd.setFont(new Font("Arial", Font.PLAIN, 14));
        JSpinner spnEnd = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(spnEnd, "dd/MM/yyyy");
        spnEnd.setEditor(endEditor);
        spnEnd.setValue(new Date());

        mainPanel.add(lblStart);
        mainPanel.add(spnStart);
        mainPanel.add(lblEnd);
        mainPanel.add(spnEnd);

        // Panel nút
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBorder(new EmptyBorder(0, 0, 10, 10));

        // Nút hủy (màu đỏ)
        RoundedButton btnCancel = new RoundedButton("Hủy");
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(90, 35));
        btnCancel.addActionListener(e -> dialog.dispose());

        // Nút xác nhận (màu xanh lá)
        RoundedButton btnConfirm = new RoundedButton("Xác nhận");
        btnConfirm.setBackground(new Color(46, 204, 113));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setPreferredSize(new Dimension(100, 35));
        btnConfirm.addActionListener(e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String startDate = sdf.format(spnStart.getValue());
            String endDate = sdf.format(spnEnd.getValue());
            
            JOptionPane.showMessageDialog(
                dialog, 
                "Đã chọn từ " + startDate + " đến " + endDate,
                "Khoảng thời gian",
                JOptionPane.INFORMATION_MESSAGE
            );
            dialog.dispose();
            
            // TODO: Cập nhật biểu đồ theo khoảng thời gian đã chọn
        });

        btnPanel.add(btnCancel);
        btnPanel.add(btnConfirm);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Lớp nút bo tròn
    class RoundedButton extends JButton {
        private int arcWidth = 20;
        private int arcHeight = 20;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            g2.setColor(getForeground());
            g2.setFont(getFont());

            FontMetrics fm = g2.getFontMetrics();
            
            if (getIcon() != null) {
                int iconWidth = getIcon().getIconWidth();
                int iconHeight = getIcon().getIconHeight();
                int iconX = 15;
                int iconY = (getHeight() - iconHeight) / 2;
                getIcon().paintIcon(this, g2, iconX, iconY);

                int textX = iconX + iconWidth + getIconTextGap();
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), textX, textY);
            } else {
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), textX, textY);
            }

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BaoCao().setVisible(true);
        });
    }
}