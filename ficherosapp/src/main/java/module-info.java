module com.ficheros {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.ficheros to javafx.fxml;
    exports com.ficheros;
}




/* module com.ficheros {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.ficheros to javafx.fxml;
    exports com.ficheros;
}
*/