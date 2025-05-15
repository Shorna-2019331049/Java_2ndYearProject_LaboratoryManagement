package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDescription {

    @FXML
    private Label Members;

    @FXML
    private TextField courseName;

    @FXML
    private TextField grpNo;

    @FXML
    private Button back;

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
    }


    @FXML
    void handleBack(ActionEvent event) throws IOException {
        back.getScene().getWindow().hide();
        Stage newassgn=new Stage();
        Parent root= FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/StudentProfile.fxml"));
        Scene sc=new Scene(root);
        newassgn.setScene(sc);
        newassgn.show();
    }
}
