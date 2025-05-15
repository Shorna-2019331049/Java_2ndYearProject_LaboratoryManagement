package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

public class Addcourse {
    @FXML
    private Button back;

    @FXML
    private TextField code;

    @FXML
    private TextField courseName;

    @FXML
    private TextField credit;

    @FXML
    private TextField teacher;

    @FXML
    private Button save;

    @FXML
    private Label err;

    static String CODE;

    public Connection conn;
    public DBhandler handler;
    public PreparedStatement pst;

    @FXML
    void saveCourse(ActionEvent event) throws IOException {

        courseName.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toLowerCase());
            return change;
        }));
        String s1 = "SELECT * FROM courselist where courseName = ? and courseCode = ?";
        try {
            pst = conn.prepareStatement(s1);
            pst.setString(1,courseName.getText());
            pst.setString(2,code.getText());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                err.setVisible(true);
                PauseTransition pt = new PauseTransition(Duration.seconds(5));
                pt.setOnFinished(ev->{err.setVisible(false);});
                pt.play();
                courseName.setText("");
                code.setText("");
                credit.setText("");
                teacher.setText("");
            }
            else{
                String stm = "INSERT INTO courselist(courseName,courseCode,teacherName,credit)"+ "VALUES (?,?,?,?)";
                    pst = conn.prepareStatement(stm);
                    pst.setString(1,courseName.getText());
                    pst.setString(2,code.getText());
                    pst.setString(3,teacher.getText());
                    pst.setString(4,credit.getText());
                    CODE=code.getText();
                    pst.executeUpdate();
                    System.out.println("Course Added");

                String tableName = courseName.getText();
                String st = " CREATE TABLE " + tableName
                        + "(idCourse INT NOT NULL AUTO_INCREMENT,"
                        +"groupno INT,"
                        +"expName VARCHAR(45) NOT NULL,"
                        +"materials VARCHAR(45) NOT NULL,"
                        + "assignment VARCHAR(255) NOT NULL,"
                        +"date DATETIME NOT NULL,"
                        +"report LONGBLOB,"
                        + "status BOOLEAN,"
                        + "PRIMARY KEY (idCourse))";

                    Statement sql = conn.createStatement();
                    sql.executeUpdate(st);
                String st1 = " CREATE TABLE " + tableName + "_enrolled_Students"
                        + "(idCourse INT NOT NULL AUTO_INCREMENT,"
                        +"regNo VARCHAR(45) NOT NULL,"
                        +"name VARCHAR(45) NOT NULL,"
                        +"grpNo INT,"
                        + "contact VARCHAR(255) NOT NULL,"
                        + "username VARCHAR(45) NOT NULL,"
                        + "atdMrk DOUBLE,"
                        + "labMrk DOUBLE,"
                        + "fullMrk DOUBLE,"
                        + "grade VARCHAR(45),"
                        + "report LONGBLOB,"
                        + "pdf VARCHAR(45),"
                        + "grpFine DOUBLE,"
                        + "PRIMARY KEY (idCourse))";
                    Statement sql1 = conn.createStatement();
                    sql1.executeUpdate(st1);

                String st2 = " CREATE TABLE " + tableName + "_report"
                        + "(idCourse INT NOT NULL AUTO_INCREMENT,"
                        +"regNo VARCHAR(45) NOT NULL,"
                        +"name VARCHAR(45) NOT NULL,"
                        +"grpNo INT,"
                        +"expName VARCHAR(45) NOT NULL,"
                        + "assignment VARCHAR(255) NOT NULL,"
                        +"date DATETIME NOT NULL,"
                        + "pdf VARCHAR(255) NOT NULL,"
                        + "report LONGBLOB,"
                        + "mrk DOUBLE,"
                        + "status VARCHAR(255) NOT NULL,"
                        + "PRIMARY KEY (idCourse))";
                    Statement sql2 = conn.createStatement();
                    sql2.executeUpdate(st2);

                String st3 = " CREATE TABLE " + tableName + "_routine"
                        + "(idCourse INT NOT NULL AUTO_INCREMENT,"
                        +"date DATETIME NOT NULL,"
                        + "PRIMARY KEY (idCourse))";
                    Statement sql3 = conn.createStatement();
                    sql3.executeUpdate(st3);

                String st4 = " CREATE TABLE " + tableName + "_experiments"
                        + "(idCourse INT NOT NULL AUTO_INCREMENT,"
                        +"expName VARCHAR(45) NOT NULL,"
                        +"expNo INT NOT NULL,"
                        +"materials VARCHAR(45) NOT NULL,"
                        + "assignment VARCHAR(255) NOT NULL,"
                        + "fullMrk DOUBLE NOT NULL,"
                        + "PRIMARY KEY (idCourse))";
                    Statement sql4 = conn.createStatement();
                    sql4.executeUpdate(st4);
                save.getScene().getWindow().hide();
                Stage newassign=new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/TeacherProfile.fxml"));
                Scene sc = new Scene(root);
                newassign.setScene(sc);
                newassign.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleback(ActionEvent event) throws IOException {
        back.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/TeacherProfile.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();
    }

    @FXML
    void initialize() {
        handler = new DBhandler();
        conn = handler.getConnection();
        teacher.setText(TeacherProfile.tName);
        teacher.setEditable(false);
    }
}
