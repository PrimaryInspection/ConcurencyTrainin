package Producer_Consumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Producer/Consumer pattern using BlockingQueue
 * */

@SuppressWarnings("all")
public class BlockingQueueCP {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);

        //Producer
      final Runnable producer = () -> {
            while (true){
                int number=10;
                int randomNumber= number +(int) (Math.random() * 20);
                try {
                    Thread.sleep(1000);
                    queue.put(Integer.valueOf(randomNumber));
                    System.out.println("PUT: " + randomNumber);
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
                    System.out.println("TAKE: " + i);
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
