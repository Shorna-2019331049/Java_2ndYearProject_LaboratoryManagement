package LaboratoryManagement.Controller;

import LaboratoryManagement.StudentResult;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherProfile extends ProfileController{

    @FXML
    private Button logout;

    @FXML
    private Label address;

    @FXML
    private Label designation;

    @FXML
    private Button report;

    @FXML
    private Button assignment;

    @FXML
    private Button Course;

    @FXML
    private ImageView teacherImg;

    @FXML
    private TextField courseName;

    @FXML
    private Button rsSheet;

    @FXML
    private Button dwn;

    @FXML
    private TextField crsNam;

    static String tName;
    @Override
    void show(){
        conn = handler.getConnection();
        String s1 = "SELECT * from teacher where username=? and password=?";
        try {
            pst = conn.prepareStatement(s1);
            pst.setString(1,loginController.USERID);
            pst.setString(2,loginController.PASS);
            ResultSet rs = pst.executeQuery();
            rs.next();
            username.setText("Username: " + loginController.USERID);
            tName = rs.getString("name");
            fullname.setText("Name: " + rs.getString("name"));
            department.setText("Department: " + rs.getString("department"));
            contact.setText("Contact: " + rs.getString("contact"));
            address.setText("Address: " + rs.getString("address"));
            designation.setText("Designation: " + rs.getString("designation"));
            InputStream imageData = rs.getBinaryStream("teacherImg");
            Image img=new Image(imageData);
            teacherImg.setImage(img);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void addCourse(ActionEvent event) throws IOException {
        Course.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/Addcourse.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();
    //    newassign.setResizable(false);
    }

    @FXML
    void assignassignment(ActionEvent event) throws Exception{

        assignment.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/AssignmentAssign.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();
    //    newassign.setResizable(false);

    }

    @FXML
    void handlelogout(ActionEvent event) throws IOException {
        logout.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/login.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();
    //    newassign.setResizable(false);
    }

    @FXML
    void showReport(ActionEvent event){
        crsNam.setVisible(false);
        dwn.setVisible(false);
        asgGrpPane.setVisible(false);
        courseName.setVisible(true);
    }
    @FXML
    void showReports(ActionEvent event) throws IOException {
        String ss = "SELECT * from courselist where courseName = ? and teacherName = ?";
        try {
            pst = conn.prepareStatement(ss);
            pst.setString(1,courseName.getText());
            pst.setString(2,TeacherProfile.tName);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("No such course is found!");
                alert.show();
            }
            else{
                StudentResult.setCourseName(courseName.getText());
                logout.getScene().getWindow().hide();
                Stage newassign = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/studentResultTable.fxml"));
                Scene sc = new Scene(root);
                newassign.setScene(sc);
                newassign.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int cnt;
    String s;
    @FXML
    private Label resLbl;
    public void downloadRsSheet(ActionEvent actionEvent) {
        dwn.setVisible(false);
        courseName.setVisible(false);
        asgGrpPane.setVisible(false);
        crsNam.setVisible(true);
    }
    public void createRes(ActionEvent actionEvent) {
        s=crsNam.getText();

        String s = "SELECT count(*) from "+crsNam.getText()+"_enrolled_students";
        try {
            pst = conn.prepareStatement(s);
            ResultSet r = pst.executeQuery();
            while (r.next()){
                cnt = r.getInt("count(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(cnt);
        dwn.setVisible(true);
    }
    public void dwnRes(ActionEvent actionEvent) {
        System.out.println(s);
        String file_name="C:\\Users\\User\\Documents\\Total_Result\\"+s+".pdf";
        Document document = new Document();

        try{
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file_name));
            document.open();

            com.itextpdf.text.Image img= com.itextpdf.text.Image.getInstance("C:\\Users\\User\\Documents\\students_pdf\\sust_logo.png");
            img.setAbsolutePosition(250f,700f);
            img.scaleAbsolute(100,100);
            document.add(img);

            document.add(new Paragraph("\n\n\n\n\n\n "));

            Paragraph para=new Paragraph("Shahjalal University of Science and Technology");
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            para=new Paragraph("Computer Science and Engineering");
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            para=new Paragraph("Course Title: "+s);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            String stm="SELECT * FROM courselist where courseName=? ";
            pst=conn.prepareStatement(stm);
            pst.setString(1,s);
            ResultSet rs1=pst.executeQuery();
            rs1.next();
            para=new Paragraph("Course Code: "+rs1.getString("courseCode"));
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            document.add(new Paragraph("\n\n\n "));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100); // Width 100%
            table.setSpacingBefore(10f); // Space before table
            table.setSpacingAfter(10f); // Space after table

            PdfPCell cell1=new PdfPCell(new Paragraph("Registration No"));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell2=new PdfPCell(new Paragraph("Name"));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell3=new PdfPCell(new Paragraph("Grade"));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);

            String s1="SELECT * FROM "+s+"_enrolled_students where idCourse=? ";

            for(int i=1;i<=cnt;i++){
                pst = conn.prepareStatement(s1);
                pst.setInt(1, i);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    cell1 = new PdfPCell(new Paragraph(rs.getString("regNo")));
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    cell2 = new PdfPCell(new Paragraph(rs.getString("name")));
                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    cell3 = new PdfPCell(new Paragraph(rs.getString("grade")));
                    cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    table.addCell(cell1);
                    table.addCell(cell2);
                    table.addCell(cell3);
                }
            }

            document.add(table);
            document.close();
            writer.close();
        }catch(Exception e){
            System.out.println(e);
        }
        resLbl.setVisible(true);
        crsNam.setVisible(false);
        dwn.setVisible(false);
        PauseTransition pt = new PauseTransition(Duration.seconds(3));
        pt.setOnFinished(ev -> {
            resLbl.setVisible(false);
        });
        pt.play();
    }

    @FXML
    private TextField nsGrp;

    @FXML
    private Button groupAsg;

    @FXML
    private Label grpNs;

    @FXML
    private TextField crsNamGrp;

    @FXML
    private Pane asgGrpPane;

    @FXML
    private Button asgnGrp;


    @FXML
    void asgGrp(ActionEvent event) {
        crsNam.setVisible(false);
        dwn.setVisible(false);
        courseName.setVisible(false);
        asgGrpPane.setVisible(true);
    }

    @FXML
    void assignGroup(ActionEvent event) throws SQLException {
        String s = "SELECT * FROM courselist where courseName = ? and teacherName = ?";
        pst = conn.prepareStatement(s);
        pst.setString(1,crsNamGrp.getText());
        pst.setString(2,tName);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            int cnt = 0;
            String s1 = "SELECT count(*) from " + crsNamGrp.getText() + "_enrolled_students";
            pst = conn.prepareStatement(s1);
            ResultSet r = pst.executeQuery();
            if(r.next()){
               cnt = r.getInt("count(*)");
            }
            String s2 = "UPDATE " + crsNamGrp.getText() + "_enrolled_students SET grpNo = ? where idCourse = ?";
            pst = conn.prepareStatement(s2);
            int k = 0;
            for(int i = 1; i <= cnt;i+=Integer.parseInt(nsGrp.getText())){
                k++;
                for(int j = 0; j < Integer.parseInt(nsGrp.getText()); j++){
                    pst.setInt(1,k);
                    pst.setInt(2,i+j);
                    pst.executeUpdate();
                }
            }
            grpNs.setText(String.valueOf(k));
            String s3 = "UPDATE courselist SET numGrp = ? where courseName = ?";
            pst = conn.prepareStatement(s3);
            pst.setInt(1,k);
            pst.setString(2,crsNamGrp.getText());
            pst.executeUpdate();
            PauseTransition pt = new PauseTransition(Duration.seconds(3));
            pt.setOnFinished(ev->{asgGrpPane.setVisible(false);});
            pt.play();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("No such course is found!");
            alert.show();
        }
    }
}
