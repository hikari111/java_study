package ThreadDemo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: wulonghuai
 * @Description: 一个线程打印偶数，一个线程打印奇数
 * @Date: 2020/3/1 4:23 下午
 */
@Slf4j
public class TakeTurnsPrintNum3 {

    public static int num = 1;
    public static int maxNum = 100;

    public static void main(String[] args) {
//        method1();
        method2();
//        method3();
    }

    @SneakyThrows
    public static void method1() {
        log.info("------------------method1");
        ReentrantLock lock = new ReentrantLock();
        Condition c1 = lock.newCondition();
        Runnable runnable = ()-> {
            lock.lock();
            while (num <= maxNum) {
                log.info((num++) + "");
                c1.signal();
                try {
                    c1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            c1.signal();
            lock.unlock();
        };
        Thread thread1 = new Thread(runnable, "奇数");
        thread1.start();
        Thread thread2 = new Thread(runnable, "偶数");
        thread2.start();
        thread1.join();
        thread2.join();
    }

    @SneakyThrows
    private static void method2() {
        log.info("------------------method2");
        Runnable runnable = () -> {
            synchronized (TakeTurnsPrintNum3.class) {
                while (num <= 100) {
                    log.info((num++) + "");
                    TakeTurnsPrintNum3.class.notify();
                    try {
                        TakeTurnsPrintNum3.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                TakeTurnsPrintNum3.class.notify();
            }
        };
        Thread thread1 = new Thread(runnable, "2奇数");
        thread1.start();
        Thread thread2 = new Thread(runnable, "2偶数");
        thread2.start();
        thread1.join();
        thread2.join();
    }


    @SneakyThrows
    private static void method3() {
        log.info("------------------method3");
        final TakeTurnsPrintNum3 demo = new TakeTurnsPrintNum3();
        Thread thread1 = new Thread(demo::print1, "奇数");
        thread1.start();
        Thread thread2 = new Thread(demo::print2, "偶数");
        thread2.start();
        thread1.join();
        thread2.join();
    }

    public synchronized void print1() {
        for (int i = 1; i <= 100; i += 2) {
            log.info(i + "");
            this.notify();
            try {
                this.wait();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // NO
            }
        }
        this.notify();
    }

    public synchronized void print2() {
        for (int i = 2; i <= maxNum; i += 2) {
            log.info(i + "");
            this.notify();
            try {
                this.wait();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // NO
            }
        }
        this.notify();
    }
}
