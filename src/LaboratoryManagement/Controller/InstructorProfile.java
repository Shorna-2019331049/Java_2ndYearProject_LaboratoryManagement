package LaboratoryManagement.Controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InstructorProfile extends ProfileController{
    @FXML
    private Button Check;

    @FXML
    private TextField code;

    @FXML
    private TextField crs;

    @FXML
    private Label errAtd;

    @FXML
    private Pane ppane;

    @FXML
    private Label totalStd;

    @FXML
    private Label totalcls;

    @FXML
    private Label designation;
    @FXML
    private ImageView instructorImg;
    @FXML
    private Button markAtd;

    @FXML
    private TextField courseNam;

    @FXML
    private Button Exp;

    @FXML
    private Button routine;

    static String courseName;
    static int expNo;
    static ObservableList<Double> atdMarks = FXCollections.observableArrayList();
    @Override
    void show(){
        conn = handler.getConnection();
        String s1 = "SELECT * from instructor where username=? and password=?";
        try {
            pst = conn.prepareStatement(s1);
            pst.setString(1,loginController.USERID);
            pst.setString(2,loginController.PASS);
            ResultSet rs = pst.executeQuery();
            rs.next();
            username.setText("Username: " + loginController.USERID);
            fullname.setText("Name: " + rs.getString("name"));
            contact.setText("Contact: " + rs.getString("contact"));
            address.setText("Address: " + rs.getString("address"));
            designation.setText("Designation: " + rs.getString("designation"));
            InputStream imageData = rs.getBinaryStream("instructorImg");
            Image img=new Image(imageData);
            instructorImg.setImage(img);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ObservableList<String> reg = FXCollections.observableArrayList();
    List<Double> grpFine = new ArrayList<>();
    int p = 0;
    double marks = 0;

    @FXML
    private Pane atdPane;

    @FXML
    void MarkAtd(ActionEvent event) throws IOException {
        atdPane.setVisible(true);
        totalcls.setText("");
        totalStd.setText("");
    }

    @FXML
    private Label err;
    private boolean valid = true, validExp = false;
    @FXML
    void crs(ActionEvent event) {
        String ss = "SELECT * from courselist where courseName = ?";
        try {
            pst = conn.prepareStatement(ss);
            pst.setString(1,courseNam.getText());
            ResultSet rs = pst.executeQuery();
            if (!rs.next()){
                err.setVisible(true);
                valid = false;
                PauseTransition pt = new PauseTransition(Duration.seconds(5));
                pt.setOnFinished(ev->{err.setVisible(false);});
                pt.play();
            }
            else{
                valid = true;
                courseName = courseNam.getText();
                if(rs.getString("numExp") != null){
                    validExp = true;
                    expNo = Integer.parseInt(rs.getString("numExp"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Routine(ActionEvent event) throws IOException {

        crs(event);
        if(valid) {
            routine.getScene().getWindow().hide();
            Stage newassign = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/routine.fxml"));
            Scene sc = new Scene(root);
            newassign.setScene(sc);
            newassign.show();
        }
    }

    @FXML
    void setExp(ActionEvent event) throws IOException {
        crs(event);
        if(validExp){
            Exp.getScene().getWindow().hide();
            Stage newassign = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/experiments.fxml"));
            Scene sc = new Scene(root);
            newassign.setScene(sc);
            newassign.show();
        }
    }

    @FXML
    private Button back;
    @FXML
    void Back(ActionEvent event) throws IOException {
        back.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/HomePage.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();
    }

    public void chk(ActionEvent actionEvent) {
        String ss = "SELECT * from courselist where courseName = ? and courseCode=?";
        try {
            pst = conn.prepareStatement(ss);
            pst.setString(1,crs.getText());
            pst.setString(2,code.getText());
            ResultSet rs = pst.executeQuery();
            if (!rs.next()){
                errAtd.setVisible(true);
                PauseTransition pt = new PauseTransition(Duration.seconds(5));
                pt.setOnFinished(ev->{errAtd.setVisible(false);});
                pt.play();
            }
            else{
                grpFine.clear();
                grpFine.add(0.0);
                int grpNo = rs.getInt("numGrp");
                double fine = 0.0;
                String s4 = "SELECT * FROM " + crs.getText() + " where groupno = ?";
                pst = conn.prepareStatement(s4);
                for(int i = 1 ; i <= grpNo; i++){
                    pst.setInt(1, i);
                    ResultSet r4 = pst.executeQuery();
                    fine = 0.0;
                    while (r4.next()){
                        if(r4.getDate("date").after(Date.valueOf(LocalDate.now()))){
                            fine += 0.0;
                        }
                        else if(!r4.getBoolean("status")){
                            fine += 10.0;
                        }
                    }
                    grpFine.add(fine);
                }
                int cnt = 0;
                String s = "SELECT count(*) from " + crs.getText() +"_enrolled_students";
                pst = conn.prepareStatement(s);
                ResultSet r = pst.executeQuery();
                while (r.next()){
                    cnt = r.getInt("count(*)");
                }
                totalcls.setText(String.valueOf(rs.getInt("numExp")));
                totalStd.setText(String.valueOf(cnt));
                ppane.setVisible(true);
                String s1 = "SELECT * from "+ crs.getText() + "_enrolled_students where idCourse = ?";
                for(int i = 1; i <= cnt; i++){
                    pst = conn.prepareStatement(s1);
                    pst.setInt(1,i);
                    ResultSet r1 = pst.executeQuery();
                    while (r1.next()){
                        reg.add(r1.getString("regNo"));
                    }
                }
                for(String regNo : reg){
                    p = 0;
                    System.out.println(regNo);
                    String s2 = "SELECT * from attendance where regNo = ? and course = ?";
                    pst = conn.prepareStatement(s2);
                    pst.setString(1,regNo);
                    pst.setString(2,crs.getText());
                    ResultSet r2 = pst.executeQuery();
                    while (r2.next()){
                        p++;
                    }
                    String s5 = "SELECT * from "+ crs.getText() + "_enrolled_students where regNo = ?";
                    pst = conn.prepareStatement(s5);
                    pst.setString(1,regNo);
                    ResultSet r5 = pst.executeQuery();
                    int grpno = 0;
                    if(r5.next()){
                        grpno = r5.getInt("grpNo");
                    }
                    System.out.println(grpno + " " + grpFine.get(grpno));
                    String s3 = "UPDATE "+ crs.getText() +"_enrolled_students set atdMrk = ?, grpFine = ? where regNo = ?";
                    pst = conn.prepareStatement(s3);
                    marks = (40.0*p)/rs.getInt("numExp");
                    pst.setDouble(1,marks);
                    pst.setDouble(2,grpFine.get(grpno));
                    pst.setString(3,regNo);
                    InstructorProfile.atdMarks.add(marks);
                    pst.executeUpdate();
                }
                PauseTransition pt = new PauseTransition(Duration.seconds(5));
                pt.setOnFinished(ev->{
                    ppane.setVisible(false);
                    atdPane.setVisible(false);
                });
                pt.play();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
