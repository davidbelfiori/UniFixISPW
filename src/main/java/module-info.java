module org.ing.ispw.unifix {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires javafx.controls;
    requires org.mariadb.jdbc;
    requires annotations;

    opens org.ing.ispw.unifix to javafx.fxml;
    opens org.ing.ispw.unifix.controllergrafico to javafx.fxml;

    exports org.ing.ispw.unifix;
    exports org.ing.ispw.unifix.controllergrafico;
    exports org.ing.ispw.unifix.dao;
    exports org.ing.ispw.unifix.model;
    exports org.ing.ispw.unifix.utils;
    exports org.ing.ispw.unifix.cli;
    exports org.ing.ispw.unifix.exception;
    exports org.ing.ispw.unifix.controllerapplicativo;







}