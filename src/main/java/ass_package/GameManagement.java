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
    static double friction = 000;

    public double distance(Point p, Line l) {
        Point vector = new Point(l.getStartX() - l.getEndX(), l.getStartY() - l.getEndY());
        double a = vector.getY();
        double b = -vector.getX();
        double c = -(a * l.getStartX() + b * l.getStartY());
        return Math.abs(a * p.getX() + b * p.getY() + c) / Math.sqrt(a * a + b * b);
    }

    //calculate time until ballA and line intersect
    double timeToIntersect(Ball ball, Line line) {
        Point vector = new Point(line.getStartX() - line.getEndX(), line.getStartY() - line.getEndY());
        //line: ax + by + c = 0
        double a = vector.getY();
        double b = -vector.getX();
        double c = -(a * line.getStartX() + b * line.getStartY());

        //calculate when distance from ballA to line exact to ballA.radius
        //r = (ax + by + c) / sqrt(a^2 + b^2) => calculate t1
        double t1 = (ball.getRadius() * Math.sqrt(a * a + b * b) - c - a * ball.getCenterX() - b * ball.getCenterY()) / (a * ball.v.getX() + b * ball.v.getY());
        //r = -(ax + by + c) / sqrt(a^2 + b^2) => calculate t2
        double t2 = (-ball.getRadius() * Math.sqrt(a * a + b * b) - c - a * ball.getCenterX() - b * ball.getCenterY()) / (a * ball.v.getX() + b * ball.v.getY());

        return Math.min(t1, t2);
    }

    //calculate time until ballA and line intersect
    double timeToIntersect(Ball ballA, Ball ballB) {
        long pret = Math.max(ballA.pret, ballB.pret);
        Ball A = new Ball(ballA);
        Ball B = new Ball(ballB);

        //move two ballA for same time
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
    }

    //check from previous time to now if ballA will intersect with line
    double checkIntersect(Ball ball, Line line, long now) {
        double t = timeToIntersect(ball, line);
        if (t >= 0 && (t * 1e9 <= (now - ball.pret))) {
            return t;
        }
        return -1;
    }

    //check from previous time to now if ballA will intersect with line
    double checkIntersect(Ball ballA, Ball ballB, long now) {
        double t = timeToIntersect(ballA, ballB);
        if (t >= 0 && (t * 1e9 <= (now - Math.max(ballA.pret, ballB.pret)))) {
            return t;
        }
        return -1;
    }

    //reflect ballA when collision a line
    void reflectBall(Ball ball, Line line, double t) {
        ball.move((long) t);
        Point n = new Point(line.getStartX() - line.getEndX(), line.getStartY() - line.getEndY()).rotate().unit();
        ball.v = ball.v.sub(n.mul(n.dot(ball.v)).mul(2));
    }

    //reflect ballA when collision a line
    void reflectBall(Ball ballA, Ball ballB, double t) {
        //move two ballA for same time
        ballA.move((long) t);
        ballB.move((long) t);

        //get two center of two ballA
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
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        ArrayList<Ball> balls = new ArrayList<>();
        balls.add(new Ball(41, 120, 30, Color.BLACK, System.nanoTime()));
        balls.get(0).v = new Point(1000, 700);
        balls.add(new Ball(41.12208847936531, 41.241717365108215, 30, Color.RED, System.nanoTime()));
        balls.get(1).v = new Point(-1592.0843773097174, -581.1456462022795);
        for (int i = 1; i <= 10; i++) {
            Ball a = new Ball(41 + i * 31, 550 - i * 31, 30, Color.RED, System.nanoTime());
            a.v = new Point(i * 400, i * 400);
            balls.add(a);
        }
//        balls.get(1).pret = 1508444823700L;

        for (Ball ball : balls) {
            root.getChildren().add(ball);
        }

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

                //for each ballA
                for (Ball ball : balls) {

                    //check collision with line
                    for (Line line : lines) {
                        if ((tmp = checkIntersect(ball, line, now)) >= 0) {
                            queue.add(new Collision(now - tmp * 1e9 - ball.pret, ball, line));
                        }
                    }

                    //check collision with ballA
                    for (Ball ballB : balls) {
                        if (ball.equals(ballB)) {
                            continue;
                        }
                        if ((tmp = checkIntersect(ball, ballB, now)) >= 0) {
                            queue.add(new Collision(now - tmp * 1e9 - Math.max(ball.pret, ballB.pret), ball, ballB));
                        }
                    }
                }

                while (!queue.isEmpty()) {
                    Collision cl = queue.poll();
                    if (cl.b instanceof Line) {
                        tmp = checkIntersect(cl.a, (Line) cl.b, now);
                        if (tmp < 0 || (tmp >= 0 && Math.abs(Math.round(tmp * 1e9) - Math.round(now - cl.t - cl.a.pret)) > 1)) {
                            continue;
                        }
                        reflectBall(cl.a, (Line) cl.b, now - cl.t);

                    } else if (cl.b instanceof Ball) {
                        tmp = checkIntersect(cl.a, (Ball) cl.b, now);
                        if (tmp < 0 || (tmp >= 0 && Math.abs(Math.round(tmp * 1e9) - Math.round(now - cl.t - Math.max(cl.a.pret, ((Ball) cl.b).pret))) > 1)) {
                            continue;
                        }
                        reflectBall(cl.a, (Ball) cl.b, now - cl.t);
                    }

                    //check collision with line
                    for (Line line : lines) {
                        if ((tmp = checkIntersect(cl.a, line, now)) >= 0) {
                            queue.add(new Collision(now - tmp * 1e9 - cl.a.pret, cl.a, line));
                        }
                    }
                    //check collision with another ballA
                    for (Ball ballB : balls) {
                        if (cl.a.equals(ballB)) {
                            continue;
                        }
                        if ((tmp = checkIntersect(cl.a, ballB, now)) >= 0) {
                            queue.add(new Collision(now - tmp * 1e9 - Math.max(cl.a.pret, ballB.pret), cl.a, ballB));
                        }
                    }

                    //check collision with the another ball just reflect
                    if (cl.b instanceof Ball) {
                        Ball ballA = (Ball) cl.b;
                        //check collision with line
                        for (Line line : lines) {
                            if ((tmp = checkIntersect(ballA, line, now)) >= 0) {
                                queue.add(new Collision(now - tmp * 1e9 - ballA.pret, ballA, line));
                            }
                        }
                        for (Ball ballB : balls) {
                            if (ballA.equals(ballB)) {
                                continue;
                            }
                            if ((tmp = checkIntersect(ballA, ballB, now)) >= 0) {
                                queue.add(new Collision(now - tmp * 1e9 - Math.max(ballA.pret, ballB.pret), ballA, ballB));
                            }
                        }
                    }
                }

                //moving all ballA to current time
                for (Ball ball : balls) {
                    ball.move(now);
                }
            }
        };
        t.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
