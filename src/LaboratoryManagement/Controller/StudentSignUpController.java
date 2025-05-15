package LaboratoryManagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class StudentSignUpController extends SignUpController {

    @FXML
    private Button save;
    @FXML
    private ImageView imageInsrt;
    @FXML
    private Button browse;
    @FXML
    private AnchorPane anchor;
    @FXML
    private TextField department;
    @FXML
    private TextField registration;
    @FXML
    private TextField group;
    @FXML
    private TextField university;
    private String path;
    private File img;

    public void handlesignup() {
        if((Objects.equals(username.getText(), "")) || (Objects.equals(name.getText(), "")) || (Objects.equals(registration.getText(), ""))
                || (Objects.equals(contact.getText(), "")) || (Objects.equals(password.getText(), "")) || (Objects.equals(department.getText(), "")) || (Objects.equals(university.getText(), ""))
                || (Objects.equals(address.getText(), "")) || Err || img == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Fill up all information");
            alert.show();
        }
        else {

            //saving data
            String insert = "INSERT INTO student(name,registration,contact,gender,address,department,university,username,password,studentImg)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)";

            try {
                pst = conn.prepareStatement(insert);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                pst.setString(1, name.getText());
                pst.setString(2, registration.getText());
                pst.setString(3, contact.getText());
                pst.setString(4, getGender());
                pst.setString(5, address.getText());
                pst.setString(6, department.getText());
                pst.setString(7, university.getText());
                pst.setString(8, username.getText());
                pst.setString(9, password.getText());
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
                university.setText(null);
                registration.setText(null);
                try {
                    if (registration.getText() == null)
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
        super.stm = stm + "student where username=?";
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

    @FXML
    void browseImg(ActionEvent event) {
        FileChooser f = new FileChooser();
        f.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All","*.*"),
                new FileChooser.ExtensionFilter("PNG","*.png"),
                new FileChooser.ExtensionFilter("JPG","*.jpg"),
                new FileChooser.ExtensionFilter("JPEG","*.jpeg"));
        f.setInitialDirectory(new File("C:\\Users\\User\\Documents\\java\\New Folder 11\\laboratoryManagement\\src\\LaboratoryManagement\\resource"));
        f.setInitialFileName("myImage.jpg");
        img = f.showOpenDialog(new Stage());
        if(img != null){
            Image imgCh = new Image(img.toURI().toString());
            imageInsrt.setImage(imgCh);
            path = img.getAbsolutePath();
        }
    }
}
