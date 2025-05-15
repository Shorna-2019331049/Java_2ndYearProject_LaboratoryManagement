package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import LaboratoryManagement.User;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class assignmentDetails implements Initializable {

    @FXML
    private Button submitReport;

    @FXML
    private ImageView logo;

    @FXML
    private Button back;

    @FXML
    private Label coursenamelabel;

    @FXML
    private TextArea reportTextArea;

    @FXML
    private Label experimentnamelabel;

    @FXML
    private Label groupnolabel;

    @FXML
    private Label materialslabel;

    @FXML
    private Button choosefile;
    private User tableview;

    @FXML
    private TextArea reportDetails;

    @FXML
    private TextArea assignmentDetails;

    @FXML
    private Label dateLabel;

    final FileChooser fc=new FileChooser();
    File file;

    protected Connection conn;
    protected DBhandler handler;
    protected PreparedStatement pst;

    public assignmentDetails() {
    }

    public void  initData(User assignmenttable){
        tableview = assignmenttable;
        coursenamelabel.setText(tableview.getCourseName());
        groupnolabel.setText(tableview.getGroupNo().toString());
        experimentnamelabel.setText(tableview.getExpName());
        materialslabel.setText(tableview.getMaterials());
        reportTextArea.setText(tableview.getReport());
        dateLabel.setText(String.valueOf(tableview.getDate()));
    }

    @FXML
    void handleback(ActionEvent event) throws IOException {
        back.getScene().getWindow().hide();
        Stage newassign=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/AssignmentTable.fxml"));
        Scene sc = new Scene(root);
        newassign.setScene(sc);
        newassign.show();

    }

    @FXML
    void handleChooseFile(ActionEvent event) {
        fc.setTitle("Documents");

        // set the selected file or null if no file has been selected
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All","*.*"),
                new FileChooser.ExtensionFilter("Text","*.txt"),
                new FileChooser.ExtensionFilter("PDF","*.pdf"),
                new FileChooser.ExtensionFilter("PPTX","*.pptx"),
                new FileChooser.ExtensionFilter("WORD","*.DOCX")
        );
        fc.setInitialDirectory(new File("C:\\Users\\User\\Documents\\pdf"));
        fc.setInitialFileName("myReport.pdf");
        file=fc.showOpenDialog(new Stage());
        if(file == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("File is NULL!");
            alert.show();
        }
        else {
            reportTextArea.setText(file.getName());
            reportDetails.setText(experimentnamelabel.getText() + "\n" + file.getName() + "\n");
            reportDetails.setEditable(true);
        }
    }

    Date dueDay;

    @FXML
    private Label subLbl;
    @FXML
    void submitReportAction(ActionEvent event) {
        if(file == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Choose a file!");
            alert.show();
        }
        else {
            AssignmentTable.rptName = file.getName();
            String text = file.getName();

            String str = "", stm, tableName = tableview.getCourseName() + "_report";
            String s = "SELECT * FROM " + coursenamelabel.getText() + " where expName = ?";
            try {
                pst = conn.prepareStatement(s);
                pst.setString(1, experimentnamelabel.getText());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    dueDay = rs.getDate("date");
                    if (dueDay.before(Date.valueOf(LocalDate.now()))) {
                        str = "Turned in Late";
                    } else {
                        str = "Turned in Time";
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
/////eikhane check agei kichu joma dise naki
            String s4 = "SELECT * FROM " + tableName + " where regNo = ? and expName = ?";
            try {
                pst = conn.prepareStatement(s4);
                pst.setString(1, StudentProfile.regNo);
                pst.setString(2, tableview.getExpName());
                ResultSet r4 = pst.executeQuery();
                if (!r4.next()) {
                    stm = "INSERT INTO " + tableName + "(regNo,name,grpNo,expName,assignment,date,pdf,status)"
                            + "VALUES (?,?,?,?,?,?,?,?)";
                    try {
                        pst = conn.prepareStatement(stm);
                        pst.setString(1, StudentProfile.regNo);
                        pst.setString(2, StudentProfile.fullName);
                        pst.setInt(3, Integer.parseInt(tableview.getGroupNo().toString()));
                        pst.setString(4, tableview.getExpName());
                        pst.setString(5, AssignmentTable.asnDetail);
                        pst.setDate(6, (Date) tableview.getDate());
                        pst.setString(7, file.getName());
                        pst.setString(8, str);
                        //pst.setBlob(5, new FileInputStream((File) null));
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String update = "UPDATE " + coursenamelabel.getText() + " SET report = ? WHERE (groupno = ? and expName = ?)";
            try {
                pst = conn.prepareStatement(update);
                pst.setBlob(1, new FileInputStream(file));
                pst.setInt(2, Integer.parseInt(groupnolabel.getText()));
                pst.setString(3, experimentnamelabel.getText());
                pst.executeUpdate();
            } catch (SQLException | FileNotFoundException e) {
                e.printStackTrace();
            }

            String up = "UPDATE " + tableName + " SET report = ? , pdf = ? WHERE (grpNo = ? and expName = ?)";
            try {
                pst = conn.prepareStatement(up);
                pst.setBlob(1, new FileInputStream(file));
                pst.setString(2, file.getName());
                pst.setInt(3, Integer.parseInt(groupnolabel.getText()));
                pst.setString(4, experimentnamelabel.getText());
                pst.executeUpdate();
            } catch (SQLException | FileNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println(text);
            subLbl.setVisible(true);
            PauseTransition pt = new PauseTransition(Duration.seconds(3));
            pt.setOnFinished(ev -> {
                subLbl.setVisible(false);
            });
            pt.play();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = new DBhandler();
        conn = handler.getConnection();
        assignmentDetails.setText(AssignmentTable.asnDetail);
        assignmentDetails.setEditable(false);
        reportDetails.setEditable(false);
    }
}
