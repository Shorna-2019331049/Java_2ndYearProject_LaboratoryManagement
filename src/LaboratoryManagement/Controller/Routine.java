package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

public class Routine {

    private Connection conn;
    private DBhandler handler;
    private PreparedStatement pst;

    @FXML
    private TextField NumExp;

    @FXML
    private Label cnt;

    @FXML
    private Label code;

    @FXML
    private Label course;

    @FXML
    private Label credit;

    @FXML
    private DatePicker dat;

    @FXML
    private Button save;

    @FXML
    private Button back;

    int c = 0, avl = 0;

    @FXML
    void initialize(){
        handler = new DBhandler();
        conn = handler.getConnection();

        cnt.setText("0");
        course.setText(InstructorProfile.courseName);
        String ss = "SELECT * from courselist where courseName = ?";
        try {
            pst = conn.prepareStatement(ss);
            pst.setString(1,InstructorProfile.courseName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                code.setText(rs.getString("courseCode"));
                credit.setText(rs.getString("credit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Label err;

    @FXML
    void avlnum(ActionEvent event) {
        InstructorProfile.expNo = Integer.parseInt(NumExp.getText());
        String s1 = "UPDATE courselist SET numExp=? WHERE (courseName = ?)";
        try {
            pst = conn.prepareStatement(s1);
            pst.setString(1,NumExp.getText());
            pst.setString(2,InstructorProfile.courseName);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String ss = "SELECT count(*) FROM " + InstructorProfile.courseName + "_routine";
        try {
            pst = conn.prepareStatement(ss);
            ResultSet rs = pst.executeQuery();
            rs.next();
            c = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        avl = Integer.parseInt(NumExp.getText())-c;
        System.out.println(avl + " " + c);
        cnt.setText(String.valueOf(c));
    }
    @FXML
    void SaveDate(ActionEvent event) {
        if(c >= avl){
            System.out.println("Overflow!");
            err.setVisible(true);
            PauseTransition pt = new PauseTransition(Duration.seconds(5));
            pt.setOnFinished(ev->{err.setVisible(false);});
            pt.play();
        }
        else {
            String ss = "INSERT INTO " + InstructorProfile.courseName + "_routine(date) VALUES(?)";
            try {
                pst = conn.prepareStatement(ss);
                pst.setDate(1, Date.valueOf(dat.getValue()));
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dat.setValue(null);
            c++;
            cnt.setText(String.valueOf(c));
        }
    }

    @FXML
    void Back(ActionEvent event) throws IOException {
        back.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/InstructorProfile.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();
        newassign.setResizable(false);
    }
}
