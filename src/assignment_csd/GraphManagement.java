/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_csd;

import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 *
 * @author TTNhan
 */
public class GraphManagement {

    public ArrayList<Point> points = new ArrayList<>();
    public ArrayList<Point> convexPoints = new ArrayList<>();

    public Color convexColor = Color.RED;
    public Color pointColor = Color.BLACK;
    public int pointRadius = 12;
    public int lineWeight = 8;
    public int NumberOfRandomVertex = 10;
    public int maxRandomCoordinare = 100;

    public double paddingOfGraph = 0.1; //percent
    public boolean reCalZoom = true;
    public double zoom = 10;
    public double zoomInRatio = 1.2;
    public double canvasWidth;
    public double canvasHeight;
    public double middleX;
    public double middleY;

    public boolean clickToAddPoint = true;

    public double moveX = 0;
    public double moveY = 0;
    public double previousDragX = 0;
    public double previousDragY = 0;
    public boolean released = true;

    public int markPointIndex = -1;
    public int selectedPointIndex = -1;

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
        convexPoints = Convex.ConvexHull(points);
    }

    public void addPoint(Point p) {
        points.add(p);
        convexPoints = Convex.ConvexHull(points);
    }

    public void updatePoint(int index, Point p) {
        points.set(index, p);
        convexPoints = Convex.ConvexHull(points);
    }

    public Point getPoint(double x, double y) {
        double u = (x - moveX - canvasWidth / 2) / zoom + middleX;
        double v = (-y - moveY + canvasHeight / 2) / zoom + middleY;
        return new Point(u, v);
    }

    public Point getPoint(Point p) {
        double u = (p.getX() - moveX - canvasWidth / 2) / zoom + middleX;
        double v = (-p.getY() - moveY + canvasHeight / 2) / zoom + middleY;
        return new Point(u, v);
    }
}
