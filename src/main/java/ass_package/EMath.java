/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass_package;

/**
 *
 * @author TTNhan
 */
public class EMath {

    static double INF = 1000.0;
    static double error = 1e-7;

    private static final double TWO_PI = 2.0 * Math.PI;
    private static final double FOUR_PI = 4.0 * Math.PI;

    //chose minimum number that greater than or equal to 0
    static double optimalTimeOfIntersect(double a, double b) {
        if (a < 0 || Double.isNaN(a)) {
            return b;
        }
        if (b < 0 || Double.isNaN(b)) {
            return a;
        }
        return Math.min(a, b);
    }

    static double optimalTimeOfIntersect(double[] a) {
        double ans = INF;
        for (double i : a) {
            ans = optimalTimeOfIntersect(ans, i);
        }
        return ans;
    }

    static double[] Equation2(double a, double b, double c) {

        double d = b * b - 4 * a * c;
        if (Math.abs(a) < error) {
            return new double[]{-c / b};
        }
        if (d < 0) {
            return new double[0];
        }
        d = Math.sqrt(d);

        return new double[]{(-b - d) / (2 * a), (-b + d) / (2 * a)};
    }

    //solve ax^3 + bx^2 + cx + d == 0
    //return inf (1000 second because not intersect) if not found 
    static double[] Equation3(double a, double b, double c, double d) {
        if (Math.abs(a) < error) {
            return Equation2(b, c, d);
        }

        // Normalize coefficients.
        double denom = a;
        a = b / denom;
        b = c / denom;
        c = d / denom;

        // Commence solution.
        double a_over_3 = a / 3.0;
        double Q = (3 * b - a * a) / 9.0;
        double Q_CUBE = Q * Q * Q;
        double R = (9 * a * b - 27 * c - 2 * a * a * a) / 54.0;
        double R_SQR = R * R;
        double D = Q_CUBE + R_SQR;

        double x1, x2, x3;

        if (D < 0.0) {
            // Three unequal real roots.
            double theta = Math.acos(R / Math.sqrt(-Q_CUBE));
            double SQRT_Q = Math.sqrt(-Q);
            x1 = 2.0 * SQRT_Q * Math.cos(theta / 3.0) - a_over_3;
            x2 = 2.0 * SQRT_Q * Math.cos((theta + TWO_PI) / 3.0) - a_over_3;
            x3 = 2.0 * SQRT_Q * Math.cos((theta + FOUR_PI) / 3.0) - a_over_3;
        } else if (D > 0.0) {
            // One real root.
            double SQRT_D = Math.sqrt(D);
            double S = Math.cbrt(R + SQRT_D);
            double T = Math.cbrt(R - SQRT_D);
            x1 = (S + T) - a_over_3;
            x2 = Double.NaN;
            x3 = Double.NaN;
        } else {
            // Three real roots, at least two equal.
            double CBRT_R = Math.cbrt(R);
            x1 = 2 * CBRT_R - a_over_3;
            x2 = x3 = CBRT_R - a_over_3;
        }
        return new double[]{x1, x2, x3};
    }

    //solve ax^4 + bx^3 + cx^2 + dx + e == 0
    //return inf (1000 second because not intersect) if not found 
    static double[] Equation4(double a, double b, double c, double d, double e) {

        if (Math.abs(a) < error) {
            return Equation3(b, c, d, e);
        }
        //https://en.wikipedia.org/wiki/Cubic_equation#General_cubic_formula
        double x1 = 0, x2 = 0, x3, x4;

        //calculate
        double p = (8 * a * c - 3 * b * b) / (8 * a * a);
        double q = (b * b * b - 4 * a * b * c + 8 * a * a * d) / (8 * a * a * a);

        double d0 = c * c - 3 * b * d + 12 * a * e;
        double d1 = 2 * c * c * c - 9 * b * c * d + 27 * b * b * e + 27 * a * d * d - 72 * a * c * e;
        double delta = (d1 * d1 - 4 * d0 * d0 * d0) / -27;

        double S = 0, Q = 0;

        boolean calQ = false;
        boolean calS = false;

        //special case 1: delta > 0
        if (delta > error) {
            double phi = Math.acos(d1 / (2 * Math.sqrt(d0 * d0 * d0)));
            double tmp = 2 * (-p + Math.sqrt(d0) * Math.cos(phi / 3) / a) / 3;

            //not found root
            if (tmp < 0) {
                return new double[0];
            }
            S = 0.5 * Math.sqrt(tmp);
            calS = true;

            //pre cal i = x1
            double i = -b / (4 * a) - S - 0.5 * Math.sqrt(-4 * S * S - 2 * p + q / S);
            double cal = i * i * i * i * a + i * i * i * b + i * i * c + i * d + e;

            //all root are complex
            if (Math.abs(cal) > error) {
                return new double[0];
            }
        }

        //special case 2: delta != 0 and d0 == 0
        if (Math.abs(delta) > error && Math.abs(d0) < error) {
            Q = Math.cbrt(d1);
            calQ = true;
        }

        //special case 4: delta == 0 and d0 == 0
        if (Math.abs(delta) < error && Math.abs(d0) < error) {
            double[] x0 = Equation2(a * 6, b * 3, c);
            double tmp = 1e9;
            for (double i : x0) {
                double cal = i * i * i * i * a + i * i * i * b + i * i * c + i * d + e;
                if (Math.abs(cal) < tmp) {
                    x1 = i;
                    tmp = Math.abs(cal);
                }
            }

            x0 = new double[]{-b / a - 3 * x1, (-b / a - x1) / 3};

            tmp = 1e9;
            for (double i : x0) {
                double cal = i * i * i * i * a + i * i * i * b + i * i * c + i * d + e;
                if (Math.abs(cal) < tmp) {
                    x2 = i;
                    tmp = Math.abs(cal);
                }
            }

            //found two root
            return new double[]{x1, x2};
        }

        //cal Q
        if (!calQ) {
            Q = Math.cbrt((d1 + Math.sqrt(delta * -27)) / 2);
        }
        //special case 3: S == 0
        if (Math.abs((-2 * p + (Q + d0 / Q) / a) / 3) < error) {
            if (Math.abs(b) < error && Math.abs(d) < error) {
                double[] x0 = Equation2(a, c, e);
                x1 = Math.sqrt(x0[0]);
                x2 = -Math.sqrt(x0[0]);
                x3 = Math.sqrt(x0[1]);
                x4 = -Math.sqrt(x0[1]);
                return new double[]{x1, x2, x3, x4};
            } else {
                b /= a;
                c /= a;
                d /= a;
                e /= a;
                p = (8 * c - 3 * b * b) / 8;
                q = (b * b * b - 4 * b * c + 8 * d) / 8;
                double r = (-3 * b * b * b * b + 256 * e - 64 * b * d + 16 * b * b * c) / 256;

//                IO.out(p + " " + q + " " + r);
                double x0[] = Equation4(1, 0, p, q, r);

                for (int i = 0; i < x0.length; i++) {
                    x0[i] -= b / 4;
                }

                return x0;
            }
        }

        //cal S
        if (!calS) {
            S = 0.5 * Math.sqrt((-2 * p + (Q + d0 / Q) / a) / 3);
        }

//        IO.out("delta: " + delta);
//        IO.out("delta0: " + d0);
//        IO.out("delta1: " + d1);
//        IO.out("pre S: " + (-2 * p + (Q + d0 / Q) / a) / 3);
//        IO.out("S: " + S);
//        IO.out("Q: " + Q);
        x1 = -b / (4 * a) - S - 0.5 * Math.sqrt(-4 * S * S - 2 * p + q / S);
        x2 = -b / (4 * a) - S + 0.5 * Math.sqrt(-4 * S * S - 2 * p + q / S);
        x3 = -b / (4 * a) + S - 0.5 * Math.sqrt(-4 * S * S - 2 * p - q / S);
        x4 = -b / (4 * a) + S + 0.5 * Math.sqrt(-4 * S * S - 2 * p - q / S);

        return new double[]{x1, x2, x3, x4};
    }
}
