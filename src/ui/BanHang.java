package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BanHang extends JFrame {

    public BanHang() {
        // 1. Cài đặt cửa sổ chính
        setTitle("Giao diện bán hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 2. Tạo header panel
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 139));
                g2d.fillRect(0, 0, getWidth(), 70);
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));
        headerPanel.setLayout(new BorderLayout());

        // 2.1 Thanh tìm kiếm
        JPanel roundedPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        roundedPanel.setLayout(new BorderLayout());
        roundedPanel.setOpaque(false);
        roundedPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Tạo thanh tìm kiếm
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setOpaque(false);
        roundedPanel.add(searchField, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        searchPanel.setOpaque(false);
        searchPanel.add(roundedPanel);
        headerPanel.add(searchPanel, BorderLayout.WEST);

        // 2.2 Nút phím tắt và icon
        JButton shortcutButton = createRoundedButton("Phím tắt", 120, 30, Color.WHITE, Color.BLACK);
        shortcutButton.addActionListener(e -> JOptionPane.showMessageDialog(BanHang.this, 
            "Chức năng đang được phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        ImageIcon originalIcon = new ImageIcon("/GYMBADAO/src/icon/home.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(iconLabel);
        buttonPanel.add(shortcutButton);

        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.setOpaque(false);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        paddingPanel.add(buttonPanel, BorderLayout.CENTER);
        headerPanel.add(paddingPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // 3. Phần nội dung chính
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        horizontalSplitPane.setDividerSize(5);
        horizontalSplitPane.setDividerLocation(500);
        horizontalSplitPane.setBackground(Color.GRAY);

        // 3.1 Panel trái
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new BorderLayout());

        // 3.2 Panel phải
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());

        // Tạo đối tượng GridBagConstraints để điều chỉnh vị trí và kích thước của các thành phần
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Thêm khoảng cách giữa các thành phần
        gbc.anchor = GridBagConstraints.WEST; // Căn trái các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL; // Các thành phần sẽ mở rộng theo chiều ngang

        // Tạo các label và nút
        JLabel totalLabel = new JLabel("Tổng tiền:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 10, 5, 10); // Thêm padding trên
        rightPanel.add(totalLabel, gbc);

        JLabel totalValueLabel = new JLabel("0");
        totalValueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; // Căn phải giá trị
        gbc.insets = new Insets(15, 10, 5, 30); // Thêm padding phải nhiều hơn
        rightPanel.add(totalValueLabel, gbc);

        JLabel discountLabel = new JLabel("Chiết khấu (F6):");
        discountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 5, 10); // Thêm padding trên
        rightPanel.add(discountLabel, gbc);

        JLabel discountValueLabel = new JLabel("0");
        discountValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST; // Căn phải giá trị
        gbc.insets = new Insets(10, 10, 5, 30); // Thêm padding phải nhiều hơn
        rightPanel.add(discountValueLabel, gbc);

        JLabel customerPayLabel = new JLabel("KHÁCH PHẢI TRẢ:");
        customerPayLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 10, 5, 10); // Thêm padding trên
        rightPanel.add(customerPayLabel, gbc);

        JLabel amountLabel = new JLabel("0");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST; // Căn phải giá trị
        gbc.insets = new Insets(15, 10, 5, 30); // Thêm padding phải nhiều hơn
        rightPanel.add(amountLabel, gbc);

        JLabel customerGivenLabel = new JLabel("Tiền khách đưa (F2):");
        customerGivenLabel.setFont(new Font("Arial", Font.BOLD, 14)); // In đậm
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 5, 10); // Thêm padding trên
        rightPanel.add(customerGivenLabel, gbc);

        JLabel givenAmountLabel = new JLabel("0");
        givenAmountLabel.setFont(new Font("Arial", Font.BOLD, 14)); // In đậm
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST; // Căn phải giá trị
        gbc.insets = new Insets(10, 10, 5, 30); // Thêm padding phải nhiều hơn
        rightPanel.add(givenAmountLabel, gbc);

        JLabel changeLabel = new JLabel("Tiền thừa khách trả:");
        changeLabel.setFont(new Font("Arial", Font.BOLD, 14)); // In đậm
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 5, 10); // Thêm padding trên
        rightPanel.add(changeLabel, gbc);

        JLabel changeAmountLabel = new JLabel("0");
        changeAmountLabel.setFont(new Font("Arial", Font.BOLD, 14)); // In đậm
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST; // Căn phải giá trị
        gbc.insets = new Insets(10, 10, 5, 20); // Thêm padding phải nhiều hơn
        rightPanel.add(changeAmountLabel, gbc);

        // Tạo dropdown menu cho phương thức thanh toán
        JPanel paymentMethodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        paymentMethodPanel.setBackground(Color.WHITE);

        JLabel paymentMethodLabel = new JLabel("Tiền mặt (Alt+x)");
        paymentMethodLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentMethodPanel.add(paymentMethodLabel);

        // Tạo mũi tên dropdown
        JComboBox<String> paymentMethodComboBox = new JComboBox<>(new String[]{"Chuyển khoản", "Tiền mặt", "Quẹt thẻ"});
        paymentMethodComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentMethodComboBox.setPreferredSize(new Dimension(120, 25));
        paymentMethodPanel.add(paymentMethodComboBox);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Chiếm 2 cột
        gbc.insets = new Insets(10, 10, 5, 10); // Thêm padding
        rightPanel.add(paymentMethodPanel, gbc);

        // Tạo ô ghi chú hình chữ nhật bo góc với placeholder
        JPanel notePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bo tròn góc
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); // Viền bo tròn
                g2d.dispose();
            }
        };
        notePanel.setLayout(new BorderLayout());
        notePanel.setPreferredSize(new Dimension(250, 100)); // Kích thước ô ghi chú

        JTextArea noteTextArea = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(Color.GRAY);
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    g2d.drawString("Ghi chú...", 10, 20); // Placeholder text
                    g2d.dispose();
                }
            }
        };
        noteTextArea.setLineWrap(true); // Tự động xuống dòng
        noteTextArea.setWrapStyleWord(true); // Xuống dòng theo từ
        noteTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding bên trong
        noteTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        noteTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (noteTextArea.getText().equals("Ghi chú...")) {
                    noteTextArea.setText(""); // Xóa placeholder khi focus
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (noteTextArea.getText().isEmpty()) {
                    noteTextArea.setText("Ghi chú..."); // Hiển thị placeholder khi mất focus
                }
            }
        });
        notePanel.add(new JScrollPane(noteTextArea), BorderLayout.CENTER); // Thêm thanh cuộn nếu cần

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // Chiếm 2 cột
        gbc.fill = GridBagConstraints.BOTH; // Mở rộng cả chiều ngang và dọc
        gbc.insets = new Insets(10, 10, 10, 10); // Thêm padding
        rightPanel.add(notePanel, gbc);

        // Tạo nút thanh toán bo tròn góc
        JButton paymentButton = new JButton("THANH TOÁN (F1)") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(40, 167, 69)); // Màu nền xanh lá
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bo tròn góc
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
        paymentButton.setFont(new Font("Arial", Font.BOLD, 18));
        paymentButton.setPreferredSize(new Dimension(250, 50)); // Kích thước nút
        paymentButton.setFocusPainted(false);
        paymentButton.setContentAreaFilled(false); // Tắt vẽ nền mặc định
        paymentButton.setOpaque(false); // Trong suốt
        paymentButton.addActionListener(e -> JOptionPane.showMessageDialog(BanHang.this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // Chiếm 2 cột
        gbc.insets = new Insets(60, 10, 20, 10); // Thêm khoảng cách lớn hơn cho nút thanh toán
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa nút thanh toán
        rightPanel.add(paymentButton, gbc);
        
        // Thêm các panel vào JSplitPane ngang
        horizontalSplitPane.setLeftComponent(leftPanel);
        horizontalSplitPane.setRightComponent(rightPanel);

        // Tạo JSplitPane để chia phần bên trái thành hai phần theo chiều dọc
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setDividerSize(5); // Độ dày của đường chia
        verticalSplitPane.setDividerLocation(380); // Dịch đường kẻ ngang xuống dưới
        verticalSplitPane.setBackground(Color.GRAY); // Màu xám cho đường kẻ chia

        // Phần trên của bên trái: Hiển thị đơn hàng
        JPanel topLeftPanel = new JPanel(new BorderLayout());
        topLeftPanel.setBackground(Color.WHITE);

        // Tạo panel chứa danh sách đơn hàng (sẽ hiển thị khi có sản phẩm)
        JPanel orderListPanel = new JPanel();
        orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));
        orderListPanel.setBackground(Color.WHITE);
        JScrollPane orderScrollPane = new JScrollPane(orderListPanel);
        orderScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Tạo panel thông báo đơn hàng trống (sẽ hiển thị khi không có sản phẩm)
        JPanel emptyOrderPanel = new JPanel();
        emptyOrderPanel.setLayout(new BoxLayout(emptyOrderPanel, BoxLayout.Y_AXIS));
        emptyOrderPanel.setBackground(Color.WHITE);

        // Tạo icon và text thông báo
        ImageIcon originalIcon2 = new ImageIcon("/GYMBADAO/src/icon/box.png");
        Image scaledImage2 = originalIcon2.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconLabel2 = new JLabel(new ImageIcon(scaledImage2));
        iconLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel("Đơn hàng của bạn chưa có sản phẩm nào");
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emptyOrderPanel.add(Box.createVerticalGlue());
        emptyOrderPanel.add(iconLabel2);
        emptyOrderPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        emptyOrderPanel.add(textLabel);
        emptyOrderPanel.add(Box.createVerticalGlue());

        // Sử dụng CardLayout để chuyển đổi giữa 2 trạng thái
        CardLayout cardLayout = new CardLayout();
        JPanel containerPanel = new JPanel(cardLayout);
        containerPanel.add(emptyOrderPanel, "EMPTY");
        containerPanel.add(orderScrollPane, "ITEMS");
        cardLayout.show(containerPanel, "EMPTY"); // Mặc định hiển thị trạng thái trống

        topLeftPanel.add(containerPanel, BorderLayout.CENTER);

        // Phần dưới của bên trái
        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.setBackground(Color.WHITE);
        bottomLeftPanel.setLayout(new GridLayout(2, 1, 10, 5));

        // Tạo panel chứa 2 nút trên
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topButtonPanel.setBackground(Color.WHITE);

        // Tạo nút "Thao tác nhanh" bo tròn góc
        JButton quickActionButton = createRoundedButton("Thao tác nhanh", 150, 40, new Color(0, 123, 255), Color.WHITE);

        // Tạo nút "Danh sách sản phẩm" bo tròn góc
        JButton productListButton = createRoundedButton("Danh sách sản phẩm", 150, 40, new Color(40, 167, 69), Color.WHITE);

        // Thêm 2 nút vào topButtonPanel
        topButtonPanel.add(quickActionButton);
        topButtonPanel.add(productListButton);

        // Tạo panel chứa 3 nút dưới
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomButtonPanel.setBackground(Color.WHITE);
        
        // Tạo nút "Thêm dịch vụ (F9)" bo tròn góc
        JButton addServiceButton = createRoundedButton("Thêm dịch vụ (F9)", 150, 40, new Color(23, 162, 184), Color.WHITE);
        addServiceButton.addActionListener(e -> JOptionPane.showMessageDialog(BanHang.this, "Thêm dịch vụ được kích hoạt!", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        // Tạo nút "Xóa toàn bộ đơn hàng" bo tròn góc
        JButton clearOrderButton = createRoundedButton("Xóa toàn bộ đơn hàng", 150, 40, new Color(23, 162, 184), Color.WHITE);
        clearOrderButton.addActionListener(e -> JOptionPane.showMessageDialog(BanHang.this, "Xóa toàn bộ đơn hàng được kích hoạt!", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        // Tạo nút "Thông tin khách hàng" bo tròn góc
        JButton customerInfoButton = createRoundedButton("Thông tin khách hàng", 150, 40, new Color(23, 162, 184), Color.WHITE);
        customerInfoButton.addActionListener(e -> JOptionPane.showMessageDialog(BanHang.this, "Thông tin khách hàng được kích hoạt!", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        // Thêm 3 nút vào bottomButtonPanel
        bottomButtonPanel.add(addServiceButton);
        bottomButtonPanel.add(clearOrderButton);
        bottomButtonPanel.add(customerInfoButton);
//----------------------------------------------------------------//
        // Tạo panel nội dung cho từng chế độ
        JPanel quickActionContent = new JPanel(new BorderLayout());
        quickActionContent.add(bottomButtonPanel, BorderLayout.CENTER);
        
        JPanel productListContent = new JPanel(new BorderLayout());
        productListContent.setBackground(Color.WHITE);

        // Tạo panel chứa danh sách sản phẩm dạng lưới 3x2
        JPanel productGridPanel = new JPanel(new GridLayout(2, 3, 5, 5)); // 2 hàng, 3 cột, khoảng cách 10px
        productGridPanel.setBackground(Color.WHITE);
        productGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Danh sách hình sản phẩm (không giới hạn số lượng)
        List<String> productImages = Arrays.asList(
            "/GYMBADAO/src/icon/pocari.png",
            "/GYMBADAO/src/icon/revive.png",
            "/GYMBADAO/src/icon/whey.png",
            "/GYMBADAO/src/icon/cake.png",
            "/GYMBADAO/src/icon/whey2.png",
            "/GYMBADAO/src/icon/vitamin.png"
            // Có thể thêm ảnh 
        );

        // Danh sách tên sản phẩm (không giới hạn số lượng)
        List<String> customNames = new ArrayList<>(Arrays.asList(
            "Pocari", 
            "Revive", 
            "Whey golden standard",
            "Protein bar",
            "Whey hydropure",
            "Multi vitamin"
            // Có thể thêm 
        ));

        // Danh sách giá (không giới hạn số lượng)
        List<Integer> productPrices = new ArrayList<>(Arrays.asList(
            20000,    // Pocari
            15000,    // Revive
            1120000,  // Whey
            25000,    // Protein bar
            1250000,  // whey2
            350000    // Multi vitamin
            // Có thể thêm giá 
        ));

        // Thêm sản phẩm vào grid (lặp theo số lượng ảnh)
        for (int i = 0; i < productImages.size(); i++) {
    productGridPanel.add(createProductCard(
        i + 1,
        customNames.get(i),
        productPrices.get(i),
        productImages.get(i)  // Truyền đường dẫn ảnh từ list
    ));
}
        
        // Tạo JScrollPane để cuộn nếu có nhiều sản phẩm
        JScrollPane scrollPane = new JScrollPane(productGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Tăng tốc độ cuộn

        productListContent.add(scrollPane, BorderLayout.CENTER);

        // Tạo panel chứa nội dung chính (dùng CardLayout)
        JPanel mainContentPanel = new JPanel(new CardLayout());
        mainContentPanel.add(quickActionContent, "QUICK_ACTION");
        mainContentPanel.add(productListContent, "PRODUCT_LIST");

        // Thêm sự kiện cho các nút chuyển đổi
        quickActionButton.addActionListener(e -> {
        CardLayout cl = (CardLayout) mainContentPanel.getLayout();
        cl.show(mainContentPanel, "QUICK_ACTION");
        updateButtonColors(quickActionButton, productListButton);
    });

        productListButton.addActionListener(e -> {
        CardLayout cl = (CardLayout) mainContentPanel.getLayout();
        cl.show(mainContentPanel, "PRODUCT_LIST");
        updateButtonColors(productListButton, quickActionButton);
    });

        // Thêm topButtonPanel và mainContentPanel vào bottomLeftPanel
        bottomLeftPanel.add(topButtonPanel);
        bottomLeftPanel.add(mainContentPanel); 
        
        // Thêm các panel vào JSplitPane dọc
        verticalSplitPane.setTopComponent(topLeftPanel);
        verticalSplitPane.setBottomComponent(bottomLeftPanel);

        // Đặt JSplitPane dọc vào phần bên trái của JSplitPane ngang
        leftPanel.add(verticalSplitPane, BorderLayout.CENTER);

        // Thêm JSplitPane ngang vào cửa sổ chính
        add(horizontalSplitPane, BorderLayout.CENTER);

        // Hiển thị cửa sổ
        setVisible(true);                
    }

        // 4. Các phương thức hỗ trợ
        private JButton createRoundedButton(String text, int width, int height, Color bgColor, Color fgColor) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Bật khử răng cưa
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bo tròn góc
            g2d.setColor(getForeground());
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D textBounds = fm.getStringBounds(getText(), g2d);
            int textX = (int) ((getWidth() - textBounds.getWidth()) / 2);
            int textY = (int) ((getHeight() - textBounds.getHeight()) / 2 + fm.getAscent());
            g2d.drawString(getText(), textX, textY); // Vẽ chữ giữa nút
            g2d.dispose();
    }

                    @Override
        protected void paintBorder(Graphics g) {
        // Không vẽ viền mặc định
    }
};
            button.setPreferredSize(new Dimension(width, height));
            button.setBackground(bgColor);
            button.setForeground(fgColor);
            button.setFocusPainted(false); // Loại bỏ viền focus
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding bên trong
            button.setContentAreaFilled(false); // Tắt vẽ nền mặc định
            return button;
        }

        // Hàm cập nhật màu nút
        private void updateButtonColors(JButton activeButton, JButton inactiveButton) {
            activeButton.setBackground(activeButton.getBackground().darker());
            inactiveButton.setBackground(inactiveButton.getBackground().brighter());
    }
        
        // Phương thức rút gọn tên sản phẩm nếu quá dài (ngắn hơn)
        private String shortenName(String name, int maxLength) {
            return name.length() > maxLength ? name.substring(0, maxLength-3) + "..." : name;
        }
        
        // Hiển thị chi tiết sản phẩm khi click
        private void showProductDetail(String productCode, String productName, int price) {
            // Tạo dialog chi tiết
            JDialog detailDialog = new JDialog(this, "Chi tiết sản phẩm", true);
            detailDialog.setLayout(new BorderLayout());
            detailDialog.setSize(300, 200);

            // Hiển thị thông tin với mã SKU
            JTextArea detailText = new JTextArea();
            detailText.setText("Mã SP: " + productCode + "\nTên: " + productName + "\nGiá: " + String.format("%,dđ", price));
            detailText.setEditable(false);
            detailDialog.add(new JScrollPane(detailText), BorderLayout.CENTER);

            // Nút thêm vào giỏ hàng
            JButton addButton = new JButton("Thêm vào đơn hàng");
            detailDialog.add(addButton, BorderLayout.SOUTH);

            // Hiển thị dialog
            detailDialog.setLocationRelativeTo(this);
            detailDialog.setVisible(true);
        }
        
        private JPanel createProductCard(int productId, String productName, int price, String imagePath) {
            JPanel productPanel = new JPanel(new BorderLayout());
            productPanel.setPreferredSize(new Dimension(150, 180)); // Kích thước cố định

            // 1. Panel chứa ảnh (căn giữa)
            JPanel imageWrapper = new JPanel(new GridBagLayout()); // Dùng GridBagLayout để căn giữa
            imageWrapper.setBackground(Color.WHITE);

            // 1. Xử lý đường dẫn ảnh
            String normalizedPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;

            // 2. Load ảnh
            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            try {
                
                // Load từ file hệ thống (nếu ảnh nằm ngoài JAR)
                  if (new File(imagePath).exists()) {
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                } else {
                    throw new Exception("Không tìm thấy ảnh");
                }
            } catch (Exception e) {
                imageLabel.setText("SP" + productId);
                System.err.println("Lỗi load ảnh: " + normalizedPath);
            }

            // 3. Thêm các thành phần
            productPanel.add(imageLabel, BorderLayout.CENTER);

                    // Tạo panel nhỏ chứa tên và giá (bỏ panel lót, dùng panel chính luôn)
                    JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2)); // 2 dòng, 1 cột
                    textPanel.setOpaque(false); // Trong suốt
                    textPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

                    // Tên sản phẩm
                    JLabel nameLabel = new JLabel(shortenName(productName, 10), SwingConstants.CENTER);
                    nameLabel.setFont(new Font("Arial", Font.PLAIN, 9));

                    // Giá sản phẩm
                    JLabel priceLabel = new JLabel(String.format("%,dđ", price), SwingConstants.CENTER);
                    priceLabel.setFont(new Font("Arial", Font.BOLD, 9));
                    priceLabel.setForeground(new Color(200, 0, 0));

                    textPanel.add(nameLabel);
                    textPanel.add(priceLabel);

                    // Thêm textPanel vào phía dưới
                    productPanel.add(textPanel, BorderLayout.SOUTH);

                    // Hiệu ứng hover
                    productPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            productPanel.setBackground(new Color(220, 240, 255));
                            productPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255)));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            productPanel.setBackground(new Color(245, 245, 245));
                            productPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {
                    String skuCode = "SKU" + String.format("%03d", productId); // Tạo mã SKU khi click
                    showProductDetail(skuCode, productName, price);

                }
            });

            return productPanel;
        }

        public static void main(String[] args) {
            // Chạy ứng dụng
            SwingUtilities.invokeLater(() -> new BanHang());
    }
}