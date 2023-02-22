/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_csd;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author TTNhan
 */
public class GameManagement extends Application {

    double orgSceneX, orgSceneY;
    static double friction = -400;
    static double ballRadius = 30;
    Stage graphStage;

    boolean debug = false;

    ArrayList<Ball> balls = new ArrayList<>();
    ArrayList<Line> lines = new ArrayList<>();
    ArrayList<Hole> holes = new ArrayList<>();

    public GameManagement() {
    }

    public GameManagement(Stage graphStage) {
        this.graphStage = graphStage;
    }

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

        //calculate when distance from ballA to line exact to ballA.radius by equation degree 2
        //r = (ax + by + c) / sqrt(a^2 + b^2) => calculate t1
        //with x = x_0 + (v_x)t + 0.5(a_x)t;
        double A = 0.5 * (a * ball.getAccelerate().getX() + b * ball.getAccelerate().getY());
        A = 0;
        double B = a * ball.getV().getX() + b * ball.getV().getY();
        double C1 = -(ball.getRadius() * Math.sqrt(a * a + b * b) - c - a * ball.getCenterX() - b * ball.getCenterY());
        double C2 = -(-ball.getRadius() * Math.sqrt(a * a + b * b) - c - a * ball.getCenterX() - b * ball.getCenterY());

        double t1 = EMath.optimalTimeOfIntersect(EMath.Equation2(A, B, C1));
        //r = -(ax + by + c) / sqrt(a^2 + b^2) => calculate t2
        double t2 = EMath.optimalTimeOfIntersect(EMath.Equation2(A, B, C2));

        return EMath.optimalTimeOfIntersect(t1, t2);
    }

    //calculate time until ballA and ballB intersect with accelerate
//    double timeToIntersect(Ball ballA, Ball ballB) {
//        long pret = Math.max(ballA.pret, ballB.pret);
//        Ball A = new Ball(ballA);
//        Ball B = new Ball(ballB);
//
//        //move two ballA for same time
//        A.move(pret);
//        B.move(pret);
//
//        //get Accelerate
//        double a_xA = A.getAccelerate().getX();
//        double a_yA = A.getAccelerate().getY();
//        double a_xB = B.getAccelerate().getX();
//        double a_yB = B.getAccelerate().getY();
//
//        //get velocity
//        double v_xA = A.getV().getX();
//        double v_yA = A.getV().getY();
//        double v_xB = B.getV().getX();
//        double v_yB = B.getV().getY();
//
//        //get position
//        double x_0A = A.getCenterX();
//        double y_0A = A.getCenterY();
//        double x_0B = B.getCenterX();
//        double y_0B = B.getCenterY();
//
//        //cal radius
//        double r = A.getRadius() + B.getRadius();
//
//        //a*t^4 + b*t^3 + c*t^2 + d*t + e == 0        
//        double a = 0.25 * a_xA * a_xA
//                - 0.5 * a_xA * a_xB
//                + 0.25 * a_xB * a_xB
//                + 0.25 * a_yA * a_yA
//                - 0.5 * a_yA * a_yB
//                + 0.25 * a_yB * a_yB;
//
//        double b = 1 * a_xA * v_xA
//                - 1 * a_xA * v_xB
//                - 1 * a_xB * v_xA
//                + 1 * a_xB * v_xB
//                + 1 * a_yA * v_yA
//                - 1 * a_yA * v_yB
//                - 1 * a_yB * v_yA
//                + 1 * a_yB * v_yB;
//
//        double c = 1 * a_xA * x_0A
//                - 1 * a_xA * x_0B
//                - 1 * a_xB * x_0A
//                + 1 * a_xB * x_0B
//                + 1 * a_yA * y_0A
//                - 1 * a_yA * y_0B
//                - 1 * a_yB * y_0A
//                + 1 * a_yB * y_0B
//                + 1 * v_xA * v_xA
//                - 2 * v_xA * v_xB
//                + 1 * v_xB * v_xB
//                + 1 * v_yA * v_yA
//                - 2 * v_yA * v_yB
//                + 1 * v_yB * v_yB;
//
//        double d = 2 * v_xA * x_0A
//                - 2 * v_xA * x_0B
//                - 2 * v_xB * x_0A
//                + 2 * v_xB * x_0B
//                + 2 * v_yA * y_0A
//                - 2 * v_yA * y_0B
//                - 2 * v_yB * y_0A
//                + 2 * v_yB * y_0B;
//
//        double e = 1 * x_0A * x_0A
//                - 2 * x_0A * x_0B
//                + 1 * x_0B * x_0B
//                + 1 * y_0A * y_0A
//                - 2 * y_0A * y_0B
//                + 1 * y_0B * y_0B
//                - r * r;
//
////        IO.out(EMath.Equation4(a, b, c, d, e));
////        if (A.toPoint().distance(B.toPoint()) < 70) {
////            IO.out(EMath.Equation4(a, b, c, d, e));
////            IO.out(EMath.optimalTimeOfIntersect(EMath.Equation4(a, b, c, d, e)));
////            IO.out(ballA.getV() + " " + ballB.getV());
////        }
////        IO.out(a + " " + b + " " + c + " " + d + " " + e);
////        double i = EMath.optimalTimeOfIntersect(EMath.Equation4(a, b, c, d, e));
////        IO.out(Math.sqrt(i * i * i * i * a + i * i * i * b + i * i * c + i * d + e + r * r));
////        IO.out("pos: " + (y_0B + v_yB*i + 0.5*a_yB*i*i));
//        return EMath.optimalTimeOfIntersect(EMath.Equation4(a, b, c, d, e));
//    }
    //calculate time until ballA and ballB intersect with accelerate
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
        Point v = B.getV().sub(A.getV());

        //vx^2 + vy^2
        double vxvy = v.getX() * v.getX() + v.getY() * v.getY();

        double t1 = (-(cx * v.getX() + cy * v.getY()) - Math.sqrt(r * r * vxvy - Math.pow((cx * v.getY() - cy * v.getX()), 2))) / vxvy;
        double t2 = (-(cx * v.getX() + cy * v.getY()) + Math.sqrt(r * r * vxvy - Math.pow((cx * v.getY() - cy * v.getX()), 2))) / vxvy;

        return Math.min(t1, t2);
    }

    //check from previous time to now if ballA will intersect with line
    double checkIntersect(Ball ball, Line line, long now) {
        if (!ball.visibleProperty().get()) {
            return -1;
        }
        double t = timeToIntersect(ball, line);
        if (t >= 0 && (t * 1e9 <= (now - ball.pret))) {
            return t;
        }
        return -1;
    }

    //check from previous time to now if ballA will intersect with line
    double checkIntersect(Ball ballA, Ball ballB, long now) {
        if (!ballA.visibleProperty().get() || !ballB.visibleProperty().get()) {
            return -1;
        }
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
        ball.v = ball.getV().sub(n.mul(n.dot(ball.getV())).mul(2));
    }

    //reflect ballA when collision a ball
    void reflectBall(Ball ballA, Ball ballB, double t) {
        //move two ballA for same time
        double pret = Math.max(ballA.pret, ballB.pret);
        if (t > pret) {
            t--;
        }
        ballA.move((long) t);
        ballB.move((long) t);

        //debug
//        if (ballA.toPoint().distance(ballB.toPoint()) < 60) {
//            debug = true;
//            IO.out(ballA.pre);
//            IO.out(ballA.getV());
//            IO.out(ballB.pre);
//            IO.out(ballB.getV());
//            IO.out(ballA.pre.distance(ballB.pre));
//        }
        //get two center of two ballA
        Point A = ballA.toPoint();
        Point B = ballB.toPoint();

        //vector AB
        Point AB = B.sub(A).unit();
        Point BA = A.sub(B).unit();

        //velocity
        Point vB = AB.mul(ballA.getV().dot(AB));
        Point vA = BA.mul(ballB.getV().dot(BA));

        Point vtA = ballA.getV().sub(vB);
        Point vtB = ballB.getV().sub(vA);

        ballA.v = vA.add(vtA);
        ballB.v = vB.add(vtB);
    }

    void startGameTimer() {
        AnimationTimer t = new AnimationTimer() {
            PriorityQueue<Collision> queue = new PriorityQueue<>();

            @Override
            public void handle(long now) {

                double tmp = -1;

                //for each ballA
                for (Ball ball : balls) {
                    if (!ball.visibleProperty().get()) {
                        continue;
                    }

                    boolean hide = false;
                    if (ball.fillProperty().getValue() != Color.BLACK) {
                        for (Hole hole : holes) {
                            if (ball.toPoint().distance(hole.toPoint()) < 35) {
                                ball.setVisible(false);
                                hide = true;
                                break;
                            }
                        }
                    }
                    if (hide) {
                        continue;
                    }

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
//                    IO.out("");
//                    if (cl.b instanceof Ball) {
//                        IO.out("Ball reflect");
//                    } else {
//                        IO.out("Line reflect");
//                    }
                    if (cl.b instanceof Line) {
                        tmp = checkIntersect(cl.a, (Line) cl.b, now);
                        if (tmp < 0 || (tmp >= 0 && Math.abs(Math.round(tmp * 1e9) - Math.round(now - cl.t - cl.a.pret)) > 1)) {
                            continue;
                        }
                        reflectBall(cl.a, (Line) cl.b, now - cl.t);

                    } else if (cl.b instanceof Ball) {
                        tmp = checkIntersect(cl.a, (Ball) cl.b, now);
                        if (tmp < 0 || (tmp >= 0 && Math.abs(Math.round(tmp * 1e9) - Math.round(now - cl.t - Math.max(cl.a.pret, ((Ball) cl.b).pret))) > 1)) {
//                            IO.out("no");
                            continue;
                        }
//                        IO.out(now - cl.t - cl.a.pret);
//                        IO.out(cl.a);
//                        IO.out(cl.b);
//                        IO.out(cl.a.getV());
//                        IO.out(((Ball) cl.b).getV());
//                        IO.out("distance: " + cl.a.toPoint().distance(((Ball)cl.b).toPoint()));
                        reflectBall(cl.a, (Ball) cl.b, now - cl.t);
//                        IO.out(cl.a);
//                        IO.out(cl.b);
//                        IO.out(cl.a.getV());
//                        IO.out(((Ball) cl.b).getV());
//                        IO.out("distance: " + cl.a.toPoint().distance(((Ball)cl.b).toPoint()));
//                        IO.out("");
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

                if (debug) {
                    this.stop();
                }
//                this.stop();
            }
        };
        t.start();
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Magic");
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            if (graphStage != null) {
                graphStage.show();
            }
        });

        long now = System.nanoTime();

        balls.add(new Ball(90, 90, ballRadius, Color.BLACK, now));
        balls.get(0).v = new Point(0.12289017114034095, -1.5893042145205207);

//        IO.out(balls.get(0).toPoint().distance(balls.get(1).toPoint()));
//        IO.out(timeToIntersect(balls.get(0), balls.get(1)));
//        IO.out(checkIntersect(balls.get(0), balls.get(1), (long) now + 16666666));
        for (int i = 1; i <= 8; i++) {
            Ball a = new Ball(41 + i * 61, 550 - i * 61, ballRadius, Color.RED, System.nanoTime());
//            a.v = new Point(i * 200, i * 200);
            balls.add(a);
        }

        for (Ball ball : balls) {
            root.getChildren().add(ball);
        }

        lines.add(new Line(10, 10, scene.getWidth() - 10, 10));
        lines.add(new Line(10, 10, 10, scene.getHeight() - 10));
        lines.add(new Line(scene.getWidth() - 10, 10, scene.getWidth() - 10, scene.getHeight() - 10));
        lines.add(new Line(10, scene.getHeight() - 10, scene.getWidth() - 10, scene.getHeight() - 10));

        for (Line line : lines) {
            root.getChildren().add(line);
        }

        double padding = 20;
        holes.add(new Hole(padding, padding, 40, Color.AQUA));
        holes.add(new Hole(scene.getWidth() - padding, padding, 40, Color.AQUA));
        holes.add(new Hole(padding, scene.getHeight() - padding, 40, Color.AQUA));
        holes.add(new Hole(scene.getWidth() - padding, scene.getHeight() - padding, 40, Color.AQUA));
        holes.add(new Hole(scene.getWidth() / 2, padding, 40, Color.AQUA));
        holes.add(new Hole(scene.getWidth() / 2, scene.getHeight() - padding, 40, Color.AQUA));

        for (Hole hole : holes) {
            root.getChildren().add(hole);
        }

        scene.setOnMouseClicked((MouseEvent e) -> {
            Point p = new Point(e.getX(), e.getY());
            p = p.sub(balls.get(0).toPoint()).unit().mul(1500);
            balls.get(0).v = p;
        });
        

        startGameTimer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
