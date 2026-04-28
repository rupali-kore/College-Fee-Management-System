package com.proj.fees.controller;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.proj.fees.dao.RegistrationDAO;

public class Course extends JFrame {
    
    private JComboBox<String> acourse;
    private JLabel scourse, lTitle;
    private JButton find;
    private String course = "";
    
    public Course() {
        super("Course Selection");
        
        // १. मुख्य पॅनेल आणि डिझाइन
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Off-white background
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // २. Title
        lTitle = new JLabel("FEES STRUCTURE LOOKUP", SwingConstants.CENTER);
        lTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lTitle.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lTitle, gbc);

        // ३. Select Course Label
        scourse = new JLabel("Select Course:");
        scourse.setFont(new Font("Tahoma", Font.BOLD, 13));
        gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(scourse, gbc);

        // ४. Courses Dropdown
        String[] courses = getCourses();
        if (courses.length == 0) {
            courses = new String[]{"No Courses Found"};
        }
        acourse = new JComboBox<>(courses);
        acourse.setBackground(Color.WHITE);
        acourse.setFont(new Font("Tahoma", Font.PLAIN, 13));
        gbc.gridx = 1;
        mainPanel.add(acourse, gbc);

        // ५. Find Button (Professional Blue)
        find = new JButton("VIEW STRUCTURE");
        find.setBackground(new Color(52, 152, 219));
        find.setForeground(Color.WHITE);
        find.setFont(new Font("Arial", Font.BOLD, 12));
        find.setFocusPainted(false);
        find.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        mainPanel.add(find, gbc);

        // ६. Action Listener
        find.addActionListener((e) -> {
            course = (String) acourse.getSelectedItem();
            if (course.equals("No Courses Found")) {
                JOptionPane.showMessageDialog(this, "Please check database connection!");
            } else {
                new FeesStructure(course); // FeesStructure विंडो उघडेल
            }
        });

        // ७. विंडो सेटिंग्स
        setSize(450, 250);
        setLocationRelativeTo(null); // सेंटरला उघडेल
        setResizable(false);
        setVisible(true);
    }

    public String[] getCourses() {
        try {
            RegistrationDAO dao = new RegistrationDAO();
            ArrayList<String> courseList = dao.getCourseList();
            
            if (courseList == null || courseList.isEmpty()) {
                return new String[0];
            }
            
            return courseList.toArray(new String[0]);
        } catch (Exception e) {
            System.out.println("Error fetching courses: " + e.getMessage());
            return new String[0];
        }
    }
    
    public static void main(String[] args) {
        // Look and Feel सेट करणे (Windows style साठी)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        new Course();
    }
}