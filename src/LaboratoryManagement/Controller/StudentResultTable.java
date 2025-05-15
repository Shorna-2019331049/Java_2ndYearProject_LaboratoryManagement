package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import LaboratoryManagement.StudentResult;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentResultTable {

    @FXML
    private TableView<StudentResult> Table;

    @FXML
    private TableColumn<StudentResult, String> fullName;

    @FXML
    private TableColumn<StudentResult, Integer> grpNo;

    @FXML
    private TableColumn<StudentResult, String> regNo;

    @FXML
    private TableColumn<StudentResult, String> username;

    @FXML
    private TableColumn<StudentResult, String> contact;

    @FXML
    private Button Ok;

    @FXML
    private Button back;

    static String name,reg;
    static int grp;
    @FXML
    void initialize(){
        regNo.setCellValueFactory(new PropertyValueFactory<StudentResult,String>("regNo"));
        fullName.setCellValueFactory(new PropertyValueFactory<StudentResult,String>("fullName"));
        grpNo.setCellValueFactory(new PropertyValueFactory<StudentResult,Integer>("grpNo"));
        username.setCellValueFactory(new PropertyValueFactory<StudentResult,String>("username"));
        contact.setCellValueFactory(new PropertyValueFactory<StudentResult,String>("contact"));

        ObservableList<StudentResult> list= FXCollections.observableArrayList();
        Connection conn;
        DBhandler handler;
        PreparedStatement pst;
        handler=new DBhandler();
        conn=handler.getConnection();

        String stm="SELECT * from " + StudentResult.courseName +"_enrolled_students";
        try{
            pst=conn.prepareStatement(stm);
            //pst.setString(1, String.valueOf(1));
            //    System.out.println(StudentProfile.regNo);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                list.add(new StudentResult(rs.getString("regNo"),rs.getString("name"),rs.getInt("grpNo"),
                        rs.getString("username"),rs.getString("contact")));
                Table.setItems(list);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleOk(ActionEvent event) throws IOException {
        grp= Table.getSelectionModel().getSelectedItem().getGrpNo();
        name=Table.getSelectionModel().getSelectedItem().getUsername();
        reg=Table.getSelectionModel().getSelectedItem().getRegNo();
        Ok.getScene().getWindow().hide();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/LaboratoryManagement/FXML/resultDescription.fxml"));
        Parent root= loader.load();

        Scene sc=new Scene(root);

        ResultDescription controller=loader.getController();
        controller.initData(Table.getSelectionModel().getSelectedItem());

        Stage window=(Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(sc);
        window.show();
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        back.getScene().getWindow().hide();
        Stage newassgn=new Stage();
        Parent root=FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/TeacherProfile.fxml"));
        Scene sc=new Scene(root);
        newassgn.setScene(sc);
        newassgn.show();
    }



}