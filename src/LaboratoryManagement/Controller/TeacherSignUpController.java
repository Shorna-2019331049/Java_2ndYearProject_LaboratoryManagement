package LaboratoryManagement.Controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class TeacherSignUpController extends SignUpController{

    @FXML
    private ImageView teacherImage;
    @FXML
    private Button browseImage;
    @FXML
    private TextField department;

    @FXML
    private TextField salary;

    @FXML
    private TextField designation;

    private String path;
    private File img;

    public void handlesignup() {
        PauseTransition pt = new PauseTransition();
        pt.setDuration(Duration.seconds(2));
        if((Objects.equals(username.getText(), "")) || (Objects.equals(name.getText(), "")) || (Objects.equals(designation.getText(), "")) || (Objects.equals(salary.getText(), ""))
                || (Objects.equals(contact.getText(), "")) || (Objects.equals(password.getText(), "")) || (Objects.equals(department.getText(), ""))
                || (Objects.equals(address.getText(), "")) || Err || img == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Fill up all information");
            alert.show();
        }
        else {
            pt.setOnFinished(ev -> System.out.println("SignUp successfully"));
            pt.play();
            //saving data
            String insert = "INSERT INTO teacher(name,username,password,contact,gender,department,address,designation,salary,teacherImg)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)";

            conn = handler.getConnection();

            try {
                pst = conn.prepareStatement(insert);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                pst.setString(1, name.getText());
                pst.setString(2, username.getText());
                pst.setString(3, password.getText());
                pst.setString(4, contact.getText());
                pst.setString(5, getGender());
                pst.setString(6, department.getText());
                pst.setString(7, address.getText());
                pst.setString(8, designation.getText());
                pst.setString(9, salary.getText());
                pst.setBlob(10,new FileInputStream(img));
                signedUp = true;

                pst.executeUpdate();

            } catch (SQLException | FileNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                name.setText(null);
                username.setText(null);
                password.setText(null);
                contact.setText(null);
                address.setText(null);
                department.setText(null);
                designation.setText(null);
                salary.setText(null);
                try {
                    if (salary.getText() == null)
                        handleBackHomepage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    void checkDuplicate(KeyEvent event) {
        super.checkDuplicate(event);
        super.stm = stm + "teacher where username=?";
        try {
            pst = conn.prepareStatement(super.stm);
            pst.setString(1,super.uniqueUsername);
            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Username is duplicate!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleBrowseImage(ActionEvent actionEvent) {
        FileChooser f=new FileChooser();
        f.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All", "*.*"),
                new FileChooser.ExtensionFilter("PNG","*.png"),new FileChooser.ExtensionFilter("JPG","*.jpg"),
                new FileChooser.ExtensionFilter("JPEG","*.jpeg"));
        f.setInitialDirectory(new File("C:\\Users\\User\\Documents\\java\\New Folder 11\\laboratoryManagement\\src\\LaboratoryManagement\\resource"));
        f.setInitialFileName("myImage.jpg");
        img = f.showOpenDialog(new Stage());
        if(img != null){
            Image imgth = new Image(img.toURI().toString());
            teacherImage.setImage(imgth);
            path = img.getAbsolutePath();
        }
    }
}
