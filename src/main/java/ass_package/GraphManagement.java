/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass_package;

import javafx.scene.paint.Color;

/**
 *
 * @author TTNhan
 */
public class GraphManagement {

    public static Color convexColor = Color.RED;
    public static Color pointColor = Color.BLACK;
    public static int pointRadius = 7;
    public static int lineWeight = 5;
    public static int NumberOfRandomVertex = 10;
    public static int maxRandomCoordinare = 100;

    public static double paddingOfGraph = 0.1; //percent
    public static boolean reCalZoom = true;
    public static double zoom = 10;
    public static double zoomInRatio = 1.2;
    public static double canvasWidth;
    public static double canvasHeight;
    public static double middleX;
    public static double middleY;

    public static boolean clickToAddPoint = true;

    public static double moveX = 0;
    public static double moveY = 0;
    public static double previousDragX = 0;
    public static double previousDragY = 0;
    public static boolean released = true;

    public static int markPointIndex = -1;

    public static Point getPoint(double x, double y) {
        double u = (x - moveX - canvasWidth / 2) / zoom + middleX;
        double v = (-y - moveY + canvasHeight / 2) / zoom + middleY;
        return new Point(u, v);
    }

    public static Point getPoint(Point p) {
        double u = (p.getX() - moveX - canvasWidth / 2) / zoom + middleX;
        double v = (-p.getY() - moveY + canvasHeight / 2) / zoom + middleY;
        return new Point(u, v);
    }
}
