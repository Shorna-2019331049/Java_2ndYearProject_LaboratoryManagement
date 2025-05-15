package LaboratoryManagement;

public class StudentResult {
    private String regNo;
    private String fullName;
    private Integer grpNo;
    private String username;
    private String contact;
    public static String courseName,REGNO;

    public StudentResult(String regNo, String fullName, Integer grpNo, String username,String contact) {
        this.regNo = regNo;
        this.fullName = fullName;
        this.grpNo = grpNo;
        this.username = username;
        this.contact=contact;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getGrpNo() {
        return grpNo;
    }

    public void setGrpNo(Integer grpNo) {
        this.grpNo = grpNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public static void setCourseName(String courseName) {
        StudentResult.courseName = courseName;
    }

    public static void setREGNO(String REGNO) {
        StudentResult.REGNO = REGNO;
    }
}
