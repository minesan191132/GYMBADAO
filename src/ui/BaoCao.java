package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import dao.ThanhToanDAO;
import entity.ThanhToan;
import java.text.DecimalFormat;

public class BaoCao extends JPanel {
    private JTable table;
    private JComboBox<String> cbThoiGian;
    private JComboBox<String> cbPhuongThuc;
    private JPanel chartPanel;
    private DefaultTableModel tableModel;
    private ThanhToanDAO thanhToanDAO;

    public BaoCao() {
        this.thanhToanDAO = new ThanhToanDAO(); 
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Panel lọc
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        filterPanel.add(new JLabel("Khoảng thời gian:"));
        cbThoiGian = new JComboBox<>(new String[]{"Tháng này", "Tháng trước", "Năm nay"});
        filterPanel.add(cbThoiGian);
        
        filterPanel.add(new JLabel("Phương thức thanh toán:"));
        cbPhuongThuc = new JComboBox<>(new String[]{"Tất cả", "Tiền mặt", "Chuyển khoản", "Thẻ tín dụng", "Ví điện tử", "QR Code"});
        filterPanel.add(cbPhuongThuc);

        add(filterPanel, BorderLayout.NORTH);

        // Panel biểu đồ
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Biểu đồ thống kê"));
        add(chartPanel, BorderLayout.CENTER);

        // Bảng
        String[] columnNames = {"Phương thức thanh toán", "Tổng tiền"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(180, 200, 230));
        header.setForeground(Color.BLACK);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    String method = table.getValueAt(row, 0).toString();
                    showPaymentDetails(method);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết thanh toán"));
        add(scrollPane, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DecimalFormat df = new DecimalFormat("#,###");
        
        // Dữ liệu giả lập
        List<ThanhToan> danhSach = List.of(
            new ThanhToan("Tiền mặt", 500000),
            new ThanhToan("Chuyển khoản", 1200000),
            new ThanhToan("Thẻ tín dụng", 800000),
            new ThanhToan("Ví điện tử", 650000),
            new ThanhToan("QR Code", 900000)
        );

        double tongCong = 0;
        for (ThanhToan tt : danhSach) {
            tongCong += tt.getSoTien();
            tableModel.addRow(new Object[]{tt.getPhuongThuc(), df.format(tt.getSoTien())});
            dataset.setValue(tt.getSoTien(), "Tiền đã thanh toán", tt.getPhuongThuc());
        }

        // Thêm hàng tổng cộng
        tableModel.addRow(new Object[]{"Tổng cộng", df.format(tongCong)});

        // Cập nhật biểu đồ
        JFreeChart chart = ChartFactory.createBarChart(
                "Báo Cáo Thanh Toán", "Phương thức", "Số tiền",
                dataset, PlotOrientation.VERTICAL, false, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        chartPanel.removeAll();
        chartPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private void showPaymentDetails(String method) {
        List<ThanhToan> details = thanhToanDAO.layDanhSachThanhToanTheoPhuongThuc(method);
        StringBuilder message = new StringBuilder("Chi tiết thanh toán:\n");
        for (ThanhToan p : details) {
            message.append("- ").append(p.getSoTien()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString(), "Chi tiết - " + method, JOptionPane.INFORMATION_MESSAGE);
    }
}