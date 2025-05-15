package LaboratoryManagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the start of Laboratory Management System
 */
public class smartLab extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        /**
         * This class contains main() method
         * @param stage
         * @throws Exception
         */
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML/HomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Home page");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}