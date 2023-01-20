package ass_package;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    static Scene scene;
    static Stage graphStage;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Primary"), 1280, 800);
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
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}