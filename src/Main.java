import java.io.PrintStream;
import java.util.Arrays;
import java.util.concurrent.*;


public class Main {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
	if(args.length==0) {
		System.err.println("Input parameters! use $java Main [player_number]\n ex) java Main 7");
		System.exit(1);
	}

        int player_number = Integer.parseInt(args[0]);

        Horse[] horses;
        Thread[] threads;
        CyclicBarrier cyclicBarrier;
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(player_number);
        CountDownLatch countDownLatch = new CountDownLatch(player_number);

        horses = new Horse[player_number];
        threads = new Thread[player_number];
        cyclicBarrier = new CyclicBarrier(player_number+1);

        for (int i = 0; i < player_number; i++) {
            horses[i] = new Horse(i, queue, countDownLatch);
            Horse horse = horses[i];
            threads[i] = new Thread() {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    super.run();
                    horse.work();
                }
            };
            threads[i].start();
        }
        cyclicBarrier.await();

        while(true) {
            Thread.sleep(100);
            for(int i=0;i<100*player_number;i++) {
                System.out.println("\b");
            }

            int []progresses = new int[player_number];

            for (Horse horse:
                 horses) {
                System.out.print(horse.getNumber()+": ");
                int progress = horse.getProgress();
                for(int i=0;i<progress;i++) {
                    System.out.print("=");
                }
                System.out.print(">");
		for(int i=0;i<78-progress;i++) {
                	System.out.print(" ");
		}	
                System.out.println("|");
            }


           if (0==countDownLatch.getCount()) break;
        }

        countDownLatch.await();

	for(int i=0;!queue.isEmpty();i++) {
		System.out.println((i+1)+"place: "+queue.poll());
	}
    }
}
