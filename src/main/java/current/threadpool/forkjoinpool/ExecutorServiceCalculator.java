package current.threadpool.forkjoinpool;

import sun.nio.ch.ThreadPool;
import sun.plugin2.gluegen.runtime.CPU;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: wulonghuai
 * @Description: 多线程求和
 * @Date: 2020/3/21 5:12 下午
 */
public class ExecutorServiceCalculator implements Calculator {

    ExecutorService pool;
    int parallelism;

    public ExecutorServiceCalculator() {
        // 为了提高线程的性能，按照cpu的核心数设定线程池的核心线程数
        parallelism = Runtime.getRuntime().availableProcessors();
        // 由于本地测试内存肯定够，使用无界的链表存储任务，使用默认的拒绝策略
        pool = new ThreadPoolExecutor(parallelism , parallelism , 0 , TimeUnit.SECONDS , new LinkedBlockingQueue<Runnable>() , new ThreadPoolExecutor.AbortPolicy());
    }

    class SumTask implements Callable<Long> {
        private long[] numbers;
        private int from;
        private int to;
        public SumTask(long[] numbers, int from, int to) {
            this.numbers = numbers;
            this.from = from;
            this.to = to;
        }
        @Override
        public Long call() {
            long total = 0;
            for (int i = from; i <= to; i++) {
                total += numbers[i];
            }
            return total;
        }
    }

    @Override
    public long sumUp(long[] numbers) {
        List<Future<Long>> results = new ArrayList<>();

        // 把任务分解为 n 份，交给 n 个线程处理   4核心 就等分成4份呗
        // 然后把每一份都扔个一个SumTask线程 进行处理
        int part = numbers.length / parallelism;
        for (int i = 0; i < parallelism; i++) {
            int from = i * part; //开始位置
            int to = (i == parallelism - 1) ? numbers.length - 1 : (i + 1) * part - 1; //结束位置

            //扔给线程池计算
            results.add(pool.submit(new SumTask(numbers, from, to)));
        }

        // 把每个线程的结果相加，得到最终结果 get()方法 是阻塞的
        // 优化方案：可以采用CompletableFuture来优化  JDK1.8的新特性
        long total = 0L;
        for (Future<Long> f : results) {
            try {
                total += f.get();
            } catch (Exception ignore) {
            }
        }

        return total;
    }
}
