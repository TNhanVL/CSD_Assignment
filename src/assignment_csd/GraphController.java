/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package assignment_csd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author TTNhan
 */
public class GraphController {

    ResizableCanvas canvas = new ResizableCanvas();
    GraphManagement graph = Assignment_CSD.graph;

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
                //If hoving a vertice
                if (graph.markPointIndex != -1) {
                    IO.out("Deleting : " + graph.markPointIndex);
                } else {
                    Point point = graph.getPoint(e.getX(), e.getY());
                    graph.addPoint(point);
                    textInput.setText(textInput.getText() + point.getX() + " " + point.getY() + "\n");
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
                    s[graph.markPointIndex] = p1.getX() + " " + p1.getY();
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
            graph.markPointIndex = -1;
            int index = 0;
            for (Point point : ResizableCanvas.points) {
                if (point.distance(new Point(x, y)) <= graph.pointRadius) {
                    graph.markPointIndex = index;
                    break;
                }
                index++;
            }
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

    public void initialize() {
        initialCanvas();
        colorPickerConvex.setValue(graph.convexColor);
        colorPickerPoint.setValue(graph.pointColor);
        slider.setMin(2);
        slider.setMax(1000);
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
            input += String.valueOf(a.get(i).getX()) + " " + String.valueOf(a.get(i).getY()) + "\n";
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

        game.start(stage);
    }

}
