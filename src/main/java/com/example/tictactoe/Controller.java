package com.example.tictactoe;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller {
    @FXML private Button Btn1, Btn2, Btn3, Btn4, Btn5, Btn6, Btn7, Btn8, Btn9;
    @FXML private AnchorPane root;
    @FXML private Label title;
    private final char[] index = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private final char[] copy_index = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private int wonBtn1, wonBtn2, wonBtn3, turn = 0;
    private double xOffset = 0, yOffset = 0;
    private boolean gameWon = false;

    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/Fonts/fonts.ttf"), 10);
        title.getStyleClass().add("title-label");

        makeStageDraggable();

        for (int i = 1; i <= 9; i++) {
            final int index1 = i;
            Button button = getButton(i);
            button.setOnMousePressed(event -> setButtons(button, index1));
            if (!button.isDisable()) {
                button.setOnMouseEntered(event -> entered(button));
                button.setOnMouseExited(event -> exited(button));
            }
        }
    }

    private Button getButton(int i) {
        Button[] buttons = {null, Btn1, Btn2, Btn3, Btn4, Btn5, Btn6, Btn7, Btn8, Btn9};
        return(buttons[i]);
    }

    private void setButtons(Button B, int i) {
        if(!gameWon) {
            B.setText(String.valueOf(turn % 2 == 0 ? 'X' : 'O'));
            index[i] = turn % 2 == 0 ? 'X' : 'O';
            turn++;
            logic();
            B.setDisable(true);
            scaleOut_winBtn(B);
        }
    }

    private void entered(Button Btn) {
        if(gameWon) return;
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), Btn);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);
        scaleIn.play();
        Btn.setText(turn % 2 == 0 ? "X" : "0");
    }

    private void exited(Button Btn) {
        if(Btn.isDisable()) return;
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), Btn);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);
        scaleOut.play();
        Btn.setText("");
    }

    private void scaleIn_winBtn(Button Btn) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), Btn);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);
        scaleIn.play();
        Btn.getStyleClass().add("btn-won");
    }

    private void scaleOut_winBtn(Button Btn) {
        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), Btn);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);
        scaleOut.play();
        if(gameWon) {
            highlight_winBtn(wonBtn1);
            highlight_winBtn(wonBtn2);
            highlight_winBtn(wonBtn3);
        }
    }
    private void logic() {
        for (int i = 0; i < 3; i++) {
            checkLine(i * 3 + 1, i * 3 + 2, i * 3 + 3); // Rows
            checkLine(i + 1, i + 4, i + 7); // Columns
        }
        checkLine(1, 5, 9); // Diagonal
        checkLine(3, 5, 7); // Reverse Diagonal

    }

    private void checkLine(int a, int b, int c) {
        if (index[a] == index[b] && index[b] == index[c]) {
            gameWon = true;
            wonBtn1 = a;
            wonBtn2 = b;
            wonBtn3 = c;
            title.setText("Click to Reset");
        }
    }

    private void highlight_winBtn(int i) {
        Button[] buttons = {null, Btn1, Btn2, Btn3, Btn4, Btn5, Btn6, Btn7, Btn8, Btn9};
        scaleIn_winBtn(buttons[i]);
    }

    @FXML
    private void restart() {

        reset(Btn1, Btn2, Btn3, Btn4, Btn5, Btn6, Btn7, Btn8, Btn9);
        System.arraycopy(copy_index, 0, index, 0, 10);
        turn = 0;
        gameWon = false;

    }

    private void reset(Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(false);
            button.getStyleClass().remove("btn-won");
            title.setText("Tic Tac Toe");
            exited(button);
        }
    }
    private void makeStageDraggable() {
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
    @FXML
    private void shutdown() {
        Platform.exit();
    }
}