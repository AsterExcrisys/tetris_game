module com.asterexcrisys.tetris {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires annotations;
    requires javafx.media;
    requires org.xerial.sqlitejdbc;
    requires com.fasterxml.jackson.databind;
    exports com.asterexcrisys.tetris;
    exports com.asterexcrisys.tetris.controllers;
    exports com.asterexcrisys.tetris.types;
    opens com.asterexcrisys.tetris to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.asterexcrisys.tetris.controllers to javafx.fxml;
}