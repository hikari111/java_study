package ThreadDemo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: wulonghuai
 * @Description: 线程一 1 2 3 线程二 4 5 6 线程一 7 8 9 ...
 * @Date: 2020/3/1 4:02 下午
 */
@Slf4j
public class TakeTurnsPrintNum1 {

    // 使用volatile关键字变量
    static volatile boolean flag = true;
    // 使用无锁的原子Integer
    static AtomicInteger i = new AtomicInteger(1);

    public static void main(String[] args) {
        // 123，线程
        new Thread(()-> {
            while (i.get() <= 100) {
                if (flag) {
                    for (int j = 1; j <= 3; j++) {
                        int curValue = i.getAndIncrement();
                        if (curValue <= 100)
                            log.info(Thread.currentThread().getName() + ": " + curValue);
                    }
                    flag = false;
                }
            }
        }).start();

        // 456， 线程
        new Thread(()-> {
            while (i.get() <= 100) {
                if (!flag) {
                    for (int j = 1; j <= 3; j++) {
                        int curValue = i.getAndIncrement();
                        if (curValue <= 100)
                           log.info(Thread.currentThread().getName() + ": " + curValue);
                    }
                    flag = true;
                }
            }
        }).start();
    }
}

