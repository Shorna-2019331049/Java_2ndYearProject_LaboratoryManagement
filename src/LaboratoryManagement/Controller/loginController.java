package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class loginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button login;

    @FXML
    private PasswordField password;

    @FXML
    private TextField Passtext;

    @FXML
    private CheckBox showPass;

    @FXML
    private Button signUp;

    @FXML
    private ProgressBar progress;

    @FXML
    private TextField userId;

    @FXML
    private Button Home;

    @FXML
    private ChoiceBox<String> typeOfUser;

    private Connection conn;
    private DBhandler handler;
    private PreparedStatement pst;
    static String USERID, PASS;

    @FXML
    public void Login(ActionEvent event) {

        progress.setVisible(true);
        PauseTransition pt = new PauseTransition();
        pt.setDuration(Duration.seconds(3.0));
        pt.setOnFinished(ev ->{});
        pt.play();

        USERID = userId.getText();
        PASS = password.getText();

        //retrive from database
        conn = handler.getConnection();
        String s1 = "SELECT * from SignUp where userId=? and password=?";

        if(typeOfUser.getSelectionModel().getSelectedItem().equals("Student")){
            s1 = "SELECT * from student where username=? and password=?";
            try {
                pst = conn.prepareStatement(s1);
                pst.setString(1,userId.getText());
                pst.setString(2,password.getText());
                ResultSet rs = pst.executeQuery();
            //    int cnt = 0;
                if(rs.next()){
                    System.out.println("Login successful");
                    openProfile("STUDENT");
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Username and password incorrect!");
                    alert.show();
                    progress.setVisible(false);
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(typeOfUser.getSelectionModel().getSelectedItem().equals("Teacher")){
            s1 = "SELECT * from teacher where username=? and password=?";
            try {
                pst = conn.prepareStatement(s1);
                pst.setString(1,userId.getText());
                pst.setString(2,password.getText());
                ResultSet rs = pst.executeQuery();
                int cnt = 0;
                if(rs.next()){
                    System.out.println("Login successful");
                    openProfile("TEACHER");
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Username and password incorrect!");
                    alert.show();
                    progress.setVisible(false);
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(typeOfUser.getSelectionModel().getSelectedItem().equals("Instructor")){
            s1 = "SELECT * from instructor where username=? and password=?";
            try {
                pst = conn.prepareStatement(s1);
                pst.setString(1,userId.getText());
                pst.setString(2,password.getText());
                ResultSet rs = pst.executeQuery();
                int cnt = 0;
                if(rs.next()){
                    System.out.println("Login successful");
                    openProfile("INSTRUCTOR");
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Username and password incorrect!");
                    alert.show();
                    progress.setVisible(false);
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void ShowPassword(ActionEvent event) {
        if(showPass.isSelected()){
            Passtext.setText(password.getText());
            Passtext.setVisible(true);
            password.setVisible(false);
            return;
        }
        password.setText(Passtext.getText());
        password.setVisible(true);
        Passtext.setVisible(false);
    }

    @FXML
    void SignUp(ActionEvent event) throws IOException {
        login.getScene().getWindow().hide();
        Stage signup = new Stage();

        if(typeOfUser.getSelectionModel().getSelectedItem().equals("Student")){
            Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/StudentSignUp.fxml"));
            Scene scene = new Scene(root);
            signup.setScene(scene);
            signup.setTitle("Student Signup Form");
        }
        else if(typeOfUser.getSelectionModel().getSelectedItem().equals("Teacher")){
            Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/TeacherSignUp.fxml"));
            Scene scene = new Scene(root);
            signup.setScene(scene);
            signup.setTitle("Teacher Signup Form");
        }
        else if(typeOfUser.getSelectionModel().getSelectedItem().equals("Instructor")){
            Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/InstructorSignUp.fxml"));
            Scene scene = new Scene(root);
            signup.setScene(scene);
            signup.setTitle("Instructor Signup Form");
        }
        signup.show();
    //    signup.setResizable(false);
    }

    @FXML
    void forgetPass(ActionEvent event) {

    }

    @FXML
    void backHome(ActionEvent event) throws IOException {
        signUp.getScene().getWindow().hide();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/HomePage.fxml"));
        Scene sc = new Scene(root);
        login.setScene(sc);
        login.show();
        //login.setResizable(false);
    }

    void openProfile(String type) throws IOException {
        if(type == "STUDENT"){
            signUp.getScene().getWindow().hide();
            Stage login = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/StudentProfile.fxml"));
            Scene sc = new Scene(root);
            login.setScene(sc);
            login.show();
            //login.setResizable(false);
        }
        else if(type == "TEACHER"){
            signUp.getScene().getWindow().hide();
            Stage login = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/TeacherProfile.fxml"));
            Scene sc = new Scene(root);
            login.setScene(sc);
            login.show();
        //    login.setResizable(false);
        }
        else if (type == "INSTRUCTOR"){
            signUp.getScene().getWindow().hide();
            Stage login = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/InstructorProfile.fxml"));
            Scene sc = new Scene(root);
            login.setScene(sc);
            login.show();
            //login.setResizable(false);
        }
    }

    @FXML
    void initialize() {
        progress.setVisible(true);
        PauseTransition pt = new PauseTransition(Duration.seconds(5));
        pt.setOnFinished(ev->{progress.setVisible(false);});
        pt.play();
        userId.setStyle("-fx-text-inner-color: black;");
        password.setStyle("-fx-text-inner-color: black;");
        handler = new DBhandler();
        typeOfUser.getItems().addAll("Student","Teacher","Instructor");
        typeOfUser.setValue("Student");
    }

}