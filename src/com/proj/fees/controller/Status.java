package com.proj.fees.controller;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.proj.fees.dao.RegistrationDAO;
import com.proj.fees.model.Registration;
import com.proj.fees.properties.PropertyReader;

// Apache POI Imports
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Status extends JFrame {
    
    private JRadioButton l1, l2;
    private JButton e1;
    private JLabel d1;

    public Status() {
        super("Report Generation System");
        
        // १. मुख्य पॅनेल सेटअप
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new java.awt.Color(245, 245, 245));
        setContentPane(mainPanel);

        // २. UI घटक (Components)
        d1 = new JLabel("Select Fees Payment Status:", SwingConstants.CENTER);
        d1.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14)); // Explicitly using AWT Font

        l1 = new JRadioButton("Submitted (Completed)");
        l2 = new JRadioButton("Not Submitted (Pending)");
        l1.setBackground(new java.awt.Color(245, 245, 245));
        l2.setBackground(new java.awt.Color(245, 245, 245));

        ButtonGroup rgroup = new ButtonGroup();
        rgroup.add(l1); 
        rgroup.add(l2);

        JPanel gpanel = new JPanel();
        gpanel.add(l1); 
        gpanel.add(l2);
        
        e1 = new JButton("GENERATE EXCEL REPORT");
        e1.setBackground(new java.awt.Color(39, 174, 96));
        e1.setForeground(java.awt.Color.WHITE);
        e1.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));

        // ३. लेआउटमध्ये ॲड करणे
        add(d1);
        add(gpanel);
        add(e1);

        e1.addActionListener(e -> generation());

        setSize(450, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void generation() {
        if(!l1.isSelected() && !l2.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a status first!");
            return;
        }
        
        boolean fSubmitted = l1.isSelected();
        boolean generateSuccess = getGeneratedReport(fSubmitted);
        
        if(generateSuccess) {
            JOptionPane.showMessageDialog(this, "Excel Report generated and saved successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Check Database, File Path, or Apache POI JARs.");
        }
    }

    public boolean getGeneratedReport(boolean fSubmitted) {
        boolean generateSuccess = false;
        // तुमच्या DB मध्ये 'Completed' आणि 'Pending' असेच स्टेटस आहेत ना ते तपासा
        String status = fSubmitted ? "Completed" : "Pending";
        
        PropertyReader props = new PropertyReader();
        String generateReportPath = props.getProperty("REPORTGENPATH");
        
        if (generateReportPath == null || generateReportPath.isEmpty()) {
            generateReportPath = System.getProperty("user.home") + File.separator + "Documents";
        }

        File directory = new File(generateReportPath);
        if (!directory.exists()) directory.mkdirs();

        String fileName = generateReportPath + File.separator + "report_" + (fSubmitted ? "submitted" : "pending") + ".xlsx";
        
        RegistrationDAO dao = new RegistrationDAO();
        ArrayList<Registration> studentList = dao.getStudentByFeeStatus(status);

        if(studentList == null || studentList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data found for selected status.");
            return false;
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Student Fees Report");
            
            // Header Row Style
            String[] columns = {"ID", "Name", "Course", "Semester", "Paid Fees", "Date", "Status"};
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            
            // Apache POI Font चा वापर (Explicitly)
            org.apache.poi.ss.usermodel.Font poiFont = workbook.createFont();
            poiFont.setBold(true);
            headerStyle.setFont(poiFont);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowCount = 1;
            for (Registration r : studentList) {
                Row row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(r.getId());
                row.createCell(1).setCellValue(r.getName());
                row.createCell(2).setCellValue(r.getCourseName());
                row.createCell(3).setCellValue(r.getSemName());
                row.createCell(4).setCellValue(r.getSfee());
                row.createCell(5).setCellValue(r.getFeeSubmissionDate());
                row.createCell(6).setCellValue(r.getFeeStatus());
            }

            for (int i = 0; i < columns.length; i++) sheet.autoSizeColumn(i);

            try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                workbook.write(outputStream);
                generateSuccess = true;
                
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(fileName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generateSuccess;
    }

    public static void main(String[] args) {
        new Status();
    }
}