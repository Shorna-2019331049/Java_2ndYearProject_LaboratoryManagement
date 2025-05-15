module laboratoryManagement {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires mysql.connector.java;
    requires itextpdf;
    opens LaboratoryManagement;
    opens LaboratoryManagement.Controller;
}