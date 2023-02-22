/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_csd;

import java.util.ArrayList;

/**
 *
 * @author TTNhan
 */
public class Point {

    private double x, y;

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
    }

    public Point unit() {
        double l = distance(new Point());
        if (l <= 0) {
            return new Point(1, 1);
        }
        return new Point(x / l, y / l);
    }

    public Point rotate() {
        return new Point(y, -x);
    }

    public Point mul(double m) {
        return new Point(this.x * m, this.y * m);
    }

    public double cross(Point p1, Point p2) {
        return p1.sub(this).cross(p2.sub(this));
    }

    public double cross(Point p) {
        return x * p.getY() - y * p.getX();
    }

    public double dot(Point p) {
        return x * p.getX() + y * p.getY();
    }

    public Point add(Point p) {
        return new Point(x + p.getX(), y + p.getY());
    }

    public Point sub(Point p) {
        return new Point(x - p.getX(), y - p.getY());
    }

    public double distance(Point p) {
        Point p1 = this.sub(p);
        return Math.sqrt(p1.dot(p1));
    }

    @Override
    public Point clone() {
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return "Point{" + x + ", " + y + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (Math.abs(this.x - other.x) > 1e-6) {
            return false;
        }
        return Math.abs(this.y - other.y) < 1e-6;
    }

    public static ArrayList<Point> converToPoints(String[] s) {
        ArrayList<Point> a = new ArrayList<>();
        for (String item : s) {
            try {
                String[] t = item.split(" ");
                double u = Double.parseDouble(t[0]);
                double v = Double.parseDouble(t[1]);
                a.add(new Point(u, v));
            } catch (NumberFormatException e) {
            }
        }
        return a;
    }
}
