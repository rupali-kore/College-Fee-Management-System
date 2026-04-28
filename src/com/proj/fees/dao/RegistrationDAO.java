package com.proj.fees.dao;

import java.sql.*;
import java.util.ArrayList;
import com.proj.fees.model.FeesStructureModel;
import com.proj.fees.model.Registration;
import com.proj.fees.properties.PropertyReader;
import com.proj.fees.dao.DBConnection; // तुमच्या प्रोजेक्टमधील अचूक पॅकेजचे नाव इथे टाका

public class RegistrationDAO {
    
    private PropertyReader props;
    private String driverName, url, username, password;
    private Connection con = null;

    public RegistrationDAO() {
        props = new PropertyReader();
        init();
    }
    
    private void init() {    
        driverName = (props.getProperty("driverName") != null) ? props.getProperty("driverName").trim() : "";
        url = (props.getProperty("url") != null) ? props.getProperty("url").trim() : "";
        username = (props.getProperty("username") != null) ? props.getProperty("username").trim() : "";
        password = (props.getProperty("password") != null) ? props.getProperty("password").trim() : "";
        getConnection(); 
    }

    public Connection getConnection() {
        try {
            if (con != null && !con.isClosed()) {
                return con;
            }
            Class.forName(driverName);
            con = DriverManager.getConnection(url, username, password);
            System.out.println("✅ DATABASE CONNECTED SUCCESSFULLY");
        } catch (Exception e) {
            System.out.println("❌ CONNECTION ERROR: " + e.getMessage());
        }
        return con;
    }

    private void checkConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            getConnection();
        }
    }

    public int getCountForSemName(String semName, String studentId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM FEESTRANSACTION WHERE SEMESTER = ? AND STUDENT_ID = ?";
        try {
            checkConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, semName);
            pst.setString(2, studentId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return count;
    }

    public int getFeesAmount(String courseName, String semName) {
    int fees = 0;
    // तुमच्या डेटाबेसनुसार क्वेरी (FEES_AMOUNT हा कॉलम नाव आहे)
    String query = "SELECT FEES_AMOUNT FROM FEESSTRUCTURE WHERE COURSE_NAME=? AND SEMESTER_NAME=?";
    
    try (Connection con = DBConnection.getConnection(); 
         PreparedStatement ps = con.prepareStatement(query)) {
        
        ps.setString(1, courseName);
        ps.setString(2, semName);
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String amt = rs.getString("FEES_AMOUNT");
            if (amt != null && !amt.isEmpty()) {
                fees = Integer.parseInt(amt);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return fees;
}

    public String getFirstDateOfFeesSub(String courseName, String semName) {
        String date = "";
        String sql = "SELECT FIRST_DATE_FEES_SUBMISSION FROM FEESSTRUCTURE WHERE COURSE_NAME = ? AND SEMESTER_NAME = ?";
        try {
            checkConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, courseName);
            pst.setString(2, semName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) date = rs.getString(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return date;
    }

    public ArrayList<String> getCourseList() {
        ArrayList<String> courseList = new ArrayList<>();
        String query = props.getProperty("FEEMANG.COURSELIST");
        try {
            checkConnection();
            if(con != null && query != null) {
                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) { courseList.add(rs.getString(1)); }
                }
            }
        } catch (SQLException e) { System.out.println("Error fetching courses: " + e.getMessage()); }
        return courseList;    
    }
    
    public ArrayList<String> getSemesterList() {
        ArrayList<String> semesterList = new ArrayList<>();
        String query = props.getProperty("FEEMANG.SEMESTERLIST");
        try {
            checkConnection();
            if(con != null && query != null) {
                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        semesterList.add(rs.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching semesters: " + e.getMessage());
        }
        return semesterList;
    }

    public Registration searchByIdSemDetail(String id, String sem) {
    Registration reg = null;
    try (Connection con = getConnection()) {
        String sql = "SELECT * FROM REGISTRATION WHERE STUDENT_ID = ? AND SEMESTER = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, id);
        pst.setString(2, sem);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            reg = new Registration();
            // तुमच्या Registration.java मॉडेलमधील नवीन मेथड्सनुसार बदल:
            // rs.next() च्या लूपमध्ये किंवा इफ ब्लॉक मध्ये
            //reg.setFirstDateSubmission(rs.getString("FIRST_DATE_FEES_SUBMISSION"));
            reg.setId(rs.getString("STUDENT_ID"));
            reg.setName(rs.getString("STUDENT_NAME"));
            reg.setCourseName(rs.getString("COURSE_NAME"));
            reg.setSemName(rs.getString("SEMESTER"));
            
            // डेटाबेसचे डबल/डेसिमल व्हॅल्यू स्ट्रिंगमध्ये कन्व्हर्ट करा कारण मॉडेलमध्ये String tfee आहे
            reg.setTfee(String.valueOf(rs.getDouble("TOTAL_FEES"))); 
            reg.setSfee(String.valueOf(rs.getDouble("PAID_FEES")));
            reg.setFeeStatus(rs.getString("STATUS"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return reg;
}

    public ArrayList<FeesStructureModel> getFeesStructure(String courseName) {
        ArrayList<FeesStructureModel> list = new ArrayList<>();
        String sql = "SELECT SEMESTER_NAME, FEES_AMOUNT, FIRST_DATE_FEES_SUBMISSION FROM FEESSTRUCTURE WHERE COURSE_NAME = ?";
        try {
            checkConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, courseName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                FeesStructureModel model = new FeesStructureModel();
                model.setSemesterName(rs.getString("SEMESTER_NAME"));
                model.setFeesAmount(rs.getString("FEES_AMOUNT"));
                model.setFirstDateFeesSubm(rs.getString("FIRST_DATE_FEES_SUBMISSION"));
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void closeEverything() {
        try {
            if (con != null && !con.isClosed()) con.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }
        
        public ArrayList<Registration> getStudentByFeeStatus(String status) {
    ArrayList<Registration> list = new ArrayList<>();
    // तुमच्या डेटाबेसमधील STATUS कॉलमचे नाव तपासा (उदा. 'Pending' किंवा 'Completed')
    String sql = "SELECT * FROM REGISTRATION WHERE STATUS = ?";
    try {
        checkConnection();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, status);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            Registration reg = new Registration();
            reg.setId(rs.getString("STUDENT_ID"));
            reg.setName(rs.getString("STUDENT_NAME"));
            reg.setCourseName(rs.getString("COURSE_NAME"));
            reg.setSemName(rs.getString("SEMESTER"));
            reg.setSfee(String.valueOf(rs.getDouble("PAID_FEES")));
            // तुमच्या डेटाबेसमध्ये जर तारखेचा कॉलम नसेल तर 'rs.getString' एरर देऊ शकते
            // जर कॉलम नसेल तर ही ओळ कमेंट करा किंवा "N/A" टाका
            reg.setFeeSubmissionDate("N/A"); 
            reg.setFeeStatus(rs.getString("STATUS"));
            list.add(reg);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
        
        public int setFeesTransaction(Registration reg) {
    int result = 0;
    // तुमच्या डेटाबेसमधील टेबल आणि कॉलमच्या नावानुसार ही क्वेरी तपासा
    String sql = "UPDATE REGISTRATION SET PAID_FEES = ?, STATUS = ?, SUBMISSION_DATE = ? WHERE STUDENT_ID = ? AND SEMESTER = ?";
    
    try (Connection con = getConnection(); 
         PreparedStatement pst = con.prepareStatement(sql)) {
        
        pst.setString(1, reg.getSfee());   // Submitted Fee
        pst.setString(2, reg.getFeeStatus()); // 'Completed' किंवा 'Pending'
        pst.setString(3, reg.getFeeSubmissionDate());
        pst.setString(4, reg.getId());
        pst.setString(5, reg.getSemName());
        
        result = pst.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}
    }
