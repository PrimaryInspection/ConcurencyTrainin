package Producer_Consumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Producer/Consumer pattern using Locks and Conditions
 * */
@SuppressWarnings("all")
public class LockCondition {
    public static void main(String[] args) {
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(10);

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

class MyBlockingQueue<E>{
    private int max;
    private Queue<E> queue;
    private ReentrantLock lock = new ReentrantLock(true);
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public MyBlockingQueue(int size){
        queue = new LinkedList<>();
        this.max = size;
    }

    public void put(E e) {
        lock.lock();
        try {
            while (queue.size() == max) {
                notFull.await();
            }
            queue.add(e);
            System.out.println("Added element: " + e);
            notEmpty.signalAll();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

        public E take() throws InterruptedException {
            lock.lock();

            try {
                while(queue.size() == 0) {
                    notEmpty.await();
                }
                E item = queue.remove();
                System.out.println("removed  element: " + item);
                notFull.signalAll();
                return item;
            } finally {
                lock.unlock();
            }
        }
}
