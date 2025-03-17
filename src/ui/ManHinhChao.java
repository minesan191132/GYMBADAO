/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 *
 * @author Admin
 */
public class ManHinhChao extends JFrame {

    private JProgressBar progressBar;

    public ManHinhChao() {
        setTitle("The Fitness Club");
        setSize(1116, 627);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        ImageIcon icon = new ImageIcon("D:/Gym/GYMBADAO/src/icon/Banner.jpg");
        Image scaledImage = icon.getImage().getScaledInstance(1116, 627, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        JLabel label = new JLabel(resizedIcon);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Progress Bar ẩn ban đầu
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0xFF6600)); // vàng cam nhạt
        progressBar.setVisible(false);
        progressBar.setFont(new Font("Arial", Font.BOLD, 14));
        progressBar.setBackground(Color.WHITE); // nền trắng cho bar
        progressBar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // viền xám mờ
        progressBar.setVisible(false);

        // Bấm vào ảnh thì hiện progress bar
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                progressBar.setVisible(true);
                startProgress();
            }
        });

        add(label, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void startProgress() {
        Timer timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int value = progressBar.getValue();
                if (value < 100) {
                    progressBar.setValue(value + 1);
                } else {
                    ((Timer) e.getSource()).stop();
                    dispose();
                    new DangNhap(); // mở form đăng nhập
                }
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        new ManHinhChao();
    }
}
