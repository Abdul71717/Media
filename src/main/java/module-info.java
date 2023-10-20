module com.example.media {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.testng;
    opens com.example.media to javafx.fxml;
    exports com.example.media;
}