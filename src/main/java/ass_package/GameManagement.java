/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass_package;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public double distance(Point p, Line l) {
        Point vector = new Point(l.getStartX() - l.getEndX(), l.getStartY() - l.getEndY());
        double a = vector.getY();
        double b = -vector.getX();
        double c = -(a * l.getStartX() + b * l.getStartY());
        return Math.abs(a * p.getX() + b * p.getY() + c) / Math.sqrt(a * a + b * b);
    }

    //calculate time until ball and line intersect
    double timeToIntersect(Ball ball, Line line) {
        Point vector = new Point(line.getStartX() - line.getEndX(), line.getStartY() - line.getEndY());
        //line: ax + by + c = 0
        double a = vector.getY();
        double b = -vector.getX();
        double c = -(a * line.getStartX() + b * line.getStartY());

        //calculate when distance from ball to line exact to ball.radius
        //r = (ax + by + c) / sqrt(a^2 + b^2) => calculate t1
        double t1 = (ball.getRadius() * Math.sqrt(a * a + b * b) - c - a * ball.getCenterX() - b * ball.getCenterY()) / (a * ball.v.getX() + b * ball.v.getY());
        //r = -(ax + by + c) / sqrt(a^2 + b^2) => calculate t2
        double t2 = (-ball.getRadius() * Math.sqrt(a * a + b * b) - c - a * ball.getCenterX() - b * ball.getCenterY()) / (a * ball.v.getX() + b * ball.v.getY());
        return Math.min(t1, t2);
    }

    //check from previous time to now if ball will intersect with line
    boolean checkIntersect(Ball ball, Line line, long now) {
        double t = timeToIntersect(ball, line);
        return t >= 0 && (t < (now - ball.pret) / 1e9);
    }

    //check from previous time to now if ball will intersect with line
    boolean checkIntersect(Ball ballA, Ball ballB, long now) {
        return true;
    }

    void reflectBall(Ball ball, Line line) {
        long t = (long) (timeToIntersect(ball, line) * 1e9);
        ball.move(ball.pret + t);
        double l = distance(ball.toPoint(), line);
        Point n = new Point(line.getStartX() - line.getEndX(), line.getStartY() - line.getEndY()).rotate().unit();
        ball.v = ball.v.sub(n.mul(n.dot(ball.v)).mul(2));
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setScene(scene);
        primaryStage.show();

        Ball ballA = new Ball(30, 120, 30, Color.BLACK, System.nanoTime());
        ballA.v = new Point(1000, 700);
        Ball ballB = new Ball(1000, 700, 30, Color.BLACK, System.nanoTime());
        root.getChildren().add(ballA);
        root.getChildren().add(ballB);

        ArrayList<Line> lines = new ArrayList<>();
        lines.add(new Line(10, 10, scene.getWidth() - 10, 10));
        lines.add(new Line(10, 10, 10, scene.getHeight() - 10));
        lines.add(new Line(scene.getWidth() - 10, 10, scene.getWidth() - 10, scene.getHeight() - 10));
        lines.add(new Line(10, scene.getHeight() - 10, scene.getWidth() - 10, scene.getHeight() - 10));
        for (Line line : lines) {
            root.getChildren().add(line);
        }

        AnimationTimer t = new AnimationTimer() {
            PriorityQueue<Collision> queue = new PriorityQueue<>();

            @Override
            public void handle(long now) {

                //check collision with line
                for (Line line : lines) {
                    if (checkIntersect(ballA, line, now)) {
                        queue.add(new Collision(timeToIntersect(ballA, line), ballA, line));
                    }
                }

                while (!queue.isEmpty()) {
                    Collision cl = queue.poll();
                    if (!checkIntersect(cl.a, (Line) cl.b, now)) {
                        continue;
                    }
                    reflectBall(cl.a, (Line) cl.b);
                }

                IO.out(ballA.v);
                ballA.move(now);
                if (ballA.stand()) {
                    this.stop();
                }
            }
        };
        t.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
