package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class assignmentAssign {
    public TextArea assignment;

    public TextField coursename;

    public DatePicker date;

    public TextField groupno;

    @FXML
    private Button back;

    @FXML
    private Button save;

    @FXML
    private TextField expName;

    public static Date DATE;


    public Connection conn;
    public DBhandler handler;
    public PreparedStatement pst;

    @FXML
    void handleBack(ActionEvent event) throws IOException {
        back.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/TeacherProfile.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();

    }
    @FXML
    private TextField mat;

    @FXML
    private Label err;

    @FXML
    private Label err1;

    @FXML
    private TextField expNo;

    boolean flag = true;

    double fMrk = 0.0;
    @FXML
    void expDetail(ActionEvent event) {
        if(flag){
            String ss = "SELECT * from courselist where courseName = ?";
            try {
                pst = conn.prepareStatement(ss);
                pst.setString(1, coursename.getText());
                ResultSet rs = pst.executeQuery();
                if (!rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("No such course is found!");
                    alert.show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String table = coursename.getText() + "_experiments";
            String s = "SELECT * from " + table + " where expNo = ?";
            try {
                pst = conn.prepareStatement(s);
                pst.setInt(1, Integer.parseInt(expNo.getText()));
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    expName.setText(rs.getString("expName"));
                    mat.setText(rs.getString("materials"));
                    assignment.setText(rs.getString("assignment"));
                    fMrk = rs.getDouble("fullMrk");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String s2 = "SELECT * from " + coursename.getText() + " where expName = ? and groupno = ?";
            try {
                pst = conn.prepareStatement(s2);
                pst.setString(1, (expName.getText()));
                pst.setString(2, groupno.getText());
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    err.setVisible(true);
                    PauseTransition pt = new PauseTransition(Duration.seconds(5));
                    pt.setOnFinished(ev -> {
                        err.setVisible(false);
                    });
                    pt.play();
                    expNo.setText("");
                    expName.setText("");
                    mat.setText("");
                    assignment.setText("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            err1.setVisible(true);
            PauseTransition pt = new PauseTransition(Duration.seconds(5));
            pt.setOnFinished(ev->{err1.setVisible(false);});
            pt.play();
            flag = false;
        }
    }

    @FXML
    void saveAssignment(ActionEvent event) throws SQLException, IOException {
        if(flag){
            String tableName = coursename.getText();
            String ss = "SELECT * from courselist where courseName = ?";
            try {
                pst = conn.prepareStatement(ss);
                pst.setString(1, tableName);
                ResultSet rs = pst.executeQuery();
                if (!rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("No such course is found!");
                    alert.show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String stm;
            stm = "INSERT INTO " + tableName + "(groupno,expName,materials,assignment,date)"
                    + "VALUES (?,?,?,?,?)";
            try {
                pst = conn.prepareStatement(stm);
                pst.setInt(1, Integer.parseInt(groupno.getText()));
                pst.setString(2, expName.getText());
                pst.setString(3, mat.getText());
                pst.setString(4, assignment.getText());
                pst.setDate(5, Date.valueOf(date.valueProperty().get()));
                //pst.setBlob(5, new FileInputStream((File) null));
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            DATE = Date.valueOf(date.valueProperty().get());
            System.out.println(DATE);
            save.getScene().getWindow().hide();
            Stage newassign = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/TeacherProfile.fxml"));
            Scene sc = new Scene(root);
            newassign.setScene(sc);
            newassign.show();
        }else{
            err1.setVisible(true);
            PauseTransition pt = new PauseTransition(Duration.seconds(5));
            pt.setOnFinished(ev->{err1.setVisible(false);});
            pt.play();
            flag = false;
        }
    }

    @FXML
    void validDate(ActionEvent event){
        ObservableList<LocalDate> validDate = FXCollections.observableArrayList();
        int cnt = 0;
        String ss = "SELECT * from courselist where courseName = ? and teacherName = ?";
        try {
            pst = conn.prepareStatement(ss);
            pst.setString(1,coursename.getText());
            pst.setString(2,TeacherProfile.tName);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("No such course is found!");
                alert.show();
            }
            else{
                cnt = Integer.parseInt(rs.getString("numExp"));
                expNo.setPromptText("Enter Experiment number from 1 to "+ cnt);
                groupno.setPromptText("Total "+ rs.getInt("numGrp")+" groups");
                String s = "SELECT count(*) FROM " + coursename.getText() + "_routine";
                pst = conn.prepareStatement(s);
                ResultSet r = pst.executeQuery();
                flag = true;
                if(r.next() && r.getInt("count(*)") != cnt){
                    String s1 = "SELECT count(*) FROM " + coursename.getText() + "_experiments";
                    pst = conn.prepareStatement(s);
                    ResultSet r1 = pst.executeQuery();
                    flag = false;
                    if(r1.next() && r1.getInt("count(*)") != cnt){
                        err1.setVisible(true);
                        PauseTransition pt = new PauseTransition(Duration.seconds(5));
                        pt.setOnFinished(ev->{err1.setVisible(false);});
                        pt.play();
                        flag = false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(flag){
            String s1 = "SELECT * FROM " + coursename.getText() + "_routine WHERE idCourse = ?";
            for (int i = 1; i <= cnt; i++) {
                try {
                    pst = conn.prepareStatement(s1);
                    pst.setInt(1, i);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        validDate.add(rs.getDate("date").toLocalDate());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            date.setDayCellFactory(new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(DatePicker datePicker) {
                    return new DateCell() {
                        public void updateItem(LocalDate item, boolean emp) {
                            super.updateItem(item, emp);
                            boolean valid = validDate.contains(item);
                            setDisable(!valid);
                            setStyle(valid ? "-fx-background-color: #1b0994;" : "");
                        }
                    };
                }
            });
        }
    }

    @FXML
    void initialize() {
        handler = new DBhandler();
        conn = handler.getConnection();
    }
}
