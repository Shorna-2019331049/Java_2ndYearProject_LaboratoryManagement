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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Experiments {

    public Connection conn;
    public DBhandler handler;
    public PreparedStatement pst;

    @FXML
    private TextArea asg;

    @FXML
    private Button back;

    @FXML
    private Label course;

    @FXML
    private Label err;

    @FXML
    private Label err1;

    @FXML
    private Label expNum;

    @FXML
    private TextField experimentName;

    @FXML
    private TextField materials;

    @FXML
    private Button save;

    private int total = 0, cnt = 0;
    @FXML
    void initialize(){
        expNum.setText("Total Experiments: "+ InstructorProfile.expNo);
        course.setText(InstructorProfile.courseName);
        total = InstructorProfile.expNo;
    }

    @FXML
    private TextField mrk;

    @FXML
    void Backact(ActionEvent event) throws IOException {
        back.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/InstructorProfile.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();
    }

    @FXML
    void SaveAct(ActionEvent event) {
        handler = new DBhandler();
        conn = handler.getConnection();
        String s = "SELECT count(*) from " + InstructorProfile.courseName +"_experiments";
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
        if(cnt < total){
            String s1 = "INSERT INTO " + course.getText() + "_experiments(expName,expNo,materials,assignment,fullMrk) VALUES(?,?,?,?,?)";
            try {
                pst = conn.prepareStatement(s1);
                pst.setString(1, experimentName.getText());
                pst.setInt(2, cnt + 1);
                cnt++;
                pst.setString(3, materials.getText());
                pst.setString(4, asg.getText());
                pst.setDouble(5,Double.parseDouble(mrk.getText()));
                pst.executeUpdate();
                err1.setVisible(true);
                PauseTransition pt = new PauseTransition(Duration.seconds(5));
                pt.setOnFinished(ev->{err1.setVisible(false);});
                pt.play();
                experimentName.setText("");
                materials.setText("");
                asg.setText("");
                mrk.setText("");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            err.setVisible(true);
            PauseTransition pt = new PauseTransition(Duration.seconds(5));
            pt.setOnFinished(ev->{err.setVisible(false);});
            pt.play();
        }
    }

}
