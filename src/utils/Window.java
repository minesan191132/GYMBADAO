/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author A.Koi
 */
public class Window {
    public static void setAppIcon(JFrame frame) {
        ImageIcon icon = new ImageIcon(Window.class.getResource("/icon/logo.png"));
        frame.setIconImage(icon.getImage());
    }
public static void setAppIcon(JDialog dialog) {
    ImageIcon icon = new ImageIcon(Window.class.getResource("/icon/logo.png"));
    dialog.setIconImage(icon.getImage());
}
}