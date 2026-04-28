package com.proj.fees.controller;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminAfterLogin extends JFrame {
    private JButton baddfees, fstudent, treport, fstructure, blogout;

    public AdminAfterLogin(String title) {
        super(title);
        
        // १. मेनू बार (Professional Look साठी)
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem item1 = new JMenuItem("Logout");
        item1.addActionListener(e -> {
    dispose(); 
    new AdminLogin("Admin Login").setVisible(true); // इथे टायटल पास करा
});
        menu.add(item1);
        mb.add(menu);
        setJMenuBar(mb);

        // २. मुख्य पॅनेल आणि बॅकग्राउंड
        Container c = getContentPane();
        c.setLayout(new BorderLayout(10, 10));
        
        // ३. Header (स्वागत मेसेज)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185)); // Blue color
        JLabel lblWelcome = new JLabel("Admin Dashboard - Fees Management");
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblWelcome);
        c.add(headerPanel, BorderLayout.NORTH);

        // ४. बटण पॅनेल (GridLayout)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        baddfees = createModernButton("Add Fees", new Color(46, 204, 113));
        fstudent = createModernButton("Find Student", new Color(52, 152, 219));
        treport = createModernButton("Reports", new Color(155, 89, 182));
        fstructure = createModernButton("Fees Structure", new Color(230, 126, 34));

        buttonPanel.add(baddfees);
        buttonPanel.add(fstudent);
        buttonPanel.add(treport);
        buttonPanel.add(fstructure);
        c.add(buttonPanel, BorderLayout.CENTER);

        // ५. Action Listeners (जुने लॉजिक तसेच ठेवले आहे)
        baddfees.addActionListener(e -> new StudentFind());
        fstudent.addActionListener(e -> new StudentDetails());
        treport.addActionListener(e -> new Status());
        fstructure.addActionListener(e -> new Course());

        // ६. विंडो सेटिंग्स
        setSize(600, 450);
        setLocationRelativeTo(null); // स्क्रीनच्या मध्यभागी विंडो उघडेल
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // बटणांना मॉडर्न लूक देण्यासाठी एक वेगळी मेथड
    private JButton createModernButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        new AdminAfterLogin("Admin Dashboard");
    }
}