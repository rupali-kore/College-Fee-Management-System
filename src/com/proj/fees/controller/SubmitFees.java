package com.proj.fees.controller;

import java.awt.*;
import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import com.proj.fees.dao.RegistrationDAO;
import com.proj.fees.model.Registration;
import com.proj.fees.utility.MailServer;
import javax.swing.border.CompoundBorder;

public class SubmitFees extends JFrame {
    private JTextField tsid, subDate, dueDate, tFee, fine, sFee, sName;
    private JButton bsubmit;
    private JLabel l1, l2, l3, l4, l5, l6, l7;

    public SubmitFees(Registration r) {
        super("Process Fee Payment");
        
        // 1. Container and Styling
        Container c = getContentPane();
        c.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), 
                new TitledBorder("Confirm Payment Details")));
        formPanel.setBackground(new Color(250, 250, 250));

        // 2. Initializing Fields
        tsid = createDisplayField(r.getId());
        sName = createDisplayField(r.getName());
        subDate = createDisplayField(r.getFeeSubmissionDate());
        dueDate = createDisplayField(r.getDueDate());
        tFee = createDisplayField(r.getTfee());
        fine = createDisplayField(r.getFine());
        sFee = createDisplayField(r.getSfee());

        bsubmit = new JButton("CONFIRM & PAY");
        bsubmit.setBackground(new Color(39, 174, 96)); // Success Green
        bsubmit.setForeground(Color.WHITE);
        bsubmit.setFont(new Font("Arial", Font.BOLD, 13));

        // 3. Adding to Panel
        formPanel.add(new JLabel(" Student ID:")); formPanel.add(tsid);
        formPanel.add(new JLabel(" Student Name:")); formPanel.add(sName);
        formPanel.add(new JLabel(" Submission Date:")); formPanel.add(subDate);
        formPanel.add(new JLabel(" Due Date:")); formPanel.add(dueDate);
        formPanel.add(new JLabel(" Semester Fee:")); formPanel.add(tFee);
        formPanel.add(new JLabel(" Late Fine:")); formPanel.add(fine);
        formPanel.add(new JLabel(" Total Payable:")); formPanel.add(sFee);
        formPanel.add(new JLabel("")); // Spacer
        formPanel.add(bsubmit);

        c.add(formPanel, BorderLayout.CENTER);

        // 4. Submit Action Logic
        bsubmit.addActionListener(e -> {
            // Change cursor to wait because mail takes time
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            int record = new RegistrationDAO().setFeesTransaction(r);
            
            if (record > 0) {
                String message = "Hi " + r.getName() + ",\n\n" + 
                                "Your fee of ₹" + r.getSfee() + " for " + r.getSemName() + 
                                " has been successfully submitted.\n\nThank you!";
                
                JOptionPane.showMessageDialog(this, "Fees submitted successfully! Sending email...");
                
                // Mail sending in background
                new Thread(() -> {
                    try {
                        new MailServer().sendMail(r.getEmailid(), "Fees Submission Receipt", message);
                        System.out.println("Email sent to: " + r.getEmailid());
                    } catch (MessagingException ex) {
                        System.err.println("Mail Error: " + ex.getMessage());
                    }
                }).start();
                
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Payment Failed! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            setCursor(Cursor.getDefaultCursor());
        });

        // 5. Window Settings
        setSize(450, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private JTextField createDisplayField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setFont(new Font("Tahoma", Font.BOLD, 12));
        field.setBackground(new Color(236, 240, 241));
        return field;
    }
}