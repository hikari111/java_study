package ThreadDemo;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: wulonghuai
 * @Description: 一个线程打印偶数，一个线程打印奇数
 * @Date: 2020/3/1 4:15 下午
 */
@Slf4j
public class TakeTurnsPrintNum2 {

    public static volatile boolean flag = true;

    public static volatile int num = 1;

    public static volatile int maxNum = 10;

    public static void main(String[] args) {

        new Thread(()-> {
            while (num <= maxNum) {
                if (flag && num%2!=0) {
                    log.info((num++) + "");
                    flag = false;
                }
            }
        }, "奇数线程").start();

        new Thread(()-> {
            while (num <= maxNum) {
                if (!flag && num%2==0) {
                    log.info((num++) + "");
                    flag = true;
                }
            }
        },"偶数线程").start();
    }
}
