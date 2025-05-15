package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import LaboratoryManagement.ReportANDPdf;
import LaboratoryManagement.StudentResult;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.sql.*;

public class ResultDescription {

    @FXML
    private Button uploadPDF;

    @FXML
    private TextArea filearea;
    @FXML
    private Button choose;
    @FXML
    private Button back;

    @FXML
    private Label groupLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label regLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private ImageView studentImg;

    @FXML
    private Label contactLabel;

    @FXML
    private Button Result;

    @FXML
    private Label pdfLabel;

    @FXML
    private Label pdfLabel1;

    @FXML
    private Label EXP_nameLabel;

    @FXML
    private TextField MARK_Label;

    @FXML
    private Button select;

    @FXML
    private TableView<ReportANDPdf> Table;

    @FXML
    private TableColumn<ReportANDPdf, String> ExpName;

    @FXML
    private TableColumn<ReportANDPdf, Double> mrk;

    @FXML
    private TableColumn<ReportANDPdf, String> status;

    @FXML
    private TableColumn<ReportANDPdf, Double> fmrk;
//    @FXML
//    private TableColumn<ReportANDPdf, String> Report;

    @FXML
    private TableColumn<ReportANDPdf, String> PDF;

    String file_name,pdf_name,exp,STATUS;
    static String text;
    int idx;
    double Fmark,TOTAL, curMrk = 0;


    private StudentResult tableview;

    protected Connection conn;
    protected DBhandler handler;
    protected PreparedStatement pst,pst1;



    public void  initData(StudentResult detailstable){
        tableview = detailstable;
        regLabel.setText(tableview.getRegNo());
        nameLabel.setText(tableview.getFullName());
        groupLabel.setText(tableview.getGrpNo().toString());
        usernameLabel.setText(tableview.getUsername());
        contactLabel.setText(tableview.getContact());

    }

    @FXML
    void initialize(){
        handler = new DBhandler();
        conn=handler.getConnection();

        String s1 = "SELECT * FROM student where username = ?";
        try {
            pst = conn.prepareStatement(s1);
            pst.setString(1,StudentResultTable.name);
            System.out.println(StudentResultTable.name);
            ResultSet rs = pst.executeQuery();
            rs.next();
            //          if(rs.next()) {
            InputStream imageData = rs.getBinaryStream("studentImg");
            Image img = new Image(imageData);
            studentImg.setImage(img);
            //         }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ExpName.setCellValueFactory(new PropertyValueFactory<ReportANDPdf,String>("expName"));
        PDF.setCellValueFactory(new PropertyValueFactory<ReportANDPdf,String>("pdf"));
        status.setCellValueFactory(new PropertyValueFactory<ReportANDPdf,String>("status"));
        mrk.setCellValueFactory(new PropertyValueFactory<ReportANDPdf,Double>("mrk"));
        fmrk.setCellValueFactory(new PropertyValueFactory<ReportANDPdf,Double>("fmrk"));
//        Report.setCellValueFactory(new PropertyValueFactory<ReportANDPdf,String>("report"));

        ObservableList<ReportANDPdf> list = FXCollections.observableArrayList();

        System.out.println(StudentResultTable.reg);
        String s="SELECT * FROM "+StudentResult.courseName+"_experiments where expName=? ";
        s1="SELECT * FROM "+StudentResult.courseName+" where groupno=? ";

        TOTAL=0.0;
        try{
            pst= conn.prepareStatement(s1);
            pst.setInt(1,StudentResultTable.grp);
            System.out.println(StudentResultTable.grp);
            ResultSet rs=pst.executeQuery();

            while(rs.next()){
                pst1=conn.prepareStatement(s);
                pst1.setString(1, rs.getString("expName"));
                ResultSet rs1=pst1.executeQuery();
                rs1.next();

                if(rs.getBlob("report") == null){
                    list.add(new ReportANDPdf(rs.getString("expName"),"NULL","Missing",0.0,rs1.getDouble("fullMrk")));
                    Table.setItems(list);
                }
                else{
                    String s2="SELECT * FROM "+StudentResult.courseName+"_report where expName=? and regNo=? ";
                    pst=conn.prepareStatement(s2);
                    pst.setString(1,rs.getString("expName"));
                    pst.setString(2,StudentResultTable.reg);
                    ResultSet rs2=pst.executeQuery();
                    if(!rs2.next()){
                        list.add(new ReportANDPdf(rs.getString("expName"),"NULL","Missing",0.0,rs1.getDouble("fullMrk")));
                        Table.setItems(list);
                    }
                    else{
                        list.add(new ReportANDPdf(rs2.getString("expName"), rs2.getString("pdf"), rs2.getString("status"), rs2.getDouble("mrk"), rs1.getDouble("fullMrk")));
                        Table.setItems(list);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private Label upLbl;

    @FXML
    private Label upLbl1;

    @FXML
    void handleSelect(ActionEvent event) {
        file_name="C:\\Users\\User\\Documents\\Teacher\\"+Table.getSelectionModel().getSelectedItem().getPdf();
        pdf_name=Table.getSelectionModel().getSelectedItem().getPdf();
        STATUS=Table.getSelectionModel().getSelectedItem().getStatus();
        Fmark=Table.getSelectionModel().getSelectedItem().getFmrk();
        curMrk = Table.getSelectionModel().getSelectedItem().getMrk();
        String s="SELECT * FROM "+StudentResult.courseName+"_report where regNo=? and expName=?";
        try{
            pst=conn.prepareStatement(s);
            pst.setString(1,StudentResultTable.reg);
            pst.setString(2,Table.getSelectionModel().getSelectedItem().getExpName());
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                Blob b=rs.getBlob(9);
                byte[] byte_array=b.getBytes(1,(int)b.length());
                FileOutputStream fos=new FileOutputStream(file_name);
                fos.write(byte_array);
                fos.close();
            }
        } catch (SQLException | IOException e){
            e.printStackTrace();
        }

        exp=Table.getSelectionModel().getSelectedItem().getExpName();
        EXP_nameLabel.setText(exp);
        MARK_Label.setVisible(true);
        idx=Table.getSelectionModel().getSelectedIndex();

    }

    @FXML
    void UpdateMark(ActionEvent event) {

        Table.getItems().set(idx,new ReportANDPdf(exp,pdf_name,STATUS,Integer.parseInt(MARK_Label.getText()),Fmark));
        String update="UPDATE " + StudentResult.courseName + "_report SET mrk = ? WHERE (regNo = ? and expName = ?)";
        try {
            pst = conn.prepareStatement(update);
            pst.setDouble(1, Integer.parseInt(MARK_Label.getText()));
            pst.setString(2, StudentResultTable.reg);
            pst.setString(3,exp);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        double marks = 0;
        TOTAL = 0.0;
        String ss = "SELECT * FROM " + StudentResult.courseName + " where groupno = ?";
        try {
            PreparedStatement pst2 = conn.prepareStatement(ss);
            pst2.setInt(1,Integer.parseInt(groupLabel.getText()));
            ResultSet rs = pst2.executeQuery();
            while (rs.next()){
                String expName = rs.getString("expName");
                String stm1 = "SELECT * FROM " + StudentResult.courseName + "_experiments where expName = ?";
                PreparedStatement pst = conn.prepareStatement(stm1);
                pst.setString(1, expName);
                ResultSet r3 = pst.executeQuery();
                if (r3.next()) {
                    TOTAL += r3.getDouble("fullMrk");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String stm="SELECT * from "+StudentResult.courseName+"_report where regNo=? ";
        try{
            pst=conn.prepareStatement(stm);
            pst.setString(1,StudentResultTable.reg);
            ResultSet rs= pst.executeQuery();
            while(rs.next()){
                marks +=rs.getDouble("mrk");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println(marks + " " + TOTAL);
        String s ="UPDATE " + StudentResult.courseName + "_enrolled_students SET labMrk = ? WHERE (regNo = ?)";
        try {
            pst = conn.prepareStatement(s);
//            marks-= curMrk;
//            marks += Integer.parseInt(MARK_Label.getText());
            marks = (60*marks)/TOTAL;
            pst.setDouble(1, marks);
            pst.setString(2, StudentResultTable.reg);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MARK_Label.setVisible(false);
    }

    @FXML
    void handleBack(ActionEvent event) throws IOException {
        back.getScene().getWindow().hide();
        Stage newassgn=new Stage();
        Parent root= FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/studentResultTable.fxml"));
        Scene sc=new Scene(root);
        newassgn.setScene(sc);
        newassgn.show();
    }

    @FXML
    void handleCreatePdf(ActionEvent event) {////ekhane pdf ta enrolled_students er ekta column blob add kore rakhte hobe.

        String file_name="C:\\Users\\User\\Documents\\students_pdf\\"+StudentResult.courseName+"_"+regLabel.getText()+".pdf";

        int total=0;
        String stm="SELECT * from "+StudentResult.courseName+"_report where regNo=? ";
        try{
            pst=conn.prepareStatement(stm);
            pst.setString(1,StudentResultTable.reg);
            ResultSet rs= pst.executeQuery();
            while(rs.next()){
                total+=rs.getInt("mrk");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println(total);

        Document document = new Document();

        try{
            PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(file_name));
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

            para=new Paragraph("Course Title: "+StudentResult.courseName);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            stm="SELECT * FROM courselist where courseName=? ";
            pst=conn.prepareStatement(stm);
            pst.setString(1,StudentResult.courseName);
            System.out.println(StudentResult.courseName);
            ResultSet rs=pst.executeQuery();
            rs.next();
            para=new Paragraph("Course Code: "+rs.getString("courseCode"));
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            document.add(new Paragraph("\n\n\n "));

            document.add(new Paragraph("    Name: "+nameLabel.getText()));
            document.add(new Paragraph("    Registration: "+regLabel.getText()));
            stm="SELECT * FROM "+StudentResult.courseName+"_enrolled_students where regNo=? ";
            pst=conn.prepareStatement(stm);
            pst.setString(1,regLabel.getText());
            rs=pst.executeQuery();
            rs.next();
            if(rs.getString("grade") != null)
            {
                double d=rs.getDouble("atdMrk");
                d=Double.parseDouble(String.format("%.3f", d));
                document.add(new Paragraph("    Attendance Mark(40): " + d));

                double lb=rs.getDouble("labMrk");
                lb=Double.parseDouble(String.format("%.3f", lb));
                document.add(new Paragraph("    Total Lab Mark(60): " + lb));

                double fl=d+lb;
                fl=Double.parseDouble(String.format("%.3f", fl));
                document.add(new Paragraph("    Full Mark(100): " + fl));
                document.add(new Paragraph("    Grade: " + rs.getString("grade")));

                System.out.println(fl);

                System.out.println(rs.getString("grade"));
                if (rs.getString("grade").equals("A+")) {
                    document.add(new Paragraph("    Remarks: Excellent"));
                } else if (rs.getString("grade").equals("A") || rs.getString("grade").equals("A-")) {
                    document.add(new Paragraph("    Remarks: Satisfactory"));
                } else if (rs.getString("grade").equals("B+") || rs.getString("grade").equals("B") || rs.getString("grade").equals("B-")) {
                    document.add(new Paragraph("    Remarks: Needs Improvement"));
                } else if (rs.getString("grade").equals("C+") || rs.getString("grade").equals("C") || rs.getString("grade").equals("C-")) {
                    document.add(new Paragraph("    Remarks: Not Satisfactory"));
                } else {
                    document.add(new Paragraph("    Remarks: Fail"));
                }

                document.close();
                writer.close();

                pdfLabel.setVisible(true);
                PauseTransition pt = new PauseTransition(Duration.seconds(3));
                pt.setOnFinished(ev -> {
                    pdfLabel.setVisible(false);
                });
                pt.play();
            }
            else{
                pdfLabel1.setVisible(true);
                PauseTransition pt = new PauseTransition(Duration.seconds(3));
                pt.setOnFinished(ev -> {
                    pdfLabel1.setVisible(false);
                });
                pt.play();
            }

        }catch(Exception e){
            System.out.println(e);
        }

    }


    final FileChooser fc=new FileChooser();
    File file;

    @FXML
    void chooseFile(ActionEvent event) {
        fc.setTitle("Documents");

        // set the selected file or null if no file has been selected
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All","*.*"),
                new FileChooser.ExtensionFilter("Text","*.txt"),
                new FileChooser.ExtensionFilter("PDF","*.pdf"),
                new FileChooser.ExtensionFilter("PPTX","*.pptx"),
                new FileChooser.ExtensionFilter("WORD","*.DOCX")
        );
        fc.setInitialDirectory(new File("C:\\Users\\User\\Documents\\students_pdf"));
        fc.setInitialFileName("myReport.pdf");
        file=fc.showOpenDialog(new Stage());
        filearea.setText(file.getName());
    }


    @FXML
    void UploadPDFAction(ActionEvent event) {

        text=file.getName();
        String update="UPDATE " + StudentResult.courseName + "_enrolled_students SET report = ? , pdf=? WHERE regNo=? ";
        try {
            pst = conn.prepareStatement(update);
            pst.setBlob(1, new FileInputStream(file));
            pst.setString(2,text);
            pst.setString(3, StudentResultTable.reg);
            pst.executeUpdate();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        upLbl.setVisible(true);
        PauseTransition pt = new PauseTransition(Duration.seconds(3));
        pt.setOnFinished(ev -> {
            upLbl.setVisible(false);
        });
        pt.play();
    }

    String calGrade(double marks){
        if(marks >= (80.0)){
            return "A+";
        }
        else if(marks < 80.0 && marks >= 75.0){
            return "A";
        }
        else if(marks < 75.0 && marks >= 70.0){
            return "A-";
        }
        else if(marks < 70.0 && marks >= 65.0){
            return "B+";
        }
        else if(marks < 65.0 && marks >= 60.0){
            return "B";
        }
        else if(marks < 60.0 && marks >= 55.0){
            return "B-";
        }
        else if(marks < 55.0 && marks >= 50.0){
            return "C+";
        }
        else if(marks < 50.0 && marks >= 45.0){
            return "C";
        }
        else if(marks < 45.0 && marks >= 40.0){
            return "C-";
        }
        else {
            return "F";
        }
    }
    @FXML
    void handleResult(ActionEvent event) {
        upLbl1.setVisible(true);
        PauseTransition pt = new PauseTransition(Duration.seconds(3));
        pt.setOnFinished(ev -> {
            upLbl1.setVisible(false);
        });
        pt.play();
        Double total=0.0;
        String stm="SELECT * from "+StudentResult.courseName+"_enrolled_students where regNo=? ";
        try{
            pst=conn.prepareStatement(stm);
            pst.setString(1,StudentResultTable.reg);
            ResultSet rs= pst.executeQuery();
            while(rs.next()){
                total+=(rs.getDouble("atdMrk") + rs.getDouble("labMrk"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        String s ="UPDATE " + StudentResult.courseName + "_enrolled_students SET fullMrk = ? WHERE (regNo = ?)";
        try {
            pst = conn.prepareStatement(s);
            pst.setDouble(1, total);
            pst.setString(2, StudentResultTable.reg);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(total);
        String grd = calGrade(total);
        String s1 ="UPDATE " + StudentResult.courseName + "_enrolled_students SET grade = ? WHERE (regNo = ?)";
        try {
            pst = conn.prepareStatement(s1);
            pst.setString(1, grd);
            pst.setString(2, StudentResultTable.reg);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}