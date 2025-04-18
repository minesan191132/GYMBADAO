package ui;

import dao.DonHangDAO;
import entity.DonHang;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DonHangUI extends JPanel {

    DonHangDAO donHangDAO = new DonHangDAO();
    private DefaultTableModel tableModel;
    private JTextField txtTimKiem;

    public DonHangUI() {
        setBounds(0, 0, 800, 650);
        setLayout(null);

        // Main content panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(239, 236, 236)); // Xám nhẹ như hình
        formPanel.setBounds(-5, 0, 800, 650);
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
        searchPanel.setBackground(new Color(239, 236, 236));
        searchPanel.setLayout(new GridLayout(1, 3, 10, 10));

        // Thanh tìm kiếm bo tròn và có thể nhập liệu
        txtTimKiem = new RoundTextField("Tìm kiếm");
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setBackground(Color.WHITE);
        txtTimKiem.setBounds(50, 70, 500, 40);
        searchPanel.add(txtTimKiem);

        JPopupMenu suggestionPopup = new JPopupMenu();
        JList<String> suggestionList = new JList<>();
        suggestionList.setFont(new Font("Arial", Font.PLAIN, 14));
        suggestionPopup.add(new JScrollPane(suggestionList));

        suggestionPopup.setFocusable(false);
        suggestionList.setRequestFocusEnabled(false);

// Xử lý focus
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
                suggestionPopup.setVisible(false);
            }
        });

// Xử lý sự kiện bàn phím
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Bỏ qua các phím điều hướng trong popup
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (suggestionPopup.isVisible()) {
                        suggestionList.requestFocusInWindow();
                        return;
                    }
                }

                String input = txtTimKiem.getText().trim();
                if (input.isEmpty() || input.equals("Tìm kiếm")) {
                    suggestionPopup.setVisible(false);
                    loadDataToTable(tableModel);
                    return;
                }

                List<String> suggestions = getSuggestions(input);
                if (suggestions.isEmpty()) {
                    suggestionPopup.setVisible(false);
                } else {
                    suggestionList.setListData(suggestions.toArray(new String[0]));
                    suggestionPopup.setPopupSize(txtTimKiem.getWidth(), Math.min(suggestions.size() * 30, 150));
                    suggestionPopup.show(txtTimKiem, 0, txtTimKiem.getHeight());
                }

                searchByName(input, false);
            }
        });

// Xử lý khi chọn item từ danh sách gợi ý
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selected = suggestionList.getSelectedValue();
                    if (selected != null) {
                        txtTimKiem.setText(selected);
                        suggestionPopup.setVisible(false);
                        txtTimKiem.requestFocus();
                        searchByName(selected, true);
                    }
                }
            }
        });

// Cho phép điều hướng bằng bàn phím trong popup
        suggestionList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String selected = suggestionList.getSelectedValue();
                    if (selected != null) {
                        txtTimKiem.setText(selected);
                        suggestionPopup.setVisible(false);
                        txtTimKiem.requestFocus();
                        searchByName(selected, true);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    suggestionPopup.setVisible(false);
                    txtTimKiem.requestFocus();
                }
            }
        });
        // Nút bộ lọc
        JButton btnLoc = new JButton("Bộ lọc") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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
        btnLoc.setPreferredSize(new java.awt.Dimension(100, 40));
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
        btnXemNgay.setBorderPainted(false);
        btnXemNgay.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnXemNgay.setPreferredSize(new Dimension(120, 40));

        btnXemNgay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dateDialog = new JDialog();
                dateDialog.setTitle("Chọn khoảng thời gian");
                dateDialog.setSize(450, 250);
                dateDialog.setLayout(new BorderLayout());
                dateDialog.setLocationRelativeTo(null);

                JPanel mainPanel = new JPanel(new GridBagLayout());
                mainPanel.setBorder(new EmptyBorder(30, 40, 20, 40));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(15, 15, 15, 15);
                gbc.anchor = GridBagConstraints.WEST;

                // Ngày bắt đầu
                gbc.gridx = 0;
                gbc.gridy = 0;
                JLabel lblNgayBatDau = new JLabel("Ngày bắt đầu:");
                lblNgayBatDau.setFont(new Font("Arial", Font.BOLD, 14));
                mainPanel.add(lblNgayBatDau, gbc);

                gbc.gridx = 1;
                JSpinner spnNgayBatDau = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor editorNgayBatDau = new JSpinner.DateEditor(spnNgayBatDau, "dd/MM/yyyy");
                spnNgayBatDau.setEditor(editorNgayBatDau);
                editorNgayBatDau.getTextField().setColumns(12);
                editorNgayBatDau.getTextField().setFont(new Font("Arial", Font.PLAIN, 14));
                editorNgayBatDau.getTextField().setPreferredSize(new Dimension(180, 28));
                mainPanel.add(spnNgayBatDau, gbc);

                // Ngày kết thúc
                gbc.gridx = 0;
                gbc.gridy = 1;
                JLabel lblNgayKetThuc = new JLabel("Ngày kết thúc:");
                lblNgayKetThuc.setFont(new Font("Arial", Font.BOLD, 14));
                mainPanel.add(lblNgayKetThuc, gbc);

                gbc.gridx = 1;
//
                JSpinner spnNgayKetThuc = new JSpinner(new SpinnerDateModel());
                JSpinner.DateEditor editorNgayKetThuc = new JSpinner.DateEditor(spnNgayKetThuc, "dd/MM/yyyy");
                spnNgayKetThuc.setEditor(editorNgayKetThuc);
                editorNgayKetThuc.getTextField().setColumns(12);
                editorNgayKetThuc.getTextField().setFont(new Font("Arial", Font.PLAIN, 14));
                editorNgayKetThuc.getTextField().setPreferredSize(new Dimension(180, 28));
                mainPanel.add(spnNgayKetThuc, gbc);

                // Panel nút
                JPanel buttonPanel = new JPanel();
                buttonPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

                // Nút Xác nhận
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
                };
                btnXacNhan.setBackground(new Color(76, 175, 80));
                btnXacNhan.setForeground(Color.WHITE);
                btnXacNhan.setFont(new Font("Arial", Font.BOLD, 14));
                btnXacNhan.setPreferredSize(new Dimension(120, 40));
                btnXacNhan.setOpaque(false);
                btnXacNhan.setBorderPainted(false);
                btnXacNhan.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Date ngayBatDau = (Date) spnNgayBatDau.getValue();
                        Date ngayKetThuc = (Date) spnNgayKetThuc.getValue();

                        java.sql.Date sqlNgayBatDau = new java.sql.Date(ngayBatDau.getTime());
                        java.sql.Date sqlNgayKetThuc = new java.sql.Date(ngayKetThuc.getTime());

                        // Kiểm tra ngày hợp lệ
                        if (ngayBatDau.after(ngayKetThuc)) {
                            JOptionPane.showMessageDialog(dateDialog,
                                    "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc",
                                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Lấy dữ liệu từ DAO
                        List<DonHang> donHangList = donHangDAO.getDonHangByDateRange(sqlNgayBatDau, sqlNgayKetThuc);

                        // Cập nhật bảng
                        tableModel.setRowCount(0);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String ngayBD = sdf.format(ngayBatDau);
                        String ngayKT = sdf.format(ngayKetThuc);
                        JOptionPane.showMessageDialog(
                                dateDialog,
                                "Bạn đã chọn khoảng thời gian:\nTừ: " + ngayBD + "\nĐến: " + ngayKT,
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        dateDialog.dispose();
                    }
                });

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
                };
                btnHuy.setBackground(new Color(244, 67, 54));
                btnHuy.setForeground(Color.WHITE);
                btnHuy.setFont(new Font("Arial", Font.BOLD, 14));
                btnHuy.setPreferredSize(new Dimension(120, 40));
                btnHuy.setOpaque(false);
                btnHuy.setBorderPainted(false);
                btnHuy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dateDialog.dispose();
                    }
                });

                buttonPanel.add(btnHuy);
                buttonPanel.add(btnXacNhan);

                dateDialog.add(mainPanel, BorderLayout.CENTER);
                dateDialog.add(buttonPanel, BorderLayout.SOUTH);
                dateDialog.setVisible(true);
            }
        });
        searchPanel.add(btnXemNgay);

        // Thêm searchPanel vào formPanel
        formPanel.add(searchPanel);

        // Thêm bảng đơn hàng
        String[] columnNames = {"Mã đơn", "Tên khách hàng", "Ngày tạo", "Khách trả"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa bất kỳ ô nào
            }
        };
        JTable table = new JTable(tableModel);

        // Căn giữa tất cả các ô trong bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // bo góc 30px
                g2.dispose();
                super.paintComponent(g);
            }
        };
        scrollPane.setBounds(50, 130, 700, 450);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setBackground(Color.white); // hoặc đổi màu nếu bạn muốn
        formPanel.add(scrollPane);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
        // Tải dữ liệu từ cơ sở dữ liệu
        loadDataToTable(tableModel);
    }

    // Phương thức tải dữ liệu từ DonHangDAO vào bảng
    private void loadDataToTable(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        List<DonHang> list = donHangDAO.selectAllWithDetails(); // Nhận List<donHang>
        for (DonHang dh : list) {
            model.addRow(new Object[]{
                dh.getMaDH(), // Lấy MaDH
                dh.getHoTen(), // Lấy tên khách hàng
                (dh.getNgayLap() != null)
                ? new SimpleDateFormat("dd/MM/yyyy").format(dh.getNgayLap())
                : "", // Xử lý null cho NgayLap
                dh.getThanhTien()// Lấy số tiền
            });
        }
    }

    private List<String> getSuggestions(String input) {
        List<String> suggestions = new ArrayList<>();
        DonHangDAO donHangDAO = new DonHangDAO(); // Tạo đối tượng DAO
        List<DonHang> members = donHangDAO.selectAllWithDetails(); // Gọi phương thức từ đối tượng

        for (DonHang tv : members) {
            if (tv.getHoTen() != null
                    && tv.getHoTen().toLowerCase().startsWith(input.toLowerCase())) {
                suggestions.add(tv.getHoTen());
            }
        }
        return suggestions;
    }

    private void searchByName(String name, boolean showMessage) {
        // Xóa dữ liệu cũ trong bảng
        tableModel.setRowCount(0);

        DonHangDAO donHangDAO = new DonHangDAO();
        List<DonHang> list = donHangDAO.selectAllWithDetails();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        int count = 0;
        for (DonHang dh : list) {
            String hoTenKhachHang = dh.getHoTen().toLowerCase().trim();
            String searchName = name.toLowerCase().trim();

            // Tìm kiếm theo tên khách hàng
            if (hoTenKhachHang.contains(searchName)) {
                tableModel.addRow(new Object[]{
                    dh.getMaDH(),
                    dh.getHoTen(),
                    (dh.getNgayLap() != null) ? sdf.format(dh.getNgayLap()) : "",
                    String.format("%,.0f VND", dh.getThanhTien()),});
                count++;
            }
        }

        // Hiển thị thông báo nếu không tìm thấy
        if (count == 0 && showMessage) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy đơn hàng nào với tên khách hàng: " + name,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // Lớp RoundTextField
    class RoundTextField extends JTextField {

        public RoundTextField(String placeholder) {
            super(placeholder);
            setOpaque(false); // Quan trọng để không vẽ nền mặc định
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setFont(new Font("Arial", Font.PLAIN, 14));
            setBackground(new Color(240, 240, 240));
            setForeground(Color.GRAY); // màu chữ
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Vẽ nền bo tròn
            Shape clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setColor(getBackground());
            g2.fill(clip);

            // Clip bo góc để đảm bảo nội dung không bị vuông trở lại
            g2.setClip(clip);

            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // Bạn có thể vẽ border nếu muốn ở đây
        }
    }
}
