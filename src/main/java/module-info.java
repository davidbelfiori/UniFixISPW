module org.uniroma.ing.ispw.unifix {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.checkerframework.checker.qual;

    opens org.ing.ispw.unifix to javafx.fxml;
    exports org.ing.ispw.unifix;

    exports org.ing.ispw.unifix.controllergrafico;
    opens org.ing.ispw.unifix.controllergrafico to javafx.fxml;
}