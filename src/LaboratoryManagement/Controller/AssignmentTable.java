package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import LaboratoryManagement.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AssignmentTable {

    @FXML
    private Button back;

    @FXML
    private TableView<User> Table;

    @FXML
    private TableColumn<User, String> courseName;

    @FXML
    private TableColumn<User, String> expName;

    @FXML
    private TableColumn<User, Integer> groupNo;

    @FXML
    private TableColumn<User, String> materials;

    @FXML
    private TableColumn<User, String> report;

    @FXML
    private TableColumn<User, Date> date;

    String userid = loginController.USERID;

    @FXML
    private Button DetailsAssignment;

    public static String asnDetail, rptName = "sample";
    @FXML
    void initialize() {
    //    System.out.println("assignment table: "+assignmentAssign.DATE);
        courseName.setCellValueFactory(new PropertyValueFactory<User,String>("courseName"));
        expName.setCellValueFactory(new PropertyValueFactory<User,String>("expName"));
        groupNo.setCellValueFactory(new PropertyValueFactory<User,Integer>("groupNo"));
        materials.setCellValueFactory(new PropertyValueFactory<User,String>("materials"));
        report.setCellValueFactory(new PropertyValueFactory<User,String>("report"));
        date.setCellValueFactory(new PropertyValueFactory<User,Date>("date"));

        ObservableList<User> list = FXCollections.observableArrayList();
        Connection conn;
        DBhandler handler;
        PreparedStatement pst;
        handler = new DBhandler();
        conn = handler.getConnection();

        String stm = "SELECT * from " + StudentProfile.course + " where groupno = ?";
        try {
            pst = conn.prepareStatement(stm);
            pst.setInt(1,StudentProfile.grpno);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                String s1="SELECT * FROM "+ StudentProfile.course +"_report where (regNo=? and expName=?) ";
                try{
                    pst = conn.prepareStatement(s1);
                    pst.setString(1,StudentProfile.regNo);
                    pst.setString(2,rs.getString("expName"));
                    System.out.println(StudentProfile.regNo);
                    ResultSet r=pst.executeQuery();

                    if(r.next()){
                        rptName = r.getString("pdf");
                    }
                    else{
                        rptName = "Sample";
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                list.add(new User(StudentProfile.course,rs.getString("expName"),StudentProfile.grpno,
                        rs.getString("materials"),rptName,rs.getDate("date")));
                Table.setItems(list);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleDetailsAssignment(ActionEvent event) throws IOException {
        String stm = "SELECT * from " + StudentProfile.course + " where groupno = ? and expName = ?";
        Connection conn;
        DBhandler handler;
        PreparedStatement pst;
        handler = new DBhandler();
        conn = handler.getConnection();
        try {
            pst = conn.prepareStatement(stm);
            pst.setInt(1,StudentProfile.grpno);
            pst.setString(2,Table.getSelectionModel().getSelectedItem().getExpName());
            ResultSet rs = pst.executeQuery();
            rs.next();
            asnDetail = rs.getString("assignment");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DetailsAssignment.getScene().getWindow().hide();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/LaboratoryManagement/FXML/assignmentDetails.fxml"));
        Parent root=loader.load();

        Scene sc=new Scene(root);

        assignmentDetails controller=loader.getController();
        controller.initData(Table.getSelectionModel().getSelectedItem());

        Stage window=(Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(sc);
        window.show();
       // window.setResizable(false);
    }

    @FXML
    void handleback(ActionEvent event) throws IOException {
        back.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/StudentProfile.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();
        //newassign.setResizable(false);
    }

}
