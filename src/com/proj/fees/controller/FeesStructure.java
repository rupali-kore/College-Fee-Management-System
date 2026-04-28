package com.proj.fees.controller;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.proj.fees.dao.RegistrationDAO;
import com.proj.fees.model.FeesStructureModel;

public class FeesStructure extends JFrame {

    public FeesStructure(String courseName) {
        super("Fees Structure for " + courseName);

        // 1. Table Column Headings
        String heading[] = {"SEMESTER NAME", "FEES AMOUNT", "LAST DATE FOR SUBMISSION"};
        
        // 2. Fetch Data from Database using DAO
        RegistrationDAO dao = new RegistrationDAO();
        ArrayList<FeesStructureModel> feesStructureList = dao.getFeesStructure(courseName);
        
        // 3. Convert ArrayList to 2D Array for JTable
        String data[][] = new String[feesStructureList.size()][3];
        int r = 0;
        for (FeesStructureModel model : feesStructureList) {
            data[r][0] = model.getSemesterName();
            data[r][1] = model.getFeesAmount();
            data[r][2] = model.getFirstDateFeesSubm();
            r++;
        }

        // 4. GUI Layout setup
        Container con = getContentPane();
        con.setLayout(new BorderLayout(10, 10));

        // 5. Modernizing the Table UI
        JTable datatable = new JTable(data, heading);
        datatable.setRowHeight(25);
        datatable.setFont(new Font("Tahoma", Font.PLAIN, 13));
        datatable.setGridColor(Color.LIGHT_GRAY);

        // Styling the Table Header
        JTableHeader header = datatable.getTableHeader();
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Tahoma", Font.BOLD, 14));

        JScrollPane jsp = new JScrollPane(datatable);
        
        // 6. Title Label styling
        JLabel lblTitle = new JLabel("FEES STRUCTURE DETAILS - " + courseName.toUpperCase(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lblTitle.setForeground(new Color(41, 128, 185));

        con.add(lblTitle, BorderLayout.NORTH);
        con.add(jsp, BorderLayout.CENTER);

        // 7. Window configurations
        setSize(850, 400);
        setLocationRelativeTo(null); // Open window in center of screen
        setVisible(true);
    }
}