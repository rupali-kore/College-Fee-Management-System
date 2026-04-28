package com.proj.fees.controller;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.proj.fees.dao.RegistrationDAO;
import com.proj.fees.model.Registration;
import com.proj.fees.properties.PropertyReader;

public class StudentFind extends JFrame {

    private JTextField sid;
    private JLabel stId, sSem, lTitle;
    private JButton find;
    private JComboBox<String> semesters;

    public StudentFind() {
        super("Fee Submission - Verification");

        // 1. Layout Setup
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 2. Title
        lTitle = new JLabel("FEE SUBMISSION PORTAL", SwingConstants.CENTER);
        lTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lTitle.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lTitle, gbc);

        // 3. Form Fields
        stId = new JLabel("Student ID:");
        gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(stId, gbc);

        sid = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(sid, gbc);

        sSem = new JLabel("Select Semester:");
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(sSem, gbc);

        String[] semList = {"Select Semester", "Semester-1", "Semester-2", "Semester-3", "Semester-4"};
        semesters = new JComboBox<>(semList);
        gbc.gridx = 1;
        mainPanel.add(semesters, gbc);

        // 4. Enter Button
        find = new JButton("VERIFY & PROCEED");
        find.setBackground(new Color(46, 204, 113));
        find.setForeground(Color.WHITE);
        find.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        mainPanel.add(find, gbc);

        // 5. Action Logic
        find.addActionListener(e -> processVerification());

        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void processVerification() {
        String studentId = sid.getText().trim();
        String selectedSem = (String) semesters.getSelectedItem();

        if (studentId.isEmpty() || selectedSem.equals("Select Semester")) {
            JOptionPane.showMessageDialog(this, "Please enter Student ID and select Semester");
            return;
        }

        RegistrationDAO dao = new RegistrationDAO();
        Registration r = dao.searchByIdSemDetail(studentId, selectedSem);
        
        // Query करताना खात्री करा की तुम्ही डेटाबेसमधील COURSE_NAME वापरताय
        int baseFees = dao.getFeesAmount(r.getCourseName(), r.getSemName());
        if(baseFees == 0) {
         // जर डेटाबेसमधून ० आले, तर याचा अर्थ क्वेरी नीट चालली नाही
        System.out.println("No fees found for: " + r.getCourseName() + " " + r.getSemName());
        }
        r.setTfee(String.valueOf(baseFees));

        if (r == null) {
            JOptionPane.showMessageDialog(this, "Invalid ID or Semester selection.");
            return;
        }

        // १. डेटाबेसमधून तारीख मिळवा
        String firstDateStr = dao.getFirstDateOfFeesSub(r.getCourseName(), r.getSemName());

        // २. Null Check (जर तारीख null आली तर डिफॉल्ट तारीख वापरा)
        if (firstDateStr == null || firstDateStr.isEmpty()) {
            System.out.println("Warning: Date not found in DB, using default.");
            firstDateStr = "2026-01-01"; 
        }
        
        // ही ओळ try ब्लॉकच्या वर असायला हवी
        int baseFees = dao.getFeesAmount(r.getCourseName(), r.getSemName());
        r.setTfee(String.valueOf(baseFees));

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
            Date date = sdf.parse(firstDateStr); 
            
            // ३. Calendar व्हेरिएबल्स तयार करा
            Calendar firstDateCal = Calendar.getInstance(); 
            firstDateCal.setTime(date);
            
            Calendar submissionDate = Calendar.getInstance(); 

            // ४. फी कॅल्क्युलेशन (calculateFineAndFees कॉल करताना तिन्ही पॅरामीटर्स दिले आहेत)
            Registration rFinal = calculateFineAndFees(firstDateCal, submissionDate, r);
            
            // ५. पुढील पेमेंट विंडो उघडणे
            new SubmitFees(rFinal); 
            this.dispose();

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Date Format Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

  private Registration calculateFineAndFees(Calendar firstDate, Calendar submissionDate, Registration r) {
    PropertyReader prop = new PropertyReader();
    
    int gracePeriod = Integer.parseInt(prop.getProperty("DURATION_OF_DAYS_WITHOUT_FINE"));
    int finePerDay = Integer.parseInt(prop.getProperty("FINE_AMT_PCT"));

    Calendar dueDate = (Calendar) firstDate.clone();
    dueDate.add(Calendar.DATE, gracePeriod);

    int fineAmt = 0;
    if (submissionDate.after(dueDate)) {
        long diffInMillis = submissionDate.getTimeInMillis() - dueDate.getTimeInMillis();
        long daysDiff = diffInMillis / (24 * 60 * 60 * 1000);
        fineAmt = (int) (daysDiff * finePerDay);
    }

    // --- हा बदल महत्त्वाचा आहे ---
    String tfeeStr = r.getTfee();
    int baseFee = 0;
    
    try {
        if (tfeeStr != null && !tfeeStr.isEmpty()) {
            baseFee = Integer.parseInt(tfeeStr.trim()); 
        }
    } catch (NumberFormatException e) {
        System.out.println("Error: Fees is not a valid number: " + tfeeStr);
        baseFee = 0; 
    }

    int totalToPay = baseFee + fineAmt;
    // ----------------------------

    SimpleDateFormat displaySdf = new SimpleDateFormat("dd/MMM/yyyy");
    r.setSfee(String.valueOf(totalToPay));
    r.setFine(String.valueOf(fineAmt));
    r.setFeeSubmissionDate(displaySdf.format(submissionDate.getTime()));
    r.setDueDate(displaySdf.format(dueDate.getTime()));
    r.setFeeStatus("Completed");

    return r;
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentFind());
    }
}