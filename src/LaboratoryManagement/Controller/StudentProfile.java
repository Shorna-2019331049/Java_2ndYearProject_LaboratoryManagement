package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import LaboratoryManagement.fine;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentProfile extends ProfileController{

    @FXML
    private ImageView studentImg;

    @FXML
    private Label already_enrolled;

    @FXML
    private Button logout;

    @FXML
    private Button assignment;

    @FXML
    private Button overview;

    @FXML
    private Button result;

    @FXML
    private Label err;

    @FXML
    private Label regnum;

    @FXML
    private Label university;

    @FXML
    private Label errAtd;

    @FXML
    private Label errDate;

    @FXML
    private Label errEnr;

    @FXML
    private Label errMrk;

    @FXML
    private Label successAtd;

    @FXML
    private TextField crsName;

    @FXML
    private Pane Attendance;

    @FXML
    private Button enroll;

    @FXML
    private Button enrollBtn;

    @FXML
    private TextField enrollCourseCode;

    @FXML
    private TextField enrollCourseName;

    @FXML
    private Pane enrollPane;

    @FXML
    private Label errCode;

    @FXML
    private Button record;

    @FXML
    private Pane overviewPane;

    @FXML
    private Button groupBtn;

    @FXML
    private Button attendance;

    @FXML
    private Pane Group;

    @FXML
    private TextField courseName;

    @FXML
    private TextField grpNo;

    @FXML
    private Label Members;
    @FXML
    private Label pdfLABEL;

    @FXML
    private Button download;
    @FXML
    private Label didnotPUBlish;
    @FXML
    private Pane ResultPane;

    @FXML
    private Pane Result2ndPane;

    @FXML
    private TextField COURSEname;

    @FXML
    private Pane recordPane;

    @FXML
    private Label errRec;
    @FXML
    private TextField recCrs;

    @FXML
    private TableView<fine> tablev;

    @FXML
    private TableColumn<fine, CheckBox> retCheck;

    @FXML
    private TableColumn<fine, String> status;

    @FXML
    private TableColumn<fine, String> mat;

    @FXML
    private TableColumn<fine, Date> expDat;

    @FXML
    private TableColumn<fine, String> expNam;

    static String course,fullName,regNo;
    static Integer grpno;

    String  Contact,COURSE;
    int cnt = 0;

    @FXML
    private Button rec;

    @FXML
    private Label success1;

    ObservableList<fine> list = FXCollections.observableArrayList();
    @FXML
    void calFineRec(ActionEvent event) {
        list.clear();
        tablev.getItems().clear();
        tablev.refresh();
            expNam.setCellValueFactory(new PropertyValueFactory<fine, String>("expNam"));
            expDat.setCellValueFactory(new PropertyValueFactory<fine, Date>("dat"));
            mat.setCellValueFactory(new PropertyValueFactory<fine, String>("mat"));
            status.setCellValueFactory(new PropertyValueFactory<fine, String>("status"));
            retCheck.setCellValueFactory(new PropertyValueFactory<fine, CheckBox>("retStatus"));
            String ss = "SELECT * from courselist where courseName = ?";
            try {
                pst = conn.prepareStatement(ss);
                pst.setString(1, recCrs.getText());
                ResultSet rs = pst.executeQuery();
                if (!rs.next()) {
                    errRec.setVisible(true);
                    PauseTransition pt = new PauseTransition(Duration.seconds(3));
                    pt.setOnFinished(ev -> {
                        errRec.setVisible(false);
                    });
                    pt.play();
                } else {
                    String s = "SELECT * FROM " + recCrs.getText() + "_enrolled_students where regNo = ?";
                    pst = conn.prepareStatement(s);
                    pst.setString(1, regnum.getText());
                    ResultSet r = pst.executeQuery();
                    while (r.next()) {
                        grpno = r.getInt("grpNo");
                    }
                    String s1 = "SELECT * FROM " + recCrs.getText() + " where groupno = ?";
                    String st = "Ongoing";
                    boolean retSt = false;
                    pst = conn.prepareStatement(s1);
                    pst.setInt(1, grpno);
                    ResultSet r1 = pst.executeQuery();
                    while (r1.next()) {
                        retSt = false;
                        if (r1.getBlob("report") == null && r1.getDate("date").before(Date.valueOf(LocalDate.now()))) {
                            st = "Missed";
                        } else if (r1.getDate("date").before(Date.valueOf(LocalDate.now()))) {
                            String s3 = "SELECT * FROM " + recCrs.getText() + "_report where expName = ? and regNo = ?";
                            pst = conn.prepareStatement(s3);
                            pst.setString(1, r1.getString("expName"));
                            pst.setString(2, regnum.getText());
                            ResultSet r3 = pst.executeQuery();
                            if (!r3.next()) {
                                st = "Missed";
                            } else {
                                st = "Done";
                                if (r1.getBoolean("status")) {
                                    retSt = true;
                                }
                            }
                            System.out.println(r1.getDate("date") + " " + Date.valueOf(LocalDate.now()));
                        } else if (r1.getDate("date").after(Date.valueOf(LocalDate.now()))) {
                            System.out.println(r1.getDate("date") + " " + Date.valueOf(LocalDate.now()));
                            st = "Upcomming";
                        }
                        System.out.println(r1.getString("materials"));
                        list.add(new fine(r1.getString("expName"), r1.getString("materials"),
                                st, r1.getDate("date"), retSt));
                        st = "Ongoing";
                        tablev.setItems(list);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    @FXML
    void recodAct(ActionEvent event) throws SQLException {
        cnt = 0;
        TableColumn<fine, CheckBox> col = retCheck;
        TableColumn<fine,String> st = status;
        List<Boolean> retSt = new ArrayList<>();
        for(fine item : tablev.getItems()){
            if(st.getCellObservableValue(item).getValue().equals("Missed")){
                retSt.add(true);
            }
            else
            retSt.add(col.getCellObservableValue(item).getValue().isSelected());
        }
        int k = 0;
        cnt = retSt.size();
        System.out.println(cnt);
        String s1 = "SELECT * FROM " + recCrs.getText() + " where groupno = ?";
        pst = conn.prepareStatement(s1);
        pst.setInt(1,grpno);
        ResultSet r = pst.executeQuery();
        while (r.next()){
            String s2 = "UPDATE " + recCrs.getText() + " SET status = ? where expName = ? and groupno = ?";
            pst = conn.prepareStatement(s2);
            pst.setBoolean(1,retSt.get(k));
            k++;
            pst.setString(2,r.getString("expName"));
            pst.setInt(3,grpno);
            pst.executeUpdate();
            System.out.println(k);
        }
        recordPane.setVisible(false);
        success1.setVisible(true);
        PauseTransition pt = new PauseTransition(Duration.seconds(3));
        pt.setOnFinished(ev -> {
            success1.setVisible(false);
        });
        pt.play();
    }
    @Override
    void show(){
        conn = handler.getConnection();
        String s1 = "SELECT * from student where username=? and password=?";
        try {
            pst = conn.prepareStatement(s1);
            pst.setString(1,loginController.USERID);
            pst.setString(2,loginController.PASS);
            ResultSet rs = pst.executeQuery();
            rs.next();
            fullName = rs.getString("name");
            Contact = rs.getString("contact");
            regNo = rs.getString("registration");
            username.setText(loginController.USERID);
            fullname.setText( rs.getString("name"));
            university.setText( rs.getString("university"));
            department.setText(rs.getString("department"));
            regnum.setText(rs.getString("registration"));
            contact.setText(rs.getString("contact"));
            InputStream imageData = rs.getBinaryStream("studentImg");
            Image img = new Image(imageData);
            studentImg.setImage(img);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TextField coursename;
    public static String reportName;
    @FXML
    void handleAssignment(ActionEvent event) throws Exception{
        overviewPane.setVisible(false);
        String ss = "SELECT * from courselist where courseName = ?";
        try {
            pst = conn.prepareStatement(ss);
            pst.setString(1,course);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()){
                err.setVisible(true);
            }
            else{
                reportName = "sample";
                String s1 = "SELECT * FROM " + coursename.getText() + "_enrolled_students where regNo = ?";
                pst = conn.prepareStatement(s1);
                pst.setString(1,regNo);
                ResultSet r = pst.executeQuery();
                while (r.next()){
                    grpno = r.getInt("grpNo");
                }
                assignment.getScene().getWindow().hide();
                Stage assgn = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/assignmentTable.fxml"));
                Scene sc = new Scene(root);
                assgn.setScene(sc);
                assgn.show();
                //assgn.setResizable(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleOverview(ActionEvent event) throws Exception{
        recordPane.setVisible(false);
        ResultPane.setVisible(false);
        enrollPane.setVisible(false);
        Attendance.setVisible(false);
        Group.setVisible(false);
        overviewPane.setVisible(true);
    }
    @FXML
/***/    void attendAction(ActionEvent event) throws IOException {
        overviewPane.setVisible(false);
        Attendance.setVisible(true);
        overviewPane.setVisible(false);
    }
    @FXML
    void groupAction(ActionEvent event) throws IOException {
        overviewPane.setVisible(false);
        Group.setVisible(true);
    }

    @FXML
    void showMembers(ActionEvent event) {
        String ss = "SELECT * FROM " + courseName.getText() +"_enrolled_students where grpNo = ? ";
        Connection conn;
        DBhandler handler;
        PreparedStatement pst;
        handler=new DBhandler();
        conn=handler.getConnection();
        try {
            pst = conn.prepareStatement(ss);
            pst.setInt(1, Integer.parseInt(grpNo.getText()));
            ResultSet rs = pst.executeQuery();
            StringBuilder mem = new StringBuilder();
            while (rs.next()){
                mem.append(rs.getString("name"));
                mem.append("\n");
            }
            Members.setText(mem.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PauseTransition pt = new PauseTransition(Duration.seconds(5));
        pt.setOnFinished(ev -> {
            Group.setVisible(false);
            overviewPane.setVisible(false);
        });
        pt.play();

    }
    ObservableList<LocalDate> validDate = FXCollections.observableArrayList();
    @FXML
        void crsCheck(ActionEvent event) {
        overviewPane.setVisible(false);
        Group.setVisible(false);
        String crs = crsName.getText();
        String ss = "SELECT * FROM courselist where courseName = ?";
        try {
            pst = conn.prepareStatement(ss);
            pst.setString(1,crs);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                String s = "SELECT * FROM " + crs + "_enrolled_students where regNo = ?";
                pst = conn.prepareStatement(s);
                pst.setString(1,regNo);
                ResultSet r1 = pst.executeQuery();
                if(r1.next()) {
                    validDate.clear();
                    String s1 = "SELECT * FROM " + crs + "_routine where idCourse = ?";
                    for (int i = 1; i <= Integer.parseInt(rs.getString("numExp")); i++) {
                        pst = conn.prepareStatement(s1);
                        pst.setInt(1, i);
                        ResultSet r = pst.executeQuery();
                        while (r.next()) {
                            validDate.add(r.getDate("date").toLocalDate());
                        }
                    }
                    if (validDate.contains(LocalDate.now())) {
                        String s3 = "SELECT * FROM attendance where course = ? and regNo = ? and date = ?";
                        pst = conn.prepareStatement(s3);
                        pst.setString(1, crs);
                        pst.setString(2, regNo);
                        pst.setDate(3,Date.valueOf(LocalDate.now()));
                        ResultSet r2 = pst.executeQuery();
                        if (r2.next()) {//already marked present
                            errMrk.setVisible(true);
                            PauseTransition pt = new PauseTransition(Duration.seconds(2));
                            pt.setOnFinished(ev->{errMrk.setVisible(false);Attendance.setVisible(false);});
                            pt.play();
                        } else {
                            String s2 = "INSERT INTO attendance(course,regNo,date,status) VALUES (?,?,?,?)";
                            try {
                                pst = conn.prepareStatement(s2);
                                pst.setString(1, crs);
                                pst.setString(2, regNo);
                                pst.setDate(3, Date.valueOf(LocalDate.now()));
                                pst.setString(4, "Present");
                                pst.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            crsName.setText("");
                            successAtd.setVisible(true);
                            PauseTransition pt = new PauseTransition(Duration.seconds(2));
                            pt.setOnFinished(ev -> {
                                successAtd.setVisible(false);
                                Attendance.setVisible(false);
                            });
                            pt.play();
                        }
                    }
                    else{//no classes today
                        errDate.setVisible(true);
                        PauseTransition pt = new PauseTransition(Duration.seconds(2));
                        pt.setOnFinished(ev->{errDate.setVisible(false);});
                        pt.play();
                    }
                }
                else{//notEnrolled
                    errEnr.setVisible(true);
                    PauseTransition pt = new PauseTransition(Duration.seconds(2));
                    pt.setOnFinished(ev->{errEnr.setVisible(false);});
                    pt.play();
                }
            }
            else{//invalid course
                errAtd.setVisible(true);
                PauseTransition pt = new PauseTransition(Duration.seconds(2));
                pt.setOnFinished(ev->{errAtd.setVisible(false);});
                pt.play();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void recordAction(ActionEvent event) {
        recordPane.setVisible(true);
        overviewPane.setVisible(false);
        enrollPane.setVisible(false);
        ResultPane.setVisible(false);
        Attendance.setVisible(false);
        Group.setVisible(false);
    }
    @FXML
    void handleResult(ActionEvent event) throws Exception{
        recordPane.setVisible(false);
        overviewPane.setVisible(false);
        Group.setVisible(false);
        enrollPane.setVisible(false);
        Attendance.setVisible(false);
        ResultPane.setVisible(true);
    }

    @FXML
    private TextField fineField;

    @FXML
    private Label fineLabel;

    @FXML
    private Pane finePane;

    @FXML
    private Button finepayButton;

    @FXML
    void courseNAMEaction(ActionEvent event) {

        String s1=COURSEname.getText();
        System.out.println(s1);
        String stm="SELECT * FROM "+s1+"_enrolled_students where regNo=? ";
        try {
            pst=conn.prepareStatement(stm);
            pst.setString(1,regNo);
            ResultSet rs=pst.executeQuery();
            rs.next();
            grpno=rs.getInt("grpNo");
            System.out.println("kj:"+grpno);
            System.out.println(regNo);

            if(rs.getDouble("grpFine")==0.0){

                if(rs.getBlob("report")==null){
                    didnotPUBlish.setVisible(true);
                    PauseTransition pt = new PauseTransition(Duration.seconds(3));
                    pt.setOnFinished(ev->{didnotPUBlish.setVisible(false);});
                    pt.play();
                }
                else{
                    pdfLABEL.setText(rs.getString("pdf"));
                    Result2ndPane.setVisible(true);
                }

            }
            else{
                finePane.setVisible(true);
                fineLabel.setText(rs.getString("grpFine"));
            }
        } catch (SQLException e ) {
            e.printStackTrace();
        }
    }

    @FXML
    void fineFieldAction(ActionEvent event) {
    }

    @FXML
    void handleFinePay(ActionEvent event) {

        Double curfine= Double.valueOf(fineLabel.getText());
        Double d= Double.valueOf(fineField.getText());
        String stm="SELECT * FROM "+COURSEname.getText()+"_enrolled_students where grpNo=? ";
        String s="UPDATE "+COURSEname.getText()+"_enrolled_students SET grpFine=? where grpNo=? ";
        try {
            pst=conn.prepareStatement(stm);
            pst.setInt(1, grpno);
            ResultSet rs=pst.executeQuery();

            while(rs.next()){
                PreparedStatement psts=conn.prepareStatement(s);
                psts.setDouble(1,curfine-d);
                psts.setInt(2,rs.getInt("grpNo"));
                psts.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        COURSEname.setText("");
        fineField.setText("");

        PauseTransition pt = new PauseTransition(Duration.seconds(3));
        pt.setOnFinished(ev->{
            finePane.setVisible(false);
            ResultPane.setVisible(false);});
        pt.play();
    }

    @FXML
    private Label pdfDOWNload;

    @FXML
    void DownloadAction(ActionEvent event) {
        String file_name="C:\\Users\\User\\Documents\\Student_result\\"+pdfLABEL.getText();
        String s="SELECT * FROM "+COURSEname.getText()+"_enrolled_students where regNo=? ";
        try{
            pst=conn.prepareStatement(s);
            pst.setString(1,regNo);
            ResultSet rs=pst.executeQuery();

            if(rs.next()){
                Blob b=rs.getBlob(11);
                byte[] byte_array=b.getBytes(1,(int)b.length());
                FileOutputStream fos=new FileOutputStream(file_name);
                fos.write(byte_array);
                fos.close();
            }
        } catch (SQLException | IOException e){
            e.printStackTrace();
        }
        pdfDOWNload.setVisible(true);

        PauseTransition pt = new PauseTransition(Duration.seconds(3));
        pt.setOnFinished(ev->{
            Result2ndPane.setVisible(false);
            ResultPane.setVisible(false);});
        pt.play();

        COURSEname.setText("");
    }

    @FXML
    void handlelogout(ActionEvent event) throws IOException {
        logout.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/login.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();
        //newassign.setResizable(false);
    }


    @FXML
    void coursenameAction(KeyEvent event) {
        coursename.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toLowerCase());
            return change;
        }));
        course = coursename.getText();
    }
    @FXML
    private Label success;

    @FXML
    void enrollMentAction(ActionEvent event) {

        String ss = "SELECT * from courselist where courseName = ? and courseCode = ?";
        try {
            pst = conn.prepareStatement(ss);
            pst.setString(1,enrollCourseName.getText());
            pst.setString(2,enrollCourseCode.getText());
            ResultSet rs = pst.executeQuery();
            if (!rs.next()){
                errCode.setVisible(true);
                PauseTransition pt = new PauseTransition(Duration.seconds(5));
                pt.setOnFinished(ev->{enrollPane.setVisible(false);});
                pt.play();
            }
            else{
                String st="SELECT * from "+enrollCourseName.getText()+"_Enrolled_Students where regNo=? and username=?";
                try{
                    pst=conn.prepareStatement(st);
                    pst.setString(1,regNo);
                    pst.setString(2,username.getText());
                    rs= pst.executeQuery();
                    if(rs.next()){
                        enrollPane.setVisible(false);
                        already_enrolled.setVisible(true);
                        PauseTransition pt = new PauseTransition(Duration.seconds(5));
                        pt.setOnFinished(ev->{already_enrolled.setVisible(false);});
                        pt.play();
                    }else{
                        String ss1 = "INSERT INTO " + enrollCourseName.getText() + "_Enrolled_Students" + "(regNo, name, contact, username) VALUES (?,?,?,?)";
                        try {
                            pst = conn.prepareStatement(ss1);
                            pst.setString(1,regNo);
                            pst.setString(2,fullName);
                            pst.setString(3,Contact);
                            pst.setString(4,username.getText());
                            pst.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }finally {
                            enrollPane.setVisible(false);
                            success.setVisible(true);
                            PauseTransition pt = new PauseTransition(Duration.seconds(5));
                            pt.setOnFinished(ev->{success.setVisible(false);});
                            pt.play();
                        }
                    }

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleEnrollment(ActionEvent event) {
        recordPane.setVisible(false);
        Group.setVisible(false);
        overviewPane.setVisible(false);
        ResultPane.setVisible(false);
        Attendance.setVisible(false);
        enrollPane.setVisible(true);
//        enrollBtn.setVisible(true);
//        enrollCourseCode.setVisible(true);
//        enrollCourseName.setVisible(true);
        errCode.setVisible(false);
    }

}

