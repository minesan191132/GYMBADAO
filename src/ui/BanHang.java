package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utils.*;
import entity.*;
import java.net.URL;  // Thêm dòng này cho URL
import java.io.FileNotFoundException;  // Thêm dòng này cho FileNotFoundException

public class BanHang extends JFrame {

    // Các component chính
    private JLabel totalValueLabel, discountValueLabel, amountLabel, givenAmountLabel, changeAmountLabel;
    private JComboBox<String> paymentMethodComboBox;
    private JTextArea noteTextArea;
    private CardLayout cardLayout;
    private JPanel containerPanel;
    private BufferedImage dfimg;

    public BanHang() {
        initUI();
    }

    private void initUI() {
        setupMainWindow();
        createHeaderPanel();
        createMainContent();
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
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 139));
                g2d.fillRect(0, 0, getWidth(), 80);
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setLayout(new BorderLayout());

        // Thanh tìm kiếm
        JPanel searchPanel = createSearchPanel();
        headerPanel.add(searchPanel, BorderLayout.WEST);

        // Nút phím tắt và icon home
        JPanel buttonPanel = createHeaderButtonPanel();
        headerPanel.add(buttonPanel, BorderLayout.EAST);

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
        JButton shortcutButton = createRoundedButton("Phím tắt", 150, 40, Color.WHITE, Color.BLACK);
        shortcutButton.addActionListener(e -> showDevelopmentMessage());

        ImageIcon homeIcon = loadScaledIcon("/GYMBADAO/src/icon/home.png", 40, 40);
        JLabel iconLabel = new JLabel(homeIcon);
        iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showMainPanel();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(iconLabel);
        buttonPanel.add(shortcutButton);

        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.setOpaque(false);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 30));
        paddingPanel.add(buttonPanel, BorderLayout.CENTER);

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
        clearOrderBtn.addActionListener(e -> showDevelopmentMessage());
        customerInfoBtn.addActionListener(e -> showDevelopmentMessage());

        quickActionPanel.add(addServiceBtn);
        quickActionPanel.add(clearOrderBtn);
        quickActionPanel.add(customerInfoBtn);

        return quickActionPanel;
    }

    private JPanel createProductGridPanel() {
        List<Product> products = Arrays.asList(
                new Product("P001", "Pocari Sweat", 20000, "/GYMBADAO/src/icon/pocari.png"),
                new Product("P002", "Revive", 15000, "/GYMBADAO/src/icon/revive.png"),
                new Product("P003", "Whey Gold Standard", 1120000, "/GYMBADAO/src/icon/whey.png"),
                new Product("P004", "Protein Bar", 25000, "/GYMBADAO/src/icon/protein_bar.png"),
                new Product("P005", "Whey Hydropure", 1250000, "/GYMBADAO/src/icon/whey2.png"),
                new Product("P006", "Multi Vitamin", 350000, "/GYMBADAO/src/icon/vitamin.png"),
                new Product("P007", "BCAA", 450000, "/GYMBADAO/src/icon/bcaa.png"),
                new Product("P008", "Creatine", 300000, "/GYMBADAO/src/icon/creatine.png"),
                new Product("P009", "Pre-workout", 500000, "/GYMBADAO/src/icon/preworkout.png")
        );

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

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setPreferredSize(new Dimension(200, 220));
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Ảnh sản phẩm
        ImageIcon productIcon = loadScaledIcon(product.getImagePath(), 150, 150);
        JLabel imageLabel = new JLabel(productIcon, SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.CENTER);

        // Thông tin sản phẩm
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(shortenName(product.getName(), 20), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel priceLabel = new JLabel(String.format("%,dđ", product.getPrice()), SwingConstants.CENTER);
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
                addProductToOrder(product);
            }
        });

        return card;
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

    private void addProductToOrder(Product product) {
        // Chuyển sang panel có đơn hàng nếu đang ở panel trống
        cardLayout.show(containerPanel, "ITEMS");

        // Lấy panel chứa danh sách đơn hàng
        JScrollPane scrollPane = (JScrollPane) containerPanel.getComponent(1);
        JViewport viewport = scrollPane.getViewport();
        JPanel orderListPanel = (JPanel) viewport.getView();

        // Thêm sản phẩm vào đơn hàng
        JPanel orderItem = createOrderItem(product);
        orderListPanel.add(orderItem);

        // Cập nhật lại giao diện
        orderListPanel.revalidate();
        orderListPanel.repaint();

        // Cập nhật tổng tiền
        updateTotalPrice();
    }

    private JPanel createOrderItem(Product product) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Thông tin sản phẩm
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(new JLabel(product.getName()));
        infoPanel.add(new JLabel(String.format("%,dđ", product.getPrice())));

        // Nút xóa
        JButton removeButton = new JButton("Xóa");
        removeButton.addActionListener(e -> {
            Container parent = itemPanel.getParent();
            parent.remove(itemPanel);
            parent.revalidate();
            parent.repaint();
            updateTotalPrice();
        });

        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(removeButton, BorderLayout.EAST);

        return itemPanel;
    }

    private void updateTotalPrice() {
        // Tính tổng tiền từ các sản phẩm trong đơn hàng
        // (Bạn cần thêm logic tính toán thực tế ở đây)
        totalValueLabel.setText("Tổng tiền cần tính");
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
        JPanel discountPanel = createMoneyRow("Chiết khấu (F6):", discountValueLabel = new JLabel("0"), false, 18);
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
        JPanel changePanel = createMoneyRow("Tiền thừa khách trả:", changeAmountLabel = new JLabel("0"), true, 18);
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
        JButton paymentButton = createPaymentButton();
        paymentButton.setFont(new Font("Arial", Font.BOLD, 20)); // Tăng size chữ
        rightPanel.add(paymentButton);

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

    private JButton createPaymentButton() {
        JButton button = new JButton("THANH TOÁN (F1)") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(40, 167, 69));
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
        button.setPreferredSize(new Dimension(300, 60));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

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

    private JPanel createProductCard(int productId, String productName, int price, String imagePath) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setPreferredSize(new Dimension(200, 220));
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Ảnh sản phẩm
        ImageIcon productIcon = loadScaledIcon(imagePath, 150, 150);
        JLabel imageLabel = new JLabel(productIcon, SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.CENTER);

        // Thông tin sản phẩm
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(shortenName(productName, 20), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel priceLabel = new JLabel(String.format("%,dđ", price), SwingConstants.CENTER);
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
                showProductDetail("SKU" + String.format("%03d", productId), productName, price);
            }
        });

        return card;
    }

    private String shortenName(String name, int maxLength) {
        return name.length() > maxLength ? name.substring(0, maxLength - 3) + "..." : name;
    }

    private void showProductDetail(String productCode, String productName, int price) {
        JDialog detailDialog = new JDialog(this, "Chi tiết sản phẩm", true);
        detailDialog.setLayout(new BorderLayout());
        detailDialog.setSize(350, 250);
        detailDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextArea detailText = new JTextArea();
        detailText.setText(String.format("Mã SP: %s\n\nTên: %s\n\nGiá: %,dđ", productCode, productName, price));
        detailText.setEditable(false);
        detailText.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(new JScrollPane(detailText), BorderLayout.CENTER);

        JButton addButton = new JButton("Thêm vào đơn hàng");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.addActionListener(e -> {
            // Thêm logic thêm vào đơn hàng ở đây
            detailDialog.dispose();
        });

        contentPanel.add(addButton, BorderLayout.SOUTH);
        detailDialog.add(contentPanel);
        detailDialog.setVisible(true);
    }

    public void showMainPanel() {
        this.setVisible(false);
        new TrangChu().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BanHang());
    }
}
