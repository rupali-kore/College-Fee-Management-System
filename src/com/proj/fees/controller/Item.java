package com.proj.fees.controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import com.proj.fees.model.Registration;
import java.awt.*;

public class Item extends JFrame {
    
    private JTextField tid, tname, temailid, tmobno, tgender, tfee, subfee, fine, subDate, tfeestatus, tadd, tdept, tsem, tsec;
    private JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14;
    private JButton bClose;

    public Item(Registration r) {
        super("Student Profile View");

        // 1. Setting up the Main Container
        Container c = getContentPane();
        c.setLayout(new BorderLayout(10, 10));
        
        // 2. Creating a Form Panel with GridBagLayout for better spacing
        JPanel formPanel = new JPanel(new GridLayout(14, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "STUDENT INFORMATION", 
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), new Color(41, 128, 185)));

        // 3. Initializing and Setting values to Fields
        tid = createReadOnlyField(r.getId());
        tname = createReadOnlyField(r.getName());
        temailid = createReadOnlyField(r.getEmailid());
        tmobno = createReadOnlyField(r.getMobno());
        tgender = createReadOnlyField(r.getGender());
        tdept = createReadOnlyField(r.getCourseName());
        tsem = createReadOnlyField(r.getSemName());
        tsec = createReadOnlyField(r.getSec());
        tfee = createReadOnlyField(r.getTfee());
        fine = createReadOnlyField(r.getFine());
        subfee = createReadOnlyField(r.getSfee());
        subDate = createReadOnlyField(r.getFeeSubmissionDate());
        tfeestatus = createReadOnlyField(r.getFeeStatus());
        tadd = createReadOnlyField(r.getAddress());

        // 4. Adding Labels and Fields to the Panel
        formPanel.add(new JLabel("  Student ID:")); formPanel.add(tid);
        formPanel.add(new JLabel("  Full Name:")); formPanel.add(tname);
        formPanel.add(new JLabel("  Email Address:")); formPanel.add(temailid);
        formPanel.add(new JLabel("  Mobile Number:")); formPanel.add(tmobno);
        formPanel.add(new JLabel("  Gender:")); formPanel.add(tgender);
        formPanel.add(new JLabel("  Department/Course:")); formPanel.add(tdept);
        formPanel.add(new JLabel("  Current Semester:")); formPanel.add(tsem);
        formPanel.add(new JLabel("  Section:")); formPanel.add(tsec);
        formPanel.add(new JLabel("  Total Admission Fee:")); formPanel.add(tfee);
        formPanel.add(new JLabel("  Fine Charges:")); formPanel.add(fine);
        formPanel.add(new JLabel("  Fees Submitted:")); formPanel.add(subfee);
        formPanel.add(new JLabel("  Submission Date:")); formPanel.add(subDate);
        formPanel.add(new JLabel("  Payment Status:")); formPanel.add(tfeestatus);
        formPanel.add(new JLabel("  Residential Address:")); formPanel.add(tadd);

        // 5. Bottom Panel for Buttons
        JPanel bottomPanel = new JPanel();
        bClose = new JButton("CLOSE");
        bClose.setBackground(new Color(231, 76, 60)); // Red color
        bClose.setForeground(Color.WHITE);
        bClose.addActionListener(e -> dispose());
        bottomPanel.add(bClose);

        c.add(formPanel, BorderLayout.CENTER);
        c.add(bottomPanel, BorderLayout.SOUTH);

        // 6. Window Settings
        setSize(500, 650);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);
        setVisible(true);
    }

    // Helper Method to create non-editable fields with a specific style
    private JTextField createReadOnlyField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBackground(new Color(245, 245, 245));
        field.setFont(new Font("Tahoma", Font.BOLD, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(), 
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        return field;
    }
}