/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass_package;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author TTNhan
 */
public class Ball extends Circle {

    Point v = new Point();
    long pret = -1;

    public Ball(double d, double d1, double d2, Paint paint, long pret) {
        super(d, d1, d2, paint);
        this.pret = pret;

    }

    public Ball(Ball ball) {
        super(ball.getCenterX(), ball.getCenterY(), ball.getRadius(), ball.getFill());
        this.pret = ball.pret;
        this.v = ball.v.clone();
    }

    public Point toPoint() {
        return new Point(this.getCenterX(), this.getCenterY());
    }

    //check it not move
    public boolean stand() {
        return v.distance(new Point()) <= 0.1;
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

        Point a = v.unit().mul(GameManagement.friction);
//        a = a.add(new Point(0, 1000));

        double sx = 0.5 * a.getX() * t * t + t * v.getX();
        double sy = 0.5 * a.getY() * t * t + t * v.getY();
        v.setX(v.getX() + a.getX() * t);
        v.setY(v.getY() + a.getY() * t);

        this.setCenterX(this.getCenterX() + sx);
        this.setCenterY(this.getCenterY() + sy);
    }
}
