/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

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
    
    public void addX(double x){
        this.x += x;
    }
    
    public void addY(double y){
        this.y += y;
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

    public Point sub(Point p) {
        return new Point(x - p.getX(), y - p.getY());
    }
    
    public double distance(Point p){
        Point p1 = this.sub(p);
        return Math.sqrt(p1.dot(p1));
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
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

    public static ArrayList<Point> converToPoints(String[] s) throws Exception {
        if (s.length % 2 == 1) {
            throw new Exception();
        }
        ArrayList<Point> a = new ArrayList<>();
        for (int i = 0, j = 1; j < s.length; i += 2, j += 2) {
            try {
                double u = Double.parseDouble(s[i]);
                double v = Double.parseDouble(s[j]);
                a.add(new Point(u, v));
            } catch (NumberFormatException e) {
                throw e;
            }
        }
        return a;
    }
}
