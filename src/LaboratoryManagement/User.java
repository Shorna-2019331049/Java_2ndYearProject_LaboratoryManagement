package LaboratoryManagement;

import java.util.Date;

public class User {
    private String courseName;
    private String expName;
    private Integer groupNo;
    private String materials;
    private String report;
    private Date date;
    private String code;
    private String tname;
    private Integer expNum;

    public User(String courseName, String code, String tname, Integer expNum) {
        this.courseName = courseName;
        this.code = code;
        this.tname = tname;
        this.expNum = expNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public void setExpNum(Integer expNum) {
        this.expNum = expNum;
    }

    public String getTname() {
        return tname;
    }

    public Integer getExpNum() {
        return expNum;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public User(String courseName, String expName, Integer groupNo, String materials, String report, Date date) {
        this.courseName = courseName;
        this.expName = expName;
        this.groupNo = groupNo;
        this.materials = materials;
        this.report = report;
        this.date =date;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getExpName() {
        return expName;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public String getMaterials() {
        return materials;
    }

    public String getReport() {
        return report;
    }
}
