/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ass_package;

import java.util.PriorityQueue;

/**
 *
 * @author TTNhan
 */
public class Test {
    public static void main(String[] args) {
        PriorityQueue<Collision> queue = new PriorityQueue<>();
        queue.add(new Collision(1.3456345634));
        queue.add(new Collision(1.83456345634));
        queue.add(new Collision(1.1345324254));
        while(!queue.isEmpty()){
            IO.out(queue.poll().t);
        }
    }
}
