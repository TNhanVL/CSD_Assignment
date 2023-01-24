/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass_package;

import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author TTNhan
 */
public class Test {

    public static void main(String[] args) {
        Random rand = new Random();
        int mod = 100;

        while (true) {
            double a = rand.nextInt() % mod;
            double b = rand.nextInt() % mod;
            double c = rand.nextInt() % mod;
            double d = rand.nextInt() % mod;
            double e = rand.nextInt() % mod;
//            double[] arr = EMath.Equation4(a, b, c, d, e);
            double[] arr = EMath.Equation4(40000.0, -498539.85599999997, 1580187.4251281293, -167010.85176, 889.0);
            for (double i : arr) {
                IO.out(i);
            }
            IO.out(a + " " + b + " " + c + " " + d + " " + e);
            break;
        }
    }
}
