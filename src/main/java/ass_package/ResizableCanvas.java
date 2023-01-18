/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass_package;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author TTNhan
 */
public class ResizableCanvas extends Canvas {

    public static ArrayList <Point> points = new ArrayList <>();
    public static ArrayList <Point> convexPoints = new ArrayList <>();
    
    public ResizableCanvas() {
        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    private void drawLine(Point a, Point b, GraphicsContext gc, Color color) {
        gc.setStroke(color);
        gc.setLineWidth(GraphManagement.lineWeight);
        gc.strokeLine(a.getX(),  GraphManagement.canvasHeight - a.getY(), b.getX(), GraphManagement.canvasHeight - b.getY());
    }

    private void drawPoint(Point a, double radius, GraphicsContext gc, Color color) {
        gc.setFill(color);
        gc.fillOval(a.getX() - radius, GraphManagement.canvasHeight - a.getY() - radius, radius * 2, radius * 2);
    }

    //Example padding: 10%
    private void calToFitScreen() {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < Convex.points.size(); i++) {
            minX = Math.min(minX, Convex.points.get(i).getX());
            maxX = Math.max(maxX, Convex.points.get(i).getX());
            minY = Math.min(minY, Convex.points.get(i).getY());
            maxY = Math.max(maxY, Convex.points.get(i).getY());
        }
        for (int i = 0; i < Convex.convexPoints.size(); i++) {
            minX = Math.min(minX, Convex.convexPoints.get(i).getX());
            maxX = Math.max(maxX, Convex.convexPoints.get(i).getX());
            minY = Math.min(minY, Convex.convexPoints.get(i).getY());
            maxY = Math.max(maxY, Convex.convexPoints.get(i).getY());
        }
        GraphManagement.middleX = (minX + maxX) / 2;
        GraphManagement.middleY = (minY + maxY) / 2;
        GraphManagement.zoom = Math.min(GraphManagement.canvasWidth * (1 - GraphManagement.paddingOfGraph * 2) / (maxX - minX), GraphManagement.canvasHeight * (1 - GraphManagement.paddingOfGraph * 2) / (maxY - minY));
    }
    
    public static Point getPoint(Point p){
        Point point = new Point();
        point.setX((p.getX() - GraphManagement.middleX) * GraphManagement.zoom + GraphManagement.canvasWidth / 2 + GraphManagement.moveX);
        point.setY((p.getY() - GraphManagement.middleY) * GraphManagement.zoom + GraphManagement.canvasHeight / 2 + GraphManagement.moveY);
        return point;
    }
    
    public static ArrayList <Point> getPoint(ArrayList <Point> a){
        ArrayList <Point> ans = new ArrayList <> ();
        for(Point point: a){
            ans.add(getPoint(point));
        }
        return ans;
    }

    void draw() {
        GraphManagement.canvasWidth = getWidth();
        GraphManagement.canvasHeight = getHeight();

        //initial screen
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, GraphManagement.canvasWidth, GraphManagement.canvasHeight);
        
        points = new ArrayList<>();
        convexPoints = new ArrayList <>();
        
        if(Convex.points.size() >= 2 && GraphManagement.reCalZoom){
            calToFitScreen();
        }
        
        if(Convex.points.size() >= 2){
            points = getPoint(Convex.points);
            convexPoints = getPoint(Convex.convexPoints);
        }else if(Convex.points.size() == 1){
            points.add(new Point(GraphManagement.canvasWidth/2, GraphManagement.canvasHeight/2));
        }
        //draw convex polygon
        for (int i = 0; i < convexPoints.size(); i++) {
            drawLine(convexPoints.get(i), convexPoints.get((i + 1) % convexPoints.size()), gc, GraphManagement.convexColor);
        }
        //draw all points
        for (int i = 0; i < points.size(); i++) {
            drawPoint(points.get(i), GraphManagement.pointRadius, gc, GraphManagement.pointColor);
        }
        if(GraphManagement.markPointIndex != -1){
            Point point = points.get(GraphManagement.markPointIndex);
            drawPoint(point, GraphManagement.pointRadius + 1, gc, GraphManagement.pointColor);
            drawPoint(point, GraphManagement.pointRadius, gc, GraphManagement.convexColor);
        }
        double area = 0;
        if (Convex.convexPoints.size() >= 2) {
            for (int i = 0, j = 1; i < Convex.convexPoints.size(); i++, j = (j + 1) % convexPoints.size()) {
                area += (Convex.convexPoints.get(i).getY() - Convex.convexPoints.get(j).getY()) * (Convex.convexPoints.get(j).getX() + Convex.convexPoints.get(i).getX());
            }
        }
        area = Math.abs(area) / 2;
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        gc.strokeText("Convex Polygon Area: " + area, 10, 20);
    }
    
    void resetScreen(){
        GraphManagement.reCalZoom = true;
        GraphManagement.moveX = 0;
        GraphManagement.moveY = 0;
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

    void setOnMouseDragEntered() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
