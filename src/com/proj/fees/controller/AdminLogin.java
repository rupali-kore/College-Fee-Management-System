package com.proj.fees.controller;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.proj.fees.dao.RegistrationDAO; // १. हा Import विसरू नका
import java.sql.*; // २. SQL क्लासेससाठी

public class AdminLogin extends JFrame {
    private JLabel l1, l2, lTitle;
    private JTextField tid;
    private JPasswordField tpass;
    private JButton blogin;

    public AdminLogin(String title) {
        super(title);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(236, 240, 241)); 
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lTitle = new JLabel("ADMIN LOGIN", SwingConstants.CENTER);
        lTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lTitle.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lTitle, gbc);

        l1 = new JLabel("User ID:");
        gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(l1, gbc);

        tid = new JTextField(15);
        tid.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(tid, gbc);

        l2 = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(l2, gbc);

        tpass = new JPasswordField(15);
        tpass.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(tpass, gbc);

        blogin = new JButton("LOG IN");
        blogin.setBackground(new Color(52, 152, 219));
        blogin.setForeground(Color.WHITE);
        blogin.setFocusPainted(false);
        blogin.setFont(new Font("Arial", Font.BOLD, 14));
        blogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        mainPanel.add(blogin, gbc);

        blogin.addActionListener(e -> loginCheck());

        tpass.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) loginCheck();
            }
        });

        setSize(400, 350);
        setLocationRelativeTo(null); 
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loginCheck() {
        String id = tid.getText().trim();
        String pass = new String(tpass.getPassword()).trim();

        if (id.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both ID and Password!");
            return;
        }

        // --- डेटाबेस कनेक्शन आणि लॉगिन लॉजिक ---
        try {
            RegistrationDAO dao = new RegistrationDAO();
            Connection con = dao.getConnection(); // Connection ऑब्जेक्ट
            
            if (con != null) {
                // १. SQL Query - इथे तुमच्या टेबलचे नाव 'ADMIN_LOGIN' असल्याची खात्री करा
                String sql = "SELECT * FROM ADMIN_LOGIN WHERE USERNAME = ? AND PASSWORD = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                
                // २. व्हेरिएबल मॅपिंग (इथे 'id' वापरा 'user' ऐवजी)
                pst.setString(1, id); 
                pst.setString(2, pass);
                
                ResultSet rs = pst.executeQuery();
                
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    this.dispose(); // लॉगिन विंडो बंद करा
                    new AdminAfterLogin("Admin Dashboard"); // पुढची विंडो उघडा
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid User ID or Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
                
                con.close(); // कनेक्शन बंद करणे चांगले असते
            } else {
                JOptionPane.showMessageDialog(this, "Database Connection Failed! Check properties file.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
   public static void main(String[] args) {
    // रिकाम्या कंसाच्या ऐवजी त्यामध्ये एक "Title" टाका
    AdminLogin login = new AdminLogin("Fees Management System - Login");
}
}