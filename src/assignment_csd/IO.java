/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_csd;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author TTNhan
 */
public class IO {

    /**
     * get input from console and repeat until valid
     *
     * @param msg the message before input
     * @return Integer type
     */
    public static int getInteger(String msg) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(msg);
                String s = sc.nextLine();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("You must enter an integer!");
            }
        }
    }

    /**
     * get input from console and repeat until valid
     *
     * @param msg the message before input
     * @return Double type
     */
    public static double getDouble(String msg) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(msg);
                String s = sc.nextLine();
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("You must enter a number!");
            }
        }
    }

    /**
     * get String from console
     *
     * @param msg the message before input
     * @return String type
     */
    public static String getString(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        return s;
    }

    public static void out(double[] a) {
        for(double i: a){
            System.out.print(i + " ");
        }
        System.out.println();
    }
    
    public static void out(Object msg) {
        System.out.println(msg);
    }

    /**
     * get String of size character of ch
     */
    private static String multipleChar(char ch, int size) {
        String ans = "";
        while (size > 0) {
            size--;
            ans += ch;
        }
        return ans;
    }

    /**
     * fit String s in center of String length of size by adding some ' '
     */
    private static String fit(String s, int size) {
        return multipleChar(' ', (size - s.length()) / 2) + s + multipleChar(' ', (size - s.length() + 1) / 2);
    }

    /**
     * Show a line in table like: +---+-----+ sizeList[i] is the size of each
     * space
     */
    private static void showLine(int[] sizeList) {
        for (int i = 0; i < sizeList.length; i++) {
            System.out.print("+");
            System.out.print(multipleChar('-', sizeList[i] + 2));
        }
        System.out.println("+");
    }

    /**
     * print table to screen by list of object and list of field name
     *
     * @param <E>
     * @param os list of object
     * @param nameList list of field that want to show
     */
    public static <E> void showTable(LinkedList<E> os, String nameList[]) {
        try {

            Class<?> oClass; //class of E

            //initial width of each cell
            int[] sizeList = new int[nameList.length];
            for (int i = 0; i < nameList.length; i++) {
                sizeList[i] = nameList[i].length();
            }

            //get class of Element
            os.add((E) new Object());
            oClass = os.get(0).getClass();
            os.remove(os.size() - 1);

            //get all "get" method
//            for (Method method : oClass.getMethods()) {
//                if (method.getName().startsWith("get")) {
//                    String methodName = method.getName();
//                    if(methodName.equals("getClass")) continue;
//                    System.out.println(methodName);
//                    nameList.add(methodName);
//                    sizeList.add(methodName.length() - 3);
//                }
//            }
            //get max width of cell and store in sizeList
            for (E o : os) {
                for (int i = 1; i < nameList.length; i++) {
                    Method method = oClass.getMethod("get" + nameList[i]);
                    String ans = String.valueOf(method.invoke(o));
                    sizeList[i] = Math.max(sizeList[i], ans.length());
                }
            }

            //show a line
            showLine(sizeList);

            //show header of table
            for (int i = 0; i < sizeList.length; i++) {
                System.out.print("|");
                System.out.print(fit(nameList[i], sizeList[i] + 2));
            }
            System.out.println("|");

            //show a line
            showLine(sizeList);

            //show data of table
            int index = 1;
            for (E o : os) {
                System.out.format("| %" + sizeList[0] + "d |", index++);
                for (int i = 1; i < nameList.length; i++) {
                    Method method = oClass.getMethod("get" + nameList[i]);
                    String ans = String.valueOf(method.invoke(o));
                    System.out.format(" %-" + sizeList[i] + "s |", ans);
                }
                System.out.println();
            }

            //show a line
            showLine(sizeList);

        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.out.println(e);
        }
    }
}
