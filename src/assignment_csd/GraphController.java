/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package assignment_csd;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author TTNhan
 */
public class GraphController {

    ResizableCanvas canvas = new ResizableCanvas();
    GraphManagement graph = Assignment_CSD.graph;
    File saveFile;

    @FXML
    private Pane paneCanvas;
    @FXML
    private ColorPicker colorPickerConvex;
    @FXML
    private ColorPicker colorPickerPoint;
    @FXML
    private TextArea textInput;
    @FXML
    private Slider slider;
    @FXML
    private TextField textFieldNNumberOfVertex;
    @FXML
    private MenuItem closeFile;

    //gesture on canvas
    public void initialCanvas() {

        paneCanvas.getChildren().add(canvas);
        canvas.widthProperty().bind(paneCanvas.widthProperty().subtract(2));
        canvas.heightProperty().bind(paneCanvas.heightProperty().subtract(2));
        canvas.setTranslateX(1);
        canvas.setTranslateY(1);

        //Click action
        canvas.setOnMouseClicked((MouseEvent e) -> {
            //Secondary mouse button
            if (graph.clickToAddPoint && e.getButton() == MouseButton.SECONDARY) {
                //If hoving a vertice -> try to delete a Point
                if (graph.markPointIndex != -1) {
//                    IO.out("Deleting : " + graph.markPointIndex);
                    graph.deletePoint(graph.markPointIndex);
                    updateInput();
                    canvas.draw();
                    graph.markPointIndex = findHoverVertexIndex(e.getX(), graph.canvasHeight - e.getY());
                    canvas.draw();
                } else {
                    //Try to add a new Point
                    Point point = graph.getPoint(e.getX(), e.getY());
                    graph.markPointIndex = graph.points.size();
                    graph.addPoint(point);
                    textInput.setText(textInput.getText() + point.to_String() + "\n");
                    canvas.draw();
                }
            }
            //Primary mouse button
            if (graph.clickToAddPoint && e.getButton() == MouseButton.PRIMARY) {

            }
        });

        //move graph
        canvas.setOnMouseDragged((MouseEvent e) -> {
            double x = e.getX() - graph.previousDragX;
            double y = -(e.getY() - graph.previousDragY);
            if (!graph.released) {
                if (graph.markPointIndex == -1) {
                    graph.moveX += x;
                    graph.moveY += y;
                } else {
                    Point p = ResizableCanvas.points.get(graph.markPointIndex);
                    p.addX(x);
                    p.addY(y);
                    p.setY(graph.canvasHeight - p.getY());
                    Point p1 = graph.getPoint(p);
                    graph.updatePoint(graph.markPointIndex, p1);

                    //show point in input textfield
                    String[] s = textInput.getText().split("\n");
                    s[graph.markPointIndex] = p1.to_String();
                    textInput.setText(String.join("\n", s) + "\n");
                }
            }
            graph.previousDragX = e.getX();
            graph.previousDragY = e.getY();
            graph.released = false;
            graph.reCalZoom = false;
            canvas.draw();
        });

        //mark released mouse
        canvas.setOnMouseReleased((MouseEvent e) -> {
            graph.released = true;
        });

        //Mark hover vertice
        canvas.setOnMouseMoved((MouseEvent e) -> {
            double x = e.getX();
            double y = graph.canvasHeight - e.getY();
//            System.out.println(x + " " + y);
//            System.out.println(ResizableCanvas.points);
            int preIndex = graph.markPointIndex;
            graph.markPointIndex = findHoverVertexIndex(x, y);
            if (preIndex != graph.markPointIndex) {
                canvas.draw();
            }
//            System.out.println(e);
        });

        //Mouse scroll
        canvas.setOnScroll((ScrollEvent e) -> {
            double t = e.getDeltaY();
            double x = e.getX() - graph.canvasWidth / 2;
            double y = e.getY() - graph.canvasHeight / 2;
            graph.reCalZoom = false;
            if (t < 0) {
                graph.zoom = graph.zoom * (1 / graph.zoomInRatio);
                graph.moveX -= x;
                graph.moveX = graph.moveX * (1 / graph.zoomInRatio);
                graph.moveX += x;
                graph.moveY += y;
                graph.moveY = graph.moveY * (1 / graph.zoomInRatio);
                graph.moveY -= y;
            } else {
                graph.zoom = graph.zoom * graph.zoomInRatio;
                graph.moveX -= x;
                graph.moveX = graph.moveX * graph.zoomInRatio;
                graph.moveX += x;
                graph.moveY += y;
                graph.moveY = graph.moveY * graph.zoomInRatio;
                graph.moveY -= y;
            }
            canvas.draw();
        });
    }

    int findHoverVertexIndex(double x, double y) {
        int find = -1;
        int index = 0;
        for (Point point : ResizableCanvas.points) {
            if (point.distance(new Point(x, y)) <= graph.pointRadius) {
                find = index;
                break;
            }
            index++;
        }
        return find;
    }

    public void initialize() {
        initialCanvas();
        colorPickerConvex.setValue(graph.convexColor);
        colorPickerPoint.setValue(graph.pointColor);
        slider.setMin(2);
        slider.setMax(100);
        slider.setValue(graph.NumberOfRandomVertex);
        textFieldNNumberOfVertex.setText(String.valueOf(graph.NumberOfRandomVertex));
    }

    @FXML
    private void pickConvexColor(ActionEvent event) {
        graph.convexColor = colorPickerConvex.getValue();
        canvas.draw();
    }

    @FXML
    private void pickPointColor(ActionEvent event) {
        graph.pointColor = colorPickerPoint.getValue();
        canvas.draw();
    }

    @FXML
    private void getTextInput(KeyEvent event) {
        try {
            ArrayList<Point> a = Point.converToPoints(textInput.getText().split("[\n]"));
            graph.setPoints(a);
//            System.out.println(Convex.points);
            canvas.draw();
        } catch (Exception e) {
        }
    }

    @FXML
    private void sliderNumberOfVertex(MouseEvent event) {
        graph.NumberOfRandomVertex = (int) Math.round(slider.getValue());
        textFieldNNumberOfVertex.setText(String.valueOf(graph.NumberOfRandomVertex));
    }

    @FXML
    private void randomButton(ActionEvent event) {
        Random rand = new Random();
        Set<Point> s = new HashSet<>();
        int maxValue = graph.maxRandomCoordinare * graph.NumberOfRandomVertex;
        while (s.size() < graph.NumberOfRandomVertex) {
            int u = rand.nextInt(maxValue);
            int v = rand.nextInt(maxValue);
            s.add(new Point(u, v));
        }

        ArrayList<Point> a = new ArrayList<>(s);
        String input = "";
        for (int i = 0; i < a.size(); i++) {
            input += a.get(i).to_String() + "\n";
        }
        textInput.setText(input);
        graph.setPoints(a);
        canvas.resetScreen();
    }

    @FXML
    private void textFieldNNumberOfVertex(KeyEvent event) {
        graph.NumberOfRandomVertex = Integer.parseInt(textFieldNNumberOfVertex.getText());
        slider.setValue(Double.valueOf(graph.NumberOfRandomVertex));
    }

    @FXML
    private void resetScreen(ActionEvent event) {
        canvas.resetScreen();
    }

    @FXML
    private void magicButton(ActionEvent event) {
        GameManagement game = new GameManagement(Assignment_CSD.graphStage);
        Stage stage = new Stage();
        Assignment_CSD.graphStage.hide();

        game.setSize(canvas.getWidth(), canvas.getHeight());
        if (!graph.points.isEmpty()) {
            game.setBall(canvas.getPoint(graph.points));
        }
        game.start(stage);
    }

    @FXML
    private void about(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Help.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Help");
        stage.setWidth(1200);
        stage.setHeight(900);
        stage.show();
    }

    @FXML
    private void exit(ActionEvent event) {
        Assignment_CSD.graphStage.close();
    }

    @FXML
    void clearGraph() {
        graph.points.clear();
        graph.convexPoints.clear();
        canvas.draw();
        textInput.setText("");
    }

    private void clear(ActionEvent event) {
        clearGraph();
    }

    void saveToFile(File file) {
        if (file == null) {
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {

            for (Point p : graph.points) {
                fileWriter.write(p.getX() + " " + p.getY() + "\n");
            }

            fileWriter.close();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "There are some error when saving file!");
            alert.show();
        }
    }

    @FXML
    private void saveAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.initialFileNameProperty().set("Points.geo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("List of points", "*.geo"));
        fileChooser.setTitle(" Save File As ");
        File file = fileChooser.showSaveDialog(Assignment_CSD.graphStage);

        saveToFile(file);
    }

    @FXML
    private void save(ActionEvent event) {
        if (saveFile == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.initialFileNameProperty().set("Points.geo");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("List of points", "*.geo"));
            fileChooser.setTitle(" Save File ");
            saveFile = fileChooser.showSaveDialog(Assignment_CSD.graphStage);
        }

        saveToFile(saveFile);

        if (saveFile != null) {
            closeFile.setDisable(false);
        }
    }

    @FXML
    private void closeFile(ActionEvent event) {
        if (saveFile != null) {
            saveFile = null;
            clearGraph();
            closeFile.setDisable(true);
        }
    }

    void updateInput() {
        String input = "";
        for (int i = 0; i < graph.points.size(); i++) {
            input += graph.points.get(i).to_String() + "\n";
        }
        textInput.setText(input);
    }

    @FXML
    private void open(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Geometry File");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("List of points", "*.geo"));

        File file = fileChooser.showOpenDialog(Assignment_CSD.graphStage);

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                String s1[] = s.split(" ");
                if (s1.length == 2) {
                    graph.points.add(new Point(Double.parseDouble(s1[0]), Double.parseDouble(s1[1])));
                } else {
                    break;
                }
            }

        } catch (FileNotFoundException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "There are some error when loading file!");
            alert.show();
        }

        if (file != null) {
            saveFile = file;
            closeFile.setDisable(false);
            graph.convexPoints = Convex.ConvexHull(graph.points);
            canvas.calToFitScreen();
            updateInput();
            canvas.draw();
        }
    }

    @FXML
    private void saveAsPicture(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.initialFileNameProperty().set("Points.png");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Picture", "*.png"));
        fileChooser.setTitle(" Save File As Picture ");
        File file = fileChooser.showSaveDialog(Assignment_CSD.graphStage);

        if (file != null) {
            WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            canvas.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, "png", file);
        }
    }

}
