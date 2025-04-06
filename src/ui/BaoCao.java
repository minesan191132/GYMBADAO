package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

public class BaoCao extends JPanel {

    public BaoCao() {

        setLayout(new BorderLayout()); // Sử dụng BorderLayout cho panel chính

        // Thêm header panel vào phía Bắc
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Thêm main panel vào trung tâm
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null); // Giữ nguyên layout null như code gốc
        headerPanel.setBackground(new Color(241, 239, 236));
        headerPanel.setPreferredSize(new Dimension(800, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        // Nút xem theo ngày (đã có)
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

        int yPosition = 25; // Giữ nguyên vị trí chiều cao
        int iconWidth = 30;
        int textWidth = 80;
        int spacing = 15; // GIẢM khoảng cách giữa 2 nhóm xuống còn 15px

        // Trợ giúp - vị trí mới
        int helpX = 250; // Tăng từ 240 lên 250
        JLabel lblHelpIcon = new JLabel(new ImageIcon("D:/GYMBADAO/src/icon/question-sign.png"));
        lblHelpIcon.setBounds(helpX, yPosition, iconWidth, 30);
        headerPanel.add(lblHelpIcon);

        JLabel lblHelpText = new JLabel("Trợ giúp");
        lblHelpText.setBounds(helpX + iconWidth + 5, yPosition, textWidth, 30); // Text cách icon 5px
        lblHelpText.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(lblHelpText);

        // Góp ý - vị trí mới (giảm khoảng cách)
        int feedbackX = helpX + iconWidth + textWidth + spacing; // Tính toán tự động
        JLabel lblFeedbackIcon = new JLabel(new ImageIcon("D:/GYMBADAO/src/icon/heart.png"));
        lblFeedbackIcon.setBounds(feedbackX, yPosition, iconWidth, 30);
        headerPanel.add(lblFeedbackIcon);

        JLabel lblFeedbackText = new JLabel("Góp ý");
        lblFeedbackText.setBounds(feedbackX + iconWidth + 5, yPosition, textWidth, 30);
        lblFeedbackText.setFont(new Font("Arial", Font.PLAIN, 14));
        headerPanel.add(lblFeedbackText);

        return headerPanel;
    }

    private JPanel createIconPanel(String iconPath, String text) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentY(Component.CENTER_ALIGNMENT); // Căn giữa dọc

        JLabel iconLabel = new JLabel(new ImageIcon(iconPath));
        iconLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        panel.add(Box.createHorizontalStrut(5)); // Khoảng cách icon-text

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.add(textLabel);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25)); // Cùng padding với header
        chartContainer.add(createChartPanel(), BorderLayout.CENTER);

        mainPanel.add(chartContainer, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createChartPanel() {
        // Panel chính sử dụng BorderLayout
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(240, 240, 240));

        // ===== PHẦN BIỂU ĐỒ =====
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double tienMat = 2000000;
        double chuyenKhoan = 1500000;
        double tong = tienMat + chuyenKhoan;

        // Thêm dữ liệu vào biểu đồ
        dataset.addValue(tienMat, "Tiền mặt", "");
        dataset.addValue(chuyenKhoan, "Chuyển khoản", "");

        JFreeChart chart = ChartFactory.createBarChart(
                null, null, null, dataset,
                PlotOrientation.VERTICAL,
                true, false, false
        );

        // Cấu hình biểu đồ
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setSeriesPaint(0, new Color(70, 130, 180));
        renderer.setSeriesPaint(1, new Color(70, 130, 180));
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        renderer.setBarPainter(new StandardBarPainter()); // Thêm dòng này

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(false); // Tắt đường kẻ dọc
        plot.setRangeGridlinesVisible(false);  // Tắt đường kẻ ngang

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(750, 300)); // Kích thước lớn hơn

        // Panel chứa biểu đồ
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        chartContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== PHẦN BẢNG =====
        DecimalFormat df = new DecimalFormat("#,##0");

        Object[][] data = {
            {"Tổng", df.format(tong)},
            {"Tiền mặt", df.format(tienMat)},
            {"Chuyển khoản", df.format(chuyenKhoan)}
        };

        String[] columns = {"Phương thức thanh toán", "Tiền đã thanh toán"};

        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                // Đổi màu chữ cho dòng "Tiền mặt" và "Chuyển khoản"
                if (row == 1 || row == 2) { // Dòng 1 và 2 (0-based index)
                    c.setForeground(Color.BLUE); // Màu xanh
                } else {
                    c.setForeground(Color.BLACK); // Màu đen cho dòng "Tổng"
                }

                return c;
            }
        };

// Renderer căn giữa
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

// Áp dụng renderer
        table.setDefaultRenderer(Object.class, centerRenderer);

// Cấu hình kích thước
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setFont(new Font("Arial", Font.PLAIN, 13));

// Header
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);

// Cấu hình scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());

// Panel chứa bảng
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.setBackground(new Color(241, 239, 236));
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Thêm cả 2 vào main container
        mainContainer.add(chartContainer, BorderLayout.NORTH);
        mainContainer.add(tableContainer, BorderLayout.CENTER);

        return mainContainer;
    }

    private void showDateRangeDialog() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Chọn khoảng thời gian", true);
        dialog.setSize(400, 220);
        dialog.setLocationRelativeTo(parentFrame);  // Sửa thành parentFrame
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
}
