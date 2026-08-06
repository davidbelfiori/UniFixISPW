package org.ing.ispw.unifix.utils;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PopUp {

    private static final String CONST = "dialog-content-label";

    private void applyCustomStyle(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        dialogPane.setMinWidth(400);

        try {
            String cssPath = Objects.requireNonNull(getClass().getResource("/org/ing/ispw/unifix/dialog.css")).toExternalForm();
            dialogPane.getStylesheets().add(cssPath);
        } catch (Exception _) {
            // Fallback silenzioso se il CSS non viene trovato
        }
    }

    public void showErrorPopup(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header == null || header.isEmpty() ? null : header);

        if (content != null && !content.isEmpty()) {
            Label contentLabel = new Label(content);
            contentLabel.setWrapText(true);
            contentLabel.getStyleClass().add(CONST);
            alert.getDialogPane().setContent(contentLabel);
        }

        applyCustomStyle(alert);
        alert.showAndWait();
    }

    public void showSuccessPopup(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        if (content != null && !content.isEmpty()) {
            Label contentLabel = new Label(content);
            contentLabel.setWrapText(true);
            contentLabel.getStyleClass().add(CONST);
            alert.getDialogPane().setContent(contentLabel);
        }

        applyCustomStyle(alert);
        alert.showAndWait();
    }

    public boolean showConfirmationPopup(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header == null || header.isEmpty() ? null : header);
        alert.setGraphic(null);

        if (content != null && !content.isEmpty()) {
            Label contentLabel = new Label(content);
            contentLabel.setWrapText(true);
            contentLabel.getStyleClass().add(CONST);
            alert.getDialogPane().setContent(contentLabel);
        }

        ButtonType btnConferma = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnAnnulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConferma, btnAnnulla);

        applyCustomStyle(alert);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == btnConferma;
    }

    public boolean showConfirmationPopup(String title, String header, String question, Map<String, String> details) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header == null || header.isEmpty() ? null : header);
        alert.setGraphic(null);

        VBox cardContainer = new VBox();
        cardContainer.getStyleClass().add("summary-card");

        if (question != null && !question.isEmpty()) {
            Label questionLabel = new Label(question);
            questionLabel.getStyleClass().add("summary-title");
            cardContainer.getChildren().addAll(questionLabel, new Separator());
        }

        if (details != null && !details.isEmpty()) {
            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(8);
            grid.setPadding(new Insets(5, 0, 5, 0));

            int row = 0;
            for (Map.Entry<String, String> entry : details.entrySet()) {
                Label keyLabel = new Label(entry.getKey() + ":");
                keyLabel.getStyleClass().add("summary-key");

                Label valLabel = new Label(entry.getValue());
                valLabel.getStyleClass().add("summary-val");
                valLabel.setWrapText(true);

                grid.add(keyLabel, 0, row);
                grid.add(valLabel, 1, row);
                row++;
            }
            cardContainer.getChildren().add(grid);
        }

        alert.getDialogPane().setContent(cardContainer);

        ButtonType btnConferma = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnAnnulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConferma, btnAnnulla);

        applyCustomStyle(alert);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == btnConferma;
    }
}