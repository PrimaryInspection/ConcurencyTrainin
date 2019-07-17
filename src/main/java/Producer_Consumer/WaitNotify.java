package Producer_Consumer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Producer/Consumer pattern using wait/notify
 * */

@SuppressWarnings("all")
public class WaitNotify {
    public static void main(String[] args) {
        MyBlockingQueueWN<Integer> queue = new MyBlockingQueueWN<>(10);

        final Runnable producer = () -> {
            while (true){
                try {
                    int number=10;
                    int randomNumber= number +(int) (Math.random() * 20);
                    Thread.sleep(1000);
                    queue.put(Integer.valueOf(randomNumber));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        final Runnable consumer = () -> {
            while (true){
                try {
                    Thread.sleep(1000);
                    int i = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(producer).start();
        new Thread(consumer).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class MyBlockingQueueWN<E>{
    private int max;
    private volatile Queue<E> queue;
    private Object notEmpty = new Object();
    private Object notFull = new Object();

    public MyBlockingQueueWN(int size){
        queue = new LinkedList<>();
        this.max = size;
    }

    public void put(E e) throws InterruptedException {

        synchronized (queue) {
            while (queue.size() == max) {
                queue.wait();
            }
        }
            queue.add(e);
            System.out.println("Added element: " + e);
            synchronized (queue) {
                queue.notifyAll();
            }
        }

    public E take() throws InterruptedException {

            synchronized (queue) {
                while (queue.size() == 0) {
                    queue.wait();
                }
            }
            E item = queue.remove();
            System.out.println("removed  element: " + item);
            synchronized (queue) {
                queue.notify();
            }
            return item;
        }
    }
