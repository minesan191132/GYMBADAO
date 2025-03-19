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
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 *
 * @author Admin
 */
public class ManHinhChao extends javax.swing.JDialog {

    private JProgressBar progressBar;

    public ManHinhChao(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Gym");
        setSize(1116, 627);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        setUndecorated(true);

        ImageIcon icon = new ImageIcon("/GYMBADAO/src/icon/Banner.jpg");
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
//                    ((Timer) e.getSource()).stop();
//                    dispose();
                    ManHinhChao.this.dispose();
                }
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ManHinhChao dialog = new ManHinhChao(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
}
