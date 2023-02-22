/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_csd;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author TTNhan
 */
public class Ball extends Circle {

    Point v = new Point();
    long pret = -1;
    Point pre = new Point();

    public Ball(double d, double d1, double d2, Paint paint, long pret) {
        super(d, d1, d2, paint);
        this.pret = pret;

    }

    public Ball(Ball ball) {
        super(ball.getCenterX(), ball.getCenterY(), ball.getRadius(), ball.getFill());
        this.pret = ball.pret;
        this.v = ball.v.clone();
    }

    public void setCenter(Point p) {
        this.setCenterX(p.getX());
        this.setCenterY(p.getY());
    }

    public Point toPoint() {
        return new Point(this.getCenterX(), this.getCenterY());
    }

    //check it not move
    public boolean stand() {
        return v.distance(new Point()) <= 1;
    }

    public Point getAccelerate() {
        if (stand()) {
            return new Point();
        }
        Point a = v.unit().mul(GameManagement.friction);
//        a.addY(500.0);
        return a;
    }
    
    public Point getV() {
        if (stand()) {
            return new Point();
        }
        return v;
    }

    /**
     * move Ball to current position and update velocity too
     *
     * @param now
     */
    public void move(long now) {
        if (pret < 0 || stand()) {
            pret = now;
            return;
        }
        double t = (now - pret) / 1e9;
        pret = now;
        pre = toPoint();

        Point a = getAccelerate();

        double sx = 0.5 * a.getX() * t * t + t * v.getX();
        double sy = 0.5 * a.getY() * t * t + t * v.getY();
//        double sx = t * v.getX();
//        double sy = t * v.getY();
        v.setX(v.getX() + a.getX() * t);
        v.setY(v.getY() + a.getY() * t);

        this.setCenterX(this.getCenterX() + sx);
        this.setCenterY(this.getCenterY() + sy);
    }
}
