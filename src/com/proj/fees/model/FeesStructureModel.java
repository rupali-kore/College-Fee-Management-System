package com.proj.fees.model;

import java.io.Serializable;

/**
 * FeesStructureModel represents the mapping for the 'fees_structure' table.
 * Implements Serializable for data transfer safety.
 */
public class FeesStructureModel implements Serializable {
    
    // Serial version UID for Serializable classes
    private static final long serialVersionUID = 1L;

    private String courseName;
    private String semesterName;
    private String feesAmount;
    private String firstDateFeesSubm;

    // Default Constructor
    public FeesStructureModel() {
        super();
    }

    // Parameterized Constructor for quick object creation
    public FeesStructureModel(String courseName, String semesterName, String feesAmount, String firstDateFeesSubm) {
        this.courseName = courseName;
        this.semesterName = semesterName;
        this.feesAmount = feesAmount;
        this.firstDateFeesSubm = firstDateFeesSubm;
    }

    // --- Getters and Setters ---

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName != null ? courseName.trim() : null;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName != null ? semesterName.trim() : null;
    }

    public String getFeesAmount() {
        return feesAmount;
    }

    public void setFeesAmount(String feesAmount) {
        this.feesAmount = feesAmount;
    }

    public String getFirstDateFeesSubm() {
        return firstDateFeesSubm;
    }

    public void setFirstDateFeesSubm(String firstDateFeesSubm) {
        this.firstDateFeesSubm = firstDateFeesSubm;
    }

    // Override toString for better logging and debugging
    @Override
    public String toString() {
        return String.format("FeesStructure [Course=%s, Sem=%s, Amount=%s, Date=%s]", 
                             courseName, semesterName, feesAmount, firstDateFeesSubm);
    }
}