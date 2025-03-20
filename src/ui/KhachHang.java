package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.border.EmptyBorder;

public class KhachHang extends JPanel {

    private JPanel formPanel;
    private JPanel listPanel;
    private JButton btnDangKy;
    private JButton btnDanhSach;
    private DefaultTableModel tableModel;
    private JTextField txtMaTV, txtTenTV, txtNgayDK, txtSoDT;
    private JComboBox<String> cboGoiTap;

    public KhachHang() {
        setLayout(null);
        setBounds(0, 0, 800, 650);
     
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(-190, 0, 1000, 650);
        add(mainPanel);
        mainPanel.setBackground(new Color(240, 240, 240));
        // Nút Đăng ký và Danh sách
        btnDangKy = createGradientButton("Đăng ký", new Color(255, 153, 51), new Color(255, 94, 58));
        btnDangKy.setBounds(220, 20, 150, 40);
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setFont(new Font("Arial", Font.BOLD, 14));
        btnDangKy.setContentAreaFilled(false);
        btnDangKy.setBorder(BorderFactory.createEmptyBorder());
        btnDangKy.setFocusPainted(false);

        btnDanhSach = createGradientButton("Danh sách", new Color(44, 44, 80), new Color(33, 33, 61));
        btnDanhSach.setBounds(390, 20, 150, 40);
        btnDanhSach.setForeground(Color.WHITE);
        btnDanhSach.setFont(new Font("Arial", Font.BOLD, 14));
        btnDanhSach.setContentAreaFilled(false);
        btnDanhSach.setBorder(BorderFactory.createEmptyBorder());
        btnDanhSach.setFocusPainted(false);

        mainPanel.add(btnDangKy);
        mainPanel.add(btnDanhSach);

        // Form đăng ký
        formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBounds(210, 80, 750, 540);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        mainPanel.add(formPanel);

        // Tiêu đề và các trường nhập
        JLabel lblTitle = new JLabel("Đăng ký thành viên");
        lblTitle.setBounds(50, 20, 200, 30);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        formPanel.add(lblTitle);

        JLabel lblHelpIcon = new JLabel(new ImageIcon("")); // Để trống đường dẫn icon
        lblHelpIcon.setBounds(260, 20, 30, 30);
        formPanel.add(lblHelpIcon);

        JLabel lblHelpText = new JLabel("Trợ giúp");
        lblHelpText.setBounds(300, 20, 80, 30);
        lblHelpText.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(lblHelpText);

        JLabel lblFeedbackIcon = new JLabel(new ImageIcon("")); // Để trống đường dẫn icon
        lblFeedbackIcon.setBounds(390, 20, 30, 30);
        formPanel.add(lblFeedbackIcon);

        JLabel lblFeedbackText = new JLabel("Góp ý");
        lblFeedbackText.setBounds(430, 20, 80, 30);
        lblFeedbackText.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(lblFeedbackText);

        txtMaTV = new RoundTextField("Mã thành viên");
        txtTenTV = new RoundTextField("Tên thành viên");
        txtNgayDK = new RoundTextField("Ngày đăng ký");
        txtSoDT = new RoundTextField("Số điện thoại");

        txtMaTV.setBounds(50, 80, 550, 40);
        txtTenTV.setBounds(50, 140, 550, 40);
        txtNgayDK.setBounds(50, 260, 250, 40);
        txtSoDT.setBounds(350, 260, 250, 40);

        // Thêm FocusListener để ẩn chữ gợi ý khi click vào ô điền
        FocusListener focusListener = new FocusListener() {
            public void focusGained(FocusEvent e) {
                JTextField textField = (JTextField) e.getSource();
                if (textField.getText().equals("Mã thành viên") || textField.getText().equals("Tên thành viên")
                        || textField.getText().equals("Ngày đăng ký") || textField.getText().equals("Số điện thoại")) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField textField = (JTextField) e.getSource();
                if (textField.getText().isEmpty()) {
                    if (textField == txtMaTV) {
                        textField.setText("Mã thành viên");
                    } else if (textField == txtTenTV) {
                        textField.setText("Tên thành viên");
                    } else if (textField == txtNgayDK) {
                        textField.setText("Ngày đăng ký");
                    } else if (textField == txtSoDT) {
                        textField.setText("Số điện thoại");
                    }
                    textField.setForeground(Color.GRAY);
                }
            }
        };

        txtMaTV.addFocusListener(focusListener);
        txtTenTV.addFocusListener(focusListener);
        txtNgayDK.addFocusListener(focusListener);
        txtSoDT.addFocusListener(focusListener);

        // Giới tính
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setBounds(50, 200, 100, 30);
        lblGioiTinh.setFont(new Font("Arial", Font.BOLD, 14));

        JRadioButton rbNam = new JRadioButton("Nam");
        JRadioButton rbNu = new JRadioButton("Nữ");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(rbNam);
        genderGroup.add(rbNu);
        rbNam.setBounds(150, 200, 70, 30);
        rbNu.setBounds(230, 200, 70, 30);

        // Gói tập
        JLabel lblGoiTap = new JLabel("Gói tập:");
        lblGoiTap.setBounds(350, 200, 100, 30);
        lblGoiTap.setFont(new Font("Arial", Font.BOLD, 14));

        String[] goiTap = {"Chọn gói tập", "1 tháng", "3 tháng", "6 tháng"};
        cboGoiTap = new JComboBox<>(goiTap);
        cboGoiTap.setBounds(420, 200, 180, 35);
        cboGoiTap.setSelectedIndex(0);

        // Ghi chú
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setBounds(50, 320, 100, 30);
        lblGhiChu.setFont(new Font("Arial", Font.BOLD, 14));

        JTextArea txtGhiChu = new JTextArea();
        txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        scrollGhiChu.setBounds(50, 350, 550, 80);
        scrollGhiChu.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true));

        // Nút Lưu có màu gradient và bo góc
        JButton btnLuu = new JButton("Lưu") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 153, 51), getWidth(), getHeight(), new Color(255, 94, 58));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLuu.setBounds(270, 450, 120, 45);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Arial", Font.BOLD, 14));
        btnLuu.setContentAreaFilled(false);
        btnLuu.setBorder(BorderFactory.createEmptyBorder());

        // Thêm các thành phần vào form
        formPanel.add(txtMaTV);
        formPanel.add(txtTenTV);
        formPanel.add(txtNgayDK);
        formPanel.add(txtSoDT);
        formPanel.add(lblGioiTinh);
        formPanel.add(rbNam);
        formPanel.add(rbNu);
        formPanel.add(lblGoiTap);
        formPanel.add(cboGoiTap);
        formPanel.add(lblGhiChu);
        formPanel.add(scrollGhiChu);
        formPanel.add(btnLuu);

        // Panel danh sách
        listPanel = new JPanel();
        listPanel.setLayout(null);
        listPanel.setBounds(210, 80, 750, 540);
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        listPanel.setVisible(false);
        mainPanel.add(listPanel);

        // Tiêu đề và các icon trên form danh sách
        JLabel lblListTitle = new JLabel("Danh sách thành viên");
        lblListTitle.setBounds(50, 20, 200, 30);
        lblListTitle.setFont(new Font("Arial", Font.BOLD, 20));
        listPanel.add(lblListTitle);

        JLabel lblListHelpIcon = new JLabel(new ImageIcon("")); // Để trống đường dẫn icon
        lblListHelpIcon.setBounds(260, 20, 30, 30);
        listPanel.add(lblListHelpIcon);

        JLabel lblListHelpText = new JLabel("Trợ giúp");
        lblListHelpText.setBounds(300, 20, 80, 30);
        lblListHelpText.setFont(new Font("Arial", Font.PLAIN, 14));
        listPanel.add(lblListHelpText);

        JLabel lblListFeedbackIcon = new JLabel(new ImageIcon("")); // Để trống đường dẫn icon
        lblListFeedbackIcon.setBounds(390, 20, 30, 30);
        listPanel.add(lblListFeedbackIcon);

        JLabel lblListFeedbackText = new JLabel("Góp ý");
        lblListFeedbackText.setBounds(430, 20, 80, 30);
        lblListFeedbackText.setFont(new Font("Arial", Font.PLAIN, 14));
        listPanel.add(lblListFeedbackText);

        // Bảng danh sách
        String[] columnNames = {"ID", "Tên thành viên", "Ngày đăng ký", "Số điện thoại", "Gói tập", "Ngày kết thúc", "Trạng thái"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 120, 650, 350);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        listPanel.add(scrollPane);

        // Tạo panel chứa thanh tìm kiếm, nút bộ lọc và nút xem theo ngày tháng
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(50, 70, 650, 40);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setLayout(new GridLayout(1, 3, 10, 10));

        // Thanh tìm kiếm bo tròn
        RoundTextField txtTimKiem = new RoundTextField("Tìm kiếm");
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimKiem.setBackground(new Color(240, 240, 240));
        txtTimKiem.setBounds(50, 70, 450, 40); // Điều chỉnh kích thước (dài hơn)
        searchPanel.add(txtTimKiem);

        // Nút bộ lọc
        JButton btnLoc = new JButton("Bộ lọc") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // chỗ chỉnh màu nút
                GradientPaint gradient = new GradientPaint(0, 0, new Color(44, 44, 80), getWidth(), getHeight(), new Color(33, 33, 61));

                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g); // Quan trọng: Gọi super để vẽ chữ và icon
            }
        };
        btnLoc.setFont(new Font("Arial", Font.BOLD, 14));
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setFocusPainted(false);
        btnLoc.setContentAreaFilled(false); // Tắt nền mặc định
        btnLoc.setOpaque(false); // Đảm bảo nút trong suốt
        btnLoc.setBorderPainted(false); // Tắt viền mặc định
        btnLoc.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Đặt padding
        btnLoc.setBounds(510, 70, 120, 40); // Điều chỉnh kích thước (ngắn hơn)
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

                GradientPaint gradient = new GradientPaint(0, 0, new Color(44, 44, 80), getWidth(), getHeight(), new Color(33, 33, 61));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g); // Quan trọng: Gọi super để vẽ chữ và icon
            }
        };
        btnXemNgay.setFont(new Font("Arial", Font.BOLD, 14));
        btnXemNgay.setForeground(Color.WHITE);
        btnXemNgay.setFocusPainted(false);
        btnXemNgay.setContentAreaFilled(false); // Tắt nền mặc định
        btnXemNgay.setOpaque(false); // Đảm bảo nút trong suốt
        btnXemNgay.setBorderPainted(false); // Tắt viền mặc định
        btnXemNgay.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Đặt padding
        btnXemNgay.setBounds(640, 70, 120, 40); // Điều chỉnh kích thước (ngắn hơn)
        btnXemNgay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dateDialog = new JDialog((Dialog) SwingUtilities.getWindowAncestor(btnXemNgay), "Chọn khoảng thời gian", true);
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

        // Thêm searchPanel vào listPanel
        listPanel.add(searchPanel);

        // Xử lý sự kiện chuyển đổi giữa Đăng ký và Danh sách
        btnDangKy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formPanel.setVisible(true);
                listPanel.setVisible(false);
                updateButtonColors(btnDangKy, btnDanhSach);
            }
        });

        btnDanhSach.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formPanel.setVisible(false);
                listPanel.setVisible(true);
                updateButtonColors(btnDanhSach, btnDangKy);
            }
        });
    }

    // Tính ngày kết thúc dựa trên gói tập
    private String tinhNgayKetThuc(String ngayDK, String goiTap) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(ngayDK);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            switch (goiTap) {
                case "1 tháng":
                    calendar.add(Calendar.MONTH, 1);
                    break;
                case "3 tháng":
                    calendar.add(Calendar.MONTH, 3);
                    break;
                case "6 tháng":
                    calendar.add(Calendar.MONTH, 6);
                    break;
            }

            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // Kiểm tra trạng thái gói tập
    private String kiemTraTrangThai(String ngayKetThuc) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date ngayKT = sdf.parse(ngayKetThuc);
            Date ngayHienTai = new Date();

            if (ngayKT.after(ngayHienTai)) {
                return "Còn hạn";
            } else {
                return "Hết hạn";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // Tạo nút gradient
    private JButton createGradientButton(String text, Color startColor, Color endColor) {
        return new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
    }

    // Cập nhật màu nút
    private void updateButtonColors(JButton activeButton, JButton inactiveButton) {
        activeButton.setForeground(Color.WHITE);
        inactiveButton.setForeground(Color.WHITE);
        activeButton.repaint();
        inactiveButton.repaint();
    }



    // Lớp RoundTextField để tạo trường nhập bo tròn
    class RoundTextField extends JTextField {

        public RoundTextField(String text) {
            super(text);
            setOpaque(false);
            setFont(new Font("Arial", Font.PLAIN, 14));
            setForeground(Color.GRAY);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setColor(new Color(180, 180, 180));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }

}
