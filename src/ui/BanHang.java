package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import dao.*;
import java.util.List;
import utils.*;
import entity.*;
import java.net.URL;  // Thêm dòng này cho URL
import java.io.FileNotFoundException;  // Thêm dòng này cho FileNotFoundException
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BanHang extends JFrame {

    // Các component chính
    private JLabel totalValueLabel, discountValueLabel, amountLabel, givenAmountLabel, changeAmountLabel;
    private JComboBox<String> paymentMethodComboBox;
    private JTextArea noteTextArea;
    private CardLayout cardLayout;
    private JPanel containerPanel;
    private BufferedImage dfimg;
    private List<OrderItem> orderItems = new ArrayList<>();
    private ThanhVien selectedCustomer = null;
    private JLabel customerNameLabel;
    private double discount = 0;

    public BanHang() {
        initUI();
    }

    private void initUI() {
        setupMainWindow();
        createHeaderPanel();
        createMainContent();
        setupKeyboardShortcuts();
        setVisible(true);
    }

    private void setupMainWindow() {
        setTitle("Giao diện bán hàng");
        setSize(1720, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 139));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        // Thanh tìm kiếm (căn giữa)
        JPanel searchPanel = createSearchPanel();
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        // Panel nút bên phải (giờ đã bao gồm cả logo và tên KH)
        JPanel rightPanel = createHeaderButtonPanel();
        headerPanel.add(rightPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private JPanel createSearchPanel() {
        JPanel roundedPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setOpaque(false);
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        roundedPanel.setPreferredSize(new Dimension(400, 40));

        JTextField searchField = new JTextField();
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setOpaque(false);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        roundedPanel.add(searchField, BorderLayout.CENTER);

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        searchContainer.setOpaque(false);
        searchContainer.add(roundedPanel);

        return searchContainer;
    }

    private JPanel createHeaderButtonPanel() {
        // Panel chính dùng GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10); // Padding

        // Panel chứa icon và tên KH
        JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        customerPanel.setOpaque(false);

        // Logo home
        ImageIcon homeIcon = loadScaledIcon("/GYMBADAO/src/icon/home.png", 40, 40);
        JLabel iconLabel = new JLabel(homeIcon);
        iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showMainPanel();
            }
        });

        // Label tên khách hàng
        customerNameLabel = new JLabel("Chưa chọn khách hàng");
        customerNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        customerNameLabel.setForeground(Color.WHITE);

        customerPanel.add(iconLabel);
        customerPanel.add(customerNameLabel);

        // Cấu hình vị trí cho customerPanel (căn giữa)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(customerPanel, gbc);

        // Thêm hiệu ứng hover cho label tên KH
        customerNameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedCustomer != null) {
                    customerNameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    customerNameLabel.setForeground(new Color(255, 255, 150)); // Màu vàng nhạt khi hover
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedCustomer != null) {
                    customerNameLabel.setForeground(Color.YELLOW); // Màu vàng khi đã chọn
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedCustomer != null) {
                    // Hiển thị thông tin chi tiết khách hàng khi click
                    showCustomerDetail(selectedCustomer);
                }
            }
        });

        // Nút phím tắt (đẩy sang phải)
        JButton shortcutButton = createRoundedButton("Phím tắt", 150, 40, Color.WHITE, Color.BLACK);
        shortcutButton.addActionListener(e -> showDevelopmentMessage());

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(shortcutButton, gbc);

        // Panel padding bên ngoài
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.setOpaque(false);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 30));
        paddingPanel.add(mainPanel, BorderLayout.CENTER);

        return paddingPanel;
    }

    private void createMainContent() {
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        horizontalSplitPane.setDividerSize(5);
        horizontalSplitPane.setDividerLocation(1000); // Panel trái rộng hơn
        horizontalSplitPane.setBackground(new Color(220, 220, 220));

        // Panel trái - Danh sách sản phẩm và đơn hàng
        JPanel leftPanel = createLeftPanel();

        // Panel phải - Thanh toán
        JPanel rightPanel = createRightPanel();

        horizontalSplitPane.setLeftComponent(leftPanel);
        horizontalSplitPane.setRightComponent(rightPanel);

        add(horizontalSplitPane, BorderLayout.CENTER);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setDividerSize(5);
        verticalSplitPane.setDividerLocation(500);
        verticalSplitPane.setBackground(new Color(220, 220, 220));

        // Panel trên - Đơn hàng
        JPanel topPanel = createOrderPanel();

        // Panel dưới - Danh sách sản phẩm
        JPanel bottomPanel = createProductListPanel();

        verticalSplitPane.setTopComponent(topPanel);
        verticalSplitPane.setBottomComponent(bottomPanel);

        leftPanel.add(verticalSplitPane, BorderLayout.CENTER);
        return leftPanel;
    }

    private JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBackground(Color.WHITE);

        // Sử dụng CardLayout để chuyển đổi giữa trạng thái có đơn hàng và không có đơn hàng
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // Panel khi không có đơn hàng
        JPanel emptyOrderPanel = createEmptyOrderPanel();
        containerPanel.add(emptyOrderPanel, "EMPTY");

        // Panel khi có đơn hàng
        JScrollPane orderScrollPane = createOrderScrollPane();
        containerPanel.add(orderScrollPane, "ITEMS");

        cardLayout.show(containerPanel, "EMPTY");
        orderPanel.add(containerPanel, BorderLayout.CENTER);

        return orderPanel;
    }

    private JPanel createEmptyOrderPanel() {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setBackground(Color.WHITE);

        ImageIcon boxIcon = loadScaledIcon("/GYMBADAO/src/icon/box.png", 120, 120);
        JLabel iconLabel = new JLabel(boxIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel("Đơn hàng của bạn chưa có sản phẩm nào");
        textLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emptyPanel.add(Box.createVerticalGlue());
        emptyPanel.add(iconLabel);
        emptyPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        emptyPanel.add(textLabel);
        emptyPanel.add(Box.createVerticalGlue());

        return emptyPanel;
    }

    private JScrollPane createOrderScrollPane() {
        JPanel orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));
        orderListPanel.setBackground(Color.WHITE);

        // Thêm các item đơn hàng vào đây khi có sản phẩm
        JScrollPane scrollPane = new JScrollPane(orderListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    private JPanel createProductListPanel() {
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(Color.WHITE);

        // Panel chứa các nút chức năng
        JPanel buttonPanel = createFunctionButtonPanel();
        productPanel.add(buttonPanel, BorderLayout.NORTH);

        // Panel chứa nội dung chính (CardLayout)
        JPanel mainContentPanel = new JPanel(new CardLayout());

        // Panel thao tác nhanh
        JPanel quickActionPanel = createQuickActionPanel();
        mainContentPanel.add(quickActionPanel, "QUICK_ACTION");

        // Panel danh sách sản phẩm
        JPanel productListPanel = createProductGridPanel();
        mainContentPanel.add(productListPanel, "PRODUCT_LIST");

        productPanel.add(mainContentPanel, BorderLayout.CENTER);

        return productPanel;
    }

    private JPanel createFunctionButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        buttonPanel.setBackground(Color.WHITE);

        JButton quickActionBtn = createRoundedButton("Thao tác nhanh", 180, 45, new Color(0, 123, 255), Color.WHITE);
        JButton productListBtn = createRoundedButton("Danh sách sản phẩm", 180, 45, new Color(40, 167, 69), Color.WHITE);

        quickActionBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) ((JPanel) buttonPanel.getParent().getComponent(1)).getLayout();
            cl.show((JPanel) buttonPanel.getParent().getComponent(1), "QUICK_ACTION");
            updateButtonColors(quickActionBtn, productListBtn);
        });

        productListBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) ((JPanel) buttonPanel.getParent().getComponent(1)).getLayout();
            cl.show((JPanel) buttonPanel.getParent().getComponent(1), "PRODUCT_LIST");
            updateButtonColors(productListBtn, quickActionBtn);
        });

        buttonPanel.add(quickActionBtn);
        buttonPanel.add(productListBtn);

        return buttonPanel;
    }

    private JPanel createQuickActionPanel() {
        JPanel quickActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        quickActionPanel.setBackground(Color.WHITE);

        JButton addServiceBtn = createRoundedButton("Thêm dịch vụ (F9)", 200, 45, new Color(23, 162, 184), Color.WHITE);
        JButton clearOrderBtn = createRoundedButton("Xóa toàn bộ đơn hàng", 200, 45, new Color(220, 53, 69), Color.WHITE);
        JButton customerInfoBtn = createRoundedButton("Thông tin khách hàng", 200, 45, new Color(255, 193, 7), Color.BLACK);

        addServiceBtn.addActionListener(e -> showDevelopmentMessage());
        clearOrderBtn.addActionListener(e -> clearAllOrderItems());
        customerInfoBtn.addActionListener(e -> showCustomerSelectionDialog());

        quickActionPanel.add(addServiceBtn);
        quickActionPanel.add(clearOrderBtn);
        quickActionPanel.add(customerInfoBtn);

        return quickActionPanel;
    }

    private JPanel createProductGridPanel() {
        SanPhamDAO dao = new SanPhamDAO();
        List<Product> products = dao.selectAll(); // Lấy dữ liệu từ DB

        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        for (Product product : products) {
            gridPanel.add(createProductCard(product));
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel container = new JPanel(new BorderLayout());
        container.add(scrollPane, BorderLayout.CENTER);
        container.setBackground(Color.WHITE);

        return container;
    }

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        try {
            // Thử load từ file hệ thống trước
            File imageFile = new File(path);
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }

            // Nếu không tìm thấy file, thử load từ resources
            URL imageUrl = getClass().getResource(path);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }

            throw new FileNotFoundException("Không tìm thấy ảnh: " + path);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh: " + e.getMessage());
            // Trả về icon mặc định
            BufferedImage defaultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = defaultImage.createGraphics();
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawString("No Image", width / 4, height / 2);
            g2d.dispose();
            return new ImageIcon(defaultImage);
        }
    }

    private void updateTotalPrice() {
        int total = 0;
        for (OrderItem item : orderItems) {
            total += item.getProduct().getGia() * item.getQuantity();
        }

        totalValueLabel.setText(String.format("%,dđ", total));
        updateDiscountAndAmount();
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Panel chứa Tổng tiền và Chiết khấu (chữ to hơn)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 20, 15, 20) // Tăng padding
        ));

        // Tổng tiền (font size 20)
        JPanel totalPanel = createMoneyRow("Tổng tiền:", totalValueLabel = new JLabel("0"), false, 20);
        topPanel.add(totalPanel);

        // Chiết khấu (font size 18)
        JPanel discountPanel = createMoneyRow("Chiết khấu (F1):", discountValueLabel = new JLabel("0"), false, 18);
        topPanel.add(discountPanel);

        // Đường kẻ ngăn cách dày hơn
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(200, 200, 200));
        separator.setPreferredSize(new Dimension(0, 2));
        topPanel.add(separator);

        // KHÁCH PHẢI TRẢ (font size 22, in đậm)
        JPanel amountPanel = createMoneyRow("KHÁCH PHẢI TRẢ:", amountLabel = new JLabel("0"), true, 22);
        topPanel.add(amountPanel);

        rightPanel.add(topPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Tăng khoảng cách

        // 2. Panel chứa Tiền khách đưa và Tiền thừa
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 20, 15, 20) // Tăng padding
        ));

        // Tiền khách đưa (font size 18, in đậm)
        JPanel givenPanel = createMoneyRow("Tiền khách đưa (F2):", givenAmountLabel = new JLabel("0"), true, 18);
        bottomPanel.add(givenPanel);

        // Phương thức thanh toán (font size 16)
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        paymentPanel.setBackground(Color.WHITE);

        JLabel paymentLabel = new JLabel("Phương thức:");
        paymentLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        paymentPanel.add(paymentLabel);

        paymentMethodComboBox = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản"});
        paymentMethodComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        paymentMethodComboBox.setPreferredSize(new Dimension(150, 30));
        paymentPanel.add(paymentMethodComboBox);

        bottomPanel.add(paymentPanel);

        // Tiền thừa (font size 18, in đậm)
        JPanel changePanel = createMoneyRow("Tiền thừa trả khách:", changeAmountLabel = new JLabel("0"), true, 18);
        bottomPanel.add(changePanel);

        rightPanel.add(bottomPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. Phần ghi chú mới thêm
        // Panel chứa tiêu đề
        JPanel noteLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        noteLabelPanel.setBackground(Color.WHITE); // Đặt màu nền giống với giao diện
        JLabel noteLabel = new JLabel("Ghi chú:");
        noteLabel.setFont(new Font("Arial", Font.BOLD, 16));
        noteLabelPanel.add(noteLabel);

// Panel chứa JTextArea
        JPanel notePanel = new JPanel(new BorderLayout());
        notePanel.setBackground(Color.WHITE);
        notePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        noteTextArea = new JTextArea(3, 20);
        noteTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        noteTextArea.setLineWrap(true);
        noteTextArea.setWrapStyleWord(true);
        noteTextArea.setBorder(null); // Xóa viền của JTextArea

        JScrollPane scrollPane = new JScrollPane(noteTextArea);
        scrollPane.setBorder(null); // Xóa viền của JScrollPane

        notePanel.add(scrollPane, BorderLayout.CENTER);

// Thêm label và panel vào rightPanel
        rightPanel.add(noteLabelPanel);
        rightPanel.add(notePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 4. Nút thanh toán (to hơn)
        JPanel buttonPanel = new JPanel(new GridBagLayout()); // Sử dụng GridBagLayout để căn giữa
        buttonPanel.setBackground(new Color(245, 245, 245));

        JPanel paymentButtonPanel = createPaymentButtons();
        paymentButtonPanel.setFont(new Font("Arial", Font.BOLD, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Thêm padding trên dưới
        buttonPanel.add(paymentButtonPanel, gbc);

        rightPanel.add(buttonPanel);

        return rightPanel;
    }

    private JPanel createMoneyRow(String labelText, JLabel valueLabel, boolean isBold, int fontSize) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0)); // Tăng padding dọc

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, fontSize));

        valueLabel.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, fontSize));
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        if (isBold) {
            valueLabel.setForeground(new Color(200, 0, 0));
        }

        panel.add(label, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createPaymentButtons() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(245, 245, 245));

        // Tạo nút duy nhất
        JButton combinedPaymentBtn = createPaymentButton("THANH TOÁN & IN", new Color(40, 167, 69)); // Màu xanh lá

        // Căn giữa nút
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 5, 10, 5);
        buttonPanel.add(combinedPaymentBtn, gbc);

        // Gọi cả hai chức năng khi ấn nút (có thể tùy biến logic bên trong)
        combinedPaymentBtn.addActionListener(e -> {
            // Kiểm tra đơn hàng trống trước khi thanh toán
            if (orderItems.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Đơn hàng chưa có sản phẩm nào!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Hỏi người dùng có muốn in hóa đơn không
            int option = JOptionPane.showConfirmDialog(null, "Bạn có muốn in hóa đơn không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            boolean printInvoice = (option == JOptionPane.YES_OPTION);

            processPayment(printInvoice); // Gọi phương thức xử lý
        });

        return buttonPanel;
    }

    private JButton createPaymentButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            private Color originalBgColor = bgColor;
            private Color hoverBgColor = bgColor.darker();
            private Color pressBgColor = bgColor.darker().darker();

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Thay đổi màu nền khi hover hoặc nhấn
                if (getModel().isPressed()) {
                    g2d.setColor(pressBgColor);
                } else if (getModel().isRollover()) {
                    g2d.setColor(hoverBgColor);
                } else {
                    g2d.setColor(originalBgColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                Rectangle2D textBounds = fm.getStringBounds(getText(), g2d);
                int textX = (int) ((getWidth() - textBounds.getWidth()) / 2);
                int textY = (int) ((getHeight() - textBounds.getHeight()) / 2 + fm.getAscent());
                g2d.drawString(getText(), textX, textY);
                g2d.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Không vẽ viền
            }
        };

        button.setPreferredSize(new Dimension(200, 60));
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        // Thêm hiệu ứng hover bằng MouseListener
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(Cursor.getDefaultCursor());
                button.repaint();
            }
        });

        return button;
    }

    private JButton createRoundedButton(String text, int width, int height, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.setColor(getForeground());
                FontMetrics fm = g2d.getFontMetrics();
                Rectangle2D textBounds = fm.getStringBounds(getText(), g2d);
                int textX = (int) ((getWidth() - textBounds.getWidth()) / 2);
                int textY = (int) ((getHeight() - textBounds.getHeight()) / 2 + fm.getAscent());
                g2d.drawString(getText(), textX, textY);
                g2d.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Không vẽ viền
            }
        };
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setContentAreaFilled(false);
        return button;
    }

    private void showDevelopmentMessage() {
        JOptionPane.showMessageDialog(this, "Chức năng đang được phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateButtonColors(JButton activeButton, JButton inactiveButton) {
        activeButton.setBackground(activeButton.getBackground().darker());
        inactiveButton.setBackground(inactiveButton.getBackground().brighter());
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setPreferredSize(new Dimension(200, 220));
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Ảnh sản phẩm
        ImageIcon productIcon = loadScaledIcon(product.getHinhanh(), 150, 150);
        JLabel imageLabel = new JLabel(productIcon, SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.CENTER);

        // Thông tin sản phẩm
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(shortenName(product.getTenSanPham(), 20), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel priceLabel = new JLabel(String.format("%,.0fđ", product.getGia()), SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setForeground(new Color(200, 0, 0));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        card.add(infoPanel, BorderLayout.SOUTH);

        // Hiệu ứng hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(240, 248, 255));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(100, 149, 237)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Sửa lại dòng này
                showProductDetail(product);
            }
        });

        return card;
    }

    private String shortenName(String name, int maxLength) {
        return name.length() > maxLength ? name.substring(0, maxLength - 3) + "..." : name;
    }

    private void showProductDetail(Product product) {
        JDialog detailDialog = new JDialog(this, "Chi tiết sản phẩm", true);
        detailDialog.setLayout(new BorderLayout());
        detailDialog.setSize(350, 260); // Giảm kích thước dialog
        detailDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 15)); // Tăng khoảng cách dọc
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel thông tin sản phẩm
        JPanel infoPanel = new JPanel(new BorderLayout(10, 0));

        // Hiển thị ảnh sản phẩm (nhỏ hơn)
        ImageIcon productIcon = loadScaledIcon(product.getHinhanh(), 100, 100);
        JLabel imageLabel = new JLabel(productIcon);
        infoPanel.add(imageLabel, BorderLayout.WEST);

        // Panel chi tiết sản phẩm
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Tên sản phẩm (thêm padding)
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        namePanel.add(new JLabel("Tên: " + product.getTenSanPham()));
        detailPanel.add(namePanel);

        // Giá sản phẩm (thêm padding)
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        pricePanel.add(new JLabel("Giá: " + String.format("%,.0fđ", product.getGia())));
        detailPanel.add(pricePanel);

        // Khoảng cách giữa giá và số lượng
        detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Số lượng - căn chỉnh đều nhau
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel quantityLabel = new JLabel("Số lượng:  ");
        quantityPanel.add(quantityLabel);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) quantitySpinner.getEditor();
        editor.getTextField().setColumns(3);
        quantitySpinner.setPreferredSize(new Dimension(60, 25));
        quantityPanel.add(quantitySpinner);

        // Thêm padding cho toàn bộ quantityPanel
        JPanel quantityWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        quantityWrapper.add(quantityPanel);
        detailPanel.add(quantityWrapper);

        infoPanel.add(detailPanel, BorderLayout.CENTER);
        contentPanel.add(infoPanel, BorderLayout.CENTER);

        // Nút thêm vào đơn hàng (nhỏ hơn)
        JButton addButton = new JButton("THÊM VÀO ĐƠN HÀNG") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Màu nền (xanh lá cây đậm)
                Color bgColor = new Color(46, 125, 50);
                if (getModel().isPressed()) {
                    bgColor = new Color(35, 95, 38); // Màu khi nhấn
                } else if (getModel().isRollover()) {
                    bgColor = new Color(56, 142, 60); // Màu khi hover
                }

                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Vẽ viền
                g2d.setColor(new Color(30, 80, 32));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                // Vẽ chữ
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                Rectangle2D textBounds = fm.getStringBounds(getText(), g2d);
                int textX = (int) ((getWidth() - textBounds.getWidth()) / 2);
                int textY = (int) ((getHeight() - textBounds.getHeight()) / 2 + fm.getAscent());
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Không vẽ viền mặc định
            }
        };

        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(220, 40)); // Kích thước lớn hơn
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

// Hiệu ứng hover
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setCursor(Cursor.getDefaultCursor());
            }
        });

// Sự kiện click
        addButton.addActionListener(e -> {
            int quantity = (int) quantitySpinner.getValue();
            addProductToOrder(product, quantity);
            detailDialog.dispose();
        });

// Panel chứa nút với margin
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        detailDialog.add(contentPanel);
        detailDialog.setVisible(true);
    }

    private void addProductToOrder(Product product, double quantity) {
        // Kiểm tra xem sản phẩm đã có trong đơn hàng chưa
        boolean found = false;
        for (OrderItem item : orderItems) {
            if (item.getProduct().getMaSanPham() == product.getMaSanPham()) {
    item.setQuantity(item.getQuantity() + quantity);
    found = true;
    break;
}

        }

        // Nếu chưa có thì thêm mới
        if (!found) {
            orderItems.add(new OrderItem(product, quantity));
        }

        // Cập nhật giao diện
        updateOrderDisplay();
        updateTotalPrice();
    }

    private void updateOrderDisplay() {
        if (orderItems.isEmpty()) {
            // Tạo lại empty panel nếu cần
            if (containerPanel.getComponent(0) == null) {
                containerPanel.add(createEmptyOrderPanel(), "EMPTY");
            }
            cardLayout.show(containerPanel, "EMPTY");
        } else {
            // Cập nhật danh sách sản phẩm
            JScrollPane scrollPane = (JScrollPane) containerPanel.getComponent(1);
            JViewport viewport = scrollPane.getViewport();
            JPanel orderListPanel = (JPanel) viewport.getView();

            orderListPanel.removeAll();
            for (OrderItem item : orderItems) {
                orderListPanel.add(createOrderItem(item));
            }

            orderListPanel.revalidate();
            orderListPanel.repaint();
            cardLayout.show(containerPanel, "ITEMS");
        }
    }

    private JPanel createOrderItem(OrderItem orderItem) {
        Product product = orderItem.getProduct();

        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Panel chứa ảnh và thông tin
        JPanel infoPanel = new JPanel(new BorderLayout(10, 0));

        // Ảnh sản phẩm (nhỏ hơn)
        ImageIcon productIcon = loadScaledIcon(product.getHinhanh(), 60, 60);
        JLabel imageLabel = new JLabel(productIcon);
        infoPanel.add(imageLabel, BorderLayout.WEST);

        // Thông tin sản phẩm
        JPanel detailPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        detailPanel.add(new JLabel(product.getTenSanPham()));
        detailPanel.add(new JLabel(String.format("%,.0fđ x %.0f", product.getGia(), orderItem.getQuantity())));
        infoPanel.add(detailPanel, BorderLayout.CENTER);

        // Nút xóa
        JButton removeButton = new JButton("Xóa");

// Đổi màu nền và chữ
        removeButton.setBackground(new Color(255, 102, 102)); // Màu đỏ nhạt
        removeButton.setForeground(Color.WHITE);

// Bo tròn và loại bỏ viền mặc định
        removeButton.setFocusPainted(false);
        removeButton.setBorder(BorderFactory.createLineBorder(Color.RED));
        removeButton.setFont(new Font("Arial", Font.BOLD, 12));
        removeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

// Thêm hiệu ứng hover
        removeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                removeButton.setBackground(new Color(255, 51, 51)); // Đỏ đậm khi hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                removeButton.setBackground(new Color(255, 102, 102)); // Trở lại màu cũ
            }
        });

// Sự kiện xóa
        removeButton.addActionListener(e -> {
            orderItems.remove(orderItem);
            updateOrderDisplay();
            updateTotalPrice();
        });

        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(removeButton, BorderLayout.EAST);

        return itemPanel;
    }

    private void clearAllOrderItems() {
        // Hiển thị hộp thoại xác nhận
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa toàn bộ đơn hàng?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Xóa toàn bộ đơn hàng
            orderItems.clear();

            // Cập nhật giao diện
            updateOrderDisplay();
            updateTotalPrice();

            // Hiển thị panel trống
            cardLayout.show(containerPanel, "EMPTY");

            JOptionPane.showMessageDialog(this, "Đã xóa toàn bộ đơn hàng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setupKeyboardShortcuts() {
        // Phím tắt F6 cho chiết khấu
        discountValueLabel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "editDiscount");
        discountValueLabel.getActionMap().put("editDiscount", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chietkhau();
            }
        });

//         Phím tắt F2 cho tiền khách đưa
        givenAmountLabel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "editGivenAmount");
        givenAmountLabel.getActionMap().put("editGivenAmount", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tienkhachdua();
            }
        });
    }

    //Tinh chiet khau
    private void chietkhau() {
        String input = JOptionPane.showInputDialog(this,
                "Nhập % chiết khấu (ví dụ: 10 cho 10%):",
                "");

        if (input != null && !input.trim().isEmpty()) {
            try {
                double discountPercent = Double.parseDouble(input);
                if (discountPercent < 0 || discountPercent > 100) {
                    JOptionPane.showMessageDialog(this,
                            "Chiết khấu phải từ 0% đến 100%",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Lưu giá trị % vào label (ẩn đi)
                discountValueLabel.putClientProperty("discountPercent", discountPercent);

                // Tính toán và hiển thị giá trị chiết khấu thực tế
                updateDiscountAndAmount();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Giá trị không hợp lệ! Vui lòng nhập số.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateDiscountAndAmount() {
        double total = getTotalAmount();
        Double discountPercent = (Double) discountValueLabel.getClientProperty("discountPercent");

        if (discountPercent == null) {
            discountPercent = 0.0;
        }

        double discountAmount = total * discountPercent / 100;
        double amountToPay = total - discountAmount;

        discountValueLabel.setText(String.format("%,.0fđ (%.0f%%)", discountAmount, discountPercent));
        amountLabel.setText(String.format("%,.0fđ", amountToPay));

        // Cập nhật luôn tiền thừa khi thay đổi chiết khấu
        updateChangeAmount();
    }

    private double getTotalAmount() {
        try {
            String totalText = totalValueLabel.getText().replaceAll("[^0-9]", "");
            return totalText.isEmpty() ? 0 : Double.parseDouble(totalText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi đọc tổng tiền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    private void updateChangeAmount() {
        try {
            // Lấy số tiền phải trả (đã trừ chiết khấu)
            double amountToPay = Double.parseDouble(amountLabel.getText().replaceAll("[^0-9]", ""));

            // Lấy số tiền khách đưa
            double givenAmount = Double.parseDouble(givenAmountLabel.getText().replaceAll("[^0-9]", ""));

            // Tính tiền thừa
            double change = givenAmount - amountToPay;

            // Hiển thị tiền thừa (màu đỏ nếu khách thiếu tiền, xanh lá nếu thừa)
            if (change >= 0) {
                changeAmountLabel.setForeground(new Color(0, 100, 0)); // Màu xanh lá
                changeAmountLabel.setText(String.format("%,.0fđ", change));
            } else {
                changeAmountLabel.setForeground(Color.RED);
                changeAmountLabel.setText(String.format("(Thiếu: %,.0fđ)", Math.abs(change)));
            }

        } catch (NumberFormatException ex) {
            changeAmountLabel.setText("0đ");
            changeAmountLabel.setForeground(Color.BLACK);
        }
    }

    // tinh tien khach dua
    private void tienkhachdua() {
        String input = JOptionPane.showInputDialog(this,
                "Nhập số tiền khách đưa (ví dụ: 500000):",
                "");

        if (input != null && !input.trim().isEmpty()) {
            try {
                double givenAmount = Double.parseDouble(input);
                if (givenAmount < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Số tiền không hợp lệ! Vui lòng nhập số dương.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Lưu giá trị tiền khách đưa
                givenAmountLabel.setText(String.format("%,.0fđ", givenAmount));

                // Cập nhật tiền thừa
                updateChangeAmount();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Giá trị không hợp lệ! Vui lòng nhập số.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showCustomerDetail(ThanhVien thanhvien) {
        String message = "<html><b>" + thanhvien.getHoTen() + "</b><br>"
                + "SĐT: " + thanhvien.getGioiTinh() + "<br>"
                + "Giới Tính: " + thanhvien.getSoDT() + "</html>";
        JOptionPane.showMessageDialog(this, message, "Thông tin khách hàng", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateCustomerInfoDisplay() {
        if (selectedCustomer != null) {
            // Hiển thị thông tin khách hàng đã chọn
            String displayText = selectedCustomer.getHoTen();

            // Giới hạn độ dài tên nếu quá dài
            if (displayText.length() > 20) {
                displayText = displayText.substring(0, 17) + "...";
            }

            customerNameLabel.setText(displayText);
            customerNameLabel.setForeground(new Color(255, 255, 150)); // Màu vàng nhạt
            customerNameLabel.setFont(new Font("Arial", Font.BOLD, 14));

            // Tooltip hiển thị đầy đủ thông tin
            String tooltipText = String.format("<html><b>%s</b><br>Điện thoại: %s<br>Địa chỉ: %s</html>",
                    selectedCustomer.getHoTen(),
                    selectedCustomer.getSoDT(),
                    selectedCustomer.getGioiTinh());
            customerNameLabel.setToolTipText(tooltipText);
        } else {
            // Trạng thái chưa chọn khách hàng
            customerNameLabel.setText("Chưa chọn khách hàng");
            customerNameLabel.setForeground(Color.WHITE);
            customerNameLabel.setToolTipText("Nhấn để chọn khách hàng");
            customerNameLabel.setIcon(null); // Xóa icon nếu có
        }

        // Cập nhật lại giao diện
        customerNameLabel.revalidate();
        customerNameLabel.repaint();
    }

    private void showCustomerSelectionDialog() {
        JDialog customerDialog = new JDialog(this, "Chọn khách hàng", true);
        customerDialog.setSize(700, 500);
        customerDialog.setLayout(new BorderLayout());
        customerDialog.setLocationRelativeTo(this);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.putClientProperty("JTextField.placeholderText", "Nhập tên hoặc SĐT khách hàng...");

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        JPanel searchButtonPanel = new JPanel();
        searchButtonPanel.add(searchButton);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButtonPanel, BorderLayout.EAST);

        // Bảng danh sách khách hàng
        String[] columnNames = {"Mã KH", "Tên KH", "SĐT", "Giới Tính"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable customerTable = new JTable(model);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setReorderingAllowed(false);
        customerTable.setRowHeight(25);

        // Load dữ liệu ban đầu
        loadCustomerData(model, "");

        // Sự kiện tìm kiếm
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            loadCustomerData(model, keyword);
        });

        searchField.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            loadCustomerData(model, keyword);
        });

        // Sự kiện chọn khách hàng
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedCustomer = new ThanhVien(
                            (int) model.getValueAt(selectedRow, 0), // maTV
                            (String) model.getValueAt(selectedRow, 1), // hoTen
                            (String) model.getValueAt(selectedRow, 2), // gioiTinh
                            (String) model.getValueAt(selectedRow, 3), // soDT
                            null, // ngayDK
                            null, // ngayKT
                            0, // maGoi
                            null // TenGoi
                    );
                }
            }
        });

        // Panel nút chọn/hủy
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectButton = new JButton("Chọn");
        selectButton.setBackground(new Color(34, 139, 34));
        selectButton.setForeground(Color.WHITE);
        selectButton.setPreferredSize(new Dimension(100, 30));

        JButton cancelButton = new JButton("Hủy");
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(100, 30));

        selectButton.addActionListener(e -> {
            if (selectedCustomer != null) {
                customerDialog.dispose();
                updateCustomerInfoDisplay();
            } else {
                JOptionPane.showMessageDialog(customerDialog,
                        "Vui lòng chọn khách hàng",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> customerDialog.dispose());

        buttonPanel.add(selectButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);

        // Thêm các component vào dialog
        customerDialog.add(searchPanel, BorderLayout.NORTH);
        customerDialog.add(new JScrollPane(customerTable), BorderLayout.CENTER);
        customerDialog.add(buttonPanel, BorderLayout.SOUTH);

        customerDialog.setVisible(true);
    }

    private void loadCustomerData(DefaultTableModel model, String keyword) {
        model.setRowCount(0); // Xóa dữ liệu cũ

        KhachHangDao khachHangDAO = new KhachHangDao();
        List<ThanhVien> customers = khachHangDAO.searchCustomers(keyword);

        for (ThanhVien kh : customers) {
            model.addRow(new Object[]{
                kh.getMaTV(),
                kh.getHoTen(),
                kh.getSoDT(),
                kh.getGioiTinh()
            });
        }
    }

    private void processPayment(boolean printInvoice) {
        // Kiểm tra đơn hàng trống
        if (orderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Đơn hàng chưa có sản phẩm nào!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra khách hàng
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Tính toán các giá trị
            double total = getTotalAmount();
            Double discountPercent = (Double) discountValueLabel.getClientProperty("discountPercent");
            double discountAmount = discountPercent != null ? total * discountPercent / 100 : 0;
            double amountToPay = total - discountAmount;
            double givenAmount = Double.parseDouble(givenAmountLabel.getText().replaceAll("[^0-9]", ""));
            double change = givenAmount - amountToPay;

            // Kiểm tra tiền khách đưa
            if (change < 0) {
                JOptionPane.showMessageDialog(this,
                        "Khách hàng chưa đưa đủ tiền!\nCòn thiếu: " + String.format("%,.0fđ", Math.abs(change)),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo đơn hàng mới
            entity.DonHang dh = new entity.DonHang();
            dh.setMaKH(selectedCustomer.getMaTV());
            dh.setNgayLap(new Date());
            dh.setThanhTien(amountToPay);

            // Lưu đơn hàng vào database
            DonHangDAO donHangDAO = new DonHangDAO();
            donHangDAO.insert(dh);

            // Lấy mã đơn hàng vừa tạo (giả sử mã tự tăng trong database)
            // Nếu dùng SQL Server có thể dùng: SELECT SCOPE_IDENTITY()
            // Hoặc tạo phương thức getLastInsertId() trong DonHangDAO
            int maDH = donHangDAO.getLastInsertIdShort(); // Hoặc dh.getMaDH() nếu đã được set sau khi insert

            // Lưu chi tiết đơn hàng
            ChiTietDonHangDAO chiTietDAO = new ChiTietDonHangDAO();
            for (OrderItem item : orderItems) {
                ChiTietDonHang ct = new ChiTietDonHang();
                ct.setMaDH(maDH);
                ct.setMaSP(item.getProduct().getMaSanPham());
                ct.setSoLuong((int) item.getQuantity()); // Ép kiểu nếu cần
                ct.setGia(item.getProduct().getGia());

                chiTietDAO.insert(ct);
            }

            // Xuất hóa đơn nếu được yêu cầu
            if (printInvoice) {
                exportInvoiceToExcel(maDH, selectedCustomer, orderItems);
            }

            JOptionPane.showMessageDialog(this, "Thanh toán thành công! Mã đơn hàng: " + maDH,
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            // Reset form sau khi thanh toán
            resetOrderForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thanh toán: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

// Thêm phương thức này vào DonHangDAO để lấy mã đơn hàng vừa insert

    private double calculateTotalAmount() {
        return orderItems.stream()
                .mapToDouble(item -> item.getProduct().getGia() * item.getQuantity())
                .sum();
    }

    private void resetOrderForm() {
        orderItems.clear();
        selectedCustomer = null;
        discount = 0;
        noteTextArea.setText("");
        paymentMethodComboBox.setSelectedIndex(0);

        updateOrderDisplay();
        updateTotalPrice();
        updateCustomerInfoDisplay();

        cardLayout.show(containerPanel, "EMPTY");
    }

    private void exportInvoiceToExcel(int maDH, ThanhVien thanhvien, List<OrderItem> items) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Hóa đơn " + maDH);

            // Lấy các giá trị từ các phương thức hiện có
            double total = getTotalAmount();
            Double discountPercent = (Double) discountValueLabel.getClientProperty("discountPercent");
            double discountAmount = discountPercent != null ? total * discountPercent / 100 : 0;
            double amountToPay = total - discountAmount;
            double givenAmount = Double.parseDouble(givenAmountLabel.getText().replaceAll("[^0-9]", ""));
            double change = givenAmount - amountToPay;

            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12); // tùy chỉnh kích thước
            headerFont.setFontName("Arial"); // tùy chỉnh tên font
            // Tạo style cho bảng
            CellStyle headerStyle = workbook.createCellStyle();

            headerStyle.setFont(headerFont);

            CellStyle moneyStyle = workbook.createCellStyle();
            moneyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));

            // Thông tin cửa hàng
            Row shopRow = sheet.createRow(0);
            shopRow.createCell(0).setCellValue("GYM PRO SHOP");
            shopRow.getCell(0).setCellStyle(headerStyle);

            // Thông tin khách hàng
            Row customerRow = sheet.createRow(2);
            customerRow.createCell(0).setCellValue("Khách hàng: " + thanhvien.getHoTen());
            customerRow.createCell(3).setCellValue("SĐT: " + thanhvien.getSoDT());

            // Ngày và mã hóa đơn
            Row infoRow = sheet.createRow(3);
            infoRow.createCell(0).setCellValue("Ngày: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
            infoRow.createCell(3).setCellValue("Mã HĐ: " + maDH);

            // Tiêu đề bảng sản phẩm
            Row headerRow = sheet.createRow(5);
            String[] headers = {"STT", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Chi tiết sản phẩm
            int rowNum = 6;
            int stt = 1;
            for (OrderItem item : items) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stt++);
                row.createCell(1).setCellValue(item.getProduct().getTenSanPham());
                row.createCell(2).setCellValue(item.getQuantity());

                Cell priceCell = row.createCell(3);
                priceCell.setCellValue(item.getProduct().getGia());
                priceCell.setCellStyle(moneyStyle);

                Cell totalCell = row.createCell(4);
                totalCell.setCellValue(item.getProduct().getGia() * item.getQuantity());
                totalCell.setCellStyle(moneyStyle);
            }

            // Tổng kết thanh toán
            rowNum += 2;
            addInvoiceSummaryRow(sheet, rowNum++, "Tổng tiền:", total, moneyStyle);
            addInvoiceSummaryRow(sheet, rowNum++, String.format("Chiết khấu (%s%%):", discountPercent),
                    discountAmount, moneyStyle);
            addInvoiceSummaryRow(sheet, rowNum++, "Thành tiền:", amountToPay, moneyStyle);
            addInvoiceSummaryRow(sheet, rowNum++, "Tiền khách đưa:", givenAmount, moneyStyle);

            if (change >= 0) {
                addInvoiceSummaryRow(sheet, rowNum++, "Tiền thừa:", change, moneyStyle);
            } else {
                addInvoiceSummaryRow(sheet, rowNum++, "Tiền thiếu:", Math.abs(change), moneyStyle);
            }

            // Tự động điều chỉnh cột
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Lưu file
            String fileName = "HoaDon_" + maDH + "_" + System.currentTimeMillis() + ".xlsx";
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(this, "Xuất hóa đơn thành công!\n" + fileName);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi xuất hóa đơn: " + ex.getMessage());
        }
    }

    private void addInvoiceSummaryRow(Sheet sheet, int rowNum, String label, double value, CellStyle moneyStyle) {
        Row row = sheet.createRow(rowNum);
        row.createCell(3).setCellValue(label);

        Cell valueCell = row.createCell(4);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(moneyStyle);

        updateOrderDisplay();
    }

    ;
    

    public void showMainPanel() {
        this.setVisible(false);
        new TrangChu().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BanHang());
    }
}
