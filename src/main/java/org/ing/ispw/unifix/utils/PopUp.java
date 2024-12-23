package org.ing.ispw.unifix.utils;

import javafx.scene.control.Alert;

public class PopUp {
    public PopUp() {
        //perchè lo devo usa e basta
    }

    public void showErrorPopup(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showSuccessPopup(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
