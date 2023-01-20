/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass_package;

/**
 *
 * @author TTNhan
 */
public class Collision implements Comparable {

    // time from collision to current
    double t;
    Ball a;
    Object b;

    public Collision(double t) {
        this.t = t;
    }

    public Collision(double t, Ball a, Object b) {
        this.t = t;
        this.a = a;
        this.b = b;
    }

    @Override
    public int compareTo(Object o) {
        double tmp = ((Collision) o).t - t;
        return tmp < 0 ? -1 : ((tmp == 0) ? 0 : 1);
    }

}
