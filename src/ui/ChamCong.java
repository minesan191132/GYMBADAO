package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class ChamCong {
    private static DefaultTableModel model;
    private static JLabel lblTotalDays, lblTotalHours;
    private static JLabel lblEmployeeImage;

    public static void main(String[] args) {
        JFrame frame = new JFrame("CHẤM CÔNG NHÂN VIÊN");
        frame.setSize(1100, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // ======== STYLE ========
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);
        Color checkInColor = new Color(46, 125, 50);
        Color checkOutColor = new Color(198, 40, 40);
        Color exitColor = new Color(96, 125, 139);
        Color fieldBgColor = new Color(240, 240, 240);

        // ======== PANEL CHÍNH (BÊN TRÁI) ========
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ======== PANEL CHẤM CÔNG ========
        JPanel checkPanel = new JPanel(new GridBagLayout());
        checkPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        checkPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 1: Mã nhân viên
        JLabel lblId = new JLabel("MÃ NHÂN VIÊN:");
        lblId.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        checkPanel.add(lblId, gbc);

        JTextField txtId = new RoundTextField(20);
        txtId.setFont(textFont);
        txtId.setBackground(fieldBgColor);
        txtId.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        gbc.gridx = 1; gbc.gridy = 0;
        checkPanel.add(txtId, gbc);

        // Dòng 2: Nút tải ảnh
        JButton btnUpload = new RoundButton("TẢI ẢNH", new Color(41, 98, 255), buttonFont);
        btnUpload.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 2; gbc.gridy = 0;
        checkPanel.add(btnUpload, gbc);

        // Dòng 3: Ngày
        JLabel lblDate = new JLabel("NGÀY:");
        lblDate.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1;
        checkPanel.add(lblDate, gbc);

        JLabel lblCurrentDate = new JLabel(getCurrentDate());
        lblCurrentDate.setFont(textFont);
        gbc.gridx = 1; gbc.gridy = 1;
        checkPanel.add(lblCurrentDate, gbc);

        // Dòng 4: Giờ
        JLabel lblTime = new JLabel("GIỜ HIỆN TẠI:");
        lblTime.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 2;
        checkPanel.add(lblTime, gbc);

        JLabel lblCurrentTime = new JLabel(getCurrentTime());
        lblCurrentTime.setFont(textFont);
        gbc.gridx = 1; gbc.gridy = 2;
        checkPanel.add(lblCurrentTime, gbc);

        // Dòng 5: Nút Check-in/out
        JButton btnCheckIn = new RoundButton("CHECK-IN (F1)", checkInColor, buttonFont);
        btnCheckIn.setPreferredSize(new Dimension(180, 50));
        gbc.gridx = 0; gbc.gridy = 3;
        checkPanel.add(btnCheckIn, gbc);

        JButton btnCheckOut = new RoundButton("CHECK-OUT (F2)", checkOutColor, buttonFont);
        btnCheckOut.setPreferredSize(new Dimension(180, 50));
        gbc.gridx = 1; gbc.gridy = 3;
        checkPanel.add(btnCheckOut, gbc);

        // ======== BẢNG LỊCH SỬ (KÉO DÀI) ========
        String[] columns = {"NGÀY", "CHECK-IN", "CHECK-OUT", "TỔNG GIỜ"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Căn giữa toàn bộ nội dung bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        scrollPane.setPreferredSize(new Dimension(750, 400)); // Tăng chiều cao bảng

        // ======== PANEL THỐNG KÊ ========
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 25, 25));

        // Tổng ngày (căn giữa)
        JPanel daysPanel = new RoundPanel();
        daysPanel.setBorder(BorderFactory.createTitledBorder("TỔNG NGÀY LÀM"));
        lblTotalDays = new JLabel("0 ngày", JLabel.CENTER);
        lblTotalDays.setFont(new Font("Segoe UI", Font.BOLD, 18));
        daysPanel.add(lblTotalDays);

        // Tổng giờ (căn giữa)
        JPanel hoursPanel = new RoundPanel();
        hoursPanel.setBorder(BorderFactory.createTitledBorder("TỔNG GIỜ LÀM"));
        lblTotalHours = new JLabel("0.00 giờ", JLabel.CENTER);
        lblTotalHours.setFont(new Font("Segoe UI", Font.BOLD, 18));
        hoursPanel.add(lblTotalHours);

        // Nút thoát
        JButton btnExit = new RoundButton("THOÁT (ESC)", exitColor, buttonFont);
        btnExit.setPreferredSize(new Dimension(180, 50));

        statsPanel.add(daysPanel);
        statsPanel.add(hoursPanel);
        statsPanel.add(btnExit);

        // Thêm các component vào panel trái
        leftPanel.add(checkPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(statsPanel, BorderLayout.SOUTH);

        // ======== PANEL ẢNH NHÂN VIÊN (BÊN PHẢI) ========
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(300, 0));

        // Panel hiển thị ảnh (gọn gàng)
        JPanel imagePanel = new RoundPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createTitledBorder("ẢNH NHÂN VIÊN"));

        lblEmployeeImage = new JLabel("", JLabel.CENTER);
        lblEmployeeImage.setPreferredSize(new Dimension(250, 250));
        loadDefaultImage();

        imagePanel.add(lblEmployeeImage, BorderLayout.CENTER);
        rightPanel.add(imagePanel, BorderLayout.NORTH);

        // ======== THÊM COMPONENTS VÀO FRAME ========
        frame.add(leftPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        // ======== XỬ LÝ SỰ KIỆN ========
        // Timer cập nhật giờ
        Timer timer = new Timer(1000, e -> lblCurrentTime.setText(getCurrentTime()));
        timer.start();

        // Sự kiện tải ảnh
        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn ảnh nhân viên");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedImage img = ImageIO.read(selectedFile);
                    if (img != null) {
                        ImageIcon icon = new ImageIcon(getScaledImage(img, 250, 250));
                        lblEmployeeImage.setIcon(icon);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Không thể tải ảnh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCheckIn.addActionListener(e -> {
            String employeeId = txtId.getText().trim();
            if (employeeId.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Vui lòng nhập mã nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String today = getCurrentDate();
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(today)) {
                    JOptionPane.showMessageDialog(frame, "Bạn đã check-in hôm nay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            model.addRow(new Object[]{today, getCurrentTime(), "", ""});
            updateStats();
            JOptionPane.showMessageDialog(frame, "CHECK-IN THÀNH CÔNG!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        btnCheckOut.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Vui lòng chọn bản ghi check-in!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!model.getValueAt(row, 2).toString().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Bản ghi này đã check-out!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String checkInTime = (String) model.getValueAt(row, 1);
            String checkOutTime = getCurrentTime();
            double totalHours = calculateHours(checkInTime, checkOutTime);
            
            model.setValueAt(checkOutTime, row, 2);
            model.setValueAt(String.format("%.2f giờ", totalHours), row, 3);
            updateStats();
            
            JOptionPane.showMessageDialog(frame, 
                "CHECK-OUT THÀNH CÔNG!\nTổng giờ: " + totalHours + " giờ", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        btnExit.addActionListener(e -> frame.dispose());

        // Phím tắt
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) btnCheckIn.doClick();
                if (e.getKeyCode() == KeyEvent.VK_F2) btnCheckOut.doClick();
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) btnExit.doClick();
            }
        });

        frame.setVisible(true);
    }

    private static void loadDefaultImage() {
        try {
            BufferedImage defaultImage = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = defaultImage.createGraphics();
            g2d.setColor(new Color(220, 220, 220));
            g2d.fillRect(0, 0, 250, 250);
            
            // Vẽ icon người
            g2d.setColor(Color.GRAY);
            g2d.fillOval(75, 50, 100, 100); // Đầu
            g2d.fillRect(100, 150, 50, 70); // Thân
            
            ImageIcon icon = new ImageIcon(defaultImage);
            lblEmployeeImage.setIcon(icon);
            g2d.dispose();
        } catch (Exception e) {
            lblEmployeeImage.setText("Không tải được ảnh");
        }
    }

    private static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    private static void updateStats() {
        int days = model.getRowCount();
        double totalHours = 0;
        
        for (int i = 0; i < days; i++) {
            String hoursStr = model.getValueAt(i, 3).toString();
            if (!hoursStr.isEmpty()) {
                totalHours += Double.parseDouble(hoursStr.split(" ")[0]);
            }
        }
        
        lblTotalDays.setText(days + " ngày");
        lblTotalHours.setText(String.format("%.2f giờ", totalHours));
    }

    private static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private static String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private static double calculateHours(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime startTime = LocalTime.parse(start, formatter);
        LocalTime endTime = LocalTime.parse(end, formatter);
        return ChronoUnit.MINUTES.between(startTime, endTime) / 60.0;
    }

    // ======== CÁC LỚP TÙY CHỈNH ========
    static class RoundButton extends JButton {
        private int arcWidth = 25;
        private int arcHeight = 25;
        private Color hoverColor;
        private Color originalColor;
        private Shape shape;

        public RoundButton(String text, Color bgColor, Font font) {
            super(text);
            setFont(font);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorder(new EmptyBorder(10, 25, 10, 25));
            setContentAreaFilled(false);
            setOpaque(false);
            
            this.originalColor = bgColor;
            this.hoverColor = bgColor.brighter();
            setBackground(originalColor);
            
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
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            
            g2.setColor(getBackground().darker());
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arcWidth, arcHeight);
            
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Không vẽ border mặc định
        }
        
        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, arcWidth, arcHeight);
            }
            return shape.contains(x, y);
        }
    }

    static class RoundTextField extends JTextField {
        public RoundTextField(int columns) {
            super(columns);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(200, 200, 200));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            g2.dispose();
        }
    }

    static class RoundPanel extends JPanel {
        public RoundPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            g2.setColor(new Color(200, 200, 200));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            g2.dispose();
        }
    }
}