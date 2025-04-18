package ui;

import dao.ChamCongDao;
import entity.chamCong;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.imageio.ImageIO;
import java.util.List;
import javax.swing.Timer;

public class ChamCong extends JFrame {

    private DefaultTableModel tableModel;
    private JLabel lblCurrentDate, lblCurrentTime, lblTotalDays, lblTotalHours, lblEmployeeImage, lblShift;
    private JTextField txtEmployeeId;
    private JButton btnUpload, btnCheckIn, btnCheckOut, btnExit;
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Color CHECK_IN_COLOR = new Color(46, 125, 50);
    private static final Color CHECK_OUT_COLOR = new Color(198, 40, 40);
    private static final Color EXIT_COLOR = new Color(96, 125, 139);
    private static final Color FIELD_BG_COLOR = new Color(240, 240, 240);
    ChamCongDao chamCongDao = new ChamCongDao();
    private Timer inputTimer;
    //
    private String currentMaNV;

    public ChamCong(String maNV) {
        this.currentMaNV = maNV;
        initUI();
        txtEmployeeId.setText(maNV);
        loadTodayCheckInIfExists(); // 👈 tự động tải dữ liệu
    }

    class RoundButton extends JButton {

        private Color hoverColor;
        private Color originalColor;
        private int arcWidth = 25;
        private int arcHeight = 25;

        public RoundButton(String text, Color bgColor, Font font) {
            super(text);
            this.originalColor = bgColor;
            this.hoverColor = bgColor.brighter();
            setFont(font);
            setForeground(Color.WHITE);
            setBackground(originalColor);
            setFocusPainted(false);
            setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            setContentAreaFilled(false);
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(originalColor);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            g2d.setColor(getBackground().darker());
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
            super.paintComponent(g2d);
            g2d.dispose();
        }
    }

    class RoundTextField extends JTextField {

        public RoundTextField(int columns) {
            super(columns);
            setOpaque(false);
            setBorder(new EmptyBorder(10, 15, 10, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g2d);
            g2d.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            g2d.dispose();
        }
    }

    private void startTimeUpdate() {
        Timer timer = new Timer(1000, e -> {
            lblCurrentTime.setText(getCurrentTime());
            lblShift.setText(getCurrentShift());
            lblShift.setForeground(getCurrentHour() < 16 ? new Color(0, 150, 136) : new Color(156, 39, 176));
        });
        timer.start();
    }

    private void initUI() {
        setupMainWindow();
        createMainContent();
        setupEventListeners();
        startTimeUpdate();
        setVisible(true);
    }

    private void setupMainWindow() {
        setTitle("Check-in và Check-out Cho Nhân Viên");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        utils.Window.setAppIcon(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Chặn thoát ngay
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        ChamCong.this,
                        "Bạn có chắc chắn muốn thoát?",
                        "Xác nhận thoát",
                        JOptionPane.YES_NO_OPTION
                );
                if (option == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });

    }

    private void createMainContent() {
        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel checkPanel = createCheckPanel();
        JScrollPane tableScrollPane = createTablePanel();
        JPanel statsPanel = createStatsPanel();

        leftPanel.add(checkPanel, BorderLayout.NORTH);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        leftPanel.add(statsPanel, BorderLayout.SOUTH);
        return leftPanel;
    }

    private JPanel createCheckPanel() {
        JPanel checkPanel = new JPanel(new GridBagLayout());
        checkPanel.setOpaque(false);
        checkPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblId = new JLabel("MÃ NHÂN VIÊN:");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        checkPanel.add(lblId, gbc);

        txtEmployeeId = new RoundTextField(20);
        txtEmployeeId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtEmployeeId.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        gbc.gridy = 0;
        checkPanel.add(txtEmployeeId, gbc);

        btnUpload = new RoundButton("TẢI ẢNH", new Color(41, 98, 255), new Font("Segoe UI", Font.BOLD, 18));
        btnUpload.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 2;
        gbc.gridy = 0;
        checkPanel.add(btnUpload, gbc);

        JLabel lblDate = new JLabel("NGÀY:");
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        checkPanel.add(lblDate, gbc);

        lblCurrentDate = new JLabel(getCurrentDate());
        lblCurrentDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 1;
        checkPanel.add(lblCurrentDate, gbc);

        JLabel lblTime = new JLabel("GIỜ HIỆN TẠI:");
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        checkPanel.add(lblTime, gbc);

        lblCurrentTime = new JLabel(getCurrentTime());
        lblCurrentTime.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 2;
        checkPanel.add(lblCurrentTime, gbc);

        JLabel lblShiftText = new JLabel("CA HIỆN TẠI:");
        lblShiftText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        checkPanel.add(lblShiftText, gbc);

        lblShift = new JLabel(getCurrentShift());
        lblShift.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblShift.setForeground(getCurrentHour() < 16 ? new Color(0, 150, 136) : new Color(156, 39, 176));
        gbc.gridx = 1;
        gbc.gridy = 3;
        checkPanel.add(lblShift, gbc);

        btnCheckIn = new RoundButton("CHECK-IN (F1)", new Color(46, 125, 50), new Font("Segoe UI", Font.BOLD, 18));
        btnCheckIn.setPreferredSize(new Dimension(180, 50));
        gbc.gridx = 0;
        gbc.gridy = 4;
        checkPanel.add(btnCheckIn, gbc);

        btnCheckOut = new RoundButton("CHECK-OUT (F2)", new Color(198, 40, 40), new Font("Segoe UI", Font.BOLD, 18));
        btnCheckOut.setPreferredSize(new Dimension(180, 50));
        gbc.gridx = 1;
        gbc.gridy = 4;
        checkPanel.add(btnCheckOut, gbc);
        JButton btnLoadCheckIns = new RoundButton("Hiển thị check-in", new Color(41, 98, 255), new Font("Segoe UI", Font.BOLD, 18));
        btnLoadCheckIns.setPreferredSize(new Dimension(200, 40));
        gbc.gridx = 1;
        gbc.gridy = 5;
        return checkPanel;
    }

    private void loadTodayCheckInIfExists() {
        String employeeId = txtEmployeeId.getText().trim();
        if (employeeId.isEmpty()) {
            return;
        }

        ChamCongDao dao = new ChamCongDao();
        chamCong cc = dao.findByMaNVAndNgay(employeeId, LocalDate.now());

        if (cc != null) {
            String checkInStr = cc.getCheckIn() != null ? cc.getCheckIn().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "";
            String checkOutStr = cc.getCheckOut() != null ? cc.getCheckOut().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "";
            double hours = 0;
            if (cc.getCheckIn() != null && cc.getCheckOut() != null) {
                hours = ChronoUnit.MINUTES.between(cc.getCheckIn(), cc.getCheckOut()) / 60.0;
            }

            // Xóa dữ liệu cũ nếu có
            tableModel.setRowCount(0);

            tableModel.addRow(new Object[]{
                cc.getNgay().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                checkInStr,
                checkOutStr,
                hours > 0 ? String.format("%.2f giờ", hours) : "",
                cc.getCa()
            });

            updateStats();
        }
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"NGÀY", "CHECK-IN", "CHECK-OUT", "TỔNG GIỜ", "CA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        scrollPane.setPreferredSize(new Dimension(750, 400));

        return scrollPane;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        statsPanel.setBorder(new EmptyBorder(15, 25, 25, 25));

        JPanel daysPanel = new JPanel();
        daysPanel.setBorder(BorderFactory.createTitledBorder("TỔNG NGÀY LÀM"));
        lblTotalDays = new JLabel("0 ngày", JLabel.CENTER);
        lblTotalDays.setFont(new Font("Segoe UI", Font.BOLD, 18));
        daysPanel.add(lblTotalDays);

        JPanel hoursPanel = new JPanel();
        hoursPanel.setBorder(BorderFactory.createTitledBorder("TỔNG GIỜ LÀM"));
        lblTotalHours = new JLabel("0.00 giờ", JLabel.CENTER);
        lblTotalHours.setFont(new Font("Segoe UI", Font.BOLD, 18));
        hoursPanel.add(lblTotalHours);

        btnExit = new RoundButton("THOÁT (ESC)", new Color(96, 125, 139), new Font("Segoe UI", Font.BOLD, 18));
        btnExit.setPreferredSize(new Dimension(180, 50));

        statsPanel.add(daysPanel);
        statsPanel.add(hoursPanel);
        statsPanel.add(btnExit);

        return statsPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(new Color(200, 200, 200));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        rightPanel.setPreferredSize(new Dimension(300, 0));

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createTitledBorder("ẢNH NHÂN VIÊN"));

        lblEmployeeImage = new JLabel("", JLabel.CENTER);
        lblEmployeeImage.setPreferredSize(new Dimension(250, 250));
        loadDefaultImage();

        imagePanel.add(lblEmployeeImage, BorderLayout.CENTER);
        rightPanel.add(imagePanel, BorderLayout.NORTH);

        return rightPanel;
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

    private void setupEventListeners() {
        btnUpload.addActionListener(e -> handleUploadImage());
        btnCheckIn.addActionListener(e -> handleCheckIn());
        btnCheckOut.addActionListener(e -> handleCheckOut());
        btnExit.addActionListener(e -> handleExit());

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    btnCheckIn.doClick();
                }
                if (e.getKeyCode() == KeyEvent.VK_F2) {
                    btnCheckOut.doClick();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    btnExit.doClick();
                }
            }
        });
        setFocusable(true);
    }

    private void handleUploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh nhân viên");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(selectedFile);
                if (img != null) {
                    ImageIcon icon = new ImageIcon(getScaledImage(img, 250, 250));
                    lblEmployeeImage.setIcon(icon);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Không thể tải ảnh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleCheckIn() {
        String employeeId = txtEmployeeId.getText().trim();
        if (employeeId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate ngay = LocalDate.now();
        LocalTime checkInTime = LocalTime.now();
        String ca = (checkInTime.isBefore(LocalTime.NOON)) ? "CA1" : "CA2";

        ChamCongDao dao = new ChamCongDao();
        if (dao.isCheckedIn(employeeId, ngay)) {
            JOptionPane.showMessageDialog(this, "Bạn đã check-in hôm nay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = dao.insertCheckIn(employeeId, ngay, ca, checkInTime);
        if (success) {
            tableModel.addRow(new Object[]{
                ngay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                checkInTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                "", "", ca
            });
            updateStats();
            JOptionPane.showMessageDialog(this, "CHECK-IN THÀNH CÔNG!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi ghi nhận check-in!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCheckOut() {
        String employeeId = txtEmployeeId.getText().trim();
        if (employeeId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate ngay = LocalDate.now();
        ChamCongDao dao = new ChamCongDao();
        chamCong cc = dao.findByMaNVAndNgay(employeeId, ngay);

        if (cc == null) {
            JOptionPane.showMessageDialog(this, "Bạn chưa check-in hôm nay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cc.getCheckOut() != null) {
            JOptionPane.showMessageDialog(this, "Bạn đã check-in và check-out rồi!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (cc.getCheckIn() == null) {
            JOptionPane.showMessageDialog(this, "Dữ liệu check-in bị thiếu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalTime checkOutTime = LocalTime.now();
        double totalHours = ChronoUnit.MINUTES.between(cc.getCheckIn(), checkOutTime) / 60.0;

        boolean success = dao.updateCheckOut(employeeId, ngay, checkOutTime);
        if (success) {
            // Cập nhật bảng giao diện
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{
                ngay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                cc.getCheckIn().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                checkOutTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                String.format("%.2f giờ", totalHours),
                cc.getCa()
            });

            updateStats();
            JOptionPane.showMessageDialog(this, "CHECK-OUT THÀNH CÔNG!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật check-out!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDefaultImage() {
        try {
            BufferedImage defaultImage = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = defaultImage.createGraphics();
            g2d.setColor(new Color(220, 220, 220));
            g2d.fillRect(0, 0, 250, 250);
            g2d.setColor(Color.GRAY);
            g2d.fillOval(75, 50, 100, 100);
            g2d.fillRect(100, 150, 50, 70);
            g2d.dispose();
            lblEmployeeImage.setIcon(new ImageIcon(defaultImage));
        } catch (Exception e) {
            lblEmployeeImage.setText("Không tải được ảnh");
        }
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    private void updateStats() {
        int days = tableModel.getRowCount();
        double totalHours = 0;

        for (int i = 0; i < days; i++) {
            String hoursStr = tableModel.getValueAt(i, 3).toString();
            if (!hoursStr.isEmpty()) {
                totalHours += Double.parseDouble(hoursStr.split(" ")[0]);
            }
        }

        lblTotalDays.setText(days + " ngày");
        lblTotalHours.setText(String.format("%.2f giờ", totalHours));
    }

    private String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private String getCurrentShift() {
        return getCurrentHour() < 16 ? "CA 1" : "CA 2";
    }

    private int getCurrentHour() {
        return LocalTime.now().getHour();
    }

    private double calculateHours(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime startTime = LocalTime.parse(start, formatter);
        LocalTime endTime = LocalTime.parse(end, formatter);
        return ChronoUnit.MINUTES.between(startTime, endTime) / 60.0;
    }

    public static void main(String[] args) {
    }
}
