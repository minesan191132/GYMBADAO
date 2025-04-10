package ui;

import dao.KhachHangDao;
import entity.GoiTap;
import entity.KhachHangViewModel;
import entity.ThanhVien;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.border.EmptyBorder;

public class KhachHang extends JPanel {

    private JPanel formPanel;
    private JPanel listPanel;
    private JButton btnDangKy;
    private JButton btnDanhSach;
    private DefaultTableModel tableModel;
    private JTextField txtMaTV, txtTenTV, txtNgayDK, txtSoDT, txtTimKiem;
    private JRadioButton rbNam, rbNu;
    private JLabel lblGhiChu;
    private JComboBox<GoiTap> cboGoiTap;
    private ButtonGroup genderGroup;
    private KhachHangDao khachHangDAO = new KhachHangDao();
    private boolean isEditing = false; // Cờ kiểm tra trạng thái chỉnh sửa
    private int currentMaKH = -1; // Lưu mã KH đang chỉnh sửa

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

        JLabel lblHelpIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/question-sign.png"));
        lblHelpIcon.setBounds(270, 20, 30, 30);
        formPanel.add(lblHelpIcon);

        JLabel lblHelpText = new JLabel("Trợ giúp");
        lblHelpText.setBounds(305, 20, 80, 30);
        lblHelpText.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(lblHelpText);

        JLabel lblFeedbackIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/heart.png"));
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

        txtMaTV.setEditable(false);  // Không cho phép chỉnh sửa
        txtMaTV.setBackground(new Color(128, 128, 128));  // Đổi màu nền để thể hiện là ô bị khóa

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

        rbNam = new JRadioButton("Nam");
        rbNu = new JRadioButton("Nữ");
        genderGroup = new ButtonGroup();
        genderGroup.add(rbNam);
        genderGroup.add(rbNu);
        rbNam.setBounds(150, 200, 70, 30);
        rbNu.setBounds(230, 200, 70, 30);
        rbNam.setBackground(Color.WHITE);
        rbNu.setBackground(Color.WHITE);

        // Gói tập
        JLabel lblGoiTap = new JLabel("Gói tập:");
        lblGoiTap.setBounds(350, 200, 100, 30);
        lblGoiTap.setFont(new Font("Arial", Font.BOLD, 14));

        cboGoiTap = new JComboBox<>();
        cboGoiTap.addItem(new GoiTap(0, "Chọn gói tập", 0, 0)); // Item mặc định

        List<GoiTap> listGoiTap = new KhachHangDao().getAllGoiTap();
        for (GoiTap gt : listGoiTap) {
            cboGoiTap.addItem(gt);
        }

// Thiết lập hiển thị tên gói
        cboGoiTap.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof GoiTap) {
                    setText(((GoiTap) value).getTenGoi());
                }
                return this;
            }
        });
        cboGoiTap.setBounds(420, 200, 180, 35);
        cboGoiTap.setSelectedIndex(0);

        // Ghi chú
        lblGhiChu = new JLabel("Ghi chú:");
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

        btnLuu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!validateForm()) {
                    return;
                }

                try {
                    // Lấy thông tin từ form
                    String hoTen = txtTenTV.getText();
                    String gioiTinh = rbNam.isSelected() ? "Nam" : "Nữ";
                    String soDT = txtSoDT.getText();
                    Date ngayDK = new SimpleDateFormat("dd/MM/yyyy").parse(txtNgayDK.getText());
                    GoiTap selectedGoiTap = (GoiTap) cboGoiTap.getSelectedItem();
                    int maGoi = selectedGoiTap.getMaGoi();

                    // Tạo đối tượng ThanhVien
                    ThanhVien tv = new ThanhVien();
                    tv.setHoTen(hoTen);
                    tv.setGioiTinh(gioiTinh);
                    tv.setSoDT(soDT);
                    tv.setNgayDK(ngayDK);
                    tv.setMaGoi(maGoi);

                    // Không cần set NgayKT vì trigger sẽ tự tính
                    if (isEditing) {
                        // Cập nhật thành viên
                        tv.setMaTV(currentMaKH);
                        khachHangDAO.update(tv);
                        JOptionPane.showMessageDialog(KhachHang.this,
                                "Cập nhật thành viên thành công!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Thêm thành viên mới
                        khachHangDAO.insert(tv);
                        JOptionPane.showMessageDialog(KhachHang.this,
                                "Thêm thành viên mới thành công!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    if (listPanel.isVisible()) {
                        loadDataToTable();
                    }
                    // Làm mới form sau khi lưu
                    clearForm();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(KhachHang.this,
                            "Lỗi khi lưu dữ liệu: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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
        lblListTitle.setBounds(50, 20, 250, 30);
        lblListTitle.setFont(new Font("Arial", Font.BOLD, 20));
        listPanel.add(lblListTitle);

        JLabel lblListHelpIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/question-sign.png"));
        lblListHelpIcon.setBounds(270, 20, 30, 30);
        listPanel.add(lblListHelpIcon);

        JLabel lblListHelpText = new JLabel("Trợ giúp");
        lblListHelpText.setBounds(305, 20, 80, 30);
        lblListHelpText.setFont(new Font("Arial", Font.PLAIN, 14));
        listPanel.add(lblListHelpText);

        JLabel lblListFeedbackIcon = new JLabel(new ImageIcon("/GYMBADAO/src/icon/heart.png"));
        lblListFeedbackIcon.setBounds(390, 20, 30, 30);
        listPanel.add(lblListFeedbackIcon);

        JLabel lblListFeedbackText = new JLabel("Góp ý");
        lblListFeedbackText.setBounds(430, 20, 80, 30);
        lblListFeedbackText.setFont(new Font("Arial", Font.PLAIN, 14));
        listPanel.add(lblListFeedbackText);

        // Bảng danh sách
        String[] columnNames = {"ID", "Tên thành viên", "Ngày đăng ký", "Số điện thoại", "Gói tập", "Ngày kết thúc", "Trạng thái"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Ngăn chặn việc chỉnh sửa ô
            }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 120, 650, 350);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        listPanel.add(scrollPane);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String hoTen = table.getValueAt(row, 1).toString();
                        loadDataFromTableToForm(hoTen);
                        formPanel.setVisible(true);
                        listPanel.setVisible(false);
                        updateButtonColors(btnDangKy, btnDanhSach);
                    }
                }
            }
        });

        // Tạo panel chứa thanh tìm kiếm, nút bộ lọc và nút xem theo ngày tháng
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(50, 70, 650, 40);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setLayout(new GridLayout(1, 3, 10, 10));

        // Thanh tìm kiếm bo tròn
        txtTimKiem = new RoundTextField("Tên Thành Viên");
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setBackground(new Color(240, 240, 240));
        txtTimKiem.setBounds(50, 70, 450, 40); // Điều chỉnh kích thước (dài hơn)
        searchPanel.add(txtTimKiem);

// Popup menu để hiển thị gợi ý
        JPopupMenu suggestionPopup = new JPopupMenu();
        JList<String> suggestionList = new JList<>();
        suggestionList.setFont(new Font("Arial", Font.PLAIN, 14));
        suggestionPopup.add(new JScrollPane(suggestionList));

// Thêm FocusListener
        txtTimKiem.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtTimKiem.getText().equals("Tên Thành Viên")) {
                    txtTimKiem.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtTimKiem.getText().isEmpty()) {
                    txtTimKiem.setText("Tên Thành Viên");
                }
                suggestionPopup.setVisible(false); // Ẩn gợi ý khi mất focus
            }
        });

// Thêm KeyListener để xử lý gợi ý và tìm kiếm trực tiếp
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = txtTimKiem.getText().trim();
                if (input.isEmpty() || input.equals("Tên Thành Viên")) {
                    suggestionPopup.setVisible(false);
                    loadDataToTable(); // Hiển thị lại toàn bộ dữ liệu nếu không có input
                    return;
                }

                // Lấy danh sách gợi ý từ DAO
                List<String> suggestions = getSuggestions(input);
                if (suggestions.isEmpty()) {
                    suggestionPopup.setVisible(false);
                } else {
                    // Cập nhật danh sách gợi ý
                    suggestionList.setListData(suggestions.toArray(new String[0]));
                    suggestionPopup.setPopupSize(txtTimKiem.getWidth(), Math.min(suggestions.size() * 30, 150));
                    suggestionPopup.show(txtTimKiem, 0, txtTimKiem.getHeight());
                }

                // Tìm kiếm trực tiếp khi người dùng nhập, nhưng không hiển thị thông báo
                searchByName(input, false); // Thêm tham số để kiểm soát việc hiển thị thông báo
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Khi nhấn Enter
                    String input = txtTimKiem.getText().trim();
                    if (!input.isEmpty() && !input.equals("Tên Thành Viên")) {
                        searchByName(input, true); // Gọi tìm kiếm và hiển thị thông báo nếu không tìm thấy
                    }
                }
            }
        });
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selectedName = suggestionList.getSelectedValue();
                    if (selectedName != null) {
                        txtTimKiem.setText(selectedName);
                        suggestionPopup.setVisible(false);
                        // Tự động tìm kiếm và hiển thị kết quả trong bảng, có hiển thị thông báo
                        searchByName(selectedName, true);
                    }
                }
            }
        });
// Nút bộ lọc
        JButton btnHoaDon = new JButton("Xuất hóa đơn") {
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
        btnHoaDon.setFont(new Font("Arial", Font.BOLD, 14));
        btnHoaDon.setForeground(Color.WHITE);
        btnHoaDon.setFocusPainted(false);
        btnHoaDon.setContentAreaFilled(false); // Tắt nền mặc định
        btnHoaDon.setOpaque(false); // Đảm bảo nút trong suốt
        btnHoaDon.setBorderPainted(false); // Tắt viền mặc định
        btnHoaDon.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Đặt padding
        btnHoaDon.setBounds(510, 70, 120, 40); // Điều chỉnh kích thước (ngắn hơn)
        btnHoaDon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Chức năng Xuất hóa đơn đang được phát triển!");
            }
        });
        searchPanel.add(btnHoaDon);
        // Nút xem theo ngày
        JButton btnXemNgay = new JButton("Xem theo ngày") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(44, 44, 80),
                        getWidth(), getHeight(), new Color(33, 33, 61));
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
        btnXemNgay.setBounds(640, 70, 120, 40);
        btnXemNgay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tạo dialog với parent frame và modal=true
                JDialog dateDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(btnXemNgay), "Chọn khoảng thời gian", true);
                dateDialog.setTitle("Chọn khoảng thời gian");
                dateDialog.setSize(400, 220);
                dateDialog.setLayout(new BorderLayout(10, 10));
                dateDialog.setLocationRelativeTo(null);

                // Panel chính chứa các trường nhập
                JPanel inputPanel = new JPanel(new GridLayout(2, 2, 15, 15));
                inputPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

                // Ngày bắt đầu
                JLabel lblNgayBatDau = new JLabel("Ngày bắt đầu:");
                lblNgayBatDau.setFont(new Font("Arial", Font.BOLD, 14));
                inputPanel.add(lblNgayBatDau);

                JSpinner spnNgayBatDau = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor editorNgayBatDau = new JSpinner.DateEditor(spnNgayBatDau, "dd/MM/yyyy");
                spnNgayBatDau.setEditor(editorNgayBatDau);
                editorNgayBatDau.getTextField().setColumns(10);
                editorNgayBatDau.getTextField().setFont(new Font("Arial", Font.PLAIN, 14));
                inputPanel.add(spnNgayBatDau);

                // Ngày kết thúc
                JLabel lblNgayKetThuc = new JLabel("Ngày kết thúc:");
                lblNgayKetThuc.setFont(new Font("Arial", Font.BOLD, 14));
                inputPanel.add(lblNgayKetThuc);

                JSpinner spnNgayKetThuc = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor editorNgayKetThuc = new JSpinner.DateEditor(spnNgayKetThuc, "dd/MM/yyyy");
                spnNgayKetThuc.setEditor(editorNgayKetThuc);
                editorNgayKetThuc.getTextField().setColumns(10);
                editorNgayKetThuc.getTextField().setFont(new Font("Arial", Font.PLAIN, 14));
                inputPanel.add(spnNgayKetThuc);

                // Panel chứa các nút
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                buttonPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

                // Nút Xác nhận với góc bo tròn
                JButton btnXacNhan = new JButton("Xác nhận") {
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
                        // Không vẽ viền mặc định
                    }
                };
                btnXacNhan.setBackground(new Color(76, 175, 80));
                btnXacNhan.setForeground(Color.WHITE);
                btnXacNhan.setFont(new Font("Arial", Font.BOLD, 14));
                btnXacNhan.setPreferredSize(new Dimension(120, 35));
                btnXacNhan.setContentAreaFilled(false);
                btnXacNhan.setBorderPainted(false);
                btnXacNhan.setFocusPainted(false);
                btnXacNhan.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Date ngayBatDau = (Date) spnNgayBatDau.getValue();
                        Date ngayKetThuc = (Date) spnNgayKetThuc.getValue();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String ngayBD = sdf.format(ngayBatDau);
                        String ngayKT = sdf.format(ngayKetThuc);

                        JOptionPane.showMessageDialog(
                                dateDialog,
                                "Đã chọn khoảng thời gian:\nTừ: " + ngayBD + "\nĐến: " + ngayKT,
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        dateDialog.dispose();
                    }
                });

                // Nút Hủy với góc bo tròn
                JButton btnHuy = new JButton("Hủy") {
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
                        // Không vẽ viền mặc định
                    }
                };
                btnHuy.setBackground(new Color(244, 67, 54));
                btnHuy.setForeground(Color.WHITE);
                btnHuy.setFont(new Font("Arial", Font.BOLD, 14));
                btnHuy.setPreferredSize(new Dimension(120, 35));
                btnHuy.setContentAreaFilled(false);
                btnHuy.setBorderPainted(false);
                btnHuy.setFocusPainted(false);
                btnHuy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dateDialog.dispose();
                    }
                });

                buttonPanel.add(btnHuy);
                buttonPanel.add(btnXacNhan);

                dateDialog.add(inputPanel, BorderLayout.CENTER);
                dateDialog.add(buttonPanel, BorderLayout.SOUTH);
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
                loadDataToTable(); // Load dữ liệu khi chuyển sang tab Danh sách
            }
        });
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

    private void loadDataToTable() {
        // Xóa dữ liệu cũ trong bảng
        tableModel.setRowCount(0);

        // Lấy dữ liệu từ database
        List<KhachHangViewModel> list = khachHangDAO.getAllForDisplay();

        // Định dạng ngày
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Thêm dữ liệu vào bảng
        for (KhachHangViewModel kh : list) {
            String ngayDK = kh.getNgayDangKy() != null ? sdf.format(kh.getNgayDangKy()) : "";
            String ngayKT = kh.getNgayKetThuc() != null ? sdf.format(kh.getNgayKetThuc()) : "";
            String trangThai = kh.getNgayKetThuc() != null
                    ? (kh.getNgayKetThuc().after(new Date()) ? "Còn hạn" : "Hết hạn") : "";

            tableModel.addRow(new Object[]{
                kh.getMaKH(),
                kh.getHoTen(),
                ngayDK,
                kh.getSoDienThoai(),
                kh.getTenGoi(),
                ngayKT,
                trangThai
            });
        }
    }

    private void loadDataFromTableToForm(String hoTen) {
        ThanhVien kh = khachHangDAO.selectByHoTen(hoTen); // Sửa lại DAO cho đúng
        if (kh != null) {
            currentMaKH = kh.getMaTV();
            isEditing = true;

            txtMaTV.setText(String.valueOf(kh.getMaTV()));
            txtTenTV.setText(kh.getHoTen());
            txtSoDT.setText(kh.getSoDT());

            if (kh.getGioiTinh().equalsIgnoreCase("Nam")) {
                rbNam.setSelected(true);
            } else {
                rbNu.setSelected(true);
            }

            GoiTap gt = khachHangDAO.getGoiTapById(kh.getMaGoi());
            if (gt != null) {
                cboGoiTap.setSelectedItem(gt);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            txtNgayDK.setText(sdf.format(kh.getNgayDK()));
        }
    }

    // Thêm phương thức clearForm
    private void clearForm() {
        txtMaTV.setText("Mã thành viên");
        txtTenTV.setText("Tên thành viên");
        txtSoDT.setText("Số điện thoại");
        txtNgayDK.setText("Ngày đăng ký");
        genderGroup.clearSelection();
        cboGoiTap.setSelectedIndex(0);
        isEditing = false;
        currentMaKH = -1;
        txtMaTV.setForeground(Color.GRAY);
        txtTenTV.setForeground(Color.GRAY);
        txtSoDT.setForeground(Color.GRAY);
        txtNgayDK.setForeground(Color.GRAY);
    }

    private boolean validateForm() {
        if (txtTenTV.getText().isEmpty() || txtTenTV.getText().equals("Tên thành viên")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên thành viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenTV.requestFocus();
            return false;
        }

        if (!rbNam.isSelected() && !rbNu.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (cboGoiTap.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn gói tập", "Lỗi", JOptionPane.ERROR_MESSAGE);
            cboGoiTap.requestFocus();
            return false;
        }

        try {
            if (txtNgayDK.getText().isEmpty() || txtNgayDK.getText().equals("Ngày đăng ký")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày đăng ký", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtNgayDK.requestFocus();
                return false;
            }
            new SimpleDateFormat("dd/MM/yyyy").parse(txtNgayDK.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày đăng ký không hợp lệ (dd/MM/yyyy)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtNgayDK.requestFocus();
            return false;
        }

        return true;
    }

    private List<String> getSuggestions(String input) {
        List<String> suggestions = new ArrayList<>();
        List<ThanhVien> members = khachHangDAO.selectAll(); // Lấy tất cả thành viên
        //System.out.println("Total members: " + members.size());
        for (ThanhVien tv : members) {
            //System.out.println("Member name: " + tv.getHoTen());
            if (tv.getHoTen().toLowerCase().startsWith(input.toLowerCase())) {
                suggestions.add(tv.getHoTen());
            }
        }
        //System.out.println("Suggestions: " + suggestions);
        return suggestions;
    }

    private void searchByName(String name, boolean showMessage) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng
        List<KhachHangViewModel> list = khachHangDAO.getAllForDisplay();
        //System.out.println("Total records for display: " + list.size());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        int count = 0; // Đếm số kết quả tìm thấy
        for (KhachHangViewModel kh : list) {
            String hoTen = kh.getHoTen().toLowerCase().trim();
            String searchName = name.toLowerCase().trim();
            //System.out.println("Checking: " + hoTen + " against: " + searchName);
            if (hoTen.contains(searchName)) {
                String ngayDK = kh.getNgayDangKy() != null ? sdf.format(kh.getNgayDangKy()) : "";
                String ngayKT = kh.getNgayKetThuc() != null ? sdf.format(kh.getNgayKetThuc()) : "";
                String trangThai = kh.getNgayKetThuc() != null
                        ? (kh.getNgayKetThuc().after(new Date()) ? "Còn hạn" : "Hết hạn") : "";

                tableModel.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHoTen(),
                    ngayDK,
                    kh.getSoDienThoai(),
                    kh.getTenGoi(),
                    ngayKT,
                    trangThai
                });
                count++;
            }
        }

        // Chỉ hiển thị thông báo nếu showMessage là true (khi nhấn Enter)
        if (count == 0 && showMessage) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thành viên nào với tên: " + name, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
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
