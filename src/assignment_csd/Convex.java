/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_csd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author TTNhan
 */
public class Convex {

    public static ArrayList<Point> ConvexHull(ArrayList<Point> points) {

        Set<Point> s = new HashSet<>(points);

        ArrayList<Point> pointList = new ArrayList<>(s);

        pointList.sort((Point o1, Point o2) -> {
            double t = o1.getX() - o2.getX();
            if (t == 0) {
                t = o1.getY() - o2.getY();
                return t > 0 ? 1 : (t < 0 ? -1 : 0);
            }
            return t > 0 ? 1 : (t < 0 ? -1 : 0);
        });

        //convexhull
        ArrayList<Point> up = new ArrayList<>();
        ArrayList<Point> down = new ArrayList<>();
        Point p1 = pointList.get(0);
        Point p2 = pointList.get(pointList.size() - 1);
        up.add(p1);
        down.add(p1);

        for (int i = 1; i < pointList.size(); i++) {
            if (i == pointList.size() - 1 || p1.cross(pointList.get(i), p2) < 0) {
                while (up.size() >= 2 && up.get(up.size() - 2).cross(up.get(up.size() - 1), pointList.get(i)) > 0) {
                    up.remove(up.size() - 1);
                }
                up.add(pointList.get(i));
            }
            if (i == pointList.size() - 1 || p1.cross(pointList.get(i), p2) > 0) {
                while (down.size() >= 2 && down.get(down.size() - 2).cross(down.get(down.size() - 1), pointList.get(i)) < 0) {
                    down.remove(down.size() - 1);
                }
                down.add(pointList.get(i));
            }
        }

        ArrayList<Point> convexPoints = new ArrayList<>();
        for (int i = 0; i < up.size(); i++) {
            convexPoints.add(up.get(i));
        }
        for (int i = down.size() - 2; i > 0; i--) {
            convexPoints.add(down.get(i));
        }

        return convexPoints;
    }
}
