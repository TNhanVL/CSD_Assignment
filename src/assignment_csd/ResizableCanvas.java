/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_csd;

import java.util.ArrayList;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

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

    private void drawLine(Point a, Point b, GraphicsContext gc, Color color, double lineWeight) {
        gc.setStroke(color);
        gc.setLineWidth(lineWeight);
        gc.strokeLine(a.getX(), graph.canvasHeight - a.getY(), b.getX(), graph.canvasHeight - b.getY());
    }

    private void drawPoint(Point a, double radius, GraphicsContext gc, Color color) {
        gc.setFill(color);
        gc.fillOval(a.getX() - radius, graph.canvasHeight - a.getY() - radius, radius * 2, radius * 2);
    }

    //Example padding: 10%
    void calToFitScreen() {
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

    double bestGap(double x) {
        double t = 1;
        while (x >= 10) {
            x /= 10;
            t *= 10;
        }
        //Uncomment if allow gap smaller than 1
//        while (x < 1) {
//            x *= 10;
//            t /= 10;
//        }
        double arr[] = {1, 2, 5, 10};
        double ans = 0, cl = 100;
        for (double i : arr) {
            double tmp = Math.abs(x - i);
            if (tmp < cl) {
                cl = tmp;
                ans = i;
            }
        }
        return ans * t;
    }

    void drawCoordinateAxis(GraphicsContext gc) {
        Point cPoint = getPoint(new Point(0, 0));
//        drawPoint(cPoint, graph.pointRadius, gc, Color.BLUE);

        Point left = graph.getPoint(new Point(0, graph.canvasHeight - cPoint.getY()));
        Point right = graph.getPoint(new Point(graph.canvasWidth, graph.canvasHeight - cPoint.getY()));
        Point top = graph.getPoint(new Point(cPoint.getX(), 0));
        Point bottom = graph.getPoint(new Point(cPoint.getX(), graph.canvasHeight));

//        drawPoint(getPoint(left), graph.pointRadius, gc, Color.BLUE);
//        drawPoint(getPoint(right), graph.pointRadius, gc, Color.BLUE);
//        drawPoint(getPoint(top), graph.pointRadius, gc, Color.BLUE);
//        drawPoint(getPoint(bottom), graph.pointRadius, gc, Color.BLUE);
        drawLine(getPoint(top), getPoint(bottom), gc, graph.coorAxisColor, graph.coorAxisWeight);
        drawLine(getPoint(left), getPoint(right), gc, graph.coorAxisColor, graph.coorAxisWeight);

        double length = graph.gapBetweenLabel / graph.zoom;

        double step = bestGap(length);

        Point tmp = new Point(bottom.getX(), Math.floor(bottom.getY() / step) * step);
        while (true) {
            Point p = getPoint(tmp);

            if (Math.round(tmp.getY()) != 0) {
                //Draw number label
                Font font = Font.font(graph.coorAxisFontSize);
                gc.setTextAlign(TextAlignment.RIGHT);
                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(font);
                gc.strokeText(String.valueOf(Math.round(tmp.getY())), p.getX() - 5, graph.canvasHeight - p.getY());
                //Draw small line
                Point a = p.clone();
                Point b = p.clone();
                a.addX(-2);
                b.addX(2);
                drawLine(a, b, gc, graph.coorAxisColor, graph.coorAxisWeight);
            }

//            drawPoint(p, graph.pointRadius, gc, Color.BLUE);
            tmp.addY(step);
            if (tmp.getY() > top.getY()) {
                break;
            }
        }

        tmp = new Point(Math.floor(left.getX() / step) * step, left.getY());
        while (true) {
            Point p = getPoint(tmp);

            if (Math.round(tmp.getX()) != 0) {
                //Draw number label
                Font font = Font.font(graph.coorAxisFontSize);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(font);
                gc.strokeText(String.valueOf(Math.round(tmp.getX())), p.getX(), graph.canvasHeight - p.getY() + 10);
                //Draw small line
                Point a = p.clone();
                Point b = p.clone();
                a.addY(-2);
                b.addY(2);
                drawLine(a, b, gc, graph.coorAxisColor, graph.coorAxisWeight);
            } else {
                Font font = Font.font(graph.coorAxisFontSize);
                gc.setTextAlign(TextAlignment.RIGHT);
                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(font);
                gc.strokeText("0", p.getX() - 5, graph.canvasHeight - p.getY() + 10);
            }

//            drawPoint(p, graph.pointRadius, gc, Color.BLUE);
            tmp.addX(step);
            if (tmp.getX() > right.getX()) {
                break;
            }
        }

//        drawPoint(getPoint(new Point(0, 0)), graph.pointRadius, gc, Color.BLUE);
//        IO.out(getPoint(new Point(10, 10)));
//        IO.out(graph.canvasWidth / graph.zoom);
    }

    void draw() {
        graph.canvasWidth = getWidth();
        graph.canvasHeight = getHeight();

        //initial screen
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, graph.canvasWidth, graph.canvasHeight);

        if (graph.convexPoints.isEmpty() && graph.reCalZoom) {
            graph.zoom = 1;
        }

        if (graph.reCalZoom) {
            calToFitScreen();
        }

        points = getPoint(graph.points);
        convexPoints = getPoint(graph.convexPoints);

        //Draw Coordinate Axis
        drawCoordinateAxis(gc);

        //draw convex polygon
        for (int i = 0; i < convexPoints.size(); i++) {
            drawLine(convexPoints.get(i), convexPoints.get((i + 1) % convexPoints.size()), gc, graph.convexColor, graph.lineWeight);
        }
        //draw all points
        for (int i = 0; i < points.size(); i++) {
            drawPoint(points.get(i), graph.pointRadius, gc, graph.pointColor);
        }
        //Draw mark point
        if (0 <= graph.markPointIndex && graph.markPointIndex < graph.points.size()) {
            Point point = points.get(graph.markPointIndex);
            drawPoint(point, graph.pointRadius + 1, gc, graph.pointColor);
            drawPoint(point, graph.pointRadius, gc, graph.convexColor);
        }

        //Show area of convex polygon
        Font font = Font.font(14);
        gc.setFont(font);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.CENTER);
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        gc.strokeText("Convex Polygon Area: " + Math.round(graph.calConvexArea() * 1000) / 1000.0, 10, 10);
        gc.strokeText("Number of Points: " + graph.points.size(), 10, 30);
        double totalLength = 0;
        for (int i = 0; i < graph.convexPoints.size(); i++) {
            totalLength += graph.convexPoints.get(i).distance(graph.convexPoints.get((i + 1) % graph.convexPoints.size()));
        }
        gc.strokeText("Total Length of Convex: " + Math.round(totalLength * 1000) / 1000.0, 10, 50);
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
