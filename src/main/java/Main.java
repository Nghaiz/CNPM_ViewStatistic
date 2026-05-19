/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import view.LoginFrm;
import javax.swing.UIManager;

/**
 *
 * @author Nghaiz
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Không thể thiết lập Look and Feel.");
        }
        java.awt.EventQueue.invokeLater(() -> new LoginFrm().setVisible(true));
    }
}