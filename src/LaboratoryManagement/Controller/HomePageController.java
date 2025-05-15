package LaboratoryManagement.Controller;

import DBconnection.DBhandler;
import LaboratoryManagement.User;
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
import javafx.scene.shape.Arc;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomePageController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label contact1;

    @FXML
    private Label contact2;

    @FXML
    private Label contact3;

    @FXML
    private Label Appname;

    @FXML
    private Label Llogo;

    @FXML
    private Label Slogo;

    @FXML
    private ImageView img1;

    @FXML
    private ImageView img2;

    @FXML
    private ImageView img3;

    @FXML
    private ImageView appHomeImg;

    @FXML
    private Button contact;

    @FXML
    private Button dashboard;

    @FXML
    private Button SignUp;

    @FXML
    private Button login;

    @FXML
    private Arc logo;

    @FXML
    private ChoiceBox<String> typeOfUser;

    @FXML
    private Label welcomeText;

    @FXML
    private Label success;

    @FXML
    private TableView<User> list;

    @FXML
    private TableColumn<User,String> crsCode;

    @FXML
    private TableColumn<User, String> crsNam;

    @FXML
    private TableColumn<User, Integer> expNo;

    @FXML
    private TableColumn<User, String> tName;

    private Connection conn;
    private DBhandler handler;
    private PreparedStatement pst;
    @FXML
    void loginAction(ActionEvent event) throws IOException {
        Slogo.getScene().getWindow().hide();
        Stage logIN = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/LaboratoryManagement/FXML/login.fxml"));
        Scene sc = new Scene(root);
        logIN.setScene(sc);
        logIN.setTitle("Login");
        logIN.show();
        //logIN.setResizable(false);
    }


    @FXML
    void signUpAction(ActionEvent event) throws IOException, InterruptedException {

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
        Thread.sleep(500);
        Slogo.getScene().getWindow().hide();
        signup.show();
        //signup.setResizable(false);
    }
    @FXML
    void initialize() {
        if(SignUpController.signedUp){
            success.setVisible(true);
            PauseTransition pt = new PauseTransition(Duration.seconds(5));
            pt.setOnFinished(actionEvent -> {success.setVisible(false);});
            pt.play();
            SignUpController.signedUp = false;
        }
        contact.setStyle("-fx-background-color:#2D3447;-fx-border-color: #45cbf4;-fx-background-radius: 30;-fx-border-radius:30;");
        dashboard.setStyle("-fx-background-color:#2D3447;-fx-border-color: #45cbf4;-fx-background-radius: 30;-fx-border-radius:30;");
        typeOfUser.setStyle("-fx-background-color:" +
                " linear-gradient( #9cefe9, #2DD4BF)," +
                "radial-gradient(center 50% -40%, radius 200%,  #9cefe9 45%, #2DD4BF 50%);" +
                "-fx-effect: dropshadow( three-pass-box, rgba(0,0,0,0,4), 5, 0.0, 0, 1);" +
                "-fx-background-insets: 0, 1;" +
                "-fx-background-radius: 10,5,1;");

        typeOfUser.getItems().addAll("Student","Teacher","Instructor");
        typeOfUser.setValue("Student");
    }
    int cntc = 0;
    @FXML
    void showContact(ActionEvent event) {///
        if(cntc%2 == 0){
            contact1.setText("ayeshachowdhuryrimi@gmail.com");
            contact2.setText("shorna412@gmail.com");
            contact3.setText("Shahjalal University of Science and Technology");
            File imgPth = new File("C:\\Users\\User\\Documents\\java\\New Folder 11\\laboratoryManagement\\src\\LaboratoryManagement\\resource\\msgIcon.png");
            img1.setImage(new Image(imgPth.toURI().toString()));
            img1.setVisible(true);
            img2.setImage(new Image(imgPth.toURI().toString()));
            img2.setVisible(true);
            File imgPth1 = new File("C:\\Users\\User\\Documents\\java\\New Folder 11\\laboratoryManagement\\src\\LaboratoryManagement\\resource\\location.png");
            img3.setImage(new Image(imgPth1.toURI().toString()));
            img3.setVisible(true);
            contact1.setVisible(true);
            contact2.setVisible(true);
            contact3.setVisible(true);
        }
        else{
            img3.setVisible(false);
            img2.setVisible(false);
            img1.setVisible(false);
            contact1.setVisible(false);
            contact2.setVisible(false);
            contact3.setVisible(false);
        }
        cntc++;
    }

    ObservableList<User> crsList = FXCollections.observableArrayList();
    int dashClicked = 0;
    @FXML
    void showCrs(ActionEvent event) throws SQLException {
        handler = new DBhandler();
        conn = handler.getConnection();
        dashClicked++;
        if (dashClicked % 2 == 0) {
            list.setVisible(false);
        } else {
            crsList.clear();
            crsNam.setCellValueFactory(new PropertyValueFactory<User, String>("courseName"));
            crsCode.setCellValueFactory(new PropertyValueFactory<User, String>("Code"));
            tName.setCellValueFactory(new PropertyValueFactory<User, String>("tname"));
            expNo.setCellValueFactory(new PropertyValueFactory<User, Integer>("expNum"));
            list.setVisible(true);
            int cnt = 0;
            String s = "SELECT count(*) from courselist";
            try {
                pst = conn.prepareStatement(s);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    cnt = rs.getInt("count(*)");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(cnt);
            String ss = "SELECT * FROM courselist where id = ?";
            for (int i = 1; i <= cnt; i++) {
                pst = conn.prepareStatement(ss);
                pst.setInt(1, i);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString("courseName"));
                    crsList.add(new User(rs.getString("courseName"), rs.getString("courseCode"),
                            rs.getString("teacherName"), rs.getInt("numExp")));
                    list.setItems(crsList);
                }
            }
        }
    }
}
