package com.proj.fees.controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.proj.fees.dao.RegistrationDAO;
import com.proj.fees.model.Registration;
import java.awt.*;
import java.util.ArrayList;

public class StudentDetails extends JFrame {
    
    private JTextField sid;
    private JLabel stId, sSem, lTitle;
    private JButton b1;
    private JComboBox<String> semesters;

    public StudentDetails() {
        super("Search Student Profile");
        
        // 1. Setup Main Panel with modern layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 2. Title Label
        lTitle = new JLabel("FIND STUDENT RECORDS", SwingConstants.CENTER);
        lTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lTitle.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lTitle, gbc);

        // 3. Input Fields
        stId = new JLabel("Enter Student ID:");
        stId.setFont(new Font("Tahoma", Font.BOLD, 12));
        gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(stId, gbc);

        sid = new JTextField(15);
        sid.setFont(new Font("Tahoma", Font.PLAIN, 13));
        gbc.gridx = 1;
        mainPanel.add(sid, gbc);

        sSem = new JLabel("Select Semester:");
        sSem.setFont(new Font("Tahoma", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(sSem, gbc);

        String[] sem = getSemesters();
        semesters = new JComboBox<>(sem.length > 0 ? sem : new String[]{"No Semesters Found"});
        semesters.setBackground(Color.WHITE);
        gbc.gridx = 1;
        mainPanel.add(semesters, gbc);

        // 4. Search Button (Blue Style)
        b1 = new JButton("SEARCH DETAILS");
        b1.setBackground(new Color(52, 152, 219));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Arial", Font.BOLD, 12));
        b1.setFocusPainted(false);
        b1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        mainPanel.add(b1, gbc);

        // 5. Button Action Listener
        b1.addActionListener(e -> {
            String studentId = sid.getText().trim();
            String selectedSem = (String) semesters.getSelectedItem();

            if (studentId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Student ID!", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            RegistrationDAO dao = new RegistrationDAO();
            Registration r = dao.searchByIdSemDetail(studentId, selectedSem);
            
            if (r != null) {
                new Item(r); // Open the student profile view
            } else {
                JOptionPane.showMessageDialog(this, "No record found for ID: " + studentId + " in " + selectedSem, "Not Found", JOptionPane.ERROR_MESSAGE);
                sid.setText("");
            }
        });

        // 6. Window Settings
        setSize(400, 300);
        setLocationRelativeTo(null); // Open in center
        setResizable(false);
        setVisible(true);
    }

    public String[] getSemesters() {
        try {
            RegistrationDAO dao = new RegistrationDAO();
            ArrayList<String> semesterList = dao.getSemesterList();
            if (semesterList == null || semesterList.isEmpty()) return new String[0];
            return semesterList.toArray(new String[0]);
        } catch (Exception e) {
            return new String[0];
        }
    }

    public static void main(String[] args) {
        new StudentDetails();
    }
}