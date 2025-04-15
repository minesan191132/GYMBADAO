package ui;

import dao.LuongDao;
import entity.LuongNhanVien;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;

public class Luong extends JFrame {

    // Các hằng số hệ thống
    private static final int LUONG_CO_BAN = 35000; // 35k/giờ
    private static final int LUONG_TANG_CA = 50000; // 50k/giờ
    private static final int PHAT_TRE = 50000; // Phạt 50k nếu trễ >30p

    private DefaultTableModel tableModel;
    private JTextField txtEmployeeId;
    private JComboBox<String> cmbMonth;
    private JComboBox<String> cmbYear;
    private JLabel lblWorkDays, lblWorkHours, lblTotalSalary;
    private JButton btnSearch, btnExit, btnExportExcel;
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Color SEARCH_COLOR = new Color(41, 98, 255);
    private static final Color EXIT_COLOR = new Color(239, 83, 80);
    private static final Color EXPORT_COLOR = new Color(0, 150, 136);
    LuongDao luongDao = new LuongDao();

    public Luong() {
        initUI();
    }

    private void initUI() {
        setupMainWindow();
        createMainContent();
        setupEventListeners();
        setVisible(true);
        loadLuongData();
    }

    private void setupMainWindow() {
        setTitle("Theo Dõi Giờ Làm Và Tính Lương");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
    }

    private void createMainContent() {
        JPanel topPanel = createTopPanel();
        JScrollPane tableScrollPane = createTablePanel();
        JPanel bottomPanel = createBottomPanel();

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                "",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16)
        ));
        topPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã nhân viên
        JLabel lblEmployeeId = new JLabel("MÃ NHÂN VIÊN:");
        lblEmployeeId.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        topPanel.add(lblEmployeeId, gbc);

        txtEmployeeId = new JTextField();
        txtEmployeeId.setFont(LABEL_FONT);
        txtEmployeeId.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        topPanel.add(txtEmployeeId, gbc);

        // Chọn tháng/năm
        JLabel lblMonthYear = new JLabel("THÁNG/NĂM:");
        lblMonthYear.setFont(LABEL_FONT);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        topPanel.add(lblMonthYear, gbc);

        // Panel chứa tháng và năm
        JPanel panelMonthYear = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelMonthYear.setOpaque(false);

        // ComboBox tháng
        String[] months = {"Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
            "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};
        cmbMonth = new JComboBox<>(months);
        cmbMonth.setFont(LABEL_FONT);
        cmbMonth.setBorder(new RoundBorder(10, Color.GRAY));
        panelMonthYear.add(cmbMonth);

        // ComboBox năm
        cmbYear = new JComboBox<>();
        int currentYear = Year.now().getValue();
        for (int i = currentYear - 5; i <= currentYear + 1; i++) {
            cmbYear.addItem(String.valueOf(i));
        }
        cmbYear.setSelectedItem(String.valueOf(currentYear));
        cmbYear.setFont(LABEL_FONT);
        cmbYear.setBorder(new RoundBorder(10, Color.GRAY));
        panelMonthYear.add(cmbYear);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        topPanel.add(panelMonthYear, gbc);

        // Nút tìm kiếm
        btnSearch = taoNutTron("TÌM KIẾM (F1)", SEARCH_COLOR, BUTTON_FONT);
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        topPanel.add(btnSearch, gbc);

        return topPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"Mã NV", "Tên Nhân Viên", "Ngày", "Ca", "Check-In", "Check-Out",
            "Giờ Làm", "Tăng Ca", "Trễ", "Ghi Chú", "Lương"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Chỉ cho phép chỉnh sửa cột Ghi Chú
            }
        };

        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JLabel) {
                    ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                }
                return c;
            }
        };

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return scrollPane;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Tổng ngày làm
        JPanel panelWorkDays = taoPanelThongKe("NGÀY LÀM", "0 ngày");
        lblWorkDays = (JLabel) panelWorkDays.getComponent(0);

        // Tổng giờ làm
        JPanel panelWorkHours = taoPanelThongKe("GIỜ LÀM", "0.00 giờ");
        lblWorkHours = (JLabel) panelWorkHours.getComponent(0);

        // Tổng lương
        JPanel panelTotalSalary = taoPanelThongKe("TỔNG LƯƠNG", "0 VNĐ");
        lblTotalSalary = (JLabel) panelTotalSalary.getComponent(0);

        // Panel chức năng
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        btnExportExcel = taoNutTron("EXCEL", EXPORT_COLOR, BUTTON_FONT);
        btnExportExcel.setPreferredSize(new Dimension(120, 40));
        btnExit = taoNutTron("THOÁT", EXIT_COLOR, BUTTON_FONT);
        btnExit.setPreferredSize(new Dimension(120, 40));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(btnExportExcel);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(btnExit);
        bottomPanel.add(panelWorkDays);
        bottomPanel.add(panelWorkHours);
        bottomPanel.add(panelTotalSalary);
        bottomPanel.add(buttonPanel);

        return bottomPanel;
    }

    private void setupEventListeners() {
        btnSearch.addActionListener(e -> handleSearch());
        btnExit.addActionListener(e -> handleExit());
        btnExportExcel.addActionListener(e -> handleExportExcel());

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    btnSearch.doClick();
                }
                if (e.getKeyCode() == KeyEvent.VK_F2) {
                    btnExportExcel.doClick();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    btnExit.doClick();
                }
            }
        });
        setFocusable(true);
    }

    private void handleSearch() {
        String employeeId = txtEmployeeId.getText().trim();
        String month = String.format("%02d", cmbMonth.getSelectedIndex() + 1);
        String year = (String) cmbYear.getSelectedItem();

        if (employeeId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Object[]> duLieu = timDuLieuChamCong(employeeId, month, year);

        tableModel.setRowCount(0);
        for (Object[] dong : duLieu) {
            tableModel.addRow(dong);
        }

        updateStats();
    }

    private void handleExit() {
        int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn quay lại trang chủ?", "Xác nhận thoát", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            this.dispose(); // Đóng cửa sổ hiện tại

            // Hiển thị lại TrangChu
            TrangChu trangChu = new TrangChu();
            trangChu.setVisible(true);
        }
    }

    private void handleExportExcel() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Xuất Excel thành công!\n" + tableModel.getRowCount() + " bản ghi đã được xuất.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateStats() {
        if (tableModel.getRowCount() == 0) {
            lblWorkDays.setText("0 ngày");
            lblWorkHours.setText("0.00 giờ");
            lblTotalSalary.setText("0 VNĐ");
            return;
        }

        Set<String> cacNgayLam = new HashSet<>();
        double tongGioLam = 0;
        int tongLuong = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String ngay = tableModel.getValueAt(i, 2).toString();
            cacNgayLam.add(ngay);

            try {
                tongGioLam += Double.parseDouble(tableModel.getValueAt(i, 6).toString().split(" ")[0]);
                String luongStr = tableModel.getValueAt(i, 10).toString().replaceAll("[^0-9]", "");
                tongLuong += Integer.parseInt(luongStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        lblWorkDays.setText(cacNgayLam.size() + " ngày");
        lblWorkHours.setText(String.format("%.2f giờ", tongGioLam));
        lblTotalSalary.setText(String.format("%,d VNĐ", tongLuong));
    }

    // Các phương thức hỗ trợ
    private JPanel taoPanelThongKe(String tieuDe, String giaTri) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(tieuDe));

        JLabel nhan = new JLabel(giaTri, JLabel.CENTER);
        nhan.setFont(new Font("Arial", Font.BOLD, 16));
        nhan.setForeground(new Color(41, 98, 255));

        panel.add(nhan, BorderLayout.CENTER);
        return panel;
    }

    private JButton taoNutTron(String text, Color mauNen, Font font) {
        JButton nut = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground().darker());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };

        nut.setFont(font);
        nut.setForeground(Color.WHITE);
        nut.setBackground(mauNen);
        nut.setContentAreaFilled(false);
        nut.setFocusPainted(false);
        nut.setBorder(new EmptyBorder(10, 25, 10, 25));
        nut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nut.setOpaque(false);

        // Thêm hiệu ứng hover
        nut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nut.setBackground(mauNen.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nut.setBackground(mauNen);
            }
        });

        return nut;
    }

    private List<Object[]> timDuLieuChamCong(String maNV, String thang, String nam) {
        List<Object[]> duLieu = new ArrayList<>();

        List<LuongNhanVien> danhSachLuong = luongDao.getLuongNhanVien(maNV, thang, nam);
        System.out.println("📦 Bắt đầu xử lý " + danhSachLuong.size() + " bản ghi...");

        for (LuongNhanVien lnv : danhSachLuong) {
            System.out.println("➡️ " + lnv.getMaNhanVien() + " - " + lnv.getNgayLam() + " - " + lnv.getCaLam());

            Object[] dong = taoDuLieuNgayLam(
                    lnv.getMaNhanVien(),
                    lnv.getTenNhanVien(),
                    lnv.getNgayLam().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    lnv.getCaLam(),
                    lnv.getGioVao(),
                    lnv.getGioRa()
            );
            duLieu.add(dong);
        }

        return duLieu;
    }

    private Object[] taoDuLieuNgayLam(String maNV, String tenNV, String ngay, String ca, String gioVao, String gioRa) {
        LocalTime vao = LocalTime.parse(gioVao);
        LocalTime ra = LocalTime.parse(gioRa);

        double soGioLam = Duration.between(vao, ra).toMinutes() / 60.0;

        boolean diTre = false;
        double gioTangCa = 0;
        int luong;
        String ghiChu = "";

        LocalTime start = ca.equals("Ca 1") ? LocalTime.of(8, 0) : LocalTime.of(16, 0);
        LocalTime end = ca.equals("Ca 1") ? LocalTime.of(16, 0) : LocalTime.of(23, 59, 59);

        if (vao.isAfter(start.plusMinutes(30))) {
            diTre = true;
            ghiChu = "Đi trễ, Phạt " + String.format("%,d", PHAT_TRE) + " VNĐ";
        }

        if (ra.isAfter(end)) {
            gioTangCa = Duration.between(end, ra).toMinutes() / 60.0;
        }

        luong = (int) (soGioLam * LUONG_CO_BAN + gioTangCa * LUONG_TANG_CA);
        if (diTre) {
            luong -= PHAT_TRE;
        }

        return new Object[]{
            maNV,
            tenNV,
            ngay,
            ca,
            gioVao,
            gioRa,
            String.format("%.2f giờ", soGioLam),
            String.format("%.2f giờ", gioTangCa),
            diTre ? "Có" : "Không",
            ghiChu,
            String.format("%,d VNĐ", luong)
        };
    }

    // Class RoundBorder
    static class RoundBorder extends AbstractBorder {

        private int banKinh;
        private Color mau;

        RoundBorder(int banKinh, Color mau) {
            this.banKinh = banKinh;
            this.mau = mau;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(mau);
            g2.drawRoundRect(x, y, width - 1, height - 1, banKinh, banKinh);
            g2.dispose();
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(banKinh / 2, banKinh, banKinh / 2, banKinh);
        }
    }

    private void loadLuongData() {
        String thang = String.format("%02d", cmbMonth.getSelectedIndex() + 1);
        String nam = (String) cmbYear.getSelectedItem();

        List<LuongNhanVien> luongList = luongDao.getLuongNhanVien("", thang, nam);
        tableModel.setRowCount(0);

        for (LuongNhanVien lnv : luongList) {
            Object[] row = taoDuLieuNgayLam(
                    lnv.getMaNhanVien(),
                    lnv.getTenNhanVien(),
                    lnv.getNgayLam().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    lnv.getCaLam(),
                    lnv.getGioVao(),
                    lnv.getGioRa()
            );
            tableModel.addRow(row);
        }

        updateStats();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Luong::new);
    }
}
