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

    //calculate time until ball and line intersect
    double timeToIntersect(Ball ballA, Ball ballB) {
        try {
            long pret = Math.max(ballA.pret, ballB.pret);
            Ball A = (Ball) ballA.clone();
            Ball B = (Ball) ballB.clone();
            //move two ball for same time
            A.move(pret);
            B.move(pret);

            //Exemple 6: https://www3.ntu.edu.sg/home/ehchua/programming/java/J8a_GameIntro-BouncingBalls.html
            double cx = B.getCenterX() - A.getCenterX();
            double cy = B.getCenterY() - A.getCenterY();
            double r = A.getRadius() + B.getRadius();
            Point v = B.v.sub(A.v);

            //vx^2 + vy^2
            double vxvy = v.getX() * v.getX() + v.getY() * v.getY();

            double t1 = (-(cx * v.getX() + cy * v.getY()) - Math.sqrt(r * r * vxvy - Math.pow((cx * v.getY() - cy * v.getX()), 2))) / vxvy;
            double t2 = (-(cx * v.getX() + cy * v.getY()) + Math.sqrt(r * r * vxvy - Math.pow((cx * v.getY() - cy * v.getX()), 2))) / vxvy;

            return Math.min(t1, t2);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(GameManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    //check from previous time to now if ball will intersect with line
    double checkIntersect(Ball ball, Line line, long now) {
        double t = timeToIntersect(ball, line);
        if (t >= 0 && (t <= (now - ball.pret) / 1e9)) {
            return t;
        }
        return -1;
    }

    //check from previous time to now if ball will intersect with line
    double checkIntersect(Ball ballA, Ball ballB, long now) {
        double t = timeToIntersect(ballA, ballB);
        if (t >= 0 && (t <= (now - Math.max(ballA.pret, ballB.pret)) / 1e9)) {
            return t;
        }
        return -1;
    }

    //reflect ball when collision a line
    void reflectBall(Ball ball, Line line, double t) {
        ball.move((long) t);
        Point n = new Point(line.getStartX() - line.getEndX(), line.getStartY() - line.getEndY()).rotate().unit();
        ball.v = ball.v.sub(n.mul(n.dot(ball.v)).mul(2));
    }

    //reflect ball when collision a line
    void reflectBall(Ball ballA, Ball ballB, double t) {
        //move two ball for same time
        ballA.move((long) t);
        ballB.move((long) t);

        //get two center of two ball
        Point A = ballA.toPoint();
        Point B = ballB.toPoint();

        //vector AB
        Point AB = B.sub(A).unit();
        Point BA = A.sub(B).unit();
        
        //velocity
        Point vB = AB.mul(ballA.v.dot(AB));
        Point vA = BA.mul(ballB.v.dot(BA));

        Point vtA = ballA.v.sub(vB);
        Point vtB = ballB.v.sub(vA);

        ballA.v = vA.add(vtA);
        ballB.v = vB.add(vtB);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setScene(scene);
        primaryStage.show();

        Ball ballA = new Ball(30, 120, 30, Color.BLACK, System.nanoTime());
        ballA.v = new Point(1000, 650);
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

                double tmp = -1;
                //check collision with line
                for (Line line : lines) {
                    if ((tmp = checkIntersect(ballA, line, now)) >= 0) {
                        queue.add(new Collision(now - timeToIntersect(ballA, line) * 1e9 - ballA.pret, ballA, line));
                    }
                }

                if ((tmp = checkIntersect(ballA, ballB, now)) >= 0) {
                    IO.out(timeToIntersect(ballA, ballB));
                    queue.add(new Collision(now - timeToIntersect(ballA, ballB) * 1e9 - Math.max(ballA.pret, ballB.pret), ballA, ballB));
                }

                while (!queue.isEmpty()) {
                    Collision cl = queue.poll();
                    if (cl.b instanceof Line) {
                        if ((tmp = checkIntersect(cl.a, (Line) cl.b, now)) < 0) {
                            continue;
                        }
                        reflectBall(cl.a, (Line) cl.b, now - cl.t);
                    } else if (cl.b instanceof Ball) {
                        if ((tmp = checkIntersect(cl.a, (Ball) cl.b, now)) < 0) {
                            continue;
                        }
                        reflectBall(cl.a, (Ball) cl.b, now - cl.t);
                    }

                    //check collision with line
                    for (Line line : lines) {
                        if ((tmp = checkIntersect(cl.a, line, now)) >= 0) {
                            queue.add(new Collision(now - timeToIntersect(cl.a, line) * 1e9 - cl.a.pret, cl.a, line));
                        }
                    }
                }

                ballA.move(now);
                ballB.move(now);
                IO.out(ballB.toPoint());
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
