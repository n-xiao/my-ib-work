/*
 * This class was created as instructed through the JavaFX installation guide:
 * https://openjfx.io/openjfx-docs/
 *
 * The code within the start method was mostly sourced from
 * https://jenkov.com/tutorials/javafx/webview.html in order to set up a WebView node.
 *
 * Calls to the class Rosetta were not sourced from anywhere.
 */

package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.backend.Rosetta;

public class MainApp extends Application {
    @Override
    public void start(Stage stage)
        throws Exception { // adapted from https://jenkov.com/tutorials/javafx/webview.html
        stage.setTitle("pls give level 7");
        stage.setResizable(false);
        WebView wv = new WebView();
        String url = MainApp.class.getResource("/frontend/index.html").toExternalForm();
        wv.getEngine().load(url); // loads html in resources/frontend

        VBox vBox = new VBox(wv);
        Scene scene = new Scene(vBox, 420, 710); // specifies window dimensions

        wv.setPrefWidth(scene.getWidth());
        wv.setPrefHeight(scene.getHeight());

        Rosetta.initialise(wv);
        Rosetta.startBigBrother(scene); // start listening for keybinds

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
