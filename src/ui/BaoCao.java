package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import entity.*;
import dao.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import java.text.DecimalFormat;


public class BaoCao extends JPanel {

    private DefaultCategoryDataset dataset; // Dữ liệu biểu đồ
    private ChartPanel chartPanel; // Panel chứa biểu đồ
    private JTable table; // Bảng thống kê
    private final DonHangDAO donHangDAO = new DonHangDAO();

    public BaoCao() {
        setLayout(new BorderLayout());

        // Khởi tạo các thành phần quan trọng trước
        dataset = new DefaultCategoryDataset();
        table = new JTable();

        // Thêm các panel
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        // Cập nhật dữ liệu ban đầu
        updateTable("Hôm nay");
        updateChart("Hôm nay");
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null); // Giữ nguyên layout null như code gốc
        headerPanel.setBackground(new Color(241, 239, 236));
        headerPanel.setPreferredSize(new Dimension(800, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

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
        btnXemNgay.setBounds(25, 20, 180, 40);
        btnXemNgay.addActionListener(e -> showDateRangeDialog());
        headerPanel.add(btnXemNgay);

        // Thêm các biểu tượng trợ giúp và góp ý
        addHelpAndFeedbackIcons(headerPanel);

        return headerPanel;
    }

    private void addHelpAndFeedbackIcons(JPanel headerPanel) {
        int yPosition = 25;
        int iconWidth = 30;
        int textWidth = 80;
        int spacing = 15;

        // Trợ giúp
        int helpX = 250;
        JLabel lblHelpIcon = new JLabel(new ImageIcon("D:/GYMBADAO/src/icon/question-sign.png"));
        lblHelpIcon.setBounds(helpX, yPosition, iconWidth, 30);
        headerPanel.add(lblHelpIcon);

        JLabel lblHelpText = new JLabel("Trợ giúp");
        lblHelpText.setBounds(helpX + iconWidth + 5, yPosition, textWidth, 30);
        lblHelpText.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(lblHelpText);

        // Góp ý
        int feedbackX = helpX + iconWidth + textWidth + spacing;
        JLabel lblFeedbackIcon = new JLabel(new ImageIcon("D:/GYMBADAO/src/icon/heart.png"));
        lblFeedbackIcon.setBounds(feedbackX, yPosition, iconWidth, 30);
        headerPanel.add(lblFeedbackIcon);

        JLabel lblFeedbackText = new JLabel("Góp ý");
        lblFeedbackText.setBounds(feedbackX + iconWidth + 5, yPosition, textWidth, 30);
        lblFeedbackText.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(lblFeedbackText);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        chartContainer.add(createChartPanel(), BorderLayout.CENTER);

        mainPanel.add(chartContainer, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createChartPanel() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(240, 240, 240));

        // Khởi tạo biểu đồ với đầy đủ tham số
        JFreeChart chart = ChartFactory.createBarChart(
                null, // Tiêu đề biểu đồ (null nếu không cần)
                null, // Nhãn trục x (null nếu không cần)
                null, // Nhãn trục y (null nếu không cần)
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Hướng biểu đồ
                true, // Hiển thị chú thích
                false, // Hiển thị tooltips
                false // Hiển thị URLs
        );

        configureChartAppearance(chart, "Hôm nay");
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(750, 300));

        // Panel chứa biểu đồ
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        chartContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel chứa bảng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.setBackground(new Color(241, 239, 236));
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainContainer.add(chartContainer, BorderLayout.NORTH);
        mainContainer.add(tableContainer, BorderLayout.CENTER);

        return mainContainer;
    }

    private void configureChartAppearance(JFreeChart chart, String option) {
        CategoryPlot plot = chart.getCategoryPlot();

        // Cấu hình renderer cho cột
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(70, 130, 180));
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        renderer.setBarPainter(new StandardBarPainter());

        // Cấu hình nền biểu đồ
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);

        // Tuỳ chỉnh nhãn trục hoành khi là "Tháng này" hoặc "Tháng trước"
        if ("Tháng này".equals(option) || "Tháng trước".equals(option)) {
            CategoryAxis domainAxis = plot.getDomainAxis();

            // Làm nghiêng nhãn 30 độ
            domainAxis.setCategoryLabelPositions(
                    CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0) // 30 độ
            );

            // Đặt margin cho nhãn để tránh cắt chữ
            domainAxis.setLowerMargin(0.02);
            domainAxis.setUpperMargin(0.02);
        }
        // Thay đổi kích thước phông chữ cho nhãn trục hoành
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10)); // Kích thước chữ nhỏ hơn

        // Điều chỉnh kích thước cột cho "Hôm trước" và "Hôm nay"
        if ("Hôm nay".equals(option) || "Hôm trước".equals(option)) {
            renderer.setMaximumBarWidth(0.4); // Giảm chiều rộng tối đa của cột
            renderer.setItemMargin(0.1); // Điều chỉnh khoảng cách giữa các cột
        } else {
            renderer.setMaximumBarWidth(0.2); // Đặt chiều rộng tối đa cho các trường hợp khác
            renderer.setItemMargin(0.05); // Đặt lại khoảng cách cho các trường hợp khác
        }
    }

    private void configureTableAppearance() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        };

        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setFont(new Font("Arial", Font.PLAIN, 13));

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
    }

    private void showDateRangeDialog() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Chọn khoảng thời gian", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] options = {
            "Hôm trước", "Hôm nay", "7 ngày qua", "Tháng trước",
            "Tháng này", "Năm nay"
        };

        for (String option : options) {
            RoundedButton btn = new RoundedButton(option);
            btn.setBackground(new Color(70, 130, 180));
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(180, 50));
            btn.setBorder(BorderFactory.createLineBorder(new Color(50, 100, 150), 2));
            btn.setFont(new Font("Arial", Font.BOLD, 14));

            // Sửa lại phần ActionListener
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedOption = ((JButton) e.getSource()).getText();
                    if (table != null && dataset != null) {
                        updateTable(selectedOption);
                        updateChart(selectedOption);
                    }
                    dialog.dispose();
                }
            });

            mainPanel.add(btn);
        }

        if (options.length % 2 != 0) {
            mainPanel.add(new JLabel());
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBorder(new EmptyBorder(0, 0, 10, 10));

        RoundedButton btnCancel = new RoundedButton("Hủy");
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(100, 40));
        btnCancel.setBorder(BorderFactory.createLineBorder(new Color(200, 50, 50), 2));
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void updateChart(String option) {
    if (dataset == null) {
        dataset = new DefaultCategoryDataset();
    }
    dataset.clear();

    List<DonHang> dataList = getDataForOption(option);
    Map<String, Double> dailyRevenue = new LinkedHashMap<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
    
    // Tìm giá trị lớn nhất và tổng doanh thu
    double maxValue = 0;
    boolean hasData = false;

    for (DonHang dh : dataList) {
        if (dh.getThanhTien() > 0) {
            hasData = true;
            String dateKey = dateFormat.format(dh.getNgayLap());
            dailyRevenue.merge(dateKey, dh.getThanhTien(), Double::sum);
            
            if (dh.getThanhTien() > maxValue) {
                maxValue = dh.getThanhTien();
            }
        }
    }

    // Xử lý khi không có dữ liệu
    if (!hasData) {
        maxValue = 2000000; // Giá trị mặc định
        dailyRevenue.put("Không có dữ liệu", 0.0);
    } else {
        // Thêm buffer 20% để biểu đồ đẹp hơn
        maxValue = maxValue * 1.2;
    }

    dailyRevenue.forEach((date, amount) -> 
        dataset.addValue(amount, "Doanh thu", date)
    );

    if (chartPanel != null) {
        JFreeChart chart = ChartFactory.createBarChart(
            null, null, null, dataset, 
            PlotOrientation.VERTICAL, true, false, false
        );
        
        configureChartAppearance(chart, option, maxValue);
        chartPanel.setChart(chart);
        chartPanel.repaint();
    }
}
    
    private void configureChartAppearance(JFreeChart chart, String option, double maxValue) {
    CategoryPlot plot = chart.getCategoryPlot();
    
    if (maxValue <= 0) {
        maxValue = 2000000; // Giá trị mặc định nếu maxValue không hợp lệ
    }
    
    // Cấu hình trục Y (NumberAxis)
    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setRange(0, maxValue); // Tự động điều chỉnh theo dữ liệu
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    
    rangeAxis.setNumberFormatOverride(NumberFormat.getNumberInstance(Locale.US));
    
    // Phần còn lại giữ nguyên
    BarRenderer renderer = (BarRenderer) plot.getRenderer();
    renderer.setSeriesPaint(0, new Color(70, 130, 180));
    renderer.setShadowVisible(false);
    renderer.setDrawBarOutline(false);
    renderer.setBarPainter(new StandardBarPainter());

    plot.setBackgroundPaint(Color.WHITE);
    plot.setDomainGridlinesVisible(false);
    plot.setRangeGridlinesVisible(false);

    if ("Tháng này".equals(option) || "Tháng trước".equals(option)) {
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);
    }
    
    plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
    
    if ("Hôm nay".equals(option) || "Hôm trước".equals(option)) {
        renderer.setMaximumBarWidth(0.4);
        renderer.setItemMargin(0.1);
    } else {
        renderer.setMaximumBarWidth(0.2);
        renderer.setItemMargin(0.05);
    }
}

    private void updateTable(String option) {
        if (table == null) {
            table = new JTable();
        }

        String[] columns = {"Mã đơn", "Tên khách hàng", "Ngày tạo", "Khách trả"};
        Object[][] data = getFormattedData(option);

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
        configureTableAppearance();
    }

    private Object[][] getFormattedData(String option) {
        List<DonHang> dataList = getDataForOption(option);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#,###");

        Object[][] result = new Object[dataList.size()][4];

        for (int i = 0; i < dataList.size(); i++) {
            DonHang dh = dataList.get(i);
            result[i][0] = "DH" + String.format("%03d", dh.getMaDH());
            result[i][1] = dh.getHoTen();
            result[i][2] = dateFormat.format(dh.getNgayLap());
            result[i][3] = df.format(dh.getThanhTien()); // Test xem giá trị có phải 0 không
;
        }

        return result;
    }

    private List<DonHang> getDataForOption(String option) {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = null;
        java.sql.Date endDate = null;

        switch (option) {
            case "Hôm nay":
                startDate = endDate = new java.sql.Date(calendar.getTimeInMillis());
                break;

            case "Hôm trước":
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                startDate = endDate = new java.sql.Date(calendar.getTimeInMillis());
                break;

            case "7 ngày qua":
                endDate = new java.sql.Date(calendar.getTimeInMillis());
                calendar.add(Calendar.DAY_OF_YEAR, -6);
                startDate = new java.sql.Date(calendar.getTimeInMillis());
                break;

            case "Tháng này":
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = new java.sql.Date(calendar.getTimeInMillis());
                endDate = new java.sql.Date(System.currentTimeMillis());
                break;

            case "Tháng trước":
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = new java.sql.Date(calendar.getTimeInMillis());
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate = new java.sql.Date(calendar.getTimeInMillis());
                break;

            case "Năm nay":
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = new java.sql.Date(calendar.getTimeInMillis());
                endDate = new java.sql.Date(System.currentTimeMillis());
                break;

            default:
                return (List<DonHang>) donHangDAO.selectAll();
        }
            return donHangDAO.selectByDateRangetest(startDate, endDate);
    }

    // Phương thức để lấy ngày hôm trước
    private Date getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    // Lớp nút bo tròn
    class RoundedButton extends JButton {

        private int arcWidth = 5;
        private int arcHeight = 5;

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
            int textX = (getWidth() - fm.stringWidth(getText())) / 2;
            int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(getText(), textX, textY);

            g2.dispose();
        }
    }
}
