module com.example.trial {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.example.trial to javafx.fxml;
    exports com.example.trial;

}