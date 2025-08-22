module com.asterexcrisys.tetris {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires annotations;
    opens com.asterexcrisys.tetris to javafx.fxml;
    exports com.asterexcrisys.tetris;
    exports com.asterexcrisys.tetris.controllers;
    opens com.asterexcrisys.tetris.controllers to javafx.fxml;
}