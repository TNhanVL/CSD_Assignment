/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_csd;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author TTNhan
 */
public class ResizableCanvas extends Canvas {

    public static ArrayList<Point> points = new ArrayList<>();
    public static ArrayList<Point> convexPoints = new ArrayList<>();

    GraphManagement graph = Assignment_CSD.graph;

    public ResizableCanvas() {
        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    private void drawLine(Point a, Point b, GraphicsContext gc, Color color) {
        gc.setStroke(color);
        gc.setLineWidth(graph.lineWeight);
        gc.strokeLine(a.getX(), graph.canvasHeight - a.getY(), b.getX(), graph.canvasHeight - b.getY());
    }

    private void drawPoint(Point a, double radius, GraphicsContext gc, Color color) {
        gc.setFill(color);
        gc.fillOval(a.getX() - radius, graph.canvasHeight - a.getY() - radius, radius * 2, radius * 2);
    }

    //Example padding: 10%
    private void calToFitScreen() {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < graph.points.size(); i++) {
            minX = Math.min(minX, graph.points.get(i).getX());
            maxX = Math.max(maxX, graph.points.get(i).getX());
            minY = Math.min(minY, graph.points.get(i).getY());
            maxY = Math.max(maxY, graph.points.get(i).getY());
        }
        for (int i = 0; i < graph.convexPoints.size(); i++) {
            minX = Math.min(minX, graph.convexPoints.get(i).getX());
            maxX = Math.max(maxX, graph.convexPoints.get(i).getX());
            minY = Math.min(minY, graph.convexPoints.get(i).getY());
            maxY = Math.max(maxY, graph.convexPoints.get(i).getY());
        }
        if (graph.points.isEmpty()) {
            graph.zoom = 1;
            minX = -graph.canvasWidth / 2;
            maxX = graph.canvasWidth / 2;
            minY = -graph.canvasHeight / 2;
            maxY = graph.canvasHeight / 2;
        }
        if (graph.points.size() == 1) {
            graph.zoom = 1;
            minX -= graph.canvasWidth / 2;
            maxX += graph.canvasWidth / 2;
            minY -= graph.canvasHeight / 2;
            maxY += graph.canvasHeight / 2;
        }

        graph.middleX = (minX + maxX) / 2;
        graph.middleY = (minY + maxY) / 2;

        graph.zoom = Math.min(graph.canvasWidth * (1 - graph.paddingOfGraph * 2) / (maxX - minX), graph.canvasHeight * (1 - graph.paddingOfGraph * 2) / (maxY - minY));
    }

    public Point getPoint(Point p) {
        Point point = new Point();
        point.setX((p.getX() - graph.middleX) * graph.zoom + graph.canvasWidth / 2 + graph.moveX);
        point.setY((p.getY() - graph.middleY) * graph.zoom + graph.canvasHeight / 2 + graph.moveY);
        return point;
    }

    public ArrayList<Point> getPoint(ArrayList<Point> a) {
        ArrayList<Point> ans = new ArrayList<>();
        for (Point point : a) {
            ans.add(getPoint(point));
        }
        return ans;
    }

    void draw() {
        graph.canvasWidth = getWidth();
        graph.canvasHeight = getHeight();

        //initial screen
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, graph.canvasWidth, graph.canvasHeight);

        if (graph.convexPoints.isEmpty()) {
            graph.zoom = 1;
        }

        if (graph.reCalZoom) {
            calToFitScreen();
        }

        points = getPoint(graph.points);
        convexPoints = getPoint(graph.convexPoints);

        //draw convex polygon
        for (int i = 0; i < convexPoints.size(); i++) {
            drawLine(convexPoints.get(i), convexPoints.get((i + 1) % convexPoints.size()), gc, graph.convexColor);
        }
        //draw all points
        for (int i = 0; i < points.size(); i++) {
            drawPoint(points.get(i), graph.pointRadius, gc, graph.pointColor);
        }
        if (0 <= graph.markPointIndex && graph.markPointIndex < graph.points.size()) {
            Point point = points.get(graph.markPointIndex);
            drawPoint(point, graph.pointRadius + 1, gc, graph.pointColor);
            drawPoint(point, graph.pointRadius, gc, graph.convexColor);
        }

        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        gc.strokeText("Convex Polygon Area: " + graph.calConvexArea(), 10, 20);
    }

    void resetScreen() {
        graph.reCalZoom = true;
        graph.moveX = 0;
        graph.moveY = 0;
        draw();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}
