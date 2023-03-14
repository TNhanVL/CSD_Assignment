/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package assignment_csd;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author TTNhan
 */
public class Assignment_CSD extends Application {

    static Scene scene;
    static Stage graphStage;
    static GraphManagement graph = new GraphManagement();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("graph"), 1280, 800);
        stage.setScene(scene);
        stage.setTitle("Geometry computing");
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.show();
        graphStage = stage;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        IO.out(Assignment_CSD.class.getResource(fxml + ".fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(Assignment_CSD.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
