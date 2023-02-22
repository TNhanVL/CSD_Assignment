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
public class Hole extends Circle {

    public Hole(double d, double d1, double d2, Paint paint) {
        super(d, d1, d2, paint);
    }

    public Point toPoint() {
        return new Point(this.getCenterX(), this.getCenterY());
    }
}
