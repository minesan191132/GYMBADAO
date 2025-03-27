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
        // Danh sách sản phẩm
//        List<Product> products = Arrays.asList(
//            new Product("Pocari", 20000, "/GYMBADAO/src/icon/pocari.png"),
//            new Product("Revive", 15000, "/GYMBADAO/src/icon/revive.png"),
//            new Product("Whey golden standard", 1120000, "/GYMBADAO/src/icon/whey.png"),
//            new Product("Protein bar", 25000, "/GYMBADAO/src/icon/cake.png"),
//            new Product("Whey hydropure", 1250000, "/GYMBADAO/src/icon/whey2.png"),
//            new Product("Multi vitamin", 350000, "/GYMBADAO/src/icon/vitamin.png"),
//            new Product("BCAA", 450000, "/GYMBADAO/src/icon/bcaa.png"),
//            new Product("Creatine", 300000, "/GYMBADAO/src/icon/creatine.png"),
//            new Product("Pre-workout", 500000, "/GYMBADAO/src/icon/preworkout.png")
//        );
//        
        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 15, 15)); // Tự động điều chỉnh số hàng, 4 cột
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

//        for (int i = 0; i < products.size(); i++) {
//            Product product = products.get(i);
//            gridPanel.add(createProductCard(i + 1, product.getName(), product.getPrice(), product.getImagePath()));
//        }
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel container = new JPanel(new BorderLayout());
        container.add(scrollPane, BorderLayout.CENTER);
        container.setBackground(Color.WHITE);

        return container;
    }

    private JPanel createRightPanel() {
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setBackground(new Color(245, 245, 245));
    rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // 1. Tổng tiền
    rightPanel.add(createAmountPanel("TỔNG TIỀN", totalValueLabel = new JLabel("0"), 36, new Color(255, 240, 240), new Color(255, 150, 150)));
    rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    // 2. Chiết khấu
    rightPanel.add(createAmountPanel("CHIẾT KHẤU (F6)", discountValueLabel = new JLabel("0"), 28, new Color(240, 248, 255), new Color(100, 149, 237)));
    rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    // 3. Khách phải trả
    rightPanel.add(createAmountPanel("KHÁCH PHẢI TRẢ", amountLabel = new JLabel("0"), 32, new Color(255, 240, 240), new Color(220, 53, 69)));
    rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    // 4. Tiền khách đưa
    rightPanel.add(createAmountPanel("TIỀN KHÁCH ĐƯA (F2)", givenAmountLabel = new JLabel("0"), 28, new Color(230, 255, 240), new Color(40, 167, 69)));
    rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    // 5. Tiền thừa
    rightPanel.add(createAmountPanel("TIỀN THỪA", changeAmountLabel = new JLabel("0"), 28, new Color(255, 245, 230), new Color(255, 193, 7)));

    // Phần thanh toán và ghi chú
    rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    rightPanel.add(createPaymentAndNotePanel());

    return rightPanel;
}

private JPanel createAmountPanel(String title, JLabel valueLabel, int fontSize, Color bgColor, Color borderColor) {
    JPanel panel = new JPanel(new BorderLayout(10, 0)) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            g2.dispose();
        }
    };
    panel.setOpaque(false);
    
    // Thêm padding
    panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    // Label tiêu đề (căn trái)
    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Arial", Font.BOLD, fontSize - 4));
    titleLabel.setForeground(Color.DARK_GRAY);
    panel.add(titleLabel, BorderLayout.WEST);

    // Label giá trị (căn phải)
    valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    valueLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
    valueLabel.setForeground(Color.BLACK);
    panel.add(valueLabel, BorderLayout.EAST);

    return panel;
}
private JPanel createPaymentAndNotePanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBackground(new Color(245, 245, 245));
    
    // Phương thức thanh toán
    panel.add(createPaymentMethodPanel(), BorderLayout.NORTH);
    
    // Ô ghi chú
    panel.add(createNotePanel(), BorderLayout.CENTER);
    
    // Nút thanh toán
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(createPaymentButton());
    panel.add(buttonPanel, BorderLayout.SOUTH);
    
    return panel;
}

private JPanel createPaymentMethodPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    panel.setBackground(new Color(245, 245, 245));
    
    JLabel label = new JLabel("Phương thức thanh toán:");
    label.setFont(new Font("Arial", Font.PLAIN, 16));
    panel.add(label);
    
    paymentMethodComboBox = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Quẹt thẻ"});
    paymentMethodComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
    paymentMethodComboBox.setPreferredSize(new Dimension(150, 30));
    panel.add(paymentMethodComboBox);
    
    return panel;
}

    private JPanel createNotePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 150));

        noteTextArea = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(Color.GRAY);
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    g2d.drawString("Ghi chú...", 10, 25);
                    g2d.dispose();
                }
            }
        };
        noteTextArea.setLineWrap(true);
        noteTextArea.setWrapStyleWord(true);
        noteTextArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        noteTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        noteTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (noteTextArea.getText().equals("Ghi chú...")) {
                    noteTextArea.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (noteTextArea.getText().isEmpty()) {
                    noteTextArea.setText("Ghi chú...");
                }
            }
        });

        panel.add(new JScrollPane(noteTextArea), BorderLayout.CENTER);
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

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh: " + path);
            // Trả về icon mặc định nếu không tìm thấy ảnh
            dfimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = dfimg.createGraphics();
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawString("No Image", width / 4, height / 2);
            g2d.dispose();
            return new ImageIcon(dfimg);
        }
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
