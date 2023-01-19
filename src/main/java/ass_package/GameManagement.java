/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass_package;

import java.awt.geom.Point2D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

/**
 *
 * @author TTNhan
 */
public class GameManagement extends Application {

    double orgSceneX, orgSceneY;
    static double friction = -100;

    class Ball extends Circle {

        Point v = new Point();
        double pret = -1;

        public Ball(double d, double d1, double d2, Paint paint) {
            super(d, d1, d2, paint);
        }

        //check it not move
        public boolean stand() {
            return v.distance(new Point()) <= 0.1;
        }

        public void move(long now) {
            if (pret < 0) {
                pret = now;
                return;
            }
            if (stand()) {
                return;
            }
            double t = (now - pret) / 1e9;
            pret = now;

            Point a = v.unit().mul(GameManagement.friction);

            double sx = 0.5 * a.getX() * t * t + t * v.getX();
            double sy = 0.5 * a.getY() * t * t + t * v.getY();
            v.setX(v.getX() + a.getX() * t);
            v.setY(v.getY() + a.getY() * t);

            this.setCenterX(this.getCenterX() + sx);
            this.setCenterY(this.getCenterY() + sy);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 1600, 900);

        Ball ballA = new Ball(30, 120, 30, Color.BLACK);
        ballA.v = new Point(500, 300);
        Ball ballB = new Ball(1000, 700, 30, Color.BLACK);
        Line lineA = new Line(0, 700, 900, 0);
        root.getChildren().add(ballA);
        root.getChildren().add(ballB);
        root.getChildren().add(lineA);

        AnimationTimer t = new AnimationTimer() {
            @Override
            public void handle(long now) {
                ballA.move(now);
            }
        };
        t.start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
