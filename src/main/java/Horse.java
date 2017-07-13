import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lyuha on 5/14/16.
 */
public class Horse{
    int number;
    int progress =0;
    final ArrayBlockingQueue<Integer> queue;
    final CountDownLatch countDownLatch;

    public Horse(int number, ArrayBlockingQueue<Integer> queue, CountDownLatch countDownLatch) {
        this.number = number;
        this.queue = queue;
        this.countDownLatch = countDownLatch;
    }

    public void work() {
        for(long j=0; j<78;j++) {
            for (long i = 0; i < 300000000L;) {
                i++;
            }
            progress++;
        }
        queue.add(this.number);
        countDownLatch.countDown();
    }

    public int getProgress() {
        return this.progress;
    }

    public int getNumber() {
        return this.number;
    }
}
