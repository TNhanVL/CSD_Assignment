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
            if (GraphManagement.clickToAddPoint && e.getButton() == MouseButton.SECONDARY) {
                //If hoving a vertice
                if (GraphManagement.markPointIndex != -1) {
                    IO.out("Deleting : " + GraphManagement.markPointIndex);
                } else {
                    Point point = GraphManagement.getPoint(e.getX(), e.getY());
                    graph.addPoint(point);
                    textInput.setText(textInput.getText() + point.getX() + " " + point.getY() + "\n");
                    canvas.draw();
                }
            }
            //Primary mouse button
            if (GraphManagement.clickToAddPoint && e.getButton() == MouseButton.PRIMARY) {

            }
        });

        //move graph
        canvas.setOnMouseDragged((MouseEvent e) -> {
            double x = e.getX() - GraphManagement.previousDragX;
            double y = -(e.getY() - GraphManagement.previousDragY);
            if (!GraphManagement.released) {
                if (GraphManagement.markPointIndex == -1) {
                    GraphManagement.moveX += x;
                    GraphManagement.moveY += y;
                } else {
                    Point p = ResizableCanvas.points.get(GraphManagement.markPointIndex);
                    p.addX(x);
                    p.addY(y);
                    p.setY(GraphManagement.canvasHeight - p.getY());
                    Point p1 = GraphManagement.getPoint(p);
                    graph.updatePoint(GraphManagement.markPointIndex, p1);

                    //show point in input textfield
                    String[] s = textInput.getText().split("\n");
                    s[GraphManagement.markPointIndex] = p1.getX() + " " + p1.getY();
                    textInput.setText(String.join("\n", s) + "\n");
                }
            }
            GraphManagement.previousDragX = e.getX();
            GraphManagement.previousDragY = e.getY();
            GraphManagement.released = false;
            GraphManagement.reCalZoom = false;
            canvas.draw();
        });

        //mark released mouse
        canvas.setOnMouseReleased((MouseEvent e) -> {
            GraphManagement.released = true;
        });

        //Mark hover vertice
        canvas.setOnMouseMoved((MouseEvent e) -> {
            double x = e.getX();
            double y = GraphManagement.canvasHeight - e.getY();
//            System.out.println(x + " " + y);
//            System.out.println(ResizableCanvas.points);
            int preIndex = GraphManagement.markPointIndex;
            GraphManagement.markPointIndex = -1;
            int index = 0;
            for (Point point : ResizableCanvas.points) {
                if (point.distance(new Point(x, y)) <= GraphManagement.pointRadius) {
                    GraphManagement.markPointIndex = index;
                    break;
                }
                index++;
            }
            if (preIndex != GraphManagement.markPointIndex) {
                canvas.draw();
            }
//            System.out.println(e);
        });

        //Mouse scroll
        canvas.setOnScroll((ScrollEvent e) -> {
            double t = e.getDeltaY();
            double x = e.getX() - GraphManagement.canvasWidth / 2;
            double y = e.getY() - GraphManagement.canvasHeight / 2;
            GraphManagement.reCalZoom = false;
            if (t < 0) {
                GraphManagement.zoom = GraphManagement.zoom * (1 / GraphManagement.zoomInRatio);
                GraphManagement.moveX -= x;
                GraphManagement.moveX = GraphManagement.moveX * (1 / GraphManagement.zoomInRatio);
                GraphManagement.moveX += x;
                GraphManagement.moveY += y;
                GraphManagement.moveY = GraphManagement.moveY * (1 / GraphManagement.zoomInRatio);
                GraphManagement.moveY -= y;
            } else {
                GraphManagement.zoom = GraphManagement.zoom * GraphManagement.zoomInRatio;
                GraphManagement.moveX -= x;
                GraphManagement.moveX = GraphManagement.moveX * GraphManagement.zoomInRatio;
                GraphManagement.moveX += x;
                GraphManagement.moveY += y;
                GraphManagement.moveY = GraphManagement.moveY * GraphManagement.zoomInRatio;
                GraphManagement.moveY -= y;
            }
            canvas.draw();
        });
    }

    public void initialize() {
        initialCanvas();
        colorPickerConvex.setValue(GraphManagement.convexColor);
        colorPickerPoint.setValue(GraphManagement.pointColor);
        slider.setMin(2);
        slider.setMax(1000);
        slider.setValue(GraphManagement.NumberOfRandomVertex);
        textFieldNNumberOfVertex.setText(String.valueOf(GraphManagement.NumberOfRandomVertex));
    }

    @FXML
    private void pickConvexColor(ActionEvent event) {
        GraphManagement.convexColor = colorPickerConvex.getValue();
        canvas.draw();
    }

    @FXML
    private void pickPointColor(ActionEvent event) {
        GraphManagement.pointColor = colorPickerPoint.getValue();
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
        GraphManagement.NumberOfRandomVertex = (int) Math.round(slider.getValue());
        textFieldNNumberOfVertex.setText(String.valueOf(GraphManagement.NumberOfRandomVertex));
    }

    @FXML
    private void randomButton(ActionEvent event) {
        Random rand = new Random();
        Set<Point> s = new HashSet<>();
        int maxValue = GraphManagement.maxRandomCoordinare * GraphManagement.NumberOfRandomVertex;
        while (s.size() < GraphManagement.NumberOfRandomVertex) {
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
        GraphManagement.NumberOfRandomVertex = Integer.parseInt(textFieldNNumberOfVertex.getText());
        slider.setValue(Double.valueOf(GraphManagement.NumberOfRandomVertex));
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
